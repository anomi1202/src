package uspn.test.data;


/**
 * Created by Andrey.Filyuta on 11.12.2015.
 */
public class USPNUser {

    private String login;
    private String password;

    public USPNUser(String login, String password){
        this.login = login;
        this.password = password;
    }

    public String getLogin(){
        return login;
    }

    public String getPassword(){
        return password;
    }

    public static USPNUser getConfigUser(){
        return new USPNUser(null, null);
    }

}
