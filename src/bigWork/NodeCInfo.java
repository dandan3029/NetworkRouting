package bigWork;

/* �����ڵ����Ϣ ��
 * 
 * ���� "B 2 2000 AB" 
 * ��ʾ ��B�ڵ�����������Ϊ2��B�Ķ˿ں�Ϊ2000����B��·��ΪAB
 * 
 * @author ����ҵ ������1601�� 201692303
 * @version 0.0.1 
 * */

public class NodeCInfo {

	char SourceName;
	char NodeName;
	int distance;
	int HistoryDistance; // ��ʷ���룬Ϊ����ڵ���׼��
	int port;

	NodeCInfo(char s, char n, int d, int p) {
		SourceName = s;
		NodeName = n;
		distance = d;
		HistoryDistance = d;
		port = p;
	}
}