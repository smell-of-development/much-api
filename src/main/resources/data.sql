INSERT INTO dev_parameter(name, use_yn) VALUES('SMS_PASS', 0);

INSERT INTO tb_user(login_id, password, phone_number, email,
                    nickname, position, role, refreshable)
VALUES ('test1',
        '$2a$10$xb/YxcPjT/LJmAjvakNmhuznMA9cDhOiOw9G5xvNjgLAzM2f.3J12', -- 123
        '01011112222', 'test1@test.test',
        '테스트닉네임1', '백엔드', 'ROLE_USER', true);

INSERT INTO tb_user(login_id, password, phone_number, email,
                    nickname, position, role, refreshable)
VALUES ('test2',
        '$2a$10$xb/YxcPjT/LJmAjvakNmhuznMA9cDhOiOw9G5xvNjgLAzM2f.3J12', -- 123
        '01099990000', 'test2@test.test',
        'qwerty', '프론트', 'ROLE_USER', true);

INSERT INTO tb_user(login_id, password, phone_number, email,
                    nickname, position, role, refreshable)
VALUES ('test3',
        '$2a$10$xb/YxcPjT/LJmAjvakNmhuznMA9cDhOiOw9G5xvNjgLAzM2f.3J12', -- 123
        '01012345678', 'test3@test.test',
        '테스트', '풀스택', 'ROLE_USER', true);


INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('QNA', 1, '테스트 QNA 게시글 - 1', '<a>내용 1</a>,', '내용 1',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('QNA', 1, '테스트 QNA 게시글 - 2', '<a>내용 2</a>,', '내용 2',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('QNA', 1, '테스트 QNA 게시글 - 3', '<a>내용 3</a>,', '내용 3',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('QNA', 2, '테스트 QNA 게시글 - 4', '<a>내용 4</a>,', '내용 4',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('QNA', 2, '테스트 QNA 게시글 - 5', '<a>내용 5</a>,', '내용 5',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('QNA', 2, '테스트 QNA 게시글 - 6', '<a>내용 6</a>,', '내용 6',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('QNA', 3, '테스트 QNA 게시글 - 7', '<a>내용 7</a>,', '내용 7',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('QNA', 3, '테스트 QNA 게시글 - 8', '<a>내용 8</a>,', '내용 8',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('QNA', 3, '테스트 QNA 게시글 - 9', '<a>내용 9</a>,', '내용 9',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('QNA', 3, '테스트 QNA 게시글 - 10', '<a>내용 10</a>,', '내용 10',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('QNA', 3, '테스트 QNA 게시글 - 11', '<a>내용 11</a>,', '내용 11',
        0, CURRENT_TIMESTAMP);


INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('FREE', 1, '테스트 자유 게시글 - 1', '<a>내용 1</a>,', '내용 1',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('FREE', 1, '테스트 자유 게시글 - 2', '<a>내용 2</a>,', '내용 2',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('FREE', 1, '테스트 자유 게시글 - 3', '<a>내용 3</a>,', '내용 3',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('FREE', 2, '테스트 자유 게시글 - 4', '<a>내용 4</a>,', '내용 4',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('FREE', 2, '테스트 자유 게시글 - 5', '<a>내용 5</a>,', '내용 5',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('FREE', 2, '테스트 자유 게시글 - 6', '<a>내용 6</a>,', '내용 6',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('FREE', 3, '테스트 자유 게시글 - 7', '<a>내용 7</a>,', '내용 7',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('FREE', 3, '테스트 자유 게시글 - 8', '<a>내용 8</a>,', '내용 8',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('FREE', 3, '테스트 자유 게시글 - 9', '<a>내용 9</a>,', '내용 9',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('FREE', 3, '테스트 자유 게시글 - 10', '<a>내용 10</a>,', '내용 10',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('FREE', 3, '테스트 자유 게시글 - 11', '<a>내용 11</a>,', '내용 11',
        0, CURRENT_TIMESTAMP);


INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('TECH_SHARE', 1, '테스트 기술공유 게시글 - 1', '<a>내용 1</a>,', '내용 1',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('TECH_SHARE', 1, '테스트 기술공유 게시글 - 2', '<a>내용 2</a>,', '내용 2',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('TECH_SHARE', 1, '테스트 기술공유 게시글 - 3', '<a>내용 3</a>,', '내용 3',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('TECH_SHARE', 2, '테스트 기술공유 게시글 - 4', '<a>내용 4</a>,', '내용 4',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('TECH_SHARE', 2, '테스트 기술공유 게시글 - 5', '<a>내용 5</a>,', '내용 5',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('TECH_SHARE', 2, '테스트 기술공유 게시글 - 6', '<a>내용 6</a>,', '내용 6',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('TECH_SHARE', 3, '테스트 기술공유 게시글 - 7', '<a>내용 7</a>,', '내용 7',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('TECH_SHARE', 3, '테스트 기술공유 게시글 - 8', '<a>내용 8</a>,', '내용 8',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('TECH_SHARE', 3, '테스트 기술공유 게시글 - 9', '<a>내용 9</a>,', '내용 9',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('TECH_SHARE', 3, '테스트 기술공유 게시글 - 10', '<a>내용 10</a>,', '내용 10',
        0, CURRENT_TIMESTAMP);

INSERT INTO tb_community(category, author_id, title, content, content_without_html_tags,
                         view_count, created_at)
VALUES ('TECH_SHARE', 3, '테스트 기술공유 게시글 - 11', '<a>내용 11</a>,', '내용 11',
        0, CURRENT_TIMESTAMP);


INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '1', 'JAVA');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '1', 'Spring');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '1', 'Spring Boot');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '1', 'JPA');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '2', 'Figma');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '2', 'Adobe XD');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '2', 'React');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '3', 'Querydsl');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '3', 'JAVA');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '3', 'Python');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '4', 'Vue');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '4', 'Node');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '5', 'JAVA');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '6', 'Figma');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '6', 'Adobe XD');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '7', 'JavaScript');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '7', 'React');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '8', 'Node');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '8', 'Spring Boot');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '9', 'JavaScript');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '9', 'Vue');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '10', 'JavaScript');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '10', 'TypeScript');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '10', 'PHP');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '11', 'Spring Boot');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '12', 'JavaScript');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '12', 'Ruby');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '12', 'C++');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '13', 'Python');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '13', 'JAVA');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '14', 'C');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '14', 'TypeScript');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '14', 'JAVA');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '14', 'Spring Boot');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '14', 'React');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '15', 'JPA');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '16', 'Querydsl');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '16', 'Querydsl');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '16', 'C');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '16', 'Python');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '17', 'Spring Boot');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '17', 'JAVA');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '17', 'JavaScript');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '17', 'Unity');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '18', 'Spring');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '19', 'Spring Data Jpa');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '19', 'Spring');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '19', 'React');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '20', 'TypeScript');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '20', 'Python');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '20', 'Ruby');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '20', 'Spring');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '21', 'Vue');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '21', 'TypeScript');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '22', 'Java');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '23', 'Python');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '24', 'Querydsl');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '25', 'Spring Boot');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '26', 'Java');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '26', 'Figma');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '26', 'Adobe XD');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '27', 'Vue');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '27', 'React');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '28', 'Unity');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '29', 'Java');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '29', 'TypeScript');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '29', 'JavaScript');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '29', 'React');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '30', 'Vue');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '30', 'React');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '30', 'Figma');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '30', 'React');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '30', 'JavaScript');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '31', 'JavaScript');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '31', 'Vue');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '32', 'JAVA');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '32', 'Spring');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '32', 'JPA');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '33', 'Vue');

INSERT INTO tb_tag_relation(relation_type, relation_id, tag_name)
VALUES ('COMMUNITY', '33', 'JavaScript');