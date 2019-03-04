package bigWork;

import java.util.ArrayList;

/* 解析器 类
 * 负责 1.将 NodeCInfo 数组 转换成 byte 数组
 *     2.将 byte 数组转换成 NodeCInfo
 *
 * @author 董礼业
 * @version 0.0.1
 * */

public class Parser {
	static byte[] toByteArray(ArrayList<NodeCInfo> a) {
		String temp = "7$" + a.get(0).SourceName;
		for (int i = 0; i < a.size(); i++) {
			temp += "$";
			temp += a.get(i).NodeName;
			temp += "$";
			temp += a.get(i).distance;
		}
		return temp.getBytes();
	}

	static ArrayList<NodeCInfo> toNodeCInfoArrayList(byte[] a) {
		ArrayList<NodeCInfo> temp = new ArrayList<>();
		String c = new String(a);
		String[] b = c.split("\\$");
		if (b.length % 2 == 1) {
			System.out.println("收到损毁数据包，内容为\n" + c);
			return null;
		}
		// System.out.println("收到似正常数据包，内容为\n" + c);
		char tempName, sourceNode = b[1].charAt(0);
		int templen;
		for (int i = 2; i < b.length; i++) {
			tempName = b[i++].charAt(0);
			if (b[i].length() > 2)
				templen = Integer.MAX_VALUE;
			else
				templen = Integer.parseInt(b[i]);
			temp.add(new NodeCInfo(sourceNode, tempName, templen, 0));
		}
		return temp;
	}

	// 以下为测试上面两方法的代码
//	public static void main(String[] args) {
//		ArrayList<NodeCInfo> a = new ArrayList<>();
//		a.add(new NodeCInfo('A', 'B', 2111111111, 2000));
//		a.add(new NodeCInfo('A', 'C', 3, 2001));
//		a.add(new NodeCInfo('A', 'D', 5, 2002));
//
//		byte[] b = Parser.toByteArray(a);
//		ArrayList<NodeCInfo> c = Parser.toNodeCInfoArrayList(b);
//
//		for (int i = 0; i < b.length; i++)
//			System.out.print(b[i] + " "); // 输出字符数组的int
//		System.out.println();
//		String d = new String(b);
//		System.out.println(d); // 输出字符数组的char
//
//		for (int i = 0; i < c.size(); i++) // 输出转化回来的内容
//			System.out.println(c.get(i).SourceName + "" + c.get(i).NodeName + " " + c.get(i).distance);
//	}
}
