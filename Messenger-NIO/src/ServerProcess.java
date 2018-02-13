import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class ServerProcess extends Thread {
	Socket socket;
	DataInputStream in;
	DataOutputStream out;
	ConcurrentHashMap<String, DataOutputStream> client_msg;

	ServerProcess(Socket socket, ConcurrentHashMap<String, DataOutputStream> client_msg) {
		this.socket = socket;
		this.client_msg = client_msg;

	}

	void BroadCast(String msg) {

		for (Map.Entry<String, DataOutputStream> Entry : client_msg.entrySet()) {
			try {
				Entry.getValue().writeUTF(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void run() {
		String Client_name = null;
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			Client_name = in.readUTF();// first received user name
			client_msg.put(Client_name, out);
			BroadCast("Enter Client : " + Client_name);
			System.out.println("Number of User :" + client_msg.size());
			while (in != null) {
				BroadCast(in.readUTF());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} finally {
			BroadCast("Exit Client : " + Client_name);
			client_msg.remove(Client_name);
			System.out.println("[" + socket.getInetAddress() //
					+ ":" + socket.getPort() + "]");
			System.out.println("Number of User :" + client_msg.size());
		}
	}
}