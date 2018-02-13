
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

class ServerProcess extends Thread {
	SocketChannel socket;
	ConcurrentHashMap<String, SocketChannel> client_msg;
	ExecutorService executorService;

	ServerProcess(SocketChannel socket, ConcurrentHashMap<String, SocketChannel> client_msg, ExecutorService executorService) {
		this.socket = socket;
		this.client_msg = client_msg;
		this.executorService = executorService;

	}

	void BroadCast(String msg) {
		for (Map.Entry<String, SocketChannel> Entry : client_msg.entrySet()) {
			try {
				Charset charset = Charset.forName("UTF-8");
				ByteBuffer byteBuffer = charset.encode(msg);
				Entry.getValue().write(byteBuffer);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void start() {
		String Client_name = null;
		ByteBuffer byteBuffer = ByteBuffer.allocate(100);

		try {
			int byteCount = socket.read(byteBuffer);

			// 클라이언트가 정상적으로 SocketChannel의 close()를 호출헀을 경우
			if (byteCount == -1) {
				throw new IOException();
			}
			byteBuffer.flip();
			Charset charset = Charset.forName("UTF-8");
			Client_name = charset.decode(byteBuffer).toString();
			// Client_name = in.readUTF();// first received user name
			client_msg.put(Client_name, this.socket);
			BroadCast("Enter Client : " + Client_name);
			System.out.println("Number of User :" + client_msg.size());

			while (true) {
				ByteBuffer byteBuffer_msg = ByteBuffer.allocate(100);

				// 클라이언트가 비정상 종료를 했을 경우 IOException 발생
				int byteCount_msg = socket.read(byteBuffer_msg);

				// 클라이언트가 정상적으로 SocketChannel의 close()를 호출헀을 경우
				if (byteCount_msg == -1) {
					throw new IOException();
				}

				byteBuffer_msg.flip();
				String data = charset.decode(byteBuffer_msg).toString();

				BroadCast(data);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} finally {
			BroadCast("Exit Client : " + Client_name);
			client_msg.remove(Client_name);
			try {
				System.out.println("[" + socket.getRemoteAddress() + "]");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Number of User :" + client_msg.size());
		}
	}
}