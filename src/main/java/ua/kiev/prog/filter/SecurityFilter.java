package ua.kiev.prog.filter;

import ua.kiev.prog.dao.GroupDao;
import ua.kiev.prog.dao.SessionDao;
import ua.kiev.prog.dao.UserDao;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"})
public class SecurityFilter implements Filter {
    private SessionDao sessionDao = SessionDao.getInstance();

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        String path = ((HttpServletRequest) req).getRequestURI();
        if (path.startsWith("/signUp") || path.startsWith("/signIn")) {
            filterChain.doFilter(req, resp); // Just continue chain
        } else {

            String sessionId = ((HttpServletRequest) req).getHeader("Cookie");

            if (existSessionId(sessionId)) {
                filterChain.doFilter(req, resp);
            } else {
                HttpServletResponse response = (HttpServletResponse) resp;
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Not allowed"); // 403
            }
        }
    }


    private boolean existSessionId(String id) {
        return id != null && sessionDao.isSessionIdExist(id);
    }
}
