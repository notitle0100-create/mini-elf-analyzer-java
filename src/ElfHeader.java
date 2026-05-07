public class ElfHeader {
    private static final int EI_CLASS_OFFSET = 4;
    private static final int EI_DATA_OFFSET = 5;
    private static final int ELF32_HEADER_SIZE = 52;
    private static final int ELF64_HEADER_SIZE = 64;

    private final boolean elfFile;
    private final String bitClass;
    private final String endian;
    private final String entryPoint;
    private final int sectionHeaderCount;

    private ElfHeader(boolean elfFile, String bitClass, String endian,
                      String entryPoint, int sectionHeaderCount) {
        this.elfFile = elfFile;
        this.bitClass = bitClass;
        this.endian = endian;
        this.entryPoint = entryPoint;
        this.sectionHeaderCount = sectionHeaderCount;
    }

    public static ElfHeader parse(BinaryFile binaryFile) {
        if (!hasElfMagic(binaryFile)) {
            return new ElfHeader(false, "N/A", "N/A", "N/A", -1);
        }

        int classValue = binaryFile.readUnsignedByte(EI_CLASS_OFFSET);
        int endianValue = binaryFile.readUnsignedByte(EI_DATA_OFFSET);
        String bitClass = parseBitClass(classValue);
        String endian = parseEndian(endianValue);

        String entryPoint = "N/A";
        int sectionHeaderCount = -1;

        if (classValue == 2 && endianValue == 1) {
            validateMinimumSize(binaryFile, ELF64_HEADER_SIZE, "64-bit Little Endian ELF");
            entryPoint = toHex(binaryFile.readLittleEndianLong(0x18));
            sectionHeaderCount = binaryFile.readLittleEndianShort(0x3C);
        } else if (classValue == 1 && endianValue == 1) {
            validateMinimumSize(binaryFile, ELF32_HEADER_SIZE, "32-bit Little Endian ELF");
            entryPoint = toHex(binaryFile.readLittleEndianInt(0x18));
            sectionHeaderCount = binaryFile.readLittleEndianShort(0x30);
        }

        return new ElfHeader(true, bitClass, endian, entryPoint, sectionHeaderCount);
    }

    private static boolean hasElfMagic(BinaryFile binaryFile) {
        return binaryFile.readUnsignedByte(0) == 0x7F
                && binaryFile.readUnsignedByte(1) == 0x45
                && binaryFile.readUnsignedByte(2) == 0x4C
                && binaryFile.readUnsignedByte(3) == 0x46;
    }

    private static String parseBitClass(int classValue) {
        if (classValue == 1) {
            return "32-bit";
        }
        if (classValue == 2) {
            return "64-bit";
        }
        return "Unknown";
    }

    private static String parseEndian(int endianValue) {
        if (endianValue == 1) {
            return "Little Endian";
        }
        if (endianValue == 2) {
            return "Big Endian";
        }
        return "Unknown";
    }

    private static void validateMinimumSize(BinaryFile binaryFile, int minimumSize, String label) {
        if (binaryFile.size() < minimumSize) {
            throw new IllegalArgumentException(label + " Header \ubd84\uc11d\uc5d0 \ub108\ubb34 \uc791\uc2b5\ub2c8\ub2e4. \ucd5c\uc18c "
                    + minimumSize + " bytes \uc774\uc0c1\uc774 \ud544\uc694\ud569\ub2c8\ub2e4.");
        }
    }

    private static String toHex(long value) {
        return "0x" + Long.toHexString(value);
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
}
