package bigWork;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/* 节点信息 类
 * 
 * 数据域：相连节点的个数，相连节点信息链表
 * 方法：收发节点信息，收发心跳包，计算最短路径
 * 
 * @author 董礼业 大工软数1601班 201692303
 *         王丹丹 大工软数1601班 201692354
 * @version 0.0.1
 * */

public class NodeInfo {

	final int NODE_NUM = 8; // 假设一共有这些节点

	char currNode; // 当前节点名称
	int port; // 当前节点端口号
	int num; // 相连的节点个数
	boolean[] CurrentNode = new boolean[NODE_NUM]; // 当前直接连接的节点
	long[] LastRecvHeartTime = new long[NODE_NUM]; // 最后接收心跳包的时间
	ArrayList<NodeCInfo> DCNode = new ArrayList<>(); // 直接相连节点信息，初始值从config.txt加载
	int[][] metrix; // Dijkstra所需矩阵

	public NodeInfo(String name, int p, String filename) throws IOException {
		currNode = name.charAt(0);
		port = p;
		loadConfigx(filename);// 加载name节点的信息

		// 初始化Dijkstra矩阵
		metrix = new int[NODE_NUM][NODE_NUM];
		for (int i = 0; i < NODE_NUM; i++) // 初始化每条路径长为100
			for (int j = 0; j < NODE_NUM; j++)
				metrix[i][j] = Integer.MAX_VALUE;
		updateDij();
		System.out.println("已加载 " + filename);
	}

	void loadConfigx(String filename) throws IOException { // 加载节点信息 比如 configA.txt
		char tempname;
		int templength, tempport;
		String temp = "";
		FileReader fr = new FileReader(filename);
		num = fr.read() - 48; // 读第一个字符
		int mm = fr.read(); // 为五位数的端口号做准备
		while (mm != -1) { // 读一个回车
			fr.read(); // 读一个换行
			tempname = (char) fr.read(); // 读名字
			fr.read(); // 读一个空格
			templength = fr.read() - 48; // 读路径长
			fr.read(); // 读一个空格
			temp += (char) fr.read(); // 读端口号的第一个字
			temp += (char) fr.read(); // 读端口号的第二个字
			temp += (char) fr.read(); // 读端口号的第三个字
			temp += (char) fr.read(); // 读端口号的第四个字
			mm = fr.read();
			if (mm > 47 && mm < 58) { // 读端口号的第五个字
				temp += (char) mm;
				fr.read();
			}
			tempport = Integer.parseInt(temp); // 把组装好的端口号转化成int
			CurrentNode[(int) tempname - 65] = true;
			DCNode.add(new NodeCInfo(currNode, tempname, templength, tempport));
			temp = "";
		}
		fr.close();
	}

	void updateDij() { // 更新Dijkstra矩阵
		for (int i = 0; i < num; i++) {
			metrix[(int) currNode - 65][(int) DCNode.get(i).NodeName - 65] = DCNode.get(i).distance;
			metrix[(int) DCNode.get(i).NodeName - 65][(int) currNode - 65] = DCNode.get(i).distance;
		}
	}
}