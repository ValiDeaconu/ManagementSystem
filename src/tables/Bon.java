/**
 * Clasa Bon reprezinta stocarea unui record din tabela bonuri_emise
 */
package tables;

/**
 * @author Ana
 *
 */

import java.util.ArrayList;

import util.Database;
import util.DateTime;
import util.ErrorLog;

public class Bon {
	
	private int id;
	private int programareId;
	private int suma;
	private DateTime dataTiparire;
	
	public final static String table = "bonuri_emise";

	public Bon() {
		this.id = 0;
		this.programareId = 0;
		this.suma = 0;
		this.dataTiparire = null;
	}
	
	public Bon(Database db, int id) {
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
	 * @return ID-ul programarii
	 */
	public int getProgramareID() {
		return programareId;
	}

	/**
	 * @param ID-ul programarii ce trebuie setat
	 */
	public void setProgramareID(int programareId) {
		this.programareId = programareId;
	}

	/**
	 * @return suma
	 */
	public int getSuma() {
		return suma;
	}

	/**
	 * @param suma ce trebuie setata
	 * @return daca suma a fost setata
	 */
	public boolean setSuma(int suma) {
		if (suma < 0)
			return false;
		
		this.suma = suma;
		return true;
	}

	/**
	 * @return data tiparirii
	 */
	public DateTime getDataTiparire() {
		return dataTiparire;
	}

	/**
	 * @param data tiparirii ce trebuie setata
	 */
	public void setDataTiparire(DateTime dataTiparire) {
		this.dataTiparire = dataTiparire;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Bon [id=" + id + ", programareId=" + programareId + ", suma=" + suma + ", dataTiparire=" + dataTiparire + "]";
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void extractFromDatabase(Database db) {
		String query = "SELECT * FROM `" + table + "` WHERE id = '" + this.id + "' LIMIT 1";
		ArrayList<String[]> result = db.doQuery(query);
		
		for (String[] row : result) {
			this.programareId = Integer.parseInt(row[1]);
			this.suma = Integer.parseInt(row[2]);
			this.dataTiparire = new DateTime(row[3]);

		}
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void updateOnDatabase(Database db) {		
		String query = "UPDATE `" + table + "` "
				+ "SET programare_id = '" + this.getProgramareID() 
				+ "', suma = '" + this.getSuma() 
				+ "', data_tiparire ='" + this.getDataTiparire()  + "' "
				+ "WHERE id = '" + this.id + "'";
		
		int rowCount = db.doUpdate(query);
		
		if (rowCount == 0) {
			ErrorLog.printError("Nu a fost gasit niciun bon cu id-ul " + id);
		} else if (rowCount > 1) {
			ErrorLog.printError("Au fost gasite mai multe bonuri cu id-ul " + id);
		}
	}

	/**
	 * @param baza de date unde opereaza
	 * @return lista bonurilor din baza de date
	 */
	public static ArrayList<Bon> downloadDatabase(Database db) {
		ArrayList<Bon> result = new ArrayList<Bon>();
	
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
			Bon b = new Bon(db, index);
			if (b != null)
				result.add(b);
		}
		
		return result;
	}

	/**
	 * @param baza de date unde opereaza
	 * @param lista bonurilor ce trebuie actualizate in baza de date
	 */
	public static void uploadDatabase(Database db, ArrayList<Bon> bonList) {	
		for (Bon b : bonList) {
			b.updateOnDatabase(db);
		}
	}


	
	

}
