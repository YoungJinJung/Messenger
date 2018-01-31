import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Server {
	Map<String, DataOutputStream> client;

	Server() {
		client = Collections.synchronizedMap(new HashMap<String, DataOutputStream>());
	}
	
	

	public static void main(String[] args) {

	}

}
