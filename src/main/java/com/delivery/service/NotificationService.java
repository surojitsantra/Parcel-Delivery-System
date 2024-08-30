package com.delivery.service;

import com.delivery.model.User;

public interface NotificationService {
    void notify(User user, String message);
}
