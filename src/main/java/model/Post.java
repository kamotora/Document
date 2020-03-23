package model;

public class Post {
    private String name;
    public static final String CHECKING_FILE = "/jsons/checking_posts.txt";
    public static final String RESP_FILE = "/jsons/responsible_posts.txt";
    public Post() {
    }

    public Post(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
