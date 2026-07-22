package cires.bemodule.services;

import cires.bemodule.dtos.ReportData;
import cires.bemodule.exceptions.ReportGenerationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class PdfReportGenerator {

    private static final DateTimeFormatter TS_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] generate(ReportData data) {
        log.info("Generating PDF report: {}", data.getTitle());
        String html = buildHtml(data);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(out);
            byte[] pdf = out.toByteArray();
            log.info("PDF report generated successfully, size: {} bytes", pdf.length);
            return pdf;
        } catch (Exception e) {
            log.error("Failed to generate PDF report: {}", data.getTitle(), e);
            throw new ReportGenerationException("Échec de la génération du rapport PDF", e);
        }
    }

    // Flying Saucer requires well-formed XHTML (every tag closed, no bare
    // &, attributes quoted) and only supports a CSS 2.1-ish subset - no
    // flexbox/grid. Built by hand here rather than via a template engine
    // to avoid adding a Thymeleaf dependency just for this.
    private String buildHtml(ReportData data) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><style>")
                .append("body { font-family: Helvetica, Arial, sans-serif; font-size: 11px; color: #222; }")
                .append("h1 { font-size: 18px; color: #007E39; margin-bottom: 2px; }")
                .append(".subtitle { color: #666; margin-bottom: 12px; }")
                .append(".summary { margin-bottom: 16px; }")
                .append(".summary td { padding: 4px 12px 4px 0; }")
                .append(".summary .label { color: #666; }")
                .append(".summary .value { font-weight: bold; color: #007E39; }")
                .append("table.data { width: 100%; border-collapse: collapse; }")
                .append("table.data th { background: #007E39; color: #fff; text-align: left; padding: 6px 8px; font-size: 10px; }")
                .append("table.data td { padding: 5px 8px; border-bottom: 1px solid #ddd; font-size: 10px; }")
                .append("table.data tr:nth-child(even) { background: #f6f6f6; }")
                .append(".footer { margin-top: 16px; color: #999; font-size: 9px; }")
                .append("</style></head><body>");

        sb.append("<h1>").append(escape(data.getTitle())).append("</h1>");
        sb.append("<div class=\"subtitle\">").append(escape(data.getSubtitle())).append("</div>");

        if (!data.getSummary().isEmpty()) {
            sb.append("<table class=\"summary\"><tr>");
            for (var entry : data.getSummary().entrySet()) {
                sb.append("<td><div class=\"label\">").append(escape(entry.getKey())).append("</div>")
                        .append("<div class=\"value\">").append(escape(entry.getValue())).append("</div></td>");
            }
            sb.append("</tr></table>");
        }

        sb.append("<table class=\"data\"><thead><tr>");
        for (String col : data.getColumns()) {
            sb.append("<th>").append(escape(col)).append("</th>");
        }
        sb.append("</tr></thead><tbody>");

        if (data.getRows().isEmpty()) {
            sb.append("<tr><td colspan=\"").append(data.getColumns().size())
                    .append("\">Aucune donnée pour cette période.</td></tr>");
        } else {
            for (var row : data.getRows()) {
                sb.append("<tr>");
                for (String cell : row) {
                    sb.append("<td>").append(escape(cell)).append("</td>");
                }
                sb.append("</tr>");
            }
        }
        sb.append("</tbody></table>");

        sb.append("<div class=\"footer\">Généré le ")
                .append(data.getGeneratedAt().format(TS_FMT))
                .append(" — Plateforme de gestion des sessions de formation, Cires Technologies</div>");

        sb.append("</body></html>");
        return sb.toString();
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}