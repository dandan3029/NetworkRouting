package bigWork;

/* ���ڵ��Ƿ�
 * 
 * ��Ҫͨ���������һ�ν�����������ʱ���뵱ǰ��ʱ����ж��Ƿ�����
 * ֻ���ж�ֱ�������ڵ��Ƿ�����
 * Ȼ�����ֱ����Ϣ����DCNode��Dijsktra����
 * 
 * @author ����ҵ
 * @version 0.0.1
 * */

import java.util.ArrayList;

public class CheckLive implements Runnable {

	private long CheckTime;
	private ArrayList<NodeCInfo> DCNode; // ֱ�������ڵ����Ϣ
	private int[][] metrix; // Dijkstra����
	private boolean[] CurrentNode; // ��ǰֱ�������Ľڵ㣬ֻ�ڳ�ʼ��ʱ��ֵ
	private long[] LastRecvHeartTime; // ����յ���������ʱ��
	private long HeartDelayTime; // ���������Щʱ�仹û�յ��������Ϳ��ж�����

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
				// �����ֱ�������Ľڵ㣬������������ʱ�Ļ������ж�Ϊ����,
				// ����Ѿ������������Ϣ�Ͳ����ظ����
				if (CurrentNode[i] && System.currentTimeMillis() - LastRecvHeartTime[i] > HeartDelayTime) {
					for (int j = 0; j < DCNode.size(); j++) {
						if (DCNode.get(j).NodeName == (i + 65) && DCNode.get(j).distance != Integer.MAX_VALUE) {
							synchronized (DCNode) {
								DCNode.get(j).distance = Integer.MAX_VALUE;
							}
							System.out.println("�ڵ� " + (char) (i + 65) + " ����");
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