CREATE USER twitteruser IDENTIFIED BY 'twitteruserpw';
grant usage on *.* to twitteruser@localhost identified by 'twitteruserpw';
grant all privileges on twitter.* to twitteruser@localhost;

create database twitter;
use twitter;

-- sec_user
create table sec_user (
	sec_user_id int not null auto_increment,	
	username varchar(50) not null, 
	password varchar(50) not null,
	user_info varchar(200) not null,
	primary key (sec_user_id)
);

insert into sec_user(username, password, user_info) values('sanjayrally', 'sanjayrallypw', "Sanjay Rally');
insert into sec_user(username, password, user_info) values('leomoreyn', 'leomoreynpw', 'Leo Moreyn');
insert into sec_user(username, password, user_info) values('sarahwest', 'sarahwestpw', 'Sarah West');
insert into sec_user(username, password, user_info) values('nba', 'nbapw', 'National Basketball Association');
insert into sec_user(username, password, user_info) values('nfl', 'nflpw', 'National Football Association');
insert into sec_user(username, password, user_info) values('mlb', 'mlbpw', 'Major League Baseball');
insert into sec_user(username, password, user_info) values('googlenews', 'googlenewspw', 'Google News');

-- tweet
create table tweet (
	tweet_id int not null auto_increment,	
	sec_user_id int not null, 
	date_added datetime not null,
	data varchar(140) not null,
	trending_flag int(1) not null,
	primary key (tweet_id),
	foreign key (sec_user_id) references sec_user(sec_user_id)
);

insert into tweet(sec_user_id, date_added, data, trending_flag) values(6, now(), "CC Sebathia to have season ending knee surgery", 1)
insert into tweet(sec_user_id, date_added, data, trending_flag) values(6, now(), "Yankees to have Derek Jeter day on September 7", 1)

insert into tweet(sec_user_id, date_added, data, trending_flag) values(5, now(), "Vikings suspend special teams coach Priefer", 1)
insert into tweet(sec_user_id, date_added, data, trending_flag) values(5, now(), "Godell floats 49ers' pad as Raiders option", 1)

insert into tweet(sec_user_id, date_added, data, trending_flag) values(7, now(), "Ukraine, rebels agree on security zone at crash site", 1)

















