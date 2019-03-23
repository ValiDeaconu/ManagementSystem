/**
 * Clasa Pacient reprezinta stocarea unui record din tabela pacienti
 */
package tables;

import java.util.ArrayList;

import util.Database;
import util.ErrorLog;

/**
 * @author Ana
 *
 */
public class Pacient {	
	public static final String table = "pacienti";
	
	private int id;
	private String nume;
	private String prenume;
	private String cnp;
	private String adresa;
	private String telefon;
	private String email;
	
	public Pacient(int id, String nume, String prenume, String cnp, String adresa, String telefon, String email) {
		super();
		this.id = id;
		this.nume = nume;
		this.prenume = prenume;
		this.cnp = cnp;
		this.adresa = adresa;
		this.telefon = telefon;
		this.email = email;
	}
	
	public Pacient(Database db, int id) {
		this.setID(id);
		this.extractFromDatabase(db);
	}
	
	/**
	 * @return ID-ul
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * @param ID-ul ce trebuie setat
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * @return numele
	 */
	public String getNume() {
		return nume;
	}
	
	/**
	 * @param numele ce trebuie setat
	 */
	public void setNume(String nume) {
		this.nume = nume;
	}
	
	/**
	 * @return the prenume
	 */
	public String getPrenume() {
		return prenume;
	}
	
	/**
	 * @param prenumele ce trebuie setat
	 */
	public void setPrenume(String prenume) {
		this.prenume = prenume;
	}
	
	/**
	 * @return CNP-ul
	 */
	public String getCNP() {
		return cnp;
	}

	/**
	 * @param CNP-ul ce trebuie setat
	 * @return daca CNP-ul s-a setat
	 */
	public boolean setCNP(String cnp) {
		if (cnp.length() != 13) {
			return false;
		}
		
		this.cnp = cnp;
		return true;
	}
	
	/**
	 * @return adresa
	 */
	public String getAdresa() {
		return adresa;
	}
	
	/**
	 * @param adresa ce trebuie setata
	 */
	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}
	
	/**
	 * @return numarul de telefon
	 */
	public String getTelefon() {
		return telefon;
	}
	
	/**
	 * @param numarul de telefon telefonul ce trebuie setat
	 */
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	
	/**
	 * @return adresa de e-mail
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param adresa de e-mail ce trebuie setata
	 * @return daca adresa de e-mail s-a setat
	 */
	public boolean setEmail(String email) {
		if (!email.matches(".*@.*\\..*"))
			return false;
		
		this.email = email;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Pacient [id=" + id + ", nume=" + nume + ", prenume=" + prenume + ", cnp=" + cnp + ", adresa=" + adresa
				+ ", telefon=" + telefon + ", email=" + email + "]";
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void extractFromDatabase(Database db) {
		String query = "SELECT * FROM `" + table + "` WHERE id = '" + this.id + "' LIMIT 1";
		ArrayList<String[]> result = db.doQuery(query);
		
		for (String[] row : result) {
			
			this.nume = row[1];
			this.prenume = row[2];
			this.cnp = row[3];
			this.adresa = row[4];
			this.telefon = row[5];
			this.email = row[6];
		}
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void updateOnDatabase(Database db) {		
		String query = "UPDATE `" + table + "` "
				+ "SET nume = '" + this.getNume() 
				+ "', prenume = '" + this.getPrenume() 
				+ "', cnp = '" + this.getCNP() 
				+ "', adresa = '" + this.getAdresa() 
				+ "', telefon ='" + this.getTelefon()
				+ "', email ='" +  this.getEmail()  + "' "
				+ "WHERE id = '" + this.id + "'";
		
		int rowCount = db.doUpdate(query);
		
		if (rowCount == 0) {
			ErrorLog.printError("Nu a fost gasit niciun pacient cu id-ul " + id);
		} else if (rowCount > 1) {
			ErrorLog.printError("Au fost gasiti mai multi pacienti cu id-ul " + id);
		}
	}

	/**
	 * @param baza de date unde opereaza
	 * @return lista pacientilor din baza de date
	 */
	public static ArrayList<Pacient> downloadDatabase(Database db) {
		ArrayList<Pacient> result = new ArrayList<Pacient>();
	
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
			Pacient p = new Pacient(db, index);
			if (p != null)
				result.add(p);
		}
		
		return result;
	}

	/**
	 * @param baza de date unde opereaza
	 * @param lista pacientilor ce trebuie actualizati in baza de date
	 */
	public static void uploadDatabase(Database db, ArrayList<Pacient> pacList) {	
		for (Pacient p : pacList) {
			p.updateOnDatabase(db);
		}
	}

}
