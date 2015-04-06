# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table `COMPANIES` (`ID` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,`NAME` VARCHAR(254) NOT NULL);
create table `EMPLOYEES` (`ID` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,`NAME` VARCHAR(254) NOT NULL,`POSITION` INTEGER NOT NULL);
create table `POSITIONS` (`ID` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,`NAME` VARCHAR(254) NOT NULL,`COMPANY` INTEGER NOT NULL);
alter table `EMPLOYEES` add constraint `POS_FK` foreign key(`POSITION`) references `POSITIONS`(`ID`) on update CASCADE on delete CASCADE;
alter table `POSITIONS` add constraint `COM_FK` foreign key(`COMPANY`) references `COMPANIES`(`ID`) on update CASCADE on delete CASCADE;

# --- !Downs

ALTER TABLE POSITIONS DROP FOREIGN KEY COM_FK;
ALTER TABLE EMPLOYEES DROP FOREIGN KEY POS_FK;
drop table `POSITIONS`;
drop table `EMPLOYEES`;
drop table `COMPANIES`;

