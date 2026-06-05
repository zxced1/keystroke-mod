package com.keystroke;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class KeystrokeRenderer {

    private static final int BOX_SIZE = 22;
    private static final int BOX_SPACING = 2;

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();
        drawKeystrokeDisplay(mc);
    }

    private void drawKeystrokeDisplay(Minecraft mc) {
        int baseX = KeystrokeConfig.posX;
        int baseY = KeystrokeConfig.posY;

        // Row 1: Up arrow
        drawKeyBox(mc, baseX + BOX_SIZE + BOX_SPACING, baseY, "↑", KeystrokeEventHandler.keysPressed[Keyboard.KEY_W]);

        // Row 2: Left, Down, Right
        drawKeyBox(mc, baseX, baseY + BOX_SIZE + BOX_SPACING, "←", KeystrokeEventHandler.keysPressed[Keyboard.KEY_A]);
        drawKeyBox(mc, baseX + BOX_SIZE + BOX_SPACING, baseY + BOX_SIZE + BOX_SPACING, "↓", KeystrokeEventHandler.keysPressed[Keyboard.KEY_S]);
        drawKeyBox(mc, baseX + 2 * (BOX_SIZE + BOX_SPACING), baseY + BOX_SIZE + BOX_SPACING, "→", KeystrokeEventHandler.keysPressed[Keyboard.KEY_D]);

        // Row 3: LMB and RMB
        drawKeyBox(mc, baseX, baseY + 2 * (BOX_SIZE + BOX_SPACING), "LMB", KeystrokeEventHandler.leftMousePressed);
        drawKeyBox(mc, baseX + BOX_SIZE + BOX_SPACING, baseY + 2 * (BOX_SIZE + BOX_SPACING), "RMB", KeystrokeEventHandler.rightMousePressed);

        // Row 4: CPS box
        int leftClicks = KeystrokeEventHandler.leftClickCount;
        int rightClicks = KeystrokeEventHandler.rightClickCount;
        String cpsText = leftClicks + " | " + rightClicks;
        drawCPSBox(mc, baseX, baseY + 3 * (BOX_SIZE + BOX_SPACING), cpsText);
    }

    private void drawKeyBox(Minecraft mc, int x, int y, String text, boolean pressed) {
        int bgColor;
        int textColor;

        if (pressed) {
            bgColor = 0xFFFFFFFF; // White background
            textColor = 0xFF000000; // Black text
        } else {
            bgColor = 0xFF000000; // Black background
            textColor = 0xFFFFFFFF; // White text
        }

        // Draw background
        drawRect(x, y, x + BOX_SIZE, y + BOX_SIZE, bgColor);

        // Draw border
        drawBorder(x, y, x + BOX_SIZE, y + BOX_SIZE, 0xFF404040, 1.5F);

        // Draw text bold
        drawBoldText(mc, text, x + BOX_SIZE / 2, y + BOX_SIZE / 2, textColor);
    }

    private void drawCPSBox(Minecraft mc, int x, int y, String text) {
        int bgColor = 0xFF2d4a3a; // Dark green-black
        int textColor = 0xFFFFFFFF; // White text

        int boxWidth = BOX_SIZE * 3 + BOX_SPACING * 2;

        // Draw background
        drawRect(x, y, x + boxWidth, y + BOX_SIZE, bgColor);

        // Draw border
        drawBorder(x, y, x + boxWidth, y + BOX_SIZE, 0xFF404040, 1.5F);

        // Draw text bold
        drawBoldText(mc, text, x + boxWidth / 2, y + BOX_SIZE / 2, textColor);
    }

    private void drawBoldText(Minecraft mc, String text, int centerX, int centerY, int color) {
        FontRenderer fr = mc.fontRendererObj;
        int width = fr.getStringWidth(text);
        int x = centerX - width / 2;
        int y = centerY - 4;

        // Draw text twice for bold effect
        fr.drawStringWithShadow(text, x, y, color);
        fr.drawStringWithShadow(text, x + 1, y, color);
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

    private void drawBorder(int x1, int y1, int x2, int y2, int color, float width) {
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;

        GL11.glColor3f(red, green, blue);
        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x2, y1);
        GL11.glVertex2f(x2, y2);
        GL11.glVertex2f(x1, y2);
        GL11.glEnd();
        GL11.glLineWidth(1.0F);
    }
}
