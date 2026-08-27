-- ------------------------------------------------------------------
-- 계정 생성 후 데이터베이스 활용
-- ------------------------------------------------------------------

-- 계정 생성은 root에서 생성합니다.

-- 1) 현재 접속한 데이터베이스(=스키마) 확인
-- mysql이 출력될 것이며, 만약 지정된 DB가 없다면 NULL이 출력됨.
SELECT DATABASE();

-- 만약, 위에서 mysql이 나오지 않는다면, 아래 SQL을 실행해 작업할 데이터베이스를 mysql로 선택.
USE mysql;

-- 2) 프로젝트에서 사용할 quotecollection 계정 새로 만들기
-- 모든 호스트에서 접속을 허용하기 위해 '%'(와일드 카드) 사용
CREATE USER 'quotecollection'@'%' IDENTIFIED BY  'quotecollection';

-- quotecollection 계정이 잘 생성되었는지 확인
SELECT user, host FROM user;

-- 3) 데이터베이스 생성 후 계정에 권한 부여
-- 프로젝트에 사용할 quotedb 데이터베이스 생성
CREATE DATABASE quotedb;

-- 데이터베이스 목록을 확인해 quotedb 존재하는지 확인
SHOW DATABASES;

-- 4) 생성한 quotedb 데이터베이스에 대하여 quotecollection 유저에게 모든 권한을 부여
GRANT ALL PRIVILEGES ON quotedb.* TO 'quotecollection'@'%';

-- quotecollection 유저에게 부여된 권한 확인 (2개 행이 조회되면 됨)
SHOW GRANTS FOR 'quotecollection'@'%';

-- 5) 새롭게 생성한 quotecollection 계정으로 접속 후
USE quotedb;