package ua.kiev.prog.dao;

import java.util.*;

public class SessionDao {
    private static final SessionDao SESSION_DAO = new SessionDao();
    private final Map<String, String> sessionIds = new HashMap<>();

    private SessionDao() {
    }

    public static SessionDao getInstance(){
        return SESSION_DAO;
    }

    public synchronized String createSession(String login) {
        String session_id = UUID.randomUUID().toString();
        sessionIds.put(session_id, login);
        return session_id;
    }

    public synchronized String getLoginBySessId(String sessId){
        return sessionIds.get(sessId);
    }

    public synchronized String getActiveUsersAsString(){
        String activeUsers = "";
        for (String value : sessionIds.values()) {
            activeUsers = activeUsers + value + "\n";
        }
        return activeUsers.trim();
    }

    public synchronized boolean isSessionIdExist(String id){
        return sessionIds.containsKey(id);
    }

    public synchronized void deleteSessionId(String id){
        sessionIds.remove(id);
    }



}
