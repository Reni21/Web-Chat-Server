package ua.kiev.prog.servlet;

import ua.kiev.prog.Utils;
import ua.kiev.prog.dao.SessionDao;
import ua.kiev.prog.dao.UserDao;
import ua.kiev.prog.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "UserServlet", urlPatterns = "/user")
public class UserServlet extends HttpServlet {
    private SessionDao sessionDao = SessionDao.getInstance();
    private UserDao userDao = UserDao.getInstance();


    @Override // set status
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        byte[] buf = Utils.requestBodyToArray(req.getInputStream());
        String status = new String(buf, StandardCharsets.UTF_8);

        if(!status.isEmpty()) {
            String login = sessionDao.getLoginBySessId(req.getHeader("Cookie"));
            userDao.setStatus(login, status);
        }
    }

    @Override // get status
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        resp.setContentType("text/plain");
        String status = userDao.getUser(login).getStatus();

        if(!status.isEmpty()) {
            OutputStream os = resp.getOutputStream();
            byte[] buf = status.getBytes(StandardCharsets.UTF_8);
            os.write(buf);
            os.flush();
            os.close();
        }
    }
}
