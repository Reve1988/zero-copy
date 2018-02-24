package kr.revelope.zerocopy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZeroCopyTest {
	private final static Logger LOGGER = LoggerFactory.getLogger(ZeroCopyTest.class);

	public static void main(String[] args) throws InterruptedException {
		Receiver receiver = new Receiver(9001);
		receiver.startServer();

		Thread.sleep(1000);
		test("./test1mb", "1MB");
		Thread.sleep(1000);
		test("./test10mb", "10MB");
//		Thread.sleep(1000);
//		test("./test100mb", "100MB");
//		Thread.sleep(1000);
//		test("./test500mb", "500MB");
//		Thread.sleep(1000);
//		test("./test1gb", "1GB");
//		Thread.sleep(1000);

		receiver.stopServer();
		receiver.join();
	}

	private static void test(String filePath, String fileSize) {
		LOGGER.info("Test {} -----------------------------------------------", fileSize);
		Sender.send("localhost", 9001, filePath, true);
		Sender.send("localhost", 9001, filePath, false);
		LOGGER.info("---------------------------------------------------------");
	}
}
