/**
 * Clasa ce reprezinta implementarea unei structuri ce stocheaza o data
 */
package util;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Vali
 *
 */
public class DateTime {
	// YYYY-MM-DD H:m:s.ms
	private static final Pattern TimeStampPattern = Pattern.compile("(\\d+)-(\\d+)-(\\d+) (\\d+):(\\d+):(\\d+)\\.(\\d+)");
	// YYYY-MM-DD H:m:s
	private static final Pattern DateTimePattern = Pattern.compile("(\\d+)-(\\d+)-(\\d+) (\\d+):(\\d+):(\\d+)");
	
	// YYYY-MM-DD
	private static final Pattern DatePattern = Pattern.compile("(\\d+)-(\\d+)-(\\d+)");
	
	private int an;
	private int luna;
	private int zi;
	
	private int ora;
	private int minute;
	private int secunde;
	
	public DateTime(int an, int luna, int zi, int ora, int minute, int secunde) {
		this.setAn(an);
		this.setLuna(luna);
		this.setZi(zi);
		this.setOra(ora);
		this.setMinute(minute);
		this.setSecunde(secunde);
	}
	
	@SuppressWarnings("deprecation")
	public DateTime(Date date) {
		this.setAn(date.getYear());
		this.setLuna(date.getMonth());
		this.setZi(date.getDay());
		this.setOra(date.getHours());
		this.setMinute(date.getMinutes());
		this.setSecunde(date.getSeconds());
	}
	
	public DateTime(Calendar calendar) {	
		this.setAn(calendar.get(Calendar.YEAR));
		this.setLuna(calendar.get(Calendar.MONTH) + 1);
		this.setZi(calendar.get(Calendar.DAY_OF_MONTH));
		this.setOra(calendar.get(Calendar.HOUR_OF_DAY));
		this.setMinute(calendar.get(Calendar.MINUTE));
		this.setSecunde(calendar.get(Calendar.SECOND));
	}
	
	public DateTime() {
		this.setAn(1001);
		this.setLuna(1);
		this.setZi(1);
		this.setOra(0);
		this.setMinute(0);
		this.setSecunde(0);
	}
	
	public DateTime(String date) {
		if (!this.convertFromDate(date)) {
			if (!this.convertFromDateTime(date)) {
				ErrorLog.printError("Data introdusa nu este compatibila.");
				this.setAn(1001);
				this.setLuna(1);
				this.setZi(1);
				this.setOra(0);
				this.setMinute(0);
				this.setSecunde(0);
			}
		}
	}
	
	/**
	 * @return anul
	 */
	public String getAn() {
		return new String("" + an + "");
	}

	/**
	 * @param anul ce trebuie setat
	 * @return daca anul a fost setat
	 */
	public boolean setAn(int an) {
		if (an < 1000 || an > 9999)
			return false;
		
		this.an = an;
		return true;
	}

	/**
	 * @return ziua
	 */
	public String getZi() {
		String str = new String("");
		
		if (zi > 0 && zi < 10)
			str += "0" + zi;
		else
			str += zi;
		
		return str;
	}

	/**
	 * @param ziua ce trebuie setata
	 * @return daca ziua a fost setata
	 */
	public boolean setZi(int zi) {
		int[] maxDays1 = new int[] {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		int[] maxDays2 = new int[] {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		int[] maxDays;
		
		if (an % 4 == 0) {
			maxDays = maxDays2;
		} else {
			maxDays = maxDays1;
		}
		
		if (zi < 1 || zi > maxDays[luna])
			return false;
		
		this.zi = zi;
		return true;
	}

	/**
	 * @return luna
	 */
	public String getLuna() {
		String str = new String("");
		
		if (luna > 0 && luna < 10)
			str += "0" + luna;
		else
			str += luna;
		
		return str;
	}

	/**
	 * @param luna ce trebuie setata
	 * @return daca luna a fost setata
	 */
	public boolean setLuna(int luna) {
		if (luna < 1 || luna > 12)
			return false;
		
		this.luna = luna;
		return true;
	}

	/**
	 * @return ora
	 */
	public String getOra() {
		String str = new String("");
		
		if (ora >= 0 && ora < 10)
			str += "0" + ora;
		else
			str += ora;
		
		return str;
	}

	/**
	 * @param ora ce trebuie setata
	 * @return daca ora a fost setata
	 */
	public boolean setOra(int ora) {
		if (ora < 0 || ora > 23)
			return false;
		
		this.ora = ora;
		return true;
	}

	/**
	 * @return minutele
	 */
	public String getMinute() {
		String str = new String("");
		
		if (minute >= 0 && minute < 10)
			str += "0" + minute;
		else
			str += minute;
		
		return str;
	}

	/**
	 * @param minutele ce trebuiesc setate
	 * @return daca minutele au fost setate
	 */
	public boolean setMinute(int minute) {
		if (ora < 0 || ora > 59)
			return false;
		
		this.minute = minute;
		return true;
	}

	/**
	 * @return secundele
	 */
	public String getSecunde() {
		String str = new String("");
		
		if (secunde >= 0 && secunde < 10)
			str += "0" + secunde;
		else
			str += secunde;
		
		return str;
	}

	/**
	 * @param secundele ce trebuiesc setate
	 * @return daca secundele au fost setate
	 */
	public boolean setSecunde(int secunde) {
		if (secunde < 0 || secunde > 59)
			return false;
		
		this.secunde = secunde;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.convertToDateTime();
	}
	
	public DateTime copy() {
		DateTime newDate = new DateTime();
		
		newDate.setAn(this.an);
		newDate.setLuna(this.luna);
		newDate.setZi(this.zi);
		newDate.setOra(this.ora);
		newDate.setMinute(this.minute);
		newDate.setSecunde(this.secunde);
		
		return newDate;
	}

	/**
	 * Metoda ce compara data cu o data primita ca parametru
	 * @param data
	 * @return -1, 0 sau 1 in functie de ordinea celor doua
	 */
	public int compareTo(DateTime date) {
		if (Integer.parseInt(this.getAn()) < Integer.parseInt(date.getAn()))
			return -1;
		else if (Integer.parseInt(this.getAn()) > Integer.parseInt(date.getAn()))
			return 1;
		
		if (Integer.parseInt(this.getLuna()) < Integer.parseInt(date.getLuna()))
			return -1;
		else if (Integer.parseInt(this.getLuna()) > Integer.parseInt(date.getLuna()))
			return 1;
		
		if (Integer.parseInt(this.getZi()) < Integer.parseInt(date.getZi()))
			return -1;
		else if (Integer.parseInt(this.getZi()) > Integer.parseInt(date.getZi()))
			return 1;
		
		if (Integer.parseInt(this.getOra()) < Integer.parseInt(date.getOra()))
			return -1;
		else if (Integer.parseInt(this.getOra()) > Integer.parseInt(date.getOra()))
			return 1;
				
		if (Integer.parseInt(this.getMinute()) < Integer.parseInt(date.getMinute()))
			return -1;
		else if (Integer.parseInt(this.getMinute()) > Integer.parseInt(date.getMinute()))
			return 1;
		
		if (Integer.parseInt(this.getSecunde()) < Integer.parseInt(date.getSecunde()))
			return -1;
		else if (Integer.parseInt(this.getSecunde()) > Integer.parseInt(date.getSecunde()))
			return 1;
		
		return 0;
	}
	
	/**
	 * Metoda ce verifica daca data este intre doua dati trimise ca parametru
	 * @param data1
	 * @param data2
	 * @return daca data este intre cele doua
	 */
	public boolean isBetween(DateTime d1, DateTime d2) {
		if (this.compareTo(d1) == 1 && this.compareTo(d2) == -1)
			return true;
		return false;
	}
	
	/**
	 * Metoda ce returneaza data primei zile din saptamana din care face parte data curenta
	 * @param data
	 * @return data primei zile din saptamana
	 */
	public DateTime getWeekFirstDay() {
		int dayIndex = this.getDayIndex();
		if (dayIndex == 0)
			return this;
		
		int[] monthDays = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		
		DateTime firstDay = this.copy();
		
		firstDay.ora = 0;
		firstDay.minute = 0;
		firstDay.secunde = 0;
		
		if (firstDay.zi - dayIndex >= 1) {
			firstDay.zi -= dayIndex;
			return firstDay;
		}
		
		if (firstDay.luna - 1 >= 1) {
			firstDay.luna -= 1;
			dayIndex -= firstDay.zi;
			
			if ((firstDay.an % 4 == 0) && (firstDay.luna == 2))
				firstDay.zi = 29 - dayIndex;
			else
				firstDay.zi = monthDays[ firstDay.luna ] - dayIndex;
			
			return firstDay;
		} 
		
		firstDay.an -= 1;
		firstDay.luna = 12;
		firstDay.zi = 31 - Math.abs(firstDay.zi - dayIndex);
		
		return firstDay;
		
	}
	

	/**
	 * Metoda ce returneaza data ultimei zile din saptamana din care face parte data curenta
	 * @param data
	 * @return data ultimei zile din saptamana
	 */
	public DateTime getWeekLastDay() {
		int dayIndex = this.getDayIndex();
		if (dayIndex == 6)
			return this;
		
		int rest = 6 - dayIndex;
		
		int[] monthDays = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		
		DateTime lastDay = this.copy();
		
		lastDay.ora = 23;
		lastDay.minute = 59;
		lastDay.secunde = 59;
		
		if (lastDay.an % 4 == 0)
			monthDays[2] = 29;
		
		if (lastDay.zi + rest <= monthDays[ lastDay.luna ]) {
			lastDay.zi += rest;
			return lastDay;
		}
		
		if (lastDay.luna + 1 <= 12) {
			rest = rest - (monthDays[ lastDay.luna ] - lastDay.zi);
			
			lastDay.luna += 1;
			lastDay.zi = rest;
			return lastDay;
		}

		lastDay.an += 1;
		lastDay.luna = 1;
		lastDay.zi = rest - (31 - lastDay.zi);
		
		return lastDay;
	}
	
	/**
	 * Metoda ce converteste in format DATE (MySQL)
	 * @return data in format DATE
	 */
	public String convertToDate() {
		return new String(getAn() + "-" + getLuna() + "-" + getZi());
	}

	/**
	 * Metoda ce converteste in format DATETIME/TIMESTAMP (MySQL)
	 * @return data in format DATETIME/TIMESTAMP
	 */
	public String convertToDateTime() {
		return new String(getAn() + "-" + getLuna() + "-" + getZi() + " " + getOra() + ":" + getMinute() + ":" + getSecunde());
	}
	
	/**
	 * @return data in format "Luni, 1 Ianuarie 2000, 10:00"
	 */
	public String convertToHumanDate() {
		String[] weekDays = { "Luni", "Marți", "Miercuri", "Joi", "Vineri", "Sâmbătă", "Duminică" };
		String[] months = { "Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie", "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie", "Decembrie" };
		
		String result = new String("");
		
		result += weekDays[ this.getDayIndex() ] + ", ";
		result += this.zi + " ";
		result += months[ this.luna - 1 ] + " ";
		result += this.an + ", ";
		result += this.getOra() + ":";
		result += this.getMinute();
		
		return result;
	}
	

	/**
	 * @return data in format "1 Ianuarie 2000, 10:00"
	 */
	public String convertToHumanDateWithoutDay() {
		String[] months = { "Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie", "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie", "Decembrie" };
		
		String result = new String("");
		
		result += this.zi + " ";
		result += months[ this.luna - 1 ] + " ";
		result += this.an + ", ";
		result += this.getOra() + ":";
		result += this.getMinute();
		
		return result;
	}
	
	/**
	 * Metoda ce salveaza dintr-o data in format DATE (MySQL)
	 * @param data in format DATE
	 * @return daca data primita a fost compatibila
	 */
	public boolean convertFromDate(String date) {
		Matcher m = DatePattern.matcher(date);
		
		if (m.matches()) {
			this.setAn(Integer.parseInt(m.group(1)));
			this.setLuna(Integer.parseInt(m.group(2)));
			this.setZi(Integer.parseInt(m.group(3)));
			return true;
		}
		
		return false;
	}

	/**
	 * Metoda ce salveaza dintr-o data in format DATETIME/TIMESTAMP (MySQL)
	 * @param data in format DATETIME/TIMESTAMP
	 * @return daca data primita a fost compatibila
	 */
	public boolean convertFromDateTime(String date) {
		Matcher m = DateTimePattern.matcher(date);
		if (m.matches()) {
			this.setAn(Integer.parseInt(m.group(1)));
			this.setLuna(Integer.parseInt(m.group(2)));
			this.setZi(Integer.parseInt(m.group(3)));
			
			this.setOra(Integer.parseInt(m.group(4)));
			this.setMinute(Integer.parseInt(m.group(5)));
			this.setSecunde(Integer.parseInt(m.group(6)));
			
			return true;
		}
		
		m = TimeStampPattern.matcher(date);
		if (m.matches()) {
			this.setAn(Integer.parseInt(m.group(1)));
			this.setLuna(Integer.parseInt(m.group(2)));
			this.setZi(Integer.parseInt(m.group(3)));
			
			this.setOra(Integer.parseInt(m.group(4)));
			this.setMinute(Integer.parseInt(m.group(5)));
			this.setSecunde(Integer.parseInt(m.group(6)));
			
			return true;
		}
		
		return false;
	}

	/**
	 * Metoda ce obtine aceasi data, o luna mai devreme
	 * @return data
	 */
	public DateTime prevMonth() {
		DateTime d = this.copy();
		
		if (d.luna == 1) {
			d.an = d.an - 1;
			d.luna = 12;
		} else
			d.luna = d.luna - 1;
		
		return d;
	}
	
	/**
	 * Metoda ce obtine aceasi data, o luna mai tarziu
	 * @return data
	 */
	public DateTime nextMonth() {
		DateTime d = this.copy();
		
		if (d.luna == 12) {
			d.an = d.an + 1;
			d.luna = 1;
		} else
			d.luna = d.luna + 1;
		
		return d;
	}
	
	/**
	 * Metoda ce obtine index-ul zilei saptamanii
	 * @return index-ul
	 */
	public int getDayIndex() {		
		int y = an;
		int m = luna;
		int d = zi;
		
		int t[] = {0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4};
	   	if (m < 3)
	   		--y;
	   	
	   	int index = (y + y/4 - y/100 + y/400 + t[m - 1] + d) % 7;
	   	int result[] = {6, 0, 1, 2, 3, 4, 5};
	    return result[index];
	}

	/**
	 * Metoda ce obtine index-ul zilei saptamanii datei curente
	 * @return index-ul
	 */
	public static int getCurrentDayIndex() {
		DateTime currentDate = new DateTime(Calendar.getInstance());
		return currentDate.getDayIndex();
	}
	
	public static String getDayByIndex(int index) {
		String[] weekDays = { "Luni", "Marți", "Miercuri", "Joi", "Vineri", "Sâmbătă", "Duminică" };
		return weekDays[index];
	}
}
