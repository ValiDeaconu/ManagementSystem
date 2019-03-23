/**
 * Modul pentru vizualizarea specialitatilor
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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import util.ErrorLog;

/**
 * @author Vali
 *
 */
public class AbilityPanel extends JPanel {
	private static final long serialVersionUID = 6871198954437545263L;
	
	private JButton btnInsertRow;
	
	private Main window;

	/**
	 * @param window frame-ul pe care se lucreaza 
	 * @param tabTitle titlul tabului
	 */
	public AbilityPanel(Main window, String tabTitle) {
		this.setLayout(null);
		this.window = window;
		
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
		};

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 83, 989, 526);
		this.add(scrollPane);
		
		DefaultTableModel viewTableModel = new DefaultTableModel (
			new Object[][] { },
			new String[] {
				"ID", "Specialitate", "Grad"
			}
		) {
			private static final long serialVersionUID = -4941439765294853786L;

			public boolean isCellEditable(int row, int column) {
				boolean[] columnEditables = new boolean[] {
						false, false, false, false, false, false, false, false
					};					
				
				return columnEditables[column];
			}
		};
		
		viewTable.setModel(viewTableModel);
		viewTable.getColumnModel().getColumn(0).setResizable(false);
		viewTable.getColumnModel().getColumn(0).setMaxWidth(80);
		viewTable.getColumnModel().getColumn(1).setResizable(false);
		viewTable.getColumnModel().getColumn(2).setResizable(false);
		viewTable.getTableHeader().setReorderingAllowed(false);
		viewTable.setFont(new Font("Ubuntu", Font.PLAIN, 16));
	
		viewTable.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent mouseEvent) {
		        Point point = mouseEvent.getPoint();
		        int row = viewTable.rowAtPoint(point);
		        if (mouseEvent.getClickCount() == 2 && viewTable.getSelectedRow() != -1) {
		        	String specializare = (String) viewTable.getValueAt(row, 1);
		        	int confirm = JOptionPane.showConfirmDialog(window.mainFrame, "Ești sigur că dorești să ștergi specializarea " + specializare + "?", "Confirmare", JOptionPane.YES_NO_OPTION);
		        	if (confirm == JOptionPane.YES_OPTION) {
		        		String query = "DELETE FROM specialitati WHERE medic_id = '" + window.userLogged.getID() + "' AND competenta_id = (SELECT id FROM competente WHERE denumire = '" + specializare + "')";
		        		try {
		        			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
		        			
		        			if (ps.executeUpdate() == 1) {
		        				JOptionPane.showMessageDialog(window.mainFrame, "Șteregerea s-a realizat!", "Succes", JOptionPane.INFORMATION_MESSAGE);
		        			} else {
		        				ErrorLog.printError("AbilityPanel RemoveAbility Au fost afectate 0 sau 2+ linii");
		        				JOptionPane.showMessageDialog(window.mainFrame, "Nu s-a putut efectua ștergerea, vă rugăm reîncercați.", "Eroare", JOptionPane.ERROR_MESSAGE);
		        			}
		        			
		        			ps.close();
		        		} catch (SQLException ex) {
		        			JOptionPane.showMessageDialog(window.mainFrame, "Nu s-a putut efectua ștergerea, vă rugăm reîncercați.", "Eroare", JOptionPane.ERROR_MESSAGE);
		        			ErrorLog.printError("AbilityPanel RemoveAbility SQLException: " + ex);
		        		}
		        	}
		        }
		    }
		});
	
		updateTable(viewTableModel);
		
		scrollPane.setViewportView(viewTable);

		JLabel lblWarning = new JLabel("* Nota: ...");
		lblWarning.setFont(new Font("Ubuntu", Font.ITALIC, 16));
		lblWarning.setBounds(12, 616, 736, 20);
		lblWarning.setVisible(false);
		this.add(lblWarning);
		
		
		if (viewTableModel.getRowCount() <= 2) {
			btnInsertRow = new JButton("Adaugă rând");
			btnInsertRow.setIcon(new ImageIcon("resources/add_icon.png"));
			btnInsertRow.setFont(new Font("Ubuntu", Font.PLAIN, 14));
			btnInsertRow.setBounds(683, 616, 144, 20);
			btnInsertRow.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String tabTitle = "Adaugă specializare";
					if (!window.isTabOpened(tabTitle)) {
						window.addNewTab(addAbility(window, tabTitle), tabTitle);
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
				updateTable(viewTableModel);
			}
			
		});
		this.add(btnRefresh);
	}
	
	/**
	 * Metoda pentru actualizarea tabelului
	 * @param viewTableModel modelul tabelului
	 */
	private void updateTable(DefaultTableModel viewTableModel) {
		viewTableModel.setRowCount(0);
		String query = "SELECT competente.denumire AS 'denumire', specialitati.grad AS 'grad' " + 
				"FROM competente " + 
				"INNER JOIN specialitati ON competente.id = specialitati.competenta_id " + 
				"WHERE specialitati.medic_id = '" + window.userLogged.getID() + "'";
		
		try {
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();
			
			int rowCount = 1;
			
			while (rst.next()) {
				String[] row = new String[3];
				row[0] = new String(rowCount + "");
				row[1] = rst.getString("denumire");
				row[2] = rst.getString("grad");
			
				++rowCount;
				
				viewTableModel.addRow(row);
			}

			ps.close();
			rst.close();
		} catch (SQLException ex) {
			ErrorLog.printError("AbilityPanel SQLException: " + ex);
		}
		
		if (viewTableModel.getRowCount() >= 3 && btnInsertRow != null)
			this.remove(btnInsertRow);
	}
	
	private JLabel lblNote;
	private boolean foundSpecializare = false;
	private String[] denumireSpecializari = new String[] { "Nu exista specializari" };
	private int[] denumireSpecializariIndexes = new int[0];
	/**
	 * Metoda ce creaza un panou in care se adauga noua specializare
	 * @param window frame-ul pe care se opereaza
	 * @param tabTitle titlul tabului
	 * @return panel panoul 
	 */
	private JPanel addAbility(Main window, String tabTitle) {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		
		String query = "SELECT id, denumire FROM competente";
		try {
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();

			int count = 0;
			while (rst.next())
				++count;

			rst.beforeFirst();
			denumireSpecializari = new String[count + 1];
			denumireSpecializariIndexes = new int[count + 1];
			int index = 0;
			denumireSpecializari[index] = "-";
			denumireSpecializariIndexes[index] = -1;
			++index;
			while (rst.next()) {
				denumireSpecializariIndexes[index] = Integer.parseInt(rst.getString("id"));
				denumireSpecializari[index] = rst.getString("denumire");
				++index;
			}
		} catch (SQLException e) {
			ErrorLog.printError("[AddServicePanel] SQLException: " + e);			
		}
		
		JLabel lblTitle = new JLabel(tabTitle);
		lblTitle.setFont(new Font("Ubuntu", Font.PLAIN, 42));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(12, 5, 989, 49);
		panel.add(lblTitle);
		
		JSeparator headSeparator = new JSeparator();
		headSeparator.setForeground(new Color(70, 130, 180));
		headSeparator.setBounds(62, 67, 889, 2);
		panel.add(headSeparator);
		
		JLabel lblSpecializare = new JLabel("Specializare");
		lblSpecializare.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSpecializare.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblSpecializare.setBounds(227, 244, 247, 25);
		panel.add(lblSpecializare);
		
		JComboBox<String> cbSpecializare = new JComboBox<String>(denumireSpecializari);
		cbSpecializare.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		cbSpecializare.setBounds(532, 244, 247, 25);
		cbSpecializare.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				String value = (String) cbSpecializare.getSelectedItem();
				if (value.equals("-")) {
					lblNote.setVisible(false);
					foundSpecializare = false;
				} else {
					lblNote.setVisible(false);
					foundSpecializare = false;
					
					String query = "SELECT id FROM `specialitati` WHERE medic_id = '" + window.userLogged.getID() + "' AND competenta_id = '" + denumireSpecializariIndexes [ cbSpecializare.getSelectedIndex() ] + "'";
					
					try {
						PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
						ResultSet rst = ps.executeQuery();
						
						if (rst.next()) {
							setNote(false, "Ți-ai setat deja specializarea " + ((String) cbSpecializare.getSelectedItem()) + ".");
						} else {
							foundSpecializare = true;
						}
					} catch (SQLException ex) {
						ErrorLog.printError("AbilityPanel AddAbility_CheckAbility SQLException: " + ex);
					}
				}
				
			}
		});
		panel.add(cbSpecializare);
		
		JLabel lblGrad = new JLabel("Grad");
		lblGrad.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGrad.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		lblGrad.setBounds(227, 282, 247, 25);
		panel.add(lblGrad);
		
		JComboBox<String> cbGrad = new JComboBox<String>(new String[] { "Specialist", "Primar" });
		cbGrad.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		cbGrad.setBounds(532, 282, 247, 25);
		panel.add(cbGrad);
		
		lblNote = new JLabel("");
		lblNote.setHorizontalAlignment(SwingConstants.CENTER);
		lblNote.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		lblNote.setBounds(12, 600, 590, 25);
		lblNote.setVisible(false);
		panel.add(lblNote);
		
		JButton btnSend = new JButton("Submit");
		btnSend.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		btnSend.setBounds(850, 600, 120, 25);
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lblNote.setVisible(false);
				
				if (!foundSpecializare) {
					setNote(false, "Trebuie introdusă o specializare validă.");
					return;
				}
				
				try {
					String query = "INSERT INTO specialitati (medic_id, competenta_id, grad) VALUES (" +
									"'" + window.userLogged.getID() + "', " +
									"'" + denumireSpecializariIndexes [ cbSpecializare.getSelectedIndex() ] + "', " +
									"'" + ((String) cbGrad.getSelectedItem()) + "')";
					
					PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
					int rowCount = ps.executeUpdate();
					
					if (rowCount != 1) {
						ErrorLog.printError("AbilityPanel AddAbilitySubmit Au fost afectate " + rowCount + " linii la inserare.");	
					} else {
						setNote(true, "Serviciul medical a fost adăugat cu succes!");
					}
				} catch (SQLException ex) {
					ErrorLog.printError("AbilityPanel AddAbilitySubmit SQLException: " + ex);
				}
			}			
		});
		panel.add(btnSend);
		
		JButton btnReset = new JButton("Reset");
		btnReset.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		btnReset.setBounds(718, 600, 120, 25);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cbSpecializare.setSelectedIndex(0);
				cbGrad.setSelectedIndex(0);
			}
		});
		panel.add(btnReset);
		
		return panel;
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
