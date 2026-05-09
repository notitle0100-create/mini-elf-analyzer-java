# Mini ELF Analyzer Report

## 1. File Information
- File Path: `/home/soke/mini-elf-analyzer-java/sample.txt`
- File Size: 54 bytes

## 2. ELF Header
- ELF File: false
- Class: N/A
- Endian: N/A
- Entry Point: N/A
- Section Header Count: N/A

## 3. Extracted Strings
- this is a normal text file used for non elf test case

## 4. Suspicious Strings
| Keyword | Result |
|---|---|
| flag | Not Found |
| password | Not Found |
| /bin/sh | Not Found |
| system | Not Found |
| execve | Not Found |

## 5. Summary
입력 파일의 ELF Magic Number가 일치하지 않아 ELF 파일이 아닌 것으로 판단됩니다.
