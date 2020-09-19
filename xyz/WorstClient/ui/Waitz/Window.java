package xyz.WorstClient.ui.Waitz;

import com.google.common.collect.Lists;
import xyz.WorstClient.management.ModuleManager;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.Client;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.api.value.Value;
import xyz.WorstClient.utils.RenderUtils;
import xyz.WorstClient.utils.render.RenderUtil;
import net.minecraft.client.gui.Gui;

import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;

public class Window {
	public ModuleType category;
	public ArrayList<Button> buttons = Lists.newArrayList();
	public boolean drag;
	public boolean extended;
	public int x;
	public int y;
	public int expand;
	public int dragX;
	public int dragY;
	public int max;
	public int scroll;
	public int scrollTo;
	public double angel;
	public static int maxLong;
	public static int GetMaxLong() {
		return maxLong;
	}
	public Window(ModuleType category, int x, int y) {
		this.category = category;
		this.x = x;
		this.y = y;
		this.max = 120;
		int y2 = y + 22;
		for (Module c : ModuleManager.getModules()) {
			if (c.getType() != category)
				continue;
			this.buttons.add(new Button(c, x + 5, y2));
			y2 += 15;
		}
		for (Button b2 : this.buttons) {
			b2.setParent(this);
		}
	}

	public void render(int mouseX, int mouseY) {
		maxLong=this.y+3  + this.expand;
		int current = 0;
		for (Button b3 : this.buttons) {
			if (b3.expand) {
				for (ValueButton v : b3.buttons) {
					current += 15;
				}
			}
			current += 15;
		}
		int height = 15 + current;
		if (this.extended) {
			this.expand = this.expand + 5 < height ? (this.expand += 5) : height;
			this.angel = this.angel + 20.0 < 180.0 ? (this.angel += 20.0) : 180.0;
		} else {
			this.expand = this.expand - 5 > 0 ? (this.expand -= 5) : 0;
			this.angel = this.angel - 20.0 > 0.0 ? (this.angel -= 20.0) : 0.0;
		}
        RenderUtils.drawRect(this.x-2, this.y + 2, this.x + 92, this.y + 5 + this.expand, new Color(0,141,255).getRGB());
        if(this.y + 5 + this.expand>this.y+15) {
        Gui.drawRect(this.x, this.y+17, this.x + 90, this.y+3  + this.expand, new Color(255, 255, 255).getRGB());
        }else {
        	RenderUtils.drawRect(this.x-2, this.y + 2, this.x + 92, this.y + 17, new Color(0,141,255).getRGB());
        }
        Client.fontManager.verdana20.drawString(this.category.name(), this.x + 15, this.y +2,new Color(255, 255, 255).getRGB());
        RenderUtils.drawImage(new ResourceLocation("Worst/Clickui/"+this.category.name()+".png"), this.x + 4, this.y+5, 8, 8);

		


		GlStateManager.pushMatrix();
		GlStateManager.translate(this.x + 90 - 10, this.y + 5, 0.0f);
		GlStateManager.rotate((float) this.angel, 0.0f, 0.0f, -1.0f);
		GlStateManager.translate(-this.x + 90 - 10, -this.y + 5, 0.0f);
		GlStateManager.popMatrix();
		if (this.expand > 0) {
			this.buttons.forEach(b2 -> b2.render(mouseX, mouseY));
		}
		if (this.drag) {
			if (!Mouse.isButtonDown((int) 0)) {
				this.drag = false;
			}
			this.x = mouseX - this.dragX;
			this.y = mouseY - this.dragY;
			this.buttons.get((int) 0).y = this.y + 22 - this.scroll;
			for (Button b4 : this.buttons) {
				b4.x = this.x + 5;
			}
		}
	}

	public void key(char typedChar, int keyCode) {
		this.buttons.forEach(b2 -> b2.key(typedChar, keyCode));
	}

	public void mouseScroll(int mouseX, int mouseY, int amount) {
		if (mouseX > this.x - 2 && mouseX < this.x + 92 && mouseY > this.y - 2 && mouseY < this.y + 17 + this.expand) {
			this.scrollTo = (int) ((float) this.scrollTo - (float) (amount / 120 * 28));
		}
	}

	public void click(int mouseX, int mouseY, int button) {
		if (mouseX > this.x - 2 && mouseX < this.x + 92 && mouseY > this.y - 2 && mouseY < this.y + 17) {
			if (button == 1) {
				boolean bl = this.extended = !this.extended;
			}
			if (button == 0) {
				this.drag = true;
				this.dragX = mouseX - this.x;
				this.dragY = mouseY - this.y;
			}
		}
		if (this.extended) {
			this.buttons.stream().filter(b2 -> b2.y < this.y + this.expand)
					.forEach(b2 -> b2.click(mouseX, mouseY, button));
		}
	}
}
