USE MASTER;

CREATE TABLE city ( id INT AUTO_INCREMENT PRIMARY KEY, NAME VARCHAR (255), state VARCHAR (255), country VARCHAR (255));

INSERT INTO city (NAME, state, country) VALUES ( 'San Francisco-master', 'CA', 'US' );

USE slave1;

CREATE TABLE city ( id INT AUTO_INCREMENT PRIMARY KEY, NAME VARCHAR (255), state VARCHAR (255), country VARCHAR (255));

INSERT INTO city (NAME, state, country) VALUES ( 'San Francisco-slave1', 'CA', 'US' );

USE slave2;

CREATE TABLE city ( id INT AUTO_INCREMENT PRIMARY KEY, NAME VARCHAR (255), state VARCHAR (255), country VARCHAR (255));

INSERT INTO city (NAME, state, country) VALUES ( 'San Francisco-slave2', 'CA', 'US' );