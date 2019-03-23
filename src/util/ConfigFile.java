/**
 * Clasa ce acceseaza fisierul de configurari, unde este salvat ID-ul policlinicii din care se opereaza
 */
package util;

/**
 * @author Vali
 *
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tables.Policlinica;

public class ConfigFile {
	private static final Pattern FilePattern = Pattern.compile("\\[policlinicaID\\] = (\\d+)");
	private static final String FilePath = "resources/policlinici.config";
	
	private String data;	
	private boolean available;
	private Database db;
	
	public ConfigFile(Database db) {
		try {
			this.data = new String(Files.readAllBytes(Paths.get(FilePath)));
			this.available = true;
			this.db = db;
		} catch (Exception e) {
			ErrorLog.printError("Nu s-a putut extrage ID-ul policlinicii din motive tehnice: " + e);
			this.available = false;
		}
	}  

	/**
	 * Metoda ce determina ID-ul policlinicii din fisier
	 * Coduri de eroare: {
	 * 	-3 -> Nu s-a putut deschide fisierul (cel mai probabil nu exista)
 	 *	-2 -> Fisierul este invalid (nu respecta pattern-ul)
	 *  -1 -> ID-ul policlinicii este invalid (nu exista nicio policlinica cu acest ID)
	 *  >0 -> ID-ul este valid
	 * @return ID-ul policlinicii din fisierul policlinici.config
	 */
	public int getPoliclinicaID() {
		if (!available) {
			return -3;
		}
		
		Matcher m = FilePattern.matcher(data);
		if (m.matches()) {
			int index = Integer.parseInt(m.group(1));
			
			if (Policlinica.isValid(db, index))
				return index;
			
			return -1;
		}
		
		return -2;
	}

	/**
	 * @param ID-ul policlinicii ce trebuie setat
	 */
	public void setPoliclinicaID(int policlinicaID) {
		if (!Policlinica.isValid(db, policlinicaID)) {
			ErrorLog.printError("Nu s-a putut salva noul ID al policlinicii din care se opereaza, deoarece nu este valid.");
			return;
		}
		
		try {
			this.data = new String("[policlinicaID] = " + policlinicaID);			
		    BufferedWriter writer = new BufferedWriter(new FileWriter(FilePath));
		    writer.write(this.data);
		    writer.close();		    
		} catch (IOException e) {
			ErrorLog.printError("Nu s-a putut salva noul ID al policlinicii din care se opereaza din motive tehnice: " + e);
		}
	}
	

}
