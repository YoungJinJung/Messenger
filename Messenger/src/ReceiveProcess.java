import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ReceiveProcess extends Thread {
	Socket socket;

	public ReceiveProcess(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
	}

	public void run() {
		DataInputStream in = null;
		try {
			in = new DataInputStream(socket.getInputStream());
			while (in != null) {
				try {
					System.out.println(in.readUTF());
				} catch (IOException e) {
				}
			}
		} catch (IOException e) {
		} finally {
			try {
				in.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}

	}
}