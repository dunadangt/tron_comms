package com.bytesw.trg.tron.server.services.bs;

import java.io.IOException;
import java.net.InetAddress;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lvasquez
 */
public class TronServiceImpl implements TronService, ServiceListener {

        static org.slf4j.Logger logger = LoggerFactory.getLogger(TronServiceImpl.class);

        private Integer serverListenPort;
        private String serviceType;
        private String serviceName;
        private String serviceText;
        JmDNS jmdns;

        public void init() {
                try {
                        logger.info("Registrando servidor");
                        jmdns = JmDNS.create(InetAddress.getLocalHost());
                        ServiceInfo serviceInfo = ServiceInfo.create(serviceType, serviceName, serverListenPort, serviceText);
                        jmdns.registerService(serviceInfo);
                        logger.info("Servidor registrado");
                } catch (IOException ex) {
                        logger.error("Error de inicializacion.", ex);
                }
        }

        public void destroy() {
                jmdns.unregisterAllServices();
        }

        public Integer getServerListenPort() {
                return serverListenPort;
        }

        public void setServerListenPort(Integer serverListenPort) {
                this.serverListenPort = serverListenPort;
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
