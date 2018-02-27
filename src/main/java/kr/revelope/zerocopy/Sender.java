package kr.revelope.zerocopy;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sender {
	private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);

	public static void send(String host, int port, String testFilePath, boolean zeroCopy) {
		SocketChannel socketChannel = null;
		FileChannel fileChannel = null;

		try {
			socketChannel = getSocketChannel(host, port);
			fileChannel = getFileChannel(testFilePath);

			long startTime = System.currentTimeMillis();
			long transferSize = send(socketChannel, fileChannel, zeroCopy);
			long endTime = System.currentTimeMillis();

			LOGGER.info("Transferred {}bytes.", transferSize);
			LOGGER.info("Time taken in {}ms.", (endTime - startTime));
		} catch (IOException e) {
			LOGGER.error("Send error", e);
		} finally {
			close(socketChannel);
			close(fileChannel);
		}
	}

	private static long send(SocketChannel socketChannel, FileChannel fileChannel, boolean zeroCopy) throws IOException {
		if (zeroCopy) {
			return fileChannel.transferTo(0, fileChannel.size(), socketChannel);
		} else {
			int transferSize = 0;

			ByteBuffer buffer = ByteBuffer.allocate(4096);
			int numberOfReadBytes = 0;
			while (numberOfReadBytes != -1) {
				numberOfReadBytes = fileChannel.read(buffer);

				socketChannel.write(buffer);
				buffer.clear();

				transferSize += numberOfReadBytes >= 0 ? numberOfReadBytes : 0;
			}

			return transferSize;
		}
	}

	private static FileChannel getFileChannel(String path) throws FileNotFoundException {
		return new FileInputStream(path).getChannel();
	}

	private static SocketChannel getSocketChannel(String host, int port) throws IOException {
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress(host, port));
		socketChannel.configureBlocking(true);

		return socketChannel;
	}

	private static void close(Channel channel) {
		try {
			if (channel != null && channel.isOpen()) {
				channel.close();
			}
		} catch (IOException e) {
			// do Nothing
		}
	}
}
