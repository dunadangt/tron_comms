package com.bytesw.trg.tron.server.services.transport;

import com.bytesw.trg.core.dto.ClientServerRequest;
import com.google.gson.Gson;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.TransformationException;
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
public class ResponseNotifier {

        static org.slf4j.Logger logger = LoggerFactory.getLogger(ResponseNotifier.class);
        
        private final String responseHost;
        private final Integer responsePort;
        private SingleEndpointPool pool; // Pool de conexiones a un mismo destino.
        private TCPNIOTransport transport;
        private final Integer clientPoolSize;
        private final Integer keepAliveTimeInSeconds;
        private final Integer transactionTimeoutInSeconds;
        private final Integer corePoolSize;
        private final Integer maxPoolSize;
        private final Integer poolConnectionTimeoutInSeconds;
        private final Integer writeTimeoutInSeconds;

        public ResponseNotifier(String host, Integer port, Integer clientPoolSize, Integer keepAliveTimeInSeconds,
                Integer transactionTimeoutInSeconds, Integer corePoolSize, Integer maxPoolSize,
                Integer poolConnectionTimeoutInSeconds, Integer writeTimeoutInSeconds) {
                this.responseHost = host;
                this.responsePort = port;
                this.clientPoolSize = clientPoolSize;
                this.keepAliveTimeInSeconds = keepAliveTimeInSeconds;
                this.transactionTimeoutInSeconds = transactionTimeoutInSeconds;
                this.corePoolSize = corePoolSize;
                this.maxPoolSize = maxPoolSize;
                this.poolConnectionTimeoutInSeconds = poolConnectionTimeoutInSeconds;
                this.writeTimeoutInSeconds = writeTimeoutInSeconds;
        }

        public void init() throws Exception {
                // Iniciando FilterChain
                FilterChainBuilder filterChainBuilder = FilterChainBuilder.stateless();
                // Transporte del filtro
                filterChainBuilder.add(new TransportFilter());
                // Agregando filtro que transforma String <--> Buffer
                filterChainBuilder.add(new StringFilter(Charset.forName("UTF-8")));
                // Agregando filtro para CDRs, en el filtro se procesara el CDR
                // recibido.
                ServerSideFilter serverSideFilter = new ServerSideFilter();
                filterChainBuilder.add(serverSideFilter);

                // Usando TCP como transporte
                transport = TCPNIOTransportBuilder.newInstance().build();
                transport.setProcessor(filterChainBuilder.build());
                ThreadPoolConfig kernelPoolConfig = ThreadPoolConfig.defaultConfig()
                        .setPoolName("NotifierPool-" + responseHost + ":" + responsePort).setKeepAliveTime(keepAliveTimeInSeconds, TimeUnit.MINUTES)
                        .setCorePoolSize(corePoolSize).setTransactionTimeout(transactionTimeoutInSeconds, TimeUnit.SECONDS)
                        .setMaxPoolSize(maxPoolSize).setDaemon(true).copy();
                GrizzlyExecutorService gs = GrizzlyExecutorService
                        .createInstance(kernelPoolConfig);
                transport.setWorkerThreadPool(gs);
                transport.setWorkerThreadPoolConfig(kernelPoolConfig);
                try {
                        transport.start();
                } catch (Exception e) {
                        logger.error("Ha ocurrido un error iniciando notificador", e);
                }

                String host = responseHost;
                if (host.startsWith("/")) {
                        host = host.replace("/", "");
                }
                int port = responsePort;
                int poolSize = clientPoolSize;

                // Creando pool de conexiones
                pool = SingleEndpointPool.builder(SocketAddress.class).connectorHandler(transport).endpointAddress(new InetSocketAddress(host, port)).maxPoolSize(poolSize).connectTimeout(poolConnectionTimeoutInSeconds, TimeUnit.SECONDS).build();
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        System.out.println("Shutting down client response");
                        close();
                }));
        }

        public void sendResponse(ClientServerRequest request) throws Exception {
                Connection connection = null;
                Future<Connection> future = pool.take();
                logger.info("Respondiendo [" + request + "]");
                try {
                        Gson json = new Gson();
                        String jsonString = json.toJson(request);
                        connection = future.get(writeTimeoutInSeconds, TimeUnit.SECONDS); // Obtener
                        connection.write(jsonString);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        logger.error("Error enviando request [" + request + "]", e);
                        throw new TransformationException("Invalid Request Content", e);
                } finally {
                        if (connection != null && pool != null) {
                                pool.release(connection);
                        }
                }

        }

        public void close() {
                if (pool != null) {
                        pool.close();
                }
        }

}
