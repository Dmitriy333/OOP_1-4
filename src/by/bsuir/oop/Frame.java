package by.bsuir.oop;

import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;

import javax.swing.UIManager;

import simple_figure.Shape;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;

import by.bsuir.filefilter.Filter;
import java.awt.event.WindowStateListener;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Frame {

	private JFrame frame;
	private Point firstPoint, secondPoint;
	private String currentFigure = "line";
	private boolean pressed = false;
	private List<Shape> shapes = new ArrayList<Shape>();
	private Map<String, Shape> choiceMap = new HashMap<String, Shape>();
	private JPanel drawPanel;
	private JComboBox<Object> comboBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame window = new Frame();
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
	public Frame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				//repaintPanel(drawPanel);
			}
		});
		frame.setBounds(100, 100, 774, 503);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save");
				int userSelection = fileChooser.showSaveDialog(null);
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File fileToSave = fileChooser.getSelectedFile();
					FileOutputStream fos;
					try {
						fos = new FileOutputStream(fileToSave);
						try {
							ObjectOutputStream oos = new ObjectOutputStream(fos);
							for (Shape shape : shapes) {
								oos.writeObject(shape);
								oos.flush();
							}
							oos.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} catch (FileNotFoundException e2) {
						e2.printStackTrace();
					}
				}
			}
		});

		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shapes = new ArrayList<Shape>();
				drawPanel.repaint();
			}
		});
		mnFile.add(mntmNew);
		mnFile.add(mntmSave);

		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileopen = new JFileChooser();
				int ret = fileopen.showDialog(null, "Open file");
				if (ret == JFileChooser.APPROVE_OPTION) {
					File file = fileopen.getSelectedFile();
					try {
						FileInputStream fis = new FileInputStream(file);
						ObjectInputStream oin = new ObjectInputStream(fis);
						try {
							shapes = new ArrayList<Shape>();
							Shape shape;
							// check if the file stream is at the end
							while (oin.available() == 0) {
								Object obj = oin.readObject();
								if (obj != null) {
									shape = (Shape) obj;
									shapes.add(shape);
								} else {
									break;
								}
								// read from the object stream,

								// which wraps the file stream

							}
							oin.close();
						} catch (ClassNotFoundException e1) {
							//e1.printStackTrace();
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				for (Shape shape : shapes) {
					shape.draw(drawPanel.getGraphics());
				}
			}
		});
		mnFile.add(mntmOpen);

		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);

		JMenu mnFigures = new JMenu("Figures");
		mnOptions.add(mnFigures);

		JMenuItem mntmLine = new JMenuItem("Line");
		mntmLine.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				currentFigure = "Line";
			}
		});
		mnFigures.add(mntmLine);

		JMenuItem mntmRectangle = new JMenuItem("Rectangle");
		mntmRectangle.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				currentFigure = "Rectangle";
			}
		});
		mnFigures.add(mntmRectangle);

		JMenuItem mntmEllipse = new JMenuItem("Ellipse");
		mntmEllipse.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				currentFigure = "Ellipse";
			}
		});
		mnFigures.add(mntmEllipse);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		comboBox = new JComboBox<Object>();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentFigure = (String)comboBox.getSelectedItem();
			}
		});
		currentFigure = initComboBox(comboBox, "Jar files");
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 556, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
					.addGap(30))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 356, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(26)
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(76, Short.MAX_VALUE))
		);
				panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
				drawPanel = new JPanel();
				panel.add(drawPanel);
				drawPanel.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						firstPoint = e.getPoint();
						pressed = true;
					}
					@Override
					public void mouseReleased(MouseEvent e) {
						Class<Shape> clazz;
						Constructor<?> ctor;
						Object object;
						secondPoint = e.getPoint();
						if (pressed) {
							Shape figure = null;
							String firstWord = currentFigure.replaceAll("\\..*","");
							currentFigure = firstWord;
								try{
									clazz = (Class<Shape>) Class.forName(currentFigure);
									ctor = clazz.getConstructor(Point.class, Point.class);
									object = ctor.newInstance(firstPoint, secondPoint);
									figure = (Shape)object;
									shapes.add(figure);
									repaintPanel(drawPanel);
								}catch(Exception e1){
									JOptionPane.showMessageDialog(null, "Cannot draw such figure",
											"Error", 1);
								}
								
								pressed = false;
						}
					}
				});
		frame.getContentPane().setLayout(groupLayout);
	}

	private void repaintPanel(JPanel panel) {
		panel.getGraphics()
				.clearRect(0, 0, panel.getWidth(), panel.getHeight());
		for (Shape shape : shapes) {
			shape.draw(panel.getGraphics());
		}
	}
	
	private String initComboBox(JComboBox<Object> comboBox, String pathName){
		File []fList;        
		File F = new File(pathName);
		Filter filter = new Filter();        
		fList = F.listFiles();
		List<String> figures = new ArrayList<>();
		for(int i=0; i<fList.length; i++)           
		{
		     if(filter.accept(fList[i])){
		    	 figures.add( fList[i].getName());
		     }
		}
		comboBox.setModel(new DefaultComboBoxModel<Object>(figures.toArray()));
		return figures.get(0);
	}
	
}
