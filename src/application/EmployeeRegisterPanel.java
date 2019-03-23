/**
 * 
 */
package application;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import util.ErrorLog;

/**
 * @author Vali
 *
 */
public class EmployeeRegisterPanel extends JPanel {
	private static final long serialVersionUID = 7289095474484679736L;


	private JTextField tfUsername;
	private boolean foundUsername;
	
	private JPasswordField pfParola;
	private boolean foundParola;
	
	private JTextField tfNume;
	private boolean foundNume;
	
	private JTextField tfPrenume;
	private boolean foundPrenume;
	
	private JTextField tfCNP;
	private boolean foundCNP;
	
	private JTextField tfAdresa;
	private boolean foundAdresa;
	
	private JTextField tfTelefon;
	private boolean foundTelefon;
	
	private JTextField tfEmail;
	private boolean foundEmail;
	
	private JTextField tfIBAN;
	private boolean foundIBAN;
	
	private JTextField tfNrContract;
	private boolean foundNrContract;
	
	private JTextField tfCodParafa;
	private boolean foundCodParafa;

	private JSpinner spSalariu;
	private JSpinner spNrOre;
	
	private JComboBox<String> cbFunctie;
	
	private JComboBox<String> cbTitluStiintific;
	private JComboBox<String> cbPostDidactic;
	private JSpinner spProcentSalariu;
	
	private JComboBox<String> cbTip;
	private JComboBox<String> cbGrad;
	
	private JLabel lblNote;
	
