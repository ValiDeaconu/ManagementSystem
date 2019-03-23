/**
 * Clasa ce implementeaza fisierul error.log pentru o depanare simplificata
 */
package util;

/**
 * @author Vali
 *
 */

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class ErrorLog {
    private static FileOutputStream out = null;
	
	public static void printError(String str) {		
		System.out.println(str);
		try (PrintWriter p = new PrintWriter(out = new FileOutputStream("error.log", true))) {
			Date date = new Date();
		    p.println("[" + date.toString() + "] " + str);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
