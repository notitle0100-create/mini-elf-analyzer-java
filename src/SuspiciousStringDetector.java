import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SuspiciousStringDetector {
    public static final List<String> KEYWORDS = Collections.unmodifiableList(
            Arrays.asList("flag", "password", "/bin/sh", "system", "execve")
    );

    public List<String> detect(List<String> extractedStrings) {
        List<String> foundKeywords = new ArrayList<String>();

        for (String keyword : KEYWORDS) {
            if (containsKeyword(extractedStrings, keyword)) {
                foundKeywords.add(keyword);
            }
        }

        return foundKeywords;
    }

    private boolean containsKeyword(List<String> extractedStrings, String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);

        for (String extractedString : extractedStrings) {
            String normalizedString = extractedString.toLowerCase(Locale.ROOT);
            if (normalizedString.contains(normalizedKeyword)) {
                return true;
            }
        }

        return false;
    }
}
