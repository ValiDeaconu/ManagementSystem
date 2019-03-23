/**
 * Clasa Raport reprezinta stocarea unui record din tabela rapoarte
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

public class Raport {
	public static final String table = "rapoarte";
	
	private int id;
	private int medicId;
	private int pacientId;
	private DateTime dataRaport;
	private String istoric;
	private String simptome;
	private String investigatii;
	private String diagnostic;
	private String recomandari;
	private boolean parafa;
	
	public Raport() {
		this.id = 0;
		this.medicId = 0;
		this.pacientId = 0;
		this.dataRaport = null;
		this.istoric = null;
		this.simptome = null;
		this.investigatii = null;
		this.diagnostic = null;
		this.recomandari = null;
		this.parafa = false;
	}
	
	public Raport(Database db, int id) {
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
	 * @return data raportului
	 */
	public DateTime getDataRaport() {
		return dataRaport;
	}

	/**
	 * @param data raportului ce trebuie setata
	 */
	public void setDataRaport(DateTime dataRaport) {
		this.dataRaport = dataRaport;
	}

	/**
	 * @return istoricul
	 */
	public String getIstoric() {
		return istoric;
	}

	/**
	 * @param istoricul ce trebuie setat
	 */
	public void setIstoric(String istoric) {
		this.istoric = istoric;
	}

	/**
	 * @return simptomele
	 */
	public String getSimptome() {
		return simptome;
	}

	/**
	 * @param simptomele ce trebuie setate
	 */
	public void setSimptome(String simptome) {
		this.simptome = simptome;
	}

	/**
	 * @return investigatiile
	 */
	public String getInvestigatii() {
		return investigatii;
	}

	/**
	 * @param investigatiile ce trebuie setate
	 */
	public void setInvestigatii(String investigatii) {
		this.investigatii = investigatii;
	}

	/**
	 * @return diagnosticul
	 */
	public String getDiagnostic() {
		return diagnostic;
	}

	/**
	 * @param diagnosticul ce trebuie setat
	 */
	public void setDiagnostic(String diagnostic) {
		this.diagnostic = diagnostic;
	}

	/**
	 * @return recomandari
	 */
	public String getRecomandari() {
		return recomandari;
	}

	/**
	 * @param recomandarile ce trebuie setate
	 */
	public void setRecomandari(String recomandari) {
		this.recomandari = recomandari;
	}

	/**
	 * @return daca a fost parafat raportul
	 */
	public boolean isParafa() {
		return parafa;
	}

	/**
	 * @param validarea parafei
	 */
	public void setParafa(boolean parafa) {
		this.parafa = parafa;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Raport [id=" + id + ", medicId=" + medicId + ", pacientId=" + pacientId + ", dataRaport=" + dataRaport
				+ ", istoric=" + istoric + ", simptome=" + simptome + ", investigatii="
				+ investigatii + ", diagnostic=" + diagnostic + ", recomandari=" + recomandari + ", parafa=" + parafa
				+ "]";
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void extractFromDatabase(Database db) {
		String query = "SELECT * FROM `" + table + "` WHERE id = '" + this.id + "' LIMIT 1";
		ArrayList<String[]> result = db.doQuery(query);
		
		if (result != null) {
			for (String[] row : result) {
				this.medicId = Integer.parseInt(row[1]);
				this.pacientId = Integer.parseInt(row[2]);
				this.dataRaport = new DateTime(row[3]);
				this.istoric = row[4];
				this.simptome = row[5];
				this.investigatii = row[6];
				this.diagnostic = row[7];
				this.recomandari = row[8];
				if (Integer.parseInt(row[9]) != 0) 
					this.parafa = true;
				else
					this.parafa = false;
				
			}
		}
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void updateOnDatabase(Database db) {	
		int i = this.isParafa()? 1 : 0;

		String query = "UPDATE `" + table + "` "
				+ "SET medic_id = '" + this.getMedicID() 
				+ "', pacient_id = '" + this.getPacientID() 
				+ "', data_raport = '" + this.getDataRaport()
				+ "', istoric = '" + this.getIstoric()
				+ "', simptome = '" + this.getSimptome()
				+ "', investigatii = '" + this.getInvestigatii()
				+ "', diagnostic = '" + this.getDiagnostic()
				+ "', recomandari = '" + this.getRecomandari()
				+ "', parafa = '" + i  + "' "
				+ "WHERE id = '" + this.id + "'";
		
		int rowCount = db.doUpdate(query);
		
		if (rowCount == 0) {
			ErrorLog.printError("Nu a fost gasit niciun raport cu id-ul " + id);
		} else if (rowCount > 1) {
			ErrorLog.printError("Au fost gasite mai multe rapoarte cu id-ul " + id);
		}
	}

	/**
	 * @param baza de date unde opereaza
	 * @return lista rapoartelor din baza de date
	 */
	public static ArrayList<Raport> downloadDatabase(Database db) {
		ArrayList<Raport> result = new ArrayList<Raport>();
	
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
			Raport r = new Raport(db, index);
			if (r != null)
				result.add(r);
		}
		
		return result;
	}

	/**
	 * @param baza de date unde opereaza
	 * @param lista rapoartelor ce trebuie actualizate in baza de date
	 */
	public static void uploadDatabase(Database db, ArrayList<Raport> rapList) {	
		for (Raport r : rapList) {
			r.updateOnDatabase(db);
		}
	}

}
