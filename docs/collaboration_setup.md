# 협업 환경 설정

## 1. Notion

생성된 Notion 항목:

- 프로젝트 허브: https://www.notion.so/35a31486bf14810baecaf43fe25f6468
- 작업 보드 데이터베이스: https://www.notion.so/94728fe5c4f144b8af7afb3179cf8394

작업 보드에는 다음 view가 포함되어 있다.

- `Status Board`: 상태별 보드
- `High Priority`: 우선순위 High 작업 목록

## 2. Discord

현재 Codex에는 Discord 서버를 직접 생성할 수 있는 연결 도구가 없다.
아래 설정으로 Discord에서 서버를 생성한다.

확인 결과:

- Discord 데스크톱 앱은 실행 중이며 응답 상태이다.
- 확인된 창 제목: `친구 - Discord`
- Codex에서 Discord 창을 직접 클릭하거나 서버를 생성하는 도구는 현재 제공되지 않는다.

- 서버 이름: `Java SE Mini ELF Analyzer`
- 역할:
  - `팀원`
  - `리뷰어`
- 채널:
  - `#공지`
  - `#일정`
  - `#개발`
  - `#테스트`
  - `#자료`
  - `#질문`

고정 메시지에 포함할 내용:

```text
프로젝트명: Java SE 기반 CLI Mini ELF Analyzer
실행 방법:
javac src/*.java
java -cp src Main

Notion 프로젝트 허브:
https://www.notion.so/35a31486bf14810baecaf43fe25f6468
```

## 3. Git

현재 Windows 환경에서는 시스템 `git` 명령이 인식되지 않아 Portable Git을 임시 폴더에 받아 사용했다.

생성된 Git 상태:

- Branch: `main`
- Initial commit: `4257782 Initial Mini ELF Analyzer implementation`

Ubuntu 또는 WSL Ubuntu에서 새로 이어 작업할 때는 다음 명령으로 Git을 설치할 수 있다.

```bash
sudo apt update
sudo apt install git

cd ~/mini-elf-analyzer
git status
```

GitHub 원격 저장소를 만든 뒤에는 다음 명령을 실행한다.

```bash
git branch -M main
git remote add origin <GITHUB_REPOSITORY_URL>
git push -u origin main
```

제출 전 확인:

```bash
git status
```

`.gitignore`에 `*.class`, `out/`, `bin/`, `target/`, IDE 설정 폴더가 포함되어 있으므로 컴파일 산출물은 커밋하지 않는다.
