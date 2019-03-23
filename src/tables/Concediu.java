/**
 * Clasa Concediu reprezinta stocarea unui record din tabela concedii
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

public class Concediu {
	public static final String table="concedii";

	private int id;
	private int angajatId;
	private DateTime dataInceput;
	private DateTime dataSfarsit;
	
	public Concediu() {
		this.id = 0;
		this.angajatId = 0;
		this.dataInceput = null;
		this.dataSfarsit = null;
	}
	
	public Concediu(Database db, int id) {
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
	 * @return ID-ul angajatului
	 */
	public int getAngajatID() {
		return angajatId;
	}

	/**
	 * @param ID-ul angajatului ce trebuie setat
	 */
	public void setAngajatID(int angajatId) {
		this.angajatId = angajatId;
	}

	/**
	 * @return data de inceput
	 */
	public DateTime getDataInceput() {
		return dataInceput;
	}

	/**
	 * @param data de inceput ce trebuie setata
	 */
	public void setDataInceput(DateTime dataInceput) {
		this.dataInceput = dataInceput;
	}

	/**
	 * @return data de sfarsit
	 */
	public DateTime getDataSfarsit() {
		return dataSfarsit;
	}

	/**
	 * @param data de sfarsit ce trebuie setata
	 */
	public void setDataSfarsit(DateTime dataSfarsit) {
		this.dataSfarsit = dataSfarsit;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getAngajatID() + " " + this.getDataInceput() + " " + this.getDataSfarsit();
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void extractFromDatabase(Database db) {
		String query = "SELECT * FROM `" + table + "` WHERE id = '" + this.id + "' LIMIT 1";
		ArrayList<String[]> result = db.doQuery(query);
		
		for (String[] row : result) {
			
			this.angajatId = Integer.parseInt(row[1]);
			this.dataInceput = new DateTime(row[2]);
			this.dataSfarsit = new DateTime(row[3]);
		}
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void updateOnDatabase(Database db) {		
		String query = "UPDATE `" + table + "` "
				+ "SET medic_id = '" + this.getAngajatID() 
				+ "', data_inceput = '" + this.getDataInceput() 
				+ "', data_sfarsit = '" + this.getDataSfarsit()  + "' "
				+ "WHERE id = '" + this.id + "'";
		
		int rowCount = db.doUpdate(query);
		
		if (rowCount == 0) {
			ErrorLog.printError("Nu a fost gasit niciun concediu cu id-ul " + id);
		} else if (rowCount > 1) {
			ErrorLog.printError("Au fost gasite mai multe concedii cu id-ul " + id);
		}
	}

	/**
	 * @param baza de date unde opereaza
	 * @return lista concediilor din baza de date
	 */
	public static ArrayList<Concediu> downloadDatabase(Database db) {
		ArrayList<Concediu> result = new ArrayList<Concediu>();
	
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
			Concediu p = new Concediu(db, index);
			if (p != null)
				result.add(p);
		}
		
		return result;
	}

	/**
	 * @param baza de date unde opereaza
	 * @param lista concediilor ce trebuie actualizate in baza de date
	 */
	public static void uploadDatabase(Database db, ArrayList<Concediu> concList) {	
		for (Concediu c : concList) {
			c.updateOnDatabase(db);
		}
	}
}
