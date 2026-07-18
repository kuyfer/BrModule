// cires.bemodule.dtos.ReportData
package cires.bemodule.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Generic tabular report model. All three report types populate this same
 * shape differently, so PDF/Excel generators only need to be written once
 * instead of once per report type.
 */
@Getter
@AllArgsConstructor
public class ReportData {
    private String title;
    private String subtitle;                  // human-readable filter summary
    private List<String> columns;
    private List<List<String>> rows;
    private LinkedHashMap<String, String> summary; // key stats shown above the table
    private LocalDateTime generatedAt;
}