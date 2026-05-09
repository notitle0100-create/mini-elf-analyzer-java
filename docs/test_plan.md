# 테스트 계획

## 테스트 환경

- OS: Ubuntu
- 프로젝트: Java SE 기반 CLI Mini ELF Analyzer
- 외부 라이브러리: 사용하지 않음
- 외부 프레임워크: 사용하지 않음

## 컴파일 및 실행 명령어

컴파일 명령어:

```bash
javac src/*.java
```

실행 명령어:

```bash
java -cp src Main
```

## 테스트 결과

| 번호 | 테스트 입력 | 기대 결과 | 실제 결과 | 판정 |
|---|---|---|---|---|
| 1 | `/bin/ls` | `ELF File: true`, `Class: 64-bit`, `Endian: Little Endian`, `Architecture: x86-64` | `Architecture: x86-64`, `Entry Point: 0x107fe0`, `Section Header Count: 34`, `Stripped Estimate: Stripped (추정)`, `Function Count Estimate: N/A` 출력. Section 이름 일부가 `readelf -S /bin/ls` 결과와 일치 | 통과 |
| 2 | `sample.txt` | `ELF File: false` | `ELF File: false`, `Class: N/A`, `Endian: N/A`, `Architecture: N/A`, `Entry Point: N/A`, `Section Header Count: N/A`, `Stripped Estimate: N/A`, `Function Count Estimate: N/A` 출력 | 통과 |
| 3 | `notfound.bin` | 파일 없음 오류 메시지 | `[ERROR] 파일이 존재하지 않습니다: notfound.bin` 출력 | 통과 |

## readelf 비교 결과

`readelf -h /bin/ls` 결과와 Java 프로그램의 `/bin/ls` 분석 결과를 비교한다.

| 비교 항목 | Java 프로그램 결과 | readelf 결과 | 판정 |
|---|---|---|---|
| Architecture | `x86-64` | `Machine: Advanced Micro Devices X86-64` | 일치 |
| Entry Point | `0x107fe0` | `Entry point address: 0x107fe0` | 일치 |
| Section Header Count | `34` | `Number of section headers: 34` | 일치 |

## readelf Section 비교 결과

`readelf -S /bin/ls` 결과와 Java 프로그램의 `Section Summary` 및 `주요 Sections` 출력을 비교한다.

| Section 이름 | Java 프로그램 결과 | readelf -S 결과 | 판정 |
|---|---|---|---|
| `.text` | 출력됨 | 출력됨 | 일치 |
| `.rodata` | 출력됨 | 출력됨 | 일치 |
| `.data` | 출력됨 | 출력됨 | 일치 |
| `.bss` | 출력됨 | 출력됨 | 일치 |
| `.dynsym` | 출력됨 | 출력됨 | 일치 |
| `.dynstr` | 출력됨 | 출력됨 | 일치 |
| `.shstrtab` | 출력됨 | 출력됨 | 일치 |

## 제한 사항 확인

- Section Header Table 상세 파싱은 64-bit Little Endian ELF 기준으로 수행한다.
- 32-bit ELF와 Big Endian ELF는 ELF Header 식별은 가능하지만 Section 상세 파싱이 제한될 수 있다.
- stripped binary처럼 `.symtab` Section이 없는 경우 Function Count Estimate는 `N/A`로 출력된다.
- Function Count Estimate는 `.symtab`의 `STT_FUNC` 타입 Symbol 개수를 기준으로 한 추정값이며 정밀한 Symbol Table 분석은 아니다.

## 수동 테스트 절차

1. `javac src/*.java` 명령으로 컴파일한다.
2. `java -cp src Main` 명령으로 프로그램을 실행한다.
3. 프롬프트에 `/bin/ls`, `sample.txt`, `notfound.bin`을 각각 입력한다.
4. 콘솔 출력과 `reports/analysis_report.md` 내용을 확인한다.
5. 정상 분석 가능한 입력에서는 `reports/analysis_report.md` 파일이 생성되는지 확인한다.

## 보고서 확인 항목

- ELF Header 항목이 실제 분석 결과와 일치하는지 확인한다.
- 비ELF 파일은 Class, Endian, Architecture, Entry Point, Section Header Count, Stripped Estimate, Function Count Estimate가 `N/A`로 출력되는지 확인한다.
- Architecture 항목이 ELF Header의 `e_machine` 값을 기준으로 출력되는지 확인한다.
- Stripped Estimate가 `.symtab` Section 존재 여부를 기준으로 `추정` 문구와 함께 출력되는지 확인한다.
- Function Count Estimate가 `.symtab`이 없을 때 `N/A`로 출력되는지 확인한다.
- Markdown 보고서에 `Section Summary` 표와 `Analysis Metadata` 항목이 출력되는지 확인한다.
- Suspicious Strings가 `Found` / `Not Found` 표 형태로 출력되는지 확인한다.
- Extracted Strings가 많은 경우 보고서에는 처음 20개 문자열만 표시되는지 확인한다.
