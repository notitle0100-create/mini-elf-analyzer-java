import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnalysisResult {
    private final String filePath;
    private final long fileSize;
    private final boolean elfFile;
    private final String bitClass;
    private final String endian;
    private final String entryPoint;
    private final int sectionHeaderCount;
    private final List<String> extractedStrings;
    private final List<String> suspiciousStrings;
    private String reportPath;

    public AnalysisResult(String filePath, long fileSize, boolean elfFile,
                          String bitClass, String endian, String entryPoint,
                          int sectionHeaderCount, List<String> extractedStrings,
                          List<String> suspiciousStrings) {
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.elfFile = elfFile;
        this.bitClass = bitClass;
        this.endian = endian;
        this.entryPoint = entryPoint;
        this.sectionHeaderCount = sectionHeaderCount;
        this.extractedStrings = Collections.unmodifiableList(new ArrayList<String>(extractedStrings));
        this.suspiciousStrings = Collections.unmodifiableList(new ArrayList<String>(suspiciousStrings));
        this.reportPath = "reports/analysis_report.md";
    }

    public String getFilePath() {
        return filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public boolean isElfFile() {
        return elfFile;
    }

    public String getBitClass() {
        return bitClass;
    }

    public String getEndian() {
        return endian;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public int getSectionHeaderCount() {
        return sectionHeaderCount;
    }

    public String getSectionHeaderCountText() {
        if (sectionHeaderCount < 0) {
            return "N/A";
        }
        return String.valueOf(sectionHeaderCount);
    }

    public List<String> getExtractedStrings() {
        return extractedStrings;
    }

    public List<String> getSuspiciousStrings() {
        return suspiciousStrings;
    }

    public String getSuspiciousStringsText() {
        if (suspiciousStrings.isEmpty()) {
            return "None";
        }
        return joinStrings(suspiciousStrings);
    }

    public String getReportPath() {
        return reportPath;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    public boolean hasSuspiciousKeyword(String keyword) {
        return suspiciousStrings.contains(keyword);
    }

    private String joinStrings(List<String> values) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(values.get(i));
        }

        return builder.toString();
    }
}
