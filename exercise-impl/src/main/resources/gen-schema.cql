// Export of keyspace syndeia_cloud_repositories
CREATE KEYSPACE IF NOT EXISTS bsbecexercise
WITH durable_writes = true
     AND replication = {
'class' : 'SimpleStrategy',
'replication_factor' : 1
};

CREATE TABLE IF NOT EXISTS bsbecexercise.black_list_ip (
id timeuuid,
ip bigint,
PRIMARY KEY (ip)
) ;

CREATE TABLE IF NOT EXISTS bsbecexercise.bank_info (
id timeuuid,
name text,
identifier text,
PRIMARY KEY (identifier,name)
) ;

CREATE TABLE IF NOT EXISTS bsbecexercise.category (
id timeuuid,
type text,
title text,
keywords set<text>,
creation_date timestamp,
PRIMARY KEY (title)
) ;

CREATE TABLE IF NOT EXISTS bsbecexercise.keyword_title (
id timeuuid,
keyword text,
title text,
PRIMARY KEY (keyword)
) ;

