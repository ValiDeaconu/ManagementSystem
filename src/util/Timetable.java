/**
 * 
 */
package util;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * @author Vali
 *
 */
public class Timetable {
	private JSeparator[] vert = new JSeparator[4];
	private JSeparator[] oriz = new JSeparator[9];
	
	private JLabel[] rowTitle = new JLabel[7];
	private JLabel[] colTitle = new JLabel[2];
	
	private JPanel panel;
	
	//JLabel[][] content = new JLabel[7][2];
	private JComponent[][] content = new JComponent[7][2];
	
	private JLabel lblNoContent;
	
	public Timetable(JPanel master, boolean editable) {
		lblNoContent = new JLabel("Nu a fost găsit niciun orar.");
		lblNoContent.setForeground(Color.RED);
		lblNoContent.setHorizontalAlignment(SwingConstants.CENTER);
		lblNoContent.setFont(new Font("Ubuntu", Font.PLAIN, 24));
		lblNoContent.setBounds(12, 276, 989, 49);
		master.add(lblNoContent);
		
		this.panel = master;
		
		buildTable();
		buildTitles();
		buildContent(editable);
	}
	
	private void buildTable() {
		vert[0] = new JSeparator();
		vert[0].setOrientation(SwingConstants.VERTICAL);
		vert[0].setForeground(Color.GRAY);
		vert[0].setBackground(new Color(70, 130, 180));
		vert[0].setBounds(234, 198, 2, 352);
		panel.add(vert[0]);
		
		vert[1] = new JSeparator();
		vert[1].setOrientation(SwingConstants.VERTICAL);
		vert[1].setForeground(new Color(128, 128, 128));
		vert[1].setBackground(new Color(70, 130, 180));
		vert[1].setBounds(361, 148, 2, 402);
		panel.add(vert[1]);
		
		vert[2] = new JSeparator();
		vert[2].setOrientation(SwingConstants.VERTICAL);
		vert[2].setForeground(Color.GRAY);
		vert[2].setBackground(new Color(70, 130, 180));
		vert[2].setBounds(575, 148, 2, 402);
		panel.add(vert[2]);
		
		vert[3] = new JSeparator();
		vert[3].setOrientation(SwingConstants.VERTICAL);
		vert[3].setForeground(Color.GRAY);
		vert[3].setBackground(new Color(70, 130, 180));
		vert[3].setBounds(792, 148, 2, 402);
		panel.add(vert[3]);
		
		oriz[0] = new JSeparator();
		oriz[0].setForeground(Color.GRAY);
		oriz[0].setBackground(new Color(70, 130, 180));
		oriz[0].setBounds(361, 148, 433, 2);
		panel.add(oriz[0]);
		
		oriz[1] = new JSeparator();
		oriz[1].setForeground(Color.GRAY);
		oriz[1].setBackground(new Color(70, 130, 180));
		oriz[1].setBounds(234, 198, 560, 2);
		panel.add(oriz[1]);
		
		oriz[2] = new JSeparator();
		oriz[2].setForeground(Color.GRAY);
		oriz[2].setBackground(new Color(70, 130, 180));
		oriz[2].setBounds(234, 248, 560, 2);
		panel.add(oriz[2]);
		
		oriz[3] = new JSeparator();
		oriz[3].setForeground(Color.GRAY);
		oriz[3].setBackground(new Color(70, 130, 180));
		oriz[3].setBounds(234, 298, 560, 2);
		panel.add(oriz[3]);
		
		oriz[4] = new JSeparator();
		oriz[4].setForeground(Color.GRAY);
		oriz[4].setBackground(new Color(70, 130, 180));
		oriz[4].setBounds(234, 348, 560, 2);
		panel.add(oriz[4]);
		
		oriz[5] = new JSeparator();
		oriz[5].setForeground(Color.GRAY);
		oriz[5].setBackground(new Color(70, 130, 180));
		oriz[5].setBounds(234, 398, 560, 2);
		panel.add(oriz[5]);
		
		oriz[6] = new JSeparator();
		oriz[6].setForeground(Color.GRAY);
		oriz[6].setBackground(new Color(70, 130, 180));
		oriz[6].setBounds(234, 448, 560, 2);
		panel.add(oriz[6]);
		
		oriz[7] = new JSeparator();
		oriz[7].setForeground(Color.GRAY);
		oriz[7].setBackground(new Color(70, 130, 180));
		oriz[7].setBounds(234, 498, 560, 2);
		panel.add(oriz[7]);
		
		oriz[8] = new JSeparator();
		oriz[8].setForeground(Color.GRAY);
		oriz[8].setBackground(new Color(70, 130, 180));
		oriz[8].setBounds(234, 548, 560, 2);
		panel.add(oriz[8]);
	}
	
	private void buildTitles() {
		rowTitle[0] = new JLabel("Luni:");
		rowTitle[0].setHorizontalAlignment(SwingConstants.CENTER);
		rowTitle[0].setFont(new Font("Ubuntu", Font.PLAIN, 24));
		rowTitle[0].setBounds(234, 198, 129, 52);
		panel.add(rowTitle[0]);
		
		rowTitle[1] = new JLabel("Marți:");
		rowTitle[1].setHorizontalAlignment(SwingConstants.CENTER);
		rowTitle[1].setFont(new Font("Ubuntu", Font.PLAIN, 24));
		rowTitle[1].setBounds(234, 248, 129, 52);
		panel.add(rowTitle[1]);
		
		rowTitle[2] = new JLabel("Miercuri:");
		rowTitle[2].setHorizontalAlignment(SwingConstants.CENTER);
		rowTitle[2].setFont(new Font("Ubuntu", Font.PLAIN, 24));
		rowTitle[2].setBounds(234, 298, 129, 52);
		panel.add(rowTitle[2]);
		
		rowTitle[3] = new JLabel("Joi:");
		rowTitle[3].setHorizontalAlignment(SwingConstants.CENTER);
		rowTitle[3].setFont(new Font("Ubuntu", Font.PLAIN, 24));
		rowTitle[3].setBounds(234, 348, 129, 52);
		panel.add(rowTitle[3]);
		
		rowTitle[4] = new JLabel("Vineri:");
		rowTitle[4].setHorizontalAlignment(SwingConstants.CENTER);
		rowTitle[4].setFont(new Font("Ubuntu", Font.PLAIN, 24));
		rowTitle[4].setBounds(234, 398, 129, 52);
		panel.add(rowTitle[4]);
		
		rowTitle[5] = new JLabel("Sâmbătă:");
		rowTitle[5].setHorizontalAlignment(SwingConstants.CENTER);
		rowTitle[5].setFont(new Font("Ubuntu", Font.PLAIN, 24));
		rowTitle[5].setBounds(234, 448, 129, 52);
		panel.add(rowTitle[5]);
		
		rowTitle[6] = new JLabel("Duminică:");
		rowTitle[6].setHorizontalAlignment(SwingConstants.CENTER);
		rowTitle[6].setFont(new Font("Ubuntu", Font.PLAIN, 24));
		rowTitle[6].setBounds(234, 498, 129, 52);
		panel.add(rowTitle[6]);
		
		colTitle[0] = new JLabel("De la:");
		colTitle[0].setHorizontalAlignment(SwingConstants.CENTER);
		colTitle[0].setFont(new Font("Ubuntu", Font.PLAIN, 24));
		colTitle[0].setBounds(361, 148, 216, 52);
		panel.add(colTitle[0]);
		
		colTitle[1] = new JLabel("Până la:");
		colTitle[1].setHorizontalAlignment(SwingConstants.CENTER);
		colTitle[1].setFont(new Font("Ubuntu", Font.PLAIN, 24));
		colTitle[1].setBounds(578, 148, 216, 52);
		panel.add(colTitle[1]);
	}
	
