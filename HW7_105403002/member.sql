CREATE DATABASE member;
USE member;

CREATE TABLE people (
   MemberID INT NOT NULL AUTO_INCREMENT,
   name varchar (20) NOT NULL,
   phone varchar (15) NOT NULL,
   e_mail varchar (30) NOT NULL,
   sex varchar (3) NOT NULL,
   PRIMARY KEY (MemberID)
);

INSERT INTO people (name, phone, e_mail, sex)
VALUES 
   ('小明', '0921-912881', 'min@gamil.com' ,'男'), 
   ('大白', '0987-878887', 'white@hotmail.com' ,'男'),
   ('西南', '0938-917223', 'johncena@yahoo.com' ,'男'), 
   ('莉莉', '0920-384422', 'lily@cc.ncu.edu.tw' ,'女'),
   ('莎莉', '0922-882733', 'sally@gmail.com' ,'女');