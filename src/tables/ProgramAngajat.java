/**
 * Clasa ProgramAngajat reprezinta stocarea unui record din tabela program_angajati
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
import util.Program;

public class ProgramAngajat {
	public final static String table = "program_angajati";
	
	private int id;
	private int angajatId;
	private int policlinicaId;
	private Program program;

	public ProgramAngajat() {
		this.id = 0;
		this.angajatId = 0;
		this.policlinicaId = 0;
		this.program = null;
	}
	
	public ProgramAngajat(Database db, int id) {
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
	 * @return programul
	 */
	public Program getProgram() {
		return program;
	}

	/**
	 * @param programul ce trebuie setat
	 */
	public void setProgram(Program program) {
		this.program = program;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProgramAngajat [id=" + id + ", angajatId=" + angajatId + ", policlinicaId=" + policlinicaId
				+ ", program=" + program + "]";
	}
	
	/**
	 * Metoda ce genereaza o matrice M organizata astfel
	 * M[i][j] = {	
	 * 	0 - daca angajatul este liber in ziua i la minutul j
	 * 	1 - daca angajatul este disponibil in ziua i la minutul j
	 * 	2 - daca angajatul este ocupat in ziua i la minutul j 
	 * }
	 * Matricea reprezinta calcularea disponibilitatii unui angajat intr-o saptamana la fiecare minut
	 * @param baza de date pe care se opereaza
	 * @param angajatul
	 * @param policlinica
	 * @param data in jurul careia se doreste procesarea orarului
	 * @return matricea M
	 */
	public static int[][] getOrar(Database db, Angajat a, Policlinica poli, DateTime dt) {
		String query = "SELECT * FROM `" + table + "` WHERE angajat_id = '" + a.getID() + "' AND policlinica_id = '" + poli.getID() + "'";
		ArrayList<String[]> result = db.doQuery(query);
		
		if (result == null)
			return null;
		
		int[][] orar = new int[7][1440];
		for (int i = 0; i < 7; ++i)
			for (int j = 0; j < 1440; ++j)
				orar[i][j] = 0;
		
		for (String[] row : result) {
			Program p = new Program(row[3]);
			
			for (int dayIndex = 0; dayIndex < 7; ++dayIndex)
				for (int minut = p.get(dayIndex).getInceput() * 60; minut <= p.get(dayIndex).getSfarsit() * 60; ++minut) {
					orar[dayIndex][minut] = 1;
				}
			
		}
		
		DateTime weekFirst = dt.getWeekFirstDay();
		DateTime weekLast = dt.getWeekLastDay();
		
		query = "CALL GET_OWN_SCHEDULE_LIST_DATE_AND_DURATION('" + a.getID() + "', '" + weekFirst.convertToDateTime() + "', '" + weekLast.convertToDateTime() + "')";
		
		ArrayList<String[]> res = db.doQuery(query);
		
		if (res != null) {
			for (String[] row : res) {
				DateTime dataProgramare = new DateTime(row[0]);
				int durata = Integer.parseInt(row[1]);
				
				int dayOfWeek = dataProgramare.getDayIndex();
				int minutProgramare = Integer.parseInt(dataProgramare.getOra()) * 60;
				
				for (int minut = minutProgramare; minut <= minutProgramare + durata; ++ minut) {
					orar[ dayOfWeek ][ minut ] = 2;
				}
			}
		}
		
		return orar;
	}
	
	/**
	 * Metoda ce genereaza o matrice M organizata astfel
	 * M[i][j] = {	
	 * 	0 - daca angajatul este liber in ziua i la minutul j
	 * 	1 - daca angajatul este disponibil in ziua i la minutul j
	 * }
	 * Matricea reprezinta calcularea disponibilitatii unui angajat intr-o saptamana la fiecare minut
	 * @param baza de date pe care se opereaza
	 * @param angajatul
	 * @param policlinica
	 * @return matricea M
	 */
	public static int[][] getOrar(Database db, Angajat a, Policlinica poli) {
		String query = "SELECT * FROM `" + table + "` WHERE angajat_id = '" + a.getID() + "' AND policlinica_id = '" + poli.getID() + "'";
		ArrayList<String[]> result = db.doQuery(query);
		
		if (result == null)
			return null;
		
		int[][] orar = new int[7][1440];
		for (int i = 0; i < 7; ++i)
			for (int j = 0; j < 1440; ++j)
				orar[i][j] = 0;
		
		for (String[] row : result) {
			Program p = new Program(row[3]);
			
			for (int dayIndex = 0; dayIndex < 7; ++dayIndex)
				for (int minut = p.get(dayIndex).getInceput() * 60; minut <= p.get(dayIndex).getSfarsit() * 60; ++minut) {
					orar[dayIndex][minut] = 1;
				}
			
		}
		return orar;
	}
	
	/**
	 * @param baza de date unde opereaza
	 */
	public void extractFromDatabase(Database db) {
		String query = "SELECT * FROM `" + table + "` WHERE id = '" + this.id + "' LIMIT 1";
		ArrayList<String[]> result = db.doQuery(query);
		
		for (String[] row : result) {
			
			this.angajatId = Integer.parseInt(row[1]);
			this.policlinicaId = Integer.parseInt(row[2]);
			this.program = new Program(row[3]);
		}
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void updateOnDatabase(Database db) {	
		String query = "UPDATE `" + table + "` "
				+ "SET  angajat_id = '" + this.getAngajatID() 
				+ "', policilinica_id = '" + this.getPoliclinicaID() 
				+ "', program = '" + this.getProgram().compressProgram() + "' "
				+ "WHERE id = '" + this.id + "'";
		
		int rowCount = db.doUpdate(query);
		
		if (rowCount == 0) {
			ErrorLog.printError("Nu a fost gasit niciun program de angajat cu id-ul " + id);
		} else if (rowCount > 1) {
			ErrorLog.printError("Au fost gasite mai multe programe de angajati cu id-ul " + id);
		}
	}

	/**
	 * @param baza de date unde opereaza
	 * @return lista programelor angajatilor din baza de date
	 */
	public static ArrayList<ProgramAngajat> downloadDatabase(Database db) {
		ArrayList<ProgramAngajat> result = new ArrayList<ProgramAngajat>();
	
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
			ProgramAngajat p = new ProgramAngajat(db, index);
			if (p != null)
				result.add(p);
		}
		
		return result;
	}

	/**
	 * @param baza de date unde opereaza
	 * @param lista programelor angajatilor ce trebuie actualizate in baza de date
	 */
	public static void uploadDatabase(Database db, ArrayList<ProgramAngajat> proList) {	
		for (ProgramAngajat a : proList) {
			a.updateOnDatabase(db);
		}
	}
}
