import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

public class Client {
	Socket socket = null;
	String serverIp;
	String UserId;
	int port;

	Client() {// Clinet의 인자가 없을 때 Default 생성자
		this.serverIp = "127.0.0.1";
		this.port = 7777;
		this.UserId = "Guest" + new Random().nextInt();
	}

	Client(String[] info) {// 포트/ip/id를 다 입력했을 때
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

	public void start() {// 서버의 ip와 port로 소켓 연결
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
		Scanner sc = new Scanner(System.in);
		System.out.println("Input ServerIp, Serverport, UserID");
		String[] ServerInfo = new String[3];

		for (int i = 0; i < 3; i++)
			ServerInfo[i] = sc.nextLine();
		if (ServerInfo[0].equals("localhost") || ServerInfo[0].equals("Localhost"))
			ServerInfo[0] = "127.0.0.1";

		if (ServerInfo[0].equals("")) {
			System.out.println("If you don't input Server Ip and port, then Default Server is local and port is 7777");
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
