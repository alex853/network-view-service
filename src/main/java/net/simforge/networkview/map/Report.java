package net.simforge.networkview.map;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "report")
@Data
public class Report implements Serializable, ReportInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_report_id")
    @SequenceGenerator(name = "pk_report_id", sequenceName = "report_id_seq", allocationSize = 1)
    private Long id;
    @Version
    private Integer version;

    private String report;
    private Integer clients;
    private Integer pilots;
    @Column(name = "has_logs")
    private Boolean hasLogs;
    private Boolean parsed;

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", report='" + report + '\'' +
                '}';
    }
}
