-- 부하테스트용 대량 시드 데이터.
-- Spring Boot의 schema.sql처럼 자동 실행되지 않는 별도 스크립트다.
-- 필요할 때 직접 psql로 실행한다.

INSERT INTO post (title, content)
SELECT '게시글 제목 ' || i, '게시글 내용 ' || i
FROM generate_series(1, 10000) AS i;

INSERT INTO comment (content, post_id)
SELECT '댓글 내용 ' || i, ((i - 1) % 10000) + 1
FROM generate_series(1, 30000) AS i;
