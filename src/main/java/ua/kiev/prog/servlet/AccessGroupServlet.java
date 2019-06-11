package ua.kiev.prog.servlet;

import ua.kiev.prog.Utils;
import ua.kiev.prog.dao.GroupDao;
import ua.kiev.prog.dao.SessionDao;
import ua.kiev.prog.dao.UserDao;
import ua.kiev.prog.entity.Group;
import ua.kiev.prog.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "AccessGroupServlet", urlPatterns = "/access")
public class AccessGroupServlet extends HttpServlet {
    private GroupDao groupDao = GroupDao.getInstance();
    private UserDao userDao = UserDao.getInstance();
    private SessionDao sessionDao = SessionDao.getInstance();

    @Override // join group
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        byte[] buf = Utils.requestBodyToArray(req.getInputStream());
        String groupName = new String(buf, StandardCharsets.UTF_8);

        if(!groupName.isEmpty() && groupDao.isGroupExist(groupName)) {
            String login = sessionDao.getLoginBySessId(req.getHeader("Cookie"));
            groupDao.joinGroup(userDao.getUser(login), groupName);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "{message: Group does not exist!}"); // - error 404
        }
    }

    @Override // leave group
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String groupName = req.getParameter("group");
        String login = sessionDao.getLoginBySessId(req.getHeader("Cookie"));

        if(groupDao.isGroupExist(groupName)){
            groupDao.leaveGroup(userDao.getUser(login), groupName);
            groupDao.deleteMember(userDao.getUser(login), groupName);
        } else {
            resp.sendError(HttpServletResponse.SC_CONFLICT, "{message: Group does not exist!}"); // - error 404
        }
    }
}
