/**
 * Modul pentru orar
 */
package application;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import util.DateTime;
import util.ErrorLog;
import util.Program;
import util.Timetable;

/**
 * @author Vali
 *
 */
public class SchedulePanel extends JPanel {
	private static final long serialVersionUID = 4970838901835098587L;

	private Main window;
	
	public SchedulePanel(Main window, String tabTitle) {
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
		
		if (window.userLogged.getFunctie() == 2) {
			medicEmployeeTimetable(this);
		} else {
			regularEmployeeTimetable(this);
		}
		
	}
	
	public void medicEmployeeTimetable(JPanel panel) {
		JTable viewTable = new JTable() {
			private static final long serialVersionUID = -6294149579462622828L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		};

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 83, 989, 526);
		panel.add(scrollPane);
		
		DefaultTableModel viewTableModel = new DefaultTableModel (
			new Object[][] { },
			new String[] {
				"Policlinica", "Luni", "Marti", "Miercuri", "Joi", "Vineri", "Sambata", "Duminica"
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
		viewTable.getColumnModel().getColumn(0).setPreferredWidth(275);
		viewTable.getColumnModel().getColumn(1).setResizable(false);
		viewTable.getColumnModel().getColumn(2).setResizable(false);
		viewTable.getColumnModel().getColumn(3).setResizable(false);
		viewTable.getColumnModel().getColumn(4).setResizable(false);
		viewTable.getColumnModel().getColumn(5).setResizable(false);
		viewTable.getColumnModel().getColumn(6).setResizable(false);
		viewTable.getColumnModel().getColumn(7).setResizable(false);
		viewTable.getTableHeader().setReorderingAllowed(false);
		viewTable.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		
		updateTable(viewTableModel);
		
		scrollPane.setViewportView(viewTable);
		
		JLabel lblWarning = new JLabel("* Nota: ...");
		lblWarning.setFont(new Font("Ubuntu", Font.ITALIC, 16));
		lblWarning.setBounds(12, 616, 736, 20);
		lblWarning.setVisible(false);
		panel.add(lblWarning);
		
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
		panel.add(btnRefresh);
	}
	
	void updateTable(DefaultTableModel model) {
		model.setRowCount(0);
		
		try {
			String query = "SELECT "
					+ "policlinici.denumire AS 'denumire', "
					+ "program_angajati.program AS 'program' "
					+ "FROM program_angajati "
					+ "INNER JOIN policlinici ON policlinici.id = program_angajati.policlinica_id "
					+ "WHERE program_angajati.angajat_id = '" + window.userLogged.getID() + "'";
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();
			
			while (rst.next()) {
				Object[] row = new Object[8];
				
				row[0] = rst.getString("denumire");
				Program p = new Program(rst.getString("program"));
				for (int dayIndex = 0; dayIndex < 7; ++dayIndex) {
					row[dayIndex + 1] = p.get(dayIndex).toString();
				}
				
				model.addRow(row);
			}
			
			ps.close();
			rst.close();
		} catch (SQLException ex) {
			
		}
	}
	
	public void regularEmployeeTimetable(JPanel panel) {
		Timetable t = new Timetable(panel, false);
		
		String query = "SELECT program FROM program_angajati WHERE angajat_id = '" + window.userLogged.getID() + "' LIMIT 1";
		try {
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();
			
			if (rst.next()) {
				t.setVisible(true);
				Program p = new Program(rst.getString("program"));
				
				for (int i = 0; i < 7; ++i) {
					if (p.get(i).isOpen()) {
						t.setContent(i, 0, p.get(i).getInceput() + "");
						t.setContent(i, 1, p.get(i).getSfarsit() + "");
					} else {
						t.setContent(i, 0, "Liber");
						t.setContent(i, 1, "Liber");
					}
				}
				
				int dayIndex = DateTime.getCurrentDayIndex();
				t.setMarkedRow(dayIndex);
			} else {
				t.setVisible(false);
			}
			
			ps.close();
			rst.close();
		} catch (SQLException ex) {
			ErrorLog.printError("SchedulePanel RegularEmployeeTimetable SQLException: " + ex);
		}
		
	}
	
}
