import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Sender extends Thread {
	Socket socket;
	String c_name;

	public Sender(Socket socket, String UserId) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.c_name = UserId;
	}

	public void run() {
		DataOutputStream out = null;

		try {
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Scanner sc = new Scanner(System.in);
		try {
			// �������� ����, �ڽ��� ��ȭ���� ������ ����
			if (out != null) {
				out.writeUTF(c_name);
			}

			while (out != null) {
				// Ű����� �Է¹��� �����͸� ������ ����
				out.writeUTF("[" + c_name + "]" + sc.nextLine());
			}
		} catch (IOException e) {
		}

	}
}
