/**
 * Clasa Asistent reprezinta stocarea unui record din tabela asistenti
 */

package tables;

import java.util.ArrayList;

import util.Database;
import util.ErrorLog;

/**
 * @author Ana
 *
 */

public class Asistent extends Angajat {
	public static final String table = "asistenti";

	private String tip;
	private String grad;
	
	public Asistent() {
		super();
		this.id = 0;
		this.tip = null;
		this.grad = null;
	}

	public Asistent(Database db, int id) {
		super(db, id);
		this.extractFromDatabase(db);
		
	}

	/**
	 * @return tipul
	 */
	public String getTip() {
		return tip;
	}

	/**
	 * @param tipul ce trebuie setat
	 */
	public void setTip(String tip) {
		this.tip = tip;
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

	/**
	 * @param angajatul ce trebuie modificat in asistent
	 * @return angajatul modificat in asistent
	 */
	public Asistent promoteAssistant(Angajat a) {
		Asistent as = (Asistent) a;
		as.setTip(null);
		as.setGrad(null);
		return as;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Asistent [id=" + id + ", tip=" + tip + ", grad=" + grad + "]";
	}	
	
	/**
	 * @param baza de date unde opereaza
	 */
	public void extractFromDatabase(Database db) {
		String query = "SELECT * FROM `" + table + "` WHERE id = '" + this.id + "' LIMIT 1";
		ArrayList<String[]> result = db.doQuery(query);
		
		for (String[] row : result) {
			
			this.tip = row[1];
			this.grad = row[2];
		}
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void updateOnDatabase(Database db) {		
		String query = "UPDATE `" + table + "` "
				+ "SET tip = '" + this.getTip() 
				+ "', grad = '" + this.getGrad()  + "' "
				+ "WHERE id = '" + this.id + "'";
		
		int rowCount = db.doUpdate(query);
		
		if (rowCount == 0) {
			ErrorLog.printError("Nu a fost gasit niciun asistent cu id-ul " + id);
		} else if (rowCount > 1) {
			ErrorLog.printError("Au fost gasiti mai multi asistenti cu id-ul " + id);
		}
	}

	/**
	 * @param baza de date unde opereaza
	 * @return lista asistentilor din baza de date
	 */
	public static ArrayList<Asistent> downloadAssistants(Database db) {
		ArrayList<Angajat> result = Angajat.downloadDatabase(db);
		ArrayList<Asistent> assResult = new ArrayList<Asistent>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); ++i) {
				Angajat a = result.get(i);
				if (a.getFunctie() == 2) {
					Asistent as = new Asistent(db, a.getID());
					assResult.add(as);
				}
			}
		} else {
			return null;
		}
		
		return assResult;
	}

	/**
	 * @param baza de date unde opereaza
	 * @param lista asistentilor ce trebuie actualizati in baza de date
	 */
	public static void uploadAssistants(Database db, ArrayList<Asistent> list) {	
		for (Asistent a : list) {
			a.updateOnDatabase(db);
		}
	}

}
