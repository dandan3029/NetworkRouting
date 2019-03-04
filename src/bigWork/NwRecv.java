package bigWork;

import java.net.*;

public class NwRecv { // NwRecv��������������󣬲���������Socket����ͨ��IOStrategy�ӿڴ��ݸ�ThreadSupport����
	public NwRecv(int port, IOStrategy ios) {
		try {
			DatagramSocket socket = new DatagramSocket(port);
			while (true) {
				byte[] info = new byte[1024 * 16];
				DatagramPacket packet = new DatagramPacket(info, info.length);// package�е���Ϣ����info��
				socket.receive(packet); // ������շ����������ݰ�
				ios.service(packet, info); // ���������˵�socket���󴫵ݸ�ThreadSupport����
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}