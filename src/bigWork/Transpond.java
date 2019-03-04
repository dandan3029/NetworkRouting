package bigWork;

/* 转发包的类
 * 用来将收到的包转发给port
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
			Thread.sleep(TranspondTime); // 等一会儿转发，防止转发过快
			DatagramSocket socket = new DatagramSocket();
			DatagramPacket sendPacket = new DatagramPacket(SendData, SendData.length);
			sendPacket.setSocketAddress(new InetSocketAddress("localhost", port));
			socket.send(sendPacket);
			// System.out.println("已转发数据包至" + port);
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}