package com.bytesw.trg.tron.server.services.bs;

import com.bytesw.trg.core.bo.Usuario;
import com.bytesw.trg.core.dto.AbandonarPartidaRequest;
import com.bytesw.trg.core.dto.ActualizarEstadoRequest;
import com.bytesw.trg.core.dto.AutenticacionRequest;
import com.bytesw.trg.core.dto.ClientServerRequest;
import com.bytesw.trg.core.dto.CrearPartidaRequest;
import com.bytesw.trg.core.dto.IniciarPartidaRequest;
import com.bytesw.trg.core.dto.IniciarPartidaResponse;
import com.bytesw.trg.core.dto.ListarPartidasRequest;
import com.bytesw.trg.core.dto.UnirsePartidaRequest;
import com.bytesw.trg.tron.server.services.transport.ResponseNotifier;
import com.bytesw.trg.tron.server.services.transport.ServerSideFilter;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
        private final Map<String, ResponseNotifier> notifierMap = new ConcurrentHashMap<>();
        private final Map<String, Usuario> userMap = new ConcurrentHashMap<>();
        private Usuario player1;

        JmDNS jmdns;

        public void init() {
                try {
                        //Listener
                        FilterChainBuilder filterChainBuilderListener = FilterChainBuilder.stateless();
                        filterChainBuilderListener.add(new TransportFilter());
                        filterChainBuilderListener.add(new StringFilter(Charset.forName("UTF-8")));
                        ServerSideFilter serverSideFilter = new ServerSideFilter();
                        serverSideFilter.setTronService(this);
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
                                listenerTransport.bind(serverListenPort);
                                listenerTransport.start();

                                logger.info("Listener bound on port [" + serverListenPort + "]");
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

        @Override
        public void abandonarPartidaRequest(AbandonarPartidaRequest request) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void actualizarEstadoRequest(ActualizarEstadoRequest request) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void autenticacionRequest(AutenticacionRequest request) {
                if (notifierMap.containsKey(request.getUsuario().getUsername())) {
                        ResponseNotifier notifier = notifierMap.get(request.getUsuario().getUsername());
                        notifier.close();
                        userMap.remove(request.getUsuario().getUsername());
                }
                ResponseNotifier notifier = new ResponseNotifier(request.getAnswerAddress().getHostAddress(), request.getAnswerPort(), 
                        clientPoolSize, keepAliveTimeInSeconds, transactionTimeoutInSeconds, 
                        corePoolSize, maxPoolSize, poolConnectionTimeoutInSeconds, writeTimeoutInSeconds);
                notifierMap.put(request.getUsuario().getUsername(), notifier);
                userMap.put(request.getUsuario().getUsername(), request.getUsuario());
                try {
                        notifier.init();
                        if (player1 != null) {
                                //response1 va a player2
                                IniciarPartidaResponse response1 = new IniciarPartidaResponse();
                                response1.getUsuarios().add(player1);
                                player1.setPosition(1);
                                
                                //response2 va a player 1
                                IniciarPartidaResponse response2 = new IniciarPartidaResponse();
                                response2.getUsuarios().add(request.getUsuario());
                                request.getUsuario().setUserAddress(request.getAnswerAddress());
                                request.getUsuario().setUserPort(request.getAnswerPort());
                                request.getUsuario().setGamePort(request.getGamePort());
                                request.getUsuario().setPosition(2);
                                
                                ResponseNotifier player1Notifier = notifierMap.get(player1.getUsername());
                                ClientServerRequest r1 = new ClientServerRequest();
                                r1.setIniciarPartidaResponse(response1);
                                ClientServerRequest r2 = new ClientServerRequest();
                                r2.setIniciarPartidaResponse(response2);
                                player1Notifier.sendResponse(r2);
                                notifier.sendResponse(r1);
                                player1 = null;
                        } else {
                                player1 = request.getUsuario();
                                player1.setUserAddress(request.getAnswerAddress());
                                player1.setUserPort(request.getAnswerPort());
                                player1.setGamePort(request.getGamePort());
                        }
                } catch (Exception ex) {
                        logger.error("Error iniciando conexion de vuelta a cliente", ex);
                }
                
        }

        @Override
        public void crearPartidaRequest(CrearPartidaRequest request) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void listarPartidasRequest(ListarPartidasRequest request) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void unirsePartidaRequest(UnirsePartidaRequest request) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void iniciarPartidaRequest(IniciarPartidaRequest request) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

}
