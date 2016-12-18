package com.bytesw.trg.core.dto;

import java.util.Queue;

/**
 *
 * @author lvasquez
 */
public class ServerNotifier {

        private Queue<ClientServerRequest> notifyToServerQueue;
        private Queue<ClientServerResponse> receiveNotificationFromServer;

        public Queue<ClientServerRequest> getNotifyToServerQueue() {
                return notifyToServerQueue;
        }

        public void setNotifyToServerQueue(Queue<ClientServerRequest> notifyToServerQueue) {
                this.notifyToServerQueue = notifyToServerQueue;
        }

        public Queue<ClientServerResponse> getReceiveNotificationFromServer() {
                return receiveNotificationFromServer;
        }

        public void setReceiveNotificationFromServer(Queue<ClientServerResponse> receiveNotificationFromServer) {
                this.receiveNotificationFromServer = receiveNotificationFromServer;
        }

}
