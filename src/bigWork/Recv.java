package bigWork;
/*
	���ฺ��������������ڵ㷢�͵����ݰ�
 * @see java.lang.Runnable#run()
 */

import java.net.*;

public class Recv implements Runnable {
	private DatagramPacket packet = null;
	private IOStrategy ios = null;
	byte[] info = null;

	public Recv(IOStrategy ios) {
		this.ios = ios;
	}

	public boolean isIdle() {
		return packet == null;
	}

	public synchronized void setPacket(DatagramPacket packet) {
		this.packet = packet;// ���ݸ�����������߳�һ�������񡱣�����������
		notify();
	}

	public void setByteInfo(byte[] info) {
		this.info = info;
	}

	@Override
	public synchronized void run() {
		// ���´������̳߳ص�ʵ��
		while (true) {
			try {
				wait(); // �����߳�������̽����������ȴ�״̬
				ios.service(packet, info); // �����Ѻ����̿�ʼ��һ������
				packet = null; // ������������̷��ص�����״̬
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}