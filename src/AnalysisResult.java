import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnalysisResult {
    private final String filePath;
    private final long fileSize;
    private final boolean elfFile;
    private final String bitClass;
    private final String endian;
    private final String architecture;
    private final String entryPoint;
    private final int sectionHeaderCount;
    private final String strippedEstimate;
    private final String functionCountEstimate;
    private final List<SectionHeader> sections;
    private final List<String> extractedStrings;
    private final List<String> suspiciousStrings;
    private String reportPath;

    public AnalysisResult(String filePath, long fileSize, boolean elfFile,
                          String bitClass, String endian, String architecture,
                          String entryPoint, int sectionHeaderCount, String strippedEstimate,
                          String functionCountEstimate, List<SectionHeader> sections,
                          List<String> extractedStrings,
                          List<String> suspiciousStrings) {
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.elfFile = elfFile;
        this.bitClass = bitClass;
        this.endian = endian;
        this.architecture = architecture;
        this.entryPoint = entryPoint;
        this.sectionHeaderCount = sectionHeaderCount;
        this.strippedEstimate = strippedEstimate;
        this.functionCountEstimate = functionCountEstimate;
        this.sections = Collections.unmodifiableList(new ArrayList<SectionHeader>(sections));
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

    public String getArchitecture() {
        return architecture;
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

    public String getStrippedEstimate() {
        return strippedEstimate;
    }

    public String getFunctionCountEstimate() {
        return functionCountEstimate;
    }

    public List<SectionHeader> getSections() {
        return sections;
    }

    public List<SectionHeader> getImportantSections() {
        String[] importantNames = {
                ".text",
                ".rodata",
                ".data",
                ".bss",
                ".dynsym",
                ".dynstr",
                ".symtab",
                ".strtab",
                ".shstrtab"
        };
        List<SectionHeader> importantSections = new ArrayList<SectionHeader>();

        for (String importantName : importantNames) {
            SectionHeader section = findSection(importantName);
            if (section != null) {
                importantSections.add(section);
            }
        }

        return Collections.unmodifiableList(importantSections);
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

    private SectionHeader findSection(String sectionName) {
        for (SectionHeader section : sections) {
            if (sectionName.equals(section.getName())) {
                return section;
            }
        }

        return null;
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
