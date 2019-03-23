/**
 * Clasa Angajat reprezinta stocarea unui record din tabela angajati
 */
package tables;

/**
 * @author Vali
 *
 */

import java.util.ArrayList;

import util.Database;
import util.DateTime;
import util.ErrorLog;

public class Angajat {
	public static final String table = "angajati";
	
	protected int id;
	private String username;
	private String nume;
	private String prenume;
	private String cnp;
	private String adresa;
	private String telefon;
	private String email;
	private String iban;
	private String nrContract;
	private DateTime dataAngajarii;
	private int functie;
	private int adminType;
	private int salariu;
	private int nrOre;
	
	public Angajat() {
		this.id = 0;
		this.username = null;
		this.nume = null;
		this.prenume = null;
		this.cnp = null;
		this.adresa = null;
		this.telefon = null;
		this.email = null;
		this.iban = null;
		this.nrContract = null;
		this.dataAngajarii = null;
		this.functie = 0;
		this.adminType = 0;
		this.salariu = 0;
		this.nrOre = 0;
	}

	public Angajat(Database db, int id) {
		this.id = id;
		this.extractFormDatabase(db);
	}

	/**
	 * @return id-ul
	 */
	public int getID() {
		return id;
	}

	/**
	 * @return username-ul
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username username-ul ce trebuie setat
	 * @return daca username-ul s-a setat
	 */
	public boolean setUsername(String username) {
		if (username.length() < 8) {
			return false;
		}
		
		this.username = username;
		return true;
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
	 * @return prenumele
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
	 * @return numarul de telefonul
	 */
	public String getTelefon() {
		return telefon;
	}

	/**
	 * @param numarul de telefon ce trebuie setat
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

	/**
	 * @return codul IBAN
	 */
	public String getIBAN() {
		return iban;
	}

	/**
	 * @param codul IBAN ce trebuie setat
	 * @return daca IBAN-ul s-a setat
	 */
	public boolean setIBAN(String iban) {
		if (iban.length() != 24)
			return false;
		
		this.iban = iban;
		return true;
	}

	/**
	 * @return numarul contractului
	 */
	public String getNrContract() {
		return nrContract;
	}

	/**
	 * @param numarul de contract ce trebuie setat
	 */
	public void setNrContract(String nrContract) {
		this.nrContract = nrContract;
	}

	/**
	 * @return data angajarii
	 */
	public DateTime getDataAngajarii() {
		return dataAngajarii;
	}

	/**
	 * @param data angajarii ce trebuie setata
	 */
	public void setDataAngajarii(DateTime dataAngajarii) {
		this.dataAngajarii = dataAngajarii;
	}

	/**
	 * @return codul functiei
	 */
	public int getFunctie() {
		return functie;
	}

	/**
	 * @param codul functiei ce trebuie setat
	 */
	public void setFunctie(int functie) {
		this.functie = functie;
	}

	/**
	 * @return gradul de admin
	 */
	public int getAdminType() {
		return adminType;
	}

	/**
	 * @param gradul de admin ce trebuie setat
	 */
	public void setAdminType(int adminType) {
		this.adminType = adminType;
	}

	/**
	 * @return salariul
	 */
	public int getSalariu() {
		return salariu;
	}

	/**
	 * @param salariul ce trebuie setat
	 */
	public void setSalariu(int salariu) {
		this.salariu = salariu;
	}

	/**
	 * @return numarul de ore
	 */
	public int getNrOre() {
		return nrOre;
	}

	/**
	 * @param numarul de ore ce trebuie setat
	 */
	public void setNrOre(int nrOre) {
		this.nrOre = nrOre;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Angajat [id=" + id + ", username=" + username + ", nume=" + nume
				+ ", prenume=" + prenume + ", cnp=" + cnp + ", adresa=" + adresa + ", telefon=" + telefon + ", email="
				+ email + ", iban=" + iban + ", nr_contract=" + nrContract + ", dataAngajarii=" + dataAngajarii
				+ ", functie=" + functie + ", admin_type=" + adminType + ", salariu=" + salariu + ", nrOre=" + nrOre
				+ "]";
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void extractFormDatabase(Database db) {
		String query = "SELECT * FROM `" + table + "` WHERE id = '" + this.id + "' LIMIT 1";
		ArrayList<String[]> result = db.doQuery(query);
		
		for (String[] row : result) {
			// id, username, password, nume, prenume, cnp, adresa, telefon, email, iban
			// nr_contract, data_angajarii, functie, admin_type, salariu, nr_ore
			
			this.username = row[1];
			// row[2] parola nu o stocam, deoarece este criptata in SHA2
			this.nume = row[3];
			this.prenume = row[4];
			this.cnp = row[5];
			this.adresa = row[6];
			this.telefon = row[7];
			this.email = row[8];
			this.iban = row[9];
			this.nrContract = row[10];
			this.dataAngajarii = new DateTime(row[11]);
			this.functie = Integer.parseInt(row[12]);
			this.adminType = Integer.parseInt(row[13]);
			this.salariu = Integer.parseInt(row[14]);
			this.nrOre = Integer.parseInt(row[15]);
		}
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void updateOnDatabase(Database db) {		
		// id, username, password, nume, prenume, cnp, adresa, telefon, email, iban
		// nr_contract, data_angajarii, functie, admin_type, salariu, nr_ore
		String query = "UPDATE `" + table + "` "
				+ "SET username = '" + this.getUsername() 
				+ "', nume = '" + this.getNume() 
				+ "', prenume = '" + this.getPrenume() 
				+ "', cnp = '" + this.getCNP() 
				+ "', adresa = '" + this.getAdresa()
				+ "', telefon = '" + this.getTelefon()
				+ "', email = '" + this.getEmail()
				+ "', iban = '" + this.getIBAN() 
				+ "', nr_contract = '" + this.getNrContract()
				+ "', data_angajarii = '" + this.getDataAngajarii() 
				+ "', functie = '" + this.getFunctie() 
				+ "', admin_type = '" + this.getAdminType() 
				+ "', salariu = '" + this.getSalariu() 
				+ "', nr_ore = '" + this.getNrOre() + "' "
				+ "WHERE id = '" + this.id + "'";
		
		int rowCount = db.doUpdate(query);
		
		if (rowCount == 0) {
			ErrorLog.printError("Nu a fost gasit niciun angajat cu id-ul " + id);
		} else if (rowCount > 1) {
			ErrorLog.printError("Au fost gasite mai multi angajati cu id-ul " + id);
		}
	}
	
	/**
	 * Metoda ce insereaza utilizatorul in baza de date (se foloseste pentru adaugarea de noi utilizatori)
	 * @param baza de date pe care se opereaza
	 * @param parola utilizatorului
	 */
	public void insertIntoDatabase(Database db, String password) {
		String query = "INSERT INTO `" + table + "` (username, password, nume, prenume, cnp, adresa, telefon, "
						+ "email, iban, nr_contract, data_angajarii, functie, admin_type, salariu, nr_ore) VALUES ("
						+ "'" + this.getUsername() + "', "
						+ "SHA2('" + password + "', 512), "
						+ "'" + this.getNume() + "', "
						+ "'" + this.getPrenume() + "', "
						+ "'" + this.getCNP() + "', "
						+ "'" + this.getAdresa() + "', "
						+ "'" + this.getTelefon() + "', "
						+ "'" + this.getEmail() + "', "
						+ "'" + this.getIBAN() + "', "
						+ "'" + this.getNrContract() + "', "
						+ "NOW(), "
						+ "'" + this.getFunctie() + "', "
						+ "'2', "
						+ "'" + this.getSalariu() + "', "
						+ "'" + this.getNrOre() + "');";

		int rowCount = db.doUpdate(query);
		
		if (rowCount == 0) {
			ErrorLog.printError("Nu s-a putut insera noul utilizator " + this.getUsername());
		} else if (rowCount > 1) {
			ErrorLog.printError("Au fost inserati mai multi utilizatori " + this.getUsername());
		}
	}

	/**
	 * @param baza de date unde opereaza
	 * @return lista angajatilor din baza de date
	 */
	public static ArrayList<Angajat> downloadDatabase(Database db) {
		ArrayList<Angajat> result = new ArrayList<Angajat>();
	
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
			Angajat a = new Angajat(db, index);
			if (a != null)
				result.add(a);
		}
		
		return result;
	}

	/**
	 * @param baza de date unde opereaza
	 * @param lista angajatilor ce trebuie actualizati in baza de date
	 */
	public static void uploadDatabase(Database db, ArrayList<Angajat> list) {	
		for (Angajat a : list) {
			a.updateOnDatabase(db);
		}
	}

	/**
	 * @param baza de date unde opereaza
	 * @param parola ce trebuie verificata
	 * @return validarea parolei
	 */
	public boolean isPasswordCorrect(Database db, String password) {
		String query = "SELECT * FROM `" + table + "` WHERE id = '" + this.getID() + "' AND password = SHA2('" + password + "', 512)";
		ArrayList<String[]> res = db.doQuery(query);
		
		int rowCount = 0;
		if (res != null)
			rowCount = res.size();	
		
		if (rowCount == 0) {
			// am gasit
			return false;
		} else if (rowCount == 1) {
			// n-am gasit
			return true;
		}
		
		// daca rowCount > 1 este o problema
		ErrorLog.printError("Exista id-uri multiple: " + this.getID());
		return false;
	}
}
