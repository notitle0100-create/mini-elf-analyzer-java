# Mini ELF Analyzer Report

## 1. File Information
- File Path: `/bin/ls`
- File Size: 11352352 bytes

## 2. ELF Header
- ELF File: true
- Class: 64-bit
- Endian: Little Endian
- Architecture: x86-64
- Entry Point: 0x107fe0
- Section Header Count: 34
- Stripped Estimate: Stripped (추정)

## Analysis Metadata
- Architecture: x86-64
- Stripped Estimate: Stripped (추정)
- Function Count Estimate: N/A

## Section Summary
| Name | Type | Offset | Size |
|---|---|---:|---:|
|  | NULL | 0x0 | 0x0 |
| .note.gnu.build-id | UNKNOWN(0x7) | 0x388 | 0x24 |
| .interp | PROGBITS | 0x3ac | 0x1c |
| .gnu.hash | UNKNOWN(0x6ffffff6) | 0x3c8 | 0x48 |
| .dynsym | DYNSYM | 0x410 | 0x19f8 |
| .dynstr | STRTAB | 0x1e08 | 0xd5f |
| .gnu.version | UNKNOWN(0x6fffffff) | 0x2b68 | 0x22a |
| .gnu.version_r | UNKNOWN(0x6ffffffe) | 0x2d98 | 0x240 |
| .rela.dyn | UNKNOWN(0x4) | 0x2fd8 | 0xee050 |
| .rela.plt | UNKNOWN(0x4) | 0xf1028 | 0xa8 |
| .init | PROGBITS | 0xf2000 | 0x1b |
| .plt | PROGBITS | 0xf2020 | 0x80 |
| .plt.got | PROGBITS | 0xf20a0 | 0x48 |
| .text | PROGBITS | 0xf2100 | 0x4b75b0 |
| .fini | PROGBITS | 0x5a96b0 | 0xd |
| .rodata | PROGBITS | 0x5aa000 | 0x2b8ae8 |
| .debug_gdb_scripts | PROGBITS | 0x862ae8 | 0x22 |
| .eh_frame_hdr | PROGBITS | 0x862b0c | 0x2d03c |
| .eh_frame | PROGBITS | 0x88fb48 | 0xd5f08 |
| .gcc_except_table | PROGBITS | 0x965a50 | 0x2818 |
| .note.gnu.property | UNKNOWN(0x7) | 0x968268 | 0x20 |
| .note.ABI-tag | UNKNOWN(0x7) | 0x968288 | 0x20 |
| .note.package | UNKNOWN(0x7) | 0x9682a8 | 0x78 |
| .tdata | PROGBITS | 0x9686d0 | 0x198 |
| .tbss | NOBITS | 0x968868 | 0x131 |
| .init_array | UNKNOWN(0xe) | 0x968868 | 0x290 |
| .fini_array | UNKNOWN(0xf) | 0x968af8 | 0x8 |
| .data.rel.ro | PROGBITS | 0x968b00 | 0x14f408 |
| .dynamic | UNKNOWN(0x6) | 0xab7f08 | 0x230 |
| .got | PROGBITS | 0xab8138 | 0x14eb8 |
| .data | PROGBITS | 0xacd000 | 0x5f18 |
| .bss | NOBITS | 0xad2f18 | 0x410 |
| .gnu_debuglink | PROGBITS | 0xad2f18 | 0x34 |
| .shstrtab | STRTAB | 0xad2f4c | 0x154 |

## 3. Extracted Strings
- /lib64/ld-linux-x86-64.so.2
- _ITM_deregisterTMCloneTable
- __gmon_start__
- _ITM_registerTMCloneTable
- context_str
- lsetfilecon_raw
- getcon
- is_selinux_enabled
- setexeccon_raw
- context_type_set
- is_selinux_mls_enabled
- security_check_context_raw
- context_role_set
- freecon
- context_user_set
- setexeccon
- lgetfilecon
- security_compute_create_name
- getcon_raw
- lgetfilecon_raw

총 58048개 중 처음 20개 문자열만 표시합니다.

## 4. Suspicious Strings
| Keyword | Result |
|---|---|
| flag | Found |
| password | Found |
| /bin/sh | Found |
| system | Found |
| execve | Not Found |

## 5. Summary
입력 파일은 64-bit, Little Endian, x86-64 ELF 파일로 식별되었습니다. Entry Point와 Section Header Count를 ELF Header에서 정적으로 추출했습니다. 지정된 의심 문자열 중 flag, password, /bin/sh, system 항목이 발견되었습니다.
Stripped 여부는 .symtab Section 존재 여부를 기준으로 Stripped (추정)으로 판단했습니다. Function Count Estimate는 N/A입니다.
