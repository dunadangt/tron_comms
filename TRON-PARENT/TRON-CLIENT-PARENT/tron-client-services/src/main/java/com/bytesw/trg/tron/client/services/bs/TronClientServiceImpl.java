package com.bytesw.trg.tron.client.services.bs;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jmdns.JmDNS;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lvasquez
 */
public class TronClientServiceImpl implements TronClientService {

        static org.slf4j.Logger logger = LoggerFactory.getLogger(TronClientServiceImpl.class);
        
        private String serviceType;
        private String serviceName;
        private String serviceText;
        private JmDNS jmDNS;

        @Override
        public void init() {
                try {
                        logger.info("Inicializando cliente");
                        jmDNS = JmDNS.create(InetAddress.getLocalHost());
                        jmDNS.addServiceListener(serviceType, new TronServiceListener());
                        logger.info("Cliente inicializado");
                } catch (IOException ex) {
                        logger.error("Error en registro", ex);
                }
        }

        public String getServiceType() {
                return serviceType;
        }

        public void setServiceType(String serviceType) {
                this.serviceType = serviceType;
        }

        public String getServiceName() {
                return serviceName;
        }

        public void setServiceName(String serviceName) {
                this.serviceName = serviceName;
        }

        public String getServiceText() {
                return serviceText;
        }

        public void setServiceText(String serviceText) {
                this.serviceText = serviceText;
        }

}
