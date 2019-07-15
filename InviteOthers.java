/**
 * Comp90015-Assignment 2
 * @author: Kung Pao Chicken
 * Ming Tang 902089
 * Yajing Huang 896243
 * Wai Yan Wong 892083
 *
 */
import java.awt.*;

import javax.swing.*;

import javax.swing.GroupLayout.Alignment;

import org.json.simple.JSONObject;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class InviteOthers {

	private JFrame frame;
	private JCheckBox[] player;
	private String[] playerList;
	private JPanel list;

	/**
	 * Launch the application.
	 */
	public void createGUI(String players) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame.setVisible(true);
					setPlayers(players);
				} catch (Exception e) {
					System.out.println("CreateGUI wrong in InviteOthers");

				}
			}
		});
	}

	private void setPlayers(String players) {
		player  = new JCheckBox[20];
		playerList = players.split(":");
		for(int i = 1; i < playerList.length; i++) {
			player[i-1] = new JCheckBox(playerList[i]);
			player[i-1].setBounds(10, 20+20*i, 200, 30);
			frame.getContentPane().add(player[i-1]);
		};
		
	}
	/**
	 * Create the application.
	 */
	public InviteOthers() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		list = new JPanel();
		frame = new JFrame();
		frame.setBounds(100, 100, 300, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		player  = new JCheckBox[20];
		for(int i = 0; i < 20; i++){
			player[i] = new JCheckBox("i");
		}
		JButton btnInvite = new JButton("Invite");
		btnInvite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String select = "invite";
				int n = 0;
				Component[] players = frame.getContentPane().getComponents();
				if(playerList.length < 20) {
					for (int k=1; k<playerList.length; k++) {
						JCheckBox player = (JCheckBox) players[k];
						if(player.isSelected()) {
							select = select + ":" + player.getText();
							n++;
						}
					}
				}
				if(n < 1 || n > 3) {
					JOptionPane.showMessageDialog(null, "Please choose 1 to 3 players", null, JOptionPane.PLAIN_MESSAGE);
					frame.setVisible(false);
					frame.dispose();
				}else {
					JSONObject jsonInvitePlayer = new JSONObject();
					jsonInvitePlayer.put("Command", select);
					Client.sendMessage(jsonInvitePlayer);
					frame.setVisible(false);
					frame.dispose();
					JOptionPane.showMessageDialog(null, "Waiting for the response.", null,
							JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnInvite)
					.addContainerGap(177, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnInvite)
					.addContainerGap(243, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
