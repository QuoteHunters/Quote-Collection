-- 테이블 생성할 때 혹시 모를 문제 방지를 위해 모든 테이블 삭제
DROP TABLE IF EXISTS bookmark;
DROP TABLE IF EXISTS quote;
DROP TABLE IF EXISTS person;
DROP TABLE IF EXISTS theme;
DROP TABLE IF EXISTS country;
DROP TABLE IF EXISTS period;
DROP TABLE IF EXISTS field;
DROP TABLE IF EXISTS quote_user;

-- 테이블 생성
CREATE TABLE IF NOT EXISTS quote_user (
    member_id INT AUTO_INCREMENT COMMENT '회원 고유 번호',
    user_id VARCHAR(30) NOT NULL COMMENT '회원 ID',
    user_pw varchar(20) NOT NULL COMMENT '회원 PW',
    user_auth INT NOT NULL COMMENT '회원 권한 (0: 관리자 / 1: 사용자)',

    CONSTRAINT pk_member_id PRIMARY KEY ( member_id ),
    CONSTRAINT uq_user_id UNIQUE ( user_id ),
    CONSTRAINT ck_user_auth CHECK ( user_auth IN (0, 1))
) ENGINE=INNODB COMMENT '회원 관리';

CREATE TABLE IF NOT EXISTS country (
    country_id INT AUTO_INCREMENT COMMENT '국가 고유 번호',
    country_name VARCHAR(30) NOT NULL COMMENT '국가명',

    CONSTRAINT pk_country_id PRIMARY KEY ( country_id ),
    CONSTRAINT uq_country_name UNIQUE ( country_name )
) ENGINE=INNODB COMMENT '국가';

CREATE TABLE IF NOT EXISTS period (
    period_id INT AUTO_INCREMENT COMMENT '시대 고유 번호',
    period_name VARCHAR(20) NOT NULL COMMENT '시대명',

    CONSTRAINT pk_period_id PRIMARY KEY ( period_id),
    CONSTRAINT uq_period_name UNIQUE ( period_name )
) ENGINE=INNODB COMMENT '시대';

CREATE TABLE IF NOT EXISTS field (
    field_id INT AUTO_INCREMENT COMMENT '분야 고유 번호',
    field_name VARCHAR(10) NOT NULL COMMENT '분야명',

    CONSTRAINT pk_field_id PRIMARY KEY ( field_id ),
    CONSTRAINT uq_field_name UNIQUE ( field_name)
) ENGINE=INNODB COMMENT '분야';

CREATE TABLE IF NOT EXISTS person (
    person_id INT AUTO_INCREMENT COMMENT '인물 고유 번호',
    country_id INT NOT NULL COMMENT '국가 고유 번호',
    period_id INT NOT NULL COMMENT '시대 고유 번호',
    field_id INT NOT NULL COMMENT '분야 고유 번호',
    person_name VARCHAR(50) NOT NULL COMMENT '인물 이름',

    CONSTRAINT pk_person_id PRIMARY KEY ( person_id ),
    CONSTRAINT fk_country_id FOREIGN KEY ( country_id ) REFERENCES country ( country_id ) ON DELETE CASCADE,
    CONSTRAINT fk_period_id FOREIGN KEY ( period_id ) REFERENCES period ( period_id ) ON DELETE CASCADE,
    CONSTRAINT fk_field_id FOREIGN KEY ( field_id ) REFERENCES field ( field_id ) ON DELETE CASCADE
) ENGINE=INNODB COMMENT '인물';

CREATE TABLE IF NOT EXISTS theme (
    theme_id INT AUTO_INCREMENT COMMENT '주제 고유 번호',
    theme_name VARCHAR(10) NOT NULL COMMENT '주제명',

    CONSTRAINT pk_theme_id PRIMARY KEY ( theme_id ),
    CONSTRAINT uq_theme_name UNIQUE ( theme_name )
) ENGINE=INNODB COMMENT '주제';

CREATE TABLE IF NOT EXISTS quote (
    quote_id INT AUTO_INCREMENT COMMENT '명언 고유 번호',
    theme_id INT NOT NULL COMMENT '주제 고유 번호',
    person_id INT NOT NULL COMMENT '인물 고유 번호',
    quote_content VARCHAR(255) NOT NULL COMMENT '명언 내용',

    CONSTRAINT pk_quote_id PRIMARY KEY ( quote_id ),
    CONSTRAINT fk_theme_id FOREIGN KEY ( theme_id ) REFERENCES theme ( theme_id ) ON DELETE CASCADE,
    CONSTRAINT fk_person_id FOREIGN KEY ( person_id ) REFERENCES person ( person_id ) ON DELETE CASCADE
) ENGINE=INNODB COMMENT '명언';

CREATE TABLE IF NOT EXISTS bookmark (
    bookmark_id INT AUTO_INCREMENT COMMENT '즐겨찾기 고유 번호',
    member_id INT NOT NULL COMMENT '회원 고유 번호',
    quote_id INT NOT NULL COMMENT '명언 고유 번호',
    create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 일시',  -- ON UPDATE는 AUTO_INCREMENT처럼 UPDATE 시 자동으로 시간이 들어감

    CONSTRAINT pk_bookmark_id PRIMARY KEY ( bookmark_id ),
    CONSTRAINT fk_member_id FOREIGN KEY ( member_id ) REFERENCES quote_user ( member_id ) ON DELETE CASCADE,
    CONSTRAINT fk_quote_id FOREIGN KEY ( quote_id ) REFERENCES quote ( quote_id ) ON DELETE CASCADE,
    CONSTRAINT uq_bookmark_member_quote UNIQUE (member_id, quote_id)  -- 이렇게 member_id와 quote_id를 동시에 unique로 두게 되면 member_id와 quote_id의 조합이 중복되는 것을 막음
) ENGINE=INNODB COMMENT '즐겨찾기';

-- COMMIT이 안됨을 방지하기 위해 COMMIT 쿼리 실행
COMMIT;