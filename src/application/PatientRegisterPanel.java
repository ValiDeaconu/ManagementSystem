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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import util.ErrorLog;

/**
 * @author Ana-Maria
 *
 */
public class PatientRegisterPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JTextField tfNume;
	private boolean foundNume;
	
	private JTextField tfPrenume;
	private boolean foundPrenume;
	
	private JTextField tfAdresa;
	private boolean foundAdresa;
	
	private JTextField tfEmail;
	private boolean foundEmail;
	
	private JTextField tfTelefon;
	private boolean foundTelefon;
	
	private JTextField tfCNP;
	private boolean foundCNP;
	
	private JLabel lblNote;
	
	public PatientRegisterPanel(Main window, String tabTitle) {
		super();
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
		
		JLabel lblNume = new JLabel("Nume:");
		lblNume.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNume.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblNume.setBounds(230, 193, 247, 25);
		this.add(lblNume);
		
		tfNume = new JTextField();
		lblNume.setLabelFor(tfNume);
		tfNume.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfNume.setBounds(535, 193, 247, 25);
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
				checkValidate();
			}
			
			private void checkValidate() {
				lblNote.setVisible(false);
				foundNume = false;
				tfNume.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.BLACK));
				
				if (tfNume.getText().equals("")) {
					// do nothing
				} else if (!tfNume.getText().matches("[a-zA-Z]+")) {
					setNote(false, "Numele poate conține numai caractere alfabetice.");
				} else {
					foundNume = true;
					tfNume.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.GREEN));
				}
			}
		});
		this.add(tfNume);
		
		JLabel lblPrenume = new JLabel("Prenume:");
		lblPrenume.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPrenume.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblPrenume.setBounds(230, 231, 247, 25);
		this.add(lblPrenume);
		
		tfPrenume = new JTextField();
		lblPrenume.setLabelFor(tfPrenume);
		tfPrenume.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfPrenume.setBounds(535, 231, 247, 25);
		tfPrenume.getDocument().addDocumentListener(new DocumentListener() {
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
				checkValidate();
			}
			
			private void checkValidate() {
				lblNote.setVisible(false);
				foundPrenume = false;
				tfPrenume.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.BLACK));
				
				if (tfPrenume.getText().equals("")) {
					// do nothing
				} else if (!tfPrenume.getText().matches("[a-zA-Z]+")) {
					setNote(false, "Prenumele poate conține numai caractere alfabetice.");
				} else {
					foundPrenume = true;
					tfPrenume.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.GREEN));
				}
			}
		});
		this.add(tfPrenume);
		
		JLabel lblCNP = new JLabel("CNP:");
		lblCNP.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCNP.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblCNP.setBounds(230, 269, 247, 25);
		this.add(lblCNP);
		lblCNP.setLabelFor(tfCNP);
		
		tfCNP = new JTextField();
		tfCNP.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfCNP.setBounds(535, 269, 247, 25);
		tfCNP.getDocument().addDocumentListener(new DocumentListener() {
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
				checkValidate();
			}
			
			private void checkValidate() {
				lblNote.setVisible(false);
				foundCNP = false;
				tfCNP.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.BLACK));
				
				if (tfCNP.getText().equals("")) {
					// do nothing
				} else if (!tfCNP.getText().matches("[0-9]+") || tfCNP.getText().length() != 13) {
					setNote(false, "CNP-ul trebuie să conțină 13 cifre.");
				} else {
					try {
						String query = "SELECT id FROM pacienti WHERE cnp = '" + tfCNP.getText() + "'";
						PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
						ResultSet rst = ps.executeQuery();
						
						if (rst.next()) {
							setNote(false, "CNP-ul introdus a fost deja înregistrat.");
							return;
						}
					} catch (SQLException ex) {
						ErrorLog.printError("PacientRegisterPanel CheckCNP SQLException: " + ex);
					}
					
					tfCNP.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.GREEN));
					foundCNP = true;
				}
			}
		});
		this.add(tfCNP);
		
		JLabel lblAdresa = new JLabel("Adresă:");
		lblAdresa.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAdresa.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblAdresa.setBounds(230, 307, 247, 25);
		this.add(lblAdresa);
		
		tfAdresa = new JTextField();
		lblAdresa.setLabelFor(tfAdresa);
		tfAdresa.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfAdresa.setBounds(535, 307, 247, 25);
		tfAdresa.getDocument().addDocumentListener(new DocumentListener() {
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
				checkValidate();
			}
			
			private void checkValidate() {
				tfAdresa.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.BLACK));
				lblNote.setVisible(false);
				foundAdresa = false;
				
				if (tfAdresa.getText().equals("")) {
					// do nothing
				} else if (!tfAdresa.getText().matches("[a-zA-Z0-9., ]+")) {
					setNote(false, "Adresa poate conține numai caractere alfanumerice, spații, virgule și puncte.");
				} else {
					foundAdresa = true;
					tfAdresa.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.GREEN));
				}
			}
		});
		this.add(tfAdresa);
		
		JLabel lblTelefon = new JLabel("Număr de telefon:");
		lblTelefon.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTelefon.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblTelefon.setBounds(230, 345, 247, 25);
		this.add(lblTelefon);
		lblTelefon.setLabelFor(tfTelefon);
		
		tfTelefon = new JTextField();
		tfTelefon.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfTelefon.setBounds(535, 345, 247, 25);
		tfTelefon.getDocument().addDocumentListener(new DocumentListener() {
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
				checkValidate();
			}
			
			private void checkValidate() {
				tfTelefon.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.BLACK));
				lblNote.setVisible(false);
				foundTelefon = false;
				
				if (tfTelefon.getText().equals("")) {
					// do nothing
				} else if (!tfTelefon.getText().matches("[0-9]+") || tfTelefon.getText().length() < 10) {
					setNote(false, "Numărul de telefon trebuie să conțină minim 10 caractere numerice");
				} else {
					try {
						String query = "SELECT id FROM pacienti WHERE telefon = '" + tfTelefon.getText() + "'";
						PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
						ResultSet rst = ps.executeQuery();
						
						if (rst.next()) {
							setNote(false, "Numărul de telefon introdus a fost deja înregistrat.");
							return;
						}
					} catch (SQLException ex) {
						ErrorLog.printError("PacientRegisterPanel CheckPhoneNumber SQLException: " + ex);
					}
					
					tfTelefon.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.GREEN));
					foundTelefon = true;
				}
			}
		});
		this.add(tfTelefon);
		
		JLabel lblEmail = new JLabel("Adresă de e-mail:");
		lblEmail.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEmail.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblEmail.setBounds(230, 383, 247, 25);
		this.add(lblEmail);
		lblEmail.setLabelFor(tfEmail);
		
		tfEmail = new JTextField();
		tfEmail.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfEmail.setBounds(535, 383, 247, 25);
		tfEmail.getDocument().addDocumentListener(new DocumentListener() {
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
				checkValidate();
			}
			
			private void checkValidate() {
				tfEmail.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.BLACK));
				lblNote.setVisible(false);
				foundEmail = false;
				
				if (tfEmail.getText().equals("")) {
					// do nothing
				} else if (!tfEmail.getText().matches(".*@.*\\..*")) {
					setNote(false, "Adresa de e-mail trebuie să fie de forma 'exemplu@medheal.ro'.");
				} else {
					try {
						String query = "SELECT id FROM pacienti WHERE email = '" + tfEmail.getText() + "'";
						PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
						ResultSet rst = ps.executeQuery();
						
						if (rst.next()) {
							setNote(false, "Adresa de e-mail introdusă a fost deja înregistrată.");
							return;
						}
					} catch (SQLException ex) {
						ErrorLog.printError("PacientRegisterPanel CheckEmail SQLException: " + ex);
					}
					
					tfEmail.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.GREEN));
					foundEmail = true;
				}
			}
		});
		this.add(tfEmail);
		
		lblNote = new JLabel("Introduceți datele pacientului.");
		lblNote.setHorizontalAlignment(SwingConstants.CENTER);
		lblNote.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		lblNote.setBounds(12, 600, 530, 25);
		lblNote.setVisible(true);
		this.add(lblNote);
		
		JButton btnSend = new JButton("Submit");
		btnSend.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		btnSend.setBounds(850, 600, 120, 25);
		btnSend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Se verifica daca toate verificarile au fost trecute
				if (foundNume && foundPrenume && foundCNP && foundAdresa && foundTelefon && foundEmail) {
					String query = "CALL ADD_PATIENT('" + 
							tfNume.getText() + "', '" + 
							tfPrenume.getText() + "', '" +
							tfCNP.getText() + "', '" + 
							tfAdresa.getText() + "', '" + 
							tfTelefon.getText() + "', '" + 
							tfEmail.getText() + "');";
					
					if (window.dbHandle.doUpdate(query) == 1) {
						setNote(true, "Pacientul a fost adăugat!");
					} else {
						setNote(false, "A apărut o eroare, vă rugăm reîncercați.");
					}
			
				} else {
					if (!foundNume)
						tfNume.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.RED));
					if (!foundPrenume)
						tfPrenume.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.RED));
					if (!foundCNP)
						tfCNP.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.RED));
					if (!foundAdresa)
						tfAdresa.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.RED));
					if (!foundTelefon)
						tfTelefon.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.RED));
					if (!foundEmail)
						tfEmail.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.RED));
				
					setNote(false, "Câmpurile nu sunt valide!");
				}
			}
		});
		this.add(btnSend);
		
		JButton btnReset = new JButton("Reset");
		btnReset.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		btnReset.setBounds(718, 600, 120, 25);
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tfNume.setText("");
				tfPrenume.setText("");
				tfCNP.setText("");
				tfAdresa.setText("");
				tfTelefon.setText("");
				tfEmail.setText("");				
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

}
