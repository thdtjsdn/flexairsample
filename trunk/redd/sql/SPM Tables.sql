drop table "SPMUsers";
create table "SPMUsers" (
	"UserID" int primary key identity not null,
	"UserName" nvarchar(32) not null,
	"PasswordHash" nvarchar(32) not null,
	"FullName" nvarchar(100) null
);

insert into "SPMUsers" ("UserName", "PasswordHash", "FullName") values ('nchrysler', '912ec803b2ce49e4a541068d495ab570', 'Nate Chrysler');

drop table "SPMUserRoles";
create table "SPMUserRoles" (
	"UserName" nvarchar(32) not null,
	"RoleName" nvarchar(32) not null
);

insert into "SPMUserRoles" ("UserName", "RoleName") values ('nchrysler', 'user');
insert into "SPMUserRoles" ("UserName", "RoleName") values ('nchrysler', 'admin');

drop table "SPMDashboards";
create table "SPMDashboards" (
	"DashboardID" int primary key identity not null,
	"UserID" int null,
	"RoleID" int null,
	"WindowID" int null,
	"DashboardKey" nvarchar(20) not null,
	"Name" nvarchar(100) not null,
	"Definition" ntext not null
);

drop table "SPMAlerts";
create table "SPMAlerts" (
	"AlertID" int primary key identity not null,
	"AlertTypeID" int not null,
	"LevelID" nvarchar(10) not null,
	"Zone" nvarchar(10) null,
	"Area" nvarchar(10) null,
	"Territory" nvarchar(10) null,
	"AlertContent" ntext not null
);

drop table "AlertTypes";
create table "AlertTypes" (
	"AlertTypeID" int primary key identity not null,
	"AlertKey" nvarchar(10) not null,
	"Description" ntext not null
);

insert into "AlertTypes" ("AlertKey", "Description") values ('note','An analyst note.');

drop table "SPMSupport";
create table "SPMSupport" (
	"ArticleID" int primary key identity not null,
	"AppArea" nvarchar(20) not null,
	"ArticleContent" ntext not null,
	"CreateDate" datetime default getdate() not null,
	"UpdateDate" datetime default getdate() not null
);

drop table "SPMForums";
create table "SPMForums" (
	"ForumID" int primary key identity not null,
	"CreatedByUserID" int not null,
	"CreateDate" datetime default getdate() not null,
	"Name" nvarchar(100) not null,
	"Description" ntext null
);

drop table "SPMForumMessages";
create table "SPMForumMessages" (
	"MessageID" int primary key identity not null,
	"ForumID" int not null,
	"UserID" int not null,
	"ReplyToMessageID" int null,
	"CreateDate" datetime default getdate() not null,
	"UpdateDate" datetime default getdate() not null,
	"MessageContent" ntext not null
);

drop table "SPMInboxMessages";
create table "SPMInboxMessages" (
	"MessageID" int primary key identity not null,
	"UserID" int not null,
	"FromUserID" int not null,
	"SentDate" datetime default getdate() not null,
	"Subject" nvarchar(250) not null,
	"IsRead" tinyint default 0 not null,
	"MessageContent" ntext not null
);

drop table "SPMMetricTypes";
create table "SPMMetricTypes" (
	"TypeID" int primary key identity not null,
	"TypeKey" nvarchar(10) not null,
	"Description" ntext not null
);

SET IDENTITY_INSERT "SPMMetricTypes" ON
insert into "SPMMetricTypes" ("TypeID", "TypeKey", "Description") values (1, 'numeric', 'Simple numeric metrics (e.g. count)');
insert into "SPMMetricTypes" ("TypeID", "TypeKey", "Description") values (2, 'currency', 'Dollars and cents');
insert into "SPMMetricTypes" ("TypeID", "TypeKey", "Description") values (3, 'percentage', 'Percentages, typically derived');
insert into "SPMMetricTypes" ("TypeID", "TypeKey", "Description") values (4, 'rank', 'Ranks, typically derived');
SET IDENTITY_INSERT "SPMMetricTypes" OFF

drop table "SPMMetricDimensions";
create table "SPMMetricDimensions" (
	"DimensionID" int primary key identity not null,
	"DimensionKey" nvarchar(10) not null,
	"Description" ntext not null
);

SET IDENTITY_INSERT "SPMMetricDimensions" ON
insert into "SPMMetricDimensions" ("DimensionID", "DimensionKey", "Description") values (1, 'geo', 'Metrics for geographic areas, such as territories, districts, and regions.');
insert into "SPMMetricDimensions" ("DimensionID", "DimensionKey", "Description") values (2, 'prescriber', 'Metrics for prescribers.');
insert into "SPMMetricDimensions" ("DimensionID", "DimensionKey", "Description") values (3, 'product', 'Metrics for products.');
SET IDENTITY_INSERT "SPMMetricDimensions" OFF

drop table "SPMMetrics";
create table "SPMMetrics" (
	"MetricID" int primary key identity not null,
	"TypeID" int not null,
	"DimensionID" int not null,
	"ParentMetricID" int null,
	"Column" nvarchar(30) not null,
	"DisplayName" nvarchar(50) not null,
	"Description" ntext null
);

SET IDENTITY_INSERT "SPMMetrics" ON
insert into "SPMMetrics" ("MetricID", "TypeID", "DimensionID", "ParentMetricID", "Column", "DisplayName") values (1, 2, 1, null, 'SalesTotal', 'Sales Total');
insert into "SPMMetrics" ("MetricID", "TypeID", "DimensionID", "ParentMetricID", "Column", "DisplayName") values (2, 2, 1, null, 'Quota', 'Quota');
insert into "SPMMetrics" ("MetricID", "TypeID", "DimensionID", "ParentMetricID", "Column", "DisplayName") values (3, 3, 1, null, '%Attainment', '% Attainment');
insert into "SPMMetrics" ("MetricID", "TypeID", "DimensionID", "ParentMetricID", "Column", "DisplayName") values (4, 4, 1, 3,    '%AttainmentAreaRank', '% Attainment');
insert into "SPMMetrics" ("MetricID", "TypeID", "DimensionID", "ParentMetricID", "Column", "DisplayName") values (5, 1, 1, null, 'CurCount', 'Count');
SET IDENTITY_INSERT "SPMMetrics" OFF
