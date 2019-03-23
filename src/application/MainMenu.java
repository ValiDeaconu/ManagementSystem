/**
 * Modulul pentru meniul principal
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

import tables.Asistent;
import tables.Medic;
import util.DateTime;
import util.ErrorLog;

/**
 * @author Vali
 *
 */

public class MainMenu extends JPanel {	
	private static final long serialVersionUID = 97611355449926248L;
	
	@SuppressWarnings("unused")
	private Main window;
	
	public MainMenu(Main window) {
		super();
		this.window = window;
		this.setLayout(null);
		
		JLabel lblBunVenit = new JLabel("Bun venit, " + window.userLogged.getUsername() + "!");
		lblBunVenit.setHorizontalAlignment(SwingConstants.CENTER);
		lblBunVenit.setFont(new Font("Ubuntu", Font.BOLD, 42));
		lblBunVenit.setBounds(12, 13, 982, 49);
		this.add(lblBunVenit);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBackground(new Color(70, 130, 180));
		separator.setBounds(503, 104, 2, 518);
		this.add(separator);
		
		JLabel lblDatePersonale = new JLabel("Date personale");
		lblDatePersonale.setFont(new Font("Ubuntu", Font.PLAIN, 25));
		lblDatePersonale.setBounds(135, 80, 180, 30);
		this.add(lblDatePersonale);
		
		JLabel lblName = new JLabel("Nume si prenume:");
		lblName.setHorizontalAlignment(SwingConstants.LEFT);
		lblName.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblName.setBounds(12, 121, 138, 30);
		this.add(lblName);
		
		JTextField tfName = new JTextField();
		tfName.setHorizontalAlignment(SwingConstants.CENTER);
		tfName.setEditable(false);
		tfName.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblName.setLabelFor(tfName);
		tfName.setBounds(234, 121, 241, 30);
		tfName.setText(window.userLogged.getNume() + " " + window.userLogged.getPrenume());
		this.add(tfName);
		
		JTextField tfCNP = new JTextField();
		tfCNP.setHorizontalAlignment(SwingConstants.CENTER);
		tfCNP.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfCNP.setEditable(false);
		tfCNP.setColumns(10);
		tfCNP.setBounds(234, 165, 241, 30);
		tfCNP.setText(window.userLogged.getCNP());
		this.add(tfCNP);
		
		JLabel lblCnp = new JLabel("CNP:");
		lblCnp.setHorizontalAlignment(SwingConstants.LEFT);
		lblCnp.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblCnp.setBounds(12, 165, 138, 30);
		this.add(lblCnp);
		
		JLabel lblAdresa = new JLabel("Adresa:");
		lblAdresa.setHorizontalAlignment(SwingConstants.LEFT);
		lblAdresa.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblAdresa.setBounds(12, 208, 138, 30);
		this.add(lblAdresa);
		
		JTextField tfAdresa = new JTextField();
		tfAdresa.setHorizontalAlignment(SwingConstants.CENTER);
		tfAdresa.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfAdresa.setEditable(false);
		tfAdresa.setColumns(10);
		tfAdresa.setBounds(234, 208, 241, 30);
		tfAdresa.setText(window.userLogged.getAdresa());
		this.add(tfAdresa);
		
		JLabel lblTelefon = new JLabel("Telefon:");
		lblTelefon.setHorizontalAlignment(SwingConstants.LEFT);
		lblTelefon.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblTelefon.setBounds(12, 251, 138, 30);
		this.add(lblTelefon);
		
		JTextField tfTelefon = new JTextField();
		tfTelefon.setHorizontalAlignment(SwingConstants.CENTER);
		tfTelefon.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfTelefon.setEditable(false);
		tfTelefon.setColumns(10);
		tfTelefon.setBounds(234, 251, 241, 30);
		tfTelefon.setText(window.userLogged.getTelefon());
		this.add(tfTelefon);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setHorizontalAlignment(SwingConstants.LEFT);
		lblEmail.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblEmail.setBounds(12, 294, 138, 30);
		this.add(lblEmail);
		
		JTextField tfEmail = new JTextField();
		tfEmail.setHorizontalAlignment(SwingConstants.CENTER);
		tfEmail.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfEmail.setEditable(false);
		tfEmail.setColumns(10);
		tfEmail.setBounds(234, 294, 241, 30);
		tfEmail.setText(window.userLogged.getEmail());
		this.add(tfEmail);
		
		JLabel lblIBAN = new JLabel("IBAN:");
		lblIBAN.setHorizontalAlignment(SwingConstants.LEFT);
		lblIBAN.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblIBAN.setBounds(12, 337, 138, 30);
		this.add(lblIBAN);
		
		JTextField tfIBAN = new JTextField();
		tfIBAN.setHorizontalAlignment(SwingConstants.CENTER);
		tfIBAN.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfIBAN.setEditable(false);
		tfIBAN.setColumns(10);
		tfIBAN.setBounds(234, 337, 241, 30);
		tfIBAN.setText(window.userLogged.getIBAN());
		this.add(tfIBAN);
		
		JLabel lblNrContract = new JLabel("Numar contract:");
		lblNrContract.setHorizontalAlignment(SwingConstants.LEFT);
		lblNrContract.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblNrContract.setBounds(12, 379, 138, 30);
		this.add(lblNrContract);
		
		JTextField tfNrContract = new JTextField();
		tfNrContract.setHorizontalAlignment(SwingConstants.CENTER);
		tfNrContract.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfNrContract.setEditable(false);
		tfNrContract.setColumns(10);
		tfNrContract.setBounds(234, 379, 241, 30);
		tfNrContract.setText("" + window.userLogged.getNrContract());
		this.add(tfNrContract);
		
		JLabel lblDataAngajarii = new JLabel("Data angajarii:");
		lblDataAngajarii.setHorizontalAlignment(SwingConstants.LEFT);
		lblDataAngajarii.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblDataAngajarii.setBounds(12, 422, 138, 30);
		this.add(lblDataAngajarii);
		
		JTextField tfDataAngajarii = new JTextField();
		tfDataAngajarii.setHorizontalAlignment(SwingConstants.CENTER);
		tfDataAngajarii.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfDataAngajarii.setEditable(false);
		tfDataAngajarii.setColumns(10);
		tfDataAngajarii.setBounds(234, 422, 241, 30);
		tfDataAngajarii.setText(window.userLogged.getDataAngajarii().convertToDate());
		this.add(tfDataAngajarii);
		
		JLabel lblFunctie = new JLabel("Functie:");
		lblFunctie.setHorizontalAlignment(SwingConstants.LEFT);
		lblFunctie.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblFunctie.setBounds(12, 466, 138, 30);
		this.add(lblFunctie);
		
		JTextField tfFunctie = new JTextField();
		tfFunctie.setHorizontalAlignment(SwingConstants.CENTER);
		tfFunctie.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfFunctie.setEditable(false);
		tfFunctie.setColumns(10);
		tfFunctie.setBounds(234, 466, 241, 30);
		String numeFunctie = new String("");
		switch(window.userLogged.getFunctie()) {
			case 0: numeFunctie += "Receptioner";
					break;
			case 1: numeFunctie += "Asistent Medical";
					break;
			case 2: numeFunctie += "Medic";
					break;
			case 3: numeFunctie += "Contabil";
					break;
			case 4: numeFunctie += "Inspector resurse umane";
					break;
			default: numeFunctie += "Neidentificat";
					break;
		}
		tfFunctie.setText(numeFunctie);
		this.add(tfFunctie);
		
		JLabel lblSalariu = new JLabel("Salariu:");
		lblSalariu.setHorizontalAlignment(SwingConstants.LEFT);
		lblSalariu.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblSalariu.setBounds(12, 508, 138, 30);
		this.add(lblSalariu);
		
		JTextField tfSalariu = new JTextField();
		tfSalariu.setHorizontalAlignment(SwingConstants.CENTER);
		tfSalariu.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfSalariu.setEditable(false);
		tfSalariu.setColumns(10);
		tfSalariu.setBounds(234, 508, 241, 30);
		if (window.userLogged.getFunctie() != 2) {
			tfSalariu.setText(window.userLogged.getSalariu() + "");
		} else {
			Medic user = new Medic(window.dbHandle, window.userLogged.getID());
			tfSalariu.setText(user.getSalariu() + " + " + user.getProcentSalariu() + "%");
		}
		this.add(tfSalariu);
		
		JLabel lblNrOre = new JLabel("Numar de ore lunar:");
		lblNrOre.setHorizontalAlignment(SwingConstants.LEFT);
		lblNrOre.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblNrOre.setBounds(12, 551, 160, 30);
		this.add(lblNrOre);
		
		JTextField tfNrOre = new JTextField();
		tfNrOre.setHorizontalAlignment(SwingConstants.CENTER);
		tfNrOre.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfNrOre.setEditable(false);
		tfNrOre.setColumns(10);
		tfNrOre.setBounds(234, 551, 241, 30);
		tfNrOre.setText(window.userLogged.getNrOre() + "");
		this.add(tfNrOre);
		
		JLabel lblProgram = new JLabel("Orar:");
		lblProgram.setHorizontalAlignment(SwingConstants.LEFT);
		lblProgram.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblProgram.setBounds(12, 591, 138, 30);
		this.add(lblProgram);
		
		boolean isInVacation = false;
		DateTime dataInceput = null, dataSfarsit = null;
		try {
			String query = "SELECT * FROM concedii WHERE (NOW() BETWEEN data_inceput AND data_sfarsit) AND angajat_id = '" + window.userLogged.getID() + "' LIMIT 1";
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();
			
			if (rst.next()) {
				isInVacation = true;
				dataInceput = new DateTime(rst.getString("data_inceput"));
				dataSfarsit = new DateTime(rst.getString("data_sfarsit"));
			}
		} catch (SQLException e) {
			ErrorLog.printError("SchedulePanelButton SQLException " + e);
			isInVacation = false;
		}
		
		JButton btnProgram = new JButton("Vizualizeaza");
		btnProgram.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		btnProgram.setBounds(234, 591, 241, 31);
		
		if (isInVacation) {
			btnProgram.setEnabled(false);
			btnProgram.setToolTipText("* Ești în concediu începând cu " + dataInceput.convertToHumanDate() + " până " + dataSfarsit.convertToHumanDate() + ".");
		}
		
		btnProgram.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				String tabTitle = "Orar angajat " + window.userLogged.getUsername();
				if (!window.isTabOpened(tabTitle)) {
					window.addNewTab(new SchedulePanel(window, tabTitle), tabTitle);
				}
			}
		});
		this.add(btnProgram);
		
		if (window.userLogged.getFunctie() == 1) {
			/**
			 * ASISTENT MEDICAL INFO
			 */
			
			Asistent user = new Asistent(window.dbHandle, window.userLogged.getID());
			
			JLabel lblMedicalTitle = new JLabel("Asistent Medical");
			lblMedicalTitle.setHorizontalAlignment(SwingConstants.CENTER);
			lblMedicalTitle.setFont(new Font("Ubuntu", Font.PLAIN, 25));
			lblMedicalTitle.setBounds(531, 80, 463, 30);
			this.add(lblMedicalTitle);
			
			JLabel lblTip = new JLabel("Tip:");
			lblTip.setHorizontalAlignment(SwingConstants.LEFT);
			lblTip.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			lblTip.setBounds(531, 121, 138, 30);
			this.add(lblTip);
			
			JTextField tfTip = new JTextField();
			tfTip.setHorizontalAlignment(SwingConstants.CENTER);
			tfTip.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			tfTip.setEditable(false);
			tfTip.setBounds(753, 121, 241, 30);
			tfTip.setText(user.getTip());
			lblTip.setLabelFor(tfTip);
			this.add(tfTip);
			
			JLabel lblGrad = new JLabel("Grad:");
			lblGrad.setHorizontalAlignment(SwingConstants.LEFT);
			lblGrad.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			lblGrad.setBounds(531, 165, 138, 30);
			this.add(lblGrad);
			
			JTextField tfGrad = new JTextField();
			tfGrad.setHorizontalAlignment(SwingConstants.CENTER);
			tfGrad.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			tfGrad.setEditable(false);
			tfGrad.setColumns(10);
			tfGrad.setBounds(753, 165, 241, 30);
			tfGrad.setText(user.getGrad());
			lblGrad.setLabelFor(tfGrad);
			this.add(tfGrad);
		} else if (window.userLogged.getFunctie() == 2) {
			/**
			 * MEDIC INFO
			 */
			
			Medic user = new Medic(window.dbHandle, window.userLogged.getID());
			
			JLabel lblMedicalTitle = new JLabel("Medic");
			lblMedicalTitle.setHorizontalAlignment(SwingConstants.CENTER);
			lblMedicalTitle.setFont(new Font("Ubuntu", Font.PLAIN, 25));
			lblMedicalTitle.setBounds(531, 80, 463, 30);
			this.add(lblMedicalTitle);
			
			JLabel lblCodParafa = new JLabel("Cod parafa:");
			lblCodParafa.setHorizontalAlignment(SwingConstants.LEFT);
			lblCodParafa.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			lblCodParafa.setBounds(531, 121, 138, 30);
			this.add(lblCodParafa);
			
			JTextField tfCodParafa = new JTextField();
			tfCodParafa.setHorizontalAlignment(SwingConstants.CENTER);
			tfCodParafa.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			tfCodParafa.setEditable(false);
			tfCodParafa.setBounds(753, 121, 241, 30);
			tfCodParafa.setText(user.getCodParafa() + "");
			lblCodParafa.setLabelFor(tfCodParafa);
			this.add(tfCodParafa);
			
			JLabel lblTitluStiintific = new JLabel("Titlu stiintific:");
			lblTitluStiintific.setHorizontalAlignment(SwingConstants.LEFT);
			lblTitluStiintific.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			lblTitluStiintific.setBounds(531, 165, 138, 30);
			this.add(lblTitluStiintific);
			
			JTextField tfTitluStiintific = new JTextField();
			tfTitluStiintific.setHorizontalAlignment(SwingConstants.CENTER);
			tfTitluStiintific.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			tfTitluStiintific.setEditable(false);
			tfTitluStiintific.setColumns(10);
			tfTitluStiintific.setBounds(753, 165, 241, 30);
			tfTitluStiintific.setText(user.getTitluStiintific());
			lblTitluStiintific.setLabelFor(tfTitluStiintific);
			this.add(tfTitluStiintific);
			
			JLabel lblPostDidactic = new JLabel("Post didactic:");
			lblPostDidactic.setHorizontalAlignment(SwingConstants.LEFT);
			lblPostDidactic.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			lblPostDidactic.setBounds(531, 208, 138, 30);
			this.add(lblPostDidactic);
			
			JTextField tfPostDidactic = new JTextField();
			tfPostDidactic.setHorizontalAlignment(SwingConstants.CENTER);
			tfPostDidactic.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			tfPostDidactic.setEditable(false);
			tfPostDidactic.setColumns(10);
			tfPostDidactic.setBounds(753, 208, 241, 30);
			tfPostDidactic.setText(user.getPostDidactic());
			lblPostDidactic.setLabelFor(tfPostDidactic);
			this.add(tfPostDidactic);
			
			JLabel lblSpecialitati = new JLabel("Specialitati:");
			lblSpecialitati.setHorizontalAlignment(SwingConstants.LEFT);
			lblSpecialitati.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			lblSpecialitati.setBounds(531, 251, 138, 30);
			this.add(lblSpecialitati);
			
			JButton btnSpecialitati = new JButton("Vizualizeaza");
			btnSpecialitati.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			btnSpecialitati.setBounds(753, 251, 241, 31);
			btnSpecialitati.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {	
					String tabTitle = "Lista specialitati " + window.userLogged.getUsername();
					if (!window.isTabOpened(tabTitle)) {
						window.addNewTab(new AbilityPanel(window, tabTitle), tabTitle);
					}
				}
			});
			this.add(btnSpecialitati);
		}
		
		JLabel lblOptiuni = new JLabel("Optiuni");
		lblOptiuni.setHorizontalAlignment(SwingConstants.CENTER);
		lblOptiuni.setFont(new Font("Ubuntu", Font.PLAIN, 25));
		lblOptiuni.setBounds(531, 291, 463, 30);
		this.add(lblOptiuni);

		if (window.userLogged.getAdminType() >= 1) {			
			JButton btnFunction1 = new JButton("Modifica datele personale");
			btnFunction1.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			btnFunction1.setToolTipText("[Functie Administrator] Modul pentru modificarea datelor personale ale angajatilor.");
			btnFunction1.setBounds(644, 337, 241, 30);
			btnFunction1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String tabTitle = "Date personale";
					if (!window.isTabOpened(tabTitle)) {
						window.addNewTab(new PersonalDataPanel(window, tabTitle), tabTitle);
					}
				}				
			});
			this.add(btnFunction1);
		}
		

		/*JButton btnFunction2 = new JButton("Buton 2");
		btnFunction2.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		btnFunction2.setBounds(644, 379, 241, 30);
		btnFunction2.setToolTipText("tooltip button 2");
		this.add(btnFunction2);
		
		JButton btnFunction3 = new JButton("Buton 3");
		btnFunction3.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		btnFunction3.setBounds(644, 422, 241, 30);
		btnFunction3.setToolTipText("tooltip button 3");
		this.add(btnFunction3);
		
		JButton btnFunction4 = new JButton("Buton 4");
		btnFunction4.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		btnFunction4.setBounds(644, 466, 241, 30);
		btnFunction4.setToolTipText("Modul pentru tiparirea unui bon fiscal.");
		this.add(btnFunction4);
		
		JButton btnFunction5 = new JButton("Buton 5");
		btnFunction5.setToolTipText("tooltip buton 5");
		btnFunction5.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		btnFunction5.setBounds(644, 508, 241, 30);
		this.add(btnFunction5);
		
		JButton btnFunction6 = new JButton("Buton 6");
		btnFunction6.setToolTipText("tooltip buton 6");
		btnFunction6.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		btnFunction6.setBounds(644, 551, 241, 30);
		this.add(btnFunction6);
		
		JButton btnFunction7 = new JButton("Buton 7");
		btnFunction7.setToolTipText("tooltip buton 7");
		btnFunction7.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		btnFunction7.setBounds(644, 593, 241, 30);
		this.add(btnFunction7);*/
		
		if (window.userLogged.getFunctie() == 0) {	
			/**
			 *  RECEPTIONER
			 */
			JButton btnFunction2 = new JButton("Lista programari");
			btnFunction2.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			btnFunction2.setBounds(644, 379, 241, 30);
			btnFunction2.setToolTipText("Modul pentru vizualizarea listei de programari.");
			btnFunction2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String tabTitle = "Lista programari";
					if (!window.isTabOpened(tabTitle)) {
						window.addNewTab(new ScheduleListPanel(window), tabTitle);
					}
				}				
			});
			this.add(btnFunction2);
			
			JButton btnFunction3 = new JButton("Înregistrare pacient");
			btnFunction3.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			btnFunction3.setBounds(644, 422, 241, 30);
			btnFunction3.setToolTipText("Modul pentru inregistrarea unui pacient nou.");
			btnFunction3.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String tabTitle = "Înregistrare pacient";
					if (!window.isTabOpened(tabTitle)) {
						window.addNewTab(new PatientRegisterPanel(window, tabTitle), tabTitle);
					}
				}				
			});
			this.add(btnFunction3);
			
			JButton btnFunction4 = new JButton("Emite bon fiscal");
			btnFunction4.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			btnFunction4.setBounds(644, 466, 241, 30);
			btnFunction4.setToolTipText("Modul pentru tiparirea unui bon fiscal.");
			btnFunction4.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String tabTitle = "Emitere bon";
					if (!window.isTabOpened(tabTitle)) {
						window.addNewTab(new ReceiptIssue(window, tabTitle), tabTitle);
					}
				}				
			});
			this.add(btnFunction4); 
		} else if (window.userLogged.getFunctie() == 1) {	
			/**
			 *  ASISTENT MEDICAL
			 */
			JButton btnFunction2 = new JButton("Rapoarte medicale");
			btnFunction2.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			btnFunction2.setBounds(644, 379, 241, 30);
			btnFunction2.setToolTipText("Modul pentru vizualizarea/inregistrarea rapoartelor medicale.");
			btnFunction2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String tabTitle = "Rapoarte medicale";
					if (!window.isTabOpened(tabTitle)) {
						window.addNewTab(new ViewMedicalReportPanel(window, tabTitle), tabTitle);
					}
				}
			});
			this.add(btnFunction2);
		} else if (window.userLogged.getFunctie() == 2) { 
			/**
			 * MEDIC
			 */
			JButton btnFunction2 = new JButton("Rapoarte medicale");
			btnFunction2.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			btnFunction2.setBounds(644, 379, 241, 30);
			btnFunction2.setToolTipText("Modul pentru vizualizarea/adăugarea/ștergerea rapoartelor medicale.");
			btnFunction2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String tabTitle = "Rapoarte medicale";
					if (!window.isTabOpened(tabTitle)) {
						window.addNewTab(new ViewMedicalReportPanel(window, tabTitle), tabTitle);
					}
				}
			});
			this.add(btnFunction2);
			
			JButton btnFunction3 = new JButton("Lista programari");
			btnFunction3.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			btnFunction3.setBounds(644, 422, 241, 30);
			btnFunction3.setToolTipText("Modul pentru vizualizarea listei de programari.");
			btnFunction3.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String tabTitle = "Lista programari";
					if (!window.isTabOpened(tabTitle)) {
						window.addNewTab(new ScheduleListPanel(window), tabTitle);
					}
				}
			});
			this.add(btnFunction3);
			
			JButton btnFunction4 = new JButton("Lista servicii");
			btnFunction4.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			btnFunction4.setBounds(644, 466, 241, 30);
			btnFunction4.setToolTipText("Modul pentru vizualizarea si modificarea preturilor serviciilor.");
			btnFunction4.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String tabTitle = "Lista servicii";
					if (!window.isTabOpened(tabTitle)) {
						window.addNewTab(new ServiceListPanel(window, tabTitle), tabTitle);
					}
				}
			});
			this.add(btnFunction4);
			
			JButton btnFunction5 = new JButton("Contabilitate");
			btnFunction5.setToolTipText("Modul pentru vizualizarea profitului brut și net.");
			btnFunction5.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			btnFunction5.setBounds(644, 508, 241, 30);
			btnFunction5.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String tabTitle = "Contabilitate";
					if (!window.isTabOpened(tabTitle)) {
						window.addNewTab(new ViewProfit(window), tabTitle);
					}
				}
			});
			this.add(btnFunction5);
		} else if (window.userLogged.getFunctie() == 3) {
			/**
			 * CONTABIL
			 */
			JButton btnFunction2 = new JButton("Contabilitate");
			btnFunction2.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			btnFunction2.setBounds(644, 379, 241, 30);
			btnFunction2.setToolTipText("Modul pentru vizualizarea profitului brut și net.");
			btnFunction2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String tabTitle = "Contabilitate";
					if (!window.isTabOpened(tabTitle)) {
						window.addNewTab(new ViewProfit(window), tabTitle);
					}
				}
			});
			this.add(btnFunction2);
		} else if (window.userLogged.getFunctie() == 4) {
			/**
			 * INSPECTOR RESURSE UMANE
			 */
			JButton btnFunction2 = new JButton("Adaugare concediu");
			btnFunction2.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			btnFunction2.setBounds(644, 379, 241, 30);
			btnFunction2.setToolTipText("Modul pentru adaugare de concedii."); 
			btnFunction2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String tabTitle = "Adaugare concediu";
					if (!window.isTabOpened(tabTitle)) {
						window.addNewTab(new AddLeave(window, tabTitle), tabTitle);
					}
				}
			});
			this.add(btnFunction2);
			
			JButton btnFunction3 = new JButton("Înregistrează angajat");
			btnFunction3.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			btnFunction3.setBounds(644, 422, 241, 30);
			btnFunction3.setToolTipText("Modul pentru înregistrarea unui nou angajat.");
			btnFunction3.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String tabTitle = "Înregistrare angajat";
					if (!window.isTabOpened(tabTitle)) {
						window.addNewTab(new EmployeeRegisterPanel(window, tabTitle), tabTitle);
					}					
				}
				
			});
			this.add(btnFunction3);
			
			JButton btnFunction4 = new JButton("Orar");
			btnFunction4.setFont(new Font("Ubuntu", Font.PLAIN, 16));
			btnFunction4.setBounds(644, 466, 241, 30);
			btnFunction4.setToolTipText("Modul pentru adăugarea/modificarea orarului unui angajat.");
			btnFunction4.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String tabTitle = "Modul orar";
					if (!window.isTabOpened(tabTitle)) {
						window.addNewTab(new ModifyProgram(window), tabTitle);
					}					
				}
				
			});
			this.add(btnFunction4);
		}
	}
}
