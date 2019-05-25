/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     04/05/2019 15:34:49                          */
/*==============================================================*/

DROP DATABASE `calllist_dev`;

CREATE SCHEMA `calllist_dev` DEFAULT CHARACTER SET utf8 ;

USE `calllist_dev`;

drop table if exists ACCESS_CODE;

drop table if exists MEETING;

drop table if exists PARTICIPANT;

drop table if exists PROFILE;

-- drop index USER on USER;

-- drop index NAME on USER;

drop table if exists RECOVERY;

drop table if exists USER;

/*==============================================================*/
/* Table: ACCESS_CODE                                            */
/*==============================================================*/
create table ACCESS_CODE
(
   ID                   bigint not null auto_increment,
   CODE                 int(4) not null,
   CREATE_DATE          datetime not null default CURRENT_TIMESTAMP,
   MEETING_ID           bigint not null,
   primary key (ID)
);

/*==============================================================*/
/* Table: MEETING                                               */
/*==============================================================*/
create table MEETING
(
   ID                   bigint not null auto_increment,
   PROFILE_ID           bigint not null,
   NAME                 varchar(200) not null,
   DATE                 datetime not null,
   primary key (ID)
);

/*==============================================================*/
/* Table: PARTICIPANT                                           */
/*==============================================================*/
create table PARTICIPANT
(
   ID                   bigint not null auto_increment,
   PROFILE_ID           bigint not null,
   MEETING_ID           bigint not null,
   primary key (ID)
);

/*==============================================================*/
/* Table: PROFILE                                               */
/*==============================================================*/
create table PROFILE
(
   ID                   bigint not null auto_increment,
   USER_ID              bigint,
   EMAIL                varchar(150) not null,
   PHONE                varchar(20) not null,
   COMPANY              varchar(150),
   ROLE                 varchar(100),
   MAIN                 boolean,
   ACTIVE               boolean,
   primary key (ID),
   key AK_KEY_2 (USER_ID, TYPE)
);

/*==============================================================*/
/* Table: RECOVERY                                              */
/*==============================================================*/
create table RECOVERY
(
    ID                 varchar(36) not null,
    USER_ID            bigint not null,
    EXPIRE_AT          datetime not null,
    primary key (ID)
);

/*==============================================================*/
/* Table: USER                                                  */
/*==============================================================*/
create table USER
(
   ID                   bigint not null auto_increment,
   NAME                 varchar(200) not null,
   USERNAME             varchar(200) not null,
   PASSWORD             varchar(128) not null,
   SALT                 varchar(32) not null,
   CREATE_DATE          datetime not null default CURRENT_TIMESTAMP,
   primary key (ID)
);

/*==============================================================*/
/* Index: NAME                                                  */
/*==============================================================*/
create index NAME on USER
(
   NAME
);

/*==============================================================*/
/* Index: USER                                                  */
/*==============================================================*/
create index USER on USER
(
   USER
);

alter table ACCESS_CODE add constraint FK_CODE_MEETING foreign key (MEETING_ID)
      references MEETING (ID) on delete restrict on update restrict;

alter table MEETING add constraint FK_PROFILE_MEETING foreign key (PROFILE_ID)
      references PROFILE (ID) on delete restrict on update restrict;

alter table PARTICIPANT add constraint FK_MEETING_PARTICIPANT foreign key (MEETING_ID)
      references MEETING (ID) on delete restrict on update restrict;

alter table PARTICIPANT add constraint FK_PROFILE_PARTICIPANT foreign key (PROFILE_ID)
      references PROFILE (ID) on delete restrict on update restrict;

alter table PROFILE add constraint FK_USER_PROFILE foreign key (USER_ID)
      references USER (ID) on delete restrict on update restrict;

alter table RECOVERY add constraint FK_USER_RECOVERY foreign key (USER_ID)
      references USER (ID) on delete restrict on update restrict;

