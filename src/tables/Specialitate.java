/**
 * Clasa Specialitate reprezinta stocarea unui record din tabela specialitati
 */
package tables;

/**
 * @author Ana
 *
 */

import java.util.ArrayList;

import util.Database;
import util.ErrorLog;

public class Specialitate {	
	public static final String table = "specialitati";
	
	private int id;
	private int medicId;
	private int competentaId;
	private String grad;
	
	public Specialitate() {
		this.id = 0;
		this.medicId = 0;
		this.competentaId = 0;
		this.grad = null;
	}
	
	public Specialitate(Database db, int id) {
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
	
	/**
	 * @return ID-ul competentei
	 */
	public int getCompetentaID() {
		return competentaId;
	}
	
	/**
	 * @param ID-ul competentei ce trebuie setat
	 */
	public void setCompetentaID(int competentaId) {
		this.competentaId = competentaId;
	}
	
	/**
	 * @return gradul
	 */
	public String getGrad() {
		return grad;
	}
	
	/**
	 * @param gradul ce trebuie setat
	 */
	public void setGrad(String grad) {
		this.grad = grad;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Specialitate [id=" + id + ", medicId=" + medicId + ", competentaId=" + competentaId + ", grad=" + grad
				+ "]";
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void extractFromDatabase(Database db) {
		String query = "SELECT * FROM `" + table + "` WHERE id = '" + this.id + "' LIMIT 1";
		ArrayList<String[]> result = db.doQuery(query);
		
		for (String[] row : result) {
			
			this.medicId = Integer.parseInt(row[1]);
			this.competentaId = Integer.parseInt(row[2]);
			this.grad = row[3];
		}
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void updateOnDatabase(Database db) {		
		String query = "UPDATE `" + table + "` "
				+ "SET medic_id = '" + this.getMedicID() 
				+ "', competenta_id = '" + this.getCompetentaID() 
				+ "', grad = '" + this.getGrad()  + "' "
				+ "WHERE id = '" + this.id + "'";
		
		int rowCount = db.doUpdate(query);
		
		if (rowCount == 0) {
			ErrorLog.printError("Nu a fost gasita nicio specialitate cu id-ul " + id);
		} else if (rowCount > 1) {
			ErrorLog.printError("Au fost gasite mai multe specialitati cu id-ul " + id);
		}
	}

	/**
	 * @param baza de date unde opereaza
	 * @return lista specialitatilor din baza de date
	 */
	public static ArrayList<Specialitate> downloadDatabase(Database db) {
		ArrayList<Specialitate> result = new ArrayList<Specialitate>();
	
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
			Specialitate p = new Specialitate(db, index);
			if (p != null)
				result.add(p);
		}
		
		return result;
	}

	/**
	 * @param baza de date unde opereaza
	 * @param lista specialitatilor ce trebuie actualizate in baza de date
	 */
	public static void uploadDatabase(Database db, ArrayList<Specialitate> specList) {	
		for (Specialitate s : specList) {
			s.updateOnDatabase(db);
		}
	}
	
}
