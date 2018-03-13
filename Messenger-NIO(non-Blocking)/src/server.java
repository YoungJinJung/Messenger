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
	private ByteBuffer buffer = ByteBuffer.allocate(1024);
	private ConcurrentHashMap<String, SocketChannel> clientMsg;
	private ServerSocketChannel serverSocket = null;
	private Selector selector = null;
	private Charset charSet = Charset.forName("UTF-8");
	private SocketChannel sc = null;
	private FileHandler fileHandler;
	private Logger logger;

	server() {
		try {
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

	private void accept(SelectionKey key) throws IOException {

		sc = serverSocket.accept();

		if (sc.isConnected()) {
			logger.info("SERVER I LOG: CONNECT SUCCESS");
		} else {
			logger.info("SERVER I LOG : CONNECT FALIED");
		}
		sc.configureBlocking(false);
		sc.register(selector, SelectionKey.OP_READ);
	}

	private void read(SelectionKey key) {
		SocketChannel cc = (SocketChannel) key.channel();
		if (!cc.isOpen()) {
			return;
		}
		StringBuilder request = new StringBuilder();
		try {
			buffer.clear();
			while (cc.read(buffer) > 0) {
				buffer.flip();
				request.append(new String(buffer.array(), buffer.position(), buffer.limit(), "UTF-8"));
				buffer.clear();
			}
			if (request.toString().startsWith("Enter UserID : ")) {
				String str[] = request.toString().split(":");
				str[1] = str[1].trim();
				clientMsg.put(str[1], sc);
				logger.info("SERVER I LOG: Enter UserID - " + str[1]);
				logger.info("SERVER I LOG: NUMBER OF USER - " + clientMsg.size());
				logger.info("----------------------DELIMETER---------------------");
			}
			write(request.toString());
		} catch (Exception exc) {
			// logger.severe("SERVER E LOG: " + exc.getMessage());
			try {
				cc.close();
				cc.socket().close();
				logger.info("SERVER I LOG: SOCKET CLOSED");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void write(String massage) {
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
		while (true)
			try {
				selector.select();
				for (Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i.hasNext(); i.remove()) {
					SelectionKey key = (SelectionKey) i.next();
					if (key.isAcceptable()) {
						accept(key);
					}
					if (key.isReadable()) {
						read(key);
					}
				}
			} catch (Exception exc) {
				exc.printStackTrace();
			}
	}

	public static void main(String[] args) {
		new server().run();
	}
}
