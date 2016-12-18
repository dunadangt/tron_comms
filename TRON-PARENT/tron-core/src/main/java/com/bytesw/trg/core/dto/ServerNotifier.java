package com.bytesw.trg.core.dto;

import java.util.Queue;

/**
 *
 * @author lvasquez
 */
public class ServerNotifier {

        private Queue<NotificacionServidor> notifyToServerQueue;
        private Queue<NotificacionServidor> receiveNotificationFromServer;

        public Queue<NotificacionServidor> getNotifyToServerQueue() {
                return notifyToServerQueue;
        }

        public void setNotifyToServerQueue(Queue<NotificacionServidor> notifyToServerQueue) {
                this.notifyToServerQueue = notifyToServerQueue;
        }

        public Queue<NotificacionServidor> getReceiveNotificationFromServer() {
                return receiveNotificationFromServer;
        }

        public void setReceiveNotificationFromServer(Queue<NotificacionServidor> receiveNotificationFromServer) {
                this.receiveNotificationFromServer = receiveNotificationFromServer;
        }

}
