package xyz.WorstClient.module.modules.render;

import xyz.WorstClient.Client;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.rendering.EventRender2D;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.module.modules.combat.Killaura;
import xyz.WorstClient.ui.fontRenderer.UnicodeFontRenderer;
import xyz.WorstClient.utils.R3DUtil;
import xyz.WorstClient.utils.RenderUtils;
import xyz.WorstClient.utils.render.Colors;
import xyz.WorstClient.utils.render.RenderUtil;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.ResourceLocation;

public class TargetHUD
		extends xyz.WorstClient.module.Module {
	private Mode<Enum> mode = new Mode("Mode", "mode", (Enum[])rendermode.values(), (Enum)rendermode.Exhibition);
	public static Numbers<Double> X = new Numbers<Double>("X", "X", 200.0, 0.0, 1920.0, 10.0);
	public static Numbers<Double> Y = new Numbers<Double>("Y", "Y", 300.0, 0.0, 1080.0, 10.0);
	float width2 = 0;

	public TargetHUD() {
		super("TargetHUD", new String[] { "targethud" }, ModuleType.Render);
		this.addValues(this.mode,this.X,this.Y);
	}

	@EventHandler
	private void onUpdate(EventPreUpdate e10) {
		this.setSuffix(this.mode.getValue());
	}

	@EventHandler
	public void onRender(EventRender2D event) {
		if(this.mode.getValue() == rendermode.Debug){
			ScaledResolution sr = new ScaledResolution(mc);
			final FontRenderer font2 = mc.fontRendererObj;
			if (Killaura.curTarget != null && Client.instance.getModuleManager().getModuleByClass(TargetHUD.class).isEnabled()
					& Client.instance.getModuleManager().getModuleByClass(Killaura.class).isEnabled()) {
				final String name = Killaura.curTarget.getName();
				font2.drawStringWithShadow(name, (float) (sr.getScaledWidth() / 2) - (font2.getStringWidth(name) / 2),
						(float) (sr.getScaledHeight() / 2 - 30), -1);
				Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));
				int i = 0;
				while ((float) i < Killaura.curTarget.getMaxHealth() / 2.0f) {
					Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect((float) (sr.getScaledWidth() / 2)
									- Killaura.curTarget.getMaxHealth() / 2.0f * 10.0f / 2.0f + (float) (i * 10),
							(float) (sr.getScaledHeight() / 2 - 16), 16, 0, 9, 9);
					++i;
				}
				i = 0;
				while ((float) i < Killaura.curTarget.getHealth() / 2.0f) {
					Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect((float) (sr.getScaledWidth() / 2)
									- Killaura.curTarget.getMaxHealth() / 2.0f * 10.0f / 2.0f + (float) (i * 10),
							(float) (sr.getScaledHeight() / 2 - 16), 52, 0, 9, 9);
					++i;
				}
			}

		}
	}

	public static int[] getFractionIndicies(final float[] fractions, final float progress) {
		final int[] range = new int[2];
		int startPoint;
		for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {}
		if (startPoint >= fractions.length) {
			startPoint = fractions.length - 1;
		}
		range[0] = startPoint - 1;
		range[1] = startPoint;
		return range;
	}

	public static Color blendColors(final float[] fractions, final Color[] colors, final float progress) {
		Color color = null;
		if (fractions == null) {
			throw new IllegalArgumentException("Fractions can't be null");
		}
		if (colors == null) {
			throw new IllegalArgumentException("Colours can't be null");
		}
		if (fractions.length == colors.length) {
			final int[] indicies = getFractionIndicies(fractions, progress);
			final float[] range = { fractions[indicies[0]], fractions[indicies[1]] };
			final Color[] colorRange = { colors[indicies[0]], colors[indicies[1]] };
			final float max = range[1] - range[0];
			final float value = progress - range[0];
			final float weight = value / max;
			color = blend(colorRange[0], colorRange[1], 1.0f - weight);
			return color;
		}
		throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
	}

	public static Color blend(final Color color1, final Color color2, final double ratio) {
		final float r = (float)ratio;
		final float ir = 1.0f - r;
		final float[] rgb1 = new float[3];
		final float[] rgb2 = new float[3];
		color1.getColorComponents(rgb1);
		color2.getColorComponents(rgb2);
		float red = rgb1[0] * r + rgb2[0] * ir;
		float green = rgb1[1] * r + rgb2[1] * ir;
		float blue = rgb1[2] * r + rgb2[2] * ir;
		if (red < 0.0f) {
			red = 0.0f;
		} else if (red > 255.0f) {
			red = 255.0f;
		}
		if (green < 0.0f) {
			green = 0.0f;
		} else if (green > 255.0f) {
			green = 255.0f;
		}
		if (blue < 0.0f) {
			blue = 0.0f;
		} else if (blue > 255.0f) {
			blue = 255.0f;
		}
		Color color3 = null;
		try {
			color3 = new Color(red, green, blue);
		} catch (IllegalArgumentException exp) {
			final NumberFormat nf = NumberFormat.getNumberInstance();
			System.out.println(nf.format((double)red) + "; " + nf.format((double)green) + "; " + nf.format((double)blue));
			exp.printStackTrace();
		}
		return color3;
	}

	@EventHandler
	public void onScreenDraw(EventRender2D er) {
		if (this.mode.getValue() == rendermode.ChaoJi){
			if(Killaura.curTarget != null) {
				float health = Killaura.curTarget.getHealth()/Killaura.curTarget.getMaxHealth();
				if(width2<90*health) {
					if(90*health-width2<1) {
						width2 = 90*health;
					}
					width2++;
				}
				if(90*health-width2>1) {
					width2 = 90*health;
				}
				width2--;


				int x = X.getValue().intValue();
				int y = Y.getValue().intValue();
				final EntityLivingBase player = (EntityLivingBase) Killaura.curTarget;
				if (Killaura.curTarget != null) {
					GlStateManager.pushMatrix();
					//RenderUtils.drawImage(new ResourceLocation("/client/TargetHUD.png"), x+51, y-52, 208, 75);
					//Gui.drawRect(x+60, y+15, x+250.0, y-45, Colors.getColor(0, 150));
					Color customrainbow = new Color(Color.HSBtoRGB((float)((double)this.mc.thePlayer.ticksExisted /80.0 + Math.sin((double)1 / 80.0 * 1.6)) % 1.0f, 1, 1.0f));
					RenderUtil.drawGradientSideways(x+65, y+6, x+65+width2, y+10, new Color(201,169,252).getRGB(),  new Color(181,220,255).getRGB());
					UnicodeFontRenderer font = Client.fontManager.verdana16;
					NumberFormat nf = NumberFormat.getInstance();
					nf.setMaximumFractionDigits(2);
                /*Client.fontManager.getFont("comfortaa.ttf", 11).drawString((Killaura.curTarget.onGround ? "On":"Off")+" Ground"+" | Distance"+Killaura.curTarget.getDistanceToEntity(player)+" | Hurt"+Killaura.curTarget.hurtTime, x+100, y-30, Colors.getColor(255, 255,255));
				Client.fontManager.getFont("comfortaa.ttf", 11).drawString("dmg out: "+Killaura.curTarget.getHealth(), x+100, y-22, Colors.getColor(255, 255,255));
				Client.fontManager.getFont("comfortaa.ttf", 11).drawString("dmg in: 1", x+100, y-14, Colors.getColor(255, 255,255));*/
					Client.fontManager.getFont("comfortaa.ttf", 22).drawStringWithShadow(player.getName()+ " at " + String.valueOf(Math.floor(Killaura.curTarget.getHealth())), x+65, y-10, -1);

					GlStateManager.scale(0.5, 0.5, 0.5);
					GlStateManager.scale(2.0f, 2.0f, 2.0f);
					GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
					GlStateManager.enableAlpha();
					GlStateManager.enableBlend();
					GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
					//R3DUtil.drawEntityOnScreen(x+75,y+5, 20, 0.0F, 9.0F,Killaura.curTarget);
					GlStateManager.popMatrix();
				}
			}
		}
		if(this.mode.getValue() == rendermode.Exhibition){
			ScaledResolution res = new ScaledResolution(this.mc);
			int x = res.getScaledWidth() /2 + 10;
			int y = res.getScaledHeight() - 90;
			final EntityLivingBase player = (EntityLivingBase) Killaura.curTarget;
			if (player != null) {

				GlStateManager.pushMatrix();
				Gui.drawRect(x+0.0, y+0.0, x+113.0, y+36.0, Colors.getColor(0, 150));

				mc.fontRendererObj.drawStringWithShadow(player.getName(), x+38.0f, y+2.0f, -1);

				BigDecimal bigDecimal = new BigDecimal((double)player.getHealth());
				bigDecimal = bigDecimal.setScale(1, RoundingMode.HALF_UP);
				double HEALTH = bigDecimal.doubleValue();

				BigDecimal DT = new BigDecimal((double)mc.thePlayer.getDistanceToEntity(player));
				DT = DT.setScale(1, RoundingMode.HALF_UP);
				double Dis = DT.doubleValue();

				final float health = player.getHealth();
				final float[] fractions = { 0.0f, 0.5f, 1.0f };
				final Color[] colors = { Color.RED, Color.YELLOW, Color.GREEN };
				final float progress = health / player.getMaxHealth();
				final Color customColor = (health >= 0.0f) ? blendColors(fractions, colors, progress).brighter() : Color.RED;
				double width = (double)mc.fontRendererObj.getStringWidth(player.getName());
				width = getIncremental(width, 10.0);
				if (width < 50.0) {
					width = 50.0;
				}
				final double healthLocation = width * progress;
				Gui.drawRect(x+37.5, y+11.5, x+38.0 + healthLocation + 0.5, y+14.5, customColor.getRGB());
				RenderUtil.rectangleBordered(x+37.0, y+11.0, x+39.0 + width, y+15.0, 0.5, Colors.getColor(0, 0), Colors.getColor(0));
				for (int i = 1; i < 10; ++i) {
					final double dThing = width / 10.0 * i;
					Gui.drawRect(x+38.0 + dThing, y+11.0, x+38.0 + dThing + 0.5, y+15.0, Colors.getColor(0));
				}
				String COLOR1;
				if (health > 20.0D) {
					COLOR1 = " \2479";
				} else if (health >= 10.0D) {
					COLOR1 = " \247a";
				} else if (health >= 3.0D) {
					COLOR1 = " \247e";
				} else {
					COLOR1 = " \2474";
				}
				RenderUtil.rectangleBordered(x+1.0, y+1.0, x+35.0, y+35.0, 0.5, Colors.getColor(0, 0), Colors.getColor(255));

				GlStateManager.scale(0.5, 0.5, 0.5);
				final String str = "HP: "+ HEALTH + " Dist: " + Dis;
				mc.fontRendererObj.drawStringWithShadow(str, x*2+76.0f, y*2+35.0f, -1);
				final String str2 = String.format("Yaw: %s Pitch: %s ", (int)player.rotationYaw, (int)player.rotationPitch);
				mc.fontRendererObj.drawStringWithShadow(str2, x*2+76.0f, y*2+47.0f, -1);
				final String str3 = String.format("G: %s HURT: %s TE: %s", player.onGround, player.hurtTime, player.ticksExisted);
				mc.fontRendererObj.drawStringWithShadow(str3, x*2+76.0f, y*2+59.0f, -1);

				GlStateManager.scale(2.0f, 2.0f, 2.0f);
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				GlStateManager.enableAlpha();
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

				if(player instanceof EntityPlayer) {
					final List var5 = GuiPlayerTabOverlay.field_175252_a.sortedCopy((Iterable)mc.thePlayer.sendQueue.getPlayerInfoMap());
					for (final Object aVar5 : var5) {
						final NetworkPlayerInfo var6 = (NetworkPlayerInfo)aVar5;
						if (mc.theWorld.getPlayerEntityByUUID(var6.getGameProfile().getId()) == player) {
							mc.getTextureManager().bindTexture(var6.getLocationSkin());
							Gui.drawScaledCustomSizeModalRect(x+2, y+2, 8.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f);
							if (((EntityPlayer)player).isWearing(EnumPlayerModelParts.HAT)) {
								Gui.drawScaledCustomSizeModalRect(x+2, y+2, 40.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f);
							}
							GlStateManager.bindTexture(0);
							break;
						}
					}

				}

				GlStateManager.popMatrix();
			}
		}
	}

	private  double getIncremental(final double val, final double inc) {
		final double one = 1.0 / inc;
		return Math.round(val * one) / one;
	}






}

enum rendermode {
	Debug,
	Exhibition,
	ChaoJi;
}



