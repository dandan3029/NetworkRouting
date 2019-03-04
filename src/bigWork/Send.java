package bigWork;

/* �����Լ�ֱ�������Ľڵ���Ϣ ��
 * ����ϣ�һֱ����
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
			while (true) {// ��֮ǰ�ж϶Է���û��
				if (DCNode.get(index).distance != Integer.MAX_VALUE) {
					SendData = Parser.toByteArray(DCNode);
					DatagramSocket socket = new DatagramSocket();
					DatagramPacket sendPacket = new DatagramPacket(SendData, SendData.length);
					sendPacket.setSocketAddress(new InetSocketAddress("localhost", port)); // ���ݰ���Ҫȥ��ĵ�ַ
					socket.send(sendPacket);
					// System.out.println("�ѷ������ݰ���" + port);
					socket.close();
				}
				Thread.sleep(UpdateTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}