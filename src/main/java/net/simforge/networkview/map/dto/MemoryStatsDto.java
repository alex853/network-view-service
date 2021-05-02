package net.simforge.networkview.map.dto;

import lombok.Data;

@Data
public class MemoryStatsDto {
    private long totalMemory;
    private long maxMemory;
    private long freeMemory;
}
