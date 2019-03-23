/**
 * Panoul principal al aplicatiei
 */
package application;

/**
 * @author Vali
 * @author Ana-Maria
 *
 */

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import tables.Angajat;
import tables.Policlinica;

import util.ConfigFile;
import util.Database;

public class Main {
	protected JFrame mainFrame = new JFrame();
	
	protected JTabbedPane tabbedPane;

	/**
	 * Reprezinta policlinica din care se opereaza
	 */
	protected Policlinica centerUsed = null;

	/**
	 * Reprezinta angajatul care este logat in aplicatie
	 */
	protected Angajat userLogged = null;

	/**
	 * Reprezinta baza de date la care este conectata aplicatia
	 */
	protected Database dbHandle;
	
	/**
	 * Reprezinta legatura cu fisierul de configurari
	 */
	protected ConfigFile configFile;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {			
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();		
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		this.dbHandle = new Database("localhost", "3306", "lant_policlinici", "adminLantPoli", "KbKtB6pw#5SVJ8hH");
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
		
		//UIManager.put("Menu.selectionBackground", Color.BLUE);
		//UIManager.put("Menu.selectionForeground", Color.WHITE);
		UIManager.put("Menu.background", Color.WHITE);
		UIManager.put("Menu.foreground", Color.BLACK);
		UIManager.put("Menu.opaque", false);
		
		JMenuBar menuBar = new JMenuBar();
		mainFrame.setJMenuBar(menuBar);
		
		if (isLogged()) {
			JMenu mnAccount = new JMenu("Account");
			menuBar.add(mnAccount);
			
			mnAccount.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					mnAccount.setSelected(true);
				};

				@Override
				public void mouseExited(MouseEvent e) {
					mnAccount.setSelected(false);
				};
			});
			
			JMenuItem mntmLogout = new JMenuItem("Logout");
			mntmLogout.setIcon(new ImageIcon("resources/arrow-next-3-icon.png"));
			mntmLogout.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					userLogged = null;
					initialize();
                }
	        });
			mnAccount.add(mntmLogout);
		}
		
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
		
		JMenuItem mntmCenter = new JMenuItem("Change center");
		Main window = this;
		mntmCenter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tabTitle = "Selectare Centru Medical";
				if (!isTabOpened(tabTitle)) {
					addNewTab(new SelectMedicalCenter(window, tabTitle), tabTitle);
				}
            }
        });
		mnHelp.add(mntmCenter);
		
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
		

		this.configFile = new ConfigFile(this.dbHandle);
		int polIndex = configFile.getPoliclinicaID();
		
		if (polIndex < 1) {
			String tabTitle = "Selectare Centru Medical";
			SelectMedicalCenter selectPanel = new SelectMedicalCenter(this, tabTitle);
			tabbedPane.addTab(tabTitle, null, selectPanel, null);
		} else {
			centerUsed = new Policlinica(this.dbHandle, polIndex);
			
			if (!isLogged()) {
				LoginPanel loginPanel = new LoginPanel(this);
				String tabTitle = "Login";
				tabbedPane.addTab(tabTitle, null, loginPanel, null);		
				//int index = tabbedPane.indexOfTab(tabTitle);
				//tabbedPane.setTabComponentAt(index, createTabHead(tabTitle));
			} else {
				MainMenu mainPanel = new MainMenu(this);
				String tabTitle = "Meniu Principal";
				tabbedPane.addTab(tabTitle, null, mainPanel, null);		
				//int index = tabbedPane.indexOfTab(tabTitle);
				//tabbedPane.setTabComponentAt(index, createTabHead(tabTitle));
			}
		}
		
		JLabel lblcMedhealSystem = new JLabel();
		if (centerUsed != null) {
			lblcMedhealSystem.setText("\u00A9 2018, MEDHEAL SYSTEM, CENTRUL MEDICAL " + centerUsed.getDenumire());
		} else {
			lblcMedhealSystem.setText("\u00A9 2018, MEDHEAL SYSTEM");
		}
		lblcMedhealSystem.setFont(new Font("Ubuntu", Font.ITALIC, 16));
		lblcMedhealSystem.setHorizontalAlignment(SwingConstants.CENTER);
		lblcMedhealSystem.setBounds(0, 678, 1018, 29);
		mainFrame.getContentPane().add(lblcMedhealSystem);
	}
	
	public JPanel createTabHead(String tabTitle) {
	    JPanel headPanelTab = new JPanel();
	    headPanelTab.setLayout(new BoxLayout(headPanelTab, BoxLayout.LINE_AXIS));
	    headPanelTab.setOpaque(false);
	    JButton btnClose = new JButton("x");
	    JLabel lblTitle = new JLabel(tabTitle + "    ");
	    btnClose.setBorderPainted(false);
	    btnClose.setOpaque(false);
	    btnClose.setFont(new Font("Ubuntu", Font.PLAIN, 12));
	    btnClose.setBackground(new Color(0, 51, 204));
	    btnClose.setForeground(Color.BLACK);
	    btnClose.setFocusPainted(false);
	    btnClose.setBorderPainted(false);

	    btnClose.addActionListener(new ActionListener() {
	        @Override 
	    	public void actionPerformed(ActionEvent e) {
	        	int index = tabbedPane.indexOfTab(tabTitle);                  
	            tabbedPane.removeTabAt(index);
	            if (tabbedPane.getTabCount() == 0) {
	            	mainFrame.dispose();
	            }
	        }
        });

	    headPanelTab.add(lblTitle);
	    headPanelTab.add(btnClose);
	    return headPanelTab;
	}
	
	public boolean isLogged() {
		return (userLogged != null);
	}
	
	public boolean isTabOpened(String tabTitle) {
		int index = tabbedPane.indexOfTab(tabTitle);
		if (index == -1)
			return false;
		
		tabbedPane.setSelectedIndex(index);
		return true;
	}
	
	public void addNewTab(JPanel panel, String tabTitle) {
		int tabCount = tabbedPane.getTabCount();
		if (tabCount >= 5)
			JOptionPane.showMessageDialog(mainFrame, "Deschiderea a mai mult de 5 file simultan poate provoca erori vizuale.", "Avertisment", JOptionPane.WARNING_MESSAGE);
		
		tabbedPane.addTab(tabTitle, null, panel, null);		
		int index = tabbedPane.indexOfTab(tabTitle);
		tabbedPane.setTabComponentAt(index, createTabHead(tabTitle));		
		tabbedPane.setSelectedIndex(index);
		
	}
}
