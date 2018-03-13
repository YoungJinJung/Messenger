import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.Scanner;

public class client {
	private SocketChannel socket = null;
	private String serverIp;
	private String userId;
	private int port;
	private boolean connectedToServer = false;
	private Charset charSet = Charset.forName("UTF-8");

	client() {// Clinet의 인자가 없을 때 Default 생성자
		this.serverIp = "127.0.0.1";
		this.port = 7777;
		this.userId = "Guest" + new Random().nextInt(1000);
	}

	client(String[] info) {// 포트/ip/id를 다 입력했을 때
		// TODO Auto-generated constructor stub
		if (info[0].equals("") && info[1].equals("")) {
			this.serverIp = "127.0.0.1";
			this.port = 7777;
			this.userId = info[2];
		} else {
			this.serverIp = info[0];
			this.port = Integer.parseInt(info[1]);
			this.userId = info[2];
		}
	}

	public void connect() {// 서버의 ip와 port로 소켓 연결
		try {
			if (connectedToServer) {
				return;
			}
			socket = SocketChannel.open();
			socket.configureBlocking(false);
			if (!socket.isOpen()) {
				socket = SocketChannel.open();
			}
			socket.connect(new InetSocketAddress(this.serverIp, this.port));
			System.out.printf("CLIENT LOG: Connecting to %s on port %d%n", this.serverIp, this.port);
			while (!socket.finishConnect()) {
				try {
					Thread.sleep(200);
				} catch (Exception exc) {
					return;
				}
				System.out.print(".");
			}
			System.out.println("CLIENT LOG: Connected!");
			connectedToServer = true;

			// Create a new thread to listen to InputStream event
			String reg = "Enter UserID : " + userId;
			sendID(reg);
		} catch (IOException exc) {
			System.out.printf("CLIENT LOG: ERROR -> Problem with connecting to %s, %s%n", "localhost",
					exc.getLocalizedMessage());
			System.exit(1);
		}
	}

	public boolean sendID(String userId) {
		if (!connectedToServer) {
			return false;
		}
		try {
			ByteBuffer messageByteBuffer = charSet.encode(userId);
			socket.write(messageByteBuffer);
		} catch (IOException e) {
			System.out.printf("CLIENT LOG: Error -> Problem with read or write to/from server %s%n",
					e.getLocalizedMessage());
		}

		return true;

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		System.out.println("CLIENT LOG: Input SERVER IP, SERVER PORT, USER ID");
		System.out.println(
				"CLIENT LOG: If you don't input Server Ip and port, then Default Server is local and port is 7777");
		String[] serverInfo = new String[3];

		System.out.println("SERVER IP : ");
		serverInfo[0] = sc.nextLine();

		System.out.println("SERVER PORT : ");
		serverInfo[1] = sc.nextLine();

		System.out.println("USER ID : ");
		serverInfo[2] = sc.nextLine();

		if (serverInfo[0].equals("")) {

			if (serverInfo[2].equals("")) {
				client c = new client();
				c.connect();

				sendProcess sender = new sendProcess(c.socket, c.userId);
				receiveProcess receiver = new receiveProcess(c.socket);
				sender.start();
				receiver.start();
			} else {
				client c = new client(serverInfo);
				c.connect();

				sendProcess sender = new sendProcess(c.socket, c.userId);
				receiveProcess receiver = new receiveProcess(c.socket);
				sender.start();
				receiver.start();
			}
		} else {
			client c = new client(serverInfo);
			c.connect();

			sendProcess sender = new sendProcess(c.socket, c.userId);
			receiveProcess receiver = new receiveProcess(c.socket);
			sender.start();
			receiver.start();

		}
	}
}
