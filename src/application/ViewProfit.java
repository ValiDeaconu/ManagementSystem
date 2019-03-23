/**
 * 
 */
package application;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import tables.Medic;
import tables.Policlinica;
import util.DateTime;
import util.ErrorLog;

/**
 * @author Ana-Maria
 * @author Vali
 *
 */
public class ViewProfit extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Main window;

	private JComboBox<String> cbBy;
	private JComboBox<String> cbLuna;
	
	private JComboBox<String> cbMedic;
	private JComboBox<String> cbModul;
	
	private final String[] monthNames = { 
		"", "Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie", "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie", "Decembrie"
	};
	
	private JTable table;
	
	private JLabel lblTotal;
	
	private JScrollPane scrollPane;
	
	public ViewProfit(Main window) {
		this.setLayout(null);
		this.window = window;

		JLabel lblTitle = new JLabel("Contabilitate");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Ubuntu", Font.PLAIN, 42));
		lblTitle.setBounds(12, 13, 989, 49);
		this.add(lblTitle);
		
		JSeparator headSeparator = new JSeparator();
		headSeparator.setBackground(new Color(70, 130, 180));
		headSeparator.setBounds(62, 75, 877, 2);
		this.add(headSeparator);
		
		JSeparator bodySeparator = new JSeparator();
		bodySeparator.setBackground(new Color(70, 130, 180));
		bodySeparator.setBounds(62, 138, 877, 2);
		this.add(bodySeparator);
		
		lblTotal = new JLabel("TOTAL: XXXXXXX");
		lblTotal.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotal.setFont(new Font("Ubuntu", Font.BOLD, 18));
		lblTotal.setBounds(626, 615, 284, 25);
		lblTotal.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
		this.add(lblTotal);	
		
		if (window.userLogged.getFunctie() == 3) {
			// daca e contabil
			initialize();
		} else if (window.userLogged.getFunctie() == 2) {
			// daca e medic
			JLabel lblBy = new JLabel("Afișază profitul după");
			lblBy.setFont(new Font("Ubuntu", Font.PLAIN, 18));
			lblBy.setBounds(225, 90, 169, 35);
			this.add(lblBy);
			
			cbModul = new JComboBox<String>();
			cbModul.setFont(new Font("Ubuntu", Font.PLAIN, 18));
			cbModul.setModel(new DefaultComboBoxModel<String>(new String[] {"policlinică", "specializare"}));
			cbModul.setBounds(406, 90, 141, 35);
			cbModul.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					updateMedicInterface();
				}
				
			});
			this.add(cbModul);
			
			JLabel lblLuna = new JLabel("în luna");
			lblLuna.setFont(new Font("Ubuntu", Font.PLAIN, 18));
			lblLuna.setBounds(559, 90, 55, 35);
			this.add(lblLuna);

			DateTime dt = new DateTime(Calendar.getInstance());
			DateTime p1 = dt.prevMonth();
			DateTime p2 = p1.prevMonth();
			DateTime p3 = p2.prevMonth();
			
			String[] monthSelect = new String[] {
				monthNames[ Integer.parseInt(p3.getLuna()) ],
				monthNames[ Integer.parseInt(p2.getLuna()) ],
				monthNames[ Integer.parseInt(p1.getLuna()) ]
			};
			
			cbLuna = new JComboBox<String>();
			cbLuna.setFont(new Font("Ubuntu", Font.PLAIN, 18));
			cbLuna.setModel(new DefaultComboBoxModel<String>(monthSelect));
			cbLuna.setBounds(626, 90, 156, 35);
			cbLuna.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					updateMedicInterface();
				}
				
			});
			this.add(cbLuna);
			
			scrollPane = new JScrollPane();
			scrollPane.setBounds(62, 153, 877, 459);
			this.add(scrollPane);
			
			updateMedicInterface();
		}
	}
	
	private void initialize() {		
		String[] items = null;
		try {
			String query = "SELECT CONCAT(nume, ' ', prenume) AS 'angajat' FROM angajati WHERE functie = '2'";
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();
			
			int count = 0;
			while (rst.next()) {
				++count;
			}
			rst.beforeFirst();
			items = new String[count];
			count = 0;
			while (rst.next()) {
				items[count++] = rst.getString("angajat");
			}
			
			ps.close();
			rst.close();
		} catch (SQLException ex) {
			ErrorLog.printError("ViewProfit GetUsers SQLException: " + ex);
		}
		
		JLabel lblBy = new JLabel("Afișază profitul după");
		lblBy.setFont(new Font("Ubuntu", Font.PLAIN, 18));
		lblBy.setBounds(258, 90, 169, 35);
		this.add(lblBy);
		
		cbBy = new JComboBox<String>();
		cbBy.setFont(new Font("Ubuntu", Font.PLAIN, 18));
		cbBy.setModel(new DefaultComboBoxModel<String>(new String[] {"policlinică", "medic"}));
		cbBy.setBounds(439, 90, 108, 35);
		cbBy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (cbBy.getSelectedIndex() == 0) {
					cbMedic.setEnabled(false);
					cbMedic.setSelectedIndex(-1);
					cbModul.setEnabled(false);
					cbModul.setSelectedIndex(-1);
				} else {
					cbMedic.setEnabled(true);
					cbMedic.setSelectedIndex(0);
					cbModul.setEnabled(true);
					cbModul.setSelectedIndex(0);
				}
				updateInterface();
			}
			
		});
		this.add(cbBy);
		
		JLabel lblLuna = new JLabel("în luna");
		lblLuna.setFont(new Font("Ubuntu", Font.PLAIN, 18));
		lblLuna.setBounds(559, 90, 55, 35);
		this.add(lblLuna);
		
		DateTime dt = new DateTime(Calendar.getInstance());
		DateTime p1 = dt.prevMonth();
		DateTime p2 = p1.prevMonth();
		DateTime p3 = p2.prevMonth();
		
		String[] monthSelect = new String[] {
			monthNames[ Integer.parseInt(p3.getLuna()) ],
			monthNames[ Integer.parseInt(p2.getLuna()) ],
			monthNames[ Integer.parseInt(p1.getLuna()) ]
		};
		
		cbLuna = new JComboBox<String>();
		cbLuna.setFont(new Font("Ubuntu", Font.PLAIN, 18));
		cbLuna.setBounds(626, 90, 156, 35);
		cbLuna.setModel(new DefaultComboBoxModel<String>(monthSelect));
		cbLuna.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateInterface();
			}
			
		});
		this.add(cbLuna);
		
		JLabel lblMedic = new JLabel("Medic");
		lblMedic.setFont(new Font("Ubuntu", Font.PLAIN, 18));
		lblMedic.setBounds(111, 153, 55, 35);
		this.add(lblMedic);
		
		cbMedic = new JComboBox<String>();
		cbMedic.setEditable(true);
		cbMedic.setModel(new DefaultComboBoxModel<String>(items));
		cbMedic.setFont(new Font("Ubuntu", Font.PLAIN, 18));
		cbMedic.setBounds(181, 153, 243, 35);
		cbMedic.setEnabled(false);
		cbMedic.setSelectedIndex(-1);
		
		AutoCompleteDecorator.decorate(cbMedic);
		
		Component editor = cbMedic.getEditor().getEditorComponent();
		if (editor instanceof JTextComponent) {
			final JTextComponent tc = (JTextComponent) editor;
			tc.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void changedUpdate(DocumentEvent arg0) {
					updateInterface();
				}

				@Override
				public void insertUpdate(DocumentEvent arg0) {
					updateInterface();
				}

				@Override
				public void removeUpdate(DocumentEvent arg0) {
					updateInterface();
				}
				
			});
		}
		
		this.add(cbMedic);
		
		JLabel lblModul = new JLabel("După ");
		lblModul.setFont(new Font("Ubuntu", Font.PLAIN, 18));
		lblModul.setBounds(672, 153, 47, 35);
		this.add(lblModul);

		cbModul = new JComboBox<String>();
		cbModul.setFont(new Font("Ubuntu", Font.PLAIN, 18));
		cbModul.setBounds(731, 153, 156, 35);
		cbModul.setModel(new DefaultComboBoxModel<String>(new String[] {"policlinică", "specializare"}));
		cbModul.setEnabled(false);
		cbModul.setSelectedIndex(-1);
		cbModul.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateInterface();
			}
			
		});
		this.add(cbModul);
		
		JSeparator contentSeparator = new JSeparator();
		contentSeparator.setBackground(new Color(70, 130, 180));
		contentSeparator.setBounds(62, 201, 877, 2);
		this.add(contentSeparator);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(62, 216, 877, 396);
		this.add(scrollPane);

		updateInterface();
		
		table.setFont(new Font("Ubuntu", Font.PLAIN, 16));	
	}
	
	private void updateInterface() {
		if (table != null)
			this.remove(table);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		lblTotal.setText("");
		lblTotal.setVisible(false);
		
		int month = 0;
		for (int index = 1; index <= 12; ++index)
			if (monthNames[index].equals((String) cbLuna.getSelectedItem())) {
				month = index;
				break;
			}
		
		
		DateTime current = new DateTime(Calendar.getInstance());
		current.setZi(1);
		current.setOra(0);
		current.setMinute(0);
		current.setSecunde(0);
		
		DateTime monthSearchFor = current.copy();
		monthSearchFor.setLuna(month);
		
		DateTime monthBegin = current.copy();
		for (int i = 1; i <= 3; ++i) {
			monthBegin = monthBegin.prevMonth();
			if (monthBegin.getLuna().equals(monthSearchFor.getLuna())) {
				break;
			}
		}
		
		if (cbBy.getSelectedIndex() == 0) {
			// Daca este "afisare profit dupa policlinica"
			updateTable(monthBegin);
		} else {
			// Daca este "afisare profit dupa medic"
			
			int medicID = -1;
			try {
				String query = "SELECT id FROM angajati WHERE CONCAT(nume, ' ', prenume) LIKE '%" + ((String) cbMedic.getSelectedItem()) + "%' LIMIT 1";
				PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
				ResultSet rst = ps.executeQuery();
				
				if (rst.next()) {
					medicID = rst.getInt("id");
				} else {
					JOptionPane.showMessageDialog(window.mainFrame, "Nu am putut selecta medicul. Vă rugăm reîncercați.", "Eroare", JOptionPane.ERROR_MESSAGE);
				}
				
				ps.close();
				rst.close();
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(window.mainFrame, "Nu am putut selecta medicul. Vă rugăm reîncercați.", "Eroare", JOptionPane.ERROR_MESSAGE);
				ErrorLog.printError("ViewProfit GetMedic SQLException: " + ex);
			}
			
			Medic m = new Medic(window.dbHandle, medicID);
			
			if (cbModul.getSelectedIndex() == 0) {
				// Daca este "afisare profit dupa medic, dupa policlinica"
				updateTableByCenter(monthBegin, m);
			} else {
				// Daca este "afisare profit dupa medic, dupa specializare"
				updateTableByComp(monthBegin, m);
			}
		}
	}
	
	private void updateMedicInterface() {
		if (table != null)
			this.remove(table);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		lblTotal.setText("");
		lblTotal.setVisible(false);

		int month = 0;
		for (int index = 1; index <= 12; ++index)
			if (monthNames[index].equals((String) cbLuna.getSelectedItem())) {
				month = index;
				break;
			}
		
		DateTime current = new DateTime(Calendar.getInstance());
		current.setZi(1);
		current.setOra(0);
		current.setMinute(0);
		current.setSecunde(0);
		
		DateTime monthSearchFor = current.copy();
		monthSearchFor.setLuna(month);
		
		DateTime monthBegin = current.copy();
		for (int i = 1; i <= 3; ++i) {
			monthBegin = monthBegin.prevMonth();
			if (monthBegin.getLuna().equals(monthSearchFor.getLuna())) {
				break;
			}
		}
		
		Medic m = new Medic(window.dbHandle, window.userLogged.getID());
		
		if (cbModul.getSelectedIndex() == 0) {
			// Daca este "afisare profit dupa medic, dupa policlinica"
			updateTableByCenter(monthBegin, m);
		} else {
			// Daca este "afisare profit dupa medic, dupa specializare"
			updateTableByComp(monthBegin, m);
		}
	}
	
	private void updateTable(DateTime monthBegin) {
		DateTime monthEnd = monthBegin.nextMonth();
		DefaultTableModel model = new DefaultTableModel (
			new Object[][] { },
			new String[] {
				"Policlinică", "Profit brut", "Profit net"
			}
		) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
			
		table.setModel(model);
		
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setResizable(false);

		model.setRowCount(0);
		
		double total = 0.0;
		
		ArrayList<Policlinica> list = Policlinica.downloadDatabase(window.dbHandle);
		for (Policlinica p : list) {
			double venituri = 0.0;
			double cheltuieli = 0.0;
			try {				
				String query = "SELECT "
						+ "suma, "
						+ "medici.procent_salariu "
						+ "FROM bonuri_emise "
						+ "INNER JOIN programari ON programari.id = bonuri_emise.programare_id "
						+ "INNER JOIN medici ON medici.id = programari.medic_id "
						+ "WHERE programari.policlinica_id = '" + p.getID() + "' "
						+ "AND programari.data_programare BETWEEN '" + monthBegin + "' AND '" + monthEnd + "'";
				
				PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
				ResultSet rst = ps.executeQuery();
				
				int proc, suma;
				
				while (rst.next()) {
					proc = rst.getInt("procent_salariu");
					suma = rst.getInt("suma");
					cheltuieli += (proc * 0.01) * suma;
				
					venituri += suma;
				}
				
				ps.close();
				rst.close();
			} catch (SQLException ex) {
				ErrorLog.printError("ViewProfit CenterProfit SQLException: " + ex);
			}
			
			model.addRow(new String[] {
				p.getDenumire(),
				venituri + "",
				(venituri - cheltuieli) + ""
			});
			
			total += (venituri - cheltuieli);
		}
		
		lblTotal.setVisible(true);
		lblTotal.setText("TOTAL: " + total + " LEI");
	}
	
	private void updateTableByCenter(DateTime monthBegin, Medic m) {
		DateTime monthEnd = monthBegin.nextMonth();
		
		DefaultTableModel model = new DefaultTableModel (
				new Object[][] { },
				new String[] {
					"Policlinică", "Profit brut", "Profit net"
				}
			) {
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
				
		table.setModel(model);
		
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(2).setResizable(false);

		model.setRowCount(0);
		
		double total = 0.0;
		try {
			String query = "SELECT " + 
					"	policlinici.denumire, " + 
					"    (SELECT SUM(suma) " + 
					"	FROM bonuri_emise " + 
					"    INNER JOIN programari ON bonuri_emise.programare_id = programari.id " + 
					"    WHERE programari.medic_id = '" + m.getID() + "' " + 
					"    AND programari.policlinica_id = policlinici.id " + 
					"    AND programari.data_programare BETWEEN '" + monthBegin + "' AND '" + monthEnd + "') AS 'suma' " + 
					"FROM program_angajati " + 
					"INNER JOIN policlinici ON policlinici.id = program_angajati.policlinica_id " + 
					"WHERE program_angajati.angajat_id = '" + m.getID() + "'";

			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();
			
			while (rst.next()) {
				Object[] row = new Object[3];
				row[0] = rst.getString("denumire");
				
				double suma = rst.getInt("suma") * 1.0;
				double proc = Integer.parseInt(m.getProcentSalariu()) * 0.01;
				
				row[1] = suma;
				
				double net = suma - (suma * proc);
				if (window.userLogged.getFunctie() == 2)
					net = (suma * proc);
				
				row[2] = net;
				
				total += net;
				
				model.addRow(row);
			}
			
			ps.close();
			rst.close();
		} catch (SQLException ex) {
			ErrorLog.printError("ViewProfit GetProfitByCenter SQLException: " + ex);
		}
		
		lblTotal.setVisible(true);
		lblTotal.setText("TOTAL: " + total + " LEI");
	}
	
	private void updateTableByComp(DateTime monthBegin, Medic m) {
		DateTime monthEnd = monthBegin.nextMonth();
		
		DefaultTableModel model = new DefaultTableModel (
				new Object[][] { },
				new String[] {
					"Specializare", "Profit brut", "Profit net"
				}
			) {
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
				
		table.setModel(model);
		
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(2).setResizable(false);

		model.setRowCount(0);
		
		String query = "select ds.denumire,"
				+ "(SELECT SUM(suma) " 
				+ "FROM bonuri_emise "  
			    + "INNER JOIN programari ON bonuri_emise.programare_id = programari.id "
			    + "WHERE programari.medic_id = '"+ m.getID() + "' "
			    + "AND programari.serviciu_id = ds.id "
			    + "and programari.data_programare between '" + monthBegin + "' and '" + monthEnd + "') as 'suma' " 
			    + " from servicii inner join denumire_servicii ds on ds.id = servicii.denumire_id " 
			    + " where medic_id = '" + m.getID() + "'";

		double total = 0.0;
		try {
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();
			
			while (rst.next()) {
				Object[] row = new Object[3];
				row[0] = rst.getString("ds.denumire");
				
				double suma = rst.getInt("suma") * 1.0;
				double proc = Integer.parseInt(m.getProcentSalariu()) * 0.01;
				
				row[1] = suma;
				
				double net = suma - (suma * proc);
				if (window.userLogged.getFunctie() == 2)
					net = (suma * proc);
				
				row[2] = net;
				
				total += net;
				
				model.addRow(row);
			}
			
			ps.close();
			rst.close();			
		} catch (SQLException ex) {
			ErrorLog.printError("ViewProfit GetProfitByComp SQLException: " + ex);
		}
		
		lblTotal.setVisible(true);
		lblTotal.setText("TOTAL: " + total + " LEI");
	}

}
