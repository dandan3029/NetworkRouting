package bigWork;

/* 相连节点的信息 类
 * 
 * 比如 "B 2 2000 AB" 
 * 表示 与B节点相连，距离为2，B的端口号为2000，到B的路径为AB
 * 
 * @author 董礼业 大工软数1601班 201692303
 * @version 0.0.1 
 * */

public class NodeCInfo {

	char SourceName;
	char NodeName;
	int distance;
	int HistoryDistance; // 历史距离，为复活节点做准备
	int port;

	NodeCInfo(char s, char n, int d, int p) {
		SourceName = s;
		NodeName = n;
		distance = d;
		HistoryDistance = d;
		port = p;
	}
}