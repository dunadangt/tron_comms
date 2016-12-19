package com.bytesw.trg.tron.client.services.transport;

import com.bytesw.trg.core.bo.Usuario;
import com.bytesw.trg.core.dto.Evento;
import com.bytesw.trg.core.dto.Match;
import com.bytesw.trg.core.dto.NotificacionServidor;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lvasquez
 */
public class UDPSocketThread implements Runnable {

        static org.slf4j.Logger logger = LoggerFactory.getLogger(UDPSocketThread.class);
        public static final int MESSAGE_LENGTH = 64;

        private Integer gamePort;
        private List<Usuario> usuarios;
        private DatagramSocket socket;
        private Long tripTime;
        private Match match;
        private Queue<NotificacionServidor> notificacionServidorQueue;
        private boolean running = true;

        public void init() {
                try {
                        socket = new DatagramSocket(gamePort);
                } catch (SocketException ex) {
                        logger.error("Error iniciando puerto UDP", ex);
                }
        }

        public void stop() {
                running = false;
        }

        @Override
        public void run() {
                while (running) {
                        try {
                                byte[] buffer = new byte[MESSAGE_LENGTH];
                                DatagramPacket packet = new DatagramPacket(buffer, MESSAGE_LENGTH);
                                socket.receive(packet);
                                String[] data = new String(packet.getData()).split(",");
                                logger.info("Package received [" + packet.getData() + "]");
                                switch (data[0]) {
                                        case "0":
                                                DatagramPacket answerPacket = new DatagramPacket(packet.getData(), MESSAGE_LENGTH, packet.getAddress(), packet.getPort());
                                                answerPacket.setData(packet.getData());
                                                logger.info("Answering roundtrip test");
                                                socket.send(answerPacket);
                                                break;
                                        case "1":
                                                try {
                                                        logger.info("Starting match soon");
                                                        Thread.sleep(Long.valueOf(data[1]));
                                                        start();
                                                } catch (InterruptedException ex) {
                                                        logger.error("Error en espera de inicio de juego", ex);
                                                }
                                                break;
                                        case "2":
                                                Evento evento = new Evento();
                                                evento.setUsername(data[1]);
                                                evento.setTipo(Integer.valueOf(data[2]));
                                                evento.setX(Integer.valueOf(data[3]));
                                                evento.setY(Integer.valueOf(data[4]));
                                                logger.info("Incoming Event[" + evento + "]");
                                                match.getIncomingEventQueue().offer(evento);
                                                
                                                break;
                                }
                        } catch (IOException ex) {
                                logger.error("Error de comunicación", ex);
                        }

                }
        }

        public void testRoundTrip() {
                Usuario usuario = usuarios.get(0);
                long times = 0l;
                int j = 0;
                for (int i = 0; i < 10; i++) {
                        long time0 = System.currentTimeMillis();
                        String d0 = "0," + time0 + ",";
                        byte[] messageBuffer = new byte[MESSAGE_LENGTH];

                        System.arraycopy(d0.getBytes(), 0, messageBuffer, 0, d0.getBytes().length);

                        try {
                                logger.info("Testing rt [" + i + "]");
                                DatagramPacket packet = new DatagramPacket(messageBuffer, MESSAGE_LENGTH, usuario.getUserAddress(), usuario.getGamePort());
                                socket.send(packet);
                                socket.receive(packet);
                                times = times + (System.currentTimeMillis() - time0);
                                j++;
                                logger.info("Tested rt [" + i + "]");
                        } catch (IOException ex) {
                                Logger.getLogger(UDPSocketThread.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
                tripTime = times / (2 * j);
        }

        public void notifyStart() {
                Usuario usuario = usuarios.get(0);
                byte[] messageBuffer = new byte[MESSAGE_LENGTH];
                long time = 2000l - tripTime;
                String d0 = "1," + time + ",";
                System.arraycopy(d0.getBytes(), 0, messageBuffer, 0, d0.getBytes().length);
                try {
                        DatagramPacket packet = new DatagramPacket(messageBuffer, MESSAGE_LENGTH, usuario.getUserAddress(), usuario.getGamePort());
                        socket.send(packet);
                } catch (IOException ex) {
                        Logger.getLogger(UDPSocketThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                        logger.info("Notify start");
                        Thread.sleep(2000l);
                        Thread t = new Thread(this);
                        t.start();
                        start();
                } catch (InterruptedException ex) {
                        logger.error("Error en espera de inicio de juego", ex);
                }
        }

        public void start() {
                match = new Match();
                match.setJugadores(usuarios);
                NotificacionServidor ns = new NotificacionServidor();
                ns.setMatch(match);
                getNotificacionServidorQueue().offer(ns);
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

        public Queue<NotificacionServidor> getNotificacionServidorQueue() {
                return notificacionServidorQueue;
        }

        public void setNotificacionServidorQueue(Queue<NotificacionServidor> notificacionServidorQueue) {
                this.notificacionServidorQueue = notificacionServidorQueue;
        }

        public void setRunning(boolean running) {
                this.running = running;
        }

        public Match getMatch() {
                return match;
        }

        public void setMatch(Match match) {
                this.match = match;
        }

}