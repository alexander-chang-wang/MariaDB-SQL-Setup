CREATE TABLE MEMBERS (
	member_id            INT         NOT NULL AUTO_INCREMENT,      
	student_name         VARCHAR(50) NOT NULL, 
	student_email        VARCHAR(50), 
	membership_status    VARCHAR(50), 
	last_contacted       DATE, 
	
	CONSTRAINT MEMBERS_pk
		PRIMARY KEY (member_id)
);