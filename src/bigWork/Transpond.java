package bigWork;

/* ת��������
 * �������յ��İ�ת����port
 * */

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class Transpond implements Runnable {

	int port;
	byte[] SendData;
	long TranspondTime;

	Transpond(long t, int p, byte[] b) {
		TranspondTime = t;
		port = p;
		SendData = b;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(TranspondTime); // ��һ���ת������ֹת������
			DatagramSocket socket = new DatagramSocket();
			DatagramPacket sendPacket = new DatagramPacket(SendData, SendData.length);
			sendPacket.setSocketAddress(new InetSocketAddress("localhost", port));
			socket.send(sendPacket);
			// System.out.println("��ת�����ݰ���" + port);
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}