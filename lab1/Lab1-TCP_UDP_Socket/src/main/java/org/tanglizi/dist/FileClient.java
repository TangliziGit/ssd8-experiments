package org.tanglizi.dist;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * A file client for emit requests or commands to a file server.
 * @author Zhang Chunxu
 */
public class FileClient {

    final static Integer TCP_PORT = 2021;
    final static Integer UDP_PORT = 2020;
    final Integer PACKET_SIZE = 512;
    final String END_STRING = "";

    private String host;
    private Socket socket;
    private PrintWriter remoteWriter;
    private BufferedReader remoteReader;
    private Scanner scanner;

    /**
     * Constructor for FileClient, initialize host and create a TCP connection to FileServer.
     * @param host
     * @throws IOException
     */
    public FileClient(String host) throws IOException {
        this(host, 5000);
    }

    /**
     * Constructor for FileClient, initialize host and create a TCP connection to FileServer,
     *  and thread count of thread pool.
     * The parameter timeout is for connection timeout.
     * @param host
     * @param timeout
     * @throws IOException
     */
    public FileClient(String host, Integer timeout) throws IOException {
        this.host = host;
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(host, TCP_PORT), timeout);
        ThreadPoolUtil.resetThreadCountPerProcessor(2);
    }

    /**
     * Initialize IO reading and writing objects.
     * @throws IOException
     */
    private void initializeIO() throws IOException {
        remoteWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        remoteReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        scanner = new Scanner(System.in);
    }

    /**
     * Start interacting on the file server.
     * @throws IOException
     */
    public void start() throws IOException {
        try {
            initializeIO();
            System.out.println(remoteReader.readLine());

            // show the prompt line, `command >`.
            showPrompt();
            String commandLine, reply, lastReply = null;
            while (scanner.hasNextLine()){
                // the command line which user entered.
                commandLine = scanner.nextLine();

                // send this command to server, and get the reply of the file server.
                remoteWriter.println(commandLine);
                while ((reply = remoteReader.readLine()) != null){
                    if (reply.equals(END_STRING))
                        break;
                    System.out.println(reply);
                    lastReply = reply;
                }

                // parse the command line, get command and it's arguments.
                String[] commandLineParsed = CommandParserUtil.parseCommandLine(commandLine);
                String command = commandLineParsed[0];
                String argument = commandLineParsed[1];

                // if user entered nothing.
                if (null == command)
                    continue;

                // if command is `get`, client should create a UDP connection using new thread from the thread pool.
                // user can get remote file, while entering another command.
                if (command.equals("get")){
                    String finalLastReply = lastReply;
                    ThreadPoolUtil.run(new Runnable() {
                        @Override
                        public void run() {
                            getFile(finalLastReply);
                        }
                    });
                }

                // exit interaction.
                if (command.equals("bye"))
                    break;
                showPrompt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // not forget close client socket, unless it keep the connection for long time.
            if (null != socket)
                socket.close();
            System.exit(0);
        }
    }

    /**
     * Show the prompt line.
     * @throws InterruptedException
     */
    private void showPrompt() throws InterruptedException {
        // sleep for short time waiting other thread to print on same screen.
        Thread.sleep(100);
        System.out.print("command > ");
    }

    /**
     * Get the file on UDP connection using IO stream.
     * @param fileInformation
     */
    private void getFile(String fileInformation) {
        // show the file information for user.
        System.out.println("File information: " + fileInformation);
        // check if there exist the file. if not, exit this connection.
        if (null == fileInformation || fileInformation.equals("no such file")) {
            System.err.println("server has no such file, please check the filename.");
            return;
        }

        // parse the file information.
        String[] information = fileInformation.split(" ");
        String path = information[0];
        String filename = information[1];
        Integer size = Integer.parseInt(information[2]);

        // check if exist a file using same filename on local storage.
        // if necessary, check the md5 value between the remote and local file content.
        File file = new File(filename);
        if (file.exists()){
            System.err.println("same file is in local current directory.");
            return;
        }

        // start receiving the packets, which can be combined as the whole file.
        FileOutputStream fileOutputStream = null;
        DatagramSocket socket = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            socket = new DatagramSocket();
            InetSocketAddress address = new InetSocketAddress(host, UDP_PORT);

            byte[] content = new byte[PACKET_SIZE];
            DatagramPacket packet = new DatagramPacket(path.getBytes(), path.getBytes().length, address);

            // client must send a packet first, so that server can get client's ip and port.
            socket.send(packet);

            // calculate packets count, regardless of packet loss.
            int counter = size / PACKET_SIZE + ((size % PACKET_SIZE > 0)?1:0);
            packet = new DatagramPacket(content, PACKET_SIZE, address);
            while (counter-- > 0){
                socket.receive(packet);
                content = new byte[packet.getLength()];
                System.arraycopy(packet.getData(), 0, content, 0, packet.getLength());

                fileOutputStream.write(content);
            }
            fileOutputStream.flush();

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // remember to close IO stream and socket.
            try {
                if (null != fileOutputStream)
                    fileOutputStream.close();
                if (null != socket)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * The running entry for FileClient
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        new FileClient("localhost").start();
    }
}
