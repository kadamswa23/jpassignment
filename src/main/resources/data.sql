--drop table if exists address
--drop table if exists contact
--drop table if exists user_profile;

CREATE TABLE user_profile (
  USER_ID int auto_increment not null,
  FIRST_NAME varchar(255),
  LAST_NAME varchar(255),
  DOB date,
  PRIMARY KEY (USER_ID)
);

CREATE TABLE ADDRESS(
ADDRESS_ID int auto_increment not null,
USER_ID int not null,
STREET VARCHAR(255) ,
PRIMARY KEY (address_id),
CONSTRAINT fk_address_user FOREIGN KEY (USER_ID) REFERENCES user_profile(USER_ID)
);

CREATE TABLE CONTACT(
CONTACT_ID int auto_increment not null,
USER_ID int not null,
EMAIL varchar(255) not null,
HOME_PHONE VARCHAR(50) ,
MOBILE_PHONE VARCHAR(50) ,
PRIMARY KEY ( CONTACT_ID ),
CONSTRAINT fk_contact_user FOREIGN KEY (USER_ID) REFERENCES user_profile(USER_ID)
);

