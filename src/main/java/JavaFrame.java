import java.awt.*;
import java.awt.event.*;
import java.net.URI;

import javax.swing.*;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * <h1>JavaFrame</h1> Creates JFrame (windowed) components, simple GUI
 * 
 *
 * @author Tommy Yasi (toya1800)
 * @version 1.0
 * @since 2019-12-31
 */

public class JavaFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	// Color buttons
	private JButton button1 = new JButton();
	private JButton button2 = new JButton();
	private JButton button3 = new JButton();
	private JButton button4 = new JButton();
	private JButton button5 = new JButton();
	private JButton button6 = new JButton();
	
	JMenuItem jmiClear = new JMenuItem("Clear");
	JMenuItem jmiHost = new JMenuItem("Host");
	JMenuItem jmiConnect = new JMenuItem("Connect");
	JMenuItem jmiExit = new JMenuItem("Exit");
	
	
	// Current selected color as string
	private String selectedColor = "";
	
	// Shape to be drawn
	private Shape newShape;

	// Box shows color selected
	private JPanel tinySquare = null;
	
	// Rectangle or Circle selected as shape
	String[] shapes = { "Rectangle", "Circle", "Straight line", "Freehand" };
	JComboBox<String> shapeList = new JComboBox<String>(shapes);
	
	// Choose thickness
	String[] thickness = { "Thickness: 1", "Thickness: 2", "Thickness: 3", "Thickness: 4", "Thickness: 5" };
	JComboBox<String> thicknessList = new JComboBox<String>(thickness);
	
	// Drawing area
	private DrawingPanel drawArea = new DrawingPanel();

	// Mouse position for mouse movement
	private String fullCoordinates = 0 + ", " + 0;
	private JLabel textCoord = new JLabel(fullCoordinates); // Used to update coordinates
	
	// Client - Server part
	private Client client;
	private Server server;
	
	// Mouse behavior
    Disposable mouseMoved = mouseMoved()
    		.map(me -> me.getX() + ", " + me.getY())
			.subscribe(textCoord::setText);
    
    Disposable mousePressed = mousePressed()
    		.filter(e -> !selectedColor.equals(""))
    		.subscribe(e -> buildShape(e));
    
    Disposable mouseDragged = mouseDragged()
    		.filter(e -> !selectedColor.equals(""))
    		.map(e-> drawShape(e))
    		.observeOn(Schedulers.computation())
    		.subscribe(drawArea::addShape);
    
    Disposable mouseReleased = mouseReleased() 
    		.filter(e -> !selectedColor.equals(""))
    		.subscribe(this::sendShape); // Send shape if you are connected
    
    // Action events
    Disposable clearDrawing = clearDrawing()
    		.map(e -> newDrawArea())
    		.subscribe(drawArea::setDrawing);
    
    Disposable exitProgram = exitProgram()
    		.subscribe(e -> System.exit(0));
    
    Disposable startHost = hostButton()
    		.subscribe(e -> startHost());
    
    Disposable startConnect = connectButton()
    		.subscribe(e -> startConnect());
    
    
    // Color selection
    Disposable blackColor = blackColor()
    		.map(e -> selectedColor = "black")
    		.subscribe(color -> changeColor(color));
    
    Disposable whiteColor = whiteColor()
    		.map(e -> selectedColor = "white")
    		.subscribe(color -> changeColor(color));
    
    Disposable greenColor = greenColor()
    		.map(e -> selectedColor = "green")
    		.subscribe(color -> changeColor(color));
    
    
    Disposable blueColor = blueColor()
    		.map(e -> selectedColor = "blue")
    		.subscribe(color -> changeColor(color));
    
    Disposable redColor = redColor()
    		.map(e -> selectedColor = "red")
    		.subscribe(color -> changeColor(color));
    
    Disposable yellowColor = yellowColor()
    		.map(e -> selectedColor = "yellow")
    		.subscribe(color -> changeColor(color));

	// Constructor
	public JavaFrame() {
		setTitle("JavaPaintDemo");

		// Close window
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Set icon
		ImageIcon img = new ImageIcon(JavaFrame.class.getResource("paint-logo.png"));
		setIconImage(img.getImage());

		// Size and location
		setLocationRelativeTo(null); // Position it in the middle
		
		createMenuBar();
		createAndAddContent();
		pack(); // Sets size of frame based on components
	}

	private JPanel createStatusRow() {
		// Shows what color is active
		tinySquare = new JPanel(new BorderLayout());
		tinySquare.setMaximumSize(new Dimension(20, 20));
		tinySquare.setPreferredSize(new Dimension(20, 20));

		JLabel coordinates = new JLabel("Coordinates: ", JLabel.LEFT);
		JLabel color = new JLabel("Selected color: ", JLabel.RIGHT);
		coordinates.setFont(new Font(coordinates.getFont().getFontName(), Font.BOLD, 15));
		color.setFont(new Font(color.getFont().getFontName(), Font.BOLD, 15));
		textCoord.setFont(new Font(textCoord.getFont().getFontName(), Font.BOLD, 15));

		// Show text "Selected" and color box
		JPanel colorPanel = new JPanel(new BorderLayout());
		colorPanel.add(color, BorderLayout.LINE_START);
		colorPanel.add(tinySquare, BorderLayout.LINE_END);

		// coordinate panel, show text "Coordinates" and numbers
		JPanel coordinatesPanel = new JPanel(new BorderLayout());
		coordinatesPanel.add(coordinates, BorderLayout.LINE_START);
		coordinatesPanel.add(textCoord, BorderLayout.LINE_END);

		JPanel statusBar = new JPanel(new BorderLayout());
		statusBar.add(coordinatesPanel, BorderLayout.LINE_START);
		statusBar.add(colorPanel, BorderLayout.LINE_END);

		JPanel fullDrawPanel = new JPanel(new BorderLayout());
		fullDrawPanel.add(statusBar, BorderLayout.PAGE_END);

		return fullDrawPanel;
	}

	private JPanel createToolRow() {
		// Create 5 color buttons
		button1.setBackground(Color.black);
		button2.setBackground(Color.white);
		button3.setBackground(Color.green);
		button4.setBackground(Color.blue);
		button5.setBackground(Color.red);
		button6.setBackground(Color.yellow);

		JPanel colorPanel = new JPanel(new GridLayout(1, 0));
		colorPanel.add(button1);
		colorPanel.add(button2);
		colorPanel.add(button3);
		colorPanel.add(button4);
		colorPanel.add(button5);
		colorPanel.add(button6);

		// Box were user chooses shape
		shapeList.setPreferredSize(new Dimension(100, 50));
		shapeList.setMaximumSize(new Dimension(100, 50));

		JPanel comboBoxPanelColor = new JPanel(new BorderLayout());
		comboBoxPanelColor.add(shapeList);
		comboBoxPanelColor.setPreferredSize(new Dimension(100, 50));
		comboBoxPanelColor.setMaximumSize(new Dimension(100, 50));
		comboBoxPanelColor.setBackground(Color.CYAN);
		
		// Box were user chooses shape
		thicknessList.setPreferredSize(new Dimension(100, 50));
		thicknessList.setMaximumSize(new Dimension(100, 50));

		JPanel comboBoxPanelThickness = new JPanel(new BorderLayout());
		comboBoxPanelThickness.add(thicknessList);
		comboBoxPanelThickness.setPreferredSize(new Dimension(100, 50));
		comboBoxPanelThickness.setMaximumSize(new Dimension(100, 50));
		comboBoxPanelThickness.setBackground(Color.CYAN);

		JPanel fullPanel = new JPanel();
		fullPanel.setLayout(new BoxLayout(fullPanel, BoxLayout.LINE_AXIS));
		fullPanel.add(colorPanel);
		fullPanel.add(comboBoxPanelColor);
		fullPanel.add(comboBoxPanelThickness);
		return fullPanel;
	}

	private void createAndAddContent() {
		JPanel toolbar = createToolRow();
		add(toolbar, BorderLayout.PAGE_START);

		drawArea.setPreferredSize(new Dimension(600, 300));
		add(drawArea, BorderLayout.CENTER);

		JPanel statusbar = createStatusRow();
		add(statusbar, BorderLayout.PAGE_END);
	}
	
	private void createMenuBar() {
		// Menu bar
		JMenuBar menuBar = new JMenuBar();

		// File menu
		JMenu jmFile = new JMenu("File");

		// Add menu components to the File-menu
		jmFile.add(jmiClear);
		jmFile.addSeparator();
		jmFile.add(jmiHost);
		jmFile.add(jmiConnect);
		jmFile.addSeparator();
		jmFile.add(jmiExit);
		menuBar.add(jmFile); // Add to the menu bar

		// Add menu bar to the frame
		setJMenuBar(menuBar);
	}
	
	private int getThickness() {
		String thicknessLevel = String.valueOf(thicknessList.getSelectedItem()); // Get level of thickness
		int thickness = 0;
		switch(thicknessLevel) {
		case "Thickness: 1": thickness = 1;
		    break;
	    case "Thickness: 2": thickness = 2;
	        break;
	    case "Thickness: 3": thickness = 3;
	        break;
	    case "Thickness: 4": thickness = 4;
	        break;
	    case "Thickness: 5": thickness = 5;
	        break;
	    }
		
		return thickness;
	}
	
	private void buildShape(MouseEvent e) {
		String shapeType = String.valueOf(shapeList.getSelectedItem());
		
		switch(shapeType) {
		case "Rectangle": newShape = new Rectangle(e.getX(), e.getY(), selectedColor, getThickness());
		  break;
		case "Circle": newShape = new Circle(e.getX(), e.getY(), selectedColor, getThickness());
		  break;
		case "Straight line": newShape = new StraightLine(e.getX(), e.getY(), selectedColor, getThickness());
		  break;
		case "Freehand": newShape = new FreeHand(e.getX(), e.getY(), selectedColor, getThickness());
		  break;
		}
	}
	
	private Shape drawShape(MouseEvent e) {
		if(newShape != null) {
			newShape.addPoint(e.getX(), e.getY());	
		}
		return newShape;
	}
	
	private Drawing newDrawArea() {
		Drawing drawing = new Drawing();
		drawArea.clear();
		if(client != null) {
			client.send(drawArea.getDrawing());
		}
		return drawing;
	}
	
	private String changeColor(String color) {
		switch(color) {
		case "black":
			tinySquare.setBackground(Color.black);
			break;
		case "white":
			tinySquare.setBackground(Color.white);
			break;
		case "green":
			tinySquare.setBackground(Color.green);
			break;
		case "blue": 
			tinySquare.setBackground(Color.blue);
			break;
		case "red": 
			tinySquare.setBackground(Color.red);
			break;
		case "yellow":
			tinySquare.setBackground(Color.yellow);
			break;
		}
		
		return color;
	}
	
	private boolean startServer(int port) {
		server = new Server(port); // Start up the server
		
		if(server.startServer()) {
			/*
			 * Joining as a client to the server
			 * even if you are the host will make it easier
			 * to send and receive the data
			 */
			return startClient("localhost", port);
		} 
		return false; // Failed to start
	}
	
	private void startHost() {
		String userInput = "";
		int port = 10000;
		
		try {
			userInput = JOptionPane.showInputDialog("Port number: ", port);
			port = Integer.parseInt(userInput);
			
			// Now start and host the server
			if(!startServer(port)) {
				return;
			}
			
			// Disable other buttons to avoid errors and unpredictable behavior
			if(server != null 
					&& server.serverSocket != null 
					&& !server.serverSocket.isClosed()) {
				jmiHost.setEnabled(false);
				jmiConnect.setEnabled(false);
			} else {
				System.err.println("Server is null!");
			}
		} catch(NumberFormatException e) {
			System.err.println(e.getMessage());
			System.err.println("The input is not valid!");
		}
	}
	
	private boolean startClient(String host, int port) {
		// Clear the drawing area
		newDrawArea();
		
		// Start new client
		client = new Client(host, port);
		
		// If the client is connected get the objects, else return false
		if(client.socket != null && !client.socket.isClosed()) {
			// Get the objects
			client
			.receive()
			.filter(s -> s instanceof Drawing || s instanceof Shape) // Either it's a Drawing or a Shape that will be sent
			.subscribe(s -> {
				// Send all drawings to the drawing area
				if(s instanceof Drawing) {
					drawArea.setDrawing((Drawing) s); // Cast argument to Drawing object
				}
				
				// Send shapes to the drawing area
				if(s instanceof Shape) {
					drawArea.addShape((Shape) s); // Cast to Shape object
				}
			}
			, error -> {
				System.err.println("Error within startClient() function!");
				
				// Disconnect the client
				client.disconnect();
				
				// Clear the whole drawing area
				newDrawArea();
			});
			
			// Return success
			return true;
		}
		return false;
	}
	
	private void startConnect() {
		String userInput = "";
		
		/*
		 * Use URI variable.
		 * It is a sequence of characters that identifies
		 * an abstract or physical resource.
		 */
		URI uri;
		
		// Try to connect to the given location
		try {
			userInput = JOptionPane.showInputDialog("Input port number to connect to: ", "10000");
			
			// Create URI with input from user
			uri = new URI("http://localhost:" + userInput);
			
			// Check if the URI has a host before starting the client
			// Have to use both host and port so check if they are defined
			if(uri.getHost() != null && uri.getPort() != -1) {
				startClient(uri.getHost(), uri.getPort());
				// If the server is working then disable buttons to avoid errors
				if(server != null 
						&& server.serverSocket != null 
						&& !server.serverSocket.isClosed()) {
					jmiHost.setEnabled(false);
					jmiConnect.setEnabled(false);
				}
			} else {
				System.err.println("Cannot get host or port!");
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
			System.err.println("The URI you have typed is not valid!");
		}
	}
	
	private void sendShape(MouseEvent e) {
		if(newShape != null) {
			if(client != null) {
				if(client.socket != null && !client.socket.isClosed()) {
					client.send(newShape);
				}
			}
			newShape = null;
		}
	}
	
	private Observable<MouseEvent> mouseMoved() {
        return Observable.create(s -> {
            drawArea.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    s.onNext(e);    
                }
            });
        });
    }
	
	private Observable<MouseEvent> mouseDragged() {
        return Observable.create(s -> {
            drawArea.addMouseMotionListener(new MouseAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					s.onNext(e);
				}
            });
        });
    }
	
	private Observable<MouseEvent> mousePressed() {
        return Observable.create(s -> {
            drawArea.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					s.onNext(e);
				}
            });
        });
    }
	
	private Observable<MouseEvent> mouseReleased() {
		return Observable.create(s -> {
			drawArea.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					s.onNext(e);
				}
			});
		});
	}
	
	private Observable<ActionEvent> clearDrawing() {
		return Observable.create(s -> {
			jmiClear.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					s.onNext(e);
				}
				
			});
			
		});
	}
	
	private Observable<ActionEvent> exitProgram() {
		return Observable.create(s -> {
			jmiExit.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					s.onNext(e);
				}
				
			});
			
		});
	}
	
	private Observable<ActionEvent> hostButton(){
		return Observable.create(s -> {
			jmiHost.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					s.onNext(e);
				}
				
			});
		});
	}
	
	private Observable<ActionEvent> connectButton(){
		return Observable.create(s -> {
			jmiConnect.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					s.onNext(e);
				}
				
			});
		});
	}
	
	private Observable<ActionEvent> blackColor() {
		return Observable.create(s -> {
			button1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					s.onNext(e);
				}
				
			});
			
		});
	}
	
	private Observable<ActionEvent> whiteColor() {
		return Observable.create(s -> {
			button2.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					s.onNext(e);
				}
				
			});
			
		});
	}
	
	private Observable<ActionEvent> greenColor() {
		return Observable.create(s -> {
			button3.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					s.onNext(e);
				}
				
			});
			
		});
	}
	
	private Observable<ActionEvent> blueColor() {
		return Observable.create(s -> {
			button4.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					s.onNext(e);
				}
				
			});
			
		});
	}
	
	private Observable<ActionEvent> redColor() {
		return Observable.create(s -> {
			button5.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					s.onNext(e);
				}
				
			});
			
		});
	}
	
	private Observable<ActionEvent> yellowColor() {
		return Observable.create(s -> {
			button6.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					s.onNext(e);
				}
				
			});
			
		});
	}
	
}