
/**
 * Comp90015-Assignment 2
 * @author: Kung Pao Chicken
 * Ming Tang 902089
 * Yajing Huang 896243
 * Wai Yan Wong 892083
 *
 */
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import org.json.simple.JSONObject;

public class LoginGUI {

	private static JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void createGUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginGUI window = new LoginGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					System.out.println("CreateGUI wrong in Login GUI!");

				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginGUI() {
		initialize();
	}

	// If the input user-name is invalid, an error window will be popped up
	// If the input user-name is valid, the user will enter the game hall			
	public static void validUser(boolean vaild, String msg) {
		if (vaild) {
			Hall.createGUI();
			frame.setVisible(false);
			frame.dispose();
		} else {
			JOptionPane.showMessageDialog(null, msg, null, JOptionPane.PLAIN_MESSAGE);
			frame.setVisible(false);
			frame.dispose();
			System.exit(0);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 300, 130);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		textField = new JTextField();
		textField.setBounds(80, 21, 210, 37);
		textField.setColumns(10);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userName = textField.getText();
				if(!userName.equals("")) {
					JSONObject jsonUserName = new JSONObject();
					jsonUserName.put("username", userName);
					Client.sendMessage(jsonUserName);
				}else{
					JOptionPane.showMessageDialog(null, "Please input a username", null, JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
		btnLogin.setBounds(80, 70, 210, 29);
		frame.getContentPane().setLayout(null);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(6, 25, 70, 29);
		frame.getContentPane().add(lblUsername);
		frame.getContentPane().add(textField);
		frame.getContentPane().add(btnLogin);
		
		frame.addWindowListener(new WindowAdapter()
        {
			//If user close the GUI, the system close the socket
			//and exist
            @Override
            public void windowClosing ( WindowEvent e )
            {
            	Client.closeSocket();
            	System.out.println("System exit.");
                System.exit (0);
            }
        });
	}

}
