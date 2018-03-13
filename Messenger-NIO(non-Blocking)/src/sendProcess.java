import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class sendProcess extends Thread {
	private SocketChannel socket;
	private String clientName;
	private Charset charSet = Charset.forName("UTF-8");

	sendProcess(SocketChannel socket, String userId) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.clientName = userId;
	}

	public void run() {
		Scanner sc = new Scanner(System.in);
		try {
			while (this.socket != null) {
				ByteBuffer byteBuffer = charSet.encode("[" + this.clientName + "] " + sc.nextLine());
				this.socket.write(byteBuffer);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				sc.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
	}
}
