
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

class ServerProcess extends Thread {
	SocketChannel socket;
	ConcurrentHashMap<String, SocketChannel> client_msg;
	ExecutorService executorService;
	Logger logger;
	

	ServerProcess(SocketChannel socket, ConcurrentHashMap<String, SocketChannel> client_msg,
			ExecutorService executorService, Logger logger) {
		this.socket = socket;
		this.client_msg = client_msg;
		this.executorService = executorService;
		this.logger = logger;
	
	}

	void BroadCast(String msg) {
		for (Map.Entry<String, SocketChannel> Entry : client_msg.entrySet()) {
			try {
				Charset charset = Charset.forName("UTF-8");
				ByteBuffer byteBuffer = charset.encode(msg);
				Entry.getValue().write(byteBuffer);
			} catch (Exception e) {

				logger.severe(e.getMessage().toString());
				// e.printStackTrace();
			}
		}
	}

	public void start() {
		Runnable runnable = new Runnable() {
			String Client_name = null;
			int count = 0;
			Charset charset = Charset.forName("UTF-8");

			@Override
			public void run() {
				try {
					while (true) {
						ByteBuffer byteBuffer = ByteBuffer.allocate(100);
						int byteCount = 0;
						if (count == 0) {
							byteCount = socket.read(byteBuffer);
							// 클라이언트가 정상적으로 SocketChannel의 close()를 호출헀을 경우
							if (byteCount == -1) {
								throw new IOException();
							}
							byteBuffer.flip();
							Client_name = charset.decode(byteBuffer).toString();
							// Client_name = in.readUTF();// first received user name
							client_msg.put(Client_name, socket);
							logger.info("Enter Client : " + Client_name);
							BroadCast("Enter Client : " + Client_name);
							logger.info("Number of User :" + client_msg.size());
						} else {
							// 클라이언트가 비정상 종료를 했을 경우 IOException 발생

							byteCount = socket.read(byteBuffer);
							// 클라이언트가 정상적으로 SocketChannel의 close()를 호출헀을 경우
							if (byteCount == -1) {
								throw new IOException();
							}
							byteBuffer.flip();

							String data = charset.decode(byteBuffer).toString();

							if (data.equals("Exit") || data.equals("exit"))
								break;

							BroadCast(data);
						}
						count++;
					}
				} catch (IOException e) {
					logger.severe(e.getMessage());
					// TODO Auto-generated catch block
				} finally {
					exit(Client_name);
				}
			}
		};
		executorService.submit(runnable);
	}

	public void exit(String Client_name) {
		logger.info("Exit Client : " + Client_name);
		BroadCast("Exit Client : " + Client_name);
		client_msg.remove(Client_name);
		try {
			logger.info("[" + socket.getRemoteAddress() + "] is exit");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.severe(e.getMessage());
		}
		logger.info("Number of User :" + client_msg.size());
	}
}