	private void buildContent(boolean editable) {
		if (editable) {
			for (int i = 0; i < 7; ++i) {
				content[i][0] = new JSpinner();
				((JSpinner) content[i][0]).setModel(new SpinnerNumberModel(0, 0, 24, 1));
				content[i][0].setFont(new Font("Ubuntu", Font.PLAIN, 24));
				content[i][0].setBounds(361, 198 + 50 * i, 216, 52);
				panel.add(content[i][0]);
			}
			
			for (int i = 0; i < 7; ++i) {				
				content[i][1] = new JSpinner();
				((JSpinner) content[i][1]).setModel(new SpinnerNumberModel(0, 0, 24, 1));
				content[i][1].setFont(new Font("Ubuntu", Font.PLAIN, 24));
				content[i][1].setBounds(578, 198 + 50 * i, 216, 52);
				panel.add(content[i][1]);
				
			}
		} else {
			for (int i = 0; i < 7; ++i) {
				content[i][0] = new JLabel();
				((JLabel) content[i][0]).setHorizontalAlignment(SwingUtilities.CENTER);
				content[i][0].setFont(new Font("Ubuntu", Font.PLAIN, 24));
				content[i][0].setBounds(361, 198 + 50 * i, 216, 52);
				panel.add(content[i][0]);
			}
			
			for (int i = 0; i < 7; ++i) {				
				content[i][1] = new JLabel();
				((JLabel) content[i][1]).setHorizontalAlignment(SwingUtilities.CENTER);
				content[i][1].setFont(new Font("Ubuntu", Font.PLAIN, 24));
				content[i][1].setBounds(578, 198 + 50 * i, 216, 52);
				panel.add(content[i][1]);
				
			}
		}
	}	
	
	public void setMarkedRow(int row) {
		for (int i = 0; i < 7; ++i) {
			if (i == row) {
				rowTitle[i].setForeground(new Color(165, 42, 42));
				content[i][0].setForeground(new Color(165, 42, 42));
				content[i][1].setForeground(new Color(165, 42, 42));
			} else {
				rowTitle[i].setForeground(null);
				content[i][0].setForeground(null);
				content[i][1].setForeground(null);				
			}
		}
	}
	
	public void setContent(int row, int col, String content) {
		if (this.content[row][col] instanceof JLabel) {
			((JLabel) this.content[row][col]).setText(content);
		}
	}
	

	public void setContent(int row, int col, int content) {
		if (this.content[row][col] instanceof JSpinner) {
			((JSpinner) this.content[row][col]).setValue(content);
		}
	}
	
	public Program getContentAsProgram() {
		if (content[0][0] instanceof JSpinner) {
			Program p = new Program();
			
			for (int i = 0; i < 7; ++i) {
				JSpinner spI = (JSpinner) content[i][0];
				JSpinner spF = (JSpinner) content[i][1];
				
				int v1 = ((int) spI.getValue());
				int v2 = ((int) spF.getValue());
				if (v1 == v2) {
					p.set(i, new OraProgram(0, 0, false));
				} else if (v1 > v2) {
					p.set(i, new OraProgram(0, 0, false));
				} else {
					p.set(i, new OraProgram(v1, v2, true));
				}
			}
			
			return p;
		}
		
		return null;
	}
	public void setRowTitle(int row, String title) {
		this.rowTitle[row].setText(title);
	}
	
	public void setColumnTitle(int col, String title) {
		this.colTitle[col].setText(title);
	}
	
	public void setVisible(boolean visible) {
		for (int i = 0; i < 4; ++i)
			vert[i].setVisible(visible);
		for (int i = 0; i < 9; ++i)
			oriz[i].setVisible(visible);
		
		for (int i = 0; i < 7; ++i)
			rowTitle[i].setVisible(visible);
		
		for (int i = 0; i < 2; ++i)
			colTitle[i].setVisible(visible);
		
		for (int i = 0; i < 7; ++i)
			for (int j = 0; j < 2; ++j)
				content[i][j].setVisible(visible);
		
		lblNoContent.setVisible(!visible);
	}
}
