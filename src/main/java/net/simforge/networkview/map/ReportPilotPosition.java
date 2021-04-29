package net.simforge.networkview.map;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "report_pilot_position")
@Data
public class ReportPilotPosition implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_report_pilot_position_id")
    @SequenceGenerator(name = "pk_report_pilot_position_id", sequenceName = "report_pilot_position_id_seq", allocationSize = 1)
    private Long id;
    @Version
    private Integer version;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private Report report;
    @Column(name = "pilot_number")
    private Integer pilotNumber;
    private String callsign;
    private Double latitude;
    private Double longitude;
    private Integer altitude;
    private Integer groundspeed;
    private Integer heading;
    @Column(name = "qnh_mb")
    private Integer qnhMb;
    @Column(name = "on_ground")
    private Boolean onGround;
    @Column(name = "fp_aircraft")
    private String fpAircraft;
    @Column(name = "fp_origin")
    private String fpOrigin;
    @Column(name = "fp_destination")
    private String fpDestination;
    @ManyToOne
    @JoinColumn(name = "fp_remarks_id")
    private ReportPilotFpRemarks fpRemarks;
    @Column(name = "parsed_reg_no")
    private String parsedRegNo;
}
