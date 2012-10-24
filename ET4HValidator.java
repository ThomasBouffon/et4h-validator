import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.JTextField;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent; 
import java.awt.event.KeyEvent;

public class ET4HValidator implements Runnable {
	JTextField inputField=new JTextField(50);
	JPanel p1 = new JPanel ();
	JPanel p2 = new JPanel ();
	JLabel resultMessage = new JLabel("Type a ticket # in the field above");
	String message;
	private void validate(String input) {
		switch (input) {
			case "0" : 
				message="Canceled Ticket";
				break;
			case "1" :
				message="Valid Ticket";
				break;
			case "2" : 
				message="Ticket Already Validated";
				break;
			default:
				message="Unknown code";
				break;
		}
		resultMessage.setText(message);
		resultMessage.repaint();

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

		JFrame f = new JFrame ("ET4HValidator");

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
	}

	public static void main(String[] args) {
		ET4HValidator se = new ET4HValidator();
		// Schedules the application to be run at the correct time in the event queue.
		SwingUtilities.invokeLater(se);
	}
}
