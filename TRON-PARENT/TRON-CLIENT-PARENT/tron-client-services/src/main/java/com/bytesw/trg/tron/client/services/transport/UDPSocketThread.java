package com.bytesw.trg.tron.client.services.transport;

import com.bytesw.trg.core.bo.Usuario;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lvasquez
 */
public class UDPSocketThread implements Runnable {

        static org.slf4j.Logger logger = LoggerFactory.getLogger(ClientSideFilter.class);
        public static final int MESSAGE_LENGTH = 1024;

        private Integer gamePort;
        private List<Usuario> usuarios;
        private DatagramSocket socket;
        byte[] buffer = new byte[MESSAGE_LENGTH];
        private Integer roundtrips;
        private Long roundtripTestInitTime;

        public void init() {
                try {
                        socket = new DatagramSocket(gamePort);
                } catch (SocketException ex) {
                        logger.error("Error iniciando puerto UDP", ex);
                }

        }

        @Override
        public void run() {
                while (true) {
                        DatagramPacket packet = new DatagramPacket(buffer, MESSAGE_LENGTH);
                        try {
                                socket.receive(packet);
                        } catch (IOException ex) {
                                logger.error("Error de comunicación", ex);
                        }
                        String data = new String(buffer);

                }
        }

        public void testRoundTrip() {
                Usuario usuario = usuarios.get(0);
                roundtripTestInitTime = System.currentTimeMillis();
                roundtrips = roundtrips + 1;
                String d0 = "0," + roundtripTestInitTime;
                byte[] messageBuffer = new byte[MESSAGE_LENGTH];
                System.arraycopy(d0.getBytes(), 0, messageBuffer, 0, messageBuffer.length);
                DatagramPacket packet = new DatagramPacket(messageBuffer, MESSAGE_LENGTH, usuario.getUserAddress(), usuario.getGamePort());
                try {
                        socket.send(packet);
                        socket.receive(packet);
                } catch (IOException ex) {
                        Logger.getLogger(UDPSocketThread.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        public void roundTripAnswered(String data) {

        }

        public Integer getGamePort() {
                return gamePort;
        }

        public void setGamePort(Integer gamePort) {
                this.gamePort = gamePort;
        }

        public List<Usuario> getUsuarios() {
                return usuarios;
        }

        public void setUsuarios(List<Usuario> usuarios) {
                this.usuarios = usuarios;
        }

}
