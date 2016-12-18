package com.bytesw.trg.tron.server.services.bs;

import com.bytesw.trg.tron.server.services.transport.ServerSideFilter;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import org.glassfish.grizzly.filterchain.FilterChainBuilder;
import org.glassfish.grizzly.filterchain.TransportFilter;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.nio.transport.TCPNIOTransportBuilder;
import org.glassfish.grizzly.threadpool.GrizzlyExecutorService;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;
import org.glassfish.grizzly.utils.StringFilter;
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

        private Integer listenerPort;
        private TCPNIOTransport listenerTransport;
        private Integer clientCorePoolSize;
        private Integer clientMaxPoolSize;
        private Integer keepAliveTimeInSeconds;
        private Integer transactionTimeoutInSeconds;
        private Integer corePoolSize;
        private Integer maxPoolSize;
        private Integer poolConnectionTimeoutInSeconds;
        private Integer writeTimeoutInSeconds;
        private Integer clientPoolSize;

        JmDNS jmdns;

        public void init() {
                try {
                        //Listener
                        FilterChainBuilder filterChainBuilderListener = FilterChainBuilder.stateless();
                        filterChainBuilderListener.add(new TransportFilter());
                        filterChainBuilderListener.add(new StringFilter(Charset.forName("UTF-8")));
                        ServerSideFilter serverSideFilter = new ServerSideFilter();
//                        serverSideFilter.setLockEntityServerService(this);
                        filterChainBuilderListener.add(serverSideFilter);

                        listenerTransport = TCPNIOTransportBuilder.newInstance().build();
                        listenerTransport.setProcessor(filterChainBuilderListener.build());
                        ThreadPoolConfig listenerLernelPoolConfig = ThreadPoolConfig.defaultConfig()
                                .setPoolName("BCS-Listener-Thread-Pool").setKeepAliveTime(keepAliveTimeInSeconds, TimeUnit.MINUTES)
                                .setCorePoolSize(corePoolSize).setTransactionTimeout(transactionTimeoutInSeconds, TimeUnit.SECONDS)
                                .setMaxPoolSize(maxPoolSize).setDaemon(true).copy();
                        GrizzlyExecutorService listenerGS = GrizzlyExecutorService
                                .createInstance(listenerLernelPoolConfig);
                        listenerTransport.setWorkerThreadPool(listenerGS);
                        listenerTransport.setWorkerThreadPoolConfig(listenerLernelPoolConfig);
                        try {
                                listenerTransport.bind(listenerPort);
                                listenerTransport.start();

                                logger.info("Listener bound on port [" + listenerPort + "]");
                        } catch (Exception e) {
                                logger.error("Error iniciando listener.", e);
                                throw e;
                        }

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
                try {
                        listenerTransport.shutdownNow();
                } catch (Exception e) {
                        logger.error("Error al cerrar listener", e);
                }
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

        public Integer getListenerPort() {
                return listenerPort;
        }

        public void setListenerPort(Integer listenerPort) {
                this.listenerPort = listenerPort;
        }

        public Integer getClientCorePoolSize() {
                return clientCorePoolSize;
        }

        public void setClientCorePoolSize(Integer clientCorePoolSize) {
                this.clientCorePoolSize = clientCorePoolSize;
        }

        public Integer getClientMaxPoolSize() {
                return clientMaxPoolSize;
        }

        public void setClientMaxPoolSize(Integer clientMaxPoolSize) {
                this.clientMaxPoolSize = clientMaxPoolSize;
        }

        public Integer getKeepAliveTimeInSeconds() {
                return keepAliveTimeInSeconds;
        }

        public void setKeepAliveTimeInSeconds(Integer keepAliveTimeInSeconds) {
                this.keepAliveTimeInSeconds = keepAliveTimeInSeconds;
        }

        public Integer getTransactionTimeoutInSeconds() {
                return transactionTimeoutInSeconds;
        }

        public void setTransactionTimeoutInSeconds(Integer transactionTimeoutInSeconds) {
                this.transactionTimeoutInSeconds = transactionTimeoutInSeconds;
        }

        public Integer getCorePoolSize() {
                return corePoolSize;
        }

        public void setCorePoolSize(Integer corePoolSize) {
                this.corePoolSize = corePoolSize;
        }

        public Integer getMaxPoolSize() {
                return maxPoolSize;
        }

        public void setMaxPoolSize(Integer maxPoolSize) {
                this.maxPoolSize = maxPoolSize;
        }

        public Integer getPoolConnectionTimeoutInSeconds() {
                return poolConnectionTimeoutInSeconds;
        }

        public void setPoolConnectionTimeoutInSeconds(Integer poolConnectionTimeoutInSeconds) {
                this.poolConnectionTimeoutInSeconds = poolConnectionTimeoutInSeconds;
        }

        public Integer getWriteTimeoutInSeconds() {
                return writeTimeoutInSeconds;
        }

        public void setWriteTimeoutInSeconds(Integer writeTimeoutInSeconds) {
                this.writeTimeoutInSeconds = writeTimeoutInSeconds;
        }

        public Integer getClientPoolSize() {
                return clientPoolSize;
        }

        public void setClientPoolSize(Integer clientPoolSize) {
                this.clientPoolSize = clientPoolSize;
        }

}
