package ua.kiev.prog.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
public class User {
    private Credentials credentials;
    private String status;
    private List<Group> groups = new ArrayList<>();
    private List<Message> privetMsg = new ArrayList<>();


    public User(String login, String pass) {
        credentials = new Credentials(login, pass);
    }

    public String getLogin() {
        return credentials.getLogin();
    }

    public void setlogin(String login) {
        this.credentials.setLogin(login);
    }

    public String getPass() {
        return credentials.getPass();
    }

    public void setPass(String pass) {
        this.credentials.setPass(pass);
    }

    public List<String> getGroupsList() {
        List<String> list = new ArrayList<>();
        for (Group group : groups) {
            list.add(group.getName());
        }
        return list;
    }

    public void addGroup(Group group) {
        this.groups.add(group);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void addPrivateMsg(Message msg){
        privetMsg.add(msg);
    }

    public List<Message> getPrivateMsg(int fromIndex) {
        List<Message> list = new ArrayList<>();
        for (int i = fromIndex; i < privetMsg.size(); i++)
            list.add(privetMsg.get(i));
        return list;
    }

    public void leaveGroup(Group group){
        if(groups.contains(group)){
            groups.remove(group);
        }
    }




}
