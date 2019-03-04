package bigWork;
/*
	该类负责接受来自其他节点发送的数据包
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
		this.packet = packet;// 传递给这个阻塞的线程一个“任务”，并唤醒它。
		notify();
	}

	public void setByteInfo(byte[] info) {
		this.info = info;
	}

	@Override
	public synchronized void run() {
		// 以下代码是线程池的实现
		while (true) {
			try {
				wait(); // 进入线程体后，立刻进入阻塞，等待状态
				ios.service(packet, info); // 被唤醒后，立刻开始下一个服务
				packet = null; // 服务结束后，立刻返回到空闲状态
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}