package com.assolb.notifications.client.gui;

import com.assolb.notifications.Notification;
import com.assolb.notifications.client.NotificationsManager;
import com.assolb.notifications.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

@Mod.EventBusSubscriber(modid = Constants.MODID, value = Side.CLIENT)
public class NotificationsGui extends Gui {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/gui/notification.png");

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Post e)
    {
        if (e.getType() != RenderGameOverlayEvent.ElementType.ALL || Minecraft.getMinecraft().currentScreen != null) return;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();

        GlStateManager.translate(0, 0, 0);
        float startX = e.getResolution().getScaledWidth() - 140;
        float startY = 10;
        for(int i = 0; i < NotificationsManager.getNotifications().size(); i++)
        {
            Notification notification = NotificationsManager.getNotifications().get(i);
            GlStateManager.pushMatrix();
            GlStateManager.color(1, 1, 1, notification.getOpacity() < 0 ? 0 : notification.getOpacity());
            Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
            drawScaledCustomSizeModalRect(startX + 180 * (1 - notification.getOpacity()), startY + i * 35, 0, 0, 417, 65, 180, 30, 417, 65);

            Color color = new Color(1, 1, 1, Math.max(notification.getOpacity(), 0.1f));

            List<String> lines = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(notification.getText(), 130);
            if(lines.size() == 1)
            {
                Minecraft.getMinecraft().fontRenderer.drawString(lines.get(0), (int) (startX + 10 + 180 * (1 - notification.getOpacity())), (int) (startY + 11 + i * 35), color.getRGB());
            }
            else
            {
                for (int j = 0; j < lines.size() && j < 2; j++)
                {
                    if (j == 1 && lines.size() > 2)
                    {
                        lines.set(1, lines.get(1).substring(0, lines.get(1).length() - 3) + "...");
                    }
                    Minecraft.getMinecraft().fontRenderer.drawString(lines.get(j), (int) (startX + 10 + 180 * (1 - notification.getOpacity())), (int) (startY + 6 + i * 35 + j * 10), color.getRGB());
                }
            }
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.popMatrix();
        }
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public static void onDrawGuiScreen(GuiScreenEvent.DrawScreenEvent.Post e)
    {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        RenderHelper.disableStandardItemLighting();

        GlStateManager.translate(0, 0, 1000);
        float startX = e.getGui().width - 140;
        float startY = 10;
        for(int i = 0; i < NotificationsManager.getNotifications().size(); i++)
        {
            Notification notification = NotificationsManager.getNotifications().get(i);
            GlStateManager.pushMatrix();
            GlStateManager.color(1, 1, 1, notification.getOpacity() < 0 ? 0 : notification.getOpacity());
            Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
            drawScaledCustomSizeModalRect(startX + 180 * (1 - notification.getOpacity()), startY + i * 35, 0, 0, 417, 65, 180, 30, 417, 65);

            Color color = new Color(1, 1, 1, Math.max(notification.getOpacity(), 0.1f));

            List<String> lines = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(notification.getText(), 130);
            if (lines.size() == 1)
            {
                Minecraft.getMinecraft().fontRenderer.drawString(lines.get(0), (int) (startX + 10 + 180 * (1 - notification.getOpacity())), (int) (startY + 11 + i * 35), color.getRGB());
            }
            else
            {
                for (int j = 0; j < lines.size() && j < 2; j++)
                {
                    if (j == 1 && lines.size() > 2)
                    {
                        lines.set(1, lines.get(1).substring(0, lines.get(1).length() - 3) + "...");
                    }
                    Minecraft.getMinecraft().fontRenderer.drawString(lines.get(j), (int) (startX + 10 + 180 * (1 - notification.getOpacity())), (int) (startY + 6 + i * 35 + j * 10), color.getRGB());
                }
            }
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.popMatrix();
        }
        RenderHelper.enableStandardItemLighting();
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public static void onItemAction(PlayerInteractEvent e)
    {
        if(e.getHand() == EnumHand.OFF_HAND) return;
        NotificationsManager.addNotification("You clicked on " + e.getItemStack().getDisplayName());
    }

    public static void drawScaledCustomSizeModalRect(float x, float y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight)
    {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)x, (double)(y + height), 0.0D).tex((double)(u * f), (double)((v + (float)vHeight) * f1)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), 0.0D).tex((double)((u + (float)uWidth) * f), (double)((v + (float)vHeight) * f1)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)y, 0.0D).tex((double)((u + (float)uWidth) * f), (double)(v * f1)).endVertex();
        bufferbuilder.pos((double)x, (double)y, 0.0D).tex((double)(u * f), (double)(v * f1)).endVertex();
        tessellator.draw();
    }
}
