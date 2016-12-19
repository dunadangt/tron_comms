package com.bytesw.trg.tron.client.services.bs;

import com.bytesw.trg.core.dto.ClientServerRequest;
import com.bytesw.trg.core.dto.NotificacionServidor;
import com.bytesw.trg.core.dto.NotifyServerLocation;
import com.bytesw.trg.tron.client.services.transport.ClientSideFilter;
import com.bytesw.trg.tron.client.services.transport.OutgoingUDPSocketThread;
import com.bytesw.trg.tron.client.services.transport.UDPSocketThread;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jmdns.JmDNS;
import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.connectionpool.SingleEndpointPool;
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
public class TronClientServiceImpl implements TronClientService {

        static org.slf4j.Logger logger = LoggerFactory.getLogger(TronClientServiceImpl.class);

        private String serviceType;
        private String serviceName;
        private String serviceText;
        private JmDNS jmDNS;
        private final Queue<NotificacionServidor> queue = new ConcurrentLinkedQueue();
        private NotifyServerLocation serverLocation;

        private TCPNIOTransport transport;
        private SingleEndpointPool pool;
        private Integer serverTransportTimeoutInSeconds = 1;
        private Integer serverConnectionTimeoutInSeconds = 1;
        private Integer serverConnectionKeepAliveTimeInSeconds = 1;
        private Integer serverMaxPoolSize = 1;
        private Integer serverMinPoolSize = 1;
        private Integer clientListenerMaxPoolSize = 1;
        private Integer clientListenerMinPoolSize = 1;

        private TCPNIOTransport filterTransport;

        private InetAddress clientListenerHost;
        private Integer clientListenerPort;
        private Integer gamePort;
        private UDPSocketThread udpst;
        private OutgoingUDPSocketThread oust;
        private String serverListenAddress;
        private Integer serverListenPort;

        @Override
        public void init() {
                try {
                        //                try {
//                        logger.info("Inicializando cliente");
//                        jmDNS = JmDNS.create(InetAddress.getLocalHost());
//                        TronServiceListener tronServiceListener = new TronServiceListener();
//                        tronServiceListener.setTronClientService(this);
//                        jmDNS.addServiceListener(serviceType, tronServiceListener);
//                        logger.info("Cliente inicializado");
//                } catch (IOException ex) {
//                        logger.error("Error en registro", ex);
//                }
                        notifyServerFound(InetAddress.getByName(serverListenAddress), serverListenPort, InetAddress.getLocalHost());
                } catch (Exception ex) {
                        logger.error("Error iniciando servicio", ex);
                }
        }

        @Override
        public void destroy() {
                try {
                        transport.shutdownNow();
                } catch (IOException ex) {
                        logger.error("Error destruyendo transporte", ex);
                }
                try {
                        filterTransport.shutdownNow();
                } catch (IOException ex) {
                        logger.error("Error destruyendo transporte", ex);
                }
                oust.setRunning(false);
                udpst.setRunning(false);
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
        public void notifyServerFound(InetAddress host, Integer port, InetAddress localhost) throws Exception {
                serverLocation = new NotifyServerLocation();
                serverLocation.setServerAddress(host);
                serverLocation.setPort(port);
                clientListenerHost = localhost;
                //Cliente
                FilterChainBuilder filterChainBuilder = FilterChainBuilder.stateless();
                filterChainBuilder.add(new TransportFilter());
                filterChainBuilder.add(new StringFilter(Charset.forName("UTF-8")));
                ClientSideFilter filter = new ClientSideFilter();
                filter.setAnswerQueue(queue);
                filterChainBuilder.add(filter);

                transport = TCPNIOTransportBuilder.newInstance().build();
                transport.setProcessor(filterChainBuilder.build());
                ThreadPoolConfig kernelPoolConfig = ThreadPoolConfig.defaultConfig()
                        .setPoolName("Tron-Service-Thread-Pool").setKeepAliveTime(serverConnectionKeepAliveTimeInSeconds, TimeUnit.MINUTES)
                        .setCorePoolSize(serverMinPoolSize).setTransactionTimeout(serverConnectionTimeoutInSeconds, TimeUnit.SECONDS).setQueueLimit(-1)
                        .setMaxPoolSize(serverMaxPoolSize).setDaemon(true).copy();
                GrizzlyExecutorService gs = GrizzlyExecutorService
                        .createInstance(kernelPoolConfig);
                transport.setWorkerThreadPool(gs);
                transport.setWorkerThreadPoolConfig(kernelPoolConfig);
                try {
                        transport.start();
                } catch (Exception e) {
                        logger.error("Error iniciando transporte.", e);
                        throw e;
                }
                pool = SingleEndpointPool.builder(SocketAddress.class).connectorHandler(transport).endpointAddress(new InetSocketAddress(host.getHostAddress(), port)).maxPoolSize(serverMaxPoolSize).connectTimeout(serverTransportTimeoutInSeconds, TimeUnit.SECONDS).build();

                //Listener
                FilterChainBuilder filterChainBuilderListener = FilterChainBuilder.stateless();
                filterChainBuilderListener.add(new TransportFilter());
                filterChainBuilderListener.add(new StringFilter(Charset.forName("UTF-8")));
                ClientSideFilter clientSideFilter = new ClientSideFilter();
                clientSideFilter.setAnswerQueue(queue);
                filterChainBuilderListener.add(clientSideFilter);

                filterTransport = TCPNIOTransportBuilder.newInstance().build();
                filterTransport.setProcessor(filterChainBuilderListener.build());
                ThreadPoolConfig listenerLernelPoolConfig = ThreadPoolConfig.defaultConfig()
                        .setPoolName("Tron-Listener-Thread-Pool").setKeepAliveTime(serverConnectionKeepAliveTimeInSeconds, TimeUnit.MINUTES)
                        .setCorePoolSize(clientListenerMinPoolSize).setTransactionTimeout(serverTransportTimeoutInSeconds, TimeUnit.SECONDS)
                        .setMaxPoolSize(clientListenerMaxPoolSize).setDaemon(true).copy();
                GrizzlyExecutorService listenerGS = GrizzlyExecutorService
                        .createInstance(listenerLernelPoolConfig);
                filterTransport.setWorkerThreadPool(listenerGS);
                filterTransport.setWorkerThreadPoolConfig(listenerLernelPoolConfig);
                try {
                        try (ServerSocket serverSocket = new ServerSocket(0)) {
                                clientListenerPort = serverSocket.getLocalPort();
                                serverSocket.close();
                        }
                        try (ServerSocket serverSocket = new ServerSocket(0)) {
                                gamePort = serverSocket.getLocalPort();
                                serverSocket.close();
                        }
                        udpst = new UDPSocketThread();
                        udpst.setGamePort(gamePort);
                        udpst.setNotificacionServidorQueue(queue);
                        udpst.init();

                        oust = new OutgoingUDPSocketThread();
                        filter.setOust(oust);
                        filter.setUst(udpst);
                        clientSideFilter.setOust(oust);
                        clientSideFilter.setUst(udpst);

                        filterTransport.bind(clientListenerPort);
                        filterTransport.start();
                } catch (Exception e) {
                        logger.error("Error iniciando listener.", e);
                        throw e;
                }

                NotificacionServidor notificacionServidor = new NotificacionServidor();
                notificacionServidor.setNotifyServerLocation(serverLocation);
                queue.offer(notificacionServidor);
                logger.info("Connected to server [" + host + "]: [" + port + "]");
                logger.info("Listening on [" + localhost + "]: [" + clientListenerPort + "]");
        }

        public Integer getServerTransportTimeoutInSeconds() {
                return serverTransportTimeoutInSeconds;
        }

        public void setServerTransportTimeoutInSeconds(Integer serverTransportTimeoutInSeconds) {
                this.serverTransportTimeoutInSeconds = serverTransportTimeoutInSeconds;
        }

        public Integer getServerConnectionTimeoutInSeconds() {
                return serverConnectionTimeoutInSeconds;
        }

        public void setServerConnectionTimeoutInSeconds(Integer serverConnectionTimeoutInSeconds) {
                this.serverConnectionTimeoutInSeconds = serverConnectionTimeoutInSeconds;
        }

        public Integer getServerConnectionKeepAliveTimeInSeconds() {
                return serverConnectionKeepAliveTimeInSeconds;
        }

        public void setServerConnectionKeepAliveTimeInSeconds(Integer serverConnectionKeepAliveTimeInSeconds) {
                this.serverConnectionKeepAliveTimeInSeconds = serverConnectionKeepAliveTimeInSeconds;
        }

        public Integer getServerMaxPoolSize() {
                return serverMaxPoolSize;
        }

        public void setServerMaxPoolSize(Integer serverMaxPoolSize) {
                this.serverMaxPoolSize = serverMaxPoolSize;
        }

        public Integer getServerMinPoolSize() {
                return serverMinPoolSize;
        }

        public void setServerMinPoolSize(Integer serverMinPoolSize) {
                this.serverMinPoolSize = serverMinPoolSize;
        }

        public Integer getClientListenerMaxPoolSize() {
                return clientListenerMaxPoolSize;
        }

        public void setClientListenerMaxPoolSize(Integer clientListenerMaxPoolSize) {
                this.clientListenerMaxPoolSize = clientListenerMaxPoolSize;
        }

        public Integer getClientListenerMinPoolSize() {
                return clientListenerMinPoolSize;
        }

        public void setClientListenerMinPoolSize(Integer clientListenerMinPoolSize) {
                this.clientListenerMinPoolSize = clientListenerMinPoolSize;
        }

        public InetAddress getClientListenerHost() {
                return clientListenerHost;
        }

        public void setClientListenerHost(InetAddress clientListenerHost) {
                this.clientListenerHost = clientListenerHost;
        }

        public Integer getClientListenerPort() {
                return clientListenerPort;
        }

        public void setClientListenerPort(Integer clientListenerPort) {
                this.clientListenerPort = clientListenerPort;
        }

        @Override
        public Queue<NotificacionServidor> getServerNotificationQueue() {
                return this.queue;
        }

        @Override
        public void writeToServer(ClientServerRequest request) {
                if (request.getAutenticacionRequest() != null) {
                        request.getAutenticacionRequest().setAnswerAddress(clientListenerHost);
                        request.getAutenticacionRequest().setAnswerPort(clientListenerPort);
                        request.getAutenticacionRequest().setGamePort(gamePort);
                }
                Gson json = new Gson();
                String jsonString = json.toJson(request);
                Future<Connection> future = pool.take();
                int timeout = serverTransportTimeoutInSeconds;

                Connection connection = null;
                try {
                        connection = future.get(timeout, TimeUnit.SECONDS);
                        connection.write(jsonString);
                } catch (InterruptedException | ExecutionException | TimeoutException interruptedException) {
                        logger.error("Error en escritura a servidor", interruptedException);
                } finally {
                        if (connection != null) {
                                try {
                                        pool.release(connection);
                                } catch (Exception e) {
                                        throw e;
                                }
                        }
                }
        }

        @Override
        public void notifyMatch(ClientServerRequest request) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public String getServerListenAddress() {
                return serverListenAddress;
        }

        public void setServerListenAddress(String serverListenAddress) {
                this.serverListenAddress = serverListenAddress;
        }

        public Integer getServerListenPort() {
                return serverListenPort;
        }

        public void setServerListenPort(Integer serverListenPort) {
                this.serverListenPort = serverListenPort;
        }

}
