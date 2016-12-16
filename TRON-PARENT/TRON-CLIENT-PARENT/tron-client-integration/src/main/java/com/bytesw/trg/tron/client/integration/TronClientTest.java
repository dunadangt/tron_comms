package com.bytesw.trg.tron.client.integration;

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
                service.init();
//                while (true) {
                        try {
                                Thread.sleep(30000l);
                        } catch (InterruptedException ex) {
                                Logger.getLogger(TronClientTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
//                }
        }
}
