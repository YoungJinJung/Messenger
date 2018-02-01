import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

class ServerReceiver extends Thread {
	Socket socket;
	DataInputStream in;
	DataOutputStream out;
	Map<String, DataOutputStream> client;

	ServerReceiver(Socket socket, Map<String, DataOutputStream> client) {
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
				e.printStackTrace();
			}
		}
	}

	public void run() {
		String Client_name = null;
		try {
			Client_name = in.readUTF();
			client.put(Client_name, out);
			BroadCast("Enter Client : " + Client_name);
			System.out.println("Number of User :" + client.size());
			while (in != null) {
				try {
					BroadCast(in.readUTF());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			BroadCast("Exit Client : " + Client_name);
			client.remove(Client_name);
			System.out.println("[" + socket.getInetAddress() //
					+ ":" + socket.getPort() + "]");
			System.out.println("Number of User :" + client.size());
		}
	}
}