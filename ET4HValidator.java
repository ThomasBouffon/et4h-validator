import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.JTextField;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import java.awt.BorderLayout;
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent; 
import java.awt.event.KeyEvent;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom. NamedNodeMap;
import org.w3c.dom.Element;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class ET4HValidator implements Runnable {
	JTextField inputField=new JTextField(50);
	JPanel p1 = new JPanel ();
	JPanel p2 = new JPanel ();
	JFrame f = new JFrame ("ET4HValidator");
	JLabel resultMessage = new JLabel("Type a ticket # in the field above");
	String message;
	NodeList nList;
	Document doc;
	String InputFileName;
	private int loadXML(String filename) {
		try {
			File xmlFile= new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			nList = doc.getElementsByTagName("ticket");
			/*for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					NamedNodeMap namedNodeMap=nNode.getAttributes();
					System.out.println("Root element :" +  nNode.getNodeName());
					System.err.println(namedNodeMap.getNamedItem("id").getNodeValue());

				}
			}*/
		}
		catch (Exception e) {
			System.err.printf("Error while opening ");
			System.err.println(filename);
			e.printStackTrace();

			return 1;
		}
		return 0;
	}
	private void writeXML(String filename) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filename));
			transformer.transform(source, result);
	
		}
		catch (Exception e) {
			System.err.printf("Error while writing ");
			System.err.println(filename);
			e.printStackTrace();

		
		}
		 
		System.out.println("Done");
	}
	private void validate(String input) {
		String ImageName="ko.png";
		String Status="XX";
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				NamedNodeMap namedNodeMap=nNode.getAttributes();
				if(namedNodeMap.getNamedItem("id").getNodeValue().trim().equals(input)) {
					Status=namedNodeMap.getNamedItem("status").getNodeValue(); 
					System.out.println(Status);
					if (Status.equals("1")) {
						namedNodeMap.getNamedItem("status").setNodeValue("2");

					    System.out.println("1->2");writeXML(InputFileName);
					    System.out.println("1->2");
					}
					break;
				}
				

			}
		}
		switch (Status) {
			case "0" : 
				message="Canceled Ticket";
				break;
			case "1" :
				message="Valid Ticket";
				ImageName="ok.png";
				break;
			case "2" : 
				message="Ticket Already Validated";
				break;
			case "XX" :
				message="Ticket Not found";
				break;
			default:
				message="Unknown code";
				break;
		}
		resultMessage.setText(message);
		resultMessage.setIcon(new ImageIcon(getClass().getResource(ImageName)));
		resultMessage.repaint();
		p2.setPreferredSize(p2.getPreferredSize());
		f.pack();
		f.repaint();

	}

	private ActionListener listener = new ActionListener() {  
		public void actionPerformed(ActionEvent evt) {  
			String line = inputField.getText().trim();  
			validate(line);
			System.err.printf(line);
			inputField.setText("");  
			inputField.requestFocusInWindow();  
		}  
	};  
	public void run() {
		// Create the window

		// Sets the behavior for when the window is closed
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// add a label and a button
		p1.add(new JLabel("Ticket Number"));
		inputField.addActionListener(listener);
		p1.add(inputField);
		p2.add(resultMessage);
		f.getContentPane().add(p1,BorderLayout.PAGE_START);
		f.getContentPane().add(p2,BorderLayout.PAGE_END);

		// Add a layout manager so that the button is not placed on top of the label
		// arrange the components inside the window
		f.pack();
		//By default, the window is not visible. Make it visible.
		f.setVisible(true);
		JFileChooser choix = new JFileChooser();
		int retour=choix.showOpenDialog(f);
		if(retour==JFileChooser.APPROVE_OPTION){
		   // un fichier a été choisi (sortie par OK)
		   // nom du fichier  choisi 
		   InputFileName=choix.getSelectedFile().getAbsolutePath();
		loadXML(InputFileName);
		}
	}

	public static void main(String[] args) {
		ET4HValidator se = new ET4HValidator();
		// Schedules the application to be run at the correct time in the event queue.
		SwingUtilities.invokeLater(se);
	}
}
