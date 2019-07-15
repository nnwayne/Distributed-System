
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JOptionPane;

import javax.swing.JLabel;

public class Port {

	private JFrame frame;
	private JTextField IPTextField;
	private JTextField PortTextField;

	/**
	 * Launch the application.
	 */
	public static void createGUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Port window = new Port();
					window.frame.setVisible(true);
				} catch (Exception e) {
					System.out.println("CreateGUI Wrong in Port");

				}
			}
		});
	}


	/**
	 * Create the application.
	 */
	public Port() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 300, 140);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		IPTextField = new JTextField();
		IPTextField.setColumns(10);

		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			
			// If the user click the connect button, the system will try
			// to establish the connection and create the Login window
			// If the system fails to establish the connection, an error window will be popped up
			public void actionPerformed(ActionEvent e) {
				String IPAddress = IPTextField.getText();
				String PortNum = PortTextField.getText();
				try {
					Client.ConnectServer(IPAddress, PortNum);
					LoginGUI.createGUI();
					frame.setVisible(false);
					frame.dispose();
				} catch (Exception esocket) {
					JOptionPane.showMessageDialog(null, "Fail to connect with the server.", null,
							JOptionPane.PLAIN_MESSAGE);
					System.out.println("Fail to connect with the server");
				}
			}
		});
		
		PortTextField = new JTextField();
		PortTextField.setColumns(10);
		
		JLabel lblIp = new JLabel("IP:");
		
		JLabel lblPort = new JLabel("Port:");
		
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
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(22)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblIp)
									.addGap(18))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblPort)
									.addPreferredGap(ComponentPlacement.UNRELATED)))
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(PortTextField)
								.addComponent(IPTextField, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(102)
							.addComponent(btnConnect)))
					.addContainerGap(37, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblIp)
						.addComponent(IPTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(15)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(PortTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPort))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnConnect)
					.addGap(8))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
