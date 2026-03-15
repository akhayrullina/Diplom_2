package utils.pojo;

import java.time.LocalDateTime;

public class User {
    private String email;
    private String password;
    private String name;

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public static User random() {
        var rnd = LocalDateTime.now().getNano();
        return new User("test" + rnd + "@gmail.ru", "test" + rnd, "testtest");
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
