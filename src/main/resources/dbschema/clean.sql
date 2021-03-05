/** DELETE ALL ROWS
truncate table worklogs cascade;
truncate table users cascade;
truncate table issue_fields cascade;
truncate table issues cascade;
truncate table projects cascade;
truncate table logs cascade;
truncate table sources cascade;
INSERT INTO public.sources("id", "name") VALUES(1, 'JIRA_1');
*/

/** DROP TABLES
drop table worklogs;
drop table users;
drop table issue_fields;
drop table issues;
drop table projects;
drop table logs;
drop table sources;
*/

