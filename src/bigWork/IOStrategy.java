package bigWork;

public interface IOStrategy { // IOStrategy.java
	public void service(java.net.DatagramPacket packet, byte[] info); // 对传入的packet对象进行处理
}