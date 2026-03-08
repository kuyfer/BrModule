package cires.bemodule.restcontrollers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exports")
public class ExportController {

    @GetMapping("/{filename}")
    public void exportFile(@PathVariable String filename) {}

    @GetMapping("/history")
    public void getExportHistory() {}
}