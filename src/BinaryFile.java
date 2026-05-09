import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BinaryFile {
    private final Path path;
    private final byte[] data;

    public BinaryFile(Path path) throws IOException {
        this.path = path;
        long size = Files.size(path);

        if (size > Integer.MAX_VALUE) {
            throw new IOException("\ud30c\uc77c\uc774 \ub108\ubb34 \ucee4\uc11c byte \ubc30\uc5f4\ub85c \uc77d\uc744 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4: " + size + " bytes");
        }

        this.data = Files.readAllBytes(path);
    }

    public Path getPath() {
        return path;
    }

    public byte[] getBytes() {
        byte[] copy = new byte[data.length];
        System.arraycopy(data, 0, copy, 0, data.length);
        return copy;
    }

    public long size() {
        return data.length;
    }

    public int readUnsignedByte(int offset) {
        validateRange(offset, 1);
        return data[offset] & 0xFF;
    }

    public int readLittleEndianShort(int offset) {
        validateRange(offset, 2);
        return (data[offset] & 0xFF)
                | ((data[offset + 1] & 0xFF) << 8);
    }

    public int readBigEndianShort(int offset) {
        validateRange(offset, 2);
        return ((data[offset] & 0xFF) << 8)
                | (data[offset + 1] & 0xFF);
    }

    public long readLittleEndianInt(int offset) {
        validateRange(offset, 4);
        return ((long) data[offset] & 0xFF)
                | (((long) data[offset + 1] & 0xFF) << 8)
                | (((long) data[offset + 2] & 0xFF) << 16)
                | (((long) data[offset + 3] & 0xFF) << 24);
    }

    public long readLittleEndianLong(int offset) {
        validateRange(offset, 8);
        return ((long) data[offset] & 0xFF)
                | (((long) data[offset + 1] & 0xFF) << 8)
                | (((long) data[offset + 2] & 0xFF) << 16)
                | (((long) data[offset + 3] & 0xFF) << 24)
                | (((long) data[offset + 4] & 0xFF) << 32)
                | (((long) data[offset + 5] & 0xFF) << 40)
                | (((long) data[offset + 6] & 0xFF) << 48)
                | (((long) data[offset + 7] & 0xFF) << 56);
    }

    private void validateRange(int offset, int length) {
        if (offset < 0 || length < 0 || ((long) offset + length) > data.length) {
            throw new IllegalArgumentException("\uc798\ubabb\ub41c \ud30c\uc77c offset \ubc94\uc704\uc785\ub2c8\ub2e4. offset="
                    + offset + ", length=" + length);
        }
    }
}
