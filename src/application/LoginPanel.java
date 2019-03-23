/**
 * Modul pentru logare
 */
package application;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import tables.Angajat;

/**
 * @author Vali
 *
 */
public class LoginPanel extends JPanel {
	private static final long serialVersionUID = -4562832801409304266L;
	
	public LoginPanel(Main window) {
		super();
		this.setLayout(null);
		JLabel lblIcon = new JLabel("");
		lblIcon.setBounds(780, 50, 300, 150);
		lblIcon.setIcon(new ImageIcon("resources/73150-medical-symbol-icon.png"));
		this.add(lblIcon);
		
		JLabel lblLogin = new JLabel("Panou de logare");
		lblLogin.setIcon(new ImageIcon("resources/secrecy-icon.png"));
		lblLogin.setFont(new Font("Ubuntu", Font.BOLD, 46));
		lblLogin.setBounds(300, 116, 450, 49);
		this.add(lblLogin);
		
		JTextField usernameField = new JTextField();
		usernameField.setToolTipText("Introdu username-ul contului");
		usernameField.setFont(new Font("Ubuntu", Font.BOLD, 30));
		usernameField.setBounds(353, 295, 287, 49);
		this.add(usernameField);
		usernameField.setColumns(10);
		
		JPasswordField passwordField = new JPasswordField();
		passwordField.setToolTipText("Introdu parola contului");
		passwordField.setFont(new Font("Ubuntu", Font.BOLD, 30));
		passwordField.setBounds(353, 409, 287, 49);
		this.add(passwordField);
		
		JLabel lblPassword = new JLabel("Parola:");
		lblPassword.setLabelFor(passwordField);
		lblPassword.setFont(new Font("Ubuntu", Font.PLAIN, 15));
		lblPassword.setBounds(353, 378, 79, 18);
		this.add(lblPassword);
		
		JLabel lblUsername = new JLabel("Nume de utilizator:");
		lblUsername.setLabelFor(usernameField);
		lblUsername.setFont(new Font("Ubuntu", Font.PLAIN, 15));
		lblUsername.setBounds(353, 264, 139, 18);
		this.add(lblUsername);
		
		JButton btnSubmit = new JButton("Login");
		btnSubmit.setBackground(SystemColor.menu);
		btnSubmit.setIcon(new ImageIcon("resources/Webp.net-resizeimage (1).png"));
		btnSubmit.setFont(new Font("Ubuntu", Font.PLAIN, 20));
		btnSubmit.setBounds(353, 519, 287, 33);
		btnSubmit.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
            public void actionPerformed(ActionEvent e) {
            	String query = "SELECT id FROM angajati WHERE username = '" + usernameField.getText() + "'";
            	ArrayList<String[]> result = window.dbHandle.doQuery(query);
            	if (result != null && result.size() == 1) {
            		int id = Integer.parseInt((result.get(0))[0]);
            		Angajat user = new Angajat(window.dbHandle, id);
            		
            		if (user.isPasswordCorrect(window.dbHandle, passwordField.getText())) {
            			window.userLogged = user;
            			window.initialize();
            		} else {
            			JOptionPane.showMessageDialog(window.mainFrame, "Nume de utilizator sau parola sunt incorecte!", "Eroare", JOptionPane.ERROR_MESSAGE);
            		}            		
            	} else {
            		JOptionPane.showMessageDialog(window.mainFrame, "Nume de utilizator sau parola sunt incorecte!", "Eroare", JOptionPane.ERROR_MESSAGE);
            	}
            }
		});
		this.add(btnSubmit);
		
		JSeparator separator = new JSeparator();
		separator.setBackground(new Color(70, 130, 180));
		separator.setBounds(98, 208, 810, 2);
		this.add(separator);
	}	
}
