import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ReceiveProcess extends Thread {
	SocketChannel socket;

	ReceiveProcess(SocketChannel socket2) {
		// TODO Auto-generated constructor stub
		this.socket = socket2;
	}

	public void run() {
		try {
			while (socket != null) {
				ByteBuffer byteBuffer = ByteBuffer.allocate(100);
				int byteCount = this.socket.read(byteBuffer);
				if (byteCount == -1) {
					throw new IOException();
				}
				byteBuffer.flip();
				Charset charset = Charset.forName("UTF-8");
				String data = charset.decode(byteBuffer).toString();
				System.out.println(data);
			}
		} catch (IOException e) {
		} finally {
			try {
				this.socket.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
	}
}
