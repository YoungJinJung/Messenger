
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
	ConcurrentHashMap<String, DataOutputStream> client_msg;
	ServerSocket serverSocket;
	Socket socket;
	int port;

	Server() {
		this.serverSocket = null;
		this.socket = null;
		this.client_msg = new ConcurrentHashMap<String, DataOutputStream>();
		this.port = 7777;
	}

	Server(String str) {
		// TODO Auto-generated constructor stub
		this.serverSocket = null;
		this.socket = null;
		this.client_msg = new ConcurrentHashMap<String, DataOutputStream>();
		this.port = Integer.parseInt(str);

	}

	public void start() {
		try {
			this.serverSocket = new ServerSocket(this.port);
			System.out.println("Server Start");

			while (true) {
				this.socket = this.serverSocket.accept();
				System.out.println("Connect Sucess");
				System.out.println(
						"[" + this.socket.getInetAddress() + ":" + this.socket.getPort() + "]" + "is entered Server.");
				ServerProcess sp = new ServerProcess(this.socket, this.client_msg);
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
