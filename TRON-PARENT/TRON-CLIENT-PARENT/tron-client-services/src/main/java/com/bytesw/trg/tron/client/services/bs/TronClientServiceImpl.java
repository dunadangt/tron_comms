package com.bytesw.trg.tron.client.services.bs;

import com.bytesw.trg.core.dto.Notificacion;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
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
        private Queue<Notificacion> queue = new ConcurrentLinkedQueue();

        @Override
        public void init() {
                try {
                        logger.info("Inicializando cliente");
                        jmDNS = JmDNS.create(InetAddress.getLocalHost());
                        TronServiceListener tronServiceListener = new TronServiceListener();
                        tronServiceListener.setTronClientService(this);
                        jmDNS.addServiceListener(serviceType, tronServiceListener);
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

        @Override
        public void notifyServerFound(String host, Integer port) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        
}