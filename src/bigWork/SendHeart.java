package bigWork;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class SendHeart implements Runnable {

	private int port;
	private byte[] SendData;
	long HeartBeatTime;
	ArrayList<NodeCInfo> DCNode;
	int index;

	SendHeart(long h, int p, char source, ArrayList<NodeCInfo> d, int i) {
		HeartBeatTime = h;
		port = p;
		SendData = (source + "").getBytes();
		DCNode = d;
		index = i;
	}

	@Override
	public void run() {
		try {
			while (true) { // ��֮ǰ�ж϶Է���û��
				//if (DCNode.get(index).distance != Integer.MAX_VALUE) {
					DatagramSocket socket = new DatagramSocket();
					DatagramPacket sendPacket = new DatagramPacket(SendData, SendData.length);
					sendPacket.setSocketAddress(new InetSocketAddress("localhost", port));
					socket.send(sendPacket);
					// System.out.println("�ѷ�����������" + port);
					socket.close();
				//}
				Thread.sleep(HeartBeatTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}