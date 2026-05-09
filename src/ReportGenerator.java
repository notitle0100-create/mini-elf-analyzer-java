import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ReportGenerator {
    private static final Path REPORT_PATH = Paths.get("reports", "analysis_report.md");
    private static final int MAX_REPORTED_STRINGS = 20;

    public Path generate(AnalysisResult result) throws IOException {
        FileUtil.ensureReportsDirectory();
        String markdown = buildMarkdown(result);
        FileUtil.saveTextFile(REPORT_PATH, markdown);
        return REPORT_PATH;
    }

    private String buildMarkdown(AnalysisResult result) {
        StringBuilder builder = new StringBuilder();

        builder.append("# Mini ELF Analyzer Report\n\n");
        appendFileInformation(builder, result);
        appendElfHeader(builder, result);
        appendExtractedStrings(builder, result.getExtractedStrings());
        appendSuspiciousStrings(builder, result);
        appendSummary(builder, result);

        return builder.toString();
    }

    private void appendFileInformation(StringBuilder builder, AnalysisResult result) {
        builder.append("## 1. File Information\n");
        builder.append("- File Path: `").append(escapeMarkdownText(result.getFilePath())).append("`\n");
        builder.append("- File Size: ").append(result.getFileSize()).append(" bytes\n\n");
    }

    private void appendElfHeader(StringBuilder builder, AnalysisResult result) {
        builder.append("## 2. ELF Header\n");
        builder.append("- ELF File: ").append(result.isElfFile()).append("\n");
        builder.append("- Class: ").append(result.getBitClass()).append("\n");
        builder.append("- Endian: ").append(result.getEndian()).append("\n");
        builder.append("- Architecture: ").append(result.getArchitecture()).append("\n");
        builder.append("- Entry Point: ").append(result.getEntryPoint()).append("\n");
        builder.append("- Section Header Count: ").append(result.getSectionHeaderCountText()).append("\n");
        builder.append("- Stripped Estimate: ").append(result.getStrippedEstimate()).append("\n\n");
    }

    private void appendExtractedStrings(StringBuilder builder, List<String> extractedStrings) {
        builder.append("## 3. Extracted Strings\n");

        if (extractedStrings.isEmpty()) {
            builder.append("- No readable strings found.\n\n");
            return;
        }

        int limit = Math.min(extractedStrings.size(), MAX_REPORTED_STRINGS);

        for (int i = 0; i < limit; i++) {
            String extractedString = extractedStrings.get(i);
            builder.append("- ").append(escapeMarkdownText(extractedString)).append("\n");
        }

        if (extractedStrings.size() > MAX_REPORTED_STRINGS) {
            builder.append("\n");
            builder.append("총 ")
                    .append(extractedStrings.size())
                    .append("개 중 처음 ")
                    .append(MAX_REPORTED_STRINGS)
                    .append("개 문자열만 표시합니다.\n");
        }

        builder.append("\n");
    }

    private void appendSuspiciousStrings(StringBuilder builder, AnalysisResult result) {
        builder.append("## 4. Suspicious Strings\n");
        builder.append("| Keyword | Result |\n");
        builder.append("|---|---|\n");

        for (String keyword : SuspiciousStringDetector.KEYWORDS) {
            builder.append("| ")
                    .append(keyword)
                    .append(" | ")
                    .append(result.hasSuspiciousKeyword(keyword) ? "Found" : "Not Found")
                    .append(" |\n");
        }

        builder.append("\n");
    }

    private void appendSummary(StringBuilder builder, AnalysisResult result) {
        builder.append("## 5. Summary\n");

        if (!result.isElfFile()) {
            builder.append("입력 파일의 ELF Magic Number가 일치하지 않아 ELF 파일이 아닌 것으로 판단됩니다. ")
                    .append("ELF Header 상세 항목은 N/A로 기록했습니다.\n");
            return;
        }

        builder.append("\uc785\ub825 \ud30c\uc77c\uc740 ")
                .append(result.getBitClass())
                .append(", ")
                .append(result.getEndian())
                .append(", ")
                .append(result.getArchitecture())
                .append(" ELF \ud30c\uc77c\ub85c \uc2dd\ubcc4\ub418\uc5c8\uc2b5\ub2c8\ub2e4. ");

        if ("N/A".equals(result.getEntryPoint()) || "N/A".equals(result.getSectionHeaderCountText())) {
            builder.append("\ud604\uc7ac \uad6c\ud604\uc740 Big Endian \ubc0f \uc77c\ubd80 \uc0c1\uc138 \ud30c\uc2f1\uc744 \uc81c\ud55c\ud558\ubbc0\ub85c Entry Point \ub610\ub294 Section Header Count\uac00 N/A\uc77c \uc218 \uc788\uc2b5\ub2c8\ub2e4. ");
        } else {
            builder.append("Entry Point\uc640 Section Header Count\ub97c ELF Header\uc5d0\uc11c \uc815\uc801\uc73c\ub85c \ucd94\ucd9c\ud588\uc2b5\ub2c8\ub2e4. ");
        }

        if (result.getSuspiciousStrings().isEmpty()) {
            builder.append("\uc9c0\uc815\ub41c \uc758\uc2ec \ubb38\uc790\uc5f4\uc740 \ubc1c\uacac\ub418\uc9c0 \uc54a\uc558\uc2b5\ub2c8\ub2e4.\n");
        } else {
            builder.append("\uc9c0\uc815\ub41c \uc758\uc2ec \ubb38\uc790\uc5f4 \uc911 ")
                    .append(result.getSuspiciousStringsText())
                    .append(" \ud56d\ubaa9\uc774 \ubc1c\uacac\ub418\uc5c8\uc2b5\ub2c8\ub2e4.\n");
        }

        builder.append("Stripped 여부는 문자열 목록의 .symtab 포함 여부를 기준으로 ")
                .append(result.getStrippedEstimate())
                .append("으로 판단했습니다.\n");
    }

    private String escapeMarkdownText(String text) {
        return text.replace("`", "\\`");
    }
}
