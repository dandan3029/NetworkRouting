package bigWork;

import java.io.FileInputStream;
import java.util.Properties;

/* 节点 类 主程序 包含main函数
 * 加载配置，创建NodeInfo类，开启线程：
 *                          发信息包，发心跳包，计算最短路，检查存活节点，接收数据包
 * 
 * @author 董礼业 大工软数1601班 201692303
 * @author 王丹丹 大工软数1601班 201692354
 * @version 0.0.1
 * */

public class Node {
	public static void main(String[] args) throws Exception {

		long UpdateTime, HeartBeatTime, PrintPathTime, CheckTime, HeartDelayTime;
		Properties properties = new Properties();

		// 读取配置文件
		FileInputStream fis = new FileInputStream("assignment.properties");
		properties.load(fis);
		UpdateTime = Integer.parseInt(properties.getProperty("UpdateTime"));
		HeartBeatTime = Integer.parseInt(properties.getProperty("HeartBeatTime"));
		PrintPathTime = Integer.parseInt(properties.getProperty("PrintPathTime"));
		CheckTime = Integer.parseInt(properties.getProperty("CheckTime"));
		HeartDelayTime = Integer.parseInt(properties.getProperty("HeartDelayTime"));
		System.out.println("已加载 assignment.properties");

		// 命令格式举例: java Node A 2001 configA.txt
		NodeInfo ni = new NodeInfo(args[0], Integer.parseInt(args[1]), args[2]);

		// 向直接相邻节点发送节点信息和心跳包
		for (int i = 0; i < ni.num; i++) {
			new Thread(new Send(UpdateTime, ni.DCNode.get(i).port, ni.DCNode, i)).start();
			new Thread(new SendHeart(HeartBeatTime, ni.DCNode.get(i).port, ni.currNode, ni.DCNode, i)).start();
		}
		// 计算最短路径信息
		new Thread(new Dijkstra(PrintPathTime, ni.metrix, ni.currNode)).start();
		// 检查是否存活
		new Thread(new CheckLive(CheckTime, HeartDelayTime, ni.DCNode, ni.metrix, ni.CurrentNode, ni.LastRecvHeartTime))
				.start();
		// 在该端口接收数据包
		new NwRecv(ni.port,
				new ThreadPoolSupport(new NodeInfoPacket(UpdateTime, ni.metrix, ni.DCNode, ni.LastRecvHeartTime)));
	}
}