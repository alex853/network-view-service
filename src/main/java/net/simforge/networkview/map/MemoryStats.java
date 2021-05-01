package net.simforge.networkview.map;

import lombok.Data;

@Data
public class MemoryStats {
    private long totalMemory;
    private long maxMemory;
    private long freeMemory;
}
