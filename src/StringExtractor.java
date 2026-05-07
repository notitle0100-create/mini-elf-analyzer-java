import java.util.ArrayList;
import java.util.List;

public class StringExtractor {
    private static final int MINIMUM_LENGTH = 4;

    public List<String> extract(byte[] data) {
        List<String> strings = new ArrayList<String>();
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < data.length; i++) {
            int value = data[i] & 0xFF;

            if (isPrintableAscii(value)) {
                current.append((char) value);
            } else {
                addIfReadable(strings, current);
                current.setLength(0);
            }
        }

        addIfReadable(strings, current);
        return strings;
    }

    private boolean isPrintableAscii(int value) {
        return value >= 0x20 && value <= 0x7E;
    }

    private void addIfReadable(List<String> strings, StringBuilder current) {
        if (current.length() >= MINIMUM_LENGTH) {
            strings.add(current.toString());
        }
    }
}
