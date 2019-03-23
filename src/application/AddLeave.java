/**
 * 
 */
package application;

import java.awt.Color;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JSpinnerDateEditor;

import tables.Angajat;
import util.DateTime;
import util.ErrorLog;

/**
 * @author Ana-Maria
 *
 */
public class AddLeave extends JPanel{
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 5958365989972883917L;

	private Main window;

	private JLabel lblNote;
	private JTextField tfNume;
	private Angajat angajat;
	private boolean foundAngajat;
	String[] stringModelOra, stringModelMinute, stringModelZi, stringModelLuna, stringModelAn;
	
	public AddLeave(Main window, String tabTitle) {
		this.setLayout(null);
		this.window = window;
		
		JLabel lblTitle = new JLabel(tabTitle);
		lblTitle.setFont(new Font("Ubuntu", Font.PLAIN, 42));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(12, 5, 989, 49);
		this.add(lblTitle);
		
		JSeparator headSeparator = new JSeparator();
		headSeparator.setForeground(new Color(70, 130, 180));
		headSeparator.setBounds(62, 67, 889, 2);
		this.add(headSeparator);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(70, 130, 180));
		separator.setBounds(505, 140, 2, 466);
		this.add(separator);
		
		JLabel lblNume = new JLabel("Nume angajat:");
		lblNume.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNume.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblNume.setBounds(227, 206, 247, 25);
		this.add(lblNume);
		
		tfNume = new JTextField();
		lblNume.setLabelFor(tfNume);
		tfNume.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfNume.setBounds(532, 206, 247, 25);
		tfNume.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				checkValidate();
			}
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				checkValidate();
			}
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if (tfNume.getText().equals("")) {
					lblNote.setVisible(false);
					foundAngajat = false;
					angajat = null;
				} else
					checkValidate();
			}
			
			private void checkValidate() {
				lblNote.setVisible(false);
				foundAngajat = false;
				angajat = null;
				
				String query = "SELECT id FROM `angajati` " + 
						"WHERE (nume LIKE '%" + tfNume.getText() + "%' " + 
						"OR prenume LIKE '%" + tfNume.getText() + "%') LIMIT 1";

				try {
					PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
					ResultSet rst = ps.executeQuery();
					
					if (rst.next()) {
						angajat = new Angajat(window.dbHandle, Integer.parseInt(rst.getString("id")));	
						foundAngajat = true;
						setNote(true, "Angajat identificat: " + angajat.getNume() + " " + angajat.getPrenume() + ".");
					} else {
						setNote(false, "Nu am gãsit niciun angajat cu acest nume.");
					}
				} catch (SQLException ex) {
					ErrorLog.printError("SQLException: " + ex);
				}
				
			}
		});
		this.add(tfNume);
		
		JLabel lblDataInceput = new JLabel("Data început: ");
		lblDataInceput.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDataInceput.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblDataInceput.setBounds(227, 244, 247, 25);
		this.add(lblDataInceput);
		
		JDateChooser dcDataInceput = new JDateChooser(new JSpinnerDateEditor());
		dcDataInceput.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		dcDataInceput.setLocale(Locale.forLanguageTag("ro-RO"));
		dcDataInceput.setDateFormatString("MMMM dd, yyyy");
		dcDataInceput.setCalendar(null);
		dcDataInceput.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		dcDataInceput.setBounds(532, 244, 247, 25);
		this.add(dcDataInceput);
		
		JLabel lblDataSfarsit = new JLabel("Dată sfârșit: ");
		lblDataSfarsit.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDataSfarsit.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblDataSfarsit.setBounds(227, 282, 247, 25);
		this.add(lblDataSfarsit);
		
		JDateChooser dcDataSfarsit = new JDateChooser(new JSpinnerDateEditor());
		dcDataSfarsit.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		dcDataSfarsit.setLocale(Locale.forLanguageTag("ro-RO"));
		dcDataSfarsit.setDateFormatString("MMMM dd, yyyy");
		dcDataSfarsit.setCalendar(null);
		dcDataSfarsit.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		dcDataSfarsit.setBounds(532, 282, 247, 25);
		this.add(dcDataSfarsit);
		
		lblNote = new JLabel("New label");
		lblNote.setHorizontalAlignment(SwingConstants.CENTER);
		lblNote.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		lblNote.setBounds(12, 600, 530, 25);
		lblNote.setVisible(false);
		this.add(lblNote);
		
		JButton btnSend = new JButton("Submit");
		btnSend.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		btnSend.setBounds(850, 600, 120, 25);
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lblNote.setVisible(false);
				
				if (!foundAngajat) {
					setNote(false, "Trebuie introdus un angajat.");	
					return;							
				}
				
				
				
				DateTime dt1 = new DateTime(dcDataInceput.getCalendar());
				dt1.setOra(0);
				dt1.setMinute(0);
				dt1.setSecunde(0);
				DateTime dt2 = new DateTime(dcDataSfarsit.getCalendar());
				dt2.setOra(23);
				dt2.setMinute(59);
				dt2.setSecunde(59);
				
				if (window.userLogged.getFunctie() == 2) {				
					String query = "SELECT id FROM `programari` " + 
							"WHERE (data_programare BETWEEN '" + dt1 + "' " + 
							"AND '" + dt2 + "') AND (medic_id = " + angajat.getID() + ");";
					
					try {
						PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
						ResultSet rst = ps.executeQuery();
						
						if (rst.next()) {
							JOptionPane.showMessageDialog(null, "Aveti programari in aceasta perioada.", "Eroare", JOptionPane.ERROR_MESSAGE);
						} else {
							addLeaveInsert(angajat, dt1, dt2);
						}
					} catch (SQLException ex) {
						ErrorLog.printError("AddAppointment SQLException: " + ex);
					}
				} else {
					addLeaveInsert(angajat, dt1, dt2);
				}

			}			
		});
		this.add(btnSend);
		
		JButton btnReset = new JButton("Reset");
		btnReset.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		btnReset.setBounds(718, 600, 120, 25);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tfNume.setText("");
				dcDataInceput.setCalendar(null);
				dcDataSfarsit.setCalendar(null);
			}
		});
		this.add(btnReset);
	}
	
	private void setNote(boolean isOk, String text) {
		if (isOk) {
			lblNote.setForeground(new Color(50, 205, 50));
		} else {
			lblNote.setForeground(new Color(255, 0, 0));
		}
		
		lblNote.setVisible(true);
		lblNote.setText(text);
	}
	
	public void addLeaveInsert(Angajat angajat, DateTime dt1, DateTime dt2) {
		try {
			String queryInsert = "CALL ADD_LEAVE('" + angajat.getID() + "', '" + dt1 + "', '" + dt2 + "');";

			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(queryInsert);
			int rowCount = ps.executeUpdate();
			
			if (rowCount != 1) {
				ErrorLog.printError("[AddLeave] Au fost afectate " + rowCount + " linii la inserare.");	
			} else {
				setNote(true, "Concediul a fost adaugat cu succes!");				
			}
		} catch (SQLException ex) {
			ErrorLog.printError("[AddLeave] SQLException: " + ex);
		}
	}
	
}
