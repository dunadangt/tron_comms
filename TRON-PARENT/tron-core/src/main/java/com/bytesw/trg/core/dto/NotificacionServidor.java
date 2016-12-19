package com.bytesw.trg.core.dto;

/**
 *
 * @author lvasquez
 */
public class NotificacionServidor {

        private NotifyServerLocation notifyServerLocation;
        private Match match;

        public NotifyServerLocation getNotifyServerLocation() {
                return notifyServerLocation;
        }

        public void setNotifyServerLocation(NotifyServerLocation notifyServerLocation) {
                this.notifyServerLocation = notifyServerLocation;
        }

        public Match getMatch() {
                return match;
        }

        public void setMatch(Match match) {
                this.match = match;
        }

}
