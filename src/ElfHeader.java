public class ElfHeader {
    private static final int EI_CLASS_OFFSET = 4;
    private static final int EI_DATA_OFFSET = 5;
    private static final int E_MACHINE_OFFSET = 0x12;
    private static final int ELF32_HEADER_SIZE = 52;
    private static final int ELF64_HEADER_SIZE = 64;
    private static final int UNKNOWN_OFFSET = -1;

    private final boolean elfFile;
    private final String bitClass;
    private final String endian;
    private final String architecture;
    private final String entryPoint;
    private final long sectionHeaderOffset;
    private final int sectionHeaderEntrySize;
    private final int sectionHeaderCount;
    private final int sectionHeaderStringTableIndex;

    private ElfHeader(boolean elfFile, String bitClass, String endian,
                      String architecture, String entryPoint, long sectionHeaderOffset,
                      int sectionHeaderEntrySize, int sectionHeaderCount,
                      int sectionHeaderStringTableIndex) {
        this.elfFile = elfFile;
        this.bitClass = bitClass;
        this.endian = endian;
        this.architecture = architecture;
        this.entryPoint = entryPoint;
        this.sectionHeaderOffset = sectionHeaderOffset;
        this.sectionHeaderEntrySize = sectionHeaderEntrySize;
        this.sectionHeaderCount = sectionHeaderCount;
        this.sectionHeaderStringTableIndex = sectionHeaderStringTableIndex;
    }

    public static ElfHeader parse(BinaryFile binaryFile) {
        if (!hasElfMagic(binaryFile)) {
            return new ElfHeader(false, "N/A", "N/A", "N/A", "N/A", UNKNOWN_OFFSET, -1, -1, -1);
        }

        int classValue = binaryFile.readUnsignedByte(EI_CLASS_OFFSET);
        int endianValue = binaryFile.readUnsignedByte(EI_DATA_OFFSET);
        String bitClass = parseBitClass(classValue);
        String endian = parseEndian(endianValue);
        String architecture = parseArchitecture(readMachineValue(binaryFile, endianValue));

        String entryPoint = "N/A";
        long sectionHeaderOffset = UNKNOWN_OFFSET;
        int sectionHeaderEntrySize = -1;
        int sectionHeaderCount = -1;
        int sectionHeaderStringTableIndex = -1;

        if (classValue == 2 && endianValue == 1) {
            validateMinimumSize(binaryFile, ELF64_HEADER_SIZE, "64-bit Little Endian ELF");
            entryPoint = toHex(binaryFile.readLittleEndianLong(0x18));
            sectionHeaderOffset = binaryFile.readLittleEndianLong(0x28);
            sectionHeaderEntrySize = binaryFile.readLittleEndianShort(0x3A);
            sectionHeaderCount = binaryFile.readLittleEndianShort(0x3C);
            sectionHeaderStringTableIndex = binaryFile.readLittleEndianShort(0x3E);
        } else if (classValue == 1 && endianValue == 1) {
            validateMinimumSize(binaryFile, ELF32_HEADER_SIZE, "32-bit Little Endian ELF");
            entryPoint = toHex(binaryFile.readLittleEndianInt(0x18));
            sectionHeaderOffset = binaryFile.readLittleEndianInt(0x20);
            sectionHeaderEntrySize = binaryFile.readLittleEndianShort(0x2E);
            sectionHeaderCount = binaryFile.readLittleEndianShort(0x30);
            sectionHeaderStringTableIndex = binaryFile.readLittleEndianShort(0x32);
        }

        return new ElfHeader(
                true,
                bitClass,
                endian,
                architecture,
                entryPoint,
                sectionHeaderOffset,
                sectionHeaderEntrySize,
                sectionHeaderCount,
                sectionHeaderStringTableIndex
        );
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

    private static int readMachineValue(BinaryFile binaryFile, int endianValue) {
        if (binaryFile.size() < E_MACHINE_OFFSET + 2) {
            return -1;
        }

        if (endianValue == 1) {
            return binaryFile.readLittleEndianShort(E_MACHINE_OFFSET);
        }
        if (endianValue == 2) {
            return binaryFile.readBigEndianShort(E_MACHINE_OFFSET);
        }
        return -1;
    }

    private static String parseArchitecture(int machineValue) {
        if (machineValue == 0x3E) {
            return "x86-64";
        }
        if (machineValue == 0x03) {
            return "x86";
        }
        if (machineValue == 0xB7) {
            return "AArch64";
        }
        if (machineValue == 0x28) {
            return "ARM";
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

    public String getArchitecture() {
        return architecture;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public long getSectionHeaderOffset() {
        return sectionHeaderOffset;
    }

    public int getSectionHeaderEntrySize() {
        return sectionHeaderEntrySize;
    }

    public int getSectionHeaderCount() {
        return sectionHeaderCount;
    }

    public int getSectionHeaderStringTableIndex() {
        return sectionHeaderStringTableIndex;
    }
}
