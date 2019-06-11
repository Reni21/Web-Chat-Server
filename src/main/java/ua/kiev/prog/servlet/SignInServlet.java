package ua.kiev.prog.servlet;

import ua.kiev.prog.Utils;
import ua.kiev.prog.dao.GroupDao;
import ua.kiev.prog.dao.SessionDao;
import ua.kiev.prog.dao.UserDao;
import ua.kiev.prog.entity.Credentials;
import ua.kiev.prog.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@WebServlet(name = "SignInServlet", urlPatterns = "/signIn")
public class SignInServlet extends HttpServlet {
    private UserDao userDao = UserDao.getInstance();
    private SessionDao sessionDao = SessionDao.getInstance();


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        byte[] buf = Utils.requestBodyToArray(req.getInputStream());
        String bufStr = new String(buf, StandardCharsets.UTF_8);

        Credentials credentials = Utils.fromJSON(bufStr, Credentials.class);
        String login = credentials.getLogin();
        String pass = credentials.getPass();

        if (userDao.isUserExist(login, pass)) {
            String sessionId = sessionDao.createSession(login);

            resp.setContentType("text/plain");
            if (sessionId != null) {
                OutputStream os = resp.getOutputStream();
                byte[] buf2 = sessionId.getBytes(StandardCharsets.UTF_8);
                os.write(buf2);
                os.flush();
                os.close();
            }

        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "{message: Incorrect login or password}"); // - error 404
        }

    }
}
