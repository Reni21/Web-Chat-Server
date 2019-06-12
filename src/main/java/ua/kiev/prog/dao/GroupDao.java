package ua.kiev.prog.dao;

import ua.kiev.prog.entity.Group;
import ua.kiev.prog.entity.Message;
import ua.kiev.prog.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupDao {
    private static final GroupDao GROUP_DAO = new GroupDao();
    private final Group commonGroup = new Group("Common");
    private final Map<String, Group> privateGroups = new HashMap<>();

    private GroupDao() {
    }

    public static GroupDao getInstance() {
        return GROUP_DAO;
    }

    public synchronized String getPrivateGroupsAsString() {
        String groups = "";
        for (String groupName : privateGroups.keySet()) {
            groups = groups + groupName + "\n";
        }
        return groups.trim();
    }

    public synchronized boolean isGroupExist(String name) {
        return privateGroups.containsKey(name);
    }


    public synchronized void addMsg(String groupName, Message msg) {
        if (groupName.equals("Common")) {
            commonGroup.addMsg(msg);
        } else if (privateGroups.containsKey(groupName)) {
            privateGroups.get(groupName).addMsg(msg);
        }
    }

    public synchronized List<Message>  getMsgList(String groupName, int indexFrom) {
        if (groupName.equals("Common")) {
            return commonGroup.getGroupMsg(indexFrom);
        }
        if (privateGroups.containsKey(groupName)) {
            return privateGroups.get(groupName).getGroupMsg(indexFrom);
        }
        return null;
    }

    public synchronized void deleteGroup(String groupName) {
        Group groupForDelete = privateGroups.get(groupName);
        List<User> members = groupForDelete.getCroupMembers();
        for (User member : members) {
            member.leaveGroup(groupForDelete);
        }
        privateGroups.remove(groupName);
    }

    public synchronized void createPrivateGroup(String groupName, String admin) {
        Group group = new Group(groupName);
        group.setAdmin(admin);
        privateGroups.put(groupName, group);
    }

    public synchronized boolean isUserAdmin(String login, String groupName) {
        if (privateGroups.containsKey(groupName)) {
            String admin = privateGroups.get(groupName).getAdmin();
            return admin.equals(login);
        }
        return false;
    }

    public synchronized void deleteMember(User user, String groupName) {
        if (privateGroups.containsKey(groupName)) {
            privateGroups.get(groupName).deleteMember(user);
        }
    }

    public synchronized void leaveGroup(User user, String groupName) {
        if (isGroupExist(groupName)) {
            user.leaveGroup(privateGroups.get(groupName));
        }
    }

    public synchronized void joinGroup(User user, String groupName) {
        if (groupName.equals("Common")) {
            commonGroup.addMember(user);
        } else if (isGroupExist(groupName)) {
            privateGroups.get(groupName).addMember(user);
            user.addGroup(privateGroups.get(groupName));
        }
    }


}
