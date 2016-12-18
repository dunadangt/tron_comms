package com.bytesw.trg.core.dto;

import java.net.InetAddress;

/**
 *
 * @author lvasquez
 */
public class NotifyServerLocation {

        private InetAddress serverAddress;
        private Integer port;

        public InetAddress getServerAddress() {
                return serverAddress;
        }

        public void setServerAddress(InetAddress serverAddress) {
                this.serverAddress = serverAddress;
        }

        public Integer getPort() {
                return port;
        }

        public void setPort(Integer port) {
                this.port = port;
        }

}
