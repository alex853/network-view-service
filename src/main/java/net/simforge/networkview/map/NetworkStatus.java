package net.simforge.networkview.map;

import net.simforge.networkview.core.Network;
import net.simforge.networkview.map.dto.NetworkStatusDto;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkStatus {
    private static final Map<Network, NetworkStatusDto> networkStatuses = new ConcurrentHashMap<>();

    public static synchronized Optional<NetworkStatusDto> get(Network network) {
        return Optional.ofNullable(networkStatuses.get(network));
    }

    public static synchronized void set(Network network, NetworkStatusDto networkStatusDto) {
        networkStatuses.put(network, networkStatusDto);
    }
}
