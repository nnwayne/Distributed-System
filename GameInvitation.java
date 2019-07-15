/**
 * Comp90015-Assignment 2
 * @author: Kung Pao Chicken
 * Ming Tang 902089
 * Yajing Huang 896243
 * Wai Yan Wong 892083
 *
 */
import org.json.simple.JSONObject;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameInvitation {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void createGUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameInvitation window = new GameInvitation();
					window.frame.setVisible(true);
				} catch (Exception e) {
					System.out.println("createGUI Wrong!");
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GameInvitation() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 250, 130);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// If the user reject the game invitation, the invitation window will close
		JButton btnReject = new JButton("Reject");
		btnReject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JSONObject reject = new JSONObject();
				reject.put("Command", "decline");
				Client.sendMessage(reject);
				frame.setVisible(false);
                frame.dispose();
			}
		});
		
		JLabel lblNewLabel = new JLabel("Do you want to join a game?");
		
		// If the user accept the game invitation,
		// the user will join the game
		JButton btnJoin = new JButton("Join");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JSONObject accept = new JSONObject();
				accept.put("Command", "accept");
				Client.sendMessage(accept);
				frame.setVisible(false);
                frame.dispose();
			}
		});
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(6)
					.addComponent(btnReject, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnJoin, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
					.addGap(16))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(26, Short.MAX_VALUE)
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 202, GroupLayout.PREFERRED_SIZE)
					.addGap(22))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(21)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnReject, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnJoin, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
