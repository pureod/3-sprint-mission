package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {

    private String userName;
    private String userId;
    private String userPassword;
    private String userEmail;

    public User(String userName, String userId, String userPassword, String userEmail) {
        super();
        this.userName = userName;
        this.userId = userId;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    protected String getUserPassword() {
        return userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void update(String username, String userId,
                       String userPassword, String userEmail) {
        this.userName = username;
        this.userId = userId;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        setUpdatedAt();
    }

    @Override
    public String toString() {
        return "[User] {" +
                "" + userName + '\'' +
                " " + userId + '\'' +
                " " + userPassword + '\'' +
                " " + userEmail + '\'' +
                '}' + '\n';
    }
}
