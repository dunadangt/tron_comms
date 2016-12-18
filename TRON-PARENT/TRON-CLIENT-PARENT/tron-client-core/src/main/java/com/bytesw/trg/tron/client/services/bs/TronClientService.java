package com.bytesw.trg.tron.client.services.bs;

import com.bytesw.trg.core.dto.ClientServerRequest;
import com.bytesw.trg.core.dto.NotificacionServidor;
import java.net.InetAddress;
import java.util.Queue;

/**
 *
 * @author lvasquez
 */
public interface TronClientService {
        
        public void init();
        
        public void notifyServerFound(InetAddress host, Integer port, InetAddress localhost) throws Exception;
        
        public Queue<NotificacionServidor> getServerNotificationQueue();
        
        public void writeToServer(ClientServerRequest request);
}
