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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import util.ErrorLog;
import tables.Competenta;
import tables.Pacient;


/**
 * @author Ana-Maria
 *
 */
public class AddAppointment extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private boolean foundServiciu = false;	
	private boolean foundSpecializare = false;
	private boolean foundPacient = false;
	
	private JComboBox<String> cbSpecializare;
	private JComboBox<String> cbServiciu;
	private JTextField tfNume;
	
	private Competenta competenta;
	private Pacient pacient;
	private String[] denumireServicii = new String[] { "-" };
	private int[] denumireServiciiIndexes;
	private String[] denumireSpecializari;
	private int[] denumireSpecializariIndexes;
	
	private Main window;

	private JLabel lblNote;

	public AddAppointment(Main window, String tabTitle) {
		this.setLayout(null);
		this.window = window;
		
		getSpecializari();
		
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
		
		JLabel lblSpecializare = new JLabel("Specializare");
		lblSpecializare.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSpecializare.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblSpecializare.setBounds(227, 206, 247, 25);
		this.add(lblSpecializare);
		
		cbSpecializare = new JComboBox<String>(denumireSpecializari);
		cbSpecializare.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		cbSpecializare.setBounds(532, 206, 247, 25);
		cbSpecializare.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				String value = (String) cbSpecializare.getSelectedItem();
				if (value.equals("-")) {
					lblNote.setVisible(false);
					foundSpecializare = false;
					competenta = null;
				} else {
					foundSpecializare = true;
					competenta = new Competenta(window.dbHandle, denumireSpecializariIndexes[ cbSpecializare.getSelectedIndex() ]);
					getServicii(competenta);
					DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
					for (String item : denumireServicii) {
						model.addElement(item);
					}
					cbServiciu.setModel(model);
					cbServiciu.setEnabled(true);
				}
				
			}
		});
		this.add(cbSpecializare);

		JLabel lblServiciuMedical = new JLabel("Serviciu Medical");
		lblServiciuMedical.setHorizontalAlignment(SwingConstants.RIGHT);
		lblServiciuMedical.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblServiciuMedical.setBounds(227, 244, 247, 25);
		this.add(lblServiciuMedical);
		
		cbServiciu = new JComboBox<String>(denumireServicii);
		cbServiciu.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		cbServiciu.setBounds(532, 244, 247, 25);
		cbServiciu.setEnabled(false);
		cbServiciu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String value = (String) cbServiciu.getSelectedItem();
				if (value.equals("-")) {
					lblNote.setVisible(false);
					foundServiciu = false;
				} else {
					foundServiciu = true;
				}
			}
		});
		this.add(cbServiciu);

		JLabel lblNume = new JLabel("Nume pacient:");
		lblNume.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNume.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblNume.setBounds(227, 282, 247, 25);
		this.add(lblNume);
		
		tfNume = new JTextField();
		lblNume.setLabelFor(tfNume);
		tfNume.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfNume.setBounds(532, 282, 247, 25);
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
					foundPacient = false;
					pacient = null;
				} else
					checkValidate();
			}
			
			private void checkValidate() {
				lblNote.setVisible(false);
				foundPacient = false;
				pacient = null;
				
				String query = "SELECT id FROM `pacienti` " + 
						"WHERE (nume LIKE '%" + tfNume.getText() + "%' " + 
						"OR prenume LIKE '%" + tfNume.getText() + "%') LIMIT 1";

				try {
					PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
					ResultSet rst = ps.executeQuery();
					
					if (rst.next()) {
						pacient = new Pacient(window.dbHandle, Integer.parseInt(rst.getString("id")));	
						foundPacient = true;
						setNote(true, "Pacient identificat: " + pacient.getNume() + " " + pacient.getPrenume() + ".");
					} else {
						setNote(false, "Nu am găsit niciun pacient cu acest nume.");
					}
				} catch (SQLException ex) {
					ErrorLog.printError("AddAppointment SQLException: " + ex);
				}
				
			}
		});
		this.add(tfNume);
		
		lblNote = new JLabel("");
		lblNote.setHorizontalAlignment(SwingConstants.CENTER);
		lblNote.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		lblNote.setBounds(12, 600, 590, 25);
		lblNote.setVisible(false);
		this.add(lblNote);
		
		JButton btnSend = new JButton("Submit");
		btnSend.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		btnSend.setBounds(850, 600, 120, 25);
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lblNote.setVisible(false);
				
				if (!foundSpecializare) {
					setNote(false, "Trebuie introdusă o specializare validă.");
					return;
				}
				
				if (!foundServiciu) {
					setNote(false, "Trebuie introdus un serviciu valid.");	
					return;					
				}
				
				if (!foundPacient) {
					setNote(false, "Trebuie introdus un pacient.");	
					return;							
				}
				
				try {
					String queryInsert = "INSERT INTO programari (pacient_id, competenta_id, policlinica_id, serviciu_id, data_programare, status) " +
							" VALUES (" + 
							"'" + pacient.getID() + "', " + 
							"'" + competenta.getID() + "'," + 
							"'" + window.centerUsed.getID() + "', " + 
							"'" + denumireServiciiIndexes[ cbServiciu.getSelectedIndex() ] + "', " + 
							"NOW(), " +
							"'0')";
						
					
					PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(queryInsert);
					int rowCount = ps.executeUpdate();
					
					if (rowCount != 1) {
						ErrorLog.printError("[AddAppointment] Au fost afectate " + rowCount + " linii la inserare.");	
					} else {
						setNote(true, "Programarea a fost înregistrată!");
					}
				} catch (SQLException ex) {
					ErrorLog.printError("[AddAppointment] SQLException: " + ex);
				}
			}			
		});
		this.add(btnSend);
		
		JButton btnReset = new JButton("Reset");
		btnReset.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		btnReset.setBounds(718, 600, 120, 25);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cbServiciu.setSelectedIndex(0);
				cbSpecializare.setSelectedIndex(0);
				tfNume.setText("");
			}
		});
		this.add(btnReset);
	}
	


	private void getSpecializari() {		
		denumireSpecializari = new String[] { "Nu exista specializari" };
		
		String query = "SELECT DISTINCT competente.denumire, competente.id " + 
				"FROM competente " + 
				"INNER JOIN servicii ON competente.id = servicii.competenta_id " + 
				"WHERE servicii.medic_id IN (SELECT angajat_id FROM program_angajati WHERE policlinica_id = '" + window.centerUsed.getID() + "') ";
		

		try {
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();

			int count = 0;
			while (rst.next())
				++count;

			rst.beforeFirst();
			denumireSpecializari = new String[count + 1];
			denumireSpecializariIndexes = new int[count + 1];
			int index = 0;
			denumireSpecializari[index] = "-";
			denumireSpecializariIndexes[index] = -1;
			++index;
			while (rst.next()) {
				denumireSpecializariIndexes[index] = Integer.parseInt(rst.getString("id"));
				denumireSpecializari[index] = rst.getString("denumire");
				++index;
			}
		} catch (SQLException e) {
			ErrorLog.printError("[AddAppointment] SQLException: " + e);			
		}
	}
	
	private void getServicii(Competenta c) {
		denumireServicii = new String[] { "Nu exista servicii" };
		
		String query = "SELECT denumire_servicii.id AS 'id', denumire_servicii.denumire AS 'denumire' FROM denumire_servicii"+
				" INNER JOIN servicii ON denumire_servicii.id = servicii.denumire_id" + 
				" WHERE servicii.competenta_id =" + c.getID();
		
		try {
			
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();
			
			int count = 0;
			while (rst.next())
				++count;

			rst.beforeFirst();
			denumireServicii = new String[count + 1];
			denumireServiciiIndexes = new int[count + 1];
			int index = 0;
			denumireServicii[index] = "-";
			denumireServiciiIndexes[index] = -1;
			++index;
			while (rst.next()) {
				denumireServiciiIndexes[index] = Integer.parseInt(rst.getString("id"));
				denumireServicii[index] = rst.getString("denumire");
				++index;
			}
		} catch (SQLException e) {
			ErrorLog.printError("[AddAppointment] SQLException: " + e);
		}
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
	

}
