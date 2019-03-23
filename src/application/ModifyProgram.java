/**
 * 
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

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import design.DTable;
import tables.Angajat;
import tables.Policlinica;
import util.DateTime;
import util.ErrorLog;
import util.Program;
import util.Timetable;


/**
 * @author Ana-Maria
 *
 */
public class ModifyProgram extends JPanel {
	private static final long serialVersionUID = 1L;
	private Main window;
	
	private String query = "SELECT id, nume, prenume, functie from angajati";
	
	public ModifyProgram(Main window) {
		this.setLayout(null);
		this.window = window;
		
		JLabel lblTitle = new JLabel("Modificare program angajati");
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
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 136, 989, 473);
		this.add(scrollPane);
		
		JTable queueTable = new JTable();
		DefaultTableModel queueTableModel = new DefaultTableModel (
			new Object[][] {
				
			},
			new String[] {
				"ID", "Nume", "Functie"
			}
		) {
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] {
					false, false, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		};
		queueTable.setModel(queueTableModel);
		queueTable.getColumnModel().getColumn(0).setResizable(false);
		queueTable.getColumnModel().getColumn(0).setMaxWidth(100);
		queueTable.getColumnModel().getColumn(1).setResizable(false);
		queueTable.getColumnModel().getColumn(2).setResizable(false);
		queueTable.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		
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
				getEmployeeTable(queueTableModel);
			}
		});
		this.add(tfSearch);

		JSeparator secondHeadSeparator = new JSeparator();
		secondHeadSeparator.setForeground(new Color(70, 130, 180));
		secondHeadSeparator.setBounds(62, 121, 889, 2);
		this.add(secondHeadSeparator);

		getEmployeeTable(queueTableModel);
		scrollPane.setViewportView(queueTable);
		
		queueTable.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent mouseEvent) {
		        Point point = mouseEvent.getPoint();
		        int row = queueTable.rowAtPoint(point);
		        if (mouseEvent.getClickCount() == 2 && queueTable.getSelectedRow() != -1) {
	        		int angajatID = Integer.parseInt((String) queueTable.getValueAt(row, 0));
		        	Angajat angajat = new Angajat(window.dbHandle, angajatID);
	        		getScheduleTable(angajat);
		        }
		    }
		});
	}
	
	private void getEmployeeTable(DefaultTableModel queueTableModel) {
		queueTableModel.setRowCount(0);
		
		try {
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();
			
			while (rst.next()) {
				String[] row = new String[3];
				row[0] = rst.getString("id");
				row[1] = rst.getString("nume") + " " + rst.getString("prenume");
				String functie = "" + rst.getString("functie");
				
				switch(Integer.parseInt(functie)) {
					case 0: row[2] = "Receptioner";
							break;
					case 1: row[2] = "Asistent Medical";
							break;
					case 2: row[2] = "Medic";
							break;
					case 3: row[2] = "Contabil";
							break;
					case 4: row[2] = "Inspector resurse umane";
							break;
					default: row[2] = "Neidentificat";
							break;
				}
				
				queueTableModel.addRow(row);
			}

			ps.close();
			rst.close();
		} catch (SQLException ex) {
			ErrorLog.printError("ModifyProgram GetEmployeeTable SQLException: " + ex);
		}
	}
	
	@SuppressWarnings("serial")
	public void getScheduleTable(Angajat angajat) {
		String tabTitle = "Orar angajat " + angajat.getNume() + " " + angajat.getPrenume();
		DTable takePanel;
		if (!window.isTabOpened(tabTitle)) {
			takePanel = new DTable(window, tabTitle) {
				@Override
				public void tableInsert() {
					getComboPanel(angajat, null, false);
				}
			};
			window.addNewTab(takePanel, tabTitle);
		} else {
			return;
		}
		
		takePanel.setRemoveButtonVisibile(false);
		takePanel.setWarning("Apasă dublu click pe orarul pe care dorești să-l modifici.");	
		
		String queryMedic = "select policlinici.denumire, program_angajati.program, policlinici.id"
							+ " from policlinici"
					        + " inner join program_angajati on policlinici.id = program_angajati.policlinica_id"
					        + " where program_angajati.angajat_id = '" +angajat.getID() + "';" ;
		
		JTable queueTable1 = takePanel.getTable();
		DefaultTableModel queueTableModel1 = new DefaultTableModel (
			new Object[][] { },
			new String[] {
				"Policlinică", "Program"
			}
		) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		queueTable1.setModel(queueTableModel1);
		queueTable1.getColumnModel().getColumn(0).setResizable(false);
		queueTable1.getColumnModel().getColumn(1).setResizable(false);


		queueTable1.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		
		try {
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(queryMedic);
			ResultSet rst = ps.executeQuery();
			
			while (rst.next()) {
				Object[] row = new Object[2];
				row[0] = rst.getString("denumire");
				row[1] = rst.getString("program");
				queueTableModel1.addRow(row);
			}
			
			ps.close();
			rst.close();
		} catch (SQLException ex) {
			ErrorLog.printError("ModifyProgram GetScheduleTable SQLException: " + ex);
		}

		takePanel.setAddButtonVisibile(true);
		if (queueTableModel1.getRowCount() > 0 && angajat.getFunctie() != 2) {
			takePanel.setAddButtonVisibile(false);
		}
		
		queueTable1.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent mouseEvent) {
		    	Point point = mouseEvent.getPoint();
		    	int row = queueTable1.rowAtPoint(point);
		        if (mouseEvent.getClickCount() == 2 && queueTable1.getSelectedRow() != -1) {
		        	String query = "SELECT id FROM policlinici WHERE denumire = '" + queueTable1.getValueAt(row, 0) + "';";
		        	try {
		        		PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
		        		ResultSet rst = ps.executeQuery();
		        		
		        		if (rst.next()) {
		        			Policlinica poli = new Policlinica(window.dbHandle, rst.getInt("id"));
				        	getComboPanel(angajat, poli, true);
		        		} else {
		        			JOptionPane.showMessageDialog(window.mainFrame, "A apărut o eroare. Vă rugăm reîncercați.", "Eroare", JOptionPane.ERROR_MESSAGE);
		        		}
		        		
		        		ps.close();
		        		rst.close();
		        	} catch (SQLException ex) {
		        		ErrorLog.printError("ModifyProgram GetPoliID SQLException: " + ex);
		        	}
		        	
		        }
		    }
		});
	}
	
	private JComboBox<String> cbPoli;
	public void getComboPanel(Angajat angajat, Policlinica poli, boolean isUpdate) {
		JPanel takePanel;
		String tabTitle = "Program [Unknown]";
		if (poli != null)
			tabTitle = "Program [" + angajat.getID() + "@" + poli.getID() + "]";
		else
			tabTitle = "Program [" + angajat.getID() + "]";
		if (!window.isTabOpened(tabTitle)) {
			takePanel = new JPanel();
			window.addNewTab(takePanel, tabTitle);
		} else {
			return;
		}
		
		takePanel.setLayout(null);
		
		if (isUpdate) {
			String title = "Programul angajatului [Unknown]";
			if (poli != null)
				title = "Programul angajatului " + angajat.getNume() + " " + angajat.getPrenume() + " la policlinica " + poli.getDenumire();
			else
				title = "Programul angajatului " + angajat.getNume() + " " + angajat.getPrenume();
			
			JLabel lblTitle = new JLabel(title);
			lblTitle.setFont(new Font("Ubuntu", Font.PLAIN, 24));
			lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
			lblTitle.setBounds(12, 5, 989, 49);
			takePanel.add(lblTitle);
			
			JSeparator headSeparator = new JSeparator();
			headSeparator.setForeground(new Color(70, 130, 180));
			headSeparator.setBounds(62, 67, 889, 2);
			takePanel.add(headSeparator);
			
			Timetable t = new Timetable(takePanel, true);
			
			String query = "SELECT program FROM program_angajati WHERE angajat_id = '" + angajat.getID() + "' and policlinica_id = '" + poli.getID() + "' LIMIT 1";
			try {
				PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
				ResultSet rst = ps.executeQuery();
				
				if (rst.next()) {
					t.setVisible(true);
					Program p = new Program(rst.getString("program"));
					
					for (int i = 0; i < 7; ++i) {
						t.setContent(i, 0, p.get(i).getInceput());
						t.setContent(i, 1, p.get(i).getSfarsit());
					}
				} else {
					t.setVisible(false);
				}
				
				ps.close();
				rst.close();
			} catch (SQLException ex) {
				ErrorLog.printError("ModifyProgram GetComboPanel SQLException: " + ex);
			}
			
			JLabel lblWarning = new JLabel("* Pentru a seta o zi ca fiind liberă, setează ora de început și ora de final la 0.");
			lblWarning.setFont(new Font("Ubuntu", Font.ITALIC, 16));
			lblWarning.setBounds(12, 616, 736, 20);
			lblWarning.setVisible(true);
			takePanel.add(lblWarning);
			
			JButton btnSave = new JButton("Salvează");
			btnSave.setIcon(new ImageIcon("resources/save_icon.png"));
			btnSave.setFont(new Font("Ubuntu", Font.PLAIN, 14));
			btnSave.setBounds(857, 616, 144, 20);
			btnSave.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Program input = t.getContentAsProgram();
					
					boolean programAlright = true;
					int dayWithTrouble = 0;
					for (int i = 0; i < 7; ++i) {
						if (input.get(i).getInceput() > input.get(i).getSfarsit()) {
							programAlright = false;
							dayWithTrouble = i;
							break;
						}
					}
					
					if (programAlright) {
						if (input != null) {
							String query = "UPDATE program_angajati "
									+ "SET program = '" + input.compressProgram() + "' "
									+ "WHERE angajat_id = '" + angajat.getID() + "' "
									+ "AND policlinica_id = '" + poli.getID() + "'";
							try {
								PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
								
								
								if (ps.executeUpdate() == 1) {
									JOptionPane.showMessageDialog(window.mainFrame, "Programul a fost modificat!", "Succes", JOptionPane.INFORMATION_MESSAGE);
								} else {
									JOptionPane.showMessageDialog(window.mainFrame, "A apărut o eroare. Vă rugăm reîncercați!", "Eroare", JOptionPane.ERROR_MESSAGE);								
								}
								
								ps.close();
							} catch (SQLException ex) {
								ErrorLog.printError("ModifyProgram UpdateProgram SQLException: " + ex);
							}
						}
					} else {
						JOptionPane.showMessageDialog(window.mainFrame, "Programul de " + DateTime.getDayByIndex(dayWithTrouble) + " este greșit.", "Eroare", JOptionPane.ERROR_MESSAGE);								
					}
				}
				
			});
			takePanel.add(btnSave);
		} else {
			JLabel lblTitle = new JLabel("Adauga program pentru angajatul " + angajat.getNume() + " " + angajat.getPrenume());
			lblTitle.setFont(new Font("Ubuntu", Font.PLAIN, 24));
			lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
			lblTitle.setBounds(12, 5, 989, 49);
			takePanel.add(lblTitle);
			
			JSeparator headSeparator = new JSeparator();
			headSeparator.setForeground(new Color(70, 130, 180));
			headSeparator.setBounds(62, 67, 889, 2);
			takePanel.add(headSeparator);
			
			JLabel lblPoli = new JLabel("Policlinică: ");
			lblPoli.setFont(new Font("Ubuntu", Font.PLAIN, 18));
			lblPoli.setHorizontalAlignment(SwingConstants.CENTER);
			lblPoli.setBounds(300, 98, 214, 38);
			takePanel.add(lblPoli);
			
			String[] poliNames = null;
			try {
				String localQuery = "SELECT denumire FROM policlinici";
				PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(localQuery);
				ResultSet rst = ps.executeQuery();
				
				int count = 0;
				while (rst.next()) {
					++count;
				}
				rst.beforeFirst();
				poliNames = new String[count];
				count = 0;
				while (rst.next()) {
					poliNames[count++] = rst.getString("denumire");
				}
				
				ps.close();
				rst.close();
			} catch (SQLException ex) {
				ErrorLog.printError("ModifyProgram DownloadCentersName SQLException: " + ex);
			}
			
			cbPoli = new JComboBox<String>();
			cbPoli.setEditable(true);
			cbPoli.setModel(new DefaultComboBoxModel<String>(poliNames));
			cbPoli.setFont(new Font("Ubuntu", Font.PLAIN, 18));
			cbPoli.setBounds(500, 98, 292, 35);
			cbPoli.setSelectedIndex(-1);
			takePanel.add(cbPoli);
			
			AutoCompleteDecorator.decorate(cbPoli);
			
			Timetable t = new Timetable(takePanel, true);
			for (int i = 0; i < 7; ++i) {
				t.setContent(i, 0, 0);
				t.setContent(i, 1, 0);
			}
			t.setVisible(true);
			
			JLabel lblWarning = new JLabel("* Pentru a seta o zi ca fiind liberă, setează ora de început și ora de final la 0.");
			lblWarning.setFont(new Font("Ubuntu", Font.ITALIC, 16));
			lblWarning.setBounds(12, 616, 736, 20);
			lblWarning.setVisible(true);
			takePanel.add(lblWarning);
			
			JButton btnSave = new JButton("Salvează");
			btnSave.setIcon(new ImageIcon("resources/save_icon.png"));
			btnSave.setFont(new Font("Ubuntu", Font.PLAIN, 14));
			btnSave.setBounds(857, 616, 144, 20);
			btnSave.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Program input = t.getContentAsProgram();

					if (input != null) {
						int selectedPoliID = 0;
						try {
							String szQuery = "SELECT id FROM policlinici WHERE denumire = '" + ((String) cbPoli.getSelectedItem()) + "';";
							System.out.println(szQuery);
							PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(szQuery);
							ResultSet rst = ps.executeQuery();
							
							if (rst.next()) {
								selectedPoliID = rst.getInt("id");
							} else {
								selectedPoliID = -1;
							}
							
							ps.close();
							rst.close();						
						} catch (SQLException ex) {
							ErrorLog.printError("ModifyProgram GetCenterSelected SQLException: " + ex);						
						}
						
						System.out.println(selectedPoliID);
						
						if (selectedPoliID > 0) {
							boolean programAlright = true;
							int dayWithTrouble = 0;
							for (int i = 0; i < 7; ++i) {
								if (input.get(i).getInceput() > input.get(i).getSfarsit()) {
									programAlright = false;
									dayWithTrouble = i;
									break;
								}
							}
							
							if (programAlright) {	
								String query = "INSERT INTO program_angajati (policlinica_id, angajat_id, program) VALUES ("
										+ "'" + selectedPoliID + "', "
										+ "'" + angajat.getID() + "', "
										+ "'" + input.compressProgram() + "');";
								System.out.println(query);
								try {
									PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
									
									
									if (ps.executeUpdate() == 1) {
										JOptionPane.showMessageDialog(window.mainFrame, "Programul a fost adăugat!", "Succes", JOptionPane.INFORMATION_MESSAGE);
									} else {
										JOptionPane.showMessageDialog(window.mainFrame, "A apărut o eroare. Vă rugăm reîncercați!", "Eroare", JOptionPane.ERROR_MESSAGE);								
									}
									
									ps.close();
								} catch (SQLException ex) {
									ErrorLog.printError("ModifyProgram InsertProgram SQLException: " + ex);
								}
							} else {
								JOptionPane.showMessageDialog(window.mainFrame, "Programul de " + DateTime.getDayByIndex(dayWithTrouble) + " este greșit.", "Eroare", JOptionPane.ERROR_MESSAGE);								
							}
						} else {
							JOptionPane.showMessageDialog(window.mainFrame, "A apărut o eroare. Vă rugăm reîncercați!", "Eroare", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				
			});
			takePanel.add(btnSave);
		}
	}
}
