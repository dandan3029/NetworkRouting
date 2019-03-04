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
			System.out.println("丢包！！！");
			return;
		}
		System.arraycopy(info, 0, recvData, 0, recvData.length);

		if (recvData.length == 1) {// 如果收到的是心跳包
			if (recvData[0] < 65 || recvData[0] > 90) {// 如果心跳包异常
				System.out.println("异常！收到" + packet.getPort() + "发来的损毁心跳包，内容为" + (char) recvData[0]);
			} else {
				LastRecvHeartTime[recvData[0] - 65] = System.currentTimeMillis();
				// System.out.println("收到 " + (char) recvData[0] + " 的心跳包");
				// 如果这个节点之前被判定死亡，则此时应该复活
				for (int i = 0; i < DCNode.size(); i++)
					if (DCNode.get(i).NodeName == (char) recvData[0] && DCNode.get(i).distance == Integer.MAX_VALUE) {
						synchronized (DCNode) {
							DCNode.get(i).distance = DCNode.get(i).HistoryDistance;
						}
						synchronized (metrix) {
							metrix[DCNode.get(0).SourceName - 65][recvData[0] - 65] = DCNode.get(i).HistoryDistance;
							metrix[recvData[0] - 65][DCNode.get(0).SourceName - 65] = DCNode.get(i).HistoryDistance;
						}
						System.out.println("节点 " + (char) recvData[0] + " 复活");
						break;
					}
			}
		} else { // 如果收到的是节点信息包,并且还有跳数的话
			recvData[0]--; // 跳数减一
			ArrayList<NodeCInfo> node = new ArrayList<NodeCInfo>();
			node = Parser.toNodeCInfoArrayList(recvData);
			if (node != null) {// 临时节点数组
				// 更新metrix，synchronized这里需要同步
				synchronized (metrix) {
					for (int i = 0; i < node.size(); i++) {
						metrix[node.get(i).SourceName - 65][node.get(i).NodeName - 65] = node.get(i).distance;
						metrix[node.get(i).NodeName - 65][node.get(i).SourceName - 65] = node.get(i).distance;
					}
				}
				// 将该包传给相邻节点
				for (int i = 0; i < DCNode.size(); i++) {
					// 如果直接相连的节点还没死，并且跳数大于0，并且不发给该包的来源节点，避免泛洪
					if (DCNode.get(i).distance < Integer.MAX_VALUE && recvData[0] > 48
							&& packet.getPort() != DCNode.get(i).port && DCNode.get(i).NodeName != (char) recvData[2])
						new Thread(new Transpond(TranspondTime, DCNode.get(i).port, recvData)).start();
				}
			}
		}
	}
}