package com.revworkforce.employeemanagementservice.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PresenceService {
    private static final long ONLINE_TTL_SECONDS = 150;

    private final Set<String> websocketOnlineUsers = ConcurrentHashMap.newKeySet();
    private final ConcurrentHashMap<String, Instant> lastSeen = new ConcurrentHashMap<>();

    public void userConnected(String email) {
        if (email == null || email.isBlank()) {
            return;
        }
        websocketOnlineUsers.add(email);
        touchUser(email);
    }

    public void userDisconnected(String email) {
        if (email == null || email.isBlank()) {
            return;
        }
        websocketOnlineUsers.remove(email);
        touchUser(email);
    }

    public void touchUser(String email) {
        if (email == null || email.isBlank()) {
            return;
        }
        lastSeen.put(email, Instant.now());
    }

    public boolean isOnline(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        if (websocketOnlineUsers.contains(email)) {
            return true;
        }
        Instant seenAt = lastSeen.get(email);
        if (seenAt == null) {
            return false;
        }
        return seenAt.isAfter(Instant.now().minusSeconds(ONLINE_TTL_SECONDS));
    }

    public Set<String> getOnlineUsers() {
        Instant cutoff = Instant.now().minusSeconds(ONLINE_TTL_SECONDS);
        Set<String> online = new HashSet<>(websocketOnlineUsers);
        lastSeen.forEach((email, seenAt) -> {
            if (seenAt != null && seenAt.isAfter(cutoff)) {
                online.add(email);
            }
        });
        return Collections.unmodifiableSet(online);
    }
}
