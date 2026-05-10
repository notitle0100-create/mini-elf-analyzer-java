# Java SE 기반 CLI Mini ELF Analyzer

## 프로젝트 설명

Java SE만 사용해 리눅스 ELF 파일을 정적으로 확인하는 CLI 기반 Mini ELF Analyzer입니다.
분석 대상 파일을 실행하지 않고 byte 배열로 읽은 뒤 ELF Magic Number, Class, Endian, Architecture, Entry Point, Section Header Count, Section Header Table, printable ASCII 문자열, 의심 문자열 포함 여부, stripped 여부 추정 결과를 출력합니다.

이 프로젝트는 학습용 정적 분석 도구이며 `readelf` 전체 기능이나 `checksec` 기능을 구현하지 않습니다.

## 팀원

- 202413084 허재승
- 202413086 우정호

## 팀원 역할 분담

| 팀원 | 담당 역할 | 세부 수행 내용 |
|---|---|---|
| 허재승 | 프로젝트 총괄 및 핵심 기능 구현 | 주제 선정, 요구사항 정의, GitHub 관리, 전체 클래스 구조 설계, ELF Header 분석, Architecture 감지, Section Header 파싱, stripped 여부 추정, Function Count Estimate 구현, 의심 문자열 탐지, Markdown 보고서 생성 기능 구현 및 통합 |
| 우정호 | 테스트 검증 및 기술 문서화 | GitHub 최신 코드 clone/pull 후 실행 검증, README 실행 방법 확인, `/bin/ls`, `sample.txt`, `notfound.bin` 테스트 결과 정리, ELF 관련 개념 조사, AI 협업 기록 정리, 최종 보고서 작성 보조 |
| 공통 | 최종 점검 및 제출 준비 | 테스트 결과 공유, 오류 수정 내용 검토, 최종 보고서 문장 정리, 제출 파일 점검 |

허재승은 프로젝트 주제 선정, 요구사항 정의, 전체 클래스 구조 설계, 핵심 기능 구현 및 GitHub 관리를 담당하였다. 주요 구현 내용은 ELF Magic Number 확인, 32bit/64bit 판별, Endian 확인, Entry Point 출력, Architecture 감지, Section Header 파싱, Section 이름/offset/size/type 출력, stripped 여부 추정, Function Count Estimate 구현, 의심 문자열 탐지, Markdown 보고서 생성 기능이다. 또한 Ubuntu 환경에서 `readelf` 결과와 프로그램 출력 결과를 비교하여 분석 정확도를 검증하였다.

우정호는 프로젝트의 테스트 검증 및 기술 문서화 역할을 담당하였다. GitHub 저장소의 최신 코드를 clone 또는 pull하여 README에 작성된 실행 방법대로 프로그램이 정상 실행되는지 확인하고, `/bin/ls`, `sample.txt`, `notfound.bin`을 이용한 테스트 결과를 정리하였다. 또한 ELF 파일 구조와 `readelf`, `strings` 등 관련 개념을 조사하고, AI 협업 기록과 최종 보고서 작성 보조를 담당하였다.

## 주요 기능

1. ELF Magic Number 확인 (`0x7F 45 4C 46`)
2. 32-bit / 64-bit 판별
3. Little Endian / Big Endian 판별
4. Entry Point 출력
   - 64-bit Little Endian ELF 중심 구현
   - 32-bit Little Endian ELF 기본 파싱 포함
   - Big Endian은 식별만 하고 상세 파싱 제한
5. Section Header Count 출력
6. Architecture 감지
   - `0x3E`: x86-64
   - `0x03`: x86
   - `0xB7`: AArch64
   - `0x28`: ARM
   - 그 외 값: Unknown
7. printable ASCII 기준 readable string 추출(최소 길이 4 이상)
8. 의심 문자열 탐지
   - `flag`
   - `password`
   - `/bin/sh`
   - `system`
   - `execve`
9. stripped 여부 추정
   - Section Header Table에 `.symtab` Section이 있으면 `Not Stripped (추정)`
   - Section Header Table에 `.symtab` Section이 없으면 `Stripped (추정)`
   - 64-bit Little Endian ELF에서 Section 기반으로 추정
10. Markdown 분석 보고서 생성
   - `reports/analysis_report.md`

추가 분석 기능:

