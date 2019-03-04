package bigWork;

import java.net.*;

public class NwRecv { // NwRecv负责接受连接请求，并将创建的Socket对象通过IOStrategy接口传递给ThreadSupport对象
	public NwRecv(int port, IOStrategy ios) {
		try {
			DatagramSocket socket = new DatagramSocket(port);
			while (true) {
				byte[] info = new byte[1024 * 16];
				DatagramPacket packet = new DatagramPacket(info, info.length);// package中的信息读到info中
				socket.receive(packet); // 负责接收发过来的数据包
				ios.service(packet, info); // 将服务器端的socket对象传递给ThreadSupport对象
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}