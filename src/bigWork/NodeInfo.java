package bigWork;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/* �ڵ���Ϣ ��
 * 
 * �����������ڵ�ĸ����������ڵ���Ϣ����
 * �������շ��ڵ���Ϣ���շ����������������·��
 * 
 * @author ����ҵ ������1601�� 201692303
 *         ������ ������1601�� 201692354
 * @version 0.0.1
 * */

public class NodeInfo {

	final int NODE_NUM = 8; // ����һ������Щ�ڵ�

	char currNode; // ��ǰ�ڵ�����
	int port; // ��ǰ�ڵ�˿ں�
	int num; // �����Ľڵ����
	boolean[] CurrentNode = new boolean[NODE_NUM]; // ��ǰֱ�����ӵĽڵ�
	long[] LastRecvHeartTime = new long[NODE_NUM]; // ��������������ʱ��
	ArrayList<NodeCInfo> DCNode = new ArrayList<>(); // ֱ�������ڵ���Ϣ����ʼֵ��config.txt����
	int[][] metrix; // Dijkstra�������

	public NodeInfo(String name, int p, String filename) throws IOException {
		currNode = name.charAt(0);
		port = p;
		loadConfigx(filename);// ����name�ڵ����Ϣ

		// ��ʼ��Dijkstra����
		metrix = new int[NODE_NUM][NODE_NUM];
		for (int i = 0; i < NODE_NUM; i++) // ��ʼ��ÿ��·����Ϊ100
			for (int j = 0; j < NODE_NUM; j++)
				metrix[i][j] = Integer.MAX_VALUE;
		updateDij();
		System.out.println("�Ѽ��� " + filename);
	}

	void loadConfigx(String filename) throws IOException { // ���ؽڵ���Ϣ ���� configA.txt
		char tempname;
		int templength, tempport;
		String temp = "";
		FileReader fr = new FileReader(filename);
		num = fr.read() - 48; // ����һ���ַ�
		int mm = fr.read(); // Ϊ��λ���Ķ˿ں���׼��
		while (mm != -1) { // ��һ���س�
			fr.read(); // ��һ������
			tempname = (char) fr.read(); // ������
			fr.read(); // ��һ���ո�
			templength = fr.read() - 48; // ��·����
			fr.read(); // ��һ���ո�
			temp += (char) fr.read(); // ���˿ںŵĵ�һ����
			temp += (char) fr.read(); // ���˿ںŵĵڶ�����
			temp += (char) fr.read(); // ���˿ںŵĵ�������
			temp += (char) fr.read(); // ���˿ںŵĵ��ĸ���
			mm = fr.read();
			if (mm > 47 && mm < 58) { // ���˿ںŵĵ������
				temp += (char) mm;
				fr.read();
			}
			tempport = Integer.parseInt(temp); // ����װ�õĶ˿ں�ת����int
			CurrentNode[(int) tempname - 65] = true;
			DCNode.add(new NodeCInfo(currNode, tempname, templength, tempport));
			temp = "";
		}
		fr.close();
	}

	void updateDij() { // ����Dijkstra����
		for (int i = 0; i < num; i++) {
			metrix[(int) currNode - 65][(int) DCNode.get(i).NodeName - 65] = DCNode.get(i).distance;
			metrix[(int) DCNode.get(i).NodeName - 65][(int) currNode - 65] = DCNode.get(i).distance;
		}
	}
}