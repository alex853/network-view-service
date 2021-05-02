package net.simforge.networkview.map.dto;

import lombok.Data;
import net.simforge.commons.misc.Geo;

@Data
public class PilotPositionDto {
    private int pilotNumber;
    private String callsign;
    private String status;
    private double latitude;
    private double longitude;
    private int heading;
    private int groundspeed;
    private String altitude;
    private String type;
    private String regNo;
    private String fpOrigin;
    private Geo.Coords fpOriginCoords;
    private String fpDestination;
    private Geo.Coords fpDestinationCoords;
}
