package kr.revelope.zerocopy;

public class Main {
	private final static String SERVER = "server";
	private final static String CLIENT = "client";

	public static void main(String[] arguments) throws InterruptedException {
		if (isInvalid(arguments)) {
			System.out.println("Usage : java -jar [jarfile] [server|client] [options]");
			return;
		}

		if (SERVER.equals(arguments[0])) {
			System.out.println("Server Usage : java -jar [jarfile] server port");

			int port = Integer.parseInt(arguments[1]);

			Receiver receiver = new Receiver(port);
			receiver.startServer();

			return;
		}

		if (CLIENT.equals(arguments[0])) {
			System.out.println("Client Usage : java -jar [jarfile] host:port filePath zerocopy");

			String host = arguments[1].split(":")[0];
			int port = Integer.parseInt(arguments[1].split(":")[1]);
			String path = arguments[2];
			boolean zeroCopy = Boolean.getBoolean(arguments[3]);

			Sender.send(host, port, path, zeroCopy);
		}
	}

	private static boolean isInvalid(String[] args) {
		if (args == null) {
			return true;
		}

		if (args.length <= 0) {
			return true;
		}

		if (!SERVER.equals(args[0]) && !CLIENT.equals(args[0])) {
			return true;
		}

		return false;
	}
}
