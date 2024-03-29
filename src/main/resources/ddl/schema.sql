create table dev_parameter
(
    id     bigint auto_increment,
    name   varchar(255),
    used   boolean default 0 not null,
    constraint dev_parameter_pk primary key (id)
);

create table tb_community
(
    id                        bigint auto_increment,
    category                  varchar(50),
    title                     varchar(100),
    content                   text,
    content_without_html_tags text,
    author_id                 bigint,
    view_count                bigint default 0 not null,
    created_at                timestamp(6)     not null,
    updated_at                timestamp(6)     not null,
    constraint tb_community_pk primary key (id)
);

create table tb_file
(
    id                bigint auto_increment,
    type              varchar(50),
    image_resize_type varchar(20),
    relation_type     varchar(50),
    relation_id       bigint,
    released          boolean default 1 not null,
    extension         varchar(10),
    original_filename varchar(255),
    stored_filename   varchar(50),
    url               varchar(500),
    created_at        timestamp(6)      not null,
    updated_at        timestamp(6)      not null,
    constraint tb_file_pk primary key (id)
);

create table tb_sms_verification_hist
(
    id           bigint auto_increment,
    phone_number varchar(20),
    number       varchar(6),
    verified     boolean default 0 not null,
    created_at   timestamp(6)      not null,
    updated_at   timestamp(6)      not null,
    constraint tb_sms_verification_hist_pk primary key (id)
);

create table tb_tag_relation
(
    id            bigint auto_increment,
    relation_id   bigint,
    relation_type varchar(50),
    tag_name      varchar(100),
    created_at    timestamp(6) not null,
    updated_at    timestamp(6) not null,
    constraint tb_tag_relation_pk primary key (id)
);

create table tb_user
(
    id           bigint auto_increment,
    login_id     varchar(50),
    kakao_id     varchar(50),
    google_id    varchar(50),
    password     varchar(255),
    email        varchar(100),
    phone_number varchar(20),
    nickname     varchar(50),
    image_url    varchar(500),
    position     varchar(50),
    refreshable  boolean default 1 not null,
    role         varchar(50),
    created_at   timestamp(6)      not null,
    updated_at   timestamp(6)      not null,
    constraint tb_user_pk primary key (id)
);

create table tb_project
(
    id                             bigint auto_increment,
    writer                         bigint,
    title                          varchar(200),
    image_url                      varchar(500),
    online                         boolean          not null,
    address                        varchar(200),
    deadline                       date,
    start_date                     date,
    end_date                       date,
    meeting_days                   varchar(100),
    introduction                   text,
    introduction_without_html_tags text,
    view_count                     bigint default 0 not null,
    created_at                     timestamp(6),
    updated_at                     timestamp(6),
    constraint tb_project_pk primary key (id)
);

create table tb_project_position
(
    id         bigint auto_increment,
    project_id bigint,
    name       varchar(100),
    needs      integer           not null,
    recruited  integer default 0 not null,
    created_at timestamp(6),
    updated_at timestamp(6),
    constraint tb_project_position_pk primary key (id)
);

create table tb_project_join
(
    id          bigint auto_increment,
    project_id  bigint,
    position_id bigint,
    member_id   bigint,
    created_at  timestamp(6),
    updated_at  timestamp(6),
    constraint tb_project_join_pk primary key (id)
);

create table tb_project_application
(
    id          bigint auto_increment,
    project_id  bigint,
    position_id bigint,
    member_id   bigint,
    memo        varchar(500),
    created_at  timestamp(6),
    updated_at  timestamp(6),
    constraint tb_project_application_pk primary key (id)
);

create table tb_study
(
    id                             bigint auto_increment,
    writer                         bigint,
    title                          varchar(200),
    image_url                      varchar(500),
    online                         boolean          not null,
    address                        varchar(200),
    deadline                       date,
    start_date                     date,
    end_date                       date,
    meeting_days                   varchar(100),
    needs                          integer,
    recruited                      integer,
    introduction                   text,
    introduction_without_html_tags text,
    view_count                     bigint default 0 not null,
    created_at                     timestamp(6),
    updated_at                     timestamp(6),
    constraint tb_study_pk primary key (id)
);

create table tb_study_join
(
    id         bigint auto_increment,
    study_id   bigint,
    member_id  bigint,
    created_at timestamp(6),
    updated_at timestamp(6),
    constraint tb_study_join primary key (id)
);

create table tb_study_application
(
    id         bigint auto_increment,
    study_id   bigint,
    member_id  bigint,
    memo       varchar(500),
    created_at timestamp(6),
    updated_at timestamp(6),
    constraint tb_study_application_pk primary key (id)
);

create table tb_user_pick
(
    id          bigint auto_increment,
    available   boolean not null,
    target_id   bigint,
    target_type varchar(50),
    user_id     bigint,
    created_at  timestamp(6),
    updated_at  timestamp(6),
    constraint tb_user_pick_pk primary key (id)
);

create table tb_account_finding
(
    id               bigint auto_increment,
    verification_key varchar(36),
    sms_id           bigint,
    user_id          bigint,
    used             boolean default 0 not null,
    constraint tb_account_finding primary key (id)
);


alter table tb_community add constraint tb_community_idx1 unique (id, category);

alter table tb_user add constraint tb_user_idx1 unique (login_id);
alter table tb_user add constraint tb_user_idx2 unique (kakao_id);
alter table tb_user add constraint tb_user_idx3 unique (google_id);
alter table tb_user add constraint tb_user_idx4 unique (phone_number);

alter table tb_project_position
    add constraint tb_project_position_fk1 foreign key (project_id) references tb_project(id);

alter table tb_project_join
    add constraint tb_project_join_fk1 foreign key (project_id) references tb_project(id);

alter table tb_project_application
    add constraint tb_project_application_fk1 foreign key (project_id) references tb_project(id);

alter table tb_study_join
    add constraint tb_study_join_fk1 foreign key (study_id) references tb_study(id);

alter table tb_study_application
    add constraint tb_study_application_fk1 foreign key (study_id) references tb_study(id);

create index tb_file_idx1 on tb_file (relation_type, relation_id);
create index tb_file_idx2 on tb_file (stored_filename);

create index tb_project_idx1 on tb_project (deadline);

create index tb_project_position_idx1 on tb_project_position (project_id);

create index tb_project_join_idx1 on tb_project_join (project_id, position_id);
create index tb_project_join_idx2 on tb_project_join (project_id, member_id);
create index tb_project_join_idx3 on tb_project_join (member_id);

create index tb_project_application_idx1 on tb_project_application (project_id, position_id);
create index tb_project_application_idx2 on tb_project_application (project_id, member_id);
create index tb_project_application_idx3 on tb_project_application (member_id);

create index tb_study_idx1 on tb_study (id, deadline);

create index tb_study_join_idx1 on tb_study_join (study_id, member_id);
create index tb_study_join_idx2 on tb_study_join (member_id);

create index tb_study_application_idx1 on tb_study_application (study_id, member_id);
create index tb_study_application_idx2 on tb_study_application (member_id);

create index tb_sms_verification_hist_idx1 on tb_sms_verification_hist (phone_number);

create index tb_tag_relation_idx1 on tb_tag_relation (relation_type, relation_id);

create index tb_user_pick_idx1 on tb_user_pick (user_id, target_type, target_id);

create unique index tb_account_finding_idx1 on tb_account_finding (verification_key);