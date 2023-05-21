INSERT INTO position(code, name, parent_code)
VALUES (100, '기획', null),
       (200, '디자인', null),
       (300, '프론트엔드 개발', null),
       (400, '백엔드 개발', null),
       (500, '사업', null),
       (600, '기타', null);

INSERT INTO position(code, name, parent_code)
VALUES (101, 'UI/UX 기획', 100),
       (102, '게임 기획', 100),
       (103, '프로젝트 매니저', 100),
       (104, '하드웨어(제품) 기획', 100),
       (105, '기타', 100);

INSERT INTO position(code, name, parent_code)
VALUES (201, '그래픽 디자인', 200),
       (202, 'UI/UX 디자인', 200),
       (203, '3D 디자인', 200),
       (204, '하드웨어(제품) 디자인', 200),
       (205, '기타', 200);

INSERT INTO position(code, name, parent_code)
VALUES (301, 'IOS', 300),
       (302, '안드로이드', 300),
       (303, '웹 프론트엔드', 300),
       (304, '웹 퍼블리셔', 300),
       (305, '크로스플랫폼', 300);

INSERT INTO position(code, name, parent_code)
VALUES (401, '웹 서버', 400),
       (402, '블록체인', 400),
       (403, 'AI', 400),
       (404, 'DB/빅데이터/DS', 400),
       (405, '게임 서버', 400);

INSERT INTO position(code, name, parent_code)
VALUES (501, '사업기획', 500),
       (502, '마케팅', 500),
       (503, '재무/회계', 500),
       (504, '영업', 500),
       (505, '전략/컨설팅', 500),
       (506, '투자/고문', 500),
       (507, '그 외', 500);

INSERT INTO position(code, name, parent_code)
VALUES (601, '사업기획', 600),
       (602, '마케팅', 600),
       (603, '재무/회계', 600),
       (604, '영업', 600),
       (605, '전략/컨설팅', 600),
       (606, '투자/고문', 600),
       (607, '그 외', 600);