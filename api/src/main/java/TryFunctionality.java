
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

        ulB.insertUserLoginSingle("newUser",
                "5c531afb2e4106cc78f99ff895ef99d2fa587f7272a91f82244f06caddf89176c299afc0ef4a447d6c5042dff4690e271a3d09aabf97465faba20591f818ab27",
                "admin");

        List<UserLogin> userlogins = ulB.getAllUserLoginInfo();

        boolean couldUserLogin = ulB.authenticateUser("newUser", "1234");

        writer.append("Could user newUser potentially log in?" + couldUserLogin);


        for(UserLogin u : userlogins) {
            log.info(String.format("IDuser: %d\nUsername: %s\nPassword: %s\nRole: %s\nSalt: %d\n",
                    u.getIdUser(), u.getUsername(), u.getPassword(), u.getRole(), u.getSalt()));
            writer.append(String.format("IDuser: %d\nUsername: %s\nPassword: %s\nRole: %s\nSalt: %d\n----\n",
                    u.getIdUser(), u.getUsername(), u.getPassword(), u.getRole(), u.getSalt()));
        }
        writer.close();
    }
}