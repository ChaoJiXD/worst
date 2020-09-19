package xyz.WorstClient.ui.fontRenderer;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;

public class FontManager {
	private HashMap<String, HashMap<Float, UnicodeFontRenderer>> fonts = new HashMap();
	public UnicodeFontRenderer verdana12;
	public UnicodeFontRenderer verdana14;
	public UnicodeFontRenderer verdana16;
	public UnicodeFontRenderer verdana20;
	public UnicodeFontRenderer sigmaarr;
	public UnicodeFontRenderer zeroarr;
	public UnicodeFontRenderer zeroarr2;
	public UnicodeFontRenderer comfortaa216;
	public UnicodeFontRenderer comfortaa14;
	public UnicodeFontRenderer comfortaa20;
	public UnicodeFontRenderer comfortaa40;
	public UnicodeFontRenderer comfortaa18;
	public UnicodeFontRenderer comfortaa24;
	public UnicodeFontRenderer comfortaa34;
	public UnicodeFontRenderer jigsaw18;
	public UnicodeFontRenderer googlesans17;
	public UnicodeFontRenderer wqy12;
	public UnicodeFontRenderer wqy14;
	public UnicodeFontRenderer wqy16;
	public UnicodeFontRenderer wqy20;
	
	public FontManager() {
		verdana12 = this.getFont("verdana.ttf", 12f);
		verdana14 = this.getFont("verdana.ttf", 14f);
		verdana16 = this.getFont("verdana.ttf", 16f);
		verdana20 = this.getFont("verdana.ttf", 20f);
		
		wqy12 = this.getFont("wqy_microhei.ttf", 12f);
		wqy14 = this.getFont("wqy_microhei.ttf", 14f);
		wqy16 = this.getFont("wqy_microhei.ttf", 16f);
		wqy20 = this.getFont("wqy_microhei.ttf", 20f);
		
		sigmaarr = this.getFont("sigma.ttf", 8f);
		zeroarr = this.getFont("comfortaa.ttf", 16f);
		zeroarr2 = this.getFont("comfortaa.ttf", 15f);
		comfortaa24 = this.getFont("comfortaa.ttf", 24f);
		comfortaa216 = this.getFont("comfortaa2.ttf", 17f);
		comfortaa14 = this.getFont("comfortaa.ttf", 14f);
		comfortaa40 = this.getFont("comfortaa.ttf", 40f);
		comfortaa20 = this.getFont("comfortaa.ttf", 20f);
		comfortaa24 = this.getFont("comfortaa.ttf", 24f);
		comfortaa18 = this.getFont("comfortaa.ttf", 18f);
		comfortaa34 = this.getFont("comfortaa.ttf", 34f);
		jigsaw18 = this.getFont("Jigsaw.otf", 18f);
		googlesans17 = this.getFont("googlesans.ttf", 17f);
	}
	
	public UnicodeFontRenderer getFont(String name, float size) {
        UnicodeFontRenderer unicodeFont = null;
        try {
            if (this.fonts.containsKey(name) && this.fonts.get(name).containsKey(Float.valueOf(size))) {
                return this.fonts.get(name).get(Float.valueOf(size));
            }
            InputStream inputStream = this.getClass().getResourceAsStream("fonts/" + name);
            Font font = null;
            font = Font.createFont(0, inputStream);
            unicodeFont = new UnicodeFontRenderer(font.deriveFont(size));
            unicodeFont.setUnicodeFlag(true);
            unicodeFont.setBidiFlag(Minecraft.getMinecraft().mcLanguageManager.isCurrentLanguageBidirectional());
            HashMap<Float, UnicodeFontRenderer> map = new HashMap<Float, UnicodeFontRenderer>();
            if (this.fonts.containsKey(name)) {
                map.putAll((Map)this.fonts.get(name));
            }
            map.put(Float.valueOf(size), unicodeFont);
            this.fonts.put(name, map);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return unicodeFont;
    }

    public UnicodeFontRenderer getFont(String name, float size, boolean b) {
        UnicodeFontRenderer unicodeFont = null;
        try {
            if (this.fonts.containsKey(name) && this.fonts.get(name).containsKey(Float.valueOf(size))) {
                return this.fonts.get(name).get(Float.valueOf(size));
            }
            InputStream inputStream = this.getClass().getResourceAsStream("fonts/" + name + ".otf");
            Font font = null;
            font = Font.createFont(0, inputStream);
            unicodeFont = new UnicodeFontRenderer(font.deriveFont(size));
            unicodeFont.setUnicodeFlag(true);
            unicodeFont.setBidiFlag(Minecraft.getMinecraft().mcLanguageManager.isCurrentLanguageBidirectional());
            HashMap<Float, UnicodeFontRenderer> map = new HashMap<Float, UnicodeFontRenderer>();
            if (this.fonts.containsKey(name)) {
                map.putAll((Map)this.fonts.get(name));
            }
            map.put(Float.valueOf(size), unicodeFont);
            this.fonts.put(name, map);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return unicodeFont;
    }
}
