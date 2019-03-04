package bigWork;

/* 发与自己直接相连的节点信息 包
 * 不间断，一直发送
 * */

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class Send implements Runnable {

	int port;
	ArrayList<NodeCInfo> DCNode;
	long UpdateTime;
	int index;

	Send(long u, int p, ArrayList<NodeCInfo> d, int i) {
		UpdateTime = u;
		port = p;
		DCNode = d;
		index = i;
	}

	@Override
	public void run() {
		byte[] SendData;
		try {
			while (true) {// 发之前判断对方死没死
				if (DCNode.get(index).distance != Integer.MAX_VALUE) {
					SendData = Parser.toByteArray(DCNode);
					DatagramSocket socket = new DatagramSocket();
					DatagramPacket sendPacket = new DatagramPacket(SendData, SendData.length);
					sendPacket.setSocketAddress(new InetSocketAddress("localhost", port)); // 数据包将要去向的地址
					socket.send(sendPacket);
					// System.out.println("已发送数据包至" + port);
					socket.close();
				}
				Thread.sleep(UpdateTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}