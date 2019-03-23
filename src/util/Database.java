/**
 * Clasa ce implementeaza driverul JDBC pentru a conecta aplicatia la o baza de date MySQL
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author Vali
 *
 */
public class Database {
	private Connection dbHandle;
	
	private String dbhost;
	private String dbport;
	private String dbname;
	private String dbuser;
	private String dbpass;
	
	public Database(String dbhost, String dbport, String dbname,  String dbuser, String dbpass) {
		this.dbhost = dbhost;
		this.dbport = dbport;
		this.dbname = dbname;
		this.dbuser = dbuser;
		this.dbpass = dbpass;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			ErrorLog.printError("ClassNotFoundException: " + e);
			System.exit(1);
		}

		dbHandle = null;
		try {
			dbHandle = DriverManager.getConnection(
					"jdbc:mysql://" + this.getHost() + ":" + this.getPort() + "/" + this.getDatabase() + "?useSSL=false", 
					this.getUsername(), 
					this.getPassword()
				);
		}
		catch (SQLException ex) {
			ErrorLog.printError("SQLException: " + ex);
			System.exit(1);
		}
	}
	
	/** 
	 * @return gazda
	 */
	public String getHost() {
		return dbhost;
	}
	
	/**
	 * @return portul
	 */
	public String getPort() {
		return dbport;
	}

	/**
	 * @return numele bazei de date
	 */
	public String getDatabase() {
		return dbname;
	}
	
	/**
	 * @return username-ul
	 */
	public String getUsername() {
		return dbuser;
	}
	
	/**
	 * @return parola
	 */
	public String getPassword() {
		return dbpass;
	}	
	
	/**
	 * @return conexiunea
	 */
	public Connection getConnection() {
		return dbHandle;
	}
	
	/**
	 * Metoda ce executa o comanda de interograre si returneaza o lista cu tablou de string-uri
	 * unde fiecare string din tablou reprezinta valoarea de pe coloana din baza de date
	 * @param query-ul
	 * @return lista de tablouri de string-uri
	 */
	public ArrayList<String[]> doQuery(String queryStr) {
		ArrayList<String[]> result = new ArrayList<String[]>();
		
		int rowCount = 0;
		try {
			PreparedStatement ps = dbHandle.prepareStatement(queryStr);
			ResultSet rst = ps.executeQuery();
			ResultSetMetaData rsmd = rst.getMetaData();

			int colCount = rsmd.getColumnCount();
			
			String[] row = new String[colCount];
			
			while (rst.next()) {
				++rowCount;
				for (int i = 1; i <= colCount; ++i) {	
					Object obj = rst.getObject(i);
					
					if (obj != null)
						row[i - 1] = obj.toString();
					else
						row[i - 1] = null;
				}
				
				result.add(row);
			}

			ps.close();
			rst.close();
		} catch (SQLException ex) {
			ErrorLog.printError("SQLException: " + ex);
		}
		
		if (rowCount == 0)
			return null;
		
		return result;
	}
	
	/**
	 * Metoda ce executa o comanda de actualizare
	 * @param update-ul
	 * @return numarul de lini afecatate
	 */
	public int doUpdate(String updateStr)	{
		int rowCount = 0;
		try {
			Statement stmt = dbHandle.createStatement();
			
			rowCount = stmt.executeUpdate(updateStr);
			
			stmt.close();
		}
		catch (SQLException e) {	
			ErrorLog.printError("Operation failed: " + e);
		}
		
		return rowCount;
	}
}
