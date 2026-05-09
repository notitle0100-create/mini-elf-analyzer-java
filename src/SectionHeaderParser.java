import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SectionHeaderParser {
    private static final int ELF64_SECTION_HEADER_MIN_SIZE = 64;

    public List<SectionHeader> parse(BinaryFile binaryFile, ElfHeader elfHeader) {
        if (!canParseSections(binaryFile, elfHeader)) {
            return Collections.emptyList();
        }

        List<SectionHeader> sections = readSectionHeaders(binaryFile, elfHeader);

        if (sections.isEmpty()) {
            return sections;
        }

        return applySectionNames(binaryFile, elfHeader, sections);
    }

    private boolean canParseSections(BinaryFile binaryFile, ElfHeader elfHeader) {
        if (!elfHeader.isElfFile()) {
            return false;
        }
        if (!"64-bit".equals(elfHeader.getBitClass())
                || !"Little Endian".equals(elfHeader.getEndian())) {
            return false;
        }
        if (elfHeader.getSectionHeaderOffset() < 0
                || elfHeader.getSectionHeaderEntrySize() < ELF64_SECTION_HEADER_MIN_SIZE
                || elfHeader.getSectionHeaderCount() <= 0) {
            return false;
        }

        long tableSize = (long) elfHeader.getSectionHeaderEntrySize()
                * elfHeader.getSectionHeaderCount();
        return isValidRange(binaryFile.size(), elfHeader.getSectionHeaderOffset(), tableSize);
    }

    private List<SectionHeader> readSectionHeaders(BinaryFile binaryFile, ElfHeader elfHeader) {
        List<SectionHeader> sections = new ArrayList<SectionHeader>();
        long tableOffset = elfHeader.getSectionHeaderOffset();
        int entrySize = elfHeader.getSectionHeaderEntrySize();

        for (int i = 0; i < elfHeader.getSectionHeaderCount(); i++) {
            long entryOffset = tableOffset + ((long) i * entrySize);

            if (!isValidRange(binaryFile.size(), entryOffset, ELF64_SECTION_HEADER_MIN_SIZE)) {
                return Collections.emptyList();
            }

            sections.add(readSectionHeader(binaryFile, entryOffset));
        }

        return sections;
    }

    private SectionHeader readSectionHeader(BinaryFile binaryFile, long entryOffset) {
        int base = (int) entryOffset;
        int nameOffset = (int) binaryFile.readLittleEndianInt(base);
        long type = binaryFile.readLittleEndianInt(base + 0x04);
        long offset = binaryFile.readLittleEndianLong(base + 0x18);
        long size = binaryFile.readLittleEndianLong(base + 0x20);
        int link = (int) binaryFile.readLittleEndianInt(base + 0x28);
        long entrySize = binaryFile.readLittleEndianLong(base + 0x38);

        return new SectionHeader(nameOffset, "", type, offset, size, link, entrySize);
    }

    private List<SectionHeader> applySectionNames(BinaryFile binaryFile, ElfHeader elfHeader,
                                                  List<SectionHeader> sections) {
        int shstrtabIndex = elfHeader.getSectionHeaderStringTableIndex();

        if (shstrtabIndex < 0 || shstrtabIndex >= sections.size()) {
            return sections;
        }

        SectionHeader stringTable = sections.get(shstrtabIndex);
        if (!isValidRange(binaryFile.size(), stringTable.getOffset(), stringTable.getSize())) {
            return sections;
        }

        byte[] data = binaryFile.getBytes();
        int tableStart = (int) stringTable.getOffset();
        int tableEnd = tableStart + (int) stringTable.getSize();
        List<SectionHeader> namedSections = new ArrayList<SectionHeader>();

        for (SectionHeader section : sections) {
            String name = readNullTerminatedString(data, tableStart, tableEnd, section.getNameOffset());
            namedSections.add(section.withName(name));
        }

        return namedSections;
    }

    private String readNullTerminatedString(byte[] data, int tableStart, int tableEnd, int nameOffset) {
        long namePosition = (long) tableStart + nameOffset;

        if (nameOffset < 0 || namePosition >= tableEnd) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        int index = (int) namePosition;

        while (index < tableEnd && data[index] != 0) {
            int value = data[index] & 0xFF;
            if (value < 0x20 || value > 0x7E) {
                break;
            }
            builder.append((char) value);
            index++;
        }

        return builder.toString();
    }

    private boolean isValidRange(long fileSize, long offset, long size) {
        if (offset < 0 || size < 0) {
            return false;
        }
        return offset <= fileSize && size <= fileSize - offset;
    }
}
