import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	ServerSocketChannel serverSocket = null;
	SocketChannel socket = null;
	ConcurrentHashMap<String, SocketChannel> client_msg;
	int port;
	ExecutorService executorService;

	Server() {

		this.client_msg = new ConcurrentHashMap<String, SocketChannel>();
		this.port = 7777;
	}

	Server(String str) {
		// TODO Auto-generated constructor stub
		this.client_msg = new ConcurrentHashMap<String, SocketChannel>();
		this.port = Integer.parseInt(str);

	}

	public void start() {
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		try {

			serverSocket = ServerSocketChannel.open();
			serverSocket.configureBlocking(true);
			serverSocket.bind(new InetSocketAddress(port));
		} catch (IOException e1) {// 예외처리 해야됨(not done)
			// TODO Auto-generated catch block
			if (serverSocket.isOpen()) {
				if (serverSocket != null && serverSocket.isOpen()) {
					try {
						serverSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (executorService != null && executorService.isShutdown()) {
					executorService.shutdown();
				}
			}
		}

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				System.out.println("Server Start\nPort Number : 7777(Default)");
				while (true) {
					try {
						socket = serverSocket.accept();
						System.out.println("Connect Sucess");
						System.out.println("[" + socket.getRemoteAddress() + "]" + "is entered Server.");
						ServerProcess sp = new ServerProcess(socket, client_msg, executorService);
						sp.start();
					} catch (Exception e) {// 예외처리 해야됨(not done)
						try {
							if (serverSocket != null && serverSocket.isOpen()) {
								serverSocket.close();
							}
							if (executorService != null && executorService.isShutdown()) {
								executorService.shutdown();
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}

			}
		};
		executorService.submit(runnable);
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out
		.println("Input Server Port.\nIf you don't input port, then Default Server is local and port is 7777");
		String port = sc.nextLine();

		if (port.length() == 0) {
			Server s = new Server();
			s.start();
		} else {
			Server s = new Server(port);
			s.start();
		}
	}
}
