DROP table if exists oauth_client_details;
create table oauth_client_details
(
  client_id               VARCHAR(128) PRIMARY KEY,
  resource_ids            VARCHAR(256),
  client_secret           VARCHAR(256),
  scope                   VARCHAR(256),
  authorized_grant_types  VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities             VARCHAR(256),
  access_token_validity   INTEGER,
  refresh_token_validity  INTEGER,
  additional_information  VARCHAR(4096),
  autoapprove             VARCHAR(256)
);

DROP table if exists oauth_client_token;
create table oauth_client_token
(
  token_id          VARCHAR(256),
  token             BLOB,
  authentication_id VARCHAR(128) PRIMARY KEY,
  user_name         VARCHAR(256),
  client_id         VARCHAR(256)
);

drop table if exists oauth_access_token;
create table oauth_access_token
(
  token_id          VARCHAR(256),
  token             BLOB,
  authentication_id VARCHAR(128) PRIMARY KEY,
  user_name         VARCHAR(256),
  client_id         VARCHAR(256),
  authentication    BLOB,
  refresh_token     VARCHAR(256)
);

drop table if exists oauth_refresh_token;
create table oauth_refresh_token
(
  token_id VARCHAR(256),
  token BLOB,
  authentication BLOB
);

drop table if exists oauth_code;
create table oauth_code
(
  code VARCHAR(256),
  authentication BLOB
);

drop table if exists oauth_approvals;
create table oauth_approvals
(
  userId         VARCHAR(256),
  clientId       VARCHAR(256),
  scope          VARCHAR(256),
  status         VARCHAR(10),
  expiresAt      DATETIME,
  lastModifiedAt DATETIME
);


-- customized oauth_client_details table
drop table if exists ClientDetails;
create table ClientDetails
(
  appId                  VARCHAR(128) PRIMARY KEY,
  resourceIds            VARCHAR(256),
  appSecret              VARCHAR(256),
  scope                  VARCHAR(256),
  grantTypes             VARCHAR(256),
  redirectUrl            VARCHAR(256),
  authorities            VARCHAR(256),
  access_token_validity  INTEGER,
  refresh_token_validity INTEGER,
  additionalInformation  VARCHAR(4096),
  autoApproveScopes      VARCHAR(256)
);