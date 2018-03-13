import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class receiveProcess extends Thread {
	private SocketChannel socket;
	private Charset charSet = Charset.forName("UTF-8");

	receiveProcess(SocketChannel socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
	}

	public void run() {
		try {
			ByteBuffer byteBuffer = ByteBuffer.allocate(100);
			while (true) {
				byteBuffer.clear();
				if (socket.read(byteBuffer) > 0) {
					int byteCount = this.socket.read(byteBuffer);
					if (byteCount == -1) {
						throw new IOException();
					}
					byteBuffer.flip();

					String data = charSet.decode(byteBuffer).toString();
					System.out.println(data);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				this.socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
	}
}
