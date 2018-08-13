USE master;

CREATE TABLE city ( id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR (255), state VARCHAR (255), country VARCHAR (255));

INSERT INTO city (name, state, country) VALUES ( 'San Francisco-master', 'CA', 'US' );

CREATE TABLE `tb_user_info` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`login_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`password`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
);

INSERT INTO tb_user_info (id, login_name, password) VALUES ( 1, 'gaoguangjin', 'qazqaz' );

USE slave;

CREATE TABLE city ( id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR (255), state VARCHAR (255), country VARCHAR (255));

INSERT INTO city (name, state, country) VALUES ( 'San Francisco-slave', 'CA', 'US' );

CREATE TABLE `tb_user_info` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`login_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`password`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
);

INSERT INTO tb_user_info (id, login_name, password) VALUES ( 1, 'gaoguangjin', 'qazqaz' );


