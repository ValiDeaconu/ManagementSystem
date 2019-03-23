/**
 * Clasa Programare reprezinta stocarea unui record din tabela programari
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

public class Programare {	
	public static final String table = "programari";
	
	private int id;
	private int pacientId;
	private int competentaId;
	private int medicId;
	private int policlinicaId;
	private int serviciuId;
	private DateTime dataProgramare;
	private int status;
	
	public Programare() {
		this.id = 0;
		this.pacientId = 0;
		this.competentaId = 0;
		this.medicId = 0;
		this.policlinicaId = 0;
		this.serviciuId = 0;
		this.dataProgramare = null;
		this.status = 0;
	}
	
	public Programare(Database db, int id) {
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
	 * @return ID-ul pacientului
	 */
	public int getPacientID() {
		return pacientId;
	}
	
	/**
	 * @param ID-ul pacientului ce trebuie setat
	 */
	public void setPacientID(int pacientId) {
		this.pacientId = pacientId;
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
	 * @return ID-ul policlinicii
	 */
	public int getPoliclinicaID() {
		return policlinicaId;
	}
	
	/**
	 * @param ID-ul policlinicii ce trebuie setat
	 */
	public void setPoliclinicaID(int policlinicaId) {
		this.policlinicaId = policlinicaId;
	}
	
	/**
	 * @return ID-ul denumirii serviciului
	 */
	public int getServiciuID() {
		return serviciuId;
	}

	/**
	 * @param ID-ul denumirii serviciului ce trebuie setat
	 */
	public void setServiciuID(int serviciuId) {
		this.serviciuId = serviciuId;
	}

	/**
	 * @return data programarii
	 */
	public DateTime getDataProgramare() {
		return dataProgramare;
	}
	
	/**
	 * @param data programarii ce trebuie setata
	 */
	public void setDataProgramare(DateTime dataProgramare) {
		this.dataProgramare = dataProgramare;
	}
	
	/**
	 * @return statusul
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * @param statusul ce trebuie setat
	 * @return daca statusul a fost setat
	 */
	public boolean setStatus(int status) {
		if (status < 0 || status > 2)
			return false;
		
		this.status = status;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Programare [id=" + id + ", pacientId=" + pacientId + ", competentaId=" + competentaId + ", medicId="
				+ medicId + ", policlinicaId=" + policlinicaId + ", serviciuId=" + serviciuId + ", dataProgramare="
				+ dataProgramare + ", status=" + status + "]";
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void extractFromDatabase(Database db) {
		String query = "SELECT * FROM `" + table + "` WHERE id = '" + this.id + "' LIMIT 1";
		ArrayList<String[]> result = db.doQuery(query);
		
		for (String[] row : result) {
			this.pacientId = Integer.parseInt(row[1]);
			this.competentaId = Integer.parseInt(row[2]);
			if (row[3] == null)
				this.medicId = -1;
			else
				this.medicId = Integer.parseInt(row[3]);
			this.policlinicaId = Integer.parseInt(row[4]);
			this.serviciuId = Integer.parseInt(row[5]);
			this.dataProgramare = new DateTime(row[6]);
			this.status = Integer.parseInt(row[7]);
		}
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void updateOnDatabase(Database db) {		
		String query = "UPDATE `" + table + "` "
				+ "SET pacient_id = '" + this.getPacientID() 
				+ "', competenta_id = '" + this.getCompetentaID() 
				+ "', medic_id = '" + this.getMedicID()
				+ "', policlinica_id = '" + this.getPoliclinicaID()
				+ "', serviciu_id = '" + this.getServiciuID()
				+ "', data_programare = '" + this.getDataProgramare()
				+ "', status = '" + this.getStatus()  + "' "
				+ "WHERE id = '" + this.id + "'";
		
		int rowCount = db.doUpdate(query);
		
		if (rowCount == 0) {
			ErrorLog.printError("Nu a fost gasit nicio programare cu id-ul " + id);
		} else if (rowCount > 1) {
			ErrorLog.printError("Au fost gasite mai multe programari cu id-ul " + id);
		}
	}

	/**
	 * @param baza de date unde opereaza
	 * @return lista programarilor din baza de date
	 */
	public static ArrayList<Programare> downloadDatabase(Database db) {
		ArrayList<Programare> result = new ArrayList<Programare>();
	
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
			return null;
		} else if (rowCount > 1) {
			ErrorLog.printError("Ceva a mers gresit in selectia min(id), max(id) @ " + table + ".");	
			return null;
		}
		
		for (int index = min_id; index <= max_id; ++index) {
			Programare p = new Programare(db, index);
			if (p != null)
				result.add(p);
		}
		
		return result;
	}

	/**
	 * @param baza de date unde opereaza
	 * @param lista programarilor ce trebuie actualizate in baza de date
	 */
	public static void uploadDatabase(Database db, ArrayList<Programare> progrList) {	
		for (Programare p : progrList) {
			p.updateOnDatabase(db);
		}
	}
	

}
