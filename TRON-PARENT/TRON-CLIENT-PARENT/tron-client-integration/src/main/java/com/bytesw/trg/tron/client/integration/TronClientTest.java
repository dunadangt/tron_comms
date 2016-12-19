package com.bytesw.trg.tron.client.integration;

import com.bytesw.trg.core.bo.Usuario;
import com.bytesw.trg.core.dto.AutenticacionRequest;
import com.bytesw.trg.core.dto.ClientServerRequest;
import com.bytesw.trg.core.dto.Evento;
import com.bytesw.trg.core.dto.Punto;
import com.bytesw.trg.tron.client.services.bs.TronClientServiceImpl;
import com.bytesw.trg.tron.client.services.transport.UDPSocketThread;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lvasquez
 */
public class TronClientTest {

        public static void main(String[] args) {
                TronClientServiceImpl service = new TronClientServiceImpl();
                service.setServiceType("_tron._tcp.local.");
                service.setServerListenAddress("172.16.5.55");
                service.setServerListenPort(7777);
                service.init();
                while (service.getServerNotificationQueue().poll() == null) {
                        try {
                                Thread.sleep(500l);
                        } catch (InterruptedException ex) {
                                Logger.getLogger(TronClientTest.class.getName()).log(Level.SEVERE, null, ex);
                        }

                }
                ClientServerRequest request = new ClientServerRequest();
                request.setAutenticacionRequest(new AutenticacionRequest());
                request.getAutenticacionRequest().setUsuario(new Usuario());
                String username = "prueba" + System.currentTimeMillis();
                request.getAutenticacionRequest().getUsuario().setUsername(username);
                service.writeToServer(request);
                while (true) {
                        try {
                                Thread.sleep(1000l);
                                if (UDPSocketThread.currentMatch != null) {
                                        System.out.println("Ya existe match");
                                        Evento evento = new Evento();
                                        evento.setTipo(1);
                                        evento.setUsername(username);
                                        evento.setX(10);
                                        evento.setY(16);
                                        Punto punto1 = new Punto();
                                        punto1.setX(1);
                                        punto1.setY(1);
                                        Punto punto2 = new Punto();
                                        punto2.setX(1);
                                        punto2.setY(1);
                                        
//                                        evento.getPuntos().add(punto1);
//                                        evento.getPuntos().add(punto2);
                                        evento.setSequence(100l);
                                        System.out.println("Enviando evento [" +  evento + "]");
                                        System.out.println("Enviando evento [" +  UDPSocketThread.currentMatch.getOutgoingEventQueue().size() + "]");
                                        UDPSocketThread.currentMatch.getOutgoingEventQueue().offer(evento);
                                }
                        } catch (InterruptedException ex) {
                                Logger.getLogger(TronClientTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
        }
}
