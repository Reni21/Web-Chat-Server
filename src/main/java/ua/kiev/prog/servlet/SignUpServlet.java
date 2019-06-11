package ua.kiev.prog.servlet;

import ua.kiev.prog.Utils;
import ua.kiev.prog.dao.GroupDao;
import ua.kiev.prog.dao.UserDao;
import ua.kiev.prog.entity.Credentials;
import ua.kiev.prog.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "SignUpServlet", urlPatterns = "/signUp")
public class SignUpServlet extends HttpServlet {
    private UserDao userDao = UserDao.getInstance();
    private GroupDao groupDao = GroupDao.getInstance();


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        byte[] buf = Utils.requestBodyToArray(req.getInputStream());
        String bufStr = new String(buf, StandardCharsets.UTF_8);

        Credentials credentials = Utils.fromJSON(bufStr, Credentials.class);
        String login = credentials.getLogin();
        String pass = credentials.getPass();

        if (userDao.isUserExist(login)) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, "{message: Login is already taken}"); // - error 409
        } else {
            User user = new User(login, pass);
            userDao.registerUser(user); // на это мэтапе срабатывает, т.е. userDao != null
            groupDao.joinGroup(user, "Common");
        }

    }
}
