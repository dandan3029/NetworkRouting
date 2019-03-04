package bigWork;

/* 检查节点是否活动
 * 
 * 需要通过计算最后一次接收心跳包的时间与当前的时间差判断是否消亡
 * 只能判定直接相连节点是否消亡
 * 然后更改直连信息链表DCNode和Dijsktra矩阵
 * 
 * @author 董礼业
 * @version 0.0.1
 * */

import java.util.ArrayList;

public class CheckLive implements Runnable {

	private long CheckTime;
	private ArrayList<NodeCInfo> DCNode; // 直接相连节点的信息
	private int[][] metrix; // Dijkstra矩阵
	private boolean[] CurrentNode; // 当前直接相连的节点，只在初始化时赋值
	private long[] LastRecvHeartTime; // 最后收到心跳包的时间
	private long HeartDelayTime; // 如果经过这些时间还没收到心跳包就可判定死亡

	CheckLive(long e, long h, ArrayList<NodeCInfo> c, int[][] b, boolean[] d, long[] a) {
		CheckTime = e;
		HeartDelayTime = h;
		DCNode = c;
		metrix = b;
		CurrentNode = d;
		LastRecvHeartTime = a;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(CheckTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < metrix.length; i++)
				// 如果是直接相连的节点，并且心跳包超时的话，就判定为死亡,
				// 如果已经输出过消亡信息就不再重复输出
				if (CurrentNode[i] && System.currentTimeMillis() - LastRecvHeartTime[i] > HeartDelayTime) {
					for (int j = 0; j < DCNode.size(); j++) {
						if (DCNode.get(j).NodeName == (i + 65) && DCNode.get(j).distance != Integer.MAX_VALUE) {
							synchronized (DCNode) {
								DCNode.get(j).distance = Integer.MAX_VALUE;
							}
							System.out.println("节点 " + (char) (i + 65) + " 消亡");
							break;
						}
					}
					for (int j = 0; j < metrix.length; j++) {
						synchronized (metrix) {
							metrix[i][j] = Integer.MAX_VALUE;
							metrix[j][i] = Integer.MAX_VALUE;
						}
					}
				}
		}
	}
}