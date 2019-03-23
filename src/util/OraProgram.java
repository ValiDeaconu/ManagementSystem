/**
 * Clasa ce implementeaza un interval orar, unde ora de inceput este extremitatea stanga, iar ora de 
 * sfarsit este extremitatea dreapta
 */
package util;

/**
 * @author Vali
 *
 */

public class OraProgram {
	private int inceput;
	private int sfarsit;
	private boolean open;
	
	public OraProgram(int inceput, int sfarsit) {
		this.setInceput(inceput);
		this.setSfarsit(sfarsit);
		this.setOpen(true);
	}
	
	public OraProgram() {
		this(0, 24);
	}
	
	public OraProgram(int inceput, int sfarsit, boolean open) {
		if (open == true) {
			this.setInceput(inceput);
			this.setSfarsit(sfarsit);
			this.setOpen(true);
		} else {
			this.setInceput(0);
			this.setSfarsit(0);
			this.setOpen(false);
		}
	}
	
	/**
	 * @return ora de inceput
	 */
	public int getInceput() {
		return inceput;
	}

	/**
	 * @param ora de inceput de setat
	 */
	public void setInceput(int inceput) {
		this.inceput = inceput;
	}

	/**
	 * @return ora de sfarsit
	 */
	public int getSfarsit() {
		return sfarsit;
	}

	/**
	 * @param ora de sfarsit de setat
	 */
	public void setSfarsit(int sfarsit) {
		this.sfarsit = sfarsit;
	}

	/**
	 * @return daca este liber
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * @param validarea de setat
	 */
	public void setOpen(boolean open) {
		this.open = open;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override	
	public String toString() {
		if (isOpen())
			return new String(inceput + " - " + sfarsit);
		return new String("Liber");
	}
	
}