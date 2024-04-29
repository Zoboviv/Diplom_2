package client;

public class Credentials {
    private final String email;
    private final String password;
    private final String name;


    public static Credentials fromUser (User user){
        return new Credentials(user.getEmail(), user.getPassword(), user.getName());
    }

    public static Credentials authorization (String email, String password, String name){
        return new Credentials(email, password, name);
    }

    public Credentials(String email, String password, String name) {
        this.email=email;
        this.password=password;
        this.name=name;
    }
}