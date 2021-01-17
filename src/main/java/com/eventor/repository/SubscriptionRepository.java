package com.eventor.repository;

import com.eventor.model.Subscription;
import com.eventor.model.SubscriptionKey;
import com.eventor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionKey> {
    Boolean existsByUserId(User user);
}
