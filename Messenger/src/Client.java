import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	Socket socket;
	String serverIp;
	String UserId;
	int port;

	Client() {//Clinet�� ���ڰ� ���� �� Default ������
		this.socket = null;
		this.serverIp = "127.0.0.1";
		this.port = 7777;
		this.UserId = "Guest1";
	}

	Client(String[] args) {//��Ʈ/ip/id�� �� �Է����� ��
		// TODO Auto-generated constructor stub
		this.socket = null;
		this.serverIp = args[0];
		this.port = Integer.parseInt(args[1]);
		this.UserId = args[2];
	}

	public void start() {//������ ip�� port�� ���� ����
		try {
			socket = new Socket(serverIp, port);
			System.out.println("Connect Sucess");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length == 0) {
			System.out.println("Command Prompt : java Client ServerIp Serverport UserID");
			System.out.println("If you don't input Server Ip and port, then Default Server is local and port is 7777");
			
			Client c = new Client();
			c.start();
			
			SendProcess sender = new SendProcess(c.socket, c.UserId);
			ReceiveProcess receiver = new ReceiveProcess(c.socket);
			sender.start();
			receiver.start();
		} else {
			Client c = new Client(args);
			c.start();
			
			SendProcess sender = new SendProcess(c.socket, c.UserId);
			ReceiveProcess receiver = new ReceiveProcess(c.socket);
			sender.start();
			receiver.start();

		}
	}
}
