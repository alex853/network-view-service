package net.simforge.networkview.map;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.simforge.commons.misc.JavaTime;
import net.simforge.networkview.core.Network;
import net.simforge.networkview.core.Position;
import net.simforge.networkview.core.report.ReportUtils;
import net.simforge.networkview.core.report.persistence.Report;
import net.simforge.networkview.core.report.persistence.ReportPilotPosition;
import net.simforge.networkview.map.dto.DtoHelper;
import net.simforge.networkview.map.dto.NetworkStatusDto;
import net.simforge.networkview.map.dto.PilotPositionDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@AllArgsConstructor
public class NetworkStatusRefresher {
    private final EntityManager entityManager;

    @Scheduled(fixedRate = 1000)
    public void refresh() {
        String networkName = "IVAO";
        log.trace("Status refresh for {}: started...", networkName);

        Network network = Network.valueOf(networkName);

        String nowTimestamp = ReportUtils.toTimestamp(JavaTime.nowUtc());

        //noinspection JpaQlInspection
        Report report = (Report) entityManager
                .createQuery("select r from Report r where r.report < :now and r.parsed = true order by r.report desc")
                .setParameter("now", nowTimestamp)
                .setMaxResults(1)
                .getSingleResult();

        NetworkStatusDto loadedStatus = NetworkStatus.get();
        if (loadedStatus != null) {
            boolean reportsAreEquals = loadedStatus.getCurrentReport().equals(report.getReport());
            if (reportsAreEquals) {
                log.trace("Status refresh for {}: no new report found", networkName);
                return;
            }
            boolean newReportIsGreater = loadedStatus.getCurrentReport().compareTo(report.getReport()) < 0;
            if (newReportIsGreater) {
                log.debug("Status refresh for {}: new report {} found - loading...", networkName, report.getReport());
            } else {
                log.warn("Status refresh for {}: wrong report {} found - skipped", networkName, report.getReport());
                return;
            }
        } else {
            log.debug("Status refresh for {}: new report {} found as there is no status loaded before - loading...", networkName, report.getReport());
        }

        NetworkStatusDto newStatus = new NetworkStatusDto();
        newStatus.setNetwork(network.name());
        newStatus.setCurrentReport(report.getReport());

        //noinspection unchecked
        List<ReportPilotPosition> reportPilotPositions = entityManager
                .createQuery("select p from ReportPilotPosition p where p.report = :report")
                .setParameter("report", report)
                .getResultList();
        List<PilotPositionDto> pilotPositionDtos = new ArrayList<>();
        for (ReportPilotPosition reportPilotPosition : reportPilotPositions) {
            Position position = Position.create(reportPilotPosition);

            PilotPositionDto pilotPositionDto = DtoHelper.getPilotPositionDto(reportPilotPosition, position);

            pilotPositionDtos.add(pilotPositionDto);
        }
        newStatus.setPilotPositions(pilotPositionDtos);


        LocalDateTime reportDt = ReportUtils.fromTimestampJava(report.getReport());
        long timeDifferenceMillis = JavaTime.nowUtc().toEpochSecond(ZoneOffset.UTC) - reportDt.toEpochSecond(ZoneOffset.UTC);
        long timeDifference = timeDifferenceMillis / TimeUnit.MINUTES.toMillis(1);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        String statusCode;
        String statusMessage;
        String statusDetails;

        if (timeDifference < 5) {
            statusCode = "OK";
            statusMessage = String.format("%s flights online", reportPilotPositions.size());
            switch ((int) timeDifference) {
                case 0:
                    statusDetails = String.format("Report %s, it is actual data", timeFormatter.format(reportDt));
                    break;
                case 1:
                    statusDetails = String.format("Report %s, it is actual data", timeFormatter.format(reportDt));
                    break;
                default:
                    statusDetails = String.format("Report %s, it is %s minutes behind", timeFormatter.format(reportDt), timeDifference);
                    break;
            }
        } else if (timeDifference < 15) {
            statusCode = "GAP";
            statusMessage = String.format("%s flights online", reportPilotPositions.size());
            statusDetails = String.format("It seems like there is a GAP in reports. Last report %s, it is %s minutes behind", timeFormatter.format(reportDt), timeDifference);
        } else {
            statusCode = "OUTDATED";
            statusMessage = "Outdated positions";
            statusDetails = String.format("Data feed is down most probably. Last report %s, it is %s minutes behind", timeFormatter.format(reportDt), timeDifference);
        }

        newStatus.setCurrentStatusCode(statusCode);
        newStatus.setCurrentStatusMessage(statusMessage);
        newStatus.setCurrentStatusDetails(statusDetails);

        log.info("Status refresh for {}: new report {} loaded, pilots online {}", networkName, report.getReport(), reportPilotPositions.size());

        NetworkStatus.set(newStatus);
    }
}
