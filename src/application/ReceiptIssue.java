/**
 * 
 */
package application;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;


import tables.Bon;
import tables.Competenta;
import tables.Pacient;
import tables.Policlinica;
import tables.Programare;
import tables.Serviciu;
import util.DateTime;
import util.ErrorLog;

/**
 * @author Ana-Maria
 *
 */
public class ReceiptIssue extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6964803282636155262L;
	
	private Main window;

	private String query = "SELECT * FROM programari";
	
	public ReceiptIssue(Main window, String tabTitle) {
		super();

		this.setLayout(null);
		this.window=window;
		
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
				"ID", "Pacient", "Competenta", "Policlinica", "Serviciu", "Data Programarii"
			
			}
		) {
			private static final long serialVersionUID = 1L;
			
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		
		viewTable.setModel(viewTableModel);
		viewTable.getColumnModel().getColumn(0).setMinWidth(50);
		viewTable.getColumnModel().getColumn(0).setResizable(false);
		viewTable.getColumnModel().getColumn(1).setMinWidth(150);
		viewTable.getColumnModel().getColumn(1).setResizable(false);
		viewTable.getColumnModel().getColumn(2).setMinWidth(180);
		viewTable.getColumnModel().getColumn(2).setResizable(false);
		viewTable.getColumnModel().getColumn(3).setMinWidth(250);
		viewTable.getColumnModel().getColumn(3).setResizable(false);
		viewTable.getColumnModel().getColumn(4).setMinWidth(150);
		viewTable.getColumnModel().getColumn(4).setResizable(false);
		viewTable.getColumnModel().getColumn(5).setMinWidth(210);
		viewTable.getColumnModel().getColumn(5).setResizable(false);
		
		viewTable.setFont(new Font("Ubuntu", Font.PLAIN, 16));
	
		updateTable(viewTableModel);
		
		viewTable.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent mouseEvent) {
		        Point point = mouseEvent.getPoint();
		        int row = viewTable.rowAtPoint(point);
		        if (mouseEvent.getClickCount() == 2 && viewTable.getSelectedRow() != -1) {
		        	String str = (String)viewTable.getValueAt(row, 0);
		        	Integer programareID = Integer.parseInt(str);
		        	Programare p = new Programare(window.dbHandle, programareID);
		        	int serviciuID = 0;
		        	String queryMedic = "SELECT id FROM servicii where medic_id=" + p.getMedicID() + " AND denumire_id=" + p.getServiciuID();
					try {

						PreparedStatement ps1 = window.dbHandle.getConnection().prepareStatement(queryMedic);

						ResultSet rst1 = ps1.executeQuery();

						if (rst1.next()) {

							serviciuID = rst1.getInt("id");
					
						}						
					} catch (SQLException ex) {
						ErrorLog.printError("SQLException: " + ex);
					}
		        	
		        	Serviciu s = new Serviciu(window.dbHandle, serviciuID);
		        	int suma = s.getPret();
		        	String queryInsert = "INSERT INTO bonuri_emise (programare_id, suma, data_tiparire) VALUES (" + programareID + ", " + suma + ", NOW());";
		        	
		        	if (window.dbHandle.doUpdate(queryInsert) == 1) {
						JOptionPane.showMessageDialog(window.mainFrame, "Bonul a fost emis cu succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(window.mainFrame, "Bonul nu a putut fi emis.", "Eroare", JOptionPane.ERROR_MESSAGE);
					}
					
		        	String queryUpdate = "UPDATE programari SET status = 2 WHERE id=" + programareID;
					window.dbHandle.doUpdate(queryUpdate);
		        	
					String queryBon = "SELECT id FROM bonuri_emise where programare_id=" + programareID;
					ArrayList<String[]> result = (window.dbHandle).doQuery(queryBon); 
					int id = Integer.parseInt((result.get(0))[0]);

					Bon b = new Bon(window.dbHandle, id);
		        	openReceipt(b);
		        }
		    }
		});
		
		scrollPane.setViewportView(viewTable);

		JLabel lblHint = new JLabel("* Apasă dublu click pe programarea la care dorești să-i emiți bonul.");
		lblHint.setFont(new Font("Ubuntu", Font.ITALIC , 16));
		lblHint.setBounds(12, 616, 736, 20);
		lblHint.setVisible(true);
		this.add(lblHint);
	}
	
	String denumireServiciu = "";
	private void updateTable(DefaultTableModel viewTableModel) {
		try {
			PreparedStatement ps = (window.dbHandle).getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();
	
			while (rst.next()) {
				if (rst.getInt("status") == 1) {
					Object[] row = new Object[6];
					
					row[0] = rst.getString("id");
					
					Pacient pacient = new Pacient(window.dbHandle, Integer.parseInt(rst.getString("pacient_id")));
					row[1] = pacient.getNume() + " " + pacient.getPrenume();
					
					Competenta comp = new Competenta(window.dbHandle, Integer.parseInt(rst.getString("competenta_id")));
					row[2] = comp.getDenumire();
					
					Policlinica poli = new Policlinica(window.dbHandle, Integer.parseInt(rst.getString("policlinica_id")));
					row[3] = poli.getDenumire();
					
					
					String queryS = "SELECT denumire FROM denumire_servicii where id=" + rst.getString("serviciu_id");
					try {
						PreparedStatement ps1 = window.dbHandle.getConnection().prepareStatement(queryS);
	
						ResultSet rst1 = ps1.executeQuery();
	
						if (rst1.next()) {
							denumireServiciu = rst1.getString("denumire");
						}
					} catch (SQLException ex) {
						ErrorLog.printError("SQLException: " + ex);
					}
					
					//Serviciu serv = new Serviciu (window.dbHandle, Integer.parseInt(rst.getString("serviciu_id")));
					row[4] = denumireServiciu;
					
					DateTime dt = new DateTime(rst.getString("data_programare"));
					row[5] = dt.convertToHumanDateWithoutDay();				
					
					viewTableModel.addRow(row);
				}
				
			}

			rst.close();
		} catch (SQLException ex) {
			ErrorLog.printError("SQLException: " + ex);
		}
	}
	
	void openReceipt (Bon b) {
		JPanel bonPanel = null;

		String tabTitle = "Bon #" + b.getID();
		if (!window.isTabOpened(tabTitle)) {
			bonPanel = new JPanel();
			window.addNewTab(bonPanel, tabTitle);
		} else {
			return;
		}
		
		bonPanel.setLayout(null);
		
		String[] info = new String[12];
		
		try {
			String query = "CALL GET_RECEIPT_INFO('" + b.getID() + "')";
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();
			
			if (rst.next()) {
				info[0] = rst.getString("policlinica_denumire");
				info[1] = rst.getString("policlinica_adresa");
				info[2] = rst.getString("policlinica_telefon");
				info[3] = rst.getString("policlinica_email");
				info[4] = rst.getString("pacient");
				info[5] = rst.getString("medic");
				info[6] = rst.getString("specializare");
				info[7] = rst.getString("serviciu");
				info[8] =  (new DateTime(rst.getString("data_interventie"))).convertToHumanDateWithoutDay();
				info[9] = rst.getString("total") + " LEI";
				info[10] = (new DateTime(rst.getString("data_curenta"))).convertToHumanDateWithoutDay();
				info[11] = rst.getString("id_unic");
			}
		} catch (SQLException ex) {
			ErrorLog.printError("ReciptIssue OpenReceipt SQLException: " + ex);
		}
		
		JLabel lblVizualizareBonFiscal = new JLabel("Vizualizare bon #" + b.getID());
		lblVizualizareBonFiscal.setHorizontalAlignment(SwingConstants.CENTER);
		lblVizualizareBonFiscal.setFont(new Font("Ubuntu", Font.PLAIN, 42));
		lblVizualizareBonFiscal.setBounds(12, 13, 989, 49);
		bonPanel.add(lblVizualizareBonFiscal);
		
		JSeparator separator = new JSeparator();
		separator.setBackground(new Color(70, 130, 180));
		separator.setBounds(62, 75, 877, 2);
		bonPanel.add(separator);
		
		JEditorPane epBon = new JEditorPane();
		epBon.setContentType("text/html");
		epBon.setText(""
			+ "<center>" + info[0] + "</center>\n"
			+ "<center>" + info[1] + "</center>\n\n"
			+ "<center>*********************************************</center>\n\n"
			+ "PACIENT: <div align='right'>" + info[4] + "</div>\n"
			+ "MEDIC: <div align='right'>" + info[5] + "</div>\n"
			+ "SPECIALIZARE: <div align='right'>" + info[6] + "</div>\n"
			+ "SERVICIU MEDICAL: <div align='right'>" + info[7] + "</div>\n"
			+ "DATA INTERVENȚIEI: <div align='right'>" + info[8] + "</div>\n\n"
			+ "TOTAL: <div align='right'>" + info[9] + "</div>\n\n"		
			+ "<center>*********************************************</center>\n\n"
			+ "<center>VĂ MULȚUMIM!</center>\n"
			+ "<center>" + info[0] + "</center>\n"
			+ "<center>" + info[2] + "</center>\n"
			+ "<center>" + info[3] + "</center>\n\n"
			+ "<center>*********************************************</center>\n\n"
			+ "DATA: <div align='right'>" + info[10] + "</div>\n"
			+ "ID UNIC: <div align='right'>" + info[11] + "</div>\n\n\n"
			+ "<center>B O N&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;F I S C A L</center>"
		);
		epBon.setEditable(false);
		epBon.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		epBon.setBounds(300, 82, 348, 520);
		bonPanel.add(epBon);
		
		/*JLabel lblPacient = new JLabel("Pacient:");
		lblPacient.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblPacient.setBounds(112, 129, 140, 28);
		bonPanel.add(lblPacient);
		
		JTextField tfPacient = new JTextField();
		lblPacient.setLabelFor(tfPacient);
		tfPacient.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfPacient.setBounds(112, 170, 280, 28);
		tfPacient.setText(pacient.getNume() + " " + pacient.getPrenume());
		tfPacient.setEditable(false);
		bonPanel.add(tfPacient);
		tfPacient.setColumns(10);
		
		JLabel lblServiciu = new JLabel("Serviciu oferit:");
		lblServiciu.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblServiciu.setBounds(112, 211, 140, 28);
		bonPanel.add(lblServiciu);
		
		JTextField tfServiciu = new JTextField();
		lblServiciu.setLabelFor(tfServiciu);
		tfServiciu.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfServiciu.setColumns(10);
		tfServiciu.setBounds(112, 250, 280, 28);
		tfServiciu.setText(denumireServiciu);
		tfServiciu.setEditable(false);
		bonPanel.add(tfServiciu);
		
		JLabel lblSpecialitate = new JLabel("Specialitate:");
		lblSpecialitate.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblSpecialitate.setBounds(112, 293, 140, 28);
		bonPanel.add(lblSpecialitate);
		
		JTextField tfSpecialitate = new JTextField();
		lblSpecialitate.setLabelFor(tfSpecialitate);
		tfSpecialitate.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfSpecialitate.setColumns(10);
		tfSpecialitate.setBounds(112, 330, 280, 28);
		tfSpecialitate.setText(comp.getDenumire());
		tfSpecialitate.setEditable(false);
		bonPanel.add(tfSpecialitate);
		
		
		

		
		JLabel lblSuma = new JLabel("Suma de achitat:");
		lblSuma.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblSuma.setBounds(112, 373, 140, 28);
		bonPanel.add(lblSuma);
		
		JTextField tfSuma = new JTextField();
		lblSuma.setLabelFor(tfSuma);
		tfSuma.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfSuma.setColumns(10);
		tfSuma.setBounds(112, 410, 280, 28);
		tfSuma.setText("" + b.getSuma());
		tfSuma.setEditable(false);
		bonPanel.add(tfSuma);
		
		
		
		JLabel lblData = new JLabel("Data tiparirii:");
		lblData.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblData.setBounds(613, 129, 140, 28);
		bonPanel.add(lblData);
		
		JTextField tfData = new JTextField();
		lblData.setLabelFor(tfData);
		tfData.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		tfData.setColumns(10);
		tfData.setText((b.getDataTiparire()).convertToDateTime());
		tfData.setEditable(false);
		tfData.setBounds(613, 170, 280, 28);
		bonPanel.add(tfData);*/
		

		
	}
	
	

}
