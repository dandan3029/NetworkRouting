package bigWork;

import java.util.Date;

public class Dijkstra implements Runnable {

	int[][] metrix;
	char currNode;
	long PrintPathTime;

	public Dijkstra(long p, int[][] m, char c) {
		PrintPathTime = p;
		metrix = m;
		currNode = c;
	}

	@Override
	public void run() {
		while (true) {
			int index = (int) currNode - 65; // Dijkstra起始节点
			int n = metrix.length; // n为结点个数
			int i, j, k, min, min_index; // min_index为后续距离最近结点的下标
			boolean[] isVisited = new boolean[n]; // 是否需要访问，若不需要false，表明该节点已经OK
			int[] toShort = new int[n]; // 到i点最近的距离
			String[] path = new String[n]; // i结点的前驱结点
			synchronized (metrix) {
//				System.out.println("-----------------------------------------------------");
//				for (i = 0; i < metrix.length; i++) {
//					for (j = 0; j < metrix.length; j++) {
//						if (metrix[i][j] == Integer.MAX_VALUE)
//							System.out.print("Max ");
//						else
//							System.out.print(metrix[i][j] + " ");
//					}
//					System.out.println();
//				}
//				System.out.println("-----------------------------------------------------");
				n = metrix.length;
				for (i = 0; i < n; i++) { // 初始化数据结构
					isVisited[i] = true; // 全都需要访问
					toShort[i] = metrix[index][i]; // 初始到index结点的距离
					path[i] = currNode + ""; // 初始所有结点的前驱结点都为index结点
				}
				// 初始化开始的结点
				isVisited[index] = false;
				toShort[index] = 0;
				// 下面开始迪杰斯特拉算法
				for (i = 1; i < n; i++) {
					min = Integer.MAX_VALUE; // 假设目前最短距离为最大值
					min_index = index; // 假设目前最短距离结点下表为index
					for (k = 0; k < n; k++)
						if (isVisited[k] && toShort[k] < min) { // 如果还不OK，并且距离较小的话，就记录下来
							min = toShort[k];
							min_index = k;
						} // 最后找到此次循环距离最近的结点
					isVisited[min_index] = false; // 将min_index结点加入就绪序列
					// 由刚刚加入的k，下面开始更新各个节点的信息
					for (j = 0; j < n; j++) // 如果加上新来的结点，可以更新目前的最短距离表的话，就更新一下，接下来进入下一次循环
						if (isVisited[j] && metrix[min_index][j] != Integer.MAX_VALUE
								&& (toShort[min_index] + metrix[min_index][j] < toShort[j])) {
							toShort[j] = toShort[min_index] + metrix[min_index][j];
							path[j] = "" + path[min_index] + (char) (min_index + 65);
						}
				}
			}
			System.out.println("-----------------------------------------------------");
			System.out.println(new Date(System.currentTimeMillis()));
			for (i = 0; i < n; i++) {
				if (toShort[i] != Integer.MAX_VALUE && toShort[i] != 0)
					System.out.println("least-cost path to node " + (char) (i + 65) + ": " + path[i] + ""
							+ (char) (i + 65) + "\tand the cost is " + toShort[i]);
			}
			System.out.println("-----------------------------------------------------");
			try {
				Thread.sleep(PrintPathTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
