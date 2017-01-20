DROP TABLE CONSOS_CAFE;

DROP TABLE PROGRAMMEURS;

CREATE TABLE PROGRAMMEURS (
ID NUMBER(38) NOT NULL, 
NOM VARCHAR(30) NOT NULL, 
PRENOM VARCHAR(30) NOT NULL, 
BUREAU NUMBER(38), 
PRIMARY KEY (ID));

CREATE TABLE CONSOS_CAFE (
  NO_SEMAINE NUMBER(38) NOT NULL, 
  PROGRAMMEUR NUMBER(38) NOT NULL, 
  NB_TASSES NUMBER(38) NOT NULL, 
  PRIMARY KEY (NO_SEMAINE, PROGRAMMEUR),
  CONSTRAINT fk_ProgConsos
    FOREIGN KEY (PROGRAMMEUR)
    REFERENCES PROGRAMMEURS(ID)
);
INSERT INTO THIERRYE.PROGRAMMEURS (ID, NOM, PRENOM, BUREAU) 
	VALUES (1, 'THIERRY', 'Eric', 2);
INSERT INTO THIERRYE.PROGRAMMEURS (ID, NOM, PRENOM, BUREAU) 
	VALUES (2, 'LAGIER', 'Gaetan', 2);
INSERT INTO THIERRYE.PROGRAMMEURS (ID, NOM, PRENOM, BUREAU) 
	VALUES (3, 'COUSSOT', 'Charly', 7);
INSERT INTO THIERRYE.PROGRAMMEURS (ID, NOM, PRENOM, BUREAU) 
	VALUES (4, 'CHAILLET', 'Maxime', 7);
INSERT INTO THIERRYE.PROGRAMMEURS (ID, NOM, PRENOM, BUREAU) 
	VALUES (5, 'RICHEL', 'Nicolas', 9);

INSERT INTO THIERRYE.CONSOS_CAFE (NO_SEMAINE, PROGRAMMEUR, NB_TASSES) 
	VALUES (1, 1, 0);
INSERT INTO THIERRYE.CONSOS_CAFE (NO_SEMAINE, PROGRAMMEUR, NB_TASSES) 
	VALUES (1, 2, 12);
INSERT INTO THIERRYE.CONSOS_CAFE (NO_SEMAINE, PROGRAMMEUR, NB_TASSES) 
	VALUES (1, 3, 8);
INSERT INTO THIERRYE.CONSOS_CAFE (NO_SEMAINE, PROGRAMMEUR, NB_TASSES) 
	VALUES (1, 4, 6);
INSERT INTO THIERRYE.CONSOS_CAFE (NO_SEMAINE, PROGRAMMEUR, NB_TASSES) 
	VALUES (1, 5, 15);
INSERT INTO THIERRYE.CONSOS_CAFE (NO_SEMAINE, PROGRAMMEUR, NB_TASSES) 
	VALUES (2, 1, 0);
INSERT INTO THIERRYE.CONSOS_CAFE (NO_SEMAINE, PROGRAMMEUR, NB_TASSES) 
	VALUES (2, 2, 3);
INSERT INTO THIERRYE.CONSOS_CAFE (NO_SEMAINE, PROGRAMMEUR, NB_TASSES) 
	VALUES (2, 3, 10);
INSERT INTO THIERRYE.CONSOS_CAFE (NO_SEMAINE, PROGRAMMEUR, NB_TASSES) 
	VALUES (2, 4, 6);
INSERT INTO THIERRYE.CONSOS_CAFE (NO_SEMAINE, PROGRAMMEUR, NB_TASSES) 
	VALUES (2, 5, 8);




