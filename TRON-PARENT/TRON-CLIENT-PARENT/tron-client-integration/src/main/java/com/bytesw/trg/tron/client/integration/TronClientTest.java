package com.bytesw.trg.tron.client.integration;

import com.bytesw.trg.core.bo.Usuario;
import com.bytesw.trg.core.dto.AutenticacionRequest;
import com.bytesw.trg.core.dto.ClientServerRequest;
import com.bytesw.trg.tron.client.services.bs.TronClientServiceImpl;
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
                request.getAutenticacionRequest().getUsuario().setUsername("prueba" + System.currentTimeMillis());
                service.writeToServer(request);
                while (true) {
                        try {
                                Thread.sleep(1000l);
                        } catch (InterruptedException ex) {
                                Logger.getLogger(TronClientTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
        }
}
