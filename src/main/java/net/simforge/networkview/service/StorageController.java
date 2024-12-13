package net.simforge.networkview.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.simforge.networkview.core.Network;
import net.simforge.networkview.core.report.compact.CompactifiedStorage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("storage/v1")
@CrossOrigin
@Slf4j
@AllArgsConstructor
public class StorageController {

    // todo ak convert into bean with path from parameters
    private final CompactifiedStorage storage = CompactifiedStorage.getStorage(
            "/home/alex853/IdeaProjects/simforge/network-view-datafeeder/local/data",
            Network.VATSIM);

    @GetMapping("report/first")
    public ResponseEntity<String> getFirstReport() throws IOException {
        final String firstReport = storage.getFirstReport();
        if (firstReport == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(firstReport);
    }

    @GetMapping("report/next")
    public ResponseEntity<String> getFirstReport(final @RequestParam("report") String report) throws IOException {
        final String nextReport = storage.getNextReport(report);
        if (nextReport == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(nextReport);
    }

    @GetMapping("report/list")
    public ResponseEntity<String> getReportList() {
        throw new UnsupportedOperationException();
    }

    @GetMapping("report/positions")
    public ResponseEntity<String> getPositions(final @RequestParam("report") String report) {
        throw new UnsupportedOperationException();
    }
}
