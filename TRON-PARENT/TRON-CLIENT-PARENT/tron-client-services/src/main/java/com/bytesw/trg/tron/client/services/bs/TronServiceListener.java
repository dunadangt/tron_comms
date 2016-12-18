package com.bytesw.trg.tron.client.services.bs;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
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
                        getTronClientService().notifyServerFound(se.getInfo().getInetAddresses()[0], se.getInfo().getPort());
                }
        }

        public TronClientService getTronClientService() {
                return tronClientService;
        }

        public void setTronClientService(TronClientService tronClientService) {
                this.tronClientService = tronClientService;
        }

}
