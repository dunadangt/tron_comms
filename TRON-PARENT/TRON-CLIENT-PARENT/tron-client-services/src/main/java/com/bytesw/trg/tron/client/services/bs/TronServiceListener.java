package com.bytesw.trg.tron.client.services.bs;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lvasquez
 */
public class TronServiceListener implements ServiceListener {

        static org.slf4j.Logger logger = LoggerFactory.getLogger(TronServiceListener.class);
        
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
        }

        
}
