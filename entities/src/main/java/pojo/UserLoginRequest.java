package pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
    Class used for requests regarding user login.
    Separate class was made for request because UserLoginRequest.password is plain-text password
    (represents the password that was entered by user) whereas UserLogin.password represent hashed
    and salted password.
 */
@XmlRootElement
public class UserLoginRequest {
    @XmlElement(required = true)
    private String username;
    @XmlElement(required = true)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
