package com.bytesw.trg.tron.client.services.bs;

/**
 *
 * @author lvasquez
 */
public interface TronClientService {
        
        public void init();
        
        public void notifyServerFound(String host, Integer port);
}
