import java.util.List;

public class SymbolTableAnalyzer {
    private static final int STT_FUNC = 2;
    private static final int SHN_UNDEF = 0;
    private static final long ELF64_SYMBOL_ENTRY_SIZE = 24;

    public String estimateFunctionCount(BinaryFile binaryFile, List<SectionHeader> sections) {
        SectionHeader symbolTable = findSection(sections, ".symtab");

        if (symbolTable == null) {
            return "N/A";
        }

        long entrySize = symbolTable.getEntrySize();
        if (entrySize <= 0) {
            entrySize = ELF64_SYMBOL_ENTRY_SIZE;
        }

        if (entrySize < ELF64_SYMBOL_ENTRY_SIZE
                || !isValidRange(binaryFile.size(), symbolTable.getOffset(), symbolTable.getSize())) {
            return "N/A";
        }

        long entryCount = symbolTable.getSize() / entrySize;
        int functionCount = 0;

        for (long i = 0; i < entryCount; i++) {
            long entryOffset = symbolTable.getOffset() + (i * entrySize);

            if (!isValidRange(binaryFile.size(), entryOffset, ELF64_SYMBOL_ENTRY_SIZE)) {
                return "N/A";
            }

            int info = binaryFile.readUnsignedByte((int) entryOffset + 4);
            int sectionIndex = binaryFile.readLittleEndianShort((int) entryOffset + 6);
            int symbolType = info & 0x0F;

            if (symbolType == STT_FUNC && sectionIndex != SHN_UNDEF) {
                functionCount++;
            }
        }

        return functionCount + " (추정)";
    }

    private SectionHeader findSection(List<SectionHeader> sections, String sectionName) {
        for (SectionHeader section : sections) {
            if (sectionName.equals(section.getName())) {
                return section;
            }
        }

        return null;
    }

    private boolean isValidRange(long fileSize, long offset, long size) {
        if (offset < 0 || size < 0) {
            return false;
        }
        return offset <= fileSize && size <= fileSize - offset;
    }
}
