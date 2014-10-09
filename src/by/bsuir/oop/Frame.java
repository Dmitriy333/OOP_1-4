package by.bsuir.oop;

import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;







//import by.bsuir.shape.Ellipse;
import by.bsuir.shape.Shape;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Frame {

	private JFrame frame;
	private Point firstPoint, secondPoint;
	private String currentFigure = "line";
	private boolean pressed = false;
	private List<Shape> shapes = new ArrayList<Shape>();
	private Map<String, Shape> choiceMap = new HashMap<String, Shape>();
	private JPanel panel;

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
		frame.addMouseListener(new MouseAdapter() {
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
					switch (currentFigure) {
					case "line":
						try{
							clazz = (Class<Shape>) Class.forName("by.bsuir.line.Line");
							ctor = clazz.getConstructor(Point.class, Point.class);
							object = ctor.newInstance(firstPoint, secondPoint);
							figure = (Shape)object;
						}catch(Exception e1){
							e1.printStackTrace();
						}
						break;
					case "rectangle":
						try{
							clazz = (Class<Shape>) Class.forName("by.bsuir.rectangle.Rectangle");
							ctor = clazz.getConstructor(Point.class, Point.class);
							object = ctor.newInstance(firstPoint, secondPoint);
							figure = (Shape)object;
						}catch(Exception e1){
							e1.printStackTrace();
						}
						//figure = new Rectangle(firstPoint, secondPoint);
						break;
					case "ellipse":
						//figure = new Ellipse(firstPoint, secondPoint);
						try{
							clazz = (Class<Shape>) Class.forName("by.bsuir.ellipse.Ellipse");
							ctor = clazz.getConstructor(Point.class, Point.class);
							object = ctor.newInstance(firstPoint, secondPoint);
							figure = (Shape)object;
						}catch(Exception e1){
							e1.printStackTrace();
						}
					
						//Object object = createInstance("by.bsuir.shape.Ellipse","MyAttributeValue");
//						try {
//							figure = (Shape) Class.forName("by.bsuir.shape.Ellipse").newInstance();
//						} catch (InstantiationException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						} catch (IllegalAccessException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						} catch (ClassNotFoundException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
						break;
					}
					shapes.add(figure);
					repaintPanel(panel);
					pressed = false;
				}
			}
		});

		frame.setBounds(100, 100, 632, 431);
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
				panel.repaint();
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
							// TODO Auto-generated catch block
							//e1.printStackTrace();
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				for (Shape shape : shapes) {
					shape.draw(panel.getGraphics());
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
				currentFigure = "line";
			}
		});
		mnFigures.add(mntmLine);

		JMenuItem mntmRectangle = new JMenuItem("Rectangle");
		mntmRectangle.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				currentFigure = "rectangle";
			}
		});
		mnFigures.add(mntmRectangle);

		JMenuItem mntmEllipse = new JMenuItem("Ellipse");
		mntmEllipse.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				currentFigure = "ellipse";
			}
		});
		mnFigures.add(mntmEllipse);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
	}

	public void repaintPanel(JPanel panel) {
		panel.getGraphics()
				.clearRect(0, 0, panel.getWidth(), panel.getHeight());
		// frame.getGraphics().draw
		for (Shape shape : shapes) {
			shape.draw(frame.getGraphics());
		}
	}
}
