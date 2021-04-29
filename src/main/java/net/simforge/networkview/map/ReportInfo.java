package net.simforge.networkview.map;

import java.time.LocalDateTime;

public interface ReportInfo {
    Long getId();

    String getReport();

    default LocalDateTime getDt() {
        return ReportUtils.fromTimestampJava(getReport());
    }
}
