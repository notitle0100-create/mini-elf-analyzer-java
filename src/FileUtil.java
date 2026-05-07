import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileUtil {
    private static final Path REPORTS_DIRECTORY = Paths.get("reports");

    private FileUtil() {
    }

    public static boolean exists(Path path) {
        return Files.exists(path);
    }

    public static boolean isRegularFile(Path path) {
        return Files.isRegularFile(path);
    }

    public static long fileSize(Path path) throws IOException {
        return Files.size(path);
    }

    public static void ensureReportsDirectory() throws IOException {
        Files.createDirectories(REPORTS_DIRECTORY);
    }

    public static void saveTextFile(Path path, String content) throws IOException {
        Path parent = path.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.write(
                path,
                content.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        );
    }

    public static String stripSurroundingQuotes(String text) {
        String trimmed = text.trim();

        if (trimmed.length() >= 2) {
            boolean doubleQuoted = trimmed.startsWith("\"") && trimmed.endsWith("\"");
            boolean singleQuoted = trimmed.startsWith("'") && trimmed.endsWith("'");

            if (doubleQuoted || singleQuoted) {
                return trimmed.substring(1, trimmed.length() - 1);
            }
        }

        return trimmed;
    }
}
