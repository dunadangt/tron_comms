package com.bytesw.trg.tron.client.services.bs;

import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lvasquez
 */
public class TronServiceListener implements ServiceListener {

        static org.slf4j.Logger logger = LoggerFactory.getLogger(TronServiceListener.class);
        private TronClientService tronClientService;

        @Override
        public void serviceAdded(ServiceEvent se) {
                logger.info("Service Added [" + se.getInfo() + "]");
        }

        @Override
        public void serviceRemoved(ServiceEvent se) {
                logger.info("Service Removed [" + se.getInfo() + "]");
        }

        @Override
        public void serviceResolved(ServiceEvent se) {
                logger.info("Service Resolved [" + se.getInfo() + "]");
                if (se.getInfo().getInetAddresses() != null && se.getInfo().getInetAddresses().length > 0) {
                        try {
                                InetAddress localAddress = ((javax.jmdns.impl.JmDNSImpl) se.getSource()).getLocalHost().getInetAddress();
                                getTronClientService().notifyServerFound(se.getInfo().getInetAddresses()[0], se.getInfo().getPort(), localAddress);
                        } catch (Exception ex) {
                                logger.error("Error al iniciar conexion al servidor [" + ex.getMessage() + "]", ex);
                        }
                }
        }

        public TronClientService getTronClientService() {
                return tronClientService;
        }

        public void setTronClientService(TronClientService tronClientService) {
                this.tronClientService = tronClientService;
        }

}
