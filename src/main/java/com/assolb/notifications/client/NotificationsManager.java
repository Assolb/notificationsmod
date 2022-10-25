package com.assolb.notifications.client;

import com.assolb.notifications.Notification;
import com.assolb.notifications.utils.ConcurrentList;
import com.assolb.notifications.utils.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod.EventBusSubscriber(modid = Constants.MODID, value = Side.CLIENT)
public class NotificationsManager {

    private static final List<Notification> NOTIFICATIONS = new ArrayList<>();

    public static void addNotification(String text)
    {
        addNotification(new Notification(text));
    }

    public static void addNotification(String text, int time)
    {
        addNotification(new Notification(text, time));
    }

    public static void addNotification(Notification notification)
    {
        NOTIFICATIONS.add(notification);
    }

    @SubscribeEvent
    public static void onTickClient(TickEvent.ClientTickEvent e)
    {
        if(e.phase == TickEvent.Phase.END) return;
        Iterator<Notification> iterator = NOTIFICATIONS.iterator();
        while(iterator.hasNext())
        {
            Notification notification = iterator.next();
            if(!notification.onUpdate()) iterator.remove();
        }
    }

    public static List<Notification> getNotifications()
    {
        return NOTIFICATIONS;
    }
}
