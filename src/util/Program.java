/**
 * Clasa Program reprezinta implementarea unui program al saptamanii folosit pentru a identifica 
 * disonibilitatea unui angajat/policlinici in timpul saptamanii curente
 */
package util;

/**
 * @author Vali
 *
 */
public class Program {	
	private OraProgram Luni;
	private OraProgram Marti;
	private OraProgram Miercuri;
	private OraProgram Joi;
	private OraProgram Vineri;
	private OraProgram Sambata;
	private OraProgram Duminica;
	
	public Program(String program) {
		this.extractProgram(program);
	}
	
	public Program() {
		this(new String("L: 0-24; M: 0-24; Mi: 0-24; J: 0-24; V: 0-24; S: 0-24; D: 0-24"));
	}
	
	/**
	 * @return intervalul orar de luni
	 */
	public OraProgram getLuni() {
		return Luni;
	}

	/**
	 * @param intervalul orar de luni de setat
	 */
	public void setLuni(OraProgram luni) {
		Luni = luni;
	}

	/**
	 * @return intervalul orar de marti
	 */
	public OraProgram getMarti() {
		return Marti;
	}

	/**
	 * @param intervalul orar de marti de stat
	 */
	public void setMarti(OraProgram marti) {
		Marti = marti;
	}

	/**
	 * @return intervalul orar de miercuri
	 */
	public OraProgram getMiercuri() {
		return Miercuri;
	}

	/**
	 * @param intervalul orar de miercuri de setat
	 */
	public void setMiercuri(OraProgram miercuri) {
		Miercuri = miercuri;
	}

	/**
	 * @return intervalul orar de joi
	 */
	public OraProgram getJoi() {
		return Joi;
	}

	/**
	 * @param intervalul orar de joi de setat
	 */
	public void setJoi(OraProgram joi) {
		Joi = joi;
	}

	/**
	 * @return intervalul orar de vineri
	 */
	public OraProgram getVineri() {
		return Vineri;
	}

	/**
	 * @param intervalul orar de vineri de setat
	 */
	public void setVineri(OraProgram vineri) {
		Vineri = vineri;
	}

	/**
	 * @return intervalul orar de sambata
	 */
	public OraProgram getSambata() {
		return Sambata;
	}

	/**
	 * @param intervalul orar de sambata de setat
	 */
	public void setSambata(OraProgram sambata) {
		Sambata = sambata;
	}

	/**
	 * @return intervalul orar de duminica
	 */
	public OraProgram getDuminica() {
		return Duminica;
	}

	/**
	 * @param intervalul orar de duminica de setat
	 */
	public void setDuminica(OraProgram duminica) {
		Duminica = duminica;
	}

	/**
	 * @return programul in formatul standard
	 */
	public String getProgram() {
		return this.compressProgram();
	}

	/**
	 * @param programul in formatul standard ce trebuie setat
	 */
	public void setProgram(String program) {
		this.extractProgram(program);
	}

	/**
	 * Metoda ce returneaza intervalul orar intr-o zi dupa index-ul acesteia
	 * @param index-ul zilei
	 * @return intervalul orar din ziua respectiva
	 */
	public OraProgram get(int dayIndex) {
		switch (dayIndex) {
			case 0:
				return getLuni();
			case 1:
				return getMarti();
			case 2:
				return getMiercuri();
			case 3:
				return getJoi();
			case 4:
				return getVineri();
			case 5:
				return getSambata();
			case 6:
				return getDuminica();
			default:
				return null;
		} 
	}	
	
	/**
	 * Metoda ce seteaza intervalul orar intr-o zi dupa index-ul acesteia
	 * @param dayIndex index-ul zilei
	 * @param op intervalul orar
	 */
	public void set(int dayIndex, OraProgram op) {
		switch (dayIndex) {
			case 0:
				setLuni(op);
				break;
			case 1:
				setMarti(op);
				break;
			case 2:
				setMiercuri(op);
				break;
			case 3:
				setJoi(op);
				break;
			case 4:
				setVineri(op);
				break;
			case 5:
				setSambata(op);
				break;
			case 6:
				setDuminica(op);
				break;
			default:
				break;
		} 
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.compressProgram();
	}

