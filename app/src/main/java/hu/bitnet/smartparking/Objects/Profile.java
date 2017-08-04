package hu.bitnet.smartparking.Objects;

/**
 * Created by Attila on 2017.08.04..
 */

public class Profile {

    private String firstName;
    private String lastName;
    private String sessionid;
    private String email;
    private String phone;
    private String newsletter;
    private String userId;
    private String password;

    private String getFirstName() { return firstName; }
    private String getLastName() { return lastName; }
    public String getSessionid() { return sessionid; }
    private String getEmail() { return email; }
    private String getPhone() { return phone; }
    private String getNewsletter() { return newsletter; }
    private String getUserId() { return userId; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

}
