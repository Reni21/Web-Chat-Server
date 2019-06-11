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
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "GroupServlet", urlPatterns = "/group")
public class GroupServlet extends HttpServlet {
    private GroupDao groupDao = GroupDao.getInstance();
    private SessionDao sessionDao = SessionDao.getInstance();
    private UserDao userDao = UserDao.getInstance();

    @Override // create group
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        byte[] buf = Utils.requestBodyToArray(req.getInputStream());
        String groupName = new String(buf, StandardCharsets.UTF_8);

        if (!groupName.isEmpty() && !groupDao.isGroupExist(groupName)) {
            String login = sessionDao.getLoginBySessId(req.getHeader("Cookie"));
            groupDao.createPrivateGroup(groupName, login);
            groupDao.joinGroup(userDao.getUser(login), groupName);

        } else {
            resp.sendError(HttpServletResponse.SC_CONFLICT, "{message: Group name is already taken}"); // - error 409
        }
    }

    @Override // get groups list
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        String privateGroups = groupDao.getPrivateGroupsAsString();

        if (privateGroups != null && !privateGroups.isEmpty()) {
            OutputStream os = resp.getOutputStream();
            byte[] buf = privateGroups.getBytes(StandardCharsets.UTF_8);
            os.write(buf);
            os.flush();
            os.close();
        }
    }

    @Override // delete group
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String groupName = req.getParameter("group");
        String login = sessionDao.getLoginBySessId(req.getHeader("Cookie"));

        if (groupDao.isGroupExist(groupName)) {
            if (groupDao.isUserAdmin(login, groupName)) {
                groupDao.deleteGroup(groupName);
            } else {
                resp.sendError(HttpServletResponse.SC_CONFLICT, "{message: Not enough rights for this action!}"); // - error 409
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST); // - error 409
        }
    }
}
