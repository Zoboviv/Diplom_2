package client;

public class UserData {
    private final String email;
    private final String name;

    public static UserData changingData (String email, String name){
        return new UserData(email, name);
    }

    public UserData(String email, String name) {
        this.email=email;
        this.name=name;
    }
}
