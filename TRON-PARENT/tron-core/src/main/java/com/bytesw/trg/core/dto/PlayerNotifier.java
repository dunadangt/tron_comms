package com.bytesw.trg.core.dto;

import java.util.Queue;

/**
 *
 * @author lvasquez
 */
public class PlayerNotifier {

        private Queue<NotificacionJugador> notifyToUser;
        private Queue<NotificacionJugador> receiveNotificationFromUser;

        public Queue<NotificacionJugador> getNotifyToUser() {
                return notifyToUser;
        }

        public void setNotifyToUser(Queue<NotificacionJugador> notifyToUser) {
                this.notifyToUser = notifyToUser;
        }

        public Queue<NotificacionJugador> getReceiveNotificationFromUser() {
                return receiveNotificationFromUser;
        }

        public void setReceiveNotificationFromUser(Queue<NotificacionJugador> receiveNotificationFromUser) {
                this.receiveNotificationFromUser = receiveNotificationFromUser;
        }

}
