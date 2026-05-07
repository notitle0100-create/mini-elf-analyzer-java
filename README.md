# Java SE 기반 CLI Mini ELF Analyzer

## 프로젝트 설명

Java SE만 사용해 리눅스 ELF 실행 파일을 정적으로 분석하는 CLI 기반 Mini ELF Analyzer입니다. 파일을 실행하지 않고 byte 배열로 읽어 ELF Magic Number, bit class, endian, Entry Point, Section Header Count, readable string, 의심 문자열을 확인합니다.

외부 라이브러리, Spring, Hibernate, 외부 DB, Maven, Gradle은 사용하지 않습니다.

## 주요 기능

- 사용자로부터 ELF 파일 경로 입력
- 파일 존재 여부 및 일반 파일 여부 확인
- 파일 크기 확인
- 파일을 byte 배열로 읽기
- ELF Magic Number 확인 (`0x7F 45 4C 46`)
- 32-bit / 64-bit 판별
- Little Endian / Big Endian 판별
- Entry Point 추출
  - 64-bit Little Endian ELF 중심 구현
  - 32-bit Little Endian ELF 기본 파싱 포함
  - Big Endian은 식별만 하고 상세 파싱 제한
- Section Header Count 출력
- printable ASCII 기준 readable string 추출
  - 최소 길이 4 이상
- 의심 문자열 탐지
  - `flag`
  - `password`
  - `/bin/sh`
  - `system`
  - `execve`
- Markdown 분석 보고서 생성
  - `reports/analysis_report.md`

## 실행 방법

```bash
javac src/*.java
java -cp src Main
```

실행 후 프롬프트가 표시되면 분석할 파일 경로를 입력합니다.

```text
[Mini ELF Analyzer]
분석할 파일 경로를 입력하세요: sample
```

`javac` 명령이 인식되지 않으면 JDK 설치 여부와 `JAVA_HOME`, `PATH` 설정을 확인합니다.

## 테스트 방법

```bash
javac src/*.java
java -cp src Main
```

다음 항목을 기준으로 테스트합니다.

- 존재하지 않는 파일 입력
- 디렉터리 경로 입력
- txt 파일 입력
- 정상 ELF 파일 입력
- `flag`, `password`, `/bin/sh`, `system`, `execve` 문자열이 포함된 테스트 파일 입력
- `reports/analysis_report.md` 생성 여부 확인

자세한 테스트 케이스는 `docs/test_plan.md`를 참고합니다.

## 협업 환경

Notion, Discord, Git 설정 내용은 `docs/collaboration_setup.md`를 참고합니다.

## 팀원

- 202413084 허재승
- 202413086 우정호

## 기술 스택

- Java SE
- CLI
- File I/O
- Markdown

## 안전 범위

이 프로젝트는 정적 분석만 수행합니다. ELF 파일을 실행하지 않으며, 동적 분석, 공격 기능, 악성 행위 기능, PE/Mach-O 분석, Symbol Table 정밀 분석, Relocation 분석, PLT/GOT 분석은 구현하지 않습니다.
