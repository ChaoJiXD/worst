package net.minecraft.client.gui;


import com.google.common.collect.Lists;


import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.realm.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import viamcp.gui.GuiProtocolSelector;
import xyz.WorstClient.Client;
import xyz.WorstClient.jello.main.particles.ParticleEngine;
import xyz.WorstClient.ui.login.GuiAltManager;
import xyz.WorstClient.utils.RenderUtils;
import xyz.WorstClient.utils.render.RenderUtil;

import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback
{
    private static final AtomicInteger field_175373_f = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();
    private static final Random RANDOM = new Random();
    public static int animation;
    public int moveother;
    private int anim1 = 0;
    private int anim2 = 0;
    private int anim3 = 0;
    private int anim4 = 0;
    private int anim5 = 0;
    private int anim6 = 0;
    public static float animatedMouseX;
    public static float animatedMouseY;
    private int movemain = 120;
    /** Counts the number of screen updates. */
    private float updateCounter;
    public ParticleEngine pe = new ParticleEngine();

    /** The splash message. */
    private String splashText;
    private GuiButton buttonResetDemo;

    /** Timer used to rotate the panorama, increases every tick. */
    private int panoramaTimer;

    /**
     * Texture allocated for the current viewport of the main menu's panorama background.
     */
    private DynamicTexture viewportTexture;
    private boolean field_175375_v = true;

    /**
     * The Object object utilized as a thread lock when performing non thread-safe operations
     */
    private final Object threadLock = new Object();

    /** OpenGL graphics card warning. */
    private String openGLWarning1;

    /** OpenGL graphics card warning. */
    private String openGLWarning2;

    /** Link to the Mojang Support about minimum requirements */
    private String openGLWarningLink;
    private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
    private static final ResourceLocation minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");

    /** An array of all the paths to the panorama pictures. */
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
    public static final String field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
    private int field_92024_r;
    private int field_92023_s;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    private ResourceLocation backgroundTexture;

    /** Minecraft Realms button. */
    private GuiButton realmsButton;

    public GuiMainMenu()
    {
        this.openGLWarning2 = field_96138_a;
        this.splashText = "missingno";
        BufferedReader bufferedreader = null;

        try
        {
            List<String> list = Lists.<String>newArrayList();
            bufferedreader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));
            String s;

            while ((s = bufferedreader.readLine()) != null)
            {
                s = s.trim();

                if (!s.isEmpty())
                {
                    list.add(s);
                }
            }

            if (!list.isEmpty())
            {
                while (true)
                {
                    this.splashText = (String)list.get(RANDOM.nextInt(list.size()));

                    if (this.splashText.hashCode() != 125780783)
                    {
                        break;
                    }
                }
            }
        }
        catch (IOException var12)
        {
            ;
        }
        finally
        {
            if (bufferedreader != null)
            {
                try
                {
                    bufferedreader.close();
                }
                catch (IOException var11)
                {
                    ;
                }
            }
        }

        this.updateCounter = RANDOM.nextFloat();
        this.openGLWarning1 = "";

        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported())
        {
            this.openGLWarning1 = I18n.format("title.oldgl1", new Object[0]);
            this.openGLWarning2 = I18n.format("title.oldgl2", new Object[0]);
            this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        ++this.panoramaTimer;
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
    	pe.particles.clear();
        this.viewportTexture = new DynamicTexture(256, 256);
        this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24)
        {
            this.splashText = "Merry X-mas!";
        }
        else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1)
        {
            this.splashText = "Happy new year!";
        }
        else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31)
        {
            this.splashText = "OOoooOOOoooo! Spooky!";
        }

        int i = 24;
        int j = this.height / 4 + 48;

        if (this.mc.isDemo())
        {
            this.addDemoButtons(j, 24);
        }
        else
        {
            this.addSingleplayerMultiplayerButtons(j, 24);
        }

        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 72 + 12, 98, 20, I18n.format("menu.options", new Object[0])));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 2, j + 72 + 12, 98, 20, I18n.format("menu.quit", new Object[0])));
        this.buttonList.add(new GuiButtonLanguage(5, this.width / 2 - 124, j + 72 + 12));

        synchronized (this.threadLock)
        {
            this.field_92023_s = this.fontRendererObj.getStringWidth(this.openGLWarning1);
            this.field_92024_r = this.fontRendererObj.getStringWidth(this.openGLWarning2);
            int k = Math.max(this.field_92023_s, this.field_92024_r);
            this.field_92022_t = (this.width - k) / 2;
            this.field_92021_u = ((GuiButton)this.buttonList.get(0)).yPosition - 24;
            this.field_92020_v = this.field_92022_t + k;
            this.field_92019_w = this.field_92021_u + 24;
        }

        this.mc.func_181537_a(false);
    }

    /**
     * Adds Singleplayer and Multiplayer buttons on Main Menu for players who have bought the game.
     */
    private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_)
    {
        this.buttonList.add(new GuiButton(69, 5, 5, 90, 20, "Version"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer", new Object[0])));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 1, I18n.format("menu.multiplayer", new Object[0])));
        this.buttonList.add(this.realmsButton = new GuiButton(14, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, "Alt Login"));
    }

    /**
     * Adds Demo buttons on Main Menu for players who are playing Demo.
     */
    private void addDemoButtons(int p_73972_1_, int p_73972_2_)
    {
        this.buttonList.add(new GuiButton(11, this.width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo", new Object[0])));
        this.buttonList.add(this.buttonResetDemo = new GuiButton(12, this.width / 2 - 100, p_73972_1_ + p_73972_2_ * 1, I18n.format("menu.resetdemo", new Object[0])));
        ISaveFormat isaveformat = this.mc.getSaveLoader();
        WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

        if (worldinfo == null)
        {
            this.buttonResetDemo.enabled = false;
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 69)
        {
            this.mc.displayGuiScreen(new GuiProtocolSelector(this));
        }

        if (button.id == 11)
        {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
        }

        if (button.id == 12)
        {
            ISaveFormat isaveformat = this.mc.getSaveLoader();
            WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

            if (worldinfo != null)
            {
                GuiYesNo guiyesno = GuiSelectWorld.func_152129_a(this, worldinfo.getWorldName(), 12);
                this.mc.displayGuiScreen(guiyesno);
            }
        }
    }

    private void switchToRealms()
    {
        RealmsBridge realmsbridge = new RealmsBridge();
        realmsbridge.switchToRealms(this);
    }

    public void confirmClicked(boolean result, int id)
    {
        if (result && id == 12)
        {
            ISaveFormat isaveformat = this.mc.getSaveLoader();
            isaveformat.flushCache();
            isaveformat.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen(this);
        }
        else if (id == 13)
        {
            if (result)
            {
                try
                {
                    Class<?> oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
                    oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {new URI(this.openGLWarningLink)});
                }
                catch (Throwable throwable)
                {
                    logger.error("Couldn\'t open link", throwable);
                }
            }

            this.mc.displayGuiScreen(this);
        }
    }

    /**
     * Draws the main menu panorama
     */
    private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        int i = 8;

        for (int j = 0; j < i * i; ++j)
        {
            GlStateManager.pushMatrix();
            float f = ((float)(j % i) / (float)i - 0.5F) / 64.0F;
            float f1 = ((float)(j / i) / (float)i - 0.5F) / 64.0F;
            float f2 = 0.0F;
            GlStateManager.translate(f, f1, f2);
            GlStateManager.rotate(MathHelper.sin(((float)this.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-((float)this.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int k = 0; k < 6; ++k)
            {
                GlStateManager.pushMatrix();

                if (k == 1)
                {
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 2)
                {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 3)
                {
                    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 4)
                {
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (k == 5)
                {
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                this.mc.getTextureManager().bindTexture(titlePanoramaPaths[k]);
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                int l = 255 / (j + 1);
                float f3 = 0.0F;
                worldrenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, l).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }

        worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    /**
     * Rotate and blurs the skybox view in the main menu
     */
    private void rotateAndBlurSkybox(float p_73968_1_)
    {
        this.mc.getTextureManager().bindTexture(this.backgroundTexture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.disableAlpha();
        int i = 3;

        for (int j = 0; j < i; ++j)
        {
            float f = 1.0F / (float)(j + 1);
            int k = this.width;
            int l = this.height;
            float f1 = (float)(j - i / 2) / 256.0F;
            worldrenderer.pos((double)k, (double)l, (double)this.zLevel).tex((double)(0.0F + f1), 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos((double)k, 0.0D, (double)this.zLevel).tex((double)(1.0F + f1), 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, 0.0D, (double)this.zLevel).tex((double)(1.0F + f1), 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, (double)l, (double)this.zLevel).tex((double)(0.0F + f1), 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }

    /**
     * Renders the skybox in the main menu
     */
    private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_)
    {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        float f = this.width > this.height ? 120.0F / (float)this.width : 120.0F / (float)this.height;
        float f1 = (float)this.height * f / 256.0F;
        float f2 = (float)this.width * f / 256.0F;
        int i = this.width;
        int j = this.height;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0D, (double)j, (double)this.zLevel).tex((double)(0.5F - f1), (double)(0.5F + f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos((double)i, (double)j, (double)this.zLevel).tex((double)(0.5F - f1), (double)(0.5F - f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos((double)i, 0.0D, (double)this.zLevel).tex((double)(0.5F + f1), (double)(0.5F - f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(0.0D, 0.0D, (double)this.zLevel).tex((double)(0.5F + f1), (double)(0.5F + f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();
    }


    
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        boolean isOverSingleplayer = mouseX > sr.getScaledWidth()/10 && mouseX < sr.getScaledWidth()/10+32 && mouseY < sr.getScaledHeight() && mouseY > sr.getScaledHeight()-40;
        boolean isOverMultiplayer = mouseX > sr.getScaledWidth()/10+1*(sr.getScaledWidth()/8) && mouseX < sr.getScaledWidth()/10+1*(sr.getScaledWidth()/8)+32 &&mouseY < sr.getScaledHeight() && mouseY > sr.getScaledHeight()-40;
        boolean isOverAltManager = mouseX > sr.getScaledWidth()/10+2*(sr.getScaledWidth()/8) && mouseX < sr.getScaledWidth()/10+2*(sr.getScaledWidth()/8)+32&&mouseY < sr.getScaledHeight() && mouseY > sr.getScaledHeight()-40;
        boolean isOverSettings = mouseX > sr.getScaledWidth()/10+3*(sr.getScaledWidth()/8) && mouseX < sr.getScaledWidth()/10+3*(sr.getScaledWidth()/8)+32&& mouseY < sr.getScaledHeight() && mouseY > sr.getScaledHeight()-40;
        boolean isOverExit = mouseX > sr.getScaledWidth()/10+4*(sr.getScaledWidth()/8) && mouseX < sr.getScaledWidth()/10+4*(sr.getScaledWidth()/8)+32&& mouseY < sr.getScaledHeight() && mouseY > sr.getScaledHeight()-40;
        animatedMouseX += ((mouseX-animatedMouseX) / 1.8) + 0.1;
        animatedMouseY += ((mouseY-animatedMouseY) / 1.8) + 0.1;
    	int size = 30;
        GlStateManager.pushMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        this.mc.getTextureManager().bindTexture(new ResourceLocation("MainMenu/backGround.png"));
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawModalRectWithCustomSizedTexture((int)(-1177/2 - 372 - animatedMouseX + sr.getScaledWidth()),(int)( -34/2 +8 - animatedMouseY/9.5f + sr.getScaledHeight()/19 - 19), 0, 0, 3841/2, 1194/2, 3841/2, 1194/2);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Gui.drawRect(0, sr.getScaledHeight(), sr.getScaledWidth(), sr.getScaledHeight()-50, new Color(235,235,235,190).getRGB());

        Minecraft.fontRendererObj.drawString("worst reborn by Sunrise-team.", 5, 5, new Color(255, 255, 255).getRGB());
        RenderUtil.drawIcon(sr.getScaledWidth()/10,sr.getScaledHeight()-44,size+2, size+2,new ResourceLocation("MainMenu/single.png"), isOverSingleplayer ? new Color(255,255,255,255).getRGB():new Color(230,230,230,255).getRGB());
        Client.fontManager.verdana20.drawString("Single", sr.getScaledWidth()/10,sr.getScaledHeight()-14,isOverSingleplayer ? new Color(255,255,255,255).getRGB():new Color(130,130,130,255).getRGB());
        RenderUtil.drawIcon(sr.getScaledWidth()/10+sr.getScaledWidth()/8, sr.getScaledHeight()-44, size+2, size+2, new ResourceLocation("MainMenu/multi.png"),isOverMultiplayer ?new Color(255,255,255,255).getRGB():new Color(230,230,230,255).getRGB());
        Client.fontManager.verdana20.drawString(" Multi", sr.getScaledWidth()/10+sr.getScaledWidth()/8, sr.getScaledHeight()-14,isOverMultiplayer ? new Color(255,255,255,255).getRGB():new Color(130,130,130,255).getRGB());
        RenderUtil.drawIcon(sr.getScaledWidth()/10+2*(sr.getScaledWidth()/8), sr.getScaledHeight()-44, size+2, size+2, new ResourceLocation("MainMenu/alt.png"),isOverAltManager ? new Color(255,255,255,255).getRGB():new Color(230,230,230,255).getRGB());
        Client.fontManager.verdana20.drawString("Alt Manager", sr.getScaledWidth()/10+2*(sr.getScaledWidth()/8)-12, sr.getScaledHeight()-14,isOverAltManager ? new Color(255,255,255,255).getRGB():new Color(130,130,130,255).getRGB());
        RenderUtil.drawIcon(sr.getScaledWidth()/10+3*(sr.getScaledWidth()/8), sr.getScaledHeight()-44, size+2, size+2, new ResourceLocation("MainMenu/option.png"),isOverSettings ? new Color(255,255,255,255).getRGB():new Color(230,230,230,255).getRGB());
        Client.fontManager.verdana20.drawString("Options",  sr.getScaledWidth()/10+3*(sr.getScaledWidth()/8)-2,sr.getScaledHeight()-14,isOverSettings ? new Color(255,255,255,255).getRGB():new Color(130,130,130,255).getRGB());
        RenderUtil.drawIcon(sr.getScaledWidth()/10+4*(sr.getScaledWidth()/8), sr.getScaledHeight()-44, size+2, size+2, new ResourceLocation("MainMenu/quit.png"),isOverExit ? new Color(255,255,255,255).getRGB():new Color(230,230,230,255).getRGB());
        Client.fontManager.verdana20.drawString(" Exit", sr.getScaledWidth()/10+4*(sr.getScaledWidth()/8), sr.getScaledHeight()-14,isOverExit ? new Color(255,255,255,255).getRGB():new Color(130,130,130,255).getRGB());
        GlStateManager.popMatrix();
        if(mouseX >= 0 && mouseX < 119){
        	movemainout();
        	if(movemain <0){
        		movemain = 0;
        	}
    	}
        else{
        		movemainin();
        		if(movemain >210){
            		movemain = 210;
            	}
        }
    	pe.render(animatedMouseX, animatedMouseY);
    }
    void movemainin(){
    	movemain = movemain + 30;
    }
    void movemainout(){
    	movemain = movemain - 30;
    }
    public Boolean top1,left1,left2,right1,right2,down1,middle = false;
    public  void drawnumber(int x,int y,int number,int color,int size) {
        
        int background = new Color(0, 0, 0, 150).getRGB();
        Rect(x,y + 2,20,size,background);
        Rect(x + 2,y + 2,size,20,background);
        Rect(x + 2,y + 20 + 2,size,20,background);
        Rect(x + 20 + 2,y + 2,size,20,background);
        Rect(x + 20 + 2,y + 20 + 2,size,22,background);
        Rect(x + 2,y + 20 + 2,20,size,background);
        Rect(x + 2,y + 40 + 2,20,size,background);

        switch (number) {
            case 0:
                this.top1 = true;
                this.left1 = true;
                this.left2 = true;
                this.right1 = true;
                this.right2 = true;
                this.middle = false;
                this.down1 = true;
                break;
            case 1:
                this.top1 = false;
                this.left1 = false;
                this.left2 = false;
                this.right1 = true;
                this.right2 = true;
                this.middle = false;
                this.down1 = false;
                break;
            case 2:
                this.top1 = true;
                this.left1 = false;
                this.left2 = true;
                this.right1 = true;
                this.right2 = false;
                this.middle = true;
                this.down1 = true;
                break;
            case 3:
                this.top1 = true;
                this.left1 = false;
                this.left2 = false;
                this.right1 = true;
                this.right2 = true;
                this.middle = true;
                this.down1 = true;
                break;
            case 4:
                this.top1 = false;
                this.left1 = true;
                this.left2 = false;
                this.right1 = true;
                this.right2 = true;
                this.middle = true;
                this.down1 = false;
                break;
            case 5:
                this.top1 = true;
                this.left1 = true;
                this.left2 = false;
                this.right1 = false;
                this.right2 = true;
                this.middle = true;
                this.down1 = true;
                break;
            case 6:
                this.top1 = true;
                this.left1 = true;
                this.left2 = true;
                this.right1 = false;
                this.right2 = true;
                this.middle = true;
                this.down1 = true;
                break;
            case 7:
                this.top1 = true;
                this.left1 = false;
                this.left2 = false;
                this.right1 = true;
                this.right2 = true;
                this.middle = false;
                this.down1 = false;
                break;
            case 8:
                this.top1 = true;
                this.left1 = true;
                this.left2 = true;
                this.right1 = true;
                this.right2 = true;
                this.middle = true;
                this.down1 = true;
                break;
            case 9:
                this.top1 = true;
                this.left1 = true;
                this.left2 = false;
                this.right1 = true;
                this.right2 = true;
                this.middle = true;
                this.down1 = true;
                break;
        }

        
        if(this.top1) {
            Rect(x,y,20,size,color);
        }
        if(this.left1){
            Rect(x,y,size,20,color);
        }
        if(this.left2){
            Rect(x,y + 20,size,20,color);
        }
        if(this.right1){
            Rect(x + 20,y,size,20,color);
        }
        if(this.right2){
            Rect(x + 20,y + 20,size,20 +size,color);
        }
        if(this.middle) {
            Rect(x,y + 20,20 + size,size,color);
        }
        if(this.down1){
            Rect(x,y + 40,20,size,color);
        }
    }

    public static void Rect(double x, double y, double w, double h, int color) {
        Gui.drawRect(x, y, w + x, h + y, color);
    }
    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        boolean isOverSingleplayer = mouseX > sr.getScaledWidth()/10 && mouseX < sr.getScaledWidth()/10+32 && mouseY < sr.getScaledHeight() && mouseY > sr.getScaledHeight()-40;
        boolean isOverMultiplayer = mouseX > sr.getScaledWidth()/10+1*(sr.getScaledWidth()/8) && mouseX < sr.getScaledWidth()/10+1*(sr.getScaledWidth()/8)+32 &&mouseY < sr.getScaledHeight() && mouseY > sr.getScaledHeight()-40;
        boolean isOverAltManager = mouseX > sr.getScaledWidth()/10+2*(sr.getScaledWidth()/8) && mouseX < sr.getScaledWidth()/10+2*(sr.getScaledWidth()/8)+32&&mouseY < sr.getScaledHeight() && mouseY > sr.getScaledHeight()-40;
        boolean isOverSettings = mouseX > sr.getScaledWidth()/10+3*(sr.getScaledWidth()/8) && mouseX < sr.getScaledWidth()/10+3*(sr.getScaledWidth()/8)+32&& mouseY < sr.getScaledHeight() && mouseY > sr.getScaledHeight()-40;
        boolean isOverExit = mouseX > sr.getScaledWidth()/10+4*(sr.getScaledWidth()/8) && mouseX < sr.getScaledWidth()/10+4*(sr.getScaledWidth()/8)+32&& mouseY < sr.getScaledHeight() && mouseY > sr.getScaledHeight()-40;
        if (mouseButton == 0 && isOverSingleplayer) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        if (mouseButton == 0 && isOverMultiplayer) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if (mouseButton == 0 && isOverAltManager) {
            this.mc.displayGuiScreen(new GuiAltManager());
        }
        if (mouseButton == 0 && isOverSettings) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        if (mouseButton == 0 && isOverExit) {
            this.mc.running=false;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
