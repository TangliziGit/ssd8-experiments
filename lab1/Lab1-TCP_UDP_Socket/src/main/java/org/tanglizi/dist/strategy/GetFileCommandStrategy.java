package org.tanglizi.dist.strategy;

import org.tanglizi.dist.ThreadPoolUtil;
import org.tanglizi.dist.FileServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * CommandStrategy for `get` command.
 * @author Zhang Chunxu
 */
public class GetFileCommandStrategy extends CommandStrategy {

    public GetFileCommandStrategy() {
        super();
    }

    /**
     * Concat the new path for an absolute path on root path,
     *  or concat relative path with current path.
     * @param pathArgument
     * @return
     */
    private File getFileWithPathArgument(String pathArgument){
        String path = pathArgument;
        if (pathArgument.length() == 0)
            return rootPath;

        File file;
        if (pathArgument.charAt(0) != '/')
            file =  new File(currentPath, pathArgument);
        else
            file = new File(path);

        if (!file.getPath().startsWith(rootPath.getPath()))
            file = rootPath;
        return file;
    }

    /**
     * Listen for client UDP request, and transfer the given file.
     */
    private void listenUdpAndSendFile() {
        try (DatagramSocket datagramSocket = new DatagramSocket(FileServer.UDP_PORT)) {
            DatagramPacket packet = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);

            // receive the udp datagram, and get client ip and port.
            datagramSocket.receive(packet);
            String path = new String(packet.getData(), 0, packet.getLength());
            File file = new File(path);
            InetSocketAddress address = new InetSocketAddress(packet.getAddress(), packet.getPort());

            byte[] content = new byte[PACKET_SIZE];
            packet = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE, address);

            // check if the file exists.
            if (!file.exists() || !file.isFile()) {
                // if not, send empty line.
                packet.setData("".getBytes());
                datagramSocket.send(packet);
            } else {
                // if exists, send the packets divided from file.
                InputStream stream = new FileInputStream(file);
                int len;
                while ((len = stream.read(content)) != -1) {
                    packet.setData(content);
                    packet.setLength(len);
                    datagramSocket.send(packet);

                    // sleep a short time for client receive packet orderly.
                    TimeUnit.MICROSECONDS.sleep(1);
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if file exists, and start a new thread sending file packets while say file information.
     * @param argument
     * @return
     */
    @Override
    public String process(String argument) {
        File file = getFileWithPathArgument(argument);

        if (!file.exists() || !file.isFile())
            return "no such file\n";

        ThreadPoolUtil.run(new Runnable() {
            @Override
            public void run() {
                listenUdpAndSendFile();
            }
        });

        StringBuilder builder = new StringBuilder();

        builder.append(file.getPath()).append(" ");
        builder.append(file.getName()).append(" ");
        builder.append(file.length()).append("\n");

        return builder.toString();
    }
}
