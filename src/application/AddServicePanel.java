/**
 * Modul pentru adaugare unui serviciu medical
 */
package application;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import util.ErrorLog;

/**
 * @author Vali
 *
 */
public class AddServicePanel extends JPanel {
	private static final long serialVersionUID = 1144488094981366054L;

	private boolean foundServiciu = false;	
	private boolean foundSpecializare = false;
	
	private JComboBox<String> cbSpecializare;
	private JComboBox<String> cbServiciu;
	
	private String[] denumireServicii;
	private int[] denumireServiciiIndexes;
	private String[] denumireSpecializari;
	private int[] denumireSpecializariIndexes;
	
	private Main window;
	
	private JLabel lblNote;
	
	public AddServicePanel(Main window, String tabTitle) {
		this.setLayout(null);
		this.window = window;
		
		getNames();
		
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
		
		JLabel lblServiciuMedical = new JLabel("Serviciu Medical");
		lblServiciuMedical.setHorizontalAlignment(SwingConstants.RIGHT);
		lblServiciuMedical.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblServiciuMedical.setBounds(227, 206, 247, 25);
		this.add(lblServiciuMedical);
		
		cbServiciu = new JComboBox<String>(denumireServicii);
		cbServiciu.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		cbServiciu.setBounds(532, 206, 247, 25);
		cbServiciu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String value = (String) cbServiciu.getSelectedItem();
				if (value.equals("-")) {
					lblNote.setVisible(false);
					foundServiciu = false;
				} else {
					lblNote.setVisible(false);
					foundServiciu = false;
					
					String query = "SELECT servicii.id FROM servicii " +
							"INNER JOIN competente ON competente.id = servicii.competenta_id " +
							"INNER JOIN denumire_servicii ON denumire_servicii.id = servicii.denumire_id " +
							"WHERE competente.denumire = '" + ((String) cbSpecializare.getSelectedItem()) + "' " +
							"AND denumire_servicii.denumire = '" + value + "' " +
							"AND servicii.medic_id = '" + window.userLogged.getID() + "'";

					try {
						PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
						ResultSet rst = ps.executeQuery();
						
						if (rst.next()) {
							setNote(false, "Există deja un preț la " + value + " pentru specializarea " + ((String) cbSpecializare.getSelectedItem()) + ".");
						} else {
							foundServiciu = true;
						}
					} catch (SQLException ex) {
						ErrorLog.printError("SQLException: " + ex);
					}
				}
			}
		});
		this.add(cbServiciu);
		
		JLabel lblSpecializare = new JLabel("Specializare");
		lblSpecializare.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSpecializare.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblSpecializare.setBounds(227, 244, 247, 25);
		this.add(lblSpecializare);
		
		cbSpecializare = new JComboBox<String>(denumireSpecializari);
		cbSpecializare.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		cbSpecializare.setBounds(532, 244, 247, 25);
		cbSpecializare.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				String value = (String) cbSpecializare.getSelectedItem();
				if (value.equals("-")) {
					lblNote.setVisible(false);
					foundSpecializare = false;
				} else {
					lblNote.setVisible(false);
					foundSpecializare = false;
					
					String query = "SELECT servicii.id FROM servicii " +
							"INNER JOIN competente ON competente.id = servicii.competenta_id " +
							"INNER JOIN denumire_servicii ON denumire_servicii.id = servicii.denumire_id " +
							"WHERE competente.denumire = '" + value + "' " +
							"AND denumire_servicii.denumire = '" + ((String) cbServiciu.getSelectedItem()) + "' " +
							"AND servicii.medic_id = '" + window.userLogged.getID() + "'";
					
					try {
						PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
						ResultSet rst = ps.executeQuery();
						
						if (rst.next()) {
							setNote(false, "Există deja un preț la " + ((String) cbServiciu.getSelectedItem()) + " pentru specializarea " + value + ".");
						} else {
							foundSpecializare = true;
						}
					} catch (SQLException ex) {
						ErrorLog.printError("SQLException: " + ex);
					}
				}
				
			}
		});
		this.add(cbSpecializare);
		
		JLabel lblPret = new JLabel("Preț");
		lblPret.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPret.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblPret.setBounds(227, 282, 247, 25);
		this.add(lblPret);
		
		JSpinner spPret = new JSpinner();
		spPret.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		spPret.setBounds(532, 282, 247, 25);
		spPret.setModel(new SpinnerNumberModel(0, 0, 100000, 1));
		this.add(spPret);
		
		JLabel lblDurata = new JLabel("Durată");
		lblDurata.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDurata.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblDurata.setBounds(227, 320, 247, 25);
		this.add(lblDurata);
		
		JSpinner spDurata = new JSpinner();
		spDurata.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		spDurata.setBounds(532, 320, 247, 25);
		spDurata.setModel(new SpinnerNumberModel(0, 0, 1440, 1));
		this.add(spDurata);
		
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
				
				try {
					String query = "INSERT INTO servicii (denumire_id, pret, durata, competenta_id, medic_id) VALUES (" +
									"'" + denumireServiciiIndexes[ cbServiciu.getSelectedIndex() ] + "', " +
									"'" + spPret.getValue() + "', " +
									"'" + spDurata.getValue() + "', " +
									"'" + denumireSpecializariIndexes[ cbSpecializare.getSelectedIndex() ] + "'," +
									"'" + window.userLogged.getID() + "')";
					
					PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
					int rowCount = ps.executeUpdate();
					
					if (rowCount != 1) {
						ErrorLog.printError("[AddServicePanel] Au fost afectate " + rowCount + " linii la inserare.");	
					} else {
						setNote(true, "Serviciul medical a fost adăugat cu succes!");
					}
				} catch (SQLException ex) {
					ErrorLog.printError("AddServicePanel SQLException: " + ex);
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
				spPret.setValue(0);
				spDurata.setValue(0);				
			}
		});
		this.add(btnReset);
	}

	private void getNames() {
		denumireServicii = new String[] { "Nu exista servicii" };
		try {
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement("SELECT id, denumire FROM denumire_servicii");
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
			ErrorLog.printError("[AddServicePanel] SQLException: " + e);
		}
		
		denumireSpecializari = new String[] { "Nu exista specializari" };
		String query = "SELECT competente.denumire AS 'denumire', competente.id AS 'id' " + 
				"FROM competente " + 
				"INNER JOIN specialitati ON competente.id = specialitati.competenta_id " + 
				"WHERE specialitati.medic_id = " + window.userLogged.getID();
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
			ErrorLog.printError("[AddServicePanel] SQLException: " + e);			
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
