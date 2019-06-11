package ua.kiev.prog.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
public class Group {
    private String name;
    private String admin;
    private List<User> members = new ArrayList<>();
    private List<Message> croupMsg = new ArrayList<>();

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addMember(User user) {
        this.members.add(user);
    }

    public List<User> getCroupMembers() {
        return new ArrayList<>(members);
    }

    public List<Message> getGroupMsg() {
        return new ArrayList<>(croupMsg);
    }

    public List<Message> getGroupMsg(int fromIndex) {
        List<Message> list = new ArrayList<>();
        for (int i = fromIndex; i < croupMsg.size(); i++)
            list.add(croupMsg.get(i));
        return list;
    }

    public void addMsg(Message msg) {
        this.croupMsg.add(msg);
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getAdmin() {
        return admin;
    }

    public void deleteMember(User user){
        members.remove(user);
    }
}
