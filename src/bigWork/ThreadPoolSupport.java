package bigWork;

import java.util.*; //下面的程序可以替换ThreadSupport.java
import java.net.*;

public class ThreadPoolSupport implements IOStrategy { // ThreadPoolSupport.java
	private ArrayList threads = new ArrayList();
	private final int INIT_THREADS = 50;
	private final int MAX_THREADS = 100;
	private IOStrategy ios = null;
	byte[] info = null;

	public ThreadPoolSupport(IOStrategy ios) { // 创建线程池
		this.ios = ios;
		for (int i = 0; i < INIT_THREADS; i++) {
			Recv t = new Recv(ios); // 传递协议对象，但是还没有socket
			new Thread(t).start(); // 启动线程，进入线程体后都是wait
			threads.add(t);
		}
		try {
			Thread.sleep(300);
		} catch (Exception e) {
		} // 等待线程池的线程都“运行”
	}

	public void service(DatagramPacket packet, byte[] info) { // 遍历线程池，找到一个空闲的线程，
		Recv r = null; // 把客户端交给“它”处理
		boolean found = false;
		for (int i = 0; i < threads.size(); i++) {
			r = (Recv) threads.get(i);
			if (r.isIdle()) {
				found = true;
				break;
			}
		}
		if (!found) // 线程池中的线程都忙，没有办法了，只有创建
		{ // 一个线程了，同时添加到线程池中。
			r = new Recv(ios);
			new Thread(r).start();
			try {
				Thread.sleep(300);
			} catch (Exception e) {
			}
			threads.add(r);
		}
		r.setByteInfo(info);
		r.setPacket(packet); // 将服务器端的socket对象传递给这个空闲的线程
	} // 让其开始执行协议，向客户端提供服务
}