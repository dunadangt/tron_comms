package com.bytesw.trg.tron.client.services.bs;

import java.net.InetAddress;

/**
 *
 * @author lvasquez
 */
public interface TronClientService {
        
        public void init();
        
        public void notifyServerFound(InetAddress host, Integer port);
}
