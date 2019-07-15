/**
 * Comp90015-Assignment 2
 * @author: Kung Pao Chicken
 * Ming Tang 902089
 * Yajing Huang 896243
 * Wai Yan Wong 892083
 *
 */
import java.io.DataInputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ServerListener extends Thread {

	private DataInputStream reader;
	private static Boolean Holder = false;

	public ServerListener(DataInputStream reader) {
		this.reader = reader;
	}

	public static void IsHolder() {
		Holder = true;
	}

	@Override
	public void run() {
		JSONParser parser = new JSONParser();
		String[] ClientOnline;
		try {
			// Keep reading messages from the server
			while (true) {
				if (reader.available() > 0) {
					
					// Parse the received message to the correct format
					// And the print the received message
					JSONObject msg = (JSONObject) parser.parse(reader.readUTF());
					System.out.print("Received from server: ");
					System.out.println(msg.toJSONString());
					
					// Judge the key in the received message
					if (msg.containsKey("ReplyLogin")) {
						if (msg.get("ReplyLogin").equals("Invalid User")) {
							LoginGUI.validUser(false, "Invalid User");
						} else if(msg.get("ReplyLogin").equals("Already Logged in")){
							LoginGUI.validUser(false, "Already Logged in");
						}
						else {
							LoginGUI.validUser(true, "");
						}
					}
					if (msg.containsKey("ClientInfor")) {
						String[] userInfor = ((String)msg.get("ClientInfor")).split(":");
						if(userInfor.length > 1) {
							Hall.setUserInfor(userInfor[0],userInfor[1]);
						}
					}

					// If the key is ReplyClientList, display the clients on the hall
					else if (msg.containsKey("ReplyClientList")) {
						ClientOnline = ((String)msg.get("ReplyClientList")).split(":");
						Hall.display(ClientOnline);
					}

					// If the key is ReplyStartGame, and if the player is not the game holder
					// an invitation window will be popped up
					else if (msg.containsKey("ReplyStartGame")) {
						if (msg.get("ReplyStartGame").equals("Invitation")) {
							if (Holder == true) {
								//Hall.StartGame(Holder);
							} else {
								GameInvitation.createGUI();
								Holder = false;
							}
						} else{
							int p = Integer.parseInt(msg.get("ReplyStartGame").toString());
							Hall.StartGame(Holder, p);
							Holder = false;
						}
					}

					else if (msg.containsKey("Not Enough Player")){
						Hall.error(msg.get("Not Enough Player").toString());
						Holder = false;
					}

					else if (msg.containsKey("Error")){
							Hall.error(msg.get("Error").toString());
							Holder = false;
						}
					}
				}


		} catch (SocketTimeoutException e) {
			System.out.println("Connection is interrupted.");
			System.exit(0);
		} catch (SocketException e) {
			System.out.println("Connection is interrupted.");
			System.exit(0);
		} catch (Exception e) {
			System.out.println("run Wrong in ServerListener");
		}

	}
}
