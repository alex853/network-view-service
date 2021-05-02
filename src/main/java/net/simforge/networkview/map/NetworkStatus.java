package net.simforge.networkview.map;

import net.simforge.networkview.map.dto.NetworkStatusDto;

public class NetworkStatus {
    private static NetworkStatusDto networkStatusDto;

    public static synchronized NetworkStatusDto get() {
        return networkStatusDto;
    }

    public static synchronized void set(NetworkStatusDto networkStatusDto) {
        NetworkStatus.networkStatusDto = networkStatusDto;
    }
}
