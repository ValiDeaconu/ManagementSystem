/**
 * Clasa Medic reprezinta stocarea unui record din tabela medici
 */
package tables;

import java.util.ArrayList;

import util.Database;
import util.ErrorLog;

/**
 * @author Ana
 *
 */
public class Medic extends Angajat {
	public static final String table = "medici";
	
	private int codParafa;
	private String titluStiintific;
	private String postDidactic;
	private String procentSalariu;

	public Medic() {
		super();
		this.codParafa = 0;
		this.titluStiintific = new String("");
		this.postDidactic = new String("");
		this.procentSalariu = new String("");
	}

	public Medic(Database db, int id) {
		super(db, id);
		this.extractFromDatabase(db);
		
	}
	
	/**
	 * @return codul de parafa
	 */
	public int getCodParafa() {
		return codParafa;
	}
	
	/**
	 * @param codul de parafa ce trebuie setat
	 */
	public void setCodParafa(int codParafa) {
		this.codParafa = codParafa;
	}
	
	/**
	 * @return titlul stiintific
	 */
	public String getTitluStiintific() {
		return titluStiintific;
	}
	
	/**
	 * @param titlul stiintific ce trebuie setat
	 */
	public void setTitluStiintific(String titluStiintific) {
		this.titluStiintific = titluStiintific;
	}
	
	/**
	 * @return postul didactic
	 */
	public String getPostDidactic() {
		return postDidactic;
	}
	
	/**
	 * @param postul didactic ce trebuie setat
	 */
	public void setPostDidactic(String postDidactic) {
		this.postDidactic = postDidactic;
	}
	
	/**
	 * @return procentul salariului
	 */
	public String getProcentSalariu() {
		return procentSalariu;
	}

	/**
	 * @param procentul salariului ce trebuie setat
	 */
	public void setProcentSalariu(String procentSalariu) {
		this.procentSalariu = procentSalariu;
	}

	/**
	 * @param angajatul ce trebuie modificat in medic
	 * @return angajatul modificat in medic
	 */
	public Medic promoteMedic(Angajat a) {
		Medic m = (Medic) a;
		m.setCodParafa(0);
		m.setPostDidactic(null);
		m.setProcentSalariu(null);
		m.setTitluStiintific(null);
		return m;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Medic [id=" + id + ", codParafa=" + codParafa + ", titluStiintific=" + titluStiintific
				+ ", postDidactic=" + postDidactic + ", procentSalariu=" + procentSalariu + "]";
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void extractFromDatabase(Database db) {
		String query = "SELECT * FROM `" + table + "` WHERE id = '" + this.id + "' LIMIT 1";
		ArrayList<String[]> result = db.doQuery(query);
		if (result != null)
			for (String[] row : result) {
				this.codParafa = Integer.parseInt(row[1]);
				this.titluStiintific = row[2];
				this.postDidactic = row[3];
				this.procentSalariu = row[4];
			}
	}

	/**
	 * @param baza de date unde opereaza
	 */
	public void updateOnDatabase(Database db) {		
		String query = "UPDATE `" + table + "` "
				+ "SET cod_parafa = '" + this.getCodParafa() 
				+ "', titlu_stiintific = '" + this.getTitluStiintific() 
				+ "', post_didactic = '" + this.getPostDidactic()
				+ "', procent_salariu = '" + this.getProcentSalariu()  + "' "
				+ "WHERE id = '" + this.id + "'";
		
		int rowCount = db.doUpdate(query);
		
		if (rowCount == 0) {
			ErrorLog.printError("Nu a fost gasit niciun medic cu id-ul " + id);
		} else if (rowCount > 1) {
			ErrorLog.printError("Au fost gasiti mai multi medici cu id-ul " + id);
		}
	}

	/**
	 * @param baza de date unde opereaza
	 * @return lista medicilor din baza de date
	 */
	public static ArrayList<Medic> downloadMedics(Database db) {
		ArrayList<Angajat> result = Angajat.downloadDatabase(db);
		ArrayList<Medic> medResult = new ArrayList<Medic>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); ++i) {
				Angajat a = result.get(i);
				if (a.getFunctie() == 2) {
					Medic m = new Medic(db, a.getID());
					medResult.add(m);
				}
			}
		} else {
			return null;
		}
		
		return medResult;
	}

	/**
	 * @param baza de date unde opereaza
	 * @param lista medicilor ce trebuie actualizati in baza de date
	 */
	public static void uploadMedics(Database db, ArrayList<Medic> list) {	
		for (Medic m : list) {
			m.updateOnDatabase(db);
		}
	}

}