	/**
	 * Comprima un program impartit pe zile/ore in formatul de sir de caractere
	 * @return programul
	 */
	public String compressProgram() {
		String program = new String("");
		
		if (Luni.isOpen())
			program += "L: " + Luni.getInceput() + "-" + Luni.getSfarsit() + "; ";
		else
			program += "L: Inchis; ";
		
		if (Marti.isOpen())
			program += "M: " + Marti.getInceput() + "-" + Marti.getSfarsit() + "; ";
		else
			program += "M: Inchis; ";
		
		if (Miercuri.isOpen())
			program += "Mi: " + Miercuri.getInceput() + "-" + Miercuri.getSfarsit() + "; ";
		else
			program += "Mi: Inchis; ";
		
		if (Joi.isOpen())
			program += "J: " + Joi.getInceput() + "-" + Joi.getSfarsit() + "; ";
		else
			program += "J: Inchis; ";
			
		if (Vineri.isOpen())
			program += "V: " + Vineri.getInceput() + "-" + Vineri.getSfarsit() + "; ";
		else
			program += "V: Inchis; ";
		
		if (Sambata.isOpen())
			program += "S: " + Sambata.getInceput() + "-" + Sambata.getSfarsit() + "; ";
		else
			program += "S: Inchis; ";
		
		if (Duminica.isOpen())
			program += "D: " + Duminica.getInceput() + "-" + Duminica.getSfarsit();
		else
			program += "D: Inchis";
		
		return program;
	}
	
	/**
	 * Primeste un program in formatul de sir de caractere si il imparte pe ore
	 * @param programul ce trebuie convertit 
	 */
	public void extractProgram(String program) {
		for (String p : program.split(";")) {
			for (int i = 0; i < p.length(); ++i) {
				if (p.charAt(0) == 'L') {
					// Luni
					String orar = p.substring(3);
					if (orar.equals("Inchis")) {
						this.setLuni(new OraProgram(0, 0, false));
						continue;
					}
					
					int ore[] = new int[2];
					int j = 0;
					for (String ora : orar.split("-")) {
						ore[j++] = Integer.parseInt(ora);
					}
					
					this.setLuni(new OraProgram(ore[0], ore[1]));
				} else if (p.charAt(1) == 'M') {
					if (p.charAt(2) == 'i') {
						// Miercuri
						String orar = p.substring(5);
						if (orar.equals("Inchis")) {
							this.setMiercuri(new OraProgram(0, 0, false));
							continue;
						}
						
						int ore[] = new int[2];
						int j = 0;
						for (String ora : orar.split("-")) {
							ore[j++] = Integer.parseInt(ora);
						}
						
						this.setMiercuri(new OraProgram(ore[0], ore[1]));
					} else {
						// Marti
						String orar = p.substring(4);
						if (orar.equals("Inchis")) {
							this.setMarti(new OraProgram(0, 0, false));
							continue;
						}
						
						int ore[] = new int[2];
						int j = 0;
						for (String ora : orar.split("-")) {
							ore[j++] = Integer.parseInt(ora);
						}
						
						this.setMarti(new OraProgram(ore[0], ore[1]));
					}
				} else if (p.charAt(1) == 'J') {
					// Joi
					String orar = p.substring(4);
					if (orar.equals("Inchis")) {
						this.setJoi(new OraProgram(0, 0, false));
						continue;
					}
					
					int ore[] = new int[2];
					int j = 0;
					for (String ora : orar.split("-")) {
						ore[j++] = Integer.parseInt(ora);
					}
					
					this.setJoi(new OraProgram(ore[0], ore[1]));
				} else if (p.charAt(1) == 'V') {
					// Vineri
					String orar = p.substring(4);
					if (orar.equals("Inchis")) {
						this.setVineri(new OraProgram(0, 0, false));
						continue;
					}
					
					int ore[] = new int[2];
					int j = 0;
					for (String ora : orar.split("-")) {
						ore[j++] = Integer.parseInt(ora);
					}
					
					this.setVineri(new OraProgram(ore[0], ore[1]));
				} else if (p.charAt(1) == 'S') {
					// Sambata
					String orar = p.substring(4);
					if (orar.equals("Inchis")) {
						this.setSambata(new OraProgram(0, 0, false));
						continue;
					}
					
					int ore[] = new int[2];
					int j = 0;
					for (String ora : orar.split("-")) {
						ore[j++] = Integer.parseInt(ora);
					}
					
					this.setSambata(new OraProgram(ore[0], ore[1]));
				} else {
					// Duminica
					String orar = p.substring(4);
					if (orar.equals("Inchis")) {
						this.setDuminica(new OraProgram(0, 0, false));
						continue;
					}
					
					int ore[] = new int[2];
					int j = 0;
					for (String ora : orar.split("-")) {
						ore[j++] = Integer.parseInt(ora);
					}
					
					this.setDuminica(new OraProgram(ore[0], ore[1]));
				}
			}
		}
	}	
}
