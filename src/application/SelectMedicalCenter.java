/**
 * Modul pentru selectarea centrului medical din care se doreste a opera
 */
package application;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import util.ErrorLog;

import javax.swing.JScrollPane;
import javax.swing.JSeparator;
/**
 * @author Vali
 *
 */

public class SelectMedicalCenter extends JPanel {
	private static final long serialVersionUID = 1277890532682497385L;
	
	private JTable resultTable;
	private DefaultTableModel resultTableModel;

	private Main window;

	private String query = "SELECT * FROM policlinici";

	/**
	 * Create the application.
	 */
	public SelectMedicalCenter(Main window, String tabTitle) {
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
				query = "SELECT * FROM `policlinici` " + 
						"WHERE denumire LIKE '%" + tfSearch.getText() + "%';";
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
	
		resultTableModel = new DefaultTableModel (
			new Object[][] { },
			new String[] { "ID", "Denumire", "Adresa" }
		) {
			private static final long serialVersionUID = 1L;
			
			
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		resultTable = new JTable();
		resultTable.setModel(resultTableModel);		
		resultTable.getColumnModel().getColumn(0).setMaxWidth(50);
		resultTable.getColumnModel().getColumn(0).setResizable(false);
		resultTable.getColumnModel().getColumn(1).setMinWidth(200);
		resultTable.getColumnModel().getColumn(1).setResizable(false);
		resultTable.getColumnModel().getColumn(2).setMinWidth(200);
		resultTable.getColumnModel().getColumn(2).setResizable(false);
		
		resultTable.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		
		updateTable();
		
		resultTable.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent mouseEvent) {
		        Point point = mouseEvent.getPoint();
		        int row = resultTable.rowAtPoint(point);
		        if (mouseEvent.getClickCount() == 2 && resultTable.getSelectedRow() != -1) {
		        	int centerID = Integer.parseInt((String) resultTable.getValueAt(row, 0));
		        	String centerName = (String) resultTable.getValueAt(row, 1);
		        	int confirm = JOptionPane.showConfirmDialog(null, "Dorești să setezi Centrul Medical " + centerName + "?", "Confirmare", JOptionPane.YES_NO_OPTION);		        	
		        	if (confirm == JOptionPane.YES_OPTION) {
		        		window.configFile.setPoliclinicaID(centerID);
		        		window.initialize();
		        	}
		        }
		    }
		});
		
		scrollPane.setViewportView(resultTable);
		
		JLabel lblWarning = new JLabel("* Apasă dublu click pe policlinica pe care dorești să setezi aplicația.");
		lblWarning.setFont(new Font("Ubuntu", Font.ITALIC, 16));
		lblWarning.setBounds(12, 616, 736, 20);
		lblWarning.setVisible(true);
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
	}
	
	private void updateTable() {
		resultTableModel.setRowCount(0);
		
		try {
			PreparedStatement ps = window.dbHandle.getConnection().prepareStatement(query);
			ResultSet rst = ps.executeQuery();
			
			while (rst.next()) {
				String[] r = new String[3];
				
				r[0] = rst.getString("id");
				r[1] = rst.getString("denumire");
				r[2] = rst.getString("adresa");
				
				resultTableModel.addRow(r);
				
			}

			rst.close();
		} catch (SQLException ex) {
			ErrorLog.printError("SelectMedicalCenter SQLException: " + ex);
		}
	}
}
