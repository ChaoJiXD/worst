/*
 * Decompiled with CFR 0.136.
 */
package xyz.WorstClient.ui.Worst;

import java.awt.Color;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import xyz.WorstClient.Client;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.api.value.Value;
import xyz.WorstClient.management.ModuleManager;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.ui.fontRenderer.UnicodeFontRenderer;
import xyz.WorstClient.utils.RenderUtils;
import xyz.WorstClient.utils.render.RenderUtil;

import org.lwjgl.input.Mouse;


public class Clickui extends GuiScreen implements GuiYesNoCallback {
	public static ModuleType currentModuleType = ModuleType.Combat;//默认选中分类
	public static Module currentModule = ModuleManager.getModulesInType(currentModuleType).size() != 0? ModuleManager.getModulesInType(currentModuleType).get(0): null;//获取指定分类内功能列表
	public static float startX = 100, startY = 100;//起始位置
	public int moduleStart = 0;//功能起始计次
	public int valueStart = 0;//配置项起始计次
	boolean previousmouse = true;//上个移动
	boolean mouse;//鼠标移动
	public float moveX = 0, moveY = 0;//移动长度
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);

        
        //判断移动窗口区域
		if (isHovered(startX+60, startY-40, startX + 420, startY, mouseX, mouseY) && Mouse.isButtonDown(0)) {
			//判断移动
			if (moveX == 0 && moveY == 0) {
				//计算鼠标跟随位置
				moveX = mouseX - startX;
				moveY = mouseY - startY;
			} else {
				//计算鼠标跟随位置
				startX = mouseX - moveX;
				startY = mouseY - moveY;
			}
			this.previousmouse = true;
		} else if (moveX != 0 || moveY != 0) {
			//重置移动变量
			moveX = 0;
			moveY = 0;
		}
		//画窗体
		RenderUtil.drawRect(startX+60, startY-40, startX + 420, startY,new Color(255, 255, 255, 200).getRGB());
		RenderUtil.drawRect(startX + 60, startY, startX + 200, startY + 250,new Color(255, 255, 255, 255).getRGB());
		RenderUtil.drawRect(startX + 200, startY, startX + 420, startY + 250,new Color(235, 235, 235, 230).getRGB());
		
		//判断分类按钮被按下
		for (int i = 0; i < ModuleType.values().length; i++) {
			ModuleType[] iterator = ModuleType.values();
			//RenderUtil.drawRect(startX + 60 +i * 50, startY -40, startX + 100+i * 50, startY ,new Color(0, 5, 5, 230).getRGB());
			try {
				if (this.isCategoryHovered(startX + 60 +i * 50, startY -40, startX + 100+i * 50, startY, mouseX,
						mouseY) && Mouse.isButtonDown((int) 0)) {
					currentModuleType = iterator[i];
					currentModule = ModuleManager.getModulesInType(currentModuleType).size() != 0
							? ModuleManager.getModulesInType(currentModuleType).get(0)
							: null;
					moduleStart = 0;
				}
			} catch (Exception e) {
				System.err.println(e);
			}
		}
        //画图标
		RenderUtils.drawRect(startX + 60, startY -40,startX + 150, startY, new Color(185,185,185).getRGB());
		Client.fontManager.verdana20.drawString(currentModuleType.name(), startX + 92, startY -25,  new Color(255,255,255).getRGB());
        		if(currentModuleType==ModuleType.Combat) {
        			RenderUtil.drawIcon(startX + 60, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/Combat.png"),new Color(255,255,255).getRGB());
        			RenderUtil.drawIcon(startX + 150, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/Rander2.png"));
        			RenderUtil.drawIcon(startX + 190, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/Movement2.png"));
        			RenderUtil.drawIcon(startX + 230, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/Player2.png"));
        			RenderUtil.drawIcon(startX + 270, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/World2.png"));
        		}else if(currentModuleType==ModuleType.Render){
        			RenderUtil.drawIcon(startX + 60, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/Rander.png"),new Color(255,255,255).getRGB());
        			RenderUtil.drawIcon(startX + 150, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/Combat2.png"));
        			RenderUtil.drawIcon(startX + 190, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/Movement2.png"));
        			RenderUtil.drawIcon(startX + 230, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/Player2.png"));
        			RenderUtil.drawIcon(startX + 270, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/World2.png"));
        		}
        		else if(currentModuleType==ModuleType.Movement){
        			RenderUtil.drawIcon(startX + 60, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/Movement.png"),new Color(255,255,255).getRGB());
        			RenderUtil.drawIcon(startX + 150, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/Combat2.png"));
        			RenderUtil.drawIcon(startX + 190, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/Render2.png"));
        			RenderUtil.drawIcon(startX + 230, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/Player2.png"));
        			RenderUtil.drawIcon(startX + 270, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/World2.png"));
        		}        		
        		else if(currentModuleType==ModuleType.Player){
        			RenderUtil.drawIcon(startX + 60, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/Player.png"),new Color(255,255,255).getRGB());
        			RenderUtil.drawIcon(startX + 150, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/Combat2.png"));
        			RenderUtil.drawIcon(startX + 190, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/Render2.png"));
        			RenderUtil.drawIcon(startX + 230, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/Movement2.png"));
        			RenderUtil.drawIcon(startX + 270, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/World2.png"));
        		}
        		else if(currentModuleType==ModuleType.World){
        			RenderUtil.drawIcon(startX + 60, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/World.png"),new Color(255,255,255).getRGB());
        			RenderUtil.drawIcon(startX + 150, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/Combat2.png"));
        			RenderUtil.drawIcon(startX + 190, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/Render2.png"));
        			RenderUtil.drawIcon(startX + 230, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/Movement2.png"));
        			RenderUtil.drawIcon(startX + 270, startY -35, 32, 32, new ResourceLocation("Worst/Clickui/Player2.png"));
        		}
        		
		
		
		//判断选中功能是否为空
		if (currentModule != null) {
			float mY = startY;
			//列出功能
			for (int i = 0; i < ModuleManager.getModulesInType(currentModuleType).size(); i++) {
				Module module = ModuleManager.getModulesInType(currentModuleType).get(i);
				if (mY > startY + 225)
					break;
				if (i < moduleStart) {
					continue;
				}
				
				if (module.isEnabled()) {
					RenderUtils.drawRect(startX +60, mY , startX + 200, mY + 25, new Color(250, 250, 250, 200).getRGB());
				}
				Client.fontManager.comfortaa24.drawString(module.getName(), startX + 90, mY + 11,new Color(20,20,20,255).getRGB());

				if (isSettingsButtonHovered(startX + 90, mY,
						startX + 100 + (Client.fontManager.comfortaa24.getStringWidth(module.getName())),
						mY + 12 + Client.fontManager.comfortaa24.getStringHeight("1"), mouseX, mouseY)) {
					if (!this.previousmouse && Mouse.isButtonDown((int) 0)) {
						if (module.isEnabled()) {
							module.setEnabled(false);
						} else {
							module.setEnabled(true);
						}
						previousmouse = true;
					}
					if (!this.previousmouse && Mouse.isButtonDown((int) 1)) {
						previousmouse = true;
					}
				}

				if (!Mouse.isButtonDown((int) 0)) {
					this.previousmouse = false;
				}
				if (isSettingsButtonHovered(startX + 90, mY,
						startX + 100 + (Client.fontManager.comfortaa20.getStringWidth(module.getName())),
						mY + 12 + Client.fontManager.comfortaa20.getStringHeight("1"), mouseX, mouseY) && Mouse.isButtonDown((int) 1)) {
					currentModule = module;
					valueStart = 0;
				}
				mY += 25;
			}
			
			//列出配置选项
			mY = startY + 30;
			for (int i = 0; i < currentModule.getValues().size(); i++) {
				if (mY > startY + 230)
					break;
				if (i < valueStart) {
					continue;
				}
				UnicodeFontRenderer font = Client.fontManager.comfortaa18;
				//进度条
				Value value = currentModule.getValues().get(i);
				if (value instanceof Numbers) {
					float x = startX + 300;
					double render = (double) (68.0F
							* (((Number) value.getValue()).floatValue() - ((Numbers) value).getMinimum().floatValue())
							/ (((Numbers) value).getMaximum().floatValue()
									- ((Numbers) value).getMinimum().floatValue()));
					RenderUtil.drawRect((float) x - 6, mY + 2, (float) ((double) x + 75), mY + 3,
							(new Color(200, 200, 200, (int) 255)).getRGB());
					RenderUtil.drawRect((float) x - 6, mY + 2, (float) ((double) x + render + 6.5D), mY + 3,
							(new Color(61, 141, 255, (int) 255)).getRGB());
					font.drawString(value.getName() + ": " + value.getValue(), startX + 210, mY, new Color(0,0,0).getRGB());
					if (!Mouse.isButtonDown((int) 0)) {
						this.previousmouse = false;
					}
					if (this.isButtonHovered(x, mY - 2, x + 100, mY + 7, mouseX, mouseY)
							&& Mouse.isButtonDown((int) 0)) {
						if (!this.previousmouse && Mouse.isButtonDown((int) 0)) {
							render = ((Numbers) value).getMinimum().doubleValue();
							double max = ((Numbers) value).getMaximum().doubleValue();
							double inc = ((Numbers) value).getIncrement().doubleValue();
							double valAbs = (double) mouseX - ((double) x + 1.0D);
							double perc = valAbs / 68.0D;
							perc = Math.min(Math.max(0.0D, perc), 1.0D);
							double valRel = (max - render) * perc;
							double val = render + valRel;
							val = (double) Math.round(val * (1.0D / inc)) / (1.0D / inc);
							((Numbers) value).setValue(Double.valueOf(val));
						}
						if (!Mouse.isButtonDown((int) 0)) {
							this.previousmouse = false;
						}
					}
					mY += 20;
				}
				//开关功能
				if (value instanceof Option) {
					float x = startX + 300;
					font.drawString(value.getName(), startX + 210, mY,new Color(20,20,20).getRGB());
					if ((boolean) value.getValue()) {
						RenderUtil.drawBorderedRect(x + 56, mY-1 , x + 76, mY + 9, 3, new Color(220, 220, 255).getRGB(), new Color(220, 220, 255).getRGB());
						RenderUtils.circle(x+72, mY+4, 3, new Color(150, 150, 255).getRGB());
					} else {
						RenderUtil.drawBorderedRect(x + 56, mY-1 , x + 76, mY + 9,3,
								new Color(200, 200, 200, (int) 255).getRGB(),new Color(200, 200, 200,255).getRGB());
						RenderUtils.circle(x+60, mY+4, 3, new Color(150, 150, 150).getRGB());
					}
					if (this.isCheckBoxHovered(x + 56, mY, x + 76, mY + 9, mouseX, mouseY)) {
						if (!this.previousmouse && Mouse.isButtonDown((int) 0)) {
							this.previousmouse = true;
							this.mouse = true;
						}

						if (this.mouse) {
							value.setValue(!(boolean) value.getValue());
							this.mouse = false;
						}
					}
					if (!Mouse.isButtonDown((int) 0)) {
						this.previousmouse = false;
					}
					mY += 20;
				}
				//模式
				if (value instanceof Mode) {
					float x = startX + 300;
					font.drawString(value.getName(), startX + 210, mY, new Color(20,20,20).getRGB());

					RenderUtil.drawBorderedRect(x - 5, mY - 5, x + 90, mY + 15,2,
							new Color(120, 120, 120, (int) 255).getRGB(), new Color(120, 120, 120, (int) 255).getRGB() );
					font.drawString(((Mode) value).getModeAsString(),(float) (x + 40 - font.getStringWidth(((Mode) value).getModeAsString()) / 2), mY,new Color(20,20,20).getRGB());
					if (this.isStringHovered(x, mY - 5, x + 100, mY + 15, mouseX, mouseY)) {
						if (Mouse.isButtonDown((int) 0) && !this.previousmouse) {
							Enum current = (Enum) ((Mode) value).getValue();
							int next = current.ordinal() + 1 >= ((Mode) value).getModes().length ? 0
									: current.ordinal() + 1;
							value.setValue(((Mode) value).getModes()[next]);
							this.previousmouse = true;
						}
						if (!Mouse.isButtonDown((int) 0)) {
							this.previousmouse = false;
						}

					}
					mY += 25;
				}
			}
		}

	}

	public boolean isStringHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
		if (mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isSettingsButtonHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
		if (mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isButtonHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
		if (mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isCheckBoxHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
		if (mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isCategoryHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
		if (mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
		if (mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}


}
