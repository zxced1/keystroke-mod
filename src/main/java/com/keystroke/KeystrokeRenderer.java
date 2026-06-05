package com.keystroke;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class KeystrokeRenderer {

    private static final int BOX_SIZE = 28;
    private static final int BOX_SPACING = 3;

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();
        drawKeystrokeDisplay(mc);
    }

    private void drawKeystrokeDisplay(Minecraft mc) {
        int baseX = KeystrokeConfig.posX;
        int baseY = KeystrokeConfig.posY;

        // Row 1: Up arrow in box
        drawKeyBox(mc, baseX + BOX_SIZE + BOX_SPACING, baseY, "↑", KeystrokeEventHandler.keysPressed[Keyboard.KEY_W]);

        // Row 2: Left, Down, Right arrows in boxes
        drawKeyBox(mc, baseX, baseY + BOX_SIZE + BOX_SPACING, "←", KeystrokeEventHandler.keysPressed[Keyboard.KEY_A]);
        drawKeyBox(mc, baseX + BOX_SIZE + BOX_SPACING, baseY + BOX_SIZE + BOX_SPACING, "↓", KeystrokeEventHandler.keysPressed[Keyboard.KEY_S]);
        drawKeyBox(mc, baseX + 2 * (BOX_SIZE + BOX_SPACING), baseY + BOX_SIZE + BOX_SPACING, "→", KeystrokeEventHandler.keysPressed[Keyboard.KEY_D]);

        // Row 3: LMB and RMB boxes
        drawKeyBox(mc, baseX, baseY + 2 * (BOX_SIZE + BOX_SPACING), "LMB", KeystrokeEventHandler.leftMousePressed);
        drawKeyBox(mc, baseX + BOX_SIZE + BOX_SPACING, baseY + 2 * (BOX_SIZE + BOX_SPACING), "RMB", KeystrokeEventHandler.rightMousePressed);

        // Row 4: CPS box with format "LEFT | RIGHT"
        int leftClicks = KeystrokeEventHandler.leftClickCount;
        int rightClicks = KeystrokeEventHandler.rightClickCount;
        String cpsText = leftClicks + " | " + rightClicks;
        drawCPSBox(mc, baseX, baseY + 3 * (BOX_SIZE + BOX_SPACING), cpsText);
    }

    private void drawKeyBox(Minecraft mc, int x, int y, String text, boolean pressed) {
        int bgColor = KeystrokeConfig.COLOR_INACTIVE; // Always black
        int borderColor = pressed ? 0xFFFFFFFF : 0xFF808080; // White border if pressed, gray if not
        int textColor = pressed ? 0xFFFFFFFF : 0xFFFFFFFF; // White text

        // Draw background
        drawRect(x, y, x + BOX_SIZE, y + BOX_SIZE, bgColor);

        // Draw border
        drawBorder(x, y, x + BOX_SIZE, y + BOX_SIZE, borderColor, pressed ? 2.0F : 1.0F);

        // Draw text (bold/larger if pressed)
        int textX = x + BOX_SIZE / 2 - mc.fontRendererObj.getStringWidth(text) / 2;
        int textY = y + BOX_SIZE / 2 - 4;
        mc.fontRendererObj.drawStringWithShadow(text, textX, textY, textColor);
    }

    private void drawCPSBox(Minecraft mc, int x, int y, String text) {
        int bgColor = KeystrokeConfig.COLOR_INACTIVE; // Black
        int borderColor = 0xFF808080; // Gray border
        int textColor = 0xFFFFFFFF; // White text

        // Draw background
        drawRect(x, y, x + BOX_SIZE * 3 + BOX_SPACING * 2, y + BOX_SIZE, bgColor);

        // Draw border
        drawBorder(x, y, x + BOX_SIZE * 3 + BOX_SPACING * 2, y + BOX_SIZE, borderColor, 1.0F);

        // Draw text
        int textX = x + (BOX_SIZE * 3 + BOX_SPACING * 2) / 2 - mc.fontRendererObj.getStringWidth(text) / 2;
        int textY = y + BOX_SIZE / 2 - 4;
        mc.fontRendererObj.drawStringWithShadow(text, textX, textY, textColor);
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
