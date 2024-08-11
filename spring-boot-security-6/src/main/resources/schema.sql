create table users(username varchar_ignorecase(50) not null primary key,password varchar_ignorecase(500) not null,enabled boolean not null);
create table authorities (username varchar_ignorecase(50) not null,authority varchar_ignorecase(50) not null,constraint fk_authorities_users foreign key(username) references users(username));
create unique index ix_auth_username on authorities (username,authority);
-- for this schema -> go to browser->search spring security github -> open first link -> search users.ddl->click on first/correct path 
--   -> copy schema -> paste it here . 
-- default schema
-- we have to add this schema when we are using h2 database which is also an InMemory database