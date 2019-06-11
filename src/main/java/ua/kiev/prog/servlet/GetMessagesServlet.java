package ua.kiev.prog.servlet;

import ua.kiev.prog.JsonMessages;
import ua.kiev.prog.Utils;
import ua.kiev.prog.dao.GroupDao;
import ua.kiev.prog.dao.SessionDao;
import ua.kiev.prog.dao.UserDao;
import ua.kiev.prog.entity.User;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetMessagesServlet extends HttpServlet {
    private GroupDao groupDao = GroupDao.getInstance();
    private UserDao userDao = UserDao.getInstance();
    private SessionDao sessionDao = SessionDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int comIndex = parseIndex(req.getParameter("comFrom"));
        int privIndex = parseIndex(req.getParameter("privFrom"));

        String login = sessionDao.getLoginBySessId(req.getHeader("Cookie"));
        List<String> userGroups = userDao.getUserGroupsList(login);
        Map<String, Integer> groupsIndex = new HashMap<>();

        for (String groupName : userGroups) {
            String grIndex = req.getParameter(groupName);
            System.out.println("test parametr = " + groupName);
            int index = parseIndex(grIndex);
            if(index == -1){
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            groupsIndex.put(groupName, index);
        }

        resp.setContentType("application/json");
        JsonMessages jsonMessages = collectMessages(login, comIndex, privIndex, groupsIndex);
        String json = Utils.toJSON(jsonMessages);

        if (json != null) {
            OutputStream os = resp.getOutputStream();
            byte[] buf = json.getBytes(StandardCharsets.UTF_8);
            os.write(buf);
            os.flush();
            os.close();
        }
    }

    private JsonMessages collectMessages(String login, int comIndex, int privIndex, Map<String, Integer> groupsIndex){
        JsonMessages jsonMessages = new JsonMessages();
        jsonMessages.addToList(groupDao.getMsgList("Common", comIndex));
        jsonMessages.addToList(userDao.getPrivateMsg(login, privIndex));

        for (String groupName : groupsIndex.keySet()) {
            int indexFrom = groupsIndex.get(groupName);
            jsonMessages.addToList(groupDao.getMsgList(groupName, indexFrom));
        }
        return jsonMessages;
    }

    private int parseIndex(String fromStr) {
        if (fromStr == null) {
            return 0;
        }
        int indexFrom;
        try {
            indexFrom = Integer.parseInt(fromStr);
            if (indexFrom < 0) indexFrom = 0;
            return indexFrom;
        } catch (Exception ex) {
            return -1;
        }
    }
}
