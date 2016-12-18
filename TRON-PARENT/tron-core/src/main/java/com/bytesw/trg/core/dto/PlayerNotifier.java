package com.bytesw.trg.core.dto;

import java.util.Queue;

/**
 *
 * @author lvasquez
 */
public class PlayerNotifier {

        private Queue<Notificacion> notifyToUser;
        private Queue<Notificacion> receiveNotificationFromUser;

        public Queue<Notificacion> getNotifyToUser() {
                return notifyToUser;
        }

        public void setNotifyToUser(Queue<Notificacion> notifyToUser) {
                this.notifyToUser = notifyToUser;
        }

        public Queue<Notificacion> getReceiveNotificationFromUser() {
                return receiveNotificationFromUser;
        }

        public void setReceiveNotificationFromUser(Queue<Notificacion> receiveNotificationFromUser) {
                this.receiveNotificationFromUser = receiveNotificationFromUser;
        }

}
