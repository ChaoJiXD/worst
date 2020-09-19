/*
 * Decompiled with CFR 0_132.
 */
package xyz.WorstClient.module.modules.render.UI;

import java.awt.Color;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import xyz.WorstClient.Client;
import xyz.WorstClient.api.EventBus;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.misc.EventKey;
import xyz.WorstClient.api.events.rendering.EventRender2D;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.api.value.Value;
import xyz.WorstClient.management.Manager;
import xyz.WorstClient.management.ModuleManager;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.utils.math.*;
import xyz.WorstClient.utils.render.RenderUtil;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.module.modules.render.HUD;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.ui.fontRenderer.UnicodeFontRenderer;
import xyz.WorstClient.utils.Helper;
import xyz.WorstClient.utils.RenderUtils;
import xyz.WorstClient.utils.render.RenderUtil;

public class TabUI
implements Manager {
    private Section section = Section.TYPES;
    private ModuleType selectedType = ModuleType.values()[0];
    private Module selectedModule = null;
    private Value selectedValue = null;
    private int currentType = 0;
    private int currentModule = 0;
    private int currentValue = 0;
    private int height = 12;
    private int maxType;
    private int maxModule;
    private int maxValue;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$enjoytheban$module$modules$render$UI$TabUI$Section;

    @Override
    public void init() {
        ModuleType[] arrmoduleType = ModuleType.values();
        int n = arrmoduleType.length;
        int n2 = 0;
        while (n2 < n) {
            ModuleType mt = arrmoduleType[n2];
            if (this.maxType <= Helper.mc.fontRendererObj.getStringWidth(mt.name().toUpperCase()) + 4) {
                this.maxType = Helper.mc.fontRendererObj.getStringWidth(mt.name().toUpperCase()) + 4;
            }
            ++n2;
        }
        Client.instance.getModuleManager();
        for (Module m : ModuleManager.getModules()) {
            if (this.maxModule > Helper.mc.fontRendererObj.getStringWidth(m.getName().toUpperCase()) + 4) continue;
            this.maxModule = Helper.mc.fontRendererObj.getStringWidth(m.getName().toUpperCase()) + 4;
        }
        Client.instance.getModuleManager();
        for (Module m : ModuleManager.getModules()) {
            if (m.getValues().isEmpty()) continue;
            for (Value val : m.getValues()) {
                if (this.maxValue > Helper.mc.fontRendererObj.getStringWidth(val.getDisplayName().toUpperCase()) + 4) continue;
                this.maxValue = Helper.mc.fontRendererObj.getStringWidth(val.getDisplayName().toUpperCase()) + 4;
            }
        }
        this.maxModule += 12;
        this.maxValue += 24;
        boolean highestWidth = false;
        this.maxType = this.maxType < this.maxModule ? this.maxModule : this.maxType;
        this.maxModule += this.maxType;
        this.maxValue += this.maxModule;
        EventBus.getInstance().register(this);
    }

    private void resetValuesLength() {
        this.maxValue = 0;
        for (Value val : this.selectedModule.getValues()) {
            int off;
            int n = off = val instanceof Option ? 6 : Helper.mc.fontRendererObj.getStringWidth(String.format(" \u00a77%s", val.getValue().toString())) + 6;
            if (this.maxValue > Helper.mc.fontRendererObj.getStringWidth(val.getDisplayName().toUpperCase()) + off) continue;
            this.maxValue = Helper.mc.fontRendererObj.getStringWidth(val.getDisplayName().toUpperCase()) + off;
        }
        this.maxValue += this.maxModule;
    }

    @EventHandler
    private void renderTabGUI(EventRender2D e) {
        block34 : {
            block33 : {
                if (HUD.useFont) break block33;
            	  UnicodeFontRenderer font = Client.fontManager.getFont(HUD.FontMod.getValue().name()+".ttf", 18);
              	  if(HUD.FontMod.getValue().name()=="Jigsaw" | HUD.FontMod.getValue().name()=="simpleton") {
              		  font = Client.fontManager.getFont(HUD.FontMod.getValue().name()+".otf", 22);
              	  }
                if (Helper.mc.gameSettings.showDebugInfo || !Client.instance.getModuleManager().getModuleByClass(HUD.class).isEnabled()) break block34;
                int categoryY = HUD.shouldMove ? 26 : this.height+10;
                int moduleY = categoryY;
                int valueY = categoryY;
                //RenderUtil.drawRoundedRect(2.0f, categoryY, this.maxType - 35, categoryY + 13 * ModuleType.values().length-2, 3, new Color(210, 210, 210,140).getRGB());
        		//RenderUtils.drawRoundedRect(2.0f, categoryY, this.maxType - 35, categoryY + 13 * ModuleType.values().length-2, 2, new Color(210, 210, 210,255).getRGB());
                RenderUtils.drawBorderedRect(2.0f, categoryY, this.maxType - 35, categoryY + 13 * ModuleType.values().length-2, 3.0f, new Color(50, 50, 200, 0).getRGB(), new Color(0, 80, 200, 100).getRGB());
                ModuleType[] moduleArray = ModuleType.values();
                int mA = moduleArray.length;
                int mA2 = 0;
                while (mA2 < mA) {
                    ModuleType mt = moduleArray[mA2];
                    if (this.selectedType == mt) {
                    	RenderUtils.drawRect((float)2.2,(float)(categoryY), (float)(this.maxType - 35), (float)(categoryY + Helper.mc.fontRendererObj.FONT_HEIGHT + 6), new Color(255, 255, 255,200).getRGB());
                        moduleY = categoryY;
                    }
                    if (this.selectedType == mt) {
                    	//RenderUtil.drawIcon();
                        UnicodeFontRenderer LogoFont = Client.fontManager.getFont("NovICON.ttf", 24);
                        LogoFont.drawString("H",4,23,selectedType == ModuleType.Combat? new Color(0, 80, 255, 100).getRGB():new Color(255,255,255).getRGB());
                        LogoFont.drawString("F",3,34.5,selectedType == ModuleType.Render? new Color(0, 80, 255, 100).getRGB():new Color(255,255,255).getRGB());
                        LogoFont.drawString("I",3,46.5,selectedType == ModuleType.Movement? new Color(0, 80, 255, 100).getRGB():new Color(255,255,255).getRGB());
                        LogoFont.drawString("C",4,58.5,selectedType == ModuleType.Player? new Color(0, 80, 255, 100).getRGB():new Color(255,255,255).getRGB());
                        LogoFont.drawString("E",4,71,selectedType == ModuleType.World? new Color(0, 80, 255, 100).getRGB():new Color(255,255,255).getRGB());
                        font.drawString(mt.name(), 18, categoryY + 3, new Color(0, 80, 200, 100).getRGB());
                    } else {
                        font.drawString(mt.name(), 16, categoryY + 3, new Color(255, 255, 255).getRGB());
                    }
                    categoryY += 12;
                    ++mA2;
                }
                if (this.section == Section.MODULES || this.section == Section.VALUES) {
                	RenderUtils.drawBorderedRect(this.maxType - 20, moduleY, this.maxModule - 38, moduleY + 12 * Client.instance.getModuleManager().getModulesInType(this.selectedType).size(), 3.0f, new Color(50, 50, 200, 0).getRGB(), new Color(0, 80, 200, 100).getRGB());
                    //RenderUtils.drawBorderedRect(this.maxType - 20, moduleY, this.maxModule - 38, moduleY + 12 * Client.instance.getModuleManager().getModulesInType(this.selectedType).size(), 2.0f, new Color(0, 0, 0, 130).getRGB(), new Color(0, 0, 0, 180).getRGB());
                    for (Module m : Client.instance.getModuleManager().getModulesInType(this.selectedType)) {
                        if (this.selectedModule == m) {
                        	RenderUtils.drawRect((float)(this.maxType - 19.5), (float)(moduleY + 0.5), (float)(this.maxModule - 38.5), (float)(moduleY + Helper.mc.fontRendererObj.FONT_HEIGHT + 2.5), new Color(255, 255, 255,200).getRGB());
                            //Gui.drawRect((double)this.maxType - 19.5, (double)moduleY + 0.5, (double)this.maxModule - 38.5, (double)(moduleY + Helper.mc.fontRendererObj.FONT_HEIGHT) + 2.5, new Color(102, 172, 255).getRGB());
                            valueY = moduleY;
                        }
                        if (this.selectedModule == m) {
                            font.drawString(m.getName(), this.maxType - 15, moduleY + 3, m.isEnabled() ? -1 : 11184810);
                        } else {
                            font.drawString(m.getName(), this.maxType - 17, moduleY + 3, m.isEnabled() ? -1 : 11184810);
                        }
                        if (!m.getValues().isEmpty()) {
                        	UnicodeFontRenderer LogoFont = Client.fontManager.getFont("NovICON.ttf", 24);
                        	LogoFont.drawString("G",this.maxModule - 47, (float)(moduleY-1),-1);
                        	//RenderUtils.drawRect(this.maxModule - 38, (float)(moduleY + 0.5), this.maxModule - 39, (float)(moduleY + Helper.mc.fontRendererObj.FONT_HEIGHT + 2.5), new Color(255, 255, 255,200).getRGB());
                            //Gui.drawRect(this.maxModule - 38, (double)moduleY + 0.5, this.maxModule - 39, (double)(moduleY + Helper.mc.fontRendererObj.FONT_HEIGHT) + 2.5, new Color(153, 200, 255).getRGB());
                            if (this.section == Section.VALUES && this.selectedModule == m) {
                                RenderUtils.drawBorderedRect(this.maxModule - 32, valueY, this.maxValue - 25, valueY + 12 * this.selectedModule.getValues().size(), 2.0f, new Color(10, 10, 10, 0).getRGB(), new Color(0, 80, 200, 100).getRGB());
                                for (Value val : this.selectedModule.getValues()) {
                                	RenderUtils.drawRect((float)(this.maxModule - 31.5), (float)(valueY + 0.5), (float)(this.maxValue - 25.5), (float)(valueY + Helper.mc.fontRendererObj.FONT_HEIGHT + 2.5),this.selectedValue == val ? new Color(255, 255, 255,200).getRGB():0);
                                    //Gui.drawRect((double)this.maxModule - 31.5, (double)valueY + 0.5, (double)this.maxValue - 25.5, (double)(valueY + Helper.mc.fontRendererObj.FONT_HEIGHT) + 2.5, this.selectedValue == val ? new Color(102, 172, 255).getRGB() : 0);
                                    if (val instanceof Option) {
                                        font.drawString(val.getDisplayName(), this.selectedValue == val ? this.maxModule - 27 : this.maxModule - 29, valueY + 3, (Boolean)val.getValue() != false ? new Color(153, 200, 255).getRGB() : 11184810);
                                    } else {
                                        String toRender = String.format("%s: \u00a77%s", val.getDisplayName(), val.getValue().toString());
                                        if (this.selectedValue == val) {
                                            font.drawString(toRender.replaceAll("¡ì7",""), this.maxModule - 27, valueY + 3, -1);
                                        } else {
                                            font.drawString(toRender.replaceAll("¡ì7",""), this.maxModule - 29, valueY + 3, -1);
                                        }
                                    }
                                    valueY += 12;
                                }
                            }
                        }
                        moduleY += 12;
                    }
                }
                break block34;
            }
            if (!Helper.mc.gameSettings.showDebugInfo && Client.instance.getModuleManager().getModuleByClass(HUD.class).isEnabled()) {
                int categoryY = HUD.shouldMove ? 26 : this.height;
                int moduleY = categoryY;
                int valueY = categoryY;
                RenderUtils.drawBorderedRect(2.0f, categoryY, this.maxType - 25, categoryY + 12 * ModuleType.values().length, 2.0f, new Color(0, 0, 0, 130).getRGB(), new Color(0, 0, 0, 180).getRGB());
                ModuleType[] moduleArray = ModuleType.values();
                int mA = moduleArray.length;
                int mA2 = 0;
                while (mA2 < mA) {
                    ModuleType mt = moduleArray[mA2];
                    if (this.selectedType == mt) {
                        Gui.drawRect(2.5, (double)categoryY + 0.5, (double)this.maxType - 25.5, (double)(categoryY + Helper.mc.fontRendererObj.FONT_HEIGHT) + 2.5, new Color(102, 172, 255).getRGB());
                        moduleY = categoryY;
                    }
                    if (this.selectedType == mt) {
                        Helper.mc.fontRendererObj.drawString(mt.name(), 7.0f, categoryY + 2, -1);
                    } else {
                        Helper.mc.fontRendererObj.drawString(mt.name(), 5.0f, categoryY + 2, new Color(180, 180, 180).getRGB());
                    }
                    categoryY += 12;
                    ++mA2;
                }
                if (this.section == Section.MODULES || this.section == Section.VALUES) {
                    RenderUtils.drawBorderedRect(this.maxType - 20, moduleY, this.maxModule - 38, moduleY + 12 * Client.instance.getModuleManager().getModulesInType(this.selectedType).size(), 2.0f, new Color(0, 0, 0, 130).getRGB(), new Color(0, 0, 0, 180).getRGB());
                    for (Module m : Client.instance.getModuleManager().getModulesInType(this.selectedType)) {
                        if (this.selectedModule == m) {
                        	RenderUtils.drawBorderedRect(this.maxType - 20, moduleY, this.maxModule - 38, moduleY + 12 * Client.instance.getModuleManager().getModulesInType(this.selectedType).size(), 3.0f, new Color(50, 50, 200, 0).getRGB(), new Color(0, 80, 200, 100).getRGB());
                            Gui.drawRect((double)this.maxType - 19.5, (double)moduleY + 0.5, (double)this.maxModule - 38.5, (double)(moduleY + Helper.mc.fontRendererObj.FONT_HEIGHT) + 2.5, new Color(102, 172, 255).getRGB());
                            valueY = moduleY;
                        }
                        if (this.selectedModule == m) {
                            Helper.mc.fontRendererObj.drawString(m.getName(), this.maxType - 15, moduleY + 2, m.isEnabled() ? -1 : 11184810);
                        } else {
                            Helper.mc.fontRendererObj.drawString(m.getName(), this.maxType - 17, moduleY + 2, m.isEnabled() ? -1 : 11184810);
                        }
                        if (!m.getValues().isEmpty()) {
                            Gui.drawRect(this.maxModule - 38, (double)moduleY + 0.5, this.maxModule - 39, (double)(moduleY + Helper.mc.fontRendererObj.FONT_HEIGHT) + 2.5, new Color(153, 200, 255).getRGB());
                            if (this.section == Section.VALUES && this.selectedModule == m) {
                                RenderUtils.drawBorderedRect(this.maxModule - 32, valueY, this.maxValue - 25, valueY + 12 * this.selectedModule.getValues().size(), 2.0f, new Color(10, 10, 10, 180).getRGB(), new Color(10, 10, 10, 180).getRGB());
                                for (Value val : this.selectedModule.getValues()) {
                                    Gui.drawRect((double)this.maxModule - 31.5, (double)valueY + 0.5, (double)this.maxValue - 25.5, (double)(valueY + Helper.mc.fontRendererObj.FONT_HEIGHT) + 2.5, this.selectedValue == val ? new Color(102, 172, 255).getRGB() : 0);
                                    if (val instanceof Option) {
                                        Helper.mc.fontRendererObj.drawString(val.getDisplayName(), this.selectedValue == val ? this.maxModule - 27 : this.maxModule - 29, valueY + 2, (Boolean)val.getValue() != false ? new Color(153, 200, 255).getRGB() : 11184810);
                                    } else {
                                        String toRender = String.format("%s: \u00a77%s", val.getDisplayName(), val.getValue().toString());
                                        if (this.selectedValue == val) {
                                            Helper.mc.fontRendererObj.drawString(toRender, this.maxModule - 27, valueY + 2, -1);
                                        } else {
                                            Helper.mc.fontRendererObj.drawString(toRender, this.maxModule - 29, valueY + 2, -1);
                                        }
                                    }
                                    valueY += 12;
                                }
                            }
                        }
                        moduleY += 12;
                    }
                }
            }
        }
    }

    @EventHandler
    private void onKey(EventKey e) {
        if (!Helper.mc.gameSettings.showDebugInfo) {
            block0 : switch (e.getKey()) {
                case 208: {
                    switch (TabUI.$SWITCH_TABLE$com$enjoytheban$module$modules$render$UI$TabUI$Section()[this.section.ordinal()]) {
                        case 1: {
                            ++this.currentType;
                            if (this.currentType > ModuleType.values().length - 1) {
                                this.currentType = 0;
                            }
                            this.selectedType = ModuleType.values()[this.currentType];
                            break block0;
                        }
                        case 2: {
                            ++this.currentModule;
                            if (this.currentModule > Client.instance.getModuleManager().getModulesInType(this.selectedType).size() - 1) {
                                this.currentModule = 0;
                            }
                            this.selectedModule = Client.instance.getModuleManager().getModulesInType(this.selectedType).get(this.currentModule);
                            break block0;
                        }
                        case 3: {
                            ++this.currentValue;
                            if (this.currentValue > this.selectedModule.getValues().size() - 1) {
                                this.currentValue = 0;
                            }
                            this.selectedValue = this.selectedModule.getValues().get(this.currentValue);
                        }
                    }
                    break;
                }
                case 200: {
                    switch (TabUI.$SWITCH_TABLE$com$enjoytheban$module$modules$render$UI$TabUI$Section()[this.section.ordinal()]) {
                        case 1: {
                            --this.currentType;
                            if (this.currentType < 0) {
                                this.currentType = ModuleType.values().length - 1;
                            }
                            this.selectedType = ModuleType.values()[this.currentType];
                            break block0;
                        }
                        case 2: {
                            --this.currentModule;
                            if (this.currentModule < 0) {
                                this.currentModule = Client.instance.getModuleManager().getModulesInType(this.selectedType).size() - 1;
                            }
                            this.selectedModule = Client.instance.getModuleManager().getModulesInType(this.selectedType).get(this.currentModule);
                            break block0;
                        }
                        case 3: {
                            --this.currentValue;
                            if (this.currentValue < 0) {
                                this.currentValue = this.selectedModule.getValues().size() - 1;
                            }
                            this.selectedValue = this.selectedModule.getValues().get(this.currentValue);
                        }
                    }
                    break;
                }
                case 205: {
                    switch (TabUI.$SWITCH_TABLE$com$enjoytheban$module$modules$render$UI$TabUI$Section()[this.section.ordinal()]) {
                        case 1: {
                            this.currentModule = 0;
                            this.selectedModule = Client.instance.getModuleManager().getModulesInType(this.selectedType).get(this.currentModule);
                            this.section = Section.MODULES;
                            break block0;
                        }
                        case 2: {
                            if (this.selectedModule.getValues().isEmpty()) break block0;
                            this.resetValuesLength();
                            this.currentValue = 0;
                            this.selectedValue = this.selectedModule.getValues().get(this.currentValue);
                            this.section = Section.VALUES;
                            break block0;
                        }
                        case 3: {
                            if (Helper.onServer("enjoytheban")) break block0;
                            if (this.selectedValue instanceof Option) {
                                this.selectedValue.setValue((Boolean)this.selectedValue.getValue() == false);
                            } else if (this.selectedValue instanceof Numbers) {
                                Numbers value = (Numbers)this.selectedValue;
                                double inc = (Double)value.getValue();
                                inc += ((Double)value.getIncrement()).doubleValue();
                                if ((inc = MathUtil.toDecimalLength(inc, 1)) > (Double)value.getMaximum()) {
                                    inc = (Double)((Numbers)this.selectedValue).getMinimum();
                                }
                                this.selectedValue.setValue(inc);
                            } else if (this.selectedValue instanceof Mode) {
                                Mode theme = (Mode)this.selectedValue;
                                Enum current = (Enum)theme.getValue();
                                int next = current.ordinal() + 1 >= theme.getModes().length ? 0 : current.ordinal() + 1;
                                this.selectedValue.setValue(theme.getModes()[next]);
                            }
                            this.resetValuesLength();
                        }
                    }
                    break;
                }
                case 28: {
                    switch (TabUI.$SWITCH_TABLE$com$enjoytheban$module$modules$render$UI$TabUI$Section()[this.section.ordinal()]) {
                        case 1: {
                            break block0;
                        }
                        case 2: {
                            this.selectedModule.setEnabled(!this.selectedModule.isEnabled());
                            break block0;
                        }
                        case 3: {
                            this.section = Section.MODULES;
                        }
                    }
                    break;
                }
                case 203: {
                    switch (TabUI.$SWITCH_TABLE$com$enjoytheban$module$modules$render$UI$TabUI$Section()[this.section.ordinal()]) {
                        case 1: {
                            break block0;
                        }
                        case 2: {
                            this.section = Section.TYPES;
                            this.currentModule = 0;
                            break block0;
                        }
                        case 3: {
                            if (Helper.onServer("enjoytheban")) break block0;
                            if (this.selectedValue instanceof Option) {
                                this.selectedValue.setValue((Boolean)this.selectedValue.getValue() == false);
                            } else if (this.selectedValue instanceof Numbers) {
                                Numbers value = (Numbers)this.selectedValue;
                                double inc = (Double)value.getValue();
                                inc -= ((Double)value.getIncrement()).doubleValue();
                                if ((inc = MathUtil.toDecimalLength(inc, 1)) < (Double)value.getMinimum()) {
                                    inc = (Double)((Numbers)this.selectedValue).getMaximum();
                                }
                                this.selectedValue.setValue(inc);
                            } else if (this.selectedValue instanceof Mode) {
                                Mode theme = (Mode)this.selectedValue;
                                Enum current = (Enum)theme.getValue();
                                int next = current.ordinal() - 1 < 0 ? theme.getModes().length - 1 : current.ordinal() - 1;
                                this.selectedValue.setValue(theme.getModes()[next]);
                            }
                            this.maxValue = 0;
                            for (Value val : this.selectedModule.getValues()) {
                                int off;
                                int n = off = val instanceof Option ? 6 : Minecraft.getMinecraft().fontRendererObj.getStringWidth(String.format(" \u00a77%s", val.getValue().toString())) + 6;
                                if (this.maxValue > Minecraft.getMinecraft().fontRendererObj.getStringWidth(val.getDisplayName().toUpperCase()) + off) continue;
                                this.maxValue = Minecraft.getMinecraft().fontRendererObj.getStringWidth(val.getDisplayName().toUpperCase()) + off;
                            }
                            this.maxValue += this.maxModule;
                        }
                    }
                }
            }
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$enjoytheban$module$modules$render$UI$TabUI$Section() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$com$enjoytheban$module$modules$render$UI$TabUI$Section;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[Section.values().length];
        try {
            arrn[Section.MODULES.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Section.TYPES.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Section.VALUES.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        $SWITCH_TABLE$com$enjoytheban$module$modules$render$UI$TabUI$Section = arrn;
        return $SWITCH_TABLE$com$enjoytheban$module$modules$render$UI$TabUI$Section;
    }

    public static enum Section {
        TYPES,
        MODULES,
        VALUES;
    }

}

