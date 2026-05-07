import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ReportGenerator {
    private static final Path REPORT_PATH = Paths.get("reports", "analysis_report.md");

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
        builder.append("- Entry Point: ").append(result.getEntryPoint()).append("\n");
        builder.append("- Section Header Count: ").append(result.getSectionHeaderCountText()).append("\n\n");
    }

    private void appendExtractedStrings(StringBuilder builder, List<String> extractedStrings) {
        builder.append("## 3. Extracted Strings\n");

        if (extractedStrings.isEmpty()) {
            builder.append("- No readable strings found.\n\n");
            return;
        }

        for (String extractedString : extractedStrings) {
            builder.append("- ").append(escapeMarkdownText(extractedString)).append("\n");
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
            builder.append("\uc785\ub825 \ud30c\uc77c\uc758 ELF Magic Number\uac00 \uc77c\uce58\ud558\uc9c0 \uc54a\uc544 ELF \ud30c\uc77c\uc774 \uc544\ub2cc \uac83\uc73c\ub85c \ud310\ub2e8\ub429\ub2c8\ub2e4.\n");
            return;
        }

        builder.append("\uc785\ub825 \ud30c\uc77c\uc740 ")
                .append(result.getBitClass())
                .append(", ")
                .append(result.getEndian())
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
    }

    private String escapeMarkdownText(String text) {
        return text.replace("`", "\\`");
    }
}
