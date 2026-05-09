import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("[Mini ELF Analyzer]");
        System.out.print("\ubd84\uc11d\ud560 \ud30c\uc77c \uacbd\ub85c\ub97c \uc785\ub825\ud558\uc138\uc694: ");

        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());
        String inputPath = scanner.nextLine().trim();

        if (inputPath.isEmpty()) {
            System.out.println("[ERROR] \ud30c\uc77c \uacbd\ub85c\uac00 \uc785\ub825\ub418\uc9c0 \uc54a\uc558\uc2b5\ub2c8\ub2e4.");
            return;
        }

        try {
            ElfAnalyzer analyzer = new ElfAnalyzer(new ReportGenerator());
            AnalysisResult result = analyzer.analyze(inputPath);
            printResult(result);
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        } catch (IOException e) {
            System.out.println("[ERROR] \ud30c\uc77c \ucc98\ub9ac \uc911 \uc624\ub958\uac00 \ubc1c\uc0dd\ud588\uc2b5\ub2c8\ub2e4: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[ERROR] \ubd84\uc11d \uc911 \uc608\uae30\uce58 \ubabb\ud55c \uc624\ub958\uac00 \ubc1c\uc0dd\ud588\uc2b5\ub2c8\ub2e4: " + e.getMessage());
        }
    }

    private static void printResult(AnalysisResult result) {
        System.out.println();
        System.out.println("===== ELF Analysis Result =====");
        System.out.println("ELF File: " + result.isElfFile());

        if (!result.isElfFile()) {
            System.out.println("ELF \ud30c\uc77c\uc774 \uc544\ub2d8");
        }

        System.out.println("Class: " + result.getBitClass());
        System.out.println("Endian: " + result.getEndian());
        System.out.println("Architecture: " + result.getArchitecture());
        System.out.println("Entry Point: " + result.getEntryPoint());
        System.out.println("Section Header Count: " + result.getSectionHeaderCountText());
        System.out.println("Stripped Estimate: " + result.getStrippedEstimate());
        System.out.println("Suspicious Strings: " + result.getSuspiciousStringsText());
        System.out.println();
        System.out.println("Markdown report generated: " + result.getReportPath());
    }
}
