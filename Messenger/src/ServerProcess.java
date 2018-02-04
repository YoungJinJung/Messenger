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
	ConcurrentHashMap<String, DataOutputStream> client;

	ServerProcess(Socket socket, ConcurrentHashMap<String, DataOutputStream> client) {
		this.socket = socket;
		this.client = client;
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
		}
	}

	void BroadCast(String msg) {
		Iterator<String> it = client.keySet().iterator();

		while (it.hasNext()) {
			String name = it.next();
			DataOutputStream out = client.get(name);
			try {
				out.writeUTF(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
	}

	public void run() {
		String Client_name = null;
		try {
			Client_name = in.readUTF();//first received user name
			client.put(Client_name, out);
			BroadCast("Enter Client : " + Client_name);
			System.out.println("Number of User :" + client.size());
			while (in != null) {
					BroadCast(in.readUTF());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} finally {
			BroadCast("Exit Client : " + Client_name);
			client.remove(Client_name);
			System.out.println("[" + socket.getInetAddress() //
					+ ":" + socket.getPort() + "]");
			System.out.println("Number of User :" + client.size());
		}
	}
}