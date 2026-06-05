package com.keystroke;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class KeystrokeEventHandler {

    public static boolean[] keysPressed = new boolean[256];
    public static boolean leftMousePressed = false;
    public static boolean rightMousePressed = false;
    
    public static int leftClickCount = 0;
    public static int rightClickCount = 0;
    private static long lastLeftClickTime = 0;
    private static long lastRightClickTime = 0;
    private static boolean wasLeftPressed = false;
    private static boolean wasRightPressed = false;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            updateKeyStates();
            updateMouseStates();
            resetCPSCounters();
        }
    }

    private void updateKeyStates() {
        // W - Forward
        keysPressed[Keyboard.KEY_W] = Keyboard.isKeyDown(Keyboard.KEY_W);
        // A - Left
        keysPressed[Keyboard.KEY_A] = Keyboard.isKeyDown(Keyboard.KEY_A);
        // S - Backward
        keysPressed[Keyboard.KEY_S] = Keyboard.isKeyDown(Keyboard.KEY_S);
        // D - Right
        keysPressed[Keyboard.KEY_D] = Keyboard.isKeyDown(Keyboard.KEY_D);
        // Space
        keysPressed[Keyboard.KEY_SPACE] = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
    }

    private void updateMouseStates() {
        long currentTime = System.currentTimeMillis();

        // Left click
        if (Mouse.isButtonDown(0)) {
            leftMousePressed = true;
            if (!wasLeftPressed) {
                leftClickCount++;
                lastLeftClickTime = currentTime;
                wasLeftPressed = true;
            }
        } else {
            leftMousePressed = false;
            wasLeftPressed = false;
        }

        // Right click
        if (Mouse.isButtonDown(1)) {
            rightMousePressed = true;
            if (!wasRightPressed) {
                rightClickCount++;
                lastRightClickTime = currentTime;
                wasRightPressed = true;
            }
        } else {
            rightMousePressed = false;
            wasRightPressed = false;
        }
    }

    private void resetCPSCounters() {
        long currentTime = System.currentTimeMillis();

        // Reset left clicks after 1 second of no clicks
        if (currentTime - lastLeftClickTime > 1000) {
            leftClickCount = 0;
        }

        // Reset right clicks after 1 second of no clicks
        if (currentTime - lastRightClickTime > 1000) {
            rightClickCount = 0;
        }
    }

    public static int getTotalCPS() {
        return leftClickCount + rightClickCount;
    }
}
