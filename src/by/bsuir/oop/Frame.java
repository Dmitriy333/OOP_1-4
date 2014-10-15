package by.bsuir.oop;

import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

import javax.swing.BoxLayout;
import javax.swing.JComboBox;

import org.reflections.Reflections;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;



// ������� ��  ���� ������ �����, ������� ���������������� � ��������� ClassLoader
public class Frame {

	private JFrame frame;
	private Point firstPoint, secondPoint;
	private String currentFigure = "line";
	private boolean pressed = false;
	private List<Shape> shapes = new ArrayList<Shape>();
	private JPanel drawPanel;
	private JComboBox<Object> comboBox;
	private List<Point> points = new ArrayList<Point>();

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
				points = null;
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
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		comboBox = new JComboBox<Object>();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentFigure = (String)comboBox.getSelectedItem();
			}
		});
		currentFigure = initComboBox(comboBox);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 592, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(comboBox, 0, 138, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 396, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(36, Short.MAX_VALUE))
		);
				panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
				drawPanel = new JPanel();
				panel.add(drawPanel);
				drawPanel.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						firstPoint = e.getPoint();
						if(points == null){
							points = new ArrayList<Point>();
						}
						pressed = true;
					}
					@SuppressWarnings("unchecked")
					@Override
					public void mouseReleased(MouseEvent e) {
						Class<Shape> clazz;
						Constructor<Shape> ctor;
						Object object;
						secondPoint = e.getPoint();
						if (pressed) {
							Shape figure = null;
								try{
									clazz = (Class<Shape>) Class.forName(currentFigure);
									ctor = clazz.getConstructor(List.class);
									object = ctor.newInstance(points);
									figure = (Shape)object;
									if(figure.complexFigure){
										if(SwingUtilities.isRightMouseButton(e)){
											pressed = false;
											figure.addPoints(points);
											//add last variant of polyline in collection of shapes
											shapes.add(figure);
											points = null;
										}else{
											points.add(secondPoint);
											figure.addPoints(points);
											//draw polyline in intermediate values 
											figure.draw(drawPanel.getGraphics());
											pressed = true;
										}
									}else{
										points.add(firstPoint);
										points.add(secondPoint);
										figure.addPoints(points);
										shapes.add(figure);
										figure.draw(drawPanel.getGraphics());
										repaintPanel(drawPanel);
										points = null;
										pressed = false;
									}
								}catch(Exception e1){
									JOptionPane.showMessageDialog(null, "Cannot draw such figure",
											"Error", 1);
									points = null;
								}
								
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
	
	private String initComboBox(JComboBox<Object> comboBox){
		List<String> figures = new ArrayList<>();
		Reflections reflections = new Reflections("by.bsuir.figures");
		Set<Class<? extends Shape>> subTypes = reflections.getSubTypesOf(Shape.class);
		for (Class<? extends Shape> class1 : subTypes) {
			figures.add(class1.getName());
			System.out.println(class1.getName());
		}
		comboBox.setModel(new DefaultComboBoxModel<Object>(figures.toArray()));
		return figures.get(0);
	}
	
}
