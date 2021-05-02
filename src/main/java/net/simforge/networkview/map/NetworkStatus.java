package net.simforge.networkview.map;

import net.simforge.networkview.map.dto.NetworkStatusDto;

import java.util.Optional;

public class NetworkStatus {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static Optional<NetworkStatusDto> networkStatus = Optional.empty();

    public static synchronized Optional<NetworkStatusDto> get() {
        return networkStatus;
    }

    public static synchronized void set(NetworkStatusDto networkStatusDto) {
        NetworkStatus.networkStatus = Optional.of(networkStatusDto);
    }
}
