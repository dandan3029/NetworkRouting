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
			int index = (int) currNode - 65; // Dijkstra��ʼ�ڵ�
			int n = metrix.length; // nΪ������
			int i, j, k, min, min_index; // min_indexΪ����������������±�
			boolean[] isVisited = new boolean[n]; // �Ƿ���Ҫ���ʣ�������Ҫfalse�������ýڵ��Ѿ�OK
			int[] toShort = new int[n]; // ��i������ľ���
			String[] path = new String[n]; // i����ǰ�����
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
				for (i = 0; i < n; i++) { // ��ʼ�����ݽṹ
					isVisited[i] = true; // ȫ����Ҫ����
					toShort[i] = metrix[index][i]; // ��ʼ��index���ľ���
					path[i] = currNode + ""; // ��ʼ���н���ǰ����㶼Ϊindex���
				}
				// ��ʼ����ʼ�Ľ��
				isVisited[index] = false;
				toShort[index] = 0;
				// ���濪ʼ�Ͻ�˹�����㷨
				for (i = 1; i < n; i++) {
					min = Integer.MAX_VALUE; // ����Ŀǰ��̾���Ϊ���ֵ
					min_index = index; // ����Ŀǰ��̾������±�Ϊindex
					for (k = 0; k < n; k++)
						if (isVisited[k] && toShort[k] < min) { // �������OK�����Ҿ����С�Ļ����ͼ�¼����
							min = toShort[k];
							min_index = k;
						} // ����ҵ��˴�ѭ����������Ľ��
					isVisited[min_index] = false; // ��min_index�������������
					// �ɸոռ����k�����濪ʼ���¸����ڵ����Ϣ
					for (j = 0; j < n; j++) // ������������Ľ�㣬���Ը���Ŀǰ����̾����Ļ����͸���һ�£�������������һ��ѭ��
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
