DROP TABLE IF EXISTS DRONE;  
CREATE TABLE DRONE (  
	serial_no varchar(100) primary key,
	model varchar(50), 
	weight double, 
	battery_capasity double,
	state varchar(50)
);

DROP TABLE IF EXISTS MEDICATION;  
CREATE TABLE DRONE_MEDICATION (  
	serial_no varchar(100) primary key,
	name varchar(100),
	weight double, 
	code varchar(50),
	image varchar(50)
);


