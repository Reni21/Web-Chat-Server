package ua.kiev.prog.dao;

import ua.kiev.prog.entity.Group;
import ua.kiev.prog.entity.Message;
import ua.kiev.prog.entity.User;

import java.util.*;

public class UserDao {
    private static final UserDao USER_DAO = new UserDao();
    private final Map<String, User> registeredUsers = new HashMap<>();

    private UserDao() {
    }

    public static UserDao getInstance() {
        return USER_DAO;
    }

    public synchronized boolean isUserExist(String login) {
        return registeredUsers.containsKey(login);
    }

    public synchronized boolean isUserExist(String login, String pass) {
        return registeredUsers.containsKey(login) && registeredUsers.get(login).getPass().equals(pass);
    }

    public synchronized void registerUser(User user) {
        registeredUsers.put(user.getLogin(), user);
    }

    public synchronized User getUser(String login) {
        if (registeredUsers.containsKey(login)) {
            return registeredUsers.get(login);
        }
        return null;
    }

    public synchronized void addPrivateMsg(String login, Message msg) {
        if (registeredUsers.containsKey(login)) {
            registeredUsers.get(login).addPrivateMsg(msg);
        }
    }

    public synchronized List<String> getUserGroupsList(String login) {
        User user = registeredUsers.get(login);
        if (user != null) {
            return user.getGroupsList();
        }
        return null;
    }

    public synchronized List<Message> getPrivateMsg(String login, int fromIndex) {
        if (registeredUsers.containsKey(login)) {
            return registeredUsers.get(login).getPrivateMsg(fromIndex);
        }
        return null;
    }

    public synchronized void setStatus(String login, String status){
        if(registeredUsers.containsKey(login)){
            registeredUsers.get(login).setStatus(status);
        }
    }

}
