
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
	ServerSocket serverSocket = null;
	Socket socket = null;
	ConcurrentHashMap<String, DataOutputStream> client_msg;
	int port;
	int threadPoolSize = 16;

	Server() {
		this.client_msg = new ConcurrentHashMap<String, DataOutputStream>();
		this.port = 7777;
	}

	Server(String str) {
		// TODO Auto-generated constructor stub
		this.client_msg = new ConcurrentHashMap<String, DataOutputStream>();
		this.port = Integer.parseInt(str);

	}

	public void start() {
		Thread thread = null;
		for (int i = 0; i < threadPoolSize; i++)
			thread = new Thread() {
				public void run() {
					try {
						serverSocket = new ServerSocket(port);
						System.out.println("Server Start");
						while (true) {
							socket = serverSocket.accept();
							System.out.println("Connect Sucess");
							System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]"
									+ "is entered Server.");
							ServerProcess sp = new ServerProcess(socket, client_msg);
							sp.start();
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							socket.close();
							serverSocket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			};
		thread.start();
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			Server s = new Server();
			s.start();
		} else {
			Server s = new Server(args[0]);
			s.start();
		}
	}
}
