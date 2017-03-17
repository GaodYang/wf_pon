-- Create table
create table T_WF_PROJECT
(
  SETUP_NAME         VARCHAR2(256),
  SETUP_CODE         VARCHAR2(64),
  SETUP_REQFILE_CODE VARCHAR2(64),
  SETUP_REQER_NAME   VARCHAR2(64),
  SETUP_REQER_PHONE  VARCHAR2(32),
  SETUP_REQ_DATE     VARCHAR2(64),
  PRJ_NAME           VARCHAR2(128),
  PRJ_CODE           VARCHAR2(32),
  PRJ_CATEGORY       VARCHAR2(32),
  PRJ_TYPE           VARCHAR2(32),
  COST               VARCHAR2(16),
  LENGTH             VARCHAR2(16),
  DOMAIN_NAME        VARCHAR2(64),
  PRJ_DESC           VARCHAR2(256),
  FTP_NAME           VARCHAR2(128),
  PARENT_PRJ_CODE    VARCHAR2(128),
  CUID               VARCHAR2(128) not null,
  CREATE_TIME        DATE,
  USERNAME           VARCHAR2(64),
  PRJ_STATUS         VARCHAR2(32),
  LAST_MODIFY_TIME   DATE
);
-- Add comments to the columns 
comment on column T_WF_PROJECT.SETUP_NAME
  is '立项文件名称';
comment on column T_WF_PROJECT.SETUP_CODE
  is '立项文号';
comment on column T_WF_PROJECT.SETUP_REQFILE_CODE
  is '立项申请文号';
comment on column T_WF_PROJECT.SETUP_REQER_NAME
  is '立项申请人';
comment on column T_WF_PROJECT.SETUP_REQER_PHONE
  is '立项申请人电话';
comment on column T_WF_PROJECT.SETUP_REQ_DATE
  is '立项申请日期';
comment on column T_WF_PROJECT.PRJ_NAME
  is '工程名称';
comment on column T_WF_PROJECT.PRJ_CODE
  is '工程编号';
comment on column T_WF_PROJECT.PRJ_CATEGORY
  is '工程类别';
comment on column T_WF_PROJECT.PRJ_TYPE
  is '工程类型';
comment on column T_WF_PROJECT.COST
  is '工程总投资(万)';
comment on column T_WF_PROJECT.LENGTH
  is '工程规模长度';
comment on column T_WF_PROJECT.DOMAIN_NAME
  is '所属地市';
comment on column T_WF_PROJECT.PRJ_DESC
  is '工程描述';
comment on column T_WF_PROJECT.FTP_NAME
  is 'FTP文件名称';
comment on column T_WF_PROJECT.PARENT_PRJ_CODE
  is '所属工程编码';
comment on column T_WF_PROJECT.CUID
  is 'cuid';
comment on column T_WF_PROJECT.CREATE_TIME
  is '创建时间';
comment on column T_WF_PROJECT.USERNAME
  is '操作人名称';
comment on column T_WF_PROJECT.PRJ_STATUS
  is '施工
验收
初验
归档
作废
';
comment on column T_WF_PROJECT.LAST_MODIFY_TIME is '最后修改时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table T_WF_PROJECT
  add constraint PK_WK_PROJECT primary key (CUID);
