CREATE TABLE Adherent
  (
    Num_Adherent    NUMBER NOT NULL ,
    Prenom_Adherent VARCHAR2(50) ,
    Nom_Adherent    VARCHAR2(50)
  ) ;
ALTER TABLE Adherent ADD CONSTRAINT Adherent_PK PRIMARY KEY ( Num_Adherent ) ;

CREATE TABLE Emprunt
  (
    Num_Exemplaire     NUMBER NOT NULL ,
    Num_Adherent       NUMBER NOT NULL ,
    Date_Emprunt       DATE ,
    DateRetour_Emprunt DATE
  ) ;
ALTER TABLE Emprunt ADD CONSTRAINT Emprunt_PK PRIMARY KEY ( Num_Exemplaire ) ;

CREATE TABLE Exemplaire
  (
    Num_Exemplaire NUMBER NOT NULL ,
    Num_Livre      NUMBER NOT NULL
  ) ;
ALTER TABLE Exemplaire ADD CONSTRAINT Exemplaire_PK PRIMARY KEY ( Num_Exemplaire ) ;

CREATE TABLE Genre
  ( 
    Code_Genre NUMBER NOT NULL ,
    Nom_Genre VARCHAR2(50)
  ) ;
ALTER TABLE Genre ADD CONSTRAINT Genre_PK PRIMARY KEY ( Code_Genre ) ;

CREATE TABLE Livre
  (
    Num_Livre    NUMBER NOT NULL ,
    Code_Genre   NUMBER NOT NULL ,
    Titre_Livre  VARCHAR2(100) ,
    Auteur_Livre VARCHAR2(50) ,
    Année_Livre  NUMBER(4)
  ) ;
ALTER TABLE Livre ADD CONSTRAINT Livre_PK PRIMARY KEY ( Num_Livre ) ;

ALTER TABLE Emprunt ADD CONSTRAINT Emprunt_Adherent_FK FOREIGN KEY ( Num_Adherent ) REFERENCES Adherent ( Num_Adherent ) ;

ALTER TABLE Exemplaire ADD CONSTRAINT Exemplaire_Emprunt_FK FOREIGN KEY ( Num_Exemplaire ) REFERENCES Emprunt ( Num_Exemplaire ) ;

ALTER TABLE Exemplaire ADD CONSTRAINT Exemplaire_Livre_FK FOREIGN KEY ( Num_Livre ) REFERENCES Livre ( Num_Livre ) ;

ALTER TABLE Livre ADD CONSTRAINT Livre_Genre_FK FOREIGN KEY ( Code_Genre ) REFERENCES Genre ( Code_Genre ) ;
