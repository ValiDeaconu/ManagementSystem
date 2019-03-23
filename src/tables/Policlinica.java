/**
 * Clasa Policlinici reprezinta stocarea unui record din tabela policlinici
 */
package tables;

/**
 * @author Vali
 *
 */

import java.util.ArrayList;

import util.Database;
import util.ErrorLog;
import util.Program;

public class Policlinica {
	public static final String table = "policlinici";
	
	private int id;
	private String denumire;
	private String adresa;
	private String descriere;
	private String telefon;
	private String email;
	private Program program;

	public Policlinica(Database db, int id) {
		this.id = id;
		this.extractFormDatabase(db);
	}
	
	public Policlinica() {
		this.id = 0;
		this.denumire = null;
		this.adresa = null;
		this.descriere = null;
		this.telefon = null;
		this.email = null;
		this.program = null;
	}
	
	/**
	 * @return ID-ul
	 */
	public int getID() {
		return id;
	}

	/** 
	 * @return denumirea
	 */
	public String getDenumire() {
		return denumire;
	}


	/**
	 * @param denumirea ce se doreste setata
	 */
	public void setDenumire(String denumire) {
		this.denumire = denumire;
	}


	/**
	 * @return numarul de telefon
	 */
	public String getTelefon() {
		return telefon;
	}


	/**
	 * @param numarul de telefon ce se doreste setat
	 */
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}


	/**
	 * @return descriere
	 */
	public String getDescriere() {
		return descriere;
	}


	/**
	 * @param descrierea ce se doreste setata
	 */
	public void setDescriere(String descriere) {
		this.descriere = descriere;
	}


	/**
	 * @return adresa
	 */
	public String getAdresa() {
		return adresa;
	}


	/**
	 * @param adresa ce se doreste setata
	 */
	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}


	/**
	 * @return e-mail-ul
	 */
	public String getEmail() {
		return email;
	}


	/**
	 * @param e-mail-ul ce se doreste setat
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return program-ul
	 */
	public Program getProgram() {
		return program;
	}

	/**
	 * @param programul ce trebuie setat dupa formatul: "L: 0-24; M: 0-24; Mi: 0-24; J: 0-24; V: 0-24; S: 0-24; D: 0-24"
	 */
	public void setProgram(Program program) {
		this.program = program;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Policlinica [id=" + id + ", denumire=" + denumire + ", adresa=" + adresa + ", descriere=" + descriere
				+ ", telefon=" + telefon + ", email=" + email + ", program=" + program + "]";
	}
	
	/**
	 * Metoda ce verifica daca un ID de policlinica este valid (daca exista in baza de date)
	 * @param baza de date pe care se operaeza
	 * @param ID-ul ce se doreste cautat
	 * @return daca acesta este valid
	 */
	public static boolean isValid(Database db, int index) {
		String query = "SELECT * FROM `" + table + "` WHERE id = '" + index + "' LIMIT 1";

		if (db.doQuery(query) != null) {
			return true;
		}
		
		return false;
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void extractFormDatabase(Database db) {
		String query = "SELECT * FROM `" + table + "` WHERE id = '" + this.id + "' LIMIT 1";
		ArrayList<String[]> result = db.doQuery(query);
		
		if (result != null) {
			for (String[] row : result) {
				// Coloanele o sa fie in ordinea din select
				// pt *: exact ordinea din tabel
				// id, denumire, adresa, descriere, telefon, email, program
				
				// nu am nevoie de id, deci row[0] il ignor
				
				this.denumire = row[1];
				this.adresa = row[2];
				this.descriere = row[3];
				this.telefon = row[4];
				this.email = row[5];
				this.program = new Program(row[6]);
			}
		}
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void updateOnDatabase(Database db) {		
		String query = "UPDATE `" + table + "` "
				+ "SET denumire = '" + this.getDenumire() 
				+ "', adresa = '" + this.getAdresa() 
				+ "', descriere = '" + this.getDescriere() 
				+ "', email = '" + this.getEmail() 
				+ "', telefon = '" + this.getTelefon() 
				+ "', program = '" + this.getProgram().compressProgram() + "' "
				+ "WHERE id = '" + this.id + "'";
		
		int rowCount = db.doUpdate(query);
		
		if (rowCount == 0) {
			ErrorLog.printError("Nu a fost gasita nicio policlinica cu id-ul " + id);
		} else if (rowCount > 1) {
			ErrorLog.printError("Au fost gasite mai multe policlinici cu id-ul " + id);
		}
	}
	
	/**
	 * Metoda ce insereaza policlinica in baza de date (se foloseste pentru adaugarea de noi policlinici)
	 * @param baza de date pe care se opereaza
	 */
	public void insertIntoDatabase(Database db) {
		String query = "INSERT INTO `" + table + "` (denumire, adresa, descriere, telefon, email, program) VALUES ("
						+ "'" + this.getDenumire() + "', "
						+ "'" + this.getAdresa() + "', "
						+ "'" + this.getTelefon() + "', "
						+ "'" + this.getEmail() + "', "
						+ "'" + this.getProgram().compressProgram() + "');";

		int rowCount = db.doUpdate(query);
		
		if (rowCount == 0) {
			ErrorLog.printError("Nu s-a putut insera noua policlinica " + this.getDenumire());
		} else if (rowCount > 1) {
			ErrorLog.printError("Au fost inserate mai multe policlinici " + this.getDenumire());
		}
	}

	/**
	 * @param baza de date unde opereaza
	 * @return lista policlinicilor din baza de date
	 */
	public static ArrayList<Policlinica> downloadDatabase(Database db) {
		ArrayList<Policlinica> result = new ArrayList<Policlinica>();
	
		int min_id = 0, max_id = 0, rowCount = 0, recordsCount = 0;
		String query = "SELECT COUNT(id), MIN(id), MAX(id) FROM `" + table + "`";
		ArrayList<String[]> res = db.doQuery(query);
		if (res != null) {
			for (String[] row : res) {
				recordsCount = Integer.parseInt(row[0]);
				if (recordsCount != 0) {
					min_id = Integer.parseInt(row[1]);
					max_id = Integer.parseInt(row[2]);
					++rowCount;
				} else {
					min_id = 0;
					max_id = 0;
					break;
				}
			}
		} else {
			rowCount = 0;
		}
		
		if (rowCount == 0) {
			ErrorLog.printError("Nu s-a putut selecta min(id), max(id) @ " + table + ".");
		} else if (rowCount > 1) {
			ErrorLog.printError("Ceva a mers gresit in selectia min(id), max(id) @ " + table + ".");			
		}
		
		for (int index = min_id; index <= max_id; ++index) {
			Policlinica p = new Policlinica(db, index);
			if (p != null)
				result.add(p);
		}
		
		return result;
	}

	/**
	 * @param baza de date unde opereaza
	 * @param lista policlinicilor ce trebuie actualizate in baza de date
	 */
	public static void uploadDatabase(Database db, ArrayList<Policlinica> poliList) {	
		for (Policlinica p : poliList) {
			p.updateOnDatabase(db);
		}
	}
	
}
