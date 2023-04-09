/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package xyz.WorstClient.management;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.world.Teleporter;
import xyz.WorstClient.api.EventBus;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.misc.EventKey;
import xyz.WorstClient.api.events.rendering.EventRender2D;
import xyz.WorstClient.api.events.rendering.EventRender3D;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.api.value.Value;
import xyz.WorstClient.management.FileManager;
import xyz.WorstClient.management.Manager;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.module.modules.combat.*;
import xyz.WorstClient.module.modules.movement.*;
import xyz.WorstClient.module.modules.player.*;
import xyz.WorstClient.module.modules.render.*;
import xyz.WorstClient.module.modules.world.*;
import xyz.WorstClient.utils.render.gl.GLUtils;

import org.lwjgl.input.Keyboard;

public class ModuleManager
implements Manager {
    public static List<Module> modules = new ArrayList<Module>();
    public static ArrayList<Module> sortedModList = new ArrayList();
    private boolean enabledNeededMod = true;
    public boolean nicetry = true;

    @Override
    public void init() {
    	//combat
        this.modules.add(new AntiBot());
        this.modules.add(new AutoSword());
        this.modules.add(new AutoHeal());
        this.modules.add(new BowAimBot());
        this.modules.add(new Criticals());
        this.modules.add(new Killaura());
        //safe
        this.modules.add(new WTap());
        this.modules.add(new Reach());
        this.modules.add(new Hitbox());
        //move
        this.modules.add(new Flight());
        this.modules.add(new InvMove());
        this.modules.add(new Jesus());
        this.modules.add(new Longjump());
        this.modules.add(new NoSlowDown());
        this.modules.add(new BoostFly());
        this.modules.add(new Scaffold());
        this.modules.add(new Speed());
        this.modules.add(new Sprint());
        this.modules.add(new Teleport());
        this.modules.add(new Step());
        //player
        this.modules.add(new AntiFall());
        this.modules.add(new AntiVelocity());
        this.modules.add(new AutoTool());
        this.modules.add(new InvCleaner());
        this.modules.add(new AntiSpam());
        this.modules.add(new Spammer());
        this.modules.add(new MCF());
        this.modules.add(new NameProtect());
        this.modules.add(new NoFall());
        this.modules.add(new NoHurtCam());
        this.modules.add(new Teams());
        //render
        this.modules.add(new Animations());
        this.modules.add(new BlockOverlay());
        this.modules.add(new Brightness());
        this.modules.add(new Chams());
        this.modules.add(new BigGod());
        this.modules.add(new ChestESP());
        this.modules.add(new ChestAura());
        this.modules.add(new DMGParticle());
        this.modules.add(new ClickGui());
        this.modules.add(new Crosshair());
        this.modules.add(new EnchantEffect());
        this.modules.add(new ESP());
        this.modules.add(new ItemPhysics());
        this.modules.add(new Health());
        this.modules.add(new HUD());
        this.modules.add(new ItemESP());
        this.modules.add(new Keyrender());
        this.modules.add(new Nametags());
        this.modules.add(new Projectiles());
        this.modules.add(new Radar());
        this.modules.add(new setColor());
        this.modules.add(new SetScoreboard());
        this.modules.add(new SpookySkeltal());
        this.modules.add(new TargetHUD());
        this.modules.add(new Tracers());
        this.modules.add(new Xray());
        //world
        this.modules.add(new AutoArmor());
        this.modules.add(new AutoGG());
        this.modules.add(new AutoL());
        this.modules.add(new Blink());
        //this.modules.add(new AntiKickSpammer());
        this.modules.add(new ChestStealer());
        this.modules.add(new LagbackCheck());
        this.modules.add(new MemoryFix());
        this.modules.add(new SpeedMine());
        this.modules.add(new PacketMotior());
        this.modules.add(new NoRotate());
        this.modules.add(new Phase());
        this.modules.add(new PingSpoof());
        this.modules.add(new SafeWalk());
       
        this.readSettings();
        for (Module m : modules) {
            m.makeCommand();
        }
        EventBus.getInstance().register(this);
    }

    public static List<Module> getModules() {
        return modules;
    }

    public static Module getModuleByClass(Class<? extends Module> cls) {
        for (Module m : modules) {
            if (m.getClass() != cls) continue;
            return m;
        }
        return null;
    }

    public static Module getModuleByName(String name) {
        for (Module m : modules) {
            if (!m.getName().equalsIgnoreCase(name)) continue;
            return m;
        }
        return null;
    }

    public Module getAlias(String name) {
        for (Module f : modules) {
            if (f.getName().equalsIgnoreCase(name)) {
                return f;
            }
            String[] alias = f.getAlias();
            int length = alias.length;
            int i = 0;
            while (i < length) {
                String s = alias[i];
                if (s.equalsIgnoreCase(name)) {
                    return f;
                }
                ++i;
            }
        }
        return null;
    }

    public static List<Module> getModulesInType(ModuleType t) {
        ArrayList<Module> output = new ArrayList<Module>();
        for (Module m : modules) {
            if (m.getType() != t) continue;
            output.add(m);
        }
        return output;
    }

    @EventHandler
    private void onKeyPress(EventKey e) {
        for (Module m : modules) {
            if (m.getKey() != e.getKey()) continue;
            m.setEnabled(!m.isEnabled());
        }
    }

    @EventHandler
    private void onGLHack(EventRender3D e) {
        GlStateManager.getFloat(2982, (FloatBuffer)GLUtils.MODELVIEW.clear());
        GlStateManager.getFloat(2983, (FloatBuffer)GLUtils.PROJECTION.clear());
        GlStateManager.glGetInteger(2978, (IntBuffer)GLUtils.VIEWPORT.clear());
    }

    @EventHandler
    private void on2DRender(EventRender2D e) {
        if (this.enabledNeededMod) {
            this.enabledNeededMod = false;
            for (Module m : modules) {
                if (!m.enabledOnStartup) continue;
                m.setEnabled(true);
            }
        }
    }

    private void readSettings() {
        List<String> binds = FileManager.read("Binds.txt");
        for (String v : binds) {
            String name = v.split(":")[0];
            String bind = v.split(":")[1];
            Module m = ModuleManager.getModuleByName(name);
            if (m == null) continue;
            m.setKey(Keyboard.getKeyIndex((String)bind.toUpperCase()));
        }
        List<String> enabled = FileManager.read("Enabled.txt");
        for (String v : enabled) {
            Module m = ModuleManager.getModuleByName(v);
            if (m == null) continue;
            m.enabledOnStartup = true;
        }
        
        List<String> vals = FileManager.read("Values.txt");
        for (String v : vals) {
            String name = v.split(":")[0];
            String values = v.split(":")[1];
            Module m = ModuleManager.getModuleByName(name);
            if (m == null) continue;
            for (Value value : m.getValues()) {
                if (!value.getName().equalsIgnoreCase(values)) continue;
                if (value instanceof Option) {
                    value.setValue(Boolean.parseBoolean(v.split(":")[2]));
                    continue;
                }
                if (value instanceof Numbers) {
                    value.setValue(Double.parseDouble(v.split(":")[2]));
                    continue;
                }
                ((Mode)value).setMode(v.split(":")[2]);
            }
        }
    }

}

