package ua.kiev.prog.servlet;

import ua.kiev.prog.Utils;
import ua.kiev.prog.dao.GroupDao;
import ua.kiev.prog.dao.SessionDao;
import ua.kiev.prog.dao.UserDao;
import ua.kiev.prog.entity.Group;
import ua.kiev.prog.entity.Message;
import ua.kiev.prog.entity.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddMessagesServlet extends HttpServlet {
    private GroupDao groupDao = GroupDao.getInstance();
    private UserDao userDao = UserDao.getInstance();
    private SessionDao sessionDao = SessionDao.getInstance();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        byte[] buf = Utils.requestBodyToArray(req.getInputStream());
        String bufStr = new String(buf, StandardCharsets.UTF_8);

        Message msg = Utils.fromJSON(bufStr, Message.class);
        if (msg != null) {
            if(msg.getTo().equals("All")) {
                groupDao.addMsg("Common", msg);

            } else if(msg.getTo().startsWith("#")){
                String groupName = msg.getTo().substring(1);
                if(groupDao.isGroupExist(groupName)){
                    groupDao.addMsg(groupName, msg);
                }

            } else {
                String loginTo = msg.getTo();
                if(userDao.isUserExist(loginTo)) {
                    userDao.addPrivateMsg(loginTo, msg);
                    String loginFrom = sessionDao.getLoginBySessId(req.getHeader("Cookie"));
                    userDao.addPrivateMsg(loginFrom, msg);
                }
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
