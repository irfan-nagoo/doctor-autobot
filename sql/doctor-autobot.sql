
createdb umls;

psql umls;

ALTER USER "postgres" WITH PASSWORD 'postgres';

CREATE TABLE umls_symptom(
	id serial PRIMARY KEY,
	cui varchar(10) UNIQUE,
	name varchar(200),
	status varchar(10),
	version int,
	create_date timestamp,
	created_by varchar(50),
	update_date timestamp,
	updated_by varchar(50)
);	


CREATE TABLE umls_disease(
	id serial PRIMARY KEY,
	cui varchar(10) UNIQUE,
	name varchar(200),
	sem_type1 varchar(100),
	status varchar(10),
	version int,
	create_date timestamp,
	created_by varchar(50),
	update_date timestamp,
	updated_by varchar(50)
);	


CREATE TABLE umls_symptom_disease(
	id serial PRIMARY KEY,
	sym_cui varchar(10) REFERENCES umls_symptom(cui),
	dis_cui varchar(10) REFERENCES umls_disease(cui)	
);


CREATE TABLE umls_medicine(
	id serial PRIMARY KEY,
	cui varchar(10) UNIQUE,
	name varchar(200),
	source varchar(50),
	sem_type1 varchar(100),
	sem_type2 varchar(100),
	sem_type3 varchar(100),
	status varchar(10),
	version int,
	create_date timestamp,
	created_by varchar(50),
	update_date timestamp,
	updated_by varchar(50)
);	


CREATE TABLE umls_disease_medicine(
	id serial PRIMARY KEY,
	dis_cui varchar(10) REFERENCES umls_disease(cui),
	med_cui varchar(10) REFERENCES umls_medicine(cui)
);

