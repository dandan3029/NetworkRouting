package bigWork;

import java.io.FileInputStream;
import java.util.Properties;

/* �ڵ� �� ������ ����main����
 * �������ã�����NodeInfo�࣬�����̣߳�
 *                          ����Ϣ���������������������·�������ڵ㣬�������ݰ�
 * 
 * @author ����ҵ ������1601�� 201692303
 * @author ������ ������1601�� 201692354
 * @version 0.0.1
 * */

public class Node {
	public static void main(String[] args) throws Exception {

		long UpdateTime, HeartBeatTime, PrintPathTime, CheckTime, HeartDelayTime;
		Properties properties = new Properties();

		// ��ȡ�����ļ�
		FileInputStream fis = new FileInputStream("assignment.properties");
		properties.load(fis);
		UpdateTime = Integer.parseInt(properties.getProperty("UpdateTime"));
		HeartBeatTime = Integer.parseInt(properties.getProperty("HeartBeatTime"));
		PrintPathTime = Integer.parseInt(properties.getProperty("PrintPathTime"));
		CheckTime = Integer.parseInt(properties.getProperty("CheckTime"));
		HeartDelayTime = Integer.parseInt(properties.getProperty("HeartDelayTime"));
		System.out.println("�Ѽ��� assignment.properties");

		// �����ʽ����: java Node A 2001 configA.txt
		NodeInfo ni = new NodeInfo(args[0], Integer.parseInt(args[1]), args[2]);

		// ��ֱ�����ڽڵ㷢�ͽڵ���Ϣ��������
		for (int i = 0; i < ni.num; i++) {
			new Thread(new Send(UpdateTime, ni.DCNode.get(i).port, ni.DCNode, i)).start();
			new Thread(new SendHeart(HeartBeatTime, ni.DCNode.get(i).port, ni.currNode, ni.DCNode, i)).start();
		}
		// �������·����Ϣ
		new Thread(new Dijkstra(PrintPathTime, ni.metrix, ni.currNode)).start();
		// ����Ƿ���
		new Thread(new CheckLive(CheckTime, HeartDelayTime, ni.DCNode, ni.metrix, ni.CurrentNode, ni.LastRecvHeartTime))
				.start();
		// �ڸö˿ڽ������ݰ�
		new NwRecv(ni.port,
				new ThreadPoolSupport(new NodeInfoPacket(UpdateTime, ni.metrix, ni.DCNode, ni.LastRecvHeartTime)));
	}
}