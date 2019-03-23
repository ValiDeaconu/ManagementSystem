/**
 * Modul pentru modificarea datelor personale ale angajatilor
 */
package application;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import tables.Angajat;
import util.ErrorLog;

import javax.swing.JScrollPane;
import javax.swing.JSeparator;
/**
 * @author Vali
 *
 */

public class PersonalDataPanel extends JPanel {
	private static final long serialVersionUID = 1277890532682497385L;
	
	private JTable resultTable;
	private DefaultTableModel resultTableModel;
	
	String[] functionNames = new String[] { "Receptioner", "Asistent Medical", "Medic", "Contabil", "Inspector resurse umane" };
	
	private Main window;

	private String query = "SELECT * FROM angajati";

	/**
	 * Create the application.
	 */
	public PersonalDataPanel(Main window, String tabTitle) {
		super();
		this.window = window;

		this.setFont(new Font("Ubuntu", Font.PLAIN, 16));
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
		
		JTextField lblSearch = new JTextField();
		lblSearch.setEditable(false);
		lblSearch.setText("C\u0103utare:");
		lblSearch.setFont(new Font("Ubuntu", Font.ITALIC, 26));
		lblSearch.setBounds(62, 82, 120, 26);
		lblSearch.setColumns(10);
		this.add(lblSearch);
		
		JTextField tfSearch = new JTextField();
		tfSearch.setBounds(181, 82, 770, 26);
		tfSearch.setFont(new Font("Ubuntu", Font.PLAIN, 22));
		tfSearch.setColumns(10);
		tfSearch.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				changeString();
			}
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				changeString();
			}
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				changeString();
			}
			
			private void changeString() {
				query = "SELECT * FROM `angajati` " + 
						"WHERE nume LIKE '%" + tfSearch.getText() + "%' " + 
						"OR prenume LIKE '%" + tfSearch.getText() + "%' " +
						"OR email LIKE '%" + tfSearch.getText() + "%' " +
						"OR username LIKE '%" + tfSearch.getText() + "%';";
				updateTable();
			}
		});
		this.add(tfSearch);

		JSeparator secondHeadSeparator = new JSeparator();
		secondHeadSeparator.setForeground(new Color(70, 130, 180));
		secondHeadSeparator.setBounds(62, 121, 889, 2);
		this.add(secondHeadSeparator);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 136, 989, 473);
		this.add(scrollPane);
	
		JComboBox<String> comboAdmin = new JComboBox<String>(new String[] {"Da", "Nu"});
		
		String[] tableHead;
		if (window.userLogged.getAdminType() == 2) {
			tableHead = new String[] {
				"ID", "Nume", "CNP", "Adresa", "Telefon", "Email", "IBAN", "Numar contract", "Data angajarii", "Functie", "Salariu", "Numar ore lunar", 
				"Administrator"
			};
		} else {
			tableHead = new String[] {
				"ID", "Nume", "CNP", "Adresa", "Telefon", "Email", "IBAN", "Numar contract", "Data angajarii", "Functie", "Salariu", "Numar ore lunar"
			};
		}
		
		resultTableModel = new DefaultTableModel (
			new Object[][] { },
			tableHead
		) {
			private static final long serialVersionUID = 1L;
			
			boolean[] columnEditables;
			public boolean isCellEditable(int row, int column) {
				if (window.userLogged.getAdminType() == 2) {
					if (column == 12 && resultTable.getValueAt(row, column).toString().equals("X"))
						return false;
					
					columnEditables = new boolean[] {
						false, false, false, true, true, true, true, false, false, false, true, true, true
					};
				} else {
					columnEditables = new boolean[] {
						false, false, false, true, true, true, true, false, false, false, true, true
					};					
				}
				
				return columnEditables[column];
			}
		};
		resultTable = new JTable();
		resultTable.setModel(resultTableModel);		
		resultTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		resultTable.getColumnModel().getColumn(0).setMinWidth(50);
		resultTable.getColumnModel().getColumn(1).setPreferredWidth(200);
		resultTable.getColumnModel().getColumn(1).setMinWidth(200);
		resultTable.getColumnModel().getColumn(2).setPreferredWidth(140);
		resultTable.getColumnModel().getColumn(2).setMinWidth(140);
		resultTable.getColumnModel().getColumn(3).setPreferredWidth(250);
		resultTable.getColumnModel().getColumn(3).setMinWidth(250);
		resultTable.getColumnModel().getColumn(4).setPreferredWidth(120);
		resultTable.getColumnModel().getColumn(4).setMinWidth(120);
		resultTable.getColumnModel().getColumn(5).setPreferredWidth(250);
		resultTable.getColumnModel().getColumn(5).setMinWidth(250);
		resultTable.getColumnModel().getColumn(6).setPreferredWidth(230);
		resultTable.getColumnModel().getColumn(6).setMinWidth(230);
		resultTable.getColumnModel().getColumn(7).setPreferredWidth(150);
		resultTable.getColumnModel().getColumn(7).setMinWidth(150);
		resultTable.getColumnModel().getColumn(8).setPreferredWidth(150);
		resultTable.getColumnModel().getColumn(8).setMinWidth(200);
		resultTable.getColumnModel().getColumn(9).setPreferredWidth(200);
		resultTable.getColumnModel().getColumn(9).setMinWidth(200);	
		resultTable.getColumnModel().getColumn(10).setPreferredWidth(100);
		resultTable.getColumnModel().getColumn(10).setMinWidth(100);
		resultTable.getColumnModel().getColumn(11).setPreferredWidth(100);
		resultTable.getColumnModel().getColumn(11).setMinWidth(100);		
		if (window.userLogged.getAdminType() == 2) {
			resultTable.getColumnModel().getColumn(12).setPreferredWidth(100);
			resultTable.getColumnModel().getColumn(12).setMinWidth(100);
			resultTable.getColumnModel().getColumn(12).setCellEditor(new DefaultCellEditor(comboAdmin));				
		}			
		resultTable.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		resultTable.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					int row = resultTable.getSelectedRow();
					int column = resultTable.getSelectedColumn();
					
					String newValue = resultTable.getValueAt(row, column).toString();
					int id = Integer.parseInt(resultTable.getValueAt(row, 0).toString());
					Angajat a = new Angajat(window.dbHandle, id);
					
					switch (column) {
						case 3: 
							a.setAdresa(newValue);
							break;
						case 4:
							a.setTelefon(newValue);
							break;
						case 5:
							a.setEmail(newValue);
							break;
						case 6:
							a.setIBAN(newValue);
							break;
						case 9:
							for (int i = 0; i < 5; ++i)
								if (newValue.equals(functionNames[i])) {
									a.setFunctie(i);
									break;
								}
							break;
						case 10:
							a.setSalariu(Integer.parseInt(newValue));
							break;
						case 11:
							a.setNrOre(Integer.parseInt(newValue));
							break;
						case 12:
							if (newValue.equals("Da"))
								a.setAdminType(1);
							else
								a.setAdminType(0);
							break;
						default:
							break;
					}
					
					a.updateOnDatabase(window.dbHandle);
				}
			}
		});
		
		scrollPane.setViewportView(resultTable);
		
		JLabel lblWarning = new JLabel("* Nota: ...");
		lblWarning.setFont(new Font("Ubuntu", Font.ITALIC, 16));
		lblWarning.setBounds(12, 616, 736, 20);
		lblWarning.setVisible(false);
		this.add(lblWarning);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setIcon(new ImageIcon("resources/refresh_icon.png"));
		btnRefresh.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		btnRefresh.setBounds(857, 616, 144, 20);
		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateTable();				
			}
			
		});
		this.add(btnRefresh);

		
		updateTable();	
	}
	
	private void updateTable() {
		resultTableModel.setRowCount(0);
		
		try {
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();
			
			while (rst.next()) {
				String[] r;
				if (window.userLogged.getAdminType() == 2)
					r = new String[13];
				else
					r = new String[12];
				
				r[0] = rst.getString("id");
				r[1] = rst.getString("nume") + " " + rst.getString("prenume");
				r[2] = rst.getString("cnp");
				r[3] = rst.getString("adresa");
				r[4] = rst.getString("telefon");
				r[5] = rst.getString("email");
				r[6] = rst.getString("iban");
				r[7] = rst.getString("nr_contract");
				r[8] = rst.getString("data_angajarii");

				String numeFunctie = new String("");				
				for (int i = 0; i < 5; ++i)
					if (Integer.parseInt(rst.getString("functie")) == i) {
						numeFunctie = functionNames[i];
						break;
					}
				
				r[9] = numeFunctie;
				r[10] = rst.getString("salariu");
				r[11] = rst.getString("nr_ore");
				
				if (window.userLogged.getAdminType() == 2) {
					if (Integer.parseInt(rst.getString("admin_type")) == 1)
						r[12] = new String("Da");
					else if (Integer.parseInt(rst.getString("admin_type")) == 0)
						r[12] = new String("Nu");
					else
						r[12] = new String("X");
				}
					
				resultTableModel.addRow(r);
				
			}

			rst.close();
		} catch (SQLException ex) {
			ErrorLog.printError("PersonalDataPanel SQLException: " + ex);
		}
	}
}
