package net.simforge.networkview.map.dto;

import lombok.Data;

import java.util.List;

@Data
public class NetworkStatusDto {
    private String network;
    private String currentReport;

    // CODE     TIME THRESHOLD
    // OK       Up to 5 mins from now
    // GAP      Up to 15 mins from now
    // OUTDATED Higher
    private String currentStatusCode;
    private String currentStatusMessage;
    private String currentStatusDetails;

    private List<PilotPositionDto> pilotPositions;
}
