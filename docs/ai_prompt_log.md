# AI 협업 기록

| 날짜 | 목적 | 사용자 프롬프트 요약 | AI 제안 | 팀원 검토 및 반영 내용 |
|---|---|---|---|---|
| 2026-05-09 | 프로젝트 주제 선정 | 보안/리버싱 진로에 맞는 Java SE 기반 프로젝트 주제 요청 | ELF 파일 구조를 정적으로 확인하는 Mini ELF Analyzer 방향 제안 | 팀원이 Java SE 수업 범위와 보안 진로 연관성을 검토하고 후보로 채택 |
| 2026-05-09 | 후보 프로젝트 비교 | ELF Analyzer, 보안 로그 분석기 등 여러 후보 비교 요청 | 구현 난이도, 보안 연관성, 제출 적합성을 기준으로 장단점 비교 | 팀원이 과도한 범위의 기능을 제외하고 ELF Analyzer를 우선 후보로 결정 |
| 2026-05-09 | 구현 범위 축소 | readelf 전체 구현이 아닌 신입생 수준의 범위 설정 요청 | ELF Magic Number, Class, Endian, Entry Point, Section Header Count, 문자열 추출 중심으로 축소 제안 | 팀원이 Symbol Table, Relocation, PLT/GOT 정밀 분석 제외를 확정 |
| 2026-05-09 | 클래스 구조 설계 | Java SE CLI 구조와 클래스 분리 방법 요청 | `Main`, `ElfAnalyzer`, `ElfHeader`, `BinaryFile`, `StringExtractor`, `SuspiciousStringDetector`, `ReportGenerator`, `AnalysisResult` 구조 제안 | 팀원이 역할별 클래스 분리를 검토하고 읽기 쉬운 구조로 반영 |
| 2026-05-09 | Codex 1차 구현 | 설계한 구조를 바탕으로 CLI Mini ELF Analyzer 구현 요청 | Java SE 표준 라이브러리만 사용해 파일 검증, ELF Header 파싱, 문자열 추출, Markdown 보고서 생성 코드 작성 | 팀원이 외부 라이브러리, Maven/Gradle, 프레임워크 미사용 조건을 확인 |
| 2026-05-09 | Ubuntu ELF 테스트 | Ubuntu 환경에서 `/bin/ls`를 입력해 실행 테스트 요청 | `/bin/ls`를 byte 배열로 읽고 ELF 여부, 64-bit, Little Endian, Entry Point, Section Header Count 출력 확인 | 팀원이 ELF 파일을 실행하지 않고 정적으로 분석되는 것을 확인 |
| 2026-05-09 | readelf 비교 검증 | `readelf -h /bin/ls` 결과와 Java 프로그램 결과 비교 요청 | Entry Point `0x107fe0`, Section Header Count `34`가 일치함을 확인 | 팀원이 Java 파싱 결과와 시스템 도구 결과가 일치하는 것을 검증 |
| 2026-05-09 | 예외 처리 검증 | `sample.txt`와 `notfound.bin` 입력 시 동작 확인 요청 | 비ELF 파일은 `ELF File: false`, 존재하지 않는 파일은 오류 메시지 출력으로 처리 제안 | 팀원이 `sample.txt`와 `notfound.bin` 테스트 결과를 확인 |
| 2026-05-09 | GitHub 제출 준비 | GitHub repository 생성, Ubuntu clone, push 후 제출용 정리 요청 | README, 테스트 계획, AI 협업 기록, 보고서 출력 형식 정리 제안 | 팀원이 GitHub push 완료 후 제출 문서 보완 작업으로 반영 |
