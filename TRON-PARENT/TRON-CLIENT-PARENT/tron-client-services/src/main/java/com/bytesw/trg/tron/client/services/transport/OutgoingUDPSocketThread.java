package com.bytesw.trg.tron.client.services.transport;

import com.bytesw.trg.core.bo.Usuario;
import com.bytesw.trg.core.dto.Evento;
import com.bytesw.trg.core.dto.Match;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lvasquez
 */
public class OutgoingUDPSocketThread implements Runnable {

        static org.slf4j.Logger logger = LoggerFactory.getLogger(OutgoingUDPSocketThread.class);
        public static final int MESSAGE_LENGTH = 64;

        private List<Usuario> usuarios;
        private Match match;
        private boolean running = true;

        public void stop() {
                running = false;
        }

        @Override
        public void run() {
                while (running) {
                        try {
                                Evento eventoSalida = match.getOutgoingEventQueue().poll();
                                logger.info("checking outgoing queue [" + match.getOutgoingEventQueue().size() + "]");
                                
                                while (eventoSalida != null) {
                                        logger.info("Outgoing message [" + eventoSalida + "]");
                                        Usuario usuario = usuarios.get(0);
                                        DatagramSocket outgoingSocket = new DatagramSocket();
                                        byte[] buffer = new byte[MESSAGE_LENGTH];
                                        String data = "2," + eventoSalida.getUsername() + ","
                                                + String.valueOf(eventoSalida.getTipo()) + ","
                                                + String.valueOf(eventoSalida.getX())
                                                + ","
                                                + String.valueOf(eventoSalida.getY())
                                                + ",";
                                        System.arraycopy(data.getBytes(), 0, buffer, 0, data.getBytes().length);
                                        DatagramPacket packet = new DatagramPacket(buffer, MESSAGE_LENGTH, usuario.getUserAddress(), usuario.getGamePort());
                                        outgoingSocket.send(packet);
                                        eventoSalida = match.getOutgoingEventQueue().poll();
                                }
                                try {
                                        Thread.sleep(50l);
                                } catch (InterruptedException ex) {
                                        logger.error("Error en espera de inicio de juego", ex);
                                }
                        } catch (IOException ex) {
                                logger.error("Error de comunicación", ex);
                        }

                }
        }

        public List<Usuario> getUsuarios() {
                return usuarios;
        }

        public void setUsuarios(List<Usuario> usuarios) {
                this.usuarios = usuarios;
        }

        public Match getMatch() {
                return match;
        }

        public void setMatch(Match match) {
                this.match = match;
        }

        public void setRunning(boolean running) {
                this.running = running;
        }

}
