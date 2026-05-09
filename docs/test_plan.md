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
| 1 | `/bin/ls` | `ELF File: true`, `Class: 64-bit`, `Endian: Little Endian` | `Entry Point: 0x107fe0`, `Section Header Count: 34` 출력. `readelf -h /bin/ls` 결과와 비교하여 일치 | 통과 |
| 2 | `sample.txt` | `ELF File: false` | `ELF File: false`, `Class: N/A`, `Endian: N/A`, `Entry Point: N/A`, `Section Header Count: N/A` 출력 | 통과 |
| 3 | `notfound.bin` | 파일 없음 오류 메시지 | `[ERROR] 파일이 존재하지 않습니다: notfound.bin` 출력 | 통과 |

## 수동 테스트 절차

1. `javac src/*.java` 명령으로 컴파일한다.
2. `java -cp src Main` 명령으로 프로그램을 실행한다.
3. 프롬프트에 `/bin/ls`, `sample.txt`, `notfound.bin`을 각각 입력한다.
4. 콘솔 출력과 `reports/analysis_report.md` 내용을 확인한다.
5. 정상 분석 가능한 입력에서는 `reports/analysis_report.md` 파일이 생성되는지 확인한다.

## 보고서 확인 항목

- ELF Header 항목이 실제 분석 결과와 일치하는지 확인한다.
- 비ELF 파일은 Class, Endian, Entry Point, Section Header Count가 `N/A`로 출력되는지 확인한다.
- Suspicious Strings가 `Found` / `Not Found` 표 형태로 출력되는지 확인한다.
- Extracted Strings가 많은 경우 보고서에는 처음 20개 문자열만 표시되는지 확인한다.
