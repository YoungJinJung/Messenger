import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length == 0) {
			System.out.println("Command Prompt : java Client ServerIp Serverport UserID");
			System.out.println("If you don't input Server Ip and port, then Default Server is local and port is 7777");
		}
		String serverIp = "127.0.0.1";
		//int port = 7777;
		String UserId = "Guest1"; //User마다 Id를 다르게 해야됨.
		// String serverIp = arg[0];
		// int port = Integer.parseInt(arg[1]);
		// String UserId = arg[2];
		try {
			Socket socket = new Socket(serverIp, 7777);
			System.out.println("Connect Sucess");
			Sender sender = new Sender(socket, UserId);
			// Sender sender = new Sender(socket, arg[2]);
			Receiver receiver = new Receiver(socket);

			sender.start();
			receiver.start();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
