
import Beans.CRUD.UserLoginBean;
import entities.UserLogin;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;
@WebServlet("/servlet")
public class TryFunctionality extends HttpServlet {
    private static final Logger log = Logger.getLogger("JPAServlet");
    @Inject
    private UserLoginBean ulB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        List<UserLogin> userlogins = ulB.getAllUserLoginInfo();

        for(UserLogin u : userlogins) {
            log.info(String.format("IDuser: %d\nUsername: %s\nPassword: %s\nRole: %s\nSalt: %d\n",
                    u.getId(), u.getUsername(), u.getPassword(), u.getRole(), u.getSalt()));
            writer.append(String.format("IDuser: %d\nUsername: %s\nPassword: %s\nRole: %s\nSalt: %d\n",
                    u.getId(), u.getUsername(), u.getPassword(), u.getRole(), u.getSalt()));
        }
        writer.close();
    }
}