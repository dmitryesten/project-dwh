create role spring with password '123' superuser createdb createrole login inherit;

create database project_dwh owner spring encoding 'UTF-8'; 

create table sources (
	id   integer,
	name varchar not null,
	constraint pk_id_sources primary key (id) 
);
comment on table sources is 'Table with data about sources data (jira, redmin and etc)';
comment on column sources.id is 'source id';
comment on column sources.name is 'source name';


create table logs (
	id  integer,
	sid integer,
	start_dt timestamp not null,
	end_dt timestamp not null,
	result boolean not null,
	constraint pk_id_logs primary key (id),
	constraint fk_sid_sources foreign key (sid) references sources(id)
);
comment on table logs is 'Table with data about retrieving data from source';
comment on column logs.id is 'logs id';
comment on column logs.sid is 'foreign key of source data';
comment on column logs.start_dt is 'date-time start of retrieving from source data';
comment on column logs.end_dt is 'date-time end of retrieving from source data';
comment on column logs.result is 'result of retrieving success=true, fail=false';


create table projects (
	id integer,
	sid integer,
	log_id integer,
	source_id integer,
	name varchar,
	constraint pk_id_projects primary key (id),
	constraint fk_sid_sources foreign key (sid) references sources(id),
	constraint fk_id_logs_projects foreign key (log_id) references logs(id)
);
comment on table projects is 'Table about projects from source data';
comment on column projects.id is 'project id';
comment on column projects.sid is 'foreign key of source id';
comment on column projects.log_id is 'foreign key of log id';
comment on column projects.source_id is 'project id of source data';
comment on column projects.name is 'project name';


create table issues (
	id integer,
	pid integer,
	sid integer,
	log_id integer,
	source_id integer not null,
	hid integer,
	type varchar not null,
	name varchar not null,
	summery varchar,
	constraint pk_id_issues primary key (id),
	constraint fk_id_project foreign key (pid) references projects(id),
	constraint fk_id_source foreign key (sid) references sources(id),
	constraint fk_id_logs_issues foreign key (log_id) references logs(id),
	constraint fk_id_parent_issues foreign key (hid) references issues(id)
);
comment on table issues is 'Table about issues from source data';
comment on column issues.id is 'issues id';
comment on column issues.pid is 'project id';
comment on column issues.sid is 'source id';
comment on column issues.log_id is 'log id of retrieved data process';
comment on column issues.source_id is 'source id of source data';
comment on column issues.hid is 'parent issue id';
comment on column issues.type is 'type issue';
comment on column issues.name is 'name issue';
comment on column issues.summery is 'summery issue';


create table users (
	id integer,
	key varchar not null,
	log_id integer,
	name varchar not null,
	constraint pk_id_users primary key (id),
	constraint fk_id_logs_users foreign key (log_id) references logs(id)
);
comment on table users is 'Table about users of source data';
comment on column users.key is 'user''s key of source data';
comment on column users.log_id is 'log_id of retrieved data process';
comment on column users.name is 'name user';


create table worklogs (
	id integer,
	issue_id integer,
	log_id integer,
	sid integer,
	source_id integer,
	updated_dt timestamp,
	time_spent integer,
	username varchar,
	user_id integer,
	constraint pk_id_worklogs primary key (id),
	constraint fk_id_issues foreign key (issue_id) references issues(id),
	constraint fk_id_logs_worklogs foreign key (issue_id) references logs(id),
	constraint fk_id_sources_worklogs foreign key (sid) references sources(id),
	constraint fk_id_users_worklogs foreign key (user_id) references users(id)
);
comment on table worklogs is 'Table about users of source data';
comment on column worklogs.issue_id is 'issue id';
comment on column worklogs.log_id is 'log id';
comment on column worklogs.sid is 'source id';
comment on column worklogs.source_id is 'inside id of source data entity';
comment on column worklogs.updated_dt is 'time of updated worklogs';
comment on column worklogs.time_spent is 'time spending by worklogs';
comment on column worklogs.username is 'username';
comment on column worklogs.username is 'user id of source data';


















