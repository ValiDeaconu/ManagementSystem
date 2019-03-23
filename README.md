# MEDHEAL SYSTEM

## Romana

## Descriere

Implementarea unui sistem de gestiune al unui lant de policlinici.

## Instalare

* Se executa fisierul /mysql_database_creation/database.sql.
* Se modifica linia 86 din /src/application/Main.java cu noile date ale serverului de MySQL.
* Se adauga un utilizator de tip super administrator in baza de date.
* Se adauga o policlinica in baza de date.
* Se modifica fisierul /resources/policlinici.config cu id-ul policlinicii adaugate la pasul anterior.

Baza de date fiind deja populata, exista conturi deja create. Acestea pot fi gasite in /conturi_testare.txt.

## Librarii utilizate

* [JCalendar](https://toedter.com/jcalendar/)
* [JGoodies](http://www.jgoodies.com/)
* [JUnit](https://junit.org/junit5/)
* [Connector/J](https://dev.mysql.com/downloads/connector/j/8.0.html)
* [SwingX](http://www.java2s.com/Code/Jar/s/Downloadswingxall164jar.htm)

## Autori

* **Deaconu Lucian-Valentin**
* **Dragos Ana-Maria**

Grupa 30221, Facultatea de Automatica si Calculatoare, Universitatea Tehnica din Cluj-Napoca, 2019

## Licenta

Acest proiect este licentiat sub GNU GENERAL PUBLIC LICENSE - vezi fisierul [LICENSE](LICENSE) pentru detalii 

## English

## Description

A managing system for a chain of medical centers.

## Installation

* Upload /mysql_database_creation/database.sql on your own MySQL server.
* Change line 86 on /src/application/Main.java with your MySQL details.
* Add a super admin type user.
* Add a medical center.
* Change ID in /resources/policlinici.config with your medical center's ID gained on previous step.

Database is already populated, so you can jump over last 3 steps and use one of accounts that can be found on /conturi_testare.txt.

## Libraries used

* [JCalendar](https://toedter.com/jcalendar/)
* [JGoodies](http://www.jgoodies.com/)
* [JUnit](https://junit.org/junit5/)
* [Connector/J](https://dev.mysql.com/downloads/connector/j/8.0.html)
* [SwingX](http://www.java2s.com/Code/Jar/s/Downloadswingxall164jar.htm)

## Authors

* **Deaconu Lucian-Valentin**
* **Dragos Ana-Maria**

Group 30221, Faculty of Computer Science, Technical University of Cluj-Napoca, 2019

## License

This project is licensed under GNU GENERAL PUBLIC LICENSE - see [LICENSE](LICENSE) for details.
