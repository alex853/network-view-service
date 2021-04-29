package net.simforge.networkview.map;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "report_pilot_fp_remarks")
@Data
public class ReportPilotFpRemarks implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_report_pilot_fp_remarks_id")
    @SequenceGenerator(name = "pk_report_pilot_fp_remarks_id", sequenceName = "report_pilot_fp_remarks_id_seq", allocationSize = 1)
    private Long id;
    @Version
    private Integer version;

   // @Cut(size = 300)
    private String remarks;
}
