# Mini ELF Analyzer Report

## 1. File Information
- File Path: `/bin/ls`
- File Size: 11352352 bytes

## 2. ELF Header
- ELF File: true
- Class: 64-bit
- Endian: Little Endian
- Entry Point: 0x107fe0
- Section Header Count: 34

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
입력 파일은 64-bit, Little Endian ELF 파일로 식별되었습니다. Entry Point와 Section Header Count를 ELF Header에서 정적으로 추출했습니다. 지정된 의심 문자열 중 flag, password, /bin/sh, system 항목이 발견되었습니다.
