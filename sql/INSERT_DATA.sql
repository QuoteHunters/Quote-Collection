-- 데이터 꼬임을 방지하기 위해 기존 데이터 삭제
DELETE FROM bookmark;
DELETE FROM quote;
DELETE FROM person;
DELETE FROM theme;
DELETE FROM country;
DELETE FROM period;
DELETE FROM field;
DELETE FROM quote_user;

-- PK의 AUTO_INCREMENT의 값을 1로 초기화
ALTER TABLE bookmark AUTO_INCREMENT = 1;
ALTER TABLE quote AUTO_INCREMENT = 1;
ALTER TABLE person AUTO_INCREMENT = 1;
ALTER TABLE theme AUTO_INCREMENT = 1;
ALTER TABLE country AUTO_INCREMENT = 1;
ALTER TABLE period AUTO_INCREMENT = 1;
ALTER TABLE field AUTO_INCREMENT = 1;
ALTER TABLE quote_user AUTO_INCREMENT = 1;

-- 초기 데이터 INSERT
INSERT INTO quote_user (user_id, user_pw, user_auth)
VALUES
    ('admin01',  'adminpw01', 0),
    ('admin02', 'adminpw02', 0),
    ('user01', 'password01', 1),
    ('user02', 'password02', 1),
    ('user03', 'password03', 1),
    ('user04', 'password04', 1);

INSERT INTO country (country_name)
VALUES
    ('대한민국'),
    ('미국'),
    ('영국'),
    ('프랑스'),
    ('독일'),
    ('이탈리아'),
    ('그리스'),
    ('중국'),
    ('일본'),
    ('인도');

INSERT INTO period (period_name)
VALUES
    ('BC 6세기'),
    ('BC 5세기'),
    ('BC 4세기'),
    ('AD 1세기'),
    ('AD 15세기'),
    ('AD 16세기'),
    ('AD 17세기'),
    ('AD 18세기'),
    ('AD 19세기'),
    ('AD 20세기');

INSERT INTO field (field_name)
VALUES
    ('철학'),
    ('정치'),
    ('과학'),
    ('문학'),
    ('예술'),
    ('종교'),
    ('경제'),
    ('교육'),
    ('경영'),
    ('사회운동');

INSERT INTO person (country_id, period_id, field_id, person_name)
VALUES
    (1,  6,  1,  '이황'),
    (2,  8,  2,  '조지 워싱턴'),
    (3,  6,  4,  '윌리엄 셰익스피어'),
    (4,  7,  1,  '르네 데카르트'),
    (5,  8,  1,  '임마누엘 칸트'),
    (6,  5,  5,  '레오나르도 다빈치'),
    (7,  2,  1,  '소크라테스'),
    (8,  1,  1,  '공자'),
    (9,  7,  4,  '마쓰오 바쇼'),
    (10, 10, 10, '마하트마 간디');

INSERT INTO theme (theme_name)
VALUES
    ('인생'),
    ('성공'),
    ('사랑'),
    ('행복'),
    ('용기'),
    ('지혜'),
    ('자유'),
    ('희망'),
    ('노력'),
    ('평화');

INSERT INTO quote (theme_id, person_id, quote_content)
VALUES
    (6, 1, '옛사람을 보지 못했어도 그들이 걸어간 길은 내 앞에 있다.'),
    (8, 1, '우리도 그치지 말고 영원히 푸르게 살아가리라.'),
    (9, 1, '높은 곳에 오르려면 낮은 곳에서 시작하고, 멀리 가려면 가까운 곳에서 시작해야 한다.'),
    (4, 2, '자유를 사랑하고 법을 존중하며 근면을 실천하는 것이 행복의 가장 강한 토대다.'),
    (7, 2, '자유의 성스러운 불꽃과 공화정의 운명이 국민에게 맡겨져 있다.'),
    (3, 2, '가족과 함께 집에서 보내는 한 달이 밖에서 누릴 수년의 행복보다 낫다.'),
    (3, 3, '변화를 만났다고 변하는 사랑은 사랑이 아니다.'),
    (5, 3, '겁쟁이는 죽기 전에 여러 번 죽지만, 용감한 사람은 죽음을 한 번만 맛본다.'),
    (1, 3, '온 세상은 무대이고, 모든 남자와 여자는 배우일 뿐이다.'),
    (6, 4, '나는 생각한다. 그러므로 나는 존재한다.'),
    (6, 4, '어려움은 해결에 필요한 만큼 가능한 한 작은 부분으로 나누어라.'),
    (6, 4, '상식은 세상에서 가장 공평하게 나누어진 것이다.'),
    (5, 5, '감히 알려고 하라. 자신의 이성을 사용할 용기를 가져라.'),
    (6, 5, '계몽이란 인간이 스스로 초래한 미성숙에서 벗어나는 것이다.'),
    (7, 5, '계몽에 필요한 것은 오직 자유, 곧 자신의 이성을 공개적으로 사용할 자유다.'),
    (6, 6, '지혜는 경험의 딸이다.'),
    (9, 6, '이론 없이 실천만 사랑하는 사람은 키와 나침반 없이 배에 오르는 선원과 같다.'),
    (2, 6, '장애물은 나를 꺾을 수 없다. 굳은 결심 앞에서 모든 장애물은 물러난다.'),
    (1, 7, '성찰하지 않는 삶은 살 가치가 없다.'),
    (6, 7, '내가 더 현명하다면, 알지 못하는 것을 안다고 생각하지 않기 때문이다.'),
    (6, 7, '재산과 명예보다 지혜와 진리, 영혼을 훌륭하게 만드는 일을 돌보라.'),
    (4, 8, '배우고 때때로 익히면 또한 기쁘지 아니한가.'),
    (6, 8, '아는 것을 안다고 하고 모르는 것을 모른다고 하는 것이 진정한 앎이다.'),
    (3, 8, '자기가 원하지 않는 것을 남에게 행하지 마라.'),
    (1, 9, '날과 달은 영원한 나그네이며, 오고 가는 해 또한 여행자다.'),
    (10, 9, '오래된 연못, 개구리 뛰어드는 물소리.'),
    (1, 9, '매일이 여행이며 여행 그 자체가 집이다.'),
    (3, 10, '증오는 언제나 죽이고 사랑은 결코 죽지 않는다.'),
    (10, 10, '사람은 저마다 자신의 내면에서 평화를 찾아야 한다.'),
    (9, 10, '진정한 실천은 말이 아니라 행동으로 이루어진다.');

INSERT INTO bookmark (member_id, quote_id)
VALUES
    (3, 13),
    (3, 16),
    (4, 19),
    (4, 22),
    (5, 25),
    (5, 28);


-- COMMIT이 안됨을 방지하기 위해 COMMIT 쿼리 실행
COMMIT;
































