import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ElfAnalyzer {
    private static final int MINIMUM_IDENT_SIZE = 16;

    private final ReportGenerator reportGenerator;
    private final StringExtractor stringExtractor;
    private final SuspiciousStringDetector suspiciousStringDetector;
    private final SectionHeaderParser sectionHeaderParser;
    private final SymbolTableAnalyzer symbolTableAnalyzer;

    public ElfAnalyzer(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
        this.stringExtractor = new StringExtractor();
        this.suspiciousStringDetector = new SuspiciousStringDetector();
        this.sectionHeaderParser = new SectionHeaderParser();
        this.symbolTableAnalyzer = new SymbolTableAnalyzer();
    }

    public AnalysisResult analyze(String inputPath) throws IOException {
        Path path = Paths.get(FileUtil.stripSurroundingQuotes(inputPath));
        validateFile(path);

        BinaryFile binaryFile = new BinaryFile(path);
        ElfHeader elfHeader = ElfHeader.parse(binaryFile);
        List<String> extractedStrings = stringExtractor.extract(binaryFile.getBytes());
        List<String> suspiciousStrings = suspiciousStringDetector.detect(extractedStrings);
        List<SectionHeader> sections = sectionHeaderParser.parse(binaryFile, elfHeader);
        String strippedEstimate = estimateStripped(elfHeader.isElfFile(), sections);
        String functionCountEstimate = symbolTableAnalyzer.estimateFunctionCount(binaryFile, sections);

        AnalysisResult result = new AnalysisResult(
                path.toAbsolutePath().normalize().toString(),
                binaryFile.size(),
                elfHeader.isElfFile(),
                elfHeader.getBitClass(),
                elfHeader.getEndian(),
                elfHeader.getArchitecture(),
                elfHeader.getEntryPoint(),
                elfHeader.getSectionHeaderCount(),
                strippedEstimate,
                functionCountEstimate,
                sections,
                extractedStrings,
                suspiciousStrings
        );

        Path reportPath = reportGenerator.generate(result);
        result.setReportPath(reportPath.toString().replace('\\', '/'));
        return result;
    }

    private String estimateStripped(boolean elfFile, List<SectionHeader> sections) {
        if (!elfFile) {
            return "N/A";
        }

        if (sections.isEmpty()) {
            return "N/A";
        }

        for (SectionHeader section : sections) {
            if (".symtab".equals(section.getName())) {
                return "Not Stripped (추정)";
            }
        }

        return "Stripped (추정)";
    }

    private void validateFile(Path path) throws IOException {
        if (!FileUtil.exists(path)) {
            throw new IllegalArgumentException("\ud30c\uc77c\uc774 \uc874\uc7ac\ud558\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4: " + path);
        }

        if (!FileUtil.isRegularFile(path)) {
            throw new IllegalArgumentException("\uc77c\ubc18 \ud30c\uc77c\uc774 \uc544\ub2d9\ub2c8\ub2e4. \ub514\ub809\ud130\ub9ac \ub610\ub294 \ud2b9\uc218 \ud30c\uc77c\uc740 \ubd84\uc11d\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4: " + path);
        }

        long fileSize = FileUtil.fileSize(path);
        if (fileSize < MINIMUM_IDENT_SIZE) {
            throw new IllegalArgumentException("\ud30c\uc77c \ud06c\uae30\uac00 ELF Header \ubd84\uc11d\uc5d0 \ub108\ubb34 \uc791\uc2b5\ub2c8\ub2e4. \ucd5c\uc18c "
                    + MINIMUM_IDENT_SIZE + " bytes \uc774\uc0c1\uc774 \ud544\uc694\ud569\ub2c8\ub2e4.");
        }
    }
}
