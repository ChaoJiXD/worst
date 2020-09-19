package xyz.WorstClient.ui.fontRenderer;

import java.awt.Color;
import java.awt.Font;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class UnicodeFontRenderer extends FontRenderer {
	private final UnicodeFont font;

    public UnicodeFontRenderer(Font awtFont) {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
        this.font = new UnicodeFont(awtFont);
        this.font.addAsciiGlyphs();
        this.font.getEffects().add(new ColorEffect(Color.WHITE));
        try {
            this.font.loadGlyphs();
        }
        catch (SlickException exception) {
            throw new RuntimeException(exception);
        }
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        this.FONT_HEIGHT = this.font.getHeight(alphabet) / 2;
    } 
    
    public void drawTotalCenteredStringWithShadow(String text, double x, double y, int color) {
		drawStringWithShadow(text, (float)(x - getStringWidth(text) / 2), (float)(y - FONT_HEIGHT / 2F), color);
	}
    
    public void drawTotalCenteredString(String text, int x, int y, int color) {
		drawString(text, x - getStringWidth(text) / 2, y - FONT_HEIGHT / 2, color);
	}
	
	public void drawTotalCenteredString(String text, double x, double y, int color) {
		drawString(text, x - getStringWidth(text) / 2, y - FONT_HEIGHT / 2, color);
	}

    public int drawNoBSString(String text, double d, float y2, int color) {
        text = text.replaceAll("\u00c3\u201a", "");
        drawString(text,d,y2,color);
        return 0;
    }
	
    public int drawString(String string, float x, float y, int color) {
        if (string == null) {
            return 0;
        }
        GL11.glPushMatrix();
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        boolean blend = GL11.glIsEnabled((int)3042);
        boolean lighting = GL11.glIsEnabled((int)2896);
        boolean texture = GL11.glIsEnabled((int)3553);
        if (!blend) {
            GL11.glEnable((int)3042);
        }
        if (lighting) {
            GL11.glDisable((int)2896);
        }
        if (texture) {
            GL11.glDisable((int)3553);
        }
        this.font.drawString(x *= 2.0f, y *= 2.0f, string, new org.newdawn.slick.Color(color));
        if (texture) {
            GL11.glEnable((int)3553);
        }
        if (lighting) {
            GL11.glEnable((int)2896);
        }
        if (!blend) {
            GL11.glDisable((int)3042);
        }
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glPopMatrix();
        GlStateManager.bindTexture(0);
        return (int)x;
    }
    
    public float drawString(String string, double x, double y, int color) {
        if (string == null) {
            return 0.0f;
        }
        GL11.glPushMatrix();
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        boolean blend = GL11.glIsEnabled((int)3042);
        boolean lighting = GL11.glIsEnabled((int)2896);
        boolean texture = GL11.glIsEnabled((int)3553);
        if (!blend) {
            GL11.glEnable((int)3042);
        }
        if (lighting) {
            GL11.glDisable((int)2896);
        }
        if (texture) {
            GL11.glDisable((int)3553);
        }
        this.font.drawString((float)(x *= 2.0f), (float)(y *= 2.0f), string, new org.newdawn.slick.Color(color));
        if (texture) {
            GL11.glEnable((int)3553);
        }
        if (lighting) {
            GL11.glEnable((int)2896);
        }
        if (!blend) {
            GL11.glDisable((int)3042);
        }
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glPopMatrix();
        GlStateManager.bindTexture(0);
        return (int)x;
    }

    @Override
    public int drawStringWithShadow(String text, float x, float y, int color) {
        this.drawString(text, x + 0.5f, y + 0.5f, -16777216);
        return (int) this.drawString(text, x, y, color);
    }

    @Override
    public int getCharWidth(char c) {
        return this.getStringWidth(Character.toString(c));
    }
    
    @Override
    public int getStringWidth(String string) {
        return this.font.getWidth(string) / 2;
    }

    public int getStringHeight(String string) {
        return this.font.getHeight(string) / 2;
    }

    public void drawCenteredString(String text, float x, float y, int color) {
        this.drawString(text, x - (float)(this.getStringWidth(text) / 2), y, color);
    }
    
    public void drawCenteredString(String text, double x, double y, int color) {
        this.drawString(text, x - (float)(this.getStringWidth(text) / 2), y, color);
    }
    
    
}
