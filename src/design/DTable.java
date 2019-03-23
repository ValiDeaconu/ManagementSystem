package design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import application.Main;

@SuppressWarnings("serial")
public class DTable extends JPanel {	
	private Main window; // application
	
	// Table utilities
	private JTable table;
	
	// Footer utilities
	private JLabel lblWarning;
	private JButton addBtn;
	private JButton removeBtn;
	private JButton refreshBtn;
	
	public DTable(Main window, String title) {
		super();
		this.setLayout(new BorderLayout());

		this.window = window;
		
		JPanel header = new JPanel();
		header.setLayout(new GridLayout(0, 1));
		
		JLabel lblTitle = new JLabel(title);
		lblTitle.setFont(new Font("Ubuntu", Font.PLAIN, 42));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		header.add(lblTitle);
		
		JSeparator headSeparator = new JSeparator();
		headSeparator.setForeground(new Color(70, 130, 180));
		header.add(headSeparator);
		
		this.add(header, BorderLayout.PAGE_START);
		
		JPanel body = new JPanel();
		body.setLayout(new BorderLayout());
	
		table = new JTable();
		
		JScrollPane tableScroll = new JScrollPane(table);
		
		body.add(tableScroll);
		
		this.add(body, BorderLayout.CENTER);
	
		JPanel footer = new JPanel();
		footer.setLayout(new GridLayout(0, 2));
		
		lblWarning = new JLabel("* Warning!");
		lblWarning.setFont(new Font("Ubuntu", Font.ITALIC, 14));
		
		footer.add(lblWarning);
		
		JPanel footerButtons = new JPanel();
		footerButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		addBtn = new JButton("Adaugă");
		addBtn.setIcon(new ImageIcon("resources/add_icon.png"));
		addBtn.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tableInsert();
			}			
		});
		footerButtons.add(addBtn);
		
		removeBtn = new JButton("Șterge");
		removeBtn.setIcon(new ImageIcon("resources/delete_icon.png"));
		removeBtn.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		removeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tableRemove();
			}			
		});
		footerButtons.add(removeBtn);

		refreshBtn = new JButton("Refresh");
		refreshBtn.setIcon(new ImageIcon("resources/refresh_icon.png"));
		refreshBtn.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		refreshBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tableUpdate();
			}			
		});
		footerButtons.add(refreshBtn);
		
		footer.add(footerButtons);	
		
		this.add(footer, BorderLayout.PAGE_END);
	}
	
	public JTable getTable() {
		return table;
	}
	
	public void setWarning(String warning) {
		lblWarning.setText("* " + warning);
	}
	
	public void setWarningVisibile(boolean visibile) {
		lblWarning.setVisible(visibile);
	}
	
	public void setAddButtonVisibile(boolean visibile) {
		addBtn.setVisible(visibile);
	}
	public void setRemoveButtonVisibile(boolean visibile) {
		removeBtn.setVisible(visibile);
	}

	public Main getWindow() {
		return window;
	}
	
	public void tableUpdate() {
		// Must be overrided
	}
	
	public void tableInsert() {
		// Must be overrided
	}
	
	public void tableRemove() {
		// Must be overrided
	}
}
