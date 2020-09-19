package xyz.WorstClient.ui.Waitz;

import com.google.common.collect.Lists;
import xyz.WorstClient.Client;
import xyz.WorstClient.api.value.Value;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.ui.fontRenderer.UnicodeFontRenderer;
import xyz.WorstClient.utils.render.RenderUtil;

import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.gui.Gui;

public class Button {
	public Module cheat;
	UnicodeFontRenderer font = Client.fontManager.comfortaa18;
	public Window parent;
	public int x;
	public int y;
	public int index;
	public int remander;
	public double opacity = 0.0;
	public ArrayList<ValueButton> buttons = Lists.newArrayList();
	public boolean expand;

	public Button(Module cheat, int x, int y) {
		UnicodeFontRenderer font = Client.fontManager.comfortaa18;
		UnicodeFontRenderer mfont = Client.fontManager.verdana16;
		UnicodeFontRenderer bigfont = Client.fontManager.comfortaa24;
		this.cheat = cheat;
		this.x = x;
		this.y = y;
		int y2 = y + 14;
		for (Value v : cheat.getValues()) {
			this.buttons.add(new ValueButton(v, x + 5, y2));
			y2 += 15;
		}
	}

	public void render(int mouseX, int mouseY) {
		UnicodeFontRenderer font = Client.fontManager.comfortaa18;
		UnicodeFontRenderer mfont = Client.fontManager.verdana16;
		UnicodeFontRenderer bigfont = Client.fontManager.comfortaa24;
		if (this.index != 0) {
			Button b2 = this.parent.buttons.get(this.index - 1);
			this.y = b2.y + 15 + (b2.expand ? 15 * b2.buttons.size() : 0);
		}
		int i = 0;
		while (i < this.buttons.size()) {
			this.buttons.get((int) i).y = this.y + 14 + 15 * i;
			this.buttons.get((int) i).x = this.x + 5;
			++i;
		}
		if(this.y < Window.GetMaxLong()) {
		if (this.cheat.isEnabled()) {
			RenderUtil.drawRect(this.x - 5, this.y - 5, this.x + 85,(float) (this.y + font.getStringHeight(this.cheat.getName()) + 3.8),new Color(200, 200, 200).getRGB());
			Client.fontManager.verdana14.drawString(this.cheat.getName(), this.x, this.y, new Color(0, 0, 0).getRGB());
		} else {
			Client.fontManager.verdana14.drawString(this.cheat.getName(), this.x, this.y, new Color(55, 55, 55).getRGB());
		}
		
		if (!this.expand && this.buttons.size() >= 1) {
			Client.fontManager.verdana14.drawString("+", this.x + 75, this.y -1, new Color(140, 140, 140).getRGB());
		}
		if (this.expand) {
			this.buttons.forEach(b -> b.render(mouseX, mouseY));
		}
	}
	}

	public void key(char typedChar, int keyCode) {
		this.buttons.forEach(b -> b.key(typedChar, keyCode));
	}

	public void click(int mouseX, int mouseY, int button) {
		if (mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6
				&& mouseY < this.y + font.getStringHeight(this.cheat.getName()) + 4) {
			if (button == 0) {
				this.cheat.setEnabled(!this.cheat.isEnabled());
			}
			if (button == 1 && !this.buttons.isEmpty()) {
				boolean bl = this.expand = !this.expand;
			}
		}
		if (this.expand) {
			this.buttons.forEach(b -> b.click(mouseX, mouseY, button));
		}
	}

	public void setParent(Window parent) {
		this.parent = parent;
		int i = 0;
		while (i < this.parent.buttons.size()) {
			if (this.parent.buttons.get(i) == this) {
				this.index = i;
				this.remander = this.parent.buttons.size() - i;
				break;
			}
			++i;
		}
	}
}
