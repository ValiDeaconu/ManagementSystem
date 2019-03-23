/**
 * Aplicatie pentru desenarea pe pattern-ul stabilit
 */
package application;

/**
 * 
 * @author Vali
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import javax.swing.JScrollPane;
import javax.swing.ImageIcon;

import util.Database;
import util.ErrorLog;

import javax.swing.JRadioButton;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JProgressBar;
import javax.swing.JList;
import javax.swing.JSlider;
import javax.swing.JScrollBar;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import org.jdesktop.swingx.autocomplete.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("unused")
public class TestDesign {
	protected JFrame mainFrame = new JFrame();
	
	protected JTabbedPane tabbedPane;

	protected Database dbHandle;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestDesign window = new TestDesign();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TestDesign() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	protected void initialize() {
		mainFrame.getContentPane().removeAll();
		
		mainFrame.setResizable(false);
		mainFrame.getContentPane().setFont(new Font("Ubuntu", Font.PLAIN, 16));
		mainFrame.setVisible(true);
		mainFrame.setBounds(100, 100, 1024, 768);
		mainFrame.setTitle("MEDHEAL SYSTEM");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		mainFrame.setLocationRelativeTo(null);
		
		this.dbHandle = new Database("localhost", "3306", "lant_policlinici", "adminLantPoli", "KbKtB6pw#5SVJ8hH");
		
		//UIManager.put("Menu.selectionBackground", Color.BLUE);
		//UIManager.put("Menu.selectionForeground", Color.WHITE);
		UIManager.put("Menu.background", Color.WHITE);
		UIManager.put("Menu.foreground", Color.BLACK);
		UIManager.put("Menu.opaque", false);
		
		JMenuBar menuBar = new JMenuBar();
		mainFrame.setJMenuBar(menuBar);
		
		JMenu mnHelp = new JMenu("Help");
		mnHelp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				mnHelp.setSelected(true);
			};

			@Override
			public void mouseExited(MouseEvent e) {
				mnHelp.setSelected(false);
			};
		});
		menuBar.add(mnHelp);
		
		JMenuItem mntmCredits = new JMenuItem("Credits");
		mnHelp.add(mntmCredits);
		
		JMenu mnExit = new JMenu("Exit");
		mnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainFrame.dispose();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				mnExit.setSelected(true);
			};

			@Override
			public void mouseExited(MouseEvent e) {
				mnExit.setSelected(false);
			};
		});
		menuBar.add(mnExit);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 1018, 679);
		mainFrame.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("New tab", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblTitle = new JLabel("Contabilitate");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Ubuntu", Font.PLAIN, 42));
		lblTitle.setBounds(12, 13, 989, 49);
		panel.add(lblTitle);
		
		JSeparator headSeparator = new JSeparator();
		headSeparator.setBackground(new Color(70, 130, 180));
		headSeparator.setBounds(62, 75, 877, 2);
		panel.add(headSeparator);
		
		String[] items = null;
		try {
			String query = "SELECT CONCAT(nume, ' ', prenume) AS 'angajat' FROM angajati";
			PreparedStatement ps = dbHandle.getConnection().prepareStatement(query);
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
			ErrorLog.printError("--- GetUsers SQLException: " + ex);
		}
		
		JLabel lblBy = new JLabel("Afișază profitul după");
		lblBy.setFont(new Font("Ubuntu", Font.PLAIN, 18));
		lblBy.setBounds(225, 90, 169, 35);
		panel.add(lblBy);
		
		JComboBox<String> cbModule1 = new JComboBox<String>();
		cbModule1.setFont(new Font("Ubuntu", Font.PLAIN, 18));
		cbModule1.setModel(new DefaultComboBoxModel<String>(new String[] {"policlinică", "medic"}));
		cbModule1.setBounds(406, 90, 141, 35);
		panel.add(cbModule1);
		
		JLabel lblLuna = new JLabel("în luna");
		lblLuna.setFont(new Font("Ubuntu", Font.PLAIN, 18));
		lblLuna.setBounds(559, 90, 55, 35);
		panel.add(lblLuna);
		
		JComboBox<String> cbLuna = new JComboBox<String>();
		cbLuna.setFont(new Font("Ubuntu", Font.PLAIN, 18));
		cbLuna.setBounds(626, 90, 156, 35);
		panel.add(cbLuna);
		
		JSeparator bodySeparator = new JSeparator();
		bodySeparator.setBackground(new Color(70, 130, 180));
		bodySeparator.setBounds(62, 138, 877, 2);
		panel.add(bodySeparator);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(62, 153, 877, 459);
		panel.add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null},
			},
			new String[] {
				"New column"
			}
		));
		scrollPane.setViewportView(table);
		table.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		
		JLabel lblTotal = new JLabel("TOTAL: XXXXXXX");
		lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotal.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		lblTotal.setBounds(626, 619, 284, 17);
		panel.add(lblTotal);
		
		JLabel lblcMedhealSystem = new JLabel("\u00A9 2018, MEDHEAL SYSTEM");
		lblcMedhealSystem.setFont(new Font("Ubuntu", Font.ITALIC, 16));
		lblcMedhealSystem.setHorizontalAlignment(SwingConstants.CENTER);
		lblcMedhealSystem.setBounds(0, 678, 1018, 29);
		mainFrame.getContentPane().add(lblcMedhealSystem);
	}
}
