import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Server {
	ServerSocketChannel serverSocket = null;
	SocketChannel socket = null;
	ConcurrentHashMap<String, SocketChannel> client_msg;
	int port;
	ExecutorService executorService;
	private FileHandler fileHandler;
	Logger logger;
	
	Server() {
		try {
			logger = Logger.getLogger(Server.class.getName());
			long time = System.currentTimeMillis(); 
			fileHandler = new FileHandler(String.valueOf(time) + ".log");// 파일저장
			logger.addHandler(fileHandler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.client_msg = new ConcurrentHashMap<String, SocketChannel>();
		this.port = 7777;
	}

	Server(String str) {
		// TODO Auto-generated constructor stub
		try {
			logger = Logger.getLogger(Server.class.getName());
			Date dt = new Date();
			fileHandler = new FileHandler(dt.toString() + ".log");// 파일저장
			logger.addHandler(fileHandler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.client_msg = new ConcurrentHashMap<String, SocketChannel>();
		this.port = Integer.parseInt(str);

	}

	public void start() {
		executorService = Executors.newCachedThreadPool();
		try {
			serverSocket = ServerSocketChannel.open();
			serverSocket.configureBlocking(true);
			serverSocket.bind(new InetSocketAddress(port));
		} catch (IOException e1) {// 예외처리 해야됨(not done)
			// TODO Auto-generated catch block
			logger.severe(e1.getMessage());
			if (serverSocket.isOpen()) {
				if (serverSocket != null && serverSocket.isOpen()) {
					try {
						logger.info("Server Socket Closed");
						serverSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						logger.severe(e.getMessage());
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
				logger.info("Server Start\nPort Number : 7777(Default)");
				while (true) {
					try {
						socket = serverSocket.accept();
			
						logger.info("[" + socket.getRemoteAddress() + "]" + "is connect");
						ServerProcess sp = new ServerProcess(socket, client_msg, executorService, logger);
						sp.start();
					} catch (Exception e) {// 예외처리 해야됨(not done)
						logger.severe(e.getMessage());
						try {
							if (serverSocket != null && serverSocket.isOpen()) {
								logger.info("Server Socket Closed");
								serverSocket.close();
							}
							if (executorService != null && executorService.isShutdown()) {
								executorService.shutdown();
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							logger.severe(e1.getMessage());
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
