package com.keystroke;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class KeystrokeRenderer {

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();
        drawKeystrokeDisplay(mc);
    }

    private void drawKeystrokeDisplay(Minecraft mc) {
        int baseX = KeystrokeConfig.posX;
        int baseY = KeystrokeConfig.posY;
        int keySize = 50;
        int smallSpacing = 10;

        // Row 1: Up arrow in box (centered, larger)
        drawKeyBox(mc, baseX + 15, baseY, "↑", KeystrokeEventHandler.keysPressed[Keyboard.KEY_W]);

        // Row 2: Left, Down, Right arrows - TEXT ONLY (small)
        drawText(mc, baseX, baseY + keySize + 20, "←", KeystrokeEventHandler.keysPressed[Keyboard.KEY_A]);
        drawText(mc, baseX + 35, baseY + keySize + 20, "↓", KeystrokeEventHandler.keysPressed[Keyboard.KEY_S]);
        drawText(mc, baseX + 70, baseY + keySize + 20, "→", KeystrokeEventHandler.keysPressed[Keyboard.KEY_D]);

        // Row 3: LMB and RMB - TEXT ONLY
        drawText(mc, baseX, baseY + keySize + 50, "LMB", KeystrokeEventHandler.leftMousePressed);
        drawText(mc, baseX + 60, baseY + keySize + 50, "RMB", KeystrokeEventHandler.rightMousePressed);

        // Row 4: Numbers 4, |, 0 - TEXT ONLY
        drawText(mc, baseX, baseY + keySize + 80, "4", false);
        drawText(mc, baseX + 35, baseY + keySize + 80, "|", false);
        drawText(mc, baseX + 70, baseY + keySize + 80, "0", false);

        // Draw CPS counter above
        String cpsText = "CPS: " + KeystrokeEventHandler.getCPS();
        mc.fontRendererObj.drawStringWithShadow(cpsText, baseX, baseY - 20, 0xFFFFFF);
    }

    private void drawKeyBox(Minecraft mc, int x, int y, String text, boolean pressed) {
        int keySize = 50;
        int bgColor = pressed ? KeystrokeConfig.COLOR_ACTIVE : KeystrokeConfig.COLOR_INACTIVE;
        int textColor = pressed ? KeystrokeConfig.COLOR_TEXT_ACTIVE : KeystrokeConfig.COLOR_TEXT_INACTIVE;
        int borderColor = KeystrokeConfig.COLOR_BORDER;

        // Draw background
        drawRect(x, y, x + keySize, y + keySize, bgColor);

        // Draw border
        drawBorder(x, y, x + keySize, y + keySize, borderColor);

        // Draw text
        int textX = x + keySize / 2 - mc.fontRendererObj.getStringWidth(text) / 2;
        int textY = y + keySize / 2 - 4;
        mc.fontRendererObj.drawStringWithShadow(text, textX, textY, textColor);
    }

    private void drawText(Minecraft mc, int x, int y, String text, boolean pressed) {
        int textColor = pressed ? 0xFF00FF00 : 0xFFFFFFFF; // Green if pressed, white if not
        mc.fontRendererObj.drawStringWithShadow(text, x, y, textColor);
    }

    private void drawRect(int x1, int y1, int x2, int y2, int color) {
        float alpha = (color >> 24 & 255) / 255.0F;
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;

        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x1, y2);
        GL11.glVertex2f(x2, y2);
        GL11.glVertex2f(x2, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glEnd();
    }

    private void drawBorder(int x1, int y1, int x2, int y2, int color) {
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;

        GL11.glColor3f(red, green, blue);
        GL11.glLineWidth(2.0F);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x2, y1);
        GL11.glVertex2f(x2, y2);
        GL11.glVertex2f(x1, y2);
        GL11.glEnd();
        GL11.glLineWidth(1.0F);
    }
}
