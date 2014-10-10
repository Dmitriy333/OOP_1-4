package by.bsuir.oop;

import java.awt.EventQueue;
import java.io.File;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.DefaultComboBoxModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Form {

	private JFrame frame;
	private JComboBox<Object> comboBox;
	private JLabel label;
	private String currentFigure;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Form window = new Form();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Form() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 734, 459);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		
		comboBox = new JComboBox<Object>();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                 label.setText((String)comboBox.getSelectedItem());
			}
		});
		File []fList;        
		File F = new File("Jar files");
		        
		fList = F.listFiles();
		String [] figures = new String[fList.length];                
		for(int i=0; i<fList.length; i++)           
		{
		     if(fList[i].isFile()){
		    	 figures[i] = fList[i].getName();
		     }
		}
		currentFigure = figures[0];
		Arrays.sort(figures);
		comboBox.setModel(new DefaultComboBoxModel<Object>(figures));
		
		label = new JLabel(currentFigure);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(19)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 480, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(608, Short.MAX_VALUE)
					.addComponent(label)
					.addGap(55))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(19)
					.addComponent(label)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 314, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(67, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
