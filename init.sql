create sequence seq_table increment by 1 start with 1;

create table sources (
	id   integer default nextval('seq_table'),
	name varchar not null,
	create_dt timestamp default current_timestamp,
	constraint pk_id_sources primary key (id) 
);
comment on table sources is 'Table with data about sources data (jira, redmin and etc)';
comment on column sources.id is 'source id';
comment on column sources.name is 'source name';


create table logs (
	id  integer default nextval('seq_table'),
	hid integer,
	sid integer,
	start_dt timestamp ,
	end_dt timestamp,
	result boolean,
	constraint pk_id_logs primary key (id),
	constraint fk_hid_id foreign key (hid) references logs(id),
	constraint fk_sid_sources foreign key (sid) references sources(id)
);
comment on table logs is 'Table with data about retrieving data from source';
comment on column logs.id is 'logs id';
comment on column logs.sid is 'foreign key of source data';
comment on column logs.start_dt is 'date-time start of retrieving from source data';
comment on column logs.end_dt is 'date-time end of retrieving from source data';
comment on column logs.result is 'result of retrieving success=true, fail=false';


create table projects (
	id integer default nextval('seq_table'),
	sid integer,
	log_id integer,
	source_id integer,
	name varchar,
	create_dt timestamp default current_timestamp,
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
	id integer default nextval('seq_table'),
	pid integer,
	sid integer,
	log_id integer,
	source_id integer not null,
	hid integer,
	type varchar not null,
	name varchar not null,
	summery varchar,
	create_dt timestamp default current_timestamp,
	constraint pk_id_issues primary key (id),
	constraint fk_id_project foreign key (pid) references projects(id),
	constraint fk_id_source foreign key (sid) references sources(id),
	constraint fk_id_logs_issues foreign key (log_id) references logs(id)
	--,constraint fk_id_parent_issues foreign key (hid) references issues(id)
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

create table issue_fields (
	id integer default nextval('seq_table'),
	issue_id integer,
	sid integer,
	log_id integer,
	issue_source_id integer not null,
	field varchar,
	type varchar,
	name varchar,
	value varchar,
	create_dt timestamp default current_timestamp
);
comment on table issue_fields is 'Table issue_fields contains additional data about issues';
comment on column issue_fields.id is 'row id';
comment on column issue_fields.issue_id is 'foreign key by issues table';
comment on column issue_fields.sid is 'id source data system';
comment on column issue_fields.log_id is 'log id of retrieved data process';
comment on column issue_fields.issue_source_id is 'inside issues source id of source data system';
comment on column issue_fields.field is 'key name of json';
comment on column issue_fields.type is 'data type of value json''s jira';
comment on column issue_fields.name is 'name type of field''s issue [Бизнес|Подсистема|Заказчик]';
comment on column issue_fields.value is 'value key of json';

create table users (
	id integer default nextval('seq_table'),
	key varchar not null,
	log_id integer,
	name varchar not null,
	create_dt timestamp default current_timestamp,
	constraint pk_id_users primary key (id),
	constraint fk_id_logs_users foreign key (log_id) references logs(id)
);
comment on table users is 'Table about users of source data';
comment on column users.key is 'user''s key of source data';
comment on column users.log_id is 'log_id of retrieved data process';
comment on column users.name is 'name user';


create table worklogs (
	id integer default nextval('seq_table'),
	issue_id integer,
	log_id integer,
	sid integer,
	source_id integer,
	updated_dt timestamp,
	started_dt timestamp,
	time_spent integer,
	username varchar,
	user_id integer,
	create_dt timestamp default current_timestamp,
	constraint pk_id_worklogs primary key (id),
	constraint fk_id_issues foreign key (issue_id) references issues(id),
	constraint fk_id_logs_worklogs foreign key (log_id) references logs(id),
	constraint fk_id_sources_worklogs foreign key (sid) references sources(id)
	--,constraint fk_id_users_worklogs foreign key (user_id) references users(id)
);
comment on table worklogs is 'Table about worklogs of source data';
comment on column worklogs.issue_id is 'issue id';
comment on column worklogs.log_id is 'log id';
comment on column worklogs.sid is 'source id';
comment on column worklogs.source_id is 'inside id of source data entity';
comment on column worklogs.updated_dt is 'time of updated worklogs';
comment on column worklogs.started_dt is 'time of started worklogs';
comment on column worklogs.time_spent is 'time spending by worklogs';
comment on column worklogs.username is 'username';
comment on column worklogs.user_id is 'user id of source data';


INSERT INTO public.sources("id", "name") VALUES(1, 'JIRA_1');


















