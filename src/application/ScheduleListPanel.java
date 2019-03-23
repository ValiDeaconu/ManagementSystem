/**
 * Modul pentru lista de programari
 */
package application;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

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
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import tables.Angajat;
import tables.Pacient;
import tables.Policlinica;
import tables.ProgramAngajat;
import tables.Programare;
import util.DateTime;
import util.ErrorLog;

/**
 * @author Vali
 *
 */
public class ScheduleListPanel extends JPanel {
	private static final long serialVersionUID = 1804078311159194986L;

	private Main window;

	private int selectIndex;
	
	private boolean isInVacation;
	
	public ScheduleListPanel(Main window) {
		this.setLayout(null);
		this.window = window;
		this.selectIndex = 0;
		
		initialize();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		this.removeAll();
		this.revalidate();
		this.repaint();
		
		JLabel lblTitle = new JLabel("List\u0103 program\u0103ri ");
		lblTitle.setFont(new Font("Ubuntu", Font.PLAIN, 42));
		lblTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTitle.setBounds(12, 5, 538, 49);
		this.add(lblTitle);
		
		JComboBox cbTitle = new JComboBox();
		cbTitle.setModel(new DefaultComboBoxModel(new String[] {"\u00EEn a\u0219teptare", "proprii"}));
		cbTitle.setFont(new Font("Ubuntu", Font.PLAIN, 42));
		cbTitle.setBounds(550, 5, 272, 49);
		cbTitle.setSelectedIndex(selectIndex);
		cbTitle.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				selectIndex = cbTitle.getSelectedIndex();
				initialize();
			}			
		});
		this.add(cbTitle);
		
		if (window.userLogged.getFunctie() != 2) {
			cbTitle.setVisible(false);
			cbTitle.setSelectedIndex(0);
		}
		
		JSeparator headSeparator = new JSeparator();
		headSeparator.setForeground(new Color(70, 130, 180));
		headSeparator.setBounds(62, 67, 889, 2);
		this.add(headSeparator);
		
		if (cbTitle.getSelectedIndex() == 0 || window.userLogged.getFunctie() != 2) {
			queuePanel();
		} else {
			ownSessionPanel();
		}
	}
	
	private void queuePanel() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 83, 989, 526);
		this.add(scrollPane);
		
		JTable queueTable = new JTable();
		DefaultTableModel queueTableModel = new DefaultTableModel (
			new Object[][] {
				
			},
			new String[] {
				"ID", "Pacient", "Specializare", "Serviciu", "Policlinica"
			}
		) {
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] {
					false, false, false, false, false
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
		queueTable.getColumnModel().getColumn(3).setResizable(false);
		queueTable.getColumnModel().getColumn(4).setResizable(false);
		queueTable.getColumnModel().getColumn(4).setMinWidth(200);
		queueTable.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		
		updateSessionQueueTable(queueTableModel);
		
		isInVacation = false;
		try {
			String query = "SELECT * FROM concedii WHERE (NOW() BETWEEN data_inceput AND data_sfarsit) AND angajat_id = '" + window.userLogged.getID() + "' LIMIT 1";
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();
			
			if (rst.next()) {
				isInVacation = true;
			}
		} catch (SQLException e) {
			ErrorLog.printError("ScheduleListPanel checkVacation SQLException " + e);
			isInVacation = false;
		}
		
		if (window.userLogged.getFunctie() == 2) {
			queueTable.addMouseListener(new MouseAdapter() {
			    public void mousePressed(MouseEvent mouseEvent) {
			        Point point = mouseEvent.getPoint();
			        int row = queueTable.rowAtPoint(point);
			        if (mouseEvent.getClickCount() == 2 && queueTable.getSelectedRow() != -1) {
			        	if (!isInVacation) {
				        	int programareID = Integer.parseInt((String) queueTable.getValueAt(row, 0));
				        	Programare p = new Programare(window.dbHandle, programareID);
				        	int confirm = JOptionPane.showConfirmDialog(window.mainFrame, "Doresti sa preiei programarea ID " + p.getID() + "?", "Confirmare", JOptionPane.YES_NO_OPTION);		        	
				        	if (confirm == JOptionPane.YES_OPTION) {
				        		preiaProgramarea(p, window.userLogged);
				        	}
			        	} else {
			        		JOptionPane.showMessageDialog(window.mainFrame, "Nu poți prelua programări când ești în concediu.", "Eroare", JOptionPane.ERROR_MESSAGE);
			        	}
			        }
			    }
			});
		} else if (window.userLogged.getFunctie() == 0) {
			queueTable.addMouseListener(new MouseAdapter() {
			    public void mousePressed(MouseEvent mouseEvent) {
			        Point point = mouseEvent.getPoint();
			        int row = queueTable.rowAtPoint(point);
			        if (mouseEvent.getClickCount() == 2 && queueTable.getSelectedRow() != -1) {
			        	String str = (String)queueTable.getValueAt(row, 0);
			        	Integer programareID = Integer.parseInt(str);
			        	Programare p = new Programare(window.dbHandle, programareID);
			        	int option = JOptionPane.showConfirmDialog(window.mainFrame, "Dorești să ștergi această programare?\nAtenție! Această comandă nu poate fi revocată!", "Confirmare", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			        	if (option == JOptionPane.YES_OPTION) {
				        	String query = "DELETE FROM `programari` WHERE id = '" + p.getID() + "'";
							if (window.dbHandle.doUpdate(query) == 1) {
								JOptionPane.showMessageDialog(window.mainFrame, "Programarea a fost stearsa cu succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(window.mainFrame, "Stergere esuata.", "Eroare", JOptionPane.ERROR_MESSAGE);
								ErrorLog.printError("Problema la incercarea stergerii raportului: " + p.toString());
							}
							updateSessionQueueTable(queueTableModel);
			        	}
			        }
			    }
			});
		}
		
		scrollPane.setViewportView(queueTable);

		JLabel lblWarning = new JLabel();
		if (window.userLogged.getFunctie() == 2) {
			lblWarning.setText("* Notă: Apasă dublu click pe programarea pe care dorești să o preiei.");
		} else if (window.userLogged.getFunctie() == 0) {
			lblWarning.setText("* Notă: Apasă dublu click pe programarea pe care dorești să o ștergi.");
		}
		lblWarning.setFont(new Font("Ubuntu", Font.ITALIC, 16));
		lblWarning.setBounds(12, 616, 736, 20);
		this.add(lblWarning);
	
		if (window.userLogged.getFunctie() == 0) {
			JButton btnInsertRow = new JButton("Adaugă rând");
			btnInsertRow.setIcon(new ImageIcon("resources/add_icon.png"));
			btnInsertRow.setFont(new Font("Ubuntu", Font.PLAIN, 14));
			btnInsertRow.setBounds(683, 616, 144, 20);
			btnInsertRow.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String tabTitle = "Adaugă programare";
					if (!window.isTabOpened(tabTitle)) {
						window.addNewTab(new AddAppointment(window, tabTitle), tabTitle);
					}
				}
				
			});
			this.add(btnInsertRow);
		}
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setIcon(new ImageIcon("resources/refresh_icon.png"));
		btnRefresh.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		btnRefresh.setBounds(857, 616, 144, 20);
		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateSessionQueueTable(queueTableModel);
			}
			
		});
		this.add(btnRefresh);
	}
	
	private void updateSessionQueueTable(DefaultTableModel queueTableModel) {
		queueTableModel.setRowCount(0);
		
		String query = new String();
		if (window.userLogged.getFunctie() == 2) 
			query = "CALL GET_WAITING_SCHEDULE_LIST('" + window.userLogged.getID() + "', '" + window.centerUsed.getID() + "')";
		else
			query = "CALL GET_SCHEDULE_LIST('" + window.centerUsed.getID() + "')";
		
		try {
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();
			
			while (rst.next()) {
				String[] row = new String[5];
				row[0] = rst.getString("id");
				row[1] = rst.getString("pacient");
				row[2] = rst.getString("specializare");
				row[3] = rst.getString("denumire_serviciu");
				row[4] = rst.getString("policlinica");
				
				queueTableModel.addRow(row);
			}

			ps.close();
			rst.close();
		} catch (SQLException ex) {
			ErrorLog.printError("ViewWaitingScheduleList SQLException: " + ex);
		}
	}
	
	private void ownSessionPanel() {
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 83, 989, 526);
		this.add(scrollPane_1);
		
		JTable viewTable = new JTable();
		DefaultTableModel viewTableModel = new DefaultTableModel(
			new Object[][] { },
			new String[] {
				"ID", "Pacient", "Specializare", "Serviciu", "Policlinica", "Data programarii"
			}
		) {
			private static final long serialVersionUID = 1L;
			
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		viewTable.setModel(viewTableModel);
		viewTable.getColumnModel().getColumn(0).setResizable(false);
		viewTable.getColumnModel().getColumn(0).setMaxWidth(100);
		viewTable.getColumnModel().getColumn(1).setResizable(false);
		viewTable.getColumnModel().getColumn(2).setResizable(false);
		viewTable.getColumnModel().getColumn(3).setResizable(false);
		viewTable.getColumnModel().getColumn(4).setResizable(false);
		viewTable.getColumnModel().getColumn(4).setMinWidth(200);
		viewTable.getColumnModel().getColumn(5).setResizable(false);
		viewTable.getColumnModel().getColumn(5).setMinWidth(200);
		
		updateOwnSessionTable(viewTableModel);
		
		viewTable.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent mouseEvent) {
		        Point point = mouseEvent.getPoint();
		        int row = viewTable.rowAtPoint(point);
		        if (mouseEvent.getClickCount() == 2 && viewTable.getSelectedRow() != -1) {
		        	int programareID = Integer.parseInt((String) viewTable.getValueAt(row, 0));
		        	Pacient p = null;
		        	try {
	        			String query = "SELECT pacient_id FROM programari WHERE id = '" + programareID + "'";
	        			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
	        			ResultSet rst = ps.executeQuery();
	        			
	        			if (rst.next()) {
	        				p = new Pacient(window.dbHandle, rst.getInt("pacient_id"));
	        			}
	        			
	        			ps.close();
	        			rst.close();
	        		} catch (SQLException ex) {
	        			ErrorLog.printError("ScheduleListPanel ViewPatientHistory FindPatient SQLException: " + ex);
	        		}
		        	
		        	if (p != null) {
			        	int confirm = JOptionPane.showConfirmDialog(window.mainFrame, "Dorești să vizualizezi istoricul pentru pacientul " + p.getNume() + " " + p.getPrenume() + "?", "Confirmare", JOptionPane.YES_NO_OPTION);		        	
			        	if (confirm == JOptionPane.YES_OPTION) {
			        		showHistory(p);
			        	}
		        	} else {
		        		JOptionPane.showMessageDialog(window.mainFrame, "Nu s-a putut identifica pacientul căutat, vă rugăm reîncercați!", "Eroare", JOptionPane.ERROR_MESSAGE);
		        	}
		        }
		    }
		});
		
		scrollPane_1.setViewportView(viewTable);
		viewTable.setFont(new Font("Ubuntu", Font.PLAIN, 16));

		JLabel lblWarning = new JLabel();
		lblWarning.setText("* Apasă dublu click pe o programare pentru a vizualiza istoricul pacientului.");
		lblWarning.setFont(new Font("Ubuntu", Font.ITALIC, 16));
		lblWarning.setBounds(12, 616, 736, 20);
		this.add(lblWarning);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setIcon(new ImageIcon("resources/refresh_icon.png"));
		btnRefresh.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		btnRefresh.setBounds(857, 616, 144, 20);
		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateOwnSessionTable(viewTableModel);
			}
			
		});
		this.add(btnRefresh);
	}
	
	private void updateOwnSessionTable(DefaultTableModel viewTableModel) {
		viewTableModel.setRowCount(0);
		
		String query = "CALL GET_OWN_SCHEDULE_LIST('" + window.userLogged.getID() + "')";
		
		try {
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();
			
			while (rst.next()) {
				String[] row = new String[6];
				row[0] = rst.getString("id");
				row[1] = rst.getString("pacient");
				row[2] = rst.getString("specializare");
				row[3] = rst.getString("denumire_serviciu");
				row[4] = rst.getString("policlinica");
				DateTime dt = new DateTime(rst.getString("data_programare"));
				row[5] = dt.convertToHumanDate();
				
				viewTableModel.addRow(row);
			}

			ps.close();
			rst.close();
		} catch (SQLException ex) {
			ErrorLog.printError("ViewOwnScheduleList SQLException: " + ex);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void preiaProgramarea(Programare p, Angajat a) {
		JPanel takePanel;
		String tabTitle = "Preluare programare ID: " + p.getID();
		if (!window.isTabOpened(tabTitle)) {
			takePanel = new JPanel();
			window.addNewTab(takePanel, tabTitle);
		} else {
			return;
		}
		
		takePanel.setLayout(null);
		
		JLabel lblTitle_2 = new JLabel("Preluare programare ID: " + p.getID());
		lblTitle_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle_2.setFont(new Font("Ubuntu", Font.PLAIN, 42));
		lblTitle_2.setBounds(12, 13, 989, 49);
		takePanel.add(lblTitle_2);

		JSeparator headSeparator_2 = new JSeparator();
		headSeparator_2.setForeground(new Color(70, 130, 180));
		headSeparator_2.setBounds(62, 67, 889, 2);
		takePanel.add(headSeparator_2);
		
		String[] stringModelOra = new String[24];
		for (int i = 0; i < 24; ++i) {
			if (i < 10)
				stringModelOra[i] = new String("0" + i);
			else
				stringModelOra[i] = new String(i + "");
		}
		
		String[] stringModelMinute = new String[60];
		for (int i = 0; i < 60; ++i) {
			if (i < 10)
				stringModelMinute[i] = new String("0" + i);
			else
				stringModelMinute[i] = new String(i + "");
		}

		JLabel lblData = new JLabel("Data");
		lblData.setHorizontalAlignment(SwingConstants.RIGHT);
		lblData.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblData.setBounds(227, 206, 247, 25);
		takePanel.add(lblData);
		
		JDateChooser dcData = new JDateChooser();
		dcData.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		dcData.setLocale(Locale.forLanguageTag("ro-RO"));
		dcData.setDateFormatString("MMMM dd, yyyy");
		dcData.setCalendar(null);
		dcData.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		dcData.setBounds(532, 206, 247, 25);
		takePanel.add(dcData);
		
		JLabel lblOra = new JLabel("Ora");
		lblOra.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOra.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblOra.setBounds(227, 244, 247, 25);
		takePanel.add(lblOra);
		
		JComboBox cbOra = new JComboBox();
		cbOra.setModel(new DefaultComboBoxModel(stringModelOra));
		cbOra.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		cbOra.setBounds(532, 244, 247, 25);
		takePanel.add(cbOra);
		
		JLabel lblMinute = new JLabel("Minute");
		lblMinute.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMinute.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblMinute.setBounds(227, 282, 247, 25);
		takePanel.add(lblMinute);
		
		JComboBox cbMinute = new JComboBox();
		cbMinute.setModel(new DefaultComboBoxModel(stringModelMinute));
		cbMinute.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		cbMinute.setBounds(532, 282, 247, 25);
		takePanel.add(cbMinute);
		
		JLabel lblNote = new JLabel("Introdu data in care poti onora programarea.");
		lblNote.setHorizontalAlignment(SwingConstants.CENTER);
		lblNote.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		lblNote.setBounds(12, 600, 530, 25);
		takePanel.add(lblNote);
		
		JButton btnSend = new JButton("Submit");
		btnSend.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		btnSend.setBounds(850, 600, 120, 25);
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {			
				Policlinica poli = new Policlinica(window.dbHandle, p.getPoliclinicaID());
			
				DateTime dt = new DateTime(dcData.getCalendar());
				dt.setOra(Integer.parseInt(stringModelOra[cbOra.getSelectedIndex()]));
				dt.setMinute(Integer.parseInt(stringModelMinute[cbMinute.getSelectedIndex()]));
				dt.setSecunde(0);
				
				int[][] orar = ProgramAngajat.getOrar(window.dbHandle, window.userLogged, poli, dt);
				if (orar == null) {
					JOptionPane.showMessageDialog(null, "Nu lucrezi la policlinica " + poli.getDenumire() + " la data introdusa.", "Eroare", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				int dayIndex = dt.getDayIndex();
				int minutProgramare = Integer.parseInt(dt.getOra()) * 60 + Integer.parseInt(dt.getMinute());
				int durata = 0;
				
				try {
					String query = "SELECT durata FROM servicii WHERE denumire_id = '" + p.getServiciuID() + "' "
							+ "AND medic_id = '" + window.userLogged.getID() + "' "
							+ "AND competenta_id = '" + p.getCompetentaID() + "';";
					
					PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
					ResultSet rst = ps.executeQuery();
					
					if (rst.next()) {
						durata = Integer.parseInt(rst.getString("durata"));
					} else {
						JOptionPane.showMessageDialog(null, "Nu ți-ai definit un preț și o durată pentru acest tip de serviciu.", "Eroare", JOptionPane.ERROR_MESSAGE);
						return;
					}
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Nu s-a putut efectua preluarea din motive tehnice, vă rugăm reîncercați.", "Eroare", JOptionPane.ERROR_MESSAGE);
					ErrorLog.printError("ScheduleListPanel SQLException: " + e);
					return;
				}
				
				for (int minut = minutProgramare; minut <= minutProgramare + durata; ++minut) {
					if (orar[dayIndex][minut] == 0) {
						JOptionPane.showMessageDialog(null, "Nu lucrati la ora selectata.", "Eroare", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					if (orar[dayIndex][minut] == 2) {
						JOptionPane.showMessageDialog(null, "Aveti orarul incarcat pentru ora selectata.", "Eroare", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				
				// medicul este disponibil (e la munca si nu are programari), deci poate prelua programarea
				p.setMedicID(a.getID());
				p.setStatus(1);
				p.setDataProgramare(dt);
				p.updateOnDatabase(window.dbHandle);
				
				JOptionPane.showMessageDialog(null, "Programarea a fost preluata!", "Succes", JOptionPane.INFORMATION_MESSAGE);
			}			
		});
		takePanel.add(btnSend);
		
		JButton btnReset = new JButton("Reset");
		btnReset.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		btnReset.setBounds(718, 600, 120, 25);
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dcData.setCalendar(null);
				cbOra.setSelectedIndex(0);
				cbMinute.setSelectedIndex(0);
			}
			
		});
		takePanel.add(btnReset);
	}
	
	private void showHistory(Pacient p) {
		JPanel panel = new JPanel();
		String tabTitle = "Istoric " + p.getNume() + " " + p.getPrenume();
		if (!window.isTabOpened(tabTitle)) {
			panel = new JPanel();
			window.addNewTab(panel, tabTitle);
		} else {
			return;
		}
		panel.setLayout(null);
	
		JLabel lblTitle = new JLabel(tabTitle);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Ubuntu", Font.PLAIN, 42));
		lblTitle.setBounds(12, 13, 989, 49);
		panel.add(lblTitle);
		
		JSeparator separator = new JSeparator();
		separator.setBackground(new Color(70, 130, 180));
		separator.setBounds(62, 75, 877, 2);
		panel.add(separator);
		
		JTextArea taHistory = new JTextArea();
		taHistory.setLineWrap(true);
		taHistory.setWrapStyleWord(true);
		taHistory.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		taHistory.setBounds(62, 90, 877, 546);
		taHistory.setEditable(false);
		taHistory.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(70, 130, 180)));
		panel.add(taHistory);
		
		String reportHistory = new String("");
		try {
			String query = "SELECT "
					+ "rapoarte.id AS 'id', "
					+ "rapoarte.istoric AS 'istoric', "
					+ "CONCAT(angajati.nume, ' ', angajati.prenume) AS 'medic', "
					+ "rapoarte.data_raport AS 'data_raport' "
					+ "FROM rapoarte "
					+ "INNER JOIN angajati ON rapoarte.medic_id = angajati.id "
					+ "WHERE pacient_id = '" + p.getID() + "'";
			
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();
			
			if (rst.next()) {
				rst.beforeFirst();
				while (rst.next()) {
					reportHistory += "Raport Index: " + rst.getInt("id") + "\n";
					reportHistory += "Medic: " + rst.getString("medic") + "\n";
					reportHistory += "Data: " + (new DateTime(rst.getString("data_raport"))).convertToHumanDate() + "\n";
					reportHistory += "Istoric: " + rst.getString("istoric") + "\n\n";
				}
			} else {
				reportHistory = "Nu există informații disponibile despre pacinetul " + p.getNume() + " " + p.getPrenume() + ".\n";
			}
			
			ps.close();
			rst.close();
		} catch(SQLException ex) {
			ErrorLog.printError("ScheduleListPanel ShowHistory SQLException: " + ex);
			reportHistory = "Nu există informații disponibile despre pacinetul " + p.getNume() + " " + p.getPrenume() + ".\n";
			JOptionPane.showMessageDialog(window.mainFrame, "Nu s-au putut extrage informații despre acest pacient.", "Eroare", JOptionPane.ERROR_MESSAGE);
		}
		
		taHistory.setText(reportHistory);
	}
}