	public EmployeeRegisterPanel(Main window, String tabTitle) {
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
		
		JLabel lblUsername = new JLabel("Nume de utilizator:");
		lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUsername.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblUsername.setBounds(26, 120, 247, 25);
		this.add(lblUsername);
		
		tfUsername = new JTextField();
		tfUsername.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfUsername.setBounds(331, 120, 247, 25);
		tfUsername.getDocument().addDocumentListener(new DocumentListener() {
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
				foundUsername = false;
				tfUsername.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.BLACK));
				
				if (tfUsername.getText().equals("")) {
					// do nothing
				} else if (!tfUsername.getText().matches("^[a-zA-Z0-9._]*$")) {
					setNote(false, "Numele de utilizator poate conține numai caractere alfanumerice, puncte și underscore-uri.");
				} else if (tfUsername.getText().length() < 8) {
					setNote(false, "Numele de utilizator trebuie să aibă minim 8 caractere.");					
				} else {
					try {
						String query = "SELECT id FROM angajati WHERE username = '" + tfUsername.getText() + "'";
						PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
						ResultSet rst = ps.executeQuery();
						
						if (rst.next()) {
							setNote(false, "Numele de utilizator introdus există deja.");
							return;
						}
					} catch (SQLException ex) {
						ErrorLog.printError("WorkerRegisterPanel CheckUsername SQLException: " + ex);
					}
					
					foundUsername = true;
					tfUsername.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.GREEN));
				}
			}
		});
		this.add(tfUsername);
		lblUsername.setLabelFor(tfUsername);
		
		JLabel lblParola = new JLabel("Parolă:");
		lblParola.setHorizontalAlignment(SwingConstants.RIGHT);
		lblParola.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblParola.setBounds(26, 158, 247, 25);
		this.add(lblParola);
		
		pfParola = new JPasswordField();
		pfParola.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		pfParola.setBounds(331, 158, 247, 25);
		pfParola.getDocument().addDocumentListener(new DocumentListener() {
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
				foundParola = false;
				pfParola.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.BLACK));
				
				String value = new String(pfParola.getPassword());
				
				if (value.equals("")) {
					// do nothing
				} else if (value.contains("'") || value.contains("\"")) {
					setNote(false, "Parola conține unele caractere invalide.");
				} else if (value.length() < 6) {
					setNote(false, "Parola trebuie să aibă minim 6 caractere.");					
				} else {
					foundParola = true;
					pfParola.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.GREEN));
				}
			}
		});
		this.add(pfParola);
		lblParola.setLabelFor(pfParola);
		
		JLabel lblNume = new JLabel("Nume:");
		lblNume.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNume.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblNume.setBounds(26, 196, 247, 25);
		this.add(lblNume);
		
		tfNume = new JTextField();
		lblNume.setLabelFor(tfNume);
		tfNume.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfNume.setBounds(331, 196, 247, 25);
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
		lblPrenume.setBounds(26, 234, 247, 25);
		this.add(lblPrenume);
		
		tfPrenume = new JTextField();
		lblPrenume.setLabelFor(tfPrenume);
		tfPrenume.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfPrenume.setBounds(331, 234, 247, 25);
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
		lblCNP.setBounds(26, 272, 247, 25);
		this.add(lblCNP);
		lblCNP.setLabelFor(tfCNP);
		
		tfCNP = new JTextField();
		tfCNP.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfCNP.setBounds(331, 272, 247, 25);
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
						String query = "SELECT id FROM angajati WHERE cnp = '" + tfCNP.getText() + "'";
						PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
						ResultSet rst = ps.executeQuery();
						
						if (rst.next()) {
							setNote(false, "CNP-ul introdus a fost deja înregistrat.");
							return;
						}
					} catch (SQLException ex) {
						ErrorLog.printError("WorkerRegisterPanel CheckCNP SQLException: " + ex);
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
		lblAdresa.setBounds(26, 310, 247, 25);
		this.add(lblAdresa);
		
		tfAdresa = new JTextField();
		lblAdresa.setLabelFor(tfAdresa);
		tfAdresa.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfAdresa.setBounds(331, 310, 247, 25);
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
		
		JLabel lblTelefon = new JLabel("Telefon:");
		lblTelefon.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTelefon.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblTelefon.setBounds(26, 348, 247, 25);
		this.add(lblTelefon);
		lblTelefon.setLabelFor(tfTelefon);
		
		tfTelefon = new JTextField();
		tfTelefon.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfTelefon.setBounds(331, 348, 247, 25);
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
						String query = "SELECT id FROM angajati WHERE telefon = '" + tfTelefon.getText() + "'";
						PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
						ResultSet rst = ps.executeQuery();
						
						if (rst.next()) {
							setNote(false, "Numărul de telefon introdus a fost deja înregistrat.");
							return;
						}
					} catch (SQLException ex) {
						ErrorLog.printError("WorkerRegisterPanel CheckPhoneNumber SQLException: " + ex);
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
		lblEmail.setBounds(26, 386, 247, 25);
		this.add(lblEmail);
		lblEmail.setLabelFor(tfEmail);
		
		tfEmail = new JTextField();
		tfEmail.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfEmail.setBounds(331, 386, 247, 25);
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
						String query = "SELECT id FROM angajati WHERE email = '" + tfEmail.getText() + "'";
						PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
						ResultSet rst = ps.executeQuery();
						
						if (rst.next()) {
							setNote(false, "Adresa de e-mail introdusă a fost deja înregistrată.");
							return;
						}
					} catch (SQLException ex) {
						ErrorLog.printError("WorkerRegisterPanel CheckEmail SQLException: " + ex);
					}
					
					tfEmail.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.GREEN));
					foundEmail = true;
				}
			}
		});
		this.add(tfEmail);
		
		JLabel lblIBAN = new JLabel("Cont IBAN:");
		lblIBAN.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIBAN.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblIBAN.setBounds(26, 424, 247, 25);
		this.add(lblIBAN);
		lblIBAN.setLabelFor(tfIBAN);
		
		tfIBAN = new JTextField();
		tfIBAN.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfIBAN.setBounds(331, 424, 247, 25);
		tfIBAN.getDocument().addDocumentListener(new DocumentListener() {
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
				tfIBAN.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.BLACK));
				lblNote.setVisible(false);
				foundIBAN = false;
				
				if (tfIBAN.getText().equals("")) {
					// do nothing
				} else if (!tfIBAN.getText().matches("[a-zA-Z0-9]+")) {
					setNote(false, "Contul IBAN conține doar caractere alfanumerice.");
				} else if (tfIBAN.getText().length() != 24) {
					setNote(false, "Contul IBAN trebuie să aibă 24 de cifre.");					
				} else {
					try {
						String query = "SELECT id FROM angajati WHERE iban  = '" + tfIBAN.getText() + "'";
						PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
						ResultSet rst = ps.executeQuery();
						
						if (rst.next()) {
							setNote(false, "Contul IBAN introdus a fost deja înregistrat.");
							return;
						}
					} catch (SQLException ex) {
						ErrorLog.printError("WorkerRegisterPanel CheckIBAN SQLException: " + ex);
					}
					
					tfIBAN.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.GREEN));
					foundIBAN = true;
				}
			}
		});
		this.add(tfIBAN);
		
		JLabel lblNrContract = new JLabel("Număr Contract:");
		lblNrContract.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNrContract.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblNrContract.setBounds(26, 462, 247, 25);
		this.add(lblNrContract);
		lblNrContract.setLabelFor(tfNrContract);
		
		tfNrContract = new JTextField();
		tfNrContract.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfNrContract.setBounds(331, 462, 247, 25);
		tfNrContract.getDocument().addDocumentListener(new DocumentListener() {
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
				tfNrContract.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.BLACK));
				lblNote.setVisible(false);
				foundNrContract = false;
				
				if (tfNrContract.getText().equals("")) {
					// do nothing
				} else if (!tfNrContract.getText().matches("[0-9./]+")) {
					setNote(false, "Contul IBAN conține doar caractere numerice, slash-uri și puncte.");
				} else {
					try {
						String query = "SELECT id FROM angajati WHERE nr_contract  = '" + tfNrContract.getText() + "'";
						PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
						ResultSet rst = ps.executeQuery();
						
						if (rst.next()) {
							setNote(false, "Numarul de contract introdus a fost deja înregistrat.");
							return;
						}
					} catch (SQLException ex) {
						ErrorLog.printError("WorkerRegisterPanel CheckContractNo SQLException: " + ex);
					}
					
					tfNrContract.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.GREEN));
					foundNrContract = true;
				}
			}
		});
		this.add(tfNrContract);
		
		JLabel lblSalariu = new JLabel("Salariu:");
		lblSalariu.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSalariu.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblSalariu.setBounds(26, 500, 247, 25);
		this.add(lblSalariu);
		
		spSalariu = new JSpinner();
		lblSalariu.setLabelFor(spSalariu);
		spSalariu.setModel(new SpinnerNumberModel(1450, 1450, 300000, 50));
		spSalariu.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		spSalariu.setBounds(331, 500, 247, 25);
		this.add(spSalariu);
		
		JLabel lblNrOre = new JLabel("Număr de ore minime / săptămână:");
		lblNrOre.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNrOre.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblNrOre.setBounds(26, 538, 247, 25);
		this.add(lblNrOre);
		
		spNrOre = new JSpinner();
		lblNrOre.setLabelFor(spNrOre);
		spNrOre.setModel(new SpinnerNumberModel(20, 20, 168, 1));
		spNrOre.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		spNrOre.setBounds(331, 538, 247, 25);
		this.add(spNrOre);
		
		JLabel lblFunctie = new JLabel("Funcție:");
		lblFunctie.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblFunctie.setBounds(647, 158, 95, 25);
		this.add(lblFunctie);
		
		cbFunctie = new JComboBox<String>();
		lblFunctie.setLabelFor(cbFunctie);
		cbFunctie.setBounds(754, 158, 234, 25);
		cbFunctie.setModel(new DefaultComboBoxModel<String>(new String[] {"Recepționer", "Asistent Medical", "Medic", "Contabil", "Inspector resurse umane"}));
		cbFunctie.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		cbFunctie.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (cbFunctie.getSelectedIndex() == 1) {
					cbTip.setEnabled(true);
					cbTip.setToolTipText(null);
					cbGrad.setEnabled(true);
					cbGrad.setToolTipText(null);
				} else {
					cbTip.setEnabled(false);
					cbTip.setToolTipText("Opțiune disponibilă doar pentru funcția Asistent Medical.");
					cbGrad.setEnabled(false);
					cbGrad.setToolTipText("Opțiune disponibilă doar pentru funcția Asistent Medical.");
				}
				
				if (cbFunctie.getSelectedIndex() == 2) {
					tfCodParafa.setEnabled(true);
					tfCodParafa.setToolTipText(null);
					cbTitluStiintific.setEnabled(true);
					cbTitluStiintific.setToolTipText(null);
					cbPostDidactic.setEnabled(true);
					cbPostDidactic.setToolTipText(null);
					spProcentSalariu.setEnabled(true);
					spProcentSalariu.setToolTipText(null);
				} else {
					tfCodParafa.setEnabled(false);
					tfCodParafa.setToolTipText("Opțiune disponibilă doar pentru funcția Medic.");
					cbTitluStiintific.setEnabled(false);
					cbTitluStiintific.setToolTipText("Opțiune disponibilă doar pentru funcția Medic.");
					cbPostDidactic.setEnabled(false);
					cbPostDidactic.setToolTipText("Opțiune disponibilă doar pentru funcția Medic.");
					spProcentSalariu.setEnabled(false);
					spProcentSalariu.setToolTipText("Opțiune disponibilă doar pentru funcția Medic.");
				}
			}
		});
		this.add(cbFunctie);
				
		JLabel lblCodParafa = new JLabel("Cod parafă:");
		lblCodParafa.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblCodParafa.setBounds(647, 234, 120, 25);
		this.add(lblCodParafa);
		lblCodParafa.setLabelFor(tfCodParafa);
		
		tfCodParafa = new JTextField();
		tfCodParafa.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfCodParafa.setBounds(803, 234, 185, 25);
		tfCodParafa.getDocument().addDocumentListener(new DocumentListener() {
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
				tfCodParafa.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.BLACK));
				lblNote.setVisible(false);
				foundCodParafa = false;
				
				if (tfCodParafa.getText().equals("")) {
					// do nothing
				} else if (!tfCodParafa.getText().matches("[0-9]+")) {
					setNote(false, "Codul de parafă poate conține numai caractere numerice.");
				} else {
					try {
						String query = "SELECT id FROM medici WHERE cod_parafa = '" + tfCodParafa.getText() + "'";
						PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
						ResultSet rst = ps.executeQuery();
						
						if (rst.next()) {
							setNote(false, "Codul de parafă introdus a fost deja înregistrat.");
							return;
						}
					} catch (SQLException ex) {
						ErrorLog.printError("WorkerRegisterPanel CheckMedicCode SQLException: " + ex);
					}
					
					tfCodParafa.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.GREEN));
					foundCodParafa = true;
				}
			}
		});
		tfCodParafa.setEnabled(false);
		tfCodParafa.setToolTipText("Opțiune disponibilă doar pentru funcția Medic.");
		this.add(tfCodParafa);
		
		JLabel lblTitluStiinific = new JLabel("Titlu științific:");
		lblTitluStiinific.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblTitluStiinific.setBounds(647, 272, 120, 25);
		this.add(lblTitluStiinific);
		
		cbTitluStiintific = new JComboBox<String>();
		lblTitluStiinific.setLabelFor(cbTitluStiintific);
		cbTitluStiintific.setModel(new DefaultComboBoxModel<String>(new String[] {"-", "Doctorand", "Doctor in stiinte medicale"}));
		cbTitluStiintific.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		cbTitluStiintific.setBounds(803, 272, 185, 25);
		cbTitluStiintific.setEnabled(false);
		cbTitluStiintific.setToolTipText("Opțiune disponibilă doar pentru funcția Medic.");
		this.add(cbTitluStiintific);
		
		JLabel lblPostDidactic = new JLabel("Post didactic:");
		lblPostDidactic.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblPostDidactic.setBounds(647, 310, 120, 25);
		this.add(lblPostDidactic);
		
		cbPostDidactic = new JComboBox<String>();
		lblPostDidactic.setLabelFor(cbPostDidactic);
		cbPostDidactic.setModel(new DefaultComboBoxModel<String>(new String[] {"- ", "Preparator", "Asistent", "Lector", "Conferentiar", "Profesor"}));
		cbPostDidactic.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		cbPostDidactic.setBounds(803, 310, 185, 25);
		cbPostDidactic.setEnabled(false);
		cbPostDidactic.setToolTipText("Opțiune disponibilă doar pentru funcția Medic.");
		this.add(cbPostDidactic);
		
		JLabel lblProcentSalariu = new JLabel("Procent salariu:");
		lblProcentSalariu.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblProcentSalariu.setBounds(647, 348, 120, 25);
		this.add(lblProcentSalariu);

		spProcentSalariu = new JSpinner();
		lblProcentSalariu.setLabelFor(spProcentSalariu);
		spProcentSalariu.setModel(new SpinnerNumberModel(5, 5, 20, 1));
		spProcentSalariu.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		spProcentSalariu.setBounds(803, 348, 185, 25);
		spProcentSalariu.setEnabled(false);
		spProcentSalariu.setToolTipText("Opțiune disponibilă doar pentru funcția Medic.");
		this.add(spProcentSalariu);
		
		JLabel lblTip = new JLabel("Tip:");
		lblTip.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblTip.setBounds(647, 424, 120, 25);
		this.add(lblTip);
		
		cbTip = new JComboBox<String>();
		lblTip.setLabelFor(cbTip);
		cbTip.setModel(new DefaultComboBoxModel<String>(new String[] {"Generalist", "Laborator", "Radiologie"}));
		cbTip.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		cbTip.setBounds(803, 424, 185, 25);
		cbTip.setEnabled(false);
		cbTip.setToolTipText("Opțiune disponibilă doar pentru funcția Asistent Medical.");
		this.add(cbTip);
		
		JLabel lblGrad = new JLabel("Grad:");
		lblGrad.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblGrad.setBounds(647, 462, 120, 25);
		this.add(lblGrad);
		
		cbGrad = new JComboBox<String>();
		lblGrad.setLabelFor(cbGrad);
		cbGrad.setModel(new DefaultComboBoxModel<String>(new String[] {"Secundar", "Principal"}));
		cbGrad.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		cbGrad.setBounds(803, 462, 185, 25);
		cbGrad.setEnabled(false);
		cbGrad.setToolTipText("Opțiune disponibilă doar pentru funcția Asistent Medical.");		
		this.add(cbGrad);
		
		lblNote = new JLabel("Introduceți datele angajatului.");
		lblNote.setHorizontalAlignment(SwingConstants.CENTER);
		lblNote.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		lblNote.setBounds(12, 600, 650, 25);
		lblNote.setVisible(false);
		this.add(lblNote);
		
		JButton btnSend = new JButton("Submit");
		btnSend.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		btnSend.setBounds(850, 600, 120, 25);
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Se verifica daca toate verificarile au fost trecute
				if (foundUsername && foundParola && foundNume && foundPrenume && foundCNP && foundAdresa && foundTelefon && foundEmail && foundIBAN && foundNrContract) {
					if (cbFunctie.getSelectedIndex() == 2 && !foundCodParafa) {
						tfCodParafa.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.RED));
						setNote(false, "Câmpurile nu sunt valide!");
						return;
					}
					
					String query = "CALL ADD_USER('" + 
							tfUsername.getText() + "', '" + 
							(new String(pfParola.getPassword())) + "', '" +
							tfNume.getText() + "', '" + 
							tfPrenume.getText() + "', '" + 
							tfCNP.getText() + "', '" + 
							tfAdresa.getText() + "', '" + 
							tfTelefon.getText() + "', '" + 
							tfEmail.getText() + "', '" + 
							tfIBAN.getText() + "', '" + 
							tfNrContract.getText() + "', '" + 
							cbFunctie.getSelectedIndex() + "', '" + 
							spSalariu.getValue() + "', '" + 
							spNrOre.getValue() + "');";
					
					if (window.dbHandle.doUpdate(query) == 1) {
						setNote(true, ((String) cbFunctie.getSelectedItem()) + " " + tfUsername.getText() + " a fost adăugat!");
						
						if (cbFunctie.getSelectedIndex() == 1 || cbFunctie.getSelectedIndex() == 2) {
							int newUserID = 0;
							query = "SELECT id FROM angajati WHERE username = '" + tfUsername.getText() + "'"; 
							try {
								PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
								ResultSet rst = ps.executeQuery();
								
								if (rst.next()) {
									newUserID = rst.getInt("id");
								} else {
									ErrorLog.printError("WorkerRegisterPanel SelectUserID Username not found.");
								}
								
								ps.close();
								rst.close();
							} catch (SQLException ex) {
								ErrorLog.printError("WorkerRegisterPanel SelectUserID SQLException: " + ex);
							}
							
							if (cbFunctie.getSelectedIndex() == 1) {
								// As.Medical
								query = "INSERT INTO asistenti (id, tip, grad) VALUES (" +
										"'" + newUserID + "', " +
										"'" + ((String) cbTip.getSelectedItem()) + "', " +
										"'" + ((String) cbGrad.getSelectedItem()) + "')";
								
								try {
									PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
									if (ps.executeUpdate() == 1) {
										// success
									} else {
										setNote(true, ((String) cbFunctie.getSelectedItem()) + " " + tfUsername.getText() + " a fost adăugat, însă nu și informațiile suplimentare.");
										ErrorLog.printError("WorkerRegisterPanel AddAssistantInfo Not inserted.");
									}
									
									ps.close();
								} catch (SQLException ex) {
									ErrorLog.printError("WorkerRegisterPanel AddAssistantInfo SQLException: " + ex);
								}
							} else {
								// Medic
								// As.Medical
								query = "INSERT INTO medici (id, cod_parafa, titlu_stiintific, post_didactic, procent_salariu) VALUES (" +
										"'" + newUserID + "', " +
										"'" + tfCodParafa.getText() + "', " +
										"'" + ((String) cbTitluStiintific.getSelectedItem()) + "', " +
										"'" + ((String) cbPostDidactic.getSelectedItem()) + "', " +
										"'" + spProcentSalariu.getValue() + "')";
								
								try {
									PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
									if (ps.executeUpdate() == 1) {
										// success
									} else {
										setNote(true, ((String) cbFunctie.getSelectedItem()) + " " + tfUsername.getText() + " a fost adăugat, însă nu și informațiile suplimentare.");
										ErrorLog.printError("WorkerRegisterPanel AddMedicInfo Not inserted.");
									}
									
									ps.close();
								} catch (SQLException ex) {
									ErrorLog.printError("WorkerRegisterPanel AddMedicInfo SQLException: " + ex);
								}
							}
						}
					} else {
						setNote(false, "A apărut o eroare, vă rugăm reîncercați.");
					}						
				} else {
					if (!foundUsername)
						tfUsername.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.RED));
					if (!foundParola)
						pfParola.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.RED));
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
					if (!foundIBAN)
						tfIBAN.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.RED));
					if (!foundNrContract)
						tfNrContract.setBorder(new MatteBorder(2, 2, 2, 2, (Color) Color.RED));
				
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
				tfUsername.setText("");
				pfParola.setText("");
				tfNume.setText("");
				tfPrenume.setText("");
				tfCNP.setText("");
				tfAdresa.setText("");
				tfTelefon.setText("");
				tfEmail.setText("");
				tfIBAN.setText("");
				tfNrContract.setText("");
				spSalariu.setValue(1450);
				spNrOre.setValue(20);
				cbFunctie.setSelectedIndex(0);
				tfCodParafa.setText("");
				cbTitluStiintific.setSelectedIndex(0);
				cbPostDidactic.setSelectedIndex(0);
				spProcentSalariu.setValue(5);
				cbTip.setSelectedIndex(0);
				cbGrad.setSelectedIndex(0);
				lblNote.setText("");
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