- Section Header Table 파싱
  - 64-bit Little Endian ELF 기준 구현
  - Section 이름, Type, Offset, Size 출력
- 주요 Section 이름 출력
  - `.text`, `.data`, `.bss`, `.rodata`, `.dynsym`, `.dynstr`, `.symtab`, `.strtab`, `.shstrtab`
- `.symtab`이 있는 경우 Symbol Table의 `STT_FUNC` 항목 수를 이용해 Function Count Estimate 출력
  - stripped binary 또는 `.symtab`이 없는 경우 `N/A`
  - 정밀 분석이 아닌 추정값으로 표시

## 구현 범위 제한

- ELF 파일은 실행하지 않고 정적으로만 읽습니다.
- Java SE 표준 라이브러리만 사용합니다.
- 외부 라이브러리, 외부 프레임워크, 외부 DB를 사용하지 않습니다.
- Maven, Gradle 같은 빌드 도구를 추가하지 않습니다.
- Spring, Hibernate를 사용하지 않습니다.
- `readelf` 전체 기능을 구현하지 않습니다.
- `checksec` 기능을 구현하지 않습니다.
- Ghidra 또는 LLM 기반 분석을 수행하지 않습니다.
- Symbol Table 정밀 분석은 구현하지 않고 `.symtab` 기반 함수 개수 추정만 수행합니다.
- Relocation, PLT/GOT 정밀 분석은 구현하지 않습니다.

## 사용 기술

- Java SE
- CLI
- File I/O
- byte 배열 기반 정적 분석
- Markdown 보고서 생성

## 실행 방법

```bash
javac src/*.java
java -cp src Main
```

실행 후 프롬프트가 표시되면 분석할 파일 경로를 입력합니다.

```text
[Mini ELF Analyzer]
분석할 파일 경로를 입력하세요: /bin/ls
```

분석이 끝나면 콘솔에 요약 결과가 출력되고 `reports/analysis_report.md` 파일이 생성됩니다.
`javac` 명령이 인식되지 않으면 JDK 설치 여부와 `JAVA_HOME`, `PATH` 설정을 확인합니다.

## 테스트 방법

먼저 컴파일합니다.

```bash
javac src/*.java
```

이후 프로그램을 실행하고 프롬프트에 테스트 경로를 입력합니다.

```bash
java -cp src Main
```

### 테스트 1: 정상 ELF 파일

입력값:

```text
/bin/ls
```

확인 항목:

- `ELF File: true`
- `Class: 64-bit`
- `Endian: Little Endian`
- `Architecture: x86-64`
- `Entry Point: 0x107fe0`
- `Section Header Count: 34`
- `Stripped Estimate: Stripped (추정)`
- `Function Count Estimate: N/A`
- 주요 Sections에 `.text`, `.rodata`, `.data`, `.bss`, `.dynsym`, `.dynstr`, `.shstrtab` 등이 출력
- Markdown 보고서의 `Section Summary` 표에 Section Name, Type, Offset, Size 출력

`readelf -h /bin/ls`와 비교할 때 다음 항목을 확인합니다.

- Java `Architecture: x86-64` = readelf `Machine: Advanced Micro Devices X86-64`
- Java `Entry Point: 0x107fe0` = readelf `Entry point address: 0x107fe0`
- Java `Section Header Count: 34` = readelf `Number of section headers: 34`

`readelf -S /bin/ls`와 비교할 때 다음 Section 이름 일부가 Java 결과와 일치하는지 확인합니다.

- `.text`
- `.rodata`
- `.data`
- `.bss`
- `.dynsym`
- `.dynstr`
- `.shstrtab`

### 테스트 2: 비ELF 파일

입력값:

```text
sample.txt
```

확인 항목:

- `ELF File: false`
- `Class: N/A`
- `Endian: N/A`
- `Architecture: N/A`
- `Entry Point: N/A`
- `Section Header Count: N/A`
- `Stripped Estimate: N/A`
- `Function Count Estimate: N/A`

### 테스트 3: 존재하지 않는 파일

입력값:

```text
notfound.bin
```

확인 항목:

```text
[ERROR] 파일이 존재하지 않습니다: notfound.bin
```

자세한 테스트 케이스는 `docs/test_plan.md`를 참고합니다.

## 협업 환경

Notion, Discord, Git 설정 내용은 `docs/collaboration_setup.md`를 참고합니다.
