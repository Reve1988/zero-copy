package kr.revelope.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Receiver extends Thread {
	private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

	private int port;
	private ServerSocketChannel serverSocketChannel;
	private SocketChannel socketChannel;

	private boolean isRunning = true;

	public Receiver(int port) {
		this.port = port;
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

	public void startServer() {
		super.start();
	}

	public void stopServer() {
		this.isRunning = false;
		this.interrupt();
	}

	@Override
	public void run() {
		try {
			initServer();
			runServer();
		} catch (ClosedByInterruptException e) {
			// do Nothing
		} catch (IOException e) {
			LOGGER.error("Server error.", e);
		} finally {
			LOGGER.warn("Server is stopped.");

			close(serverSocketChannel);
			close(socketChannel);
		}
	}

	private void initServer() throws IOException {
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		serverSocketChannel.configureBlocking(true);

		LOGGER.warn("Server is initialized.");
	}

	private void runServer() throws IOException {
		ByteBuffer buffer = ByteBuffer.allocateDirect(4096);

		while (isRunning) {
			socketChannel = serverSocketChannel.accept();
			socketChannel.configureBlocking(true);

			LOGGER.info("Accepted : {}", socketChannel);

			boolean isReadContinue = true;
			while (isReadContinue) {
				try {
					isReadContinue = socketChannel.read(buffer) > -1;
				} catch (ClosedByInterruptException e) {
					isReadContinue = false;
				} catch (IOException e) {
					LOGGER.info("Byte read error.", e);
					isReadContinue = false;
				}

				// data not use
				buffer.rewind();
			}
		}
	}
}
