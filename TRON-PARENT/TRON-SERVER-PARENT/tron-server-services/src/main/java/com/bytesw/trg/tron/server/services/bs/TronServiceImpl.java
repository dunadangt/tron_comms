package com.bytesw.trg.tron.server.services.bs;

import java.io.IOException;
import java.net.InetAddress;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lvasquez
 */
public class TronServiceImpl implements TronService {

        static org.slf4j.Logger logger = LoggerFactory.getLogger(TronServiceImpl.class);

        private Integer serverListenPort;
        private String serviceType;
        private String serviceName;
        private String serviceText;
        private String serviceListenAddress;

        JmDNS jmdns;

        public void init() {
                try {
                        logger.info("Registrando servidor");
                        jmdns = JmDNS.create(InetAddress.getByName(serviceListenAddress));
                        ServiceInfo serviceInfo = ServiceInfo.create(serviceType, serviceName, serverListenPort, serviceText);
                        jmdns.registerService(serviceInfo);
                        logger.info("Servidor registrado [" + serviceInfo + "]");
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

        public String getServiceListenAddress() {
                return serviceListenAddress;
        }

        public void setServiceListenAddress(String serviceListenAddress) {
                this.serviceListenAddress = serviceListenAddress;
        }

}
