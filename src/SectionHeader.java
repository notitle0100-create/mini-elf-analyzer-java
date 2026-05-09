public class SectionHeader {
    private final int nameOffset;
    private final String name;
    private final long type;
    private final long offset;
    private final long size;
    private final int link;
    private final long entrySize;

    public SectionHeader(int nameOffset, String name, long type, long offset,
                         long size, int link, long entrySize) {
        this.nameOffset = nameOffset;
        this.name = name;
        this.type = type;
        this.offset = offset;
        this.size = size;
        this.link = link;
        this.entrySize = entrySize;
    }

    public SectionHeader withName(String sectionName) {
        return new SectionHeader(nameOffset, sectionName, type, offset, size, link, entrySize);
    }

    public int getNameOffset() {
        return nameOffset;
    }

    public String getName() {
        return name;
    }

    public long getType() {
        return type;
    }

    public String getTypeText() {
        if (type == 0) {
            return "NULL";
        }
        if (type == 1) {
            return "PROGBITS";
        }
        if (type == 2) {
            return "SYMTAB";
        }
        if (type == 3) {
            return "STRTAB";
        }
        if (type == 8) {
            return "NOBITS";
        }
        if (type == 11) {
            return "DYNSYM";
        }
        return "UNKNOWN(" + toHex(type) + ")";
    }

    public long getOffset() {
        return offset;
    }

    public long getSize() {
        return size;
    }

    public int getLink() {
        return link;
    }

    public long getEntrySize() {
        return entrySize;
    }

    public static String toHex(long value) {
        return "0x" + Long.toHexString(value);
    }
}
