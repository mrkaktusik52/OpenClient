package com.cactus.social.serverintegration.servers;

import com.cactus.settings.BooleanSetting;
import com.cactus.social.notification.NotificationManager;
import com.cactus.social.notification.NotificationType;
import com.cactus.social.serverintegration.BaseServerIntegration;
import com.cactus.social.serverintegration.ServerIntegrationManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DonutSmpIntegration extends BaseServerIntegration {
    Pattern ITEM_BOUGHT = Pattern.compile("^You bought (.+) for (.+)$");
    Pattern SHARD_EARNED = Pattern.compile("^You earned (.+) for playing the server");
    Pattern ITEM_ALREADY_BOUGHT = Pattern.compile("^This item was already bought$");
    Pattern ITEM_LISTED = Pattern.compile("^You listed (.+) for (.+)$");
    Pattern ITEM_SOLD_AH = Pattern.compile("^(.+) bought your (.+) for (.+)$");
    Pattern ITEMS_DELIVERED = Pattern.compile("^You delivered (.+) and received (.+)$");
    Pattern ORDER_CREATED = Pattern.compile("^You ordered (.+)$");
    Pattern ORDERED_ITEM_DELIVERED = Pattern.compile("^(.+) delivered you (.+)");
    Pattern ORDER_COMPLETED = Pattern.compile("^Your (.+) order is complete!");
    Pattern TPA_REQUEST = Pattern.compile("^(.+) sent you a teleport request$");
    Pattern TPA_HERE_REQUEST = Pattern.compile("^(.+) sent you a teleport here request$");

    @Override
    public String getId() {
        return "donutsmp";
    }

    @Override
    public String getName() {
        return "Donut SMP";
    }

    @Override
    public boolean supports(String host) {
        return host.equals("donutsmp.net")
                || host.endsWith(".donutsmp.net");
    }

    private final BooleanSetting itemBoughtNotif = new BooleanSetting("Item bought", "item_bought", true);
    private final BooleanSetting shardEarnedNotif = new BooleanSetting("Shard Earned", "shard_earned", false);
    private final BooleanSetting itemAlreadyBoughtNotif = new BooleanSetting("Item Already Bought", "item_not_bought", true);
    private final BooleanSetting itemListedNotif = new BooleanSetting("Item Listed", "item_listed", true);
    private final BooleanSetting itemSoldNotif = new BooleanSetting("Item Sold on AH", "ah_sold", true);
    private final BooleanSetting itemDeliveredNotif = new BooleanSetting("Item Delivered", "item_delivered", true);
    private final BooleanSetting orderCreatedNotif = new BooleanSetting("Order Created", "order_created", true);
    private final BooleanSetting orderedItemDeliveredNotif = new BooleanSetting("Ordered Item Delivered", "order_delivered", false);
    private final BooleanSetting orderCompletedNotif = new BooleanSetting("Order Completed", "order_completed", true);
    private final BooleanSetting tpaRequest = new BooleanSetting("TPA Requests", "tpa_requests", true);

    public DonutSmpIntegration() {
        addSetting(itemBoughtNotif);
        addSetting(shardEarnedNotif);
        addSetting(itemAlreadyBoughtNotif);
        addSetting(itemListedNotif);
        addSetting(itemSoldNotif);
        addSetting(itemDeliveredNotif);
        addSetting(orderCreatedNotif);
        addSetting(orderedItemDeliveredNotif);
        addSetting(orderCompletedNotif);
        addSetting(tpaRequest);
    }


    @Override
    public void onChatMessage(String message) {

        Matcher itemBoughtMatcher =
                ServerIntegrationManager.matchMessage(ITEM_BOUGHT, message);

        if (itemBoughtMatcher != null && itemBoughtNotif.getValue()) {
            String item = itemBoughtMatcher.group(1);
            String cost = itemBoughtMatcher.group(2);

            NotificationManager.push(
                    "[" + this.getName() + "] Item bought",
                    "Bought " + item + " for " + cost,
                    NotificationType.INFO,
                    5000
            );

            return;
        }


        Matcher shardEarnedMatcher =
                ServerIntegrationManager.matchMessage(SHARD_EARNED, message);

        if (shardEarnedMatcher != null && shardEarnedNotif.getValue()) {
            String shard = shardEarnedMatcher.group(1);

            NotificationManager.push(
                    "[" + this.getName() + "] Shard earned",
                    "You earned " + shard,
                    NotificationType.INFO,
                    4000
            );

            return;
        }


        Matcher alreadyBoughtMatcher =
                ServerIntegrationManager.matchMessage(ITEM_ALREADY_BOUGHT, message);

        if (alreadyBoughtMatcher != null && itemAlreadyBoughtNotif.getValue()) {
            NotificationManager.push(
                    "[" + this.getName() + "] Purchase failed",
                    "This item was already bought",
                    NotificationType.WARNING,
                    4000
            );

            return;
        }


        Matcher itemListedMatcher =
                ServerIntegrationManager.matchMessage(ITEM_LISTED, message);

        if (itemListedMatcher != null && itemListedNotif.getValue()) {
            String item = itemListedMatcher.group(1);
            String cost = itemListedMatcher.group(2);

            NotificationManager.push(
                    "[" + this.getName() + "] Item listed",
                    "Listed " + item + " for " + cost,
                    NotificationType.INFO,
                    4000
            );

            return;
        }


        Matcher itemSoldMatcher =
                ServerIntegrationManager.matchMessage(ITEM_SOLD_AH, message);

        if (itemSoldMatcher != null && itemSoldNotif.getValue()) {
            String buyer = itemSoldMatcher.group(1);
            String item = itemSoldMatcher.group(2);
            String cost = itemSoldMatcher.group(3);

            NotificationManager.push(
                    "[" + this.getName() + "] Item sold",
                    buyer + " bought your " + item + " for " + cost,
                    NotificationType.INFO,
                    5000
            );

            return;
        }


        Matcher itemDeliveredMatcher =
                ServerIntegrationManager.matchMessage(ITEMS_DELIVERED, message);

        if (itemDeliveredMatcher != null && itemDeliveredNotif.getValue()) {
            String delivered = itemDeliveredMatcher.group(1);
            String received = itemDeliveredMatcher.group(2);

            NotificationManager.push(
                    "[" + this.getName() + "] Items delivered",
                    "Delivered " + delivered + " and received " + received,
                    NotificationType.INFO,
                    5000
            );

            return;
        }


        Matcher orderCreatedMatcher =
                ServerIntegrationManager.matchMessage(ORDER_CREATED, message);

        if (orderCreatedMatcher != null && orderCreatedNotif.getValue()) {
            String order = orderCreatedMatcher.group(1);

            NotificationManager.push(
                    "[" + this.getName() + "] Order created",
                    "Ordered " + order,
                    NotificationType.SUCCESS,
                    4000
            );

            return;
        }


        Matcher orderedItemDeliveredMatcher =
                ServerIntegrationManager.matchMessage(
                        ORDERED_ITEM_DELIVERED,
                        message
                );

        if (orderedItemDeliveredMatcher != null
                && orderedItemDeliveredNotif.getValue()) {

            String player = orderedItemDeliveredMatcher.group(1);
            String item = orderedItemDeliveredMatcher.group(2);

            NotificationManager.push(
                    "[" + this.getName() + "] Order delivery",
                    player + " delivered you " + item,
                    NotificationType.INFO,
                    5000
            );

            return;
        }


        Matcher orderCompletedMatcher =
                ServerIntegrationManager.matchMessage(
                        ORDER_COMPLETED,
                        message
                );

        if (orderCompletedMatcher != null
                && orderCompletedNotif.getValue()) {

            String item = orderCompletedMatcher.group(1);

            NotificationManager.push(
                    "[" + this.getName() + "] Order complete",
                    "Your " + item + " order is complete!",
                    NotificationType.SUCCESS,
                    5000
            );
        }

        Matcher tpaRequestMatcher =
                ServerIntegrationManager.matchMessage(
                        TPA_REQUEST,
                        message
                );

        if (tpaRequestMatcher != null
                && tpaRequest.getValue()) {

            String name = tpaRequestMatcher.group(1);

            NotificationManager.push(
                    "[" + this.getName() + "] Teleport request",
                    name + " wants to teleport to you",
                    NotificationType.INFO,
                    5000
            );
        }

        Matcher tpaHereRequestMatcher =
                ServerIntegrationManager.matchMessage(
                        TPA_HERE_REQUEST,
                        message
                );

        if (tpaHereRequestMatcher != null
                && tpaRequest.getValue()) {

            String name = tpaHereRequestMatcher.group(1);

            NotificationManager.push(
                    "[" + this.getName() + "] Teleport request",
                    name + " wants you to teleport to them",
                    NotificationType.INFO,
                    5000
            );
        }
    }
}
