package ssd8.socket.http.client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 *
 * Class <em>HttpClient</em> is a class representing a simple HTTP client.
 *
 * @author wben
 */

public class HttpClient {

	/**
	 * default HTTP port is port 80
	 */
	private static int port = 80;

	/**
	 * Allow a maximum buffer size of 8192 bytes
	 */
	private static int buffer_size = 8192;

	/**
	 * Response is stored in a byte array.
	 */
	private byte[] buffer;

	/**
	 * My socket to the world.
	 */
	Socket socket = null;

	/**
	 * Default port is 80.
	 */
	private static final int PORT = 8080;

	/**
	 * Output stream to the socket.
	 */
	BufferedOutputStream ostream = null;

	/**
	 * Input stream from the socket.
	 */
	BufferedInputStream istream = null;

	/**
	 * StringBuffer storing the header
	 */
	private StringBuffer header = null;

	/**
	 * StringBuffer storing the response.
	 */
	private StringBuffer response = null;

	/**
	 * String to represent the Carriage Return and Line Feed character sequence.
	 */
	static private String CRLF = "\r\n";

	/**
	 * HttpClient constructor;
	 */
	public HttpClient() {
		buffer = new byte[buffer_size];
		header = new StringBuffer();
		response = new StringBuffer();
	}

	/**
	 * <em>connect</em> connects to the input host on the default http port --
	 * port 80. This function opens the socket and creates the input and output
	 * streams used for communication.
	 */
	public void connect(String host) throws Exception {

		/**
		 * Open my socket to the specified host at the default port.
		 */
		socket = new Socket(host, PORT);

		/**
		 * Create the output stream.
		 */
		ostream = new BufferedOutputStream(socket.getOutputStream());

		/**
		 * Create the input stream.
		 */
		istream = new BufferedInputStream(socket.getInputStream());
	}

	/**
	 * <em>processGetRequest</em> process the input GET request.
	 */
	public void processGetRequest(String request) throws Exception {
		/**
		 * Send the request to the server.
		 */
		request += CRLF + CRLF;
		buffer = request.getBytes();
		ostream.write(buffer, 0, request.length());
		ostream.flush();
		/**
		 * waiting for the response.
		 */
		processResponse();
	}

	/**
	 * <em>processPutRequest</em> process the input PUT request.
	 */
	public void processPutRequest(String request) throws Exception {
		String[] requestLineItems = request.split(" ");

		if (requestLineItems.length != 3)
			throw new IllegalArgumentException("request arguments count does not compliance the HTTP. [RTC2068, p.34]");

		String method = requestLineItems[0];
		String requestURI = requestLineItems[1];
		String HttpVersion = requestLineItems[2];

		assert method.equals("PUT");
		if (!HttpVersion.equals("HTTP/1.1"))
			throw new IllegalArgumentException("only HTTP/1.1 support PUT method. [RTD2068, p.52]");

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the file path you want put: ");
		String filePath = reader.readLine();

		File file = new File(filePath);
		if (!file.exists() || !file.isFile()){
			System.err.println("the path does not exist, or is not a file.\n" +
					"Please check it again.");
			return;
		}

		StringBuilder requestMessageBuilder = new StringBuilder(request).append(CRLF);
		requestMessageBuilder.append("Content-Length: ").append(file.length()).append(CRLF);
		requestMessageBuilder.append(CRLF);

		byte[] putBuffer = new byte[(int) (requestMessageBuilder.length() + file.length() )];
		byte[] fileBuffer = new byte[buffer_size];
		int putBufferSize = 0;

		byte[] requestMessageBytes = requestMessageBuilder.toString().getBytes();
		System.arraycopy(requestMessageBytes, 0, putBuffer, 0, requestMessageBytes.length);
		putBufferSize += requestMessageBytes.length;

		InputStream stream = new FileInputStream(file);
		int len;
		while ((len = stream.read(fileBuffer)) != -1) {
			System.arraycopy(fileBuffer, 0, putBuffer, putBufferSize, len);
			putBufferSize += len;
		}

		// ostream.write(putBuffer, 0, putBufferSize);
		ostream.write(putBuffer);
		ostream.flush();
		/**
		 * waiting for the response.
		 */
		processResponse();
	}

	/**
	 * <em>processResponse</em> process the server response.
	 *
	 */
	public void processResponse() throws Exception {
		int last = 0, c = 0;
		/**
		 * Process the header and add it to the header StringBuffer.
		 */
		boolean inHeader = true; // loop control
		while (inHeader && ((c = istream.read()) != -1)) {
			switch (c) {
			case '\r':
				break;
			case '\n':
				if (c == last) {
					inHeader = false;
					break;
				}
				last = c;
				header.append("\n");
				break;
			default:
				last = c;
				header.append((char) c);
			}
		}

		/**
		 * Read the contents and add it to the response StringBuffer.
		 */
		// there is a bug
        /*
		while (istream.read(buffer) != -1) {
			response.append(new String(buffer,"iso-8859-1"));
		}
		*/
		int len;
		while ((len = istream.read(buffer)) != -1) {
		    response.append(new String(buffer, 0, len, StandardCharsets.ISO_8859_1));
		}
	}

	/**
	 * Get the response header.
	 */
	public String getHeader() {
		return header.toString();
	}

	/**
	 * Get the server's response.
	 */
	public String getResponse() {
		return response.toString();
	}

	/**
	 * Close all open connections -- sockets and streams.
	 */
	public void close() throws Exception {
		socket.close();
		istream.close();
		ostream.close();
	}
}
