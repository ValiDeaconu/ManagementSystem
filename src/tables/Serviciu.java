/**
 * Clasa Serviciu reprezinta stocarea unui record din tabela servicii
 */

package tables;

/**
 * @author Ana
 *
 */

import java.util.ArrayList;

import util.Database;
import util.ErrorLog;

public class Serviciu {
	public static final String table = "servicii";
	
	private int id;
	private int denumireId;
	private int pret;
	private int durata;
	private int competentaId;
	private int medicId;
	
	public Serviciu() {
		this.id = 0;
		this.denumireId = 0;
		this.pret = 0;
		this.durata = 0;
		this.competentaId = 0;
		this.medicId = 0;
	}
	
	public Serviciu(Database db, int id) {
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
	 * @return ID-ul denumirii
	 */
	public int getDenumireID() {
		return denumireId;
	}

	/**
	 * @param ID-ul denumirii ce trebuie setat
	 */
	public void setDenumireID(int denumireId) {
		this.denumireId = denumireId;
	}

	/**
	 * @return pretul
	 */
	public int getPret() {
		return pret;
	}

	/**
	 * @param pretul ce trebuie setat
	 * @return daca pretul a fost setat
	 */
	public boolean setPret(int pret) {
		if (pret < 0)
			return false;
		
		this.pret = pret;
		return true;
	}

	/**
	 * @return durata
	 */
	public int getDurata() {
		return durata;
	}

	/**
	 * @param durata ce trebuie setata
	 * @return daca durata a fost setata
	 */
	public boolean setDurata(int durata) {
		if (durata < 0)
			return false;

		this.durata = durata;
		return true;
	}

	/**
	 * @return ID-ul competentei
	 */
	public int getCompetentaID() {
		return competentaId;
	}

	/**
	 * @param ID-ul compententei ce trebuie setat
	 */
	public void setCompetentaID(int competentaId) {
		this.competentaId = competentaId;
	}

	/**
	 * @return ID-ul medicului
	 */
	public int getMedicID() {
		return medicId;
	}

	/**
	 * @param ID-ul medicului ce trebuie setat
	 */
	public void setMedicID(int medicId) {
		this.medicId = medicId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Serviciu [id=" + id + ", denumireId=" + denumireId + ", pret=" + pret + ", competentaId=" + competentaId
				+ ", medicId=" + medicId + "]";
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void extractFromDatabase(Database db) {
		String query = "SELECT * FROM `" + table + "` WHERE id = '" + this.id + "' LIMIT 1";
		ArrayList<String[]> result = db.doQuery(query);
		
		if (result != null) {
			for (String[] row : result) {
				
				this.denumireId = Integer.parseInt(row[1]);
				this.pret = Integer.parseInt(row[2]);
				this.durata = Integer.parseInt(row[3]);
				this.competentaId = Integer.parseInt(row[4]);
				this.medicId = Integer.parseInt(row[5]);
			}
		}
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void updateOnDatabase(Database db) {		
		String query = "UPDATE `" + table + "` "
				+ "SET denumire_id = '" + this.getDenumireID() 
				+ "', pret = '" + this.getPret() 
				+ "', durata = '" + this.getDurata() 
				+ "', competenta_id = '" + this.getCompetentaID() 
				+ "', medic_id = '" + this.getMedicID()  + "' "
				+ "WHERE id = '" + this.id + "'";
		
		int rowCount = db.doUpdate(query);
		
		if (rowCount == 0) {
			ErrorLog.printError("Nu a fost gasit niciun serviciu cu id-ul " + id);
		} else if (rowCount > 1) {
			ErrorLog.printError("Au fost gasite mai multe servicii cu id-ul " + id);
		}
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void insertIntoDatabase(Database db) {		
		String query = "INSERT INTO `" + table + "` (denumire_id, pret, durata, competenta_id, medic_id) VALUES ("
				+ "'" + this.getDenumireID() + "', " 
				+ "'" + this.getPret() + "', "
				+ "'" + this.getDurata() + "', "
				+ "'" + this.getCompetentaID() + "', "
				+ "'" + this.getMedicID() + "')";
		
		int rowCount = db.doUpdate(query);
		
		if (rowCount == 0) {
			ErrorLog.printError("Nu s-a putut insera serviciul " + this.toString());
		} else if (rowCount > 1) {
			ErrorLog.printError("Au fost inserate mai multe servicii " + this.toString());
		}
	}
	
	/**
	 * @param baza de date unde opereaza
	 * @return lista serviciilor din baza de date
	 */
	public static ArrayList<Serviciu> downloadDatabase(Database db) {
		ArrayList<Serviciu> result = new ArrayList<Serviciu>();
	
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
			Serviciu p = new Serviciu(db, index);
			if (p != null)
				result.add(p);
		}
		
		return result;
	}

	/**
	 * @param baza de date unde opereaza
	 * @param lista serviciilor ce trebuie actualizate in baza de date
	 */
	public static void uploadDatabase(Database db, ArrayList<Serviciu> servList) {	
		for (Serviciu s : servList) {
			s.updateOnDatabase(db);
		}
	}
	

}
