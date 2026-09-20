package com.cactus.social.friends;

public class Friend {
    private String nickname;

    public Friend(String name) {
        setName(name);
    }

    public void setName(String name) {
        this.nickname = name;
    }

    public String getName() {
        return nickname;
    }
}
