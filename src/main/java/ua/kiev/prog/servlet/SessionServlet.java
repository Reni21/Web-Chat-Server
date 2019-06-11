package ua.kiev.prog.servlet;

import ua.kiev.prog.dao.SessionDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "SessionServlet", urlPatterns = "/session")
public class SessionServlet extends HttpServlet {
    private SessionDao sessionDao = SessionDao.getInstance();

    @Override // get online users
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        String activeUsers = sessionDao.getActiveUsersAsString();

        if(activeUsers != null && !activeUsers.isEmpty()) {
            OutputStream os = resp.getOutputStream();
            byte[] buf = activeUsers.getBytes(StandardCharsets.UTF_8);
            os.write(buf);
            os.flush();
            os.close();
        }
    }

    @Override // leave the chat
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getHeader("Cookie");
        sessionDao.deleteSessionId(sessionId);
    }
}
