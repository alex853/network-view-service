package net.simforge.networkview.map;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.simforge.commons.misc.JavaTime;
import net.simforge.networkview.core.Network;
import net.simforge.networkview.core.Position;
import net.simforge.networkview.core.report.ReportUtils;
import net.simforge.networkview.core.report.persistence.Report;
import net.simforge.networkview.core.report.persistence.ReportPilotPosition;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("service/v1")
@CrossOrigin
@Slf4j
@AllArgsConstructor
public class Controller {
    private final EntityManager entityManager;

    @GetMapping("/hello-world")
    public String getHelloWorld() {
        return "Hello, World!";
    }

    @GetMapping("memory-status")
    public MemoryStats getMemoryStatistics() {
        MemoryStats stats = new MemoryStats();
        stats.setTotalMemory(Runtime.getRuntime().totalMemory());
        stats.setMaxMemory(Runtime.getRuntime().maxMemory());
        stats.setFreeMemory(Runtime.getRuntime().freeMemory());
        return stats;
    }

    @GetMapping("/status")
    public ResponseEntity<NetworkStatusDto> getNetworkStatus(/*@RequestParam("network") String networkName*/) {
        String networkName = "IVAO";
        log.debug("Status request for {}: requested...", networkName);

        Network network = Network.valueOf(networkName);

        NetworkStatusDto result = new NetworkStatusDto();

        result.setNetwork(network.name());

        String nowTimestamp = ReportUtils.toTimestamp(JavaTime.nowUtc());

        //noinspection JpaQlInspection
        Report report = (Report) entityManager
                .createQuery("select r from Report r where r.report < :now and r.parsed = true order by r.report desc")
                .setParameter("now", nowTimestamp)
                .setMaxResults(1)
                .getSingleResult();

        result.setCurrentReport(report.getReport());


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
        result.setPilotPositions(pilotPositionDtos);


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

        result.setCurrentStatusCode(statusCode);
        result.setCurrentStatusMessage(statusMessage);
        result.setCurrentStatusDetails(statusDetails);

        log.info("Status request for {}: loaded report {}, pilots online {}", networkName, reportDt, reportPilotPositions.size());

        return ResponseEntity.ok(result);
    }
}
