package com.cactus.social.friends;

import com.cactus.social.notification.NotificationManager;
import com.cactus.social.notification.NotificationType;
import com.cactus.social.serverintegration.ServerIntegration;

import java.util.ArrayList;
import java.util.List;

public class FriendsManager {
    private static final List<Friend> friends = new ArrayList<>();

    public static void addFriend(String name) {
        for (int i = 0; i < friends.size(); i++) {
            if (friends.get(i).getName().equals(name)) {
                NotificationManager.push("This friend already exsists", name +" is your friend", NotificationType.ERROR, 5000);
                return;
            }
        }
        friends.add(new Friend(name));
    }

    public static List<Friend> getFriends() {
        return friends;
    }

    public static void delFriend(String name) {
        for (int i = 0; i < friends.size(); i++) {
            if (friends.get(i).getName().equals(name)) {
                friends.remove(i);
                return;
            }
        }
    }
}
