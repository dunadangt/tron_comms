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
                Inet4Address[] addresses = se.getInfo().getInet4Addresses();
                for (Inet4Address address : addresses) {
                        logger.info("Address [" + address.getHostAddress() + "] [" + address.getHostName() + "]");
                }
                for (InetAddress address : se.getInfo().getInetAddresses()) {
                        logger.info("Address [" + address.getHostAddress() + "] [" + address.getHostName() + "]");
                }
                for (Inet6Address address : se.getInfo().getInet6Addresses()) {
                        logger.info("Address [" + address.getHostAddress() + "] [" + address.getHostName() + "]");
                }
        }

        @Override
        public void serviceRemoved(ServiceEvent se) {
                logger.info("Service Removed [" + se.getInfo() + "]");
        }

        @Override
        public void serviceResolved(ServiceEvent se) {
                logger.info("Service Resolved [" + se.getInfo() + "]");
                Inet4Address[] addresses = se.getInfo().getInet4Addresses();
                for (Inet4Address address : addresses) {
                        logger.info("Address [" + address.getHostAddress() + "] [" + address.getHostName() + "]");
                }
        }

        public TronClientService getTronClientService() {
                return tronClientService;
        }

        public void setTronClientService(TronClientService tronClientService) {
                this.tronClientService = tronClientService;
        }

}
