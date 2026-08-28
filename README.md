# Quote-Collection

## 빈 패키지 안내

현재 디렉터리의 `.gitkeep` 파일은 빈 패키지를 Git에 포함하기 위한 임시 파일입니다.

해당 패키지에 Java 파일을 추가한 후에는 `.gitkeep` 파일을 삭제해 주세요.

## 브랜치 활용
### `main`

- 배포 가능한 안정적인 코드를 관리하는 브랜치입니다.
- 직접 작업하거나 커밋하지 않습니다.
- 개발이 완료되고 검증된 변경 사항만 `develop` 브랜치에서 병합합니다.

### `develop`

- 개발 중인 기능을 통합하고 테스트하는 브랜치입니다.
- 새로운 기능이나 버그 수정은 `develop` 브랜치를 기준으로 별도의 작업 브랜치를 생성합니다.
- 작업이 완료되면 Pull Request를 통해 `develop` 브랜치에 병합합니다.
- 충분한 테스트와 검증이 끝나면 `main` 브랜치에 병합합니다.

### 개발 시작 전 초기 세팅

- IntelliJ Project 설정에 들어가 encode 검색
  - File Encodings 탭에 들어가
    - Global Encoding : UTF-8
    - Project Encoding : &lt;system default: UTF-8&gt;
    - Transparent native-to-ascii Conversion 체크합니다.
- MySQL 접속
  - IntelliJ 왼쪽 데이터페이스 탭 클릭
  - new 클릭 후 MySQL 선택
  - root 계정으로 접속 후 
    - sql 폴더의 CREATE_USER_DATABASE.sql 파일의 쿼리 한줄 씩 실행합니다.
  - root 계정에서 만든 계정 접속
    - CREATE_USER_DATABASE.sql 파일의 맨 아래 USE 쿼리 실행합니다.
    - CREATE_TABLE_SCRIPT.sql 파일의 쿼리 전체 실행하여 테이블 생성합니다.
    - INSERT_DATA_SCRIPT.sql 파일의 쿼리 전체 실행하여 초기 데이터 생성합니다.
  - build gradle 설정
    - /project/Quote_Collection/build.gradle 파일 확인
      - implementation("com.mysql:mysql-connector-j:9.3.0") 가 추가되어있는지 확인 후 안돼있다면 추가 후 Sync 합니다.