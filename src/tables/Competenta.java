/**
 * Clasa Competenta reprezinta stocarea unui record din tabela competente
 */
package tables;

/**
 * @author Ana
 *
 */

import java.util.ArrayList;

import util.Database;
import util.ErrorLog;

public class Competenta {
	public static final String table = "competente";
	
	private int id;
	private String denumire;

	public Competenta() {
		this.id = 0;
		this.denumire = null;
	}
	
	public Competenta(Database db, int id) {
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
	 * @return denumirea
	 */
	public String getDenumire() {
		return denumire;
	}

	/**
	 * @param denumirea ce trebuie setata
	 */
	public void setDenumire(String denumire) {
		this.denumire = denumire;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Competenta [id=" + id + ", denumire=" + denumire + "]";
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void extractFromDatabase(Database db) {
		String query = "SELECT * FROM `" + table + "` WHERE id = '" + this.id + "' LIMIT 1";
		ArrayList<String[]> result = db.doQuery(query);
		
		for (String[] row : result) {
			
			this.denumire = row[1];

		}
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void updateOnDatabase(Database db) {		
		String query = "UPDATE `" + table + "` "
				+ "SET denumire = '" + this.getDenumire()  + "' "
				+ "WHERE id = '" + this.id + "'";
		
		int rowCount = db.doUpdate(query);
		
		if (rowCount == 0) {
			ErrorLog.printError("Nu a fost gasita nicio competenta cu id-ul " + id);
		} else if (rowCount > 1) {
			ErrorLog.printError("Au fost gasite mai multe competente cu id-ul " + id);
		}
	}

	/**
	 * @param baza de date unde opereaza
	 * @return lista competentelor din baza de date
	 */
	public static ArrayList<Competenta> downloadDatabase(Database db) {
		ArrayList<Competenta> result = new ArrayList<Competenta>();
	
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
			Competenta c = new Competenta(db, index);
			if (c != null)
				result.add(c);
		}
		
		return result;
	}

	/**
	 * @param baza de date unde opereaza
	 * @param lista competentelor ce trebuie actualizate in baza de date
	 */
	public static void uploadDatabase(Database db, ArrayList<Competenta> compList) {	
		for (Competenta c : compList) {
			c.updateOnDatabase(db);
		}
	}
}
