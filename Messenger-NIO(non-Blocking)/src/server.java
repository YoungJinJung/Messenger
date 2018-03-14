import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class server {
	private final ByteBuffer buffer = ByteBuffer.allocate(8192);
	private ConcurrentHashMap<String, SocketChannel> clientMsg;
	private ServerSocketChannel serverSocket = null;
	private Selector selector = null;
	private final Charset charSet = Charset.forName("UTF-8");
	private SocketChannel sc = null;
	private FileHandler fileHandler = null;
	private Logger logger = null;

	server() {
		try {
			// Logger 설정 - 현재시간을 ms로 하여, 파일 생성
			logger = Logger.getLogger(server.class.getName());
			long time = System.currentTimeMillis();
			fileHandler = new FileHandler(String.valueOf(time) + ".log");// 파일저장
			logger.addHandler(fileHandler);

			clientMsg = new ConcurrentHashMap<>();
			serverSocket = ServerSocketChannel.open();
			serverSocket.configureBlocking(false);
			serverSocket.bind(new InetSocketAddress(7777));
			selector = Selector.open();
			serverSocket.register(selector, SelectionKey.OP_ACCEPT);

		} catch (Exception exc) {
			System.out.println("SERVER I LOG: EXIT");
			exc.printStackTrace();
			System.exit(1);
		}
		logger.info("SERVER I LOG: SERVER START\nPORT NUMBER : 7777(DEFAULT)");
	}

	private void acceptClient(SelectionKey key) throws IOException {

		sc = serverSocket.accept();

		if (sc.isConnected()) {
			logger.info("SERVER I LOG: CONNECT SUCCESS");
		} else {
			logger.info("SERVER I LOG : CONNECT FALIED");
		}

		sc.configureBlocking(false);
		sc.register(selector, SelectionKey.OP_READ);
	}

	private void readMessage(SelectionKey key) {
		SocketChannel cc = (SocketChannel) key.channel();
		if (!cc.isOpen()) {
			return;
		}
		try {
			buffer.clear();
			while (cc.read(buffer) > 0) {
				int byteCount = cc.read(buffer);
				if (byteCount == -1) {
					throw new IOException();
				}
				buffer.flip();
				String data = charSet.decode(buffer).toString();
				if (data.startsWith("Enter UserID : ")) {// 처음 들어온 경우
					String str[] = data.split(":");
					str[1] = str[1].trim();
					clientMsg.put(str[1], sc);
					logger.info("SERVER I LOG: Enter UserID - " + str[1]);
					logger.info("SERVER I LOG: NUMBER OF USER - " + clientMsg.size());
					logger.info("----------------------DELIMETER---------------------");
					broadCastToUser(data);
				} else if (data.endsWith("EXIT") || data.endsWith("exit")) {// 명시적 종료 상황
					String str[] = data.split(" ");
					String msg = str[0].substring(1, str[0].length() - 1);
					msg = "Exit User ID : " + str[0].substring(1, str[0].length() - 1);
					broadCastToUser(msg);
					clientMsg.remove(str[0].substring(1, str[0].length() - 1));
					logger.info("SERVER I LOG: " + msg);
					logger.info("SERVER I LOG: NUMBER OF USER - " + clientMsg.size());
					logger.info("----------------------DELIMETER---------------------");
				} else
					broadCastToUser(data);
			}
		} catch (Exception exc) {
			logger.severe("SERVER E LOG: " + exc.getMessage());
		}
	}

	private void broadCastToUser(String massage) {
		for (Map.Entry<String, SocketChannel> Entry : clientMsg.entrySet()) {
			try {
				ByteBuffer encodedMessage = charSet.encode(massage);
				if (Entry.getValue().isConnected()) {
					Entry.getValue().write(encodedMessage);
				} else {
					logger.severe("SERVER E LOG: Send Falied");
				}
			} catch (Exception e) {
				logger.severe("SERVER E LOG: " + e.getMessage());
			}
		}
	}

	public void run() {
		while (true) {
			try {
				selector.select();
				for (Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i.hasNext(); i.remove()) {
					SelectionKey key = (SelectionKey) i.next();
					if (key.isAcceptable()) {
						acceptClient(key);
					}
					if (key.isReadable()) {
						readMessage(key);
					}
				}
			} catch (Exception exc) {
				logger.info("SERVER SHUTDOWN");
			}
		}
	}

	public static void main(String[] args) {
		new server().run();
	}
}
