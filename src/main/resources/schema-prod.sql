create type user_authorities as enum ('ROLE_ADMINISTRATOR', 'ROLE_USER');

create table users
(
    username              varchar(50)          not null
        primary key,
    password              varchar(60)          not null,
    is_account_not_locked boolean default true not null,
    authority             user_authorities     not null
);

create table posts
(
    id         varchar(36)                         not null
        primary key,
    content    text                                not null,
    posted_on  timestamp default CURRENT_TIMESTAMP not null,
    posted_by  varchar(50)                         not null,
    updated_on timestamp                           null,
    constraint posts_ibfk_1
        foreign key (posted_by) references users (username)
);

create table comments
(
    id         varchar(36)                         not null
        primary key,
    content    text                                not null,
    comment_on timestamp default CURRENT_TIMESTAMP not null,
    post_ref   varchar(36)                         not null,
    comment_by varchar(50)                         not null,
    updated_on timestamp                           null,
    constraint comments_ibfk_1
        foreign key (post_ref) references posts (id),
    constraint comments_ibfk_2
        foreign key (comment_by) references users (username)
);

create table refresh_tokens
(
    id       varchar(36) not null
        primary key,
    token    text        not null,
    for_user varchar(50) not null,
    constraint refresh_tokens_ibfk_1
        foreign key (for_user) references users (username)
);