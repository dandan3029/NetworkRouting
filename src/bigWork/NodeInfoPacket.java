package bigWork;

import java.net.DatagramPacket;
import java.util.*;

public class NodeInfoPacket implements IOStrategy {

	int[][] metrix;
	ArrayList<NodeCInfo> DCNode;
	long[] LastRecvHeartTime;
	long TranspondTime;

	NodeInfoPacket(long t, int[][] m, ArrayList<NodeCInfo> d, long[] L) {
		TranspondTime = t;
		metrix = m;
		DCNode = d;
		LastRecvHeartTime = L;
	}

	@Override
	public void service(DatagramPacket packet, byte[] info) {
		byte[] recvData = new byte[packet.getLength()];
		if (info == null || packet == null) {
			System.out.println("����������");
			return;
		}
		System.arraycopy(info, 0, recvData, 0, recvData.length);

		if (recvData.length == 1) {// ����յ�����������
			if (recvData[0] < 65 || recvData[0] > 90) {// ����������쳣
				System.out.println("�쳣���յ�" + packet.getPort() + "���������������������Ϊ" + (char) recvData[0]);
			} else {
				LastRecvHeartTime[recvData[0] - 65] = System.currentTimeMillis();
				// System.out.println("�յ� " + (char) recvData[0] + " ��������");
				// �������ڵ�֮ǰ���ж����������ʱӦ�ø���
				for (int i = 0; i < DCNode.size(); i++)
					if (DCNode.get(i).NodeName == (char) recvData[0] && DCNode.get(i).distance == Integer.MAX_VALUE) {
						synchronized (DCNode) {
							DCNode.get(i).distance = DCNode.get(i).HistoryDistance;
						}
						synchronized (metrix) {
							metrix[DCNode.get(0).SourceName - 65][recvData[0] - 65] = DCNode.get(i).HistoryDistance;
							metrix[recvData[0] - 65][DCNode.get(0).SourceName - 65] = DCNode.get(i).HistoryDistance;
						}
						System.out.println("�ڵ� " + (char) recvData[0] + " ����");
						break;
					}
			}
		} else { // ����յ����ǽڵ���Ϣ��,���һ��������Ļ�
			recvData[0]--; // ������һ
			ArrayList<NodeCInfo> node = new ArrayList<NodeCInfo>();
			node = Parser.toNodeCInfoArrayList(recvData);
			if (node != null) {// ��ʱ�ڵ�����
				// ����metrix��synchronized������Ҫͬ��
				synchronized (metrix) {
					for (int i = 0; i < node.size(); i++) {
						metrix[node.get(i).SourceName - 65][node.get(i).NodeName - 65] = node.get(i).distance;
						metrix[node.get(i).NodeName - 65][node.get(i).SourceName - 65] = node.get(i).distance;
					}
				}
				// ���ð��������ڽڵ�
				for (int i = 0; i < DCNode.size(); i++) {
					// ���ֱ�������Ľڵ㻹û����������������0�����Ҳ������ð�����Դ�ڵ㣬���ⷺ��
					if (DCNode.get(i).distance < Integer.MAX_VALUE && recvData[0] > 48
							&& packet.getPort() != DCNode.get(i).port && DCNode.get(i).NodeName != (char) recvData[2])
						new Thread(new Transpond(TranspondTime, DCNode.get(i).port, recvData)).start();
				}
			}
		}
	}
}