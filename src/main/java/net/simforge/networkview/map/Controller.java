package net.simforge.networkview.map;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.simforge.networkview.core.Network;
import net.simforge.networkview.map.dto.MemoryStatsDto;
import net.simforge.networkview.map.dto.NetworkStatusDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("service/v1")
@CrossOrigin
@Slf4j
@AllArgsConstructor
public class Controller {
    @GetMapping("hello-world")
    public String getHelloWorld() {
        return "Hello, World!";
    }

    @GetMapping("memory-status")
    public MemoryStatsDto getMemoryStatistics() {
        MemoryStatsDto stats = new MemoryStatsDto();
        stats.setTotalMemory(Runtime.getRuntime().totalMemory());
        stats.setMaxMemory(Runtime.getRuntime().maxMemory());
        stats.setFreeMemory(Runtime.getRuntime().freeMemory());
        return stats;
    }

    @GetMapping("status")
    public ResponseEntity<NetworkStatusDto> getNetworkStatus(@RequestParam("network") String networkName) {
        Network network = Network.valueOf(networkName);
        return NetworkStatus.get(network)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
