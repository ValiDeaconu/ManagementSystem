/**
 * Modul pentru vizualizarea rapoartelor medicale
 */
package application;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import tables.Angajat;
import tables.Pacient;
import tables.Raport;
import util.DateTime;
import util.ErrorLog;

/**
 * @author Vali
 *
 */
public class ViewMedicalReportPanel extends JPanel {
	private static final long serialVersionUID = 6492251773027303144L;

	private Main window;
	
	public ViewMedicalReportPanel(Main window, String tabTitle) {
		super();
		this.window = window;
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
		
		JTable viewTable = new JTable() {
			private static final long serialVersionUID = -6294149579462622828L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		};

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 83, 989, 526);
		this.add(scrollPane);

		DefaultTableModel viewTableModel = new DefaultTableModel (
			new Object[][] {
			
			},
			new String[] {
				"ID", "Medic", "Pacient", "Data emitere raport"	
			}
		) {
			private static final long serialVersionUID = 1L;
			
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		
		viewTable.setModel(viewTableModel);
		viewTable.getColumnModel().getColumn(0).setMinWidth(40);
		viewTable.getColumnModel().getColumn(1).setMinWidth(150);
		viewTable.getColumnModel().getColumn(2).setMinWidth(150);
		viewTable.getColumnModel().getColumn(3).setMinWidth(200);
		viewTable.setFont(new Font("Ubuntu", Font.PLAIN, 16));
	
		updateTable(viewTableModel);
		
		viewTable.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent mouseEvent) {
		        Point point = mouseEvent.getPoint();
		        int row = viewTable.rowAtPoint(point);
		        if (mouseEvent.getClickCount() == 2 && viewTable.getSelectedRow() != -1) {
		        	int raportID = (int) viewTable.getValueAt(row, 0);
		        	Raport r = new Raport(window.dbHandle, raportID);
		        	showReportDetails(r);
		        }
		    }
		});
		
		scrollPane.setViewportView(viewTable);

		JLabel lblWarning = new JLabel("Apasă dublu click pentru a vedea raportul în detaliu.");
		lblWarning.setFont(new Font("Ubuntu", Font.ITALIC, 16));
		lblWarning.setBounds(12, 616, 736, 20);
		lblWarning.setVisible(true);
		this.add(lblWarning);
	
		if (window.userLogged.getFunctie() == 2) {
			JButton btnInsertRow = new JButton("Adaugă rând");
			btnInsertRow.setIcon(new ImageIcon("resources/add_icon.png"));
			btnInsertRow.setFont(new Font("Ubuntu", Font.PLAIN, 14));
			btnInsertRow.setBounds(509, 616, 144, 20);
			btnInsertRow.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String tabTitle = "Adaugă raport";
					if (!window.isTabOpened(tabTitle)) {
						window.addNewTab(new AddMedicalReportPanel(window, tabTitle), tabTitle);
					}
				}
				
			});
			this.add(btnInsertRow);
			
			JButton btnDeleteRow = new JButton("Șterge rând");
			btnDeleteRow.setIcon(new ImageIcon("resources/delete_icon.png"));
			btnDeleteRow.setFont(new Font("Ubuntu", Font.PLAIN, 14));
			btnDeleteRow.setBounds(683, 616, 144, 20);
			btnDeleteRow.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (viewTable.getSelectedRow() != -1) {
						int reportID = (int) viewTable.getValueAt(viewTable.getSelectedRow(), 0);
						
						String query = "DELETE FROM `rapoarte` WHERE id = '" + reportID + "'";
						if (window.dbHandle.doUpdate(query) == 1) {
							JOptionPane.showMessageDialog(window.mainFrame, "Raportul a fost șters cu succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);
							updateTable(viewTableModel);
						} else {
							JOptionPane.showMessageDialog(window.mainFrame, "Ștergerea a eșuat.", "Eroare", JOptionPane.ERROR_MESSAGE);
							ErrorLog.printError("ViewMedicalReportPanel RemoveReport Au fost afectate mai multe linii.");
						}
					}
				}
				
			});
			this.add(btnDeleteRow);
		}
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setIcon(new ImageIcon("resources/refresh_icon.png"));
		btnRefresh.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		btnRefresh.setBounds(857, 616, 144, 20);
		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateTable(viewTableModel);
			}
			
		});
		this.add(btnRefresh);
	}
	
	private void updateTable(DefaultTableModel viewTableModel) {
		viewTableModel.setRowCount(0);
		
		String query = "SELECT rapoarte.id AS 'id', "
				+ "CONCAT(angajati.nume, ' ', angajati.prenume) AS 'medic', "
				+ "CONCAT(pacienti.nume, ' ', pacienti.prenume) AS 'pacient', "
				+ "rapoarte.data_raport AS 'data' " + 
				"FROM `rapoarte` " + 
				"INNER JOIN angajati ON rapoarte.medic_id = angajati.id " + 
				"INNER JOIN pacienti ON rapoarte.pacient_id = pacienti.id";
		
		try {
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();
			
			while (rst.next()) {
				Object[] row = new Object[4];
				
				row[0] = rst.getInt("id");
				row[1] = rst.getString("medic");
				row[2] = rst.getString("pacient");
				row[3] = (new DateTime(rst.getString("data"))).convertToHumanDate();
				
				viewTableModel.addRow(row);
			}
			
			ps.close();
			rst.close();
		} catch (SQLException ex) {
			ErrorLog.printError("ViewMedicalReportPanel updateTable SQLException: " + ex);
		}
	}
	
	private void showReportDetails(Raport r) {
		JPanel raportPanel = null;

		String tabTitle = "Raport #" + r.getID();
		if (!window.isTabOpened(tabTitle)) {
			raportPanel = new JPanel();
			window.addNewTab(raportPanel, tabTitle);
		} else {
			return;
		}
		
		raportPanel.setLayout(null);
		
		Angajat medic = new Angajat(window.dbHandle, r.getMedicID());
		Pacient pacient = new Pacient(window.dbHandle, r.getPacientID());
		
		JLabel lblVizualizareRaportMedical = new JLabel("Vizualizare raport #" + r.getID());
		lblVizualizareRaportMedical.setHorizontalAlignment(SwingConstants.CENTER);
		lblVizualizareRaportMedical.setFont(new Font("Ubuntu", Font.PLAIN, 42));
		lblVizualizareRaportMedical.setBounds(12, 13, 989, 49);
		raportPanel.add(lblVizualizareRaportMedical);
		
		JSeparator separator = new JSeparator();
		separator.setBackground(new Color(70, 130, 180));
		separator.setBounds(62, 75, 877, 2);
		raportPanel.add(separator);
		
		JLabel lblMedic = new JLabel("Medic responsabil:");
		lblMedic.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblMedic.setBounds(65, 100, 140, 28);
		raportPanel.add(lblMedic);
		
		JTextField tfMedic = new JTextField();
		lblMedic.setLabelFor(tfMedic);
		tfMedic.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfMedic.setBounds(214, 100, 280, 28);
		tfMedic.setEditable(false);
		tfMedic.setText(medic.getNume() + " " + medic.getPrenume());
		raportPanel.add(tfMedic);
		tfMedic.setColumns(10);
		
		JLabel lblPacient = new JLabel("Pacient:");
		lblPacient.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblPacient.setBounds(65, 141, 140, 28);
		raportPanel.add(lblPacient);
		
		JTextField tfPacient = new JTextField();
		lblPacient.setLabelFor(tfPacient);
		tfPacient.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfPacient.setColumns(10);
		tfPacient.setBounds(214, 141, 280, 28);
		tfPacient.setEditable(false);
		tfPacient.setText(pacient.getNume() + " " + pacient.getPrenume());
		raportPanel.add(tfPacient);
		
		JLabel lblIstoric = new JLabel("Istoric:");
		lblIstoric.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblIstoric.setBounds(62, 182, 140, 28);
		raportPanel.add(lblIstoric);
		
		JScrollPane spIstoric = new JScrollPane();
		spIstoric.setBounds(216, 182, 278, 68);
		raportPanel.add(spIstoric);
		
		JTextArea taIstoric = new JTextArea();
		spIstoric.setViewportView(taIstoric);
		lblIstoric.setLabelFor(taIstoric);
		taIstoric.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		taIstoric.setLineWrap(true);
		taIstoric.setWrapStyleWord(true);
		taIstoric.setEditable(false);
		taIstoric.setText(r.getIstoric());
		
		JLabel lblSimptome = new JLabel("Simptome:");
		lblSimptome.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblSimptome.setBounds(62, 264, 140, 28);
		raportPanel.add(lblSimptome);
		
		JScrollPane spSimptome = new JScrollPane();
		spSimptome.setBounds(216, 264, 278, 68);
		raportPanel.add(spSimptome);
		
		JTextArea taSimptome = new JTextArea();
		spSimptome.setViewportView(taSimptome);
		lblSimptome.setLabelFor(taSimptome);
		taSimptome.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		taSimptome.setLineWrap(true);
		taSimptome.setWrapStyleWord(true);
		taSimptome.setEditable(false);
		taSimptome.setText(r.getSimptome());
		
		JLabel lblInvestigatii = new JLabel("Investigatii:");
		lblInvestigatii.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblInvestigatii.setBounds(62, 349, 140, 28);
		raportPanel.add(lblInvestigatii);
		
		JScrollPane spInvestigatii = new JScrollPane();
		spInvestigatii.setBounds(216, 349, 278, 68);
		raportPanel.add(spInvestigatii);
		
		JTextArea taInvestigatii = new JTextArea();
		lblInvestigatii.setLabelFor(taInvestigatii);
		spInvestigatii.setViewportView(taInvestigatii);
		taInvestigatii.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		taInvestigatii.setLineWrap(true);
		taInvestigatii.setWrapStyleWord(true);
		taInvestigatii.setEditable(false);
		taInvestigatii.setText(r.getInvestigatii());
		
		JLabel lblDiagnostic = new JLabel("Diagnostic:");
		lblDiagnostic.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblDiagnostic.setBounds(62, 434, 140, 28);
		raportPanel.add(lblDiagnostic);
		
		JScrollPane spDiagnostic = new JScrollPane();
		spDiagnostic.setBounds(214, 434, 278, 68);
		raportPanel.add(spDiagnostic);
		
		JTextArea taDiagnostic = new JTextArea();
		spDiagnostic.setViewportView(taDiagnostic);
		lblDiagnostic.setLabelFor(taDiagnostic);
		taDiagnostic.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		taDiagnostic.setLineWrap(true);
		taDiagnostic.setWrapStyleWord(true);
		taDiagnostic.setEditable(false);
		taDiagnostic.setText(r.getDiagnostic());
		
		JLabel lblRecomandari = new JLabel("Recomandari:");
		lblRecomandari.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblRecomandari.setBounds(65, 517, 140, 28);
		raportPanel.add(lblRecomandari);
		
		JScrollPane spRecomandari = new JScrollPane();
		spRecomandari.setBounds(214, 517, 278, 68);
		raportPanel.add(spRecomandari);
		
		JTextArea taRecomandari = new JTextArea();
		spRecomandari.setViewportView(taRecomandari);
		lblRecomandari.setLabelFor(taRecomandari);
		taRecomandari.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		taRecomandari.setLineWrap(true);
		taRecomandari.setWrapStyleWord(true);
		taRecomandari.setEditable(false);
		taRecomandari.setText(r.getRecomandari());
		
		JLabel lblParafa = new JLabel("Parafă:");
		lblParafa.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblParafa.setBounds(62, 595, 140, 28);
		raportPanel.add(lblParafa);
		
		JRadioButton rdbtnDa = new JRadioButton("Da");
		rdbtnDa.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		rdbtnDa.setBounds(214, 598, 127, 25);
		rdbtnDa.setEnabled(false);
		if (r.isParafa())
			rdbtnDa.setSelected(true);
		raportPanel.add(rdbtnDa);
		
		JRadioButton rdbtnNu = new JRadioButton("Nu");
		rdbtnNu.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		rdbtnNu.setBounds(367, 598, 127, 25);
		rdbtnNu.setEnabled(false);
		if (!r.isParafa())
			rdbtnNu.setSelected(true);
		raportPanel.add(rdbtnNu);
		
		if (rdbtnNu.isSelected()) {
			if (window.userLogged.getFunctie() == 2) {
				taIstoric.setEditable(true);
				taSimptome.setEditable(true);
				taInvestigatii.setEditable(true);
				taDiagnostic.setEditable(true);
				taRecomandari.setEditable(true);
				
				rdbtnDa.setEnabled(true);
				rdbtnNu.setEnabled(true);
			} else if (window.userLogged.getFunctie() == 1) {
				taInvestigatii.setEditable(true);
			}
		}
		
		ButtonGroup parafaGroup = new ButtonGroup();
		parafaGroup.add(rdbtnDa);
		parafaGroup.add(rdbtnNu);
		
		JScrollPane spPrint = new JScrollPane();
		spPrint.setBounds(580, 90, 359, 495);
		raportPanel.add(spPrint);
		
		JEditorPane epPrint = new JEditorPane();
		spPrint.setViewportView(epPrint);
		epPrint.setContentType("text/html");
		epPrint.setEditable(false);
		epPrint.setFont(new Font("Ubuntu", Font.PLAIN, 16));	

		JButton btnSave = new JButton("Salvează");
		btnSave.setIcon(new ImageIcon("resources/save_icon.png"));
		btnSave.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		btnSave.setBounds(580, 598, 155, 25);
		if (r.isParafa())
			btnSave.setEnabled(false);
		
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				r.setDiagnostic(taDiagnostic.getText());
				r.setInvestigatii(taInvestigatii.getText());
				r.setIstoric(taIstoric.getText());
				r.setRecomandari(taRecomandari.getText());
				r.setSimptome(taSimptome.getText());
				
				if (rdbtnDa.isSelected())
					r.setParafa(true);
				
				if (rdbtnNu.isSelected())
					r.setParafa(false);
				
				r.updateOnDatabase(window.dbHandle);
				JOptionPane.showMessageDialog(window.mainFrame, "Modificările au fost salvate!", "Succes", JOptionPane.INFORMATION_MESSAGE);
			}			
		});
		raportPanel.add(btnSave);
		
		JButton btnGeneratePrint = new JButton("Generează");
		btnGeneratePrint.setIcon(new ImageIcon("resources/print_icon.png"));
		btnGeneratePrint.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		btnGeneratePrint.setBounds(784, 598, 155, 25);
		btnGeneratePrint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String[] info = new String[15];
				
				try {
					String query = "CALL GET_REPORT_INFO('" + r.getID() + "')";
					PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
					ResultSet rst = ps.executeQuery();
					
					if (rst.next()) {
						info[0] = rst.getString("medic");
						info[1] = rst.getString("medic_telefon");
						info[2] = rst.getString("medic_email");
						info[3] = rst.getString("pacient");
						info[4] = rst.getString("pacient_cnp");
						info[5] = rst.getString("pacient_telefon");
						info[6] = rst.getString("pacient_email");
						info[7] =  (new DateTime(rst.getString("data_raport"))).convertToHumanDateWithoutDay();
						info[8] = rst.getString("istoric");
						info[9] = rst.getString("simptome");
						info[10] = rst.getString("investigatii");
						info[11] = rst.getString("diagnostic");
						info[12] = rst.getString("recomandari");
						if (rst.getInt("parafa") == 1)
							info[13] = new String("CU PARAFĂ");
						else
							info[13] = new String("FARA PARAFĂ");
						info[14] = rst.getString("id_unic");
					}
				} catch (SQLException ex) {
					ErrorLog.printError("ReciptIssue OpenReceipt SQLException: " + ex);
				}
				
				epPrint.setText(""
					+ "<center>" + info[0] + "</center>\n"
					+ "<center>" + info[1] + "</center>\n"
					+ "<center>" + info[2] + "</center>\n\n"
					+ "<center>*********************************************</center>\n\n"
					+ "PACIENT: <div align='right'>" + info[3] + "</div>\n"
					+ "CNP: <div align='right'>" + info[4] + "</div>\n"
					+ "TELEFON: <div align='right'>" + info[5] + "</div>\n"
					+ "EMAIL: <div align='right'>" + info[6] + "</div>\n\n"
					+ "<center>*********************************************</center>\n\n"
					+ "ISTORIC: <div align='right'>" + info[8] + "</div>\n"
					+ "SIMPTOME: <div align='right'>" + info[9] + "</div>\n"
					+ "INVESTIGATII: <div align='right'>" + info[10] + "</div>\n"
					+ "DIAGNOSTIC: <div align='right'>" + info[11] + "</div>\n"
					+ "RECOMANDARI: <div align='right'>" + info[12] + "</div>\n"
					+ "PARAFA: <div align='right'>" + info[13] + "</div>\n\n"
					+ "DATA RAPORT: <div align='right'>" + info[7] + "</div>\n\n"
					+ "<center>*********************************************</center>\n\n"
					+ "DATA: <div align='right'>" + (new DateTime(Calendar.getInstance())).convertToHumanDate() + "</div>\n"
					+ "ID UNIC: <div align='right'>" + info[14] + "</div>\n\n\n"
					+ "<center>R A P O R T&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;M E D I C A L</center>"
				);
			}
			
		});
		raportPanel.add(btnGeneratePrint);
	}
}
