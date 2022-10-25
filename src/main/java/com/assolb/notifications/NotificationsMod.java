package com.assolb.notifications;

import com.assolb.notifications.proxy.CommonProxy;
import com.assolb.notifications.utils.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Constants.MODID, name = Constants.NAME, version = Constants.VERSION, acceptedMinecraftVersions = Constants.ACCEPTED_VERSIONS)
public class NotificationsMod {

    @Mod.Instance(Constants.MODID)
    public static NotificationsMod instance;

    @SidedProxy(clientSide = Constants.CLIENT_PROXY_CLASS, serverSide = Constants.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }
}
