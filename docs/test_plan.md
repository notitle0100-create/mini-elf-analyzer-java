# 테스트 계획

## 테스트 케이스

| 번호 | 테스트 항목 | 입력 예시 | 예상 결과 |
|---|---|---|---|
| 1 | 존재하지 않는 파일 | `not_found` | 파일이 존재하지 않는다는 오류 메시지를 출력하고 분석을 중단한다. |
| 2 | 디렉터리 입력 | `src` | 일반 파일이 아니라는 오류 메시지를 출력하고 분석을 중단한다. |
| 3 | txt 파일 입력 | `sample.txt` | ELF Magic Number가 맞지 않아 `ELF 파일이 아님`을 출력한다. |
| 4 | 정상 ELF 파일 입력 | Linux ELF 실행 파일 경로 | ELF 여부, Class, Endian, Entry Point, Section Header Count를 출력한다. |
| 5 | 의심 문자열 포함 파일 | `flag`, `password`, `/bin/sh`, `system`, `execve` 포함 파일 | 추출 문자열 목록과 의심 문자열 탐지 결과에 `Found`가 표시된다. |
| 6 | Markdown 보고서 생성 확인 | 정상 분석 가능한 파일 | `reports/analysis_report.md` 파일이 생성되고 분석 결과가 저장된다. |

## 수동 테스트 절차

1. `javac src/*.java` 명령으로 컴파일한다.
2. `java -cp src Main` 명령으로 프로그램을 실행한다.
3. 프롬프트에 테스트할 파일 경로를 입력한다.
4. 콘솔 출력과 `reports/analysis_report.md` 내용을 확인한다.
