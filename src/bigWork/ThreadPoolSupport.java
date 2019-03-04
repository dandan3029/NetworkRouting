package bigWork;

import java.util.*; //����ĳ�������滻ThreadSupport.java
import java.net.*;

public class ThreadPoolSupport implements IOStrategy { // ThreadPoolSupport.java
	private ArrayList threads = new ArrayList();
	private final int INIT_THREADS = 50;
	private final int MAX_THREADS = 100;
	private IOStrategy ios = null;
	byte[] info = null;

	public ThreadPoolSupport(IOStrategy ios) { // �����̳߳�
		this.ios = ios;
		for (int i = 0; i < INIT_THREADS; i++) {
			Recv t = new Recv(ios); // ����Э����󣬵��ǻ�û��socket
			new Thread(t).start(); // �����̣߳������߳������wait
			threads.add(t);
		}
		try {
			Thread.sleep(300);
		} catch (Exception e) {
		} // �ȴ��̳߳ص��̶߳������С�
	}

	public void service(DatagramPacket packet, byte[] info) { // �����̳߳أ��ҵ�һ�����е��̣߳�
		Recv r = null; // �ѿͻ��˽�������������
		boolean found = false;
		for (int i = 0; i < threads.size(); i++) {
			r = (Recv) threads.get(i);
			if (r.isIdle()) {
				found = true;
				break;
			}
		}
		if (!found) // �̳߳��е��̶߳�æ��û�а취�ˣ�ֻ�д���
		{ // һ���߳��ˣ�ͬʱ��ӵ��̳߳��С�
			r = new Recv(ios);
			new Thread(r).start();
			try {
				Thread.sleep(300);
			} catch (Exception e) {
			}
			threads.add(r);
		}
		r.setByteInfo(info);
		r.setPacket(packet); // ���������˵�socket���󴫵ݸ�������е��߳�
	} // ���俪ʼִ��Э�飬��ͻ����ṩ����
}