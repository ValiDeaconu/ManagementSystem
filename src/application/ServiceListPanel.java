/**
 * Modul pentru vizualizarea si modificarea preturilor serviciilor
 */
package application;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import tables.Serviciu;
import util.ErrorLog;

/**
 * @author Vali
 *
 */
public class ServiceListPanel extends JPanel {
	private static final long serialVersionUID = -4748084523159048372L;

	@SuppressWarnings("unused")
	private JTable viewTable;
	
	private Main window;
	
	private String[] denumireSpecializari;
	private String[] denumireServicii; 
	
	private ArrayList<Integer> indexes = new ArrayList<Integer>();
	
	public ServiceListPanel(Main window, String tabTitle) {
		this.setLayout(null);
		this.window = window;
		
		JLabel lblTitle = new JLabel("Lista servicii");
		lblTitle.setFont(new Font("Ubuntu", Font.PLAIN, 42));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(12, 5, 989, 49);
		this.add(lblTitle);
		
		JSeparator headSeparator = new JSeparator();
		headSeparator.setForeground(new Color(70, 130, 180));
		headSeparator.setBounds(62, 67, 889, 2);
		this.add(headSeparator);
		
		denumireServicii = new String[] { "Nu exista servicii" };
		try {
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement("SELECT denumire FROM denumire_servicii");
			ResultSet rst = ps.executeQuery();
			
			int count = 0;
			while (rst.next())
				++count;

			rst.beforeFirst();
			denumireServicii = new String[count];
			int index = 0;
			while (rst.next()) {
				denumireServicii[index++] = rst.getString("denumire");
			}
		} catch (SQLException e) {
			ErrorLog.printError("ServiceListPanel SQLException: " + e);
		}
		
		denumireSpecializari = new String[] { "Nu exista specializari" };
		String query = "SELECT competente.denumire AS 'denumire' " + 
				"FROM competente " + 
				"INNER JOIN specialitati ON competente.id = specialitati.competenta_id " + 
				"WHERE specialitati.medic_id = " + window.userLogged.getID();
		try {
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();

			int count = 0;
			while (rst.next())
				++count;

			rst.beforeFirst();
			denumireSpecializari = new String[count];
			int index = 0;
			while (rst.next()) {
				denumireSpecializari[index++] = rst.getString("denumire");
			}
		} catch (SQLException e) {
			ErrorLog.printError("ServiceListPanel SQLException: " + e);			
		}
		
		JTable viewTable = new JTable() {
			private static final long serialVersionUID = -6294149579462622828L;
		};

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 83, 989, 526);
		this.add(scrollPane);
		
		DefaultTableModel viewTableModel = new DefaultTableModel (
			new Object[][] { },
			new String[] {
				"ID", "Serviciu", "Pret", "Durata", "Specializare"
			}
		) {
			private static final long serialVersionUID = -4941439765294853786L;

			public boolean isCellEditable(int row, int column) {
				boolean[] columnEditables = new boolean[] {
						false, false, true, true, false
					};					
				
				return columnEditables[column];
			}
		};
		
		viewTable.setModel(viewTableModel);
		viewTable.getColumnModel().getColumn(0).setResizable(false);
		viewTable.getColumnModel().getColumn(0).setMaxWidth(80);
		viewTable.getColumnModel().getColumn(1).setResizable(false);
		viewTable.getColumnModel().getColumn(2).setResizable(false);
		viewTable.getColumnModel().getColumn(3).setResizable(false);
		viewTable.getColumnModel().getColumn(4).setResizable(false);
		viewTable.getTableHeader().setReorderingAllowed(false);
		viewTable.setFont(new Font("Ubuntu", Font.PLAIN, 16));
	
		viewTable.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					int row = viewTable.getSelectedRow();
					int column = viewTable.getSelectedColumn();
					
					String newValue = viewTable.getValueAt(row, column).toString();
					
					if (newValue == null)
						return;
					
					int index = Integer.parseInt(viewTable.getValueAt(row, 0).toString());
					int id = indexes.get(index - 1);
					Serviciu s = new Serviciu(window.dbHandle, id);
					
					switch (column) {
						case 2: 
							s.setPret(Integer.parseInt(newValue));
							break;
						case 3:
							s.setDurata(Integer.parseInt(newValue));
							break;
						default:
							break;
					}
					
					s.updateOnDatabase(window.dbHandle);
				}
			}
		});
		
		updateTable(viewTableModel);
		
		scrollPane.setViewportView(viewTable);

		JLabel lblWarning = new JLabel("* Nota: Apasă dublu click pe coloana corespunzătoare pentru a efectua o modificare.");
		lblWarning.setFont(new Font("Ubuntu", Font.ITALIC, 16));
		lblWarning.setBounds(12, 616, 736, 20);
		lblWarning.setVisible(true);
		this.add(lblWarning);
	
		JButton btnInsertRow = new JButton("Adaugă rând");
		btnInsertRow.setIcon(new ImageIcon("resources/add_icon.png"));
		btnInsertRow.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		btnInsertRow.setBounds(683, 616, 144, 20);
		btnInsertRow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String tabTitle = "Adaugă serviciu";
				if (!window.isTabOpened(tabTitle)) {
					window.addNewTab(new AddServicePanel(window, tabTitle), tabTitle);
				}
			}
			
		});
		this.add(btnInsertRow);
		
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
		String query = "SELECT servicii.id AS 'id', " +
				"denumire_servicii.denumire AS 'serviciu', " +
				"servicii.pret AS 'pret', " +
				"servicii.durata AS 'durata', " +
				"competente.denumire AS 'specializare' " + 
				"FROM servicii " + 
				"INNER JOIN denumire_servicii ON servicii.denumire_id = denumire_servicii.id " + 
				"INNER JOIN competente ON servicii.competenta_id = competente.id " + 
				"WHERE servicii.medic_id = '" + window.userLogged.getID() + "'";
		try {
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();
			
			int rowCount = 1;
			while (rst.next()) {
				String[] row = new String[5];
				indexes.add(Integer.parseInt(rst.getString("id")));
				row[0] = rowCount + "";
				row[1] = rst.getString("serviciu");
				row[2] = rst.getString("pret");
				row[3] = rst.getString("durata");
				row[4] = rst.getString("specializare");
				
				++rowCount;
				viewTableModel.addRow(row);
			}

			ps.close();
			rst.close();
		} catch (SQLException ex) {
			ErrorLog.printError("AbilityPanel SQLException: " + ex);
		}
	}
}
