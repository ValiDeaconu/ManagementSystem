/**
 * Modul pentru adaugare raport medical
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
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import tables.Angajat;
import tables.Pacient;
import util.ErrorLog;

/**
 * @author Vali
 *
 */
public class AddMedicalReportPanel extends JPanel {
	private static final long serialVersionUID = 1144488094981366054L;
	
	
	private boolean foundMedic = false;
	private Angajat medic = null;
	
	boolean foundPacient = false;
	private Pacient pacient = null;
	
	public AddMedicalReportPanel(Main window, String tabTitle) {
		this.setLayout(null);

		JLabel lblTitle = new JLabel(tabTitle);
		lblTitle.setFont(new Font("Ubuntu", Font.PLAIN, 42));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(12, 5, 989, 49);
		this.add(lblTitle);
		
		JSeparator headSeparator = new JSeparator();
		headSeparator.setForeground(new Color(70, 130, 180));
		headSeparator.setBounds(62, 67, 889, 2);
		this.add(headSeparator);

		JLabel lblMesajErr = new JLabel("Mesaj err");
		lblMesajErr.setHorizontalAlignment(SwingConstants.CENTER);
		lblMesajErr.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblMesajErr.setForeground(new Color(255, 0, 0));
		lblMesajErr.setBounds(183, 75, 616, 28);
		lblMesajErr.setVisible(false);
		this.add(lblMesajErr);
		
		JLabel lblMesajOk = new JLabel("Mesaj succ");
		lblMesajOk.setHorizontalAlignment(SwingConstants.CENTER);
		lblMesajOk.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblMesajOk.setForeground(new Color(50, 205, 50));
		lblMesajOk.setBounds(183, 75, 616, 28);
		lblMesajOk.setVisible(false);
		this.add(lblMesajOk);
		
		JLabel lblMedic = new JLabel("Medic responsabil:");
		lblMedic.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblMedic.setBounds(112, 139, 140, 28);
		this.add(lblMedic);
		
		JTextField tfMedic = new JTextField();
		lblMedic.setLabelFor(tfMedic);
		tfMedic.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfMedic.setBounds(112, 170, 280, 28);
		tfMedic.getDocument().addDocumentListener(new DocumentListener() {
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
				if (tfMedic.getText().equals("")) {
					lblMesajOk.setVisible(false);
					lblMesajErr.setVisible(false);
				} else
					checkValidate();
			}
			
			private void checkValidate() {
				lblMesajOk.setVisible(false);
				lblMesajErr.setVisible(false);
				foundMedic = false;
				medic = null;
				
				String query = "SELECT id FROM `angajati` " + 
						"WHERE (nume LIKE '%" + tfMedic.getText() + "%' " + 
						"OR prenume LIKE '%" + tfMedic.getText() + "%') " +
						"AND functie = '2' LIMIT 1";

				try {
					PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
					ResultSet rst = ps.executeQuery();
					
					if (rst.next()) {
						medic = new Angajat(window.dbHandle, Integer.parseInt(rst.getString("id")));	
						foundMedic = true;
						lblMesajOk.setText("Medic identificat: " + medic.getNume() + " " + medic.getPrenume() + ".");
						lblMesajOk.setVisible(true);
					} else {
						lblMesajErr.setText("Nu am gasit niciun angajat cu aceste detalii.");
						lblMesajErr.setVisible(true);						
					}
				} catch (SQLException ex) {
					ErrorLog.printError("SQLException: " + ex);
				}
				
			}
		});
		this.add(tfMedic);
		tfMedic.setColumns(10);
		
		JLabel lblPacient = new JLabel("Pacient:");
		lblPacient.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblPacient.setBounds(112, 211, 140, 28);
		this.add(lblPacient);
		
