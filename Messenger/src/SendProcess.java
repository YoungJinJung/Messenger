import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class SendProcess extends Thread {
	Socket socket;
	String c_name;
	DataOutputStream out;

	SendProcess(Socket socket, String UserId) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.c_name = UserId;
		this.out = null;
	}

	public void run() {
		Scanner sc = new Scanner(System.in);
		try {
			out = new DataOutputStream(socket.getOutputStream());
			if (out != null) {//first, user name send server
				out.writeUTF(c_name);
			}
			while (out != null) {
				//input data send server
				out.writeUTF("[" + c_name + "]" + sc.nextLine());
			}
		} catch (IOException e1) {
		} finally {
			try {
				sc.close();
				out.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
	}
}
