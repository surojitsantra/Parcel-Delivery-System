package com.delivery.service;

import com.delivery.model.User;

import java.util.logging.Logger;

public class EmailNotificationService implements NotificationService {

    final Logger LOG = Logger.getLogger(EmailNotificationService.class.getName());

    @Override
    public void notify(User user, String message) {

        if (user != null) {
            LOG.info(user.getName() + " : " + message);
        } else {

            LOG.info(message);
        }
    }
}
