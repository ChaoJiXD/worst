package xyz.WorstClient.ui.login;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import xyz.WorstClient.utils.MathUtil;

import java.io.IOException;

public final class GuiMicrosoftLogin extends GuiScreen {
    private volatile MicrosoftLogin microsoftLogin;
    private volatile boolean closed = false;

    private final GuiScreen parentScreen;

    public GuiMicrosoftLogin(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
        new Thread("MicrosoftLogin Thread") {
            @Override
            public void run() {
                try {
                    microsoftLogin = new MicrosoftLogin();
                    microsoftLogin.show();

                    while (true) {
                        if (microsoftLogin.logged) {
                            microsoftLogin.close();
                            microsoftLogin.status = EnumChatFormatting.GREEN + "Login succfull! " + microsoftLogin.userName;
                            mc.session = new Session(microsoftLogin.userName,microsoftLogin.uuid,microsoftLogin.accessToken,"mojang");
                            closed = true;
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    microsoftLogin.status = EnumChatFormatting.RED + "Login Faild! " + e.getClass().getName() + ":" + e.getMessage();
                    microsoftLogin.close();
                    closed = true;
                }

                interrupt();
            }
        }.start();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button.id == 0) {
            if (microsoftLogin != null && !closed) {
                microsoftLogin.close();
                closed = true;
            }

            mc.displayGuiScreen(parentScreen);
        }

        if(button.id == 1){
            mc.session = new Session(MathUtil.usingRandom(8), "","", "mojang");
        }
    }

    @Override
    public void initGui() {
        super.initGui();

        buttonList.add(new GuiButton(0,width / 2 - 100,height / 2 + 50,"Back"));
        buttonList.add(new GuiButton(1,width / 2 - 100,height / 2 + 100,"Random Offline"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (microsoftLogin == null) {
            mc.fontRendererObj.drawStringWithShadow(EnumChatFormatting.YELLOW + "Login...",width / 2.0f,height / 2.0f - 5f,-1);
        } else {
            mc.fontRendererObj.drawStringWithShadow(microsoftLogin.status,width / 2.0f,height / 2.0f - 5f,-1);
        }
    }
}
