import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class SendProcess extends Thread {
	SocketChannel socket;
	String c_name;

	SendProcess(SocketChannel socket2, String UserId) {
		// TODO Auto-generated constructor stub
		this.socket = socket2;
		this.c_name = UserId;
	}

	public void run() {
		Scanner sc = new Scanner(System.in);
		try {
			if (this.socket != null) {
				Charset charset = Charset.forName("UTF-8");
				ByteBuffer byteBuffer = charset.encode(this.c_name);
				try {
					this.socket.write(byteBuffer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			while (this.socket != null) {
				Charset charset = Charset.forName("UTF-8");
				ByteBuffer byteBuffer = charset.encode("[" + this.c_name + "]" + sc.nextLine());
				this.socket.write(byteBuffer);
			}
		} catch (IOException e1) {
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
