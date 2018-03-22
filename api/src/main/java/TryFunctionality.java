import beans.crud.UserLoginBean;
import entities.UserLogin;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

        ulB.insertUserLoginSingle("newUser",
                "1234",
                "admin");

        List<UserLogin> userlogins = ulB.getAllUserLoginInfo();

        for(UserLogin u : userlogins) {
            writer.append(String.format("IDuser: %d\nUsername: %s\nPassword: %s\nRole: %s\nSalt: %d\n----\n",
                    u.getId(), u.getUsername(), u.getPassword(), u.getRole(), u.getSalt()));
        }
        writer.close();
    }
}