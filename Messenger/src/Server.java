
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
	ConcurrentHashMap<String, DataOutputStream> client;

	Server() {
		client = new ConcurrentHashMap<String, DataOutputStream>();
	}

	public void start() {
		ServerSocket serverSocket = null;
		Socket socket = null;

		try {
			serverSocket = new ServerSocket(7777);
			System.out.println("Server Start");

			while (true) {
				socket = serverSocket.accept();
				System.out.println("Connect Sucess");
				System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "���� �����Ͽ����ϴ�.");
				// �������� Ŭ���̾�Ʈ�� �޽����� ������ Thread ����
				ServerProcess thread = new ServerProcess(socket, client);
				thread.start();
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
		new Server().start();
	}
}