		JTextField tfPacient = new JTextField();
		lblPacient.setLabelFor(tfPacient);
		tfPacient.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfPacient.setColumns(10);
		tfPacient.setBounds(112, 252, 280, 28);
		tfPacient.getDocument().addDocumentListener(new DocumentListener() {
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
				if (tfMedic.getText().equals("")) {
					lblMesajOk.setVisible(false);
					lblMesajErr.setVisible(false);
				} else
					checkValidate();
			}
			
			private void checkValidate() {
				lblMesajOk.setVisible(false);
				lblMesajErr.setVisible(false);
				pacient = null;
				foundPacient = false;
				
				String query = "SELECT id FROM `pacienti` " + 
						"WHERE (nume LIKE '%" + tfPacient.getText() + "%' " + 
						"OR prenume LIKE '%" + tfPacient.getText() + "%') " +
						"LIMIT 1";

				try {
					PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
					ResultSet rst = ps.executeQuery();
					
					if (rst.next()) {
						pacient = new Pacient(window.dbHandle, Integer.parseInt(rst.getString("id")));
						foundPacient = true;
						lblMesajOk.setText("Pacient identificat: " + pacient.getNume() + " " + pacient.getPrenume() + ".");
						lblMesajOk.setVisible(true);	
					} else {
						lblMesajErr.setText("Nu am gasit niciun pacient cu aceste detalii.");
						lblMesajErr.setVisible(true);	
					}
				} catch (SQLException ex) {
					ErrorLog.printError("SQLException: " + ex);
				}
				
			}
		});
		this.add(tfPacient);
		
		JLabel lblIstoric = new JLabel("Istoric:");
		lblIstoric.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblIstoric.setBounds(112, 293, 140, 28);
		this.add(lblIstoric);
		
		JTextArea taIstoric = new JTextArea();
		lblIstoric.setLabelFor(taIstoric);
		taIstoric.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		taIstoric.setLineWrap(true);
		taIstoric.setWrapStyleWord(true);
		JScrollPane spIstoric = new JScrollPane(taIstoric);
		spIstoric.setBounds(112, 334, 280, 70);
		this.add(spIstoric);
		
		JLabel lblSimptome = new JLabel("Simptome:");
		lblSimptome.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblSimptome.setBounds(112, 417, 140, 28);
		this.add(lblSimptome);
		
		JTextArea taSimptome = new JTextArea();
		lblSimptome.setLabelFor(taSimptome);
		taSimptome.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		taSimptome.setLineWrap(true);
		taSimptome.setWrapStyleWord(true);
		JScrollPane spSimptome = new JScrollPane(taSimptome);
		spSimptome.setBounds(112, 458, 280, 70);
		this.add(spSimptome);
		
		JLabel lblInvestigatii = new JLabel("Investigatii:");
		lblInvestigatii.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblInvestigatii.setBounds(613, 129, 140, 28);
		this.add(lblInvestigatii);
		
		JTextArea taInvestigatii = new JTextArea();
		lblInvestigatii.setLabelFor(taInvestigatii);
		taInvestigatii.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		taInvestigatii.setLineWrap(true);
		taInvestigatii.setWrapStyleWord(true);
		JScrollPane spInvestigatii = new JScrollPane(taInvestigatii);
		spInvestigatii.setBounds(613, 170, 280, 70);
		this.add(spInvestigatii);
		
		JLabel lblDiagnostic = new JLabel("Diagnostic:");
		lblDiagnostic.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblDiagnostic.setBounds(613, 252, 140, 28);
		this.add(lblDiagnostic);
		
		JTextArea taDiagnostic = new JTextArea();
		lblDiagnostic.setLabelFor(taDiagnostic);
		taDiagnostic.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		taDiagnostic.setLineWrap(true);
		taDiagnostic.setWrapStyleWord(true);
		JScrollPane spDiagnostic = new JScrollPane(taDiagnostic);
		spDiagnostic.setBounds(613, 293, 280, 70);
		this.add(spDiagnostic);
		
		JLabel lblRecomandari = new JLabel("Recomandari:");
		lblRecomandari.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblRecomandari.setBounds(613, 376, 140, 28);
		this.add(lblRecomandari);
		
		JTextArea taRecomandari = new JTextArea();
		lblRecomandari.setLabelFor(taRecomandari);
		taRecomandari.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		taRecomandari.setLineWrap(true);
		taRecomandari.setWrapStyleWord(true);
		JScrollPane spRecomandari = new JScrollPane(taRecomandari);
		spRecomandari.setBounds(613, 417, 280, 70);
		this.add(spRecomandari);
		
		JCheckBox chckbxParafa = new JCheckBox("Parafa");
		chckbxParafa.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		chckbxParafa.setBounds(613, 503, 113, 25);
		this.add(chckbxParafa);
		
		JButton btnReseteaza = new JButton("Reseteaza");
		btnReseteaza.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tfMedic.setText("");
				tfPacient.setText("");
				taIstoric.setText("");
				taSimptome.setText("");
				taInvestigatii.setText("");
				taDiagnostic.setText("");
				taRecomandari.setText("");
				chckbxParafa.setSelected(false);				
			}
		});
		btnReseteaza.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		btnReseteaza.setBounds(256, 558, 174, 49);
		this.add(btnReseteaza);
		
		JButton btnSubmit = new JButton("Trimite");
		btnSubmit.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		btnSubmit.setBounds(568, 558, 174, 49);
		btnSubmit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lblMesajOk.setVisible(false);
				lblMesajErr.setVisible(false);
				
				if (!foundMedic) {
					lblMesajErr.setText("Trebuie introdus un medic.");
					lblMesajErr.setVisible(true);	
					return;
				}
				
				if (!foundPacient) {
					lblMesajErr.setText("Trebuie introdus un pacient.");
					lblMesajErr.setVisible(true);	
					return;					
				}
				
				int parafa = 0;
				if (chckbxParafa.isSelected())
					parafa = 1;
					
				String query = "INSERT INTO `rapoarte` (medic_id, pacient_id, data_raport, istoric, "
							+ "simptome, investigatii, diagnostic, recomandari, parafa) VALUES ("
							+ "" + medic.getID() + ", "
							+ "" + pacient.getID() + ", "
							+ "NOW(), "
							+ "'" + taIstoric.getText() + "', "
							+ "'" + taSimptome.getText() + "', "
							+ "'" + taInvestigatii.getText() + "', "
							+ "'" + taDiagnostic.getText() + "', "
							+ "'" + taRecomandari.getText() + "', "
							+ "'" + parafa + "');";

				int rowCount = window.dbHandle.doUpdate(query);
				if (rowCount != 1) {
					ErrorLog.printError("Nu s-a putut insera in rapoarte, sql: " + query);
					JOptionPane.showMessageDialog(null, "A aparut o eroare, va rugam reincercati", "Eroare", JOptionPane.ERROR_MESSAGE);
				} else {
					lblMesajOk.setText("Raport adaugat cu succes!");
					lblMesajOk.setVisible(true);
				}
			}			
		});
		this.add(btnSubmit);
	}
	
}
