package com.bytesw.trg.core.bo;

import java.net.InetAddress;

/**
 *
 * @author lvasquez
 */
public class Usuario {

        private String username;
        private Integer position;
        private InetAddress userAddress;
        private Integer userPort;
        private Integer gamePort;

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public Integer getPosition() {
                return position;
        }

        public void setPosition(Integer position) {
                this.position = position;
        }

        public InetAddress getUserAddress() {
                return userAddress;
        }

        public void setUserAddress(InetAddress userAddress) {
                this.userAddress = userAddress;
        }

        public Integer getUserPort() {
                return userPort;
        }

        public void setUserPort(Integer userPort) {
                this.userPort = userPort;
        }

        public Integer getGamePort() {
                return gamePort;
        }

        public void setGamePort(Integer gamePort) {
                this.gamePort = gamePort;
        }

        @Override
        public String toString() {
                return "Usuario{" + "username=" + username + ", position=" + position + ", userAddress=" + userAddress + ", userPort=" + userPort + '}';
        }

}
