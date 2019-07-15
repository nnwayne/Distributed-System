/**
 * Comp90015-Assignment 2
 * @author: Kung Pao Chicken
 * Ming Tang 902089
 * Yajing Huang 896243
 * Wai Yan Wong 892083
 *
 */
import java.awt.event.*;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.json.simple.JSONObject;

public class Hall {

	private JFrame frame;
	private static JTextArea textArea;
	private static String username;
	private static String userID;
	private static String[] ClientOnline;
	private static String host = "0";

	/**
	 * Launch the application.
	 */
	public static void createGUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Hall window = new Hall();
					window.frame.setVisible(true);
				} catch (Exception e) {
					System.out.println("CreateGUI Wrong in Hall!");

				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Hall() {
		initialize();
	}
	
	// Once the system received the updated client list,
	// the online users show on the hall will be updated
	public static void display(String[]  Client) {
		ClientOnline = Client;
		textArea.setText("");
		for(int i = 1; i < ClientOnline.length; i++) {;
			textArea.append(ClientOnline[i]);
			textArea.append("\n");
		}
	}
	
	public static void setUserInfor(String name, String ID) {
		username = name;
		userID = ID;
	}
	
	public static void StartGame(boolean Holder, int port) {
		try{
			String host = "0";
			if(Holder){
				host = "1";
			}
			String[] userInfor = {username, host, Integer.toString(port)};
			RemoteClient.main(userInfor);
		} catch(Exception e){
			System.out.println("StartGame Wrong in Hall");

		}
	}

	public static void error(String msg){
		JOptionPane.showMessageDialog(null, msg, null, JOptionPane.PLAIN_MESSAGE);

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 300, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnCreatGame = new JButton("Create Game");
		btnCreatGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ServerListener.IsHolder();
				String inviteList = "";
				for(int i = 1; i < ClientOnline.length; i++) {
					if(!ClientOnline[i].equals(username)) {
						inviteList = inviteList + ":" + ClientOnline[i];
					}
				}
				InviteOthers invite = new InviteOthers();
				invite.createGUI(inviteList);
			}
		});
		
		JLabel lblOnlineUsers = new JLabel("Online Users:");
		
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
		
		textArea = new JTextArea();
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(17)
					.addComponent(lblOnlineUsers, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
					.addGap(36)
					.addComponent(btnCreatGame)
					.addGap(25))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGap(25)
					.addComponent(textArea)
					.addGap(23))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(12)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(5)
							.addComponent(lblOnlineUsers))
						.addComponent(btnCreatGame))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
					.addGap(25))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
