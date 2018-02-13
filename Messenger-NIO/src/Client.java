import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.Random;
import java.util.Scanner;

public class Client {
	SocketChannel socket = null;
	String serverIp;
	String UserId;
	int port;

	Client() {// Clinet�� ���ڰ� ���� �� Default ������
		this.serverIp = "127.0.0.1";
		this.port = 7777;
		this.UserId = "Guest" + new Random().nextInt(1000);
	}

	Client(String[] info) {// ��Ʈ/ip/id�� �� �Է����� ��
		// TODO Auto-generated constructor stub
		if (info[0].equals("") && info[1].equals("")) {
			this.serverIp = "127.0.0.1";
			this.port = 7777;
			this.UserId = info[2];
		} else {
			this.serverIp = info[0];
			this.port = Integer.parseInt(info[1]);
			this.UserId = info[2];
		}
	}

	public void start() {// ������ ip�� port�� ���� ����
		try {
			socket = SocketChannel.open();
			socket.configureBlocking(true);
			socket.connect(new InetSocketAddress(serverIp, port));
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
		Scanner sc = new Scanner(System.in);
		System.out.println("Input ServerIp, Serverport, UserID");
		System.out.println("If you don't input Server Ip and port, then Default Server is local and port is 7777");
		String[] ServerInfo = new String[3];

		for (int i = 0; i < 3; i++)
			ServerInfo[i] = sc.nextLine();
	

		if (ServerInfo[0].equals("")) {

			if (ServerInfo[2].equals("")) {
				Client c = new Client();
				c.start();
				SendProcess sender = new SendProcess(c.socket, c.UserId);
				ReceiveProcess receiver = new ReceiveProcess(c.socket);
				sender.start();
				receiver.start();
			} else {
				Client c = new Client(ServerInfo);
				c.start();
				SendProcess sender = new SendProcess(c.socket, c.UserId);
				ReceiveProcess receiver = new ReceiveProcess(c.socket);
				sender.start();
				receiver.start();
			}
		} else {
			Client c = new Client(ServerInfo);
			c.start();

			SendProcess sender = new SendProcess(c.socket, c.UserId);
			ReceiveProcess receiver = new ReceiveProcess(c.socket);
			sender.start();
			receiver.start();

		}
	}
}
