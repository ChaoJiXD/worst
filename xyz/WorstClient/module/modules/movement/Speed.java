/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package xyz.WorstClient.module.modules.movement;

import com.ibm.icu.math.BigDecimal;
import java.awt.Color;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;
import net.minecraft.world.World;
import xyz.WorstClient.Client;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventMove;
import xyz.WorstClient.api.events.world.EventPacketRecieve;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.management.ModuleManager;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.utils.BlockUtils;
import xyz.WorstClient.utils.MoveUtils;
import xyz.WorstClient.utils.PlayerUtil;
import xyz.WorstClient.utils.TimerUtil;
import xyz.WorstClient.utils.math.MathUtil;

public class Speed
extends Module {
    public double slow;

    private Minecraft var10000;
    private Minecraft var10001;
    private double zDist;
    private double xDist;
    public static Mode<Enum> mode = new Mode("Mode", "Mode", (Enum[])SigmaSpeedMode.values(), (Enum)SigmaSpeedMode.Hypixel);
    private Option<Boolean> lagcheck = new Option<Boolean>("LagCheck", "LagCheck", true);
    private double[] values = new double[]{0.08, 0.09316090325960147, 1.688, 2.149, 0.66};
    public static int stage;
    private double movementSpeed;
    private boolean firstjump;
    private int stupidAutisticTickCounting;
    private World world;
    double moveSpeed;
    private double distance;
    private int packetCounter;
    private TimerUtil deactivationDelay = new TimerUtil();
    private double posY;
    private int speedTick;
    private boolean legitHop = false;
    private double lastDist;
    private int tick;
    private List<AxisAlignedBB> collidingList;
    public boolean shouldslow = false;
    private TimerUtil timer = new TimerUtil();
    private double difference;
    private double speed;
    int steps;
    private int level;
    public static int aacCount;
    boolean collided = false;
    boolean lessSlow;
    TimerUtil aac = new TimerUtil();
    TimerUtil lastCheck = new TimerUtil();
    TimerUtil lastFall = new TimerUtil();
    double less;
    double stair;

    public Speed() {
        super("Speed", new String[]{"zoom"}, ModuleType.Movement);
        this.setColor(new Color(99, 248, 91).getRGB());
        this.addValues(mode, this.lagcheck);
    }

    private boolean isInLiquid() {
        if (Minecraft.thePlayer == null) {
            return false;
        }
        int x2 = MathHelper.floor_double(Minecraft.thePlayer.boundingBox.minX);
        while (x2 < MathHelper.floor_double(Minecraft.thePlayer.boundingBox.maxX) + 1) {
            int z2 = MathHelper.floor_double(Minecraft.thePlayer.boundingBox.minZ);
            while (z2 < MathHelper.floor_double(Minecraft.thePlayer.boundingBox.maxZ) + 1) {
                BlockPos pos = new BlockPos(x2, (int)Minecraft.thePlayer.boundingBox.minY, z2);
                Block block = Minecraft.theWorld.getBlockState(pos).getBlock();
                if (block != null && !(block instanceof BlockAir)) {
                    return block instanceof BlockLiquid;
                }
                ++z2;
            }
            ++x2;
        }
        return false;
    }

    @Override
    public void onDisable() {
        Timer.timerSpeed = 1.0f;
        this.tick = 0;
        aacCount = 0;
    }

    @Override
    public void onEnable() {
        boolean player = Minecraft.thePlayer == null;
        this.collided = player ? false : Minecraft.thePlayer.isCollidedHorizontally;
        this.lessSlow = false;
        if (Minecraft.thePlayer != null) {
            this.speed = MoveUtils.defaultSpeed();
        }
        this.less = 0.0;
        this.lastDist = 0.0;
        stage = 2;
        Timer.timerSpeed = 1.0f;
        super.onEnable();
    }

    private boolean canZoom() {
        if (Minecraft.thePlayer.moving() && Minecraft.thePlayer.onGround && !this.isInLiquid()) {
            return true;
        }
        return false;
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e2) {
        this.setSuffix(mode.getValue());
    }

    private boolean isBlockUnder(Material air2) {
        return false;
    }

    private int getRandom(int i2) {
        return 0;
    }

    public void setSpeed(double speed) {
        Minecraft.thePlayer.motionX = - Math.sin(this.getDirection()) * speed;
        Minecraft.thePlayer.motionZ = Math.cos(this.getDirection()) * speed;
    }

    public float getDirection() {
        float yaw = Minecraft.thePlayer.rotationYaw;
        if (Minecraft.thePlayer.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (Minecraft.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (Minecraft.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Minecraft.thePlayer.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (Minecraft.thePlayer.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw *= 0.017453292f;
    }

    @EventHandler
    private void onPacket(EventPacketRecieve ep2) {
        if (this.lagcheck.getValue().booleanValue() && ep2.getPacket() instanceof S08PacketPlayerPosLook && this.deactivationDelay.delay(2000.0f)) {
            ++this.packetCounter;
            aacCount = 0;
            S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook)ep2.getPacket();
            pac.yaw = Minecraft.thePlayer.rotationYaw;
            pac.pitch = Minecraft.thePlayer.rotationPitch;
            stage = -5;
            Client.getModuleManager();
            if (ModuleManager.getModuleByName("Speed").isEnabled()) {
                Client.getModuleManager();
                ModuleManager.getModuleByName("Speed").setEnabled(false);
            }
        }
    }

    @EventHandler
    private void onMove(EventMove e2) {
        if (mode.getValue() == SigmaSpeedMode.Hypixel) {
            if (Minecraft.thePlayer.isCollidedHorizontally) {
                this.collided = true;
            }
            if (this.collided) {
                Timer.timerSpeed = 1.0f;
                stage = -1;
            }
            if (this.stair > 0.0) {
                this.stair -= 0.25;
            }
            this.less -= this.less > 1.0 ? 0.12 : 0.11;
            if (this.less < 0.0) {
                this.less = 0.0;
            }
            if (!BlockUtils.isInLiquid() && MoveUtils.isOnGround(0.01) && PlayerUtil.isMoving2()) {

                this.collided = Minecraft.thePlayer.isCollidedHorizontally;
                if (stage >= 0 || this.collided) {
                    stage = 0;
                    double motY = 0.4086666 + (double)MoveUtils.getJumpEffect() * 0.1;
                    if (this.stair == 0.0) {
                        Minecraft.thePlayer.jump();
                        Minecraft.thePlayer.motionY = motY;
                        EventMove.setY(Minecraft.thePlayer.motionY);
                    }
                    this.less += 1.0;
                    this.lessSlow = this.less > 1.0 && !this.lessSlow;
                    if (this.less > 1.12) {
                        this.less = 1.12;
                    }
                }
            }
            this.speed = this.getHypixelSpeed(stage) + 0.0331;
            this.speed *= 0.91;
            if (this.stair > 0.0) {
                this.speed *= 0.66 - (double)MoveUtils.getSpeedEffect() * 0.1;
            }
            if (stage < 0) {
                this.speed = MoveUtils.defaultSpeed();
            }
            if (this.lessSlow) {
                this.speed *= 0.96;
            }
            if (BlockUtils.isInLiquid()) {
                this.speed = 0.12;
            }
            if (Minecraft.thePlayer.moveForward != 0.0f || Minecraft.thePlayer.moveStrafing != 0.0f) {
                this.setMotion(e2, this.speed);
                ++stage;
            }
            if (mode.getValue() == SigmaSpeedMode.WatchDog) {

                if (Minecraft.thePlayer.isCollidedHorizontally) {
                    this.collided = true;
                }
                if (this.collided) {
                    this.mc.timer.timerSpeed = 1.0f;
                    this.stage = -1;
                }
                if (this.stair > 0.0) {
                    this.stair -= 0.25;
                }
                this.less -= this.less > 1.0 ? 0.12 : 0.11;
                if (this.less < 0.0) {
                    this.less = 0.0;
                }
                if (!this.isInLiquid() && this.isOnGround(0.01) && this.isMoving2()) {
                    this.collided = Minecraft.thePlayer.isCollidedHorizontally;
                    if (this.stage >= 0 || this.collided) {
                        this.stage = 0;
                        double motY = 0.41111;
                        if (this.stair == 0.0) {
                            this.slow = (double)Speed.randomNumber(-10000, 0) / 1.0E8;
                            Minecraft.thePlayer.jump();
                            Minecraft.thePlayer.motionY = motY;
                            e2.setY(Minecraft.thePlayer.motionY);
                        }
                        this.less += 1.0;
                        this.lessSlow = this.less > 1.0 && !this.lessSlow;
                        if (this.less > 1.12) {
                            this.less = 1.12;
                        }
                    }
                }
                this.movementSpeed = this.getHypixelSpeed(this.stage) + 0.0331;
                this.movementSpeed *= 0.91;
                this.movementSpeed += this.slow;
                if (this.stair > 0.0) {
                    this.movementSpeed *= 0.7 - (double)this.getSpeedEffect() * 0.1;
                }
                if (this.stage < 0) {
                    this.movementSpeed = defaultSpeed();
                }
                if (this.lessSlow) {
                    this.movementSpeed *= 0.96;
                }
                if (this.lessSlow) {
                    this.movementSpeed *= 0.95;
                }
                if (this.isInLiquid()) {
                    this.movementSpeed = 0.12;
                }
                if (Minecraft.thePlayer.moveForward != 0.0f || Minecraft.thePlayer.moveStrafing != 0.0f) {
                    this.setMotion(e2, this.movementSpeed);
                    ++this.stage;
                }
            }
           
        }
        if(mode.getValue() == SigmaSpeedMode.LatestHypixel){
            if(mc.thePlayer.hurtTime > 0){
            if (Minecraft.thePlayer.isCollidedHorizontally) {
                this.collided = true;
            }
            if (this.collided) {
                Timer.timerSpeed = 1.0f;
                stage = -1;
            }
            if (this.stair > 0.0) {
                this.stair -= 0.25;
            }
            this.less -= this.less > 1.0 ? 0.12 : 0.11;
            if (this.less < 0.0) {
                this.less = 0.0;
            }
            if (!BlockUtils.isInLiquid() && MoveUtils.isOnGround(0.01) && PlayerUtil.isMoving2()) {
                this.collided = Minecraft.thePlayer.isCollidedHorizontally;
                if (stage >= 0 || this.collided) {
                    stage = 0;
                    double motY = 0.4086666 + (double) MoveUtils.getJumpEffect() * 0.1;
                    if (this.stair == 0.0) {
                        Minecraft.thePlayer.jump();
                        Minecraft.thePlayer.motionY = motY;
                        EventMove.setY(Minecraft.thePlayer.motionY);
                    }
                    this.less += 1.0;
                    this.lessSlow = this.less > 1.0 && !this.lessSlow;
                    if (this.less > 1.12) {
                        this.less = 1.12;
                     }
                 }
                }

            this.speed = this.getHypixelSpeed(stage) + 0.0331;
            this.speed *= 0.91;
            if (this.stair > 0.0) {
                this.speed *= 0.66 - (double)MoveUtils.getSpeedEffect() * 0.1;
            }
            if (stage < 0) {
                this.speed = MoveUtils.defaultSpeed();
            }
            if (this.lessSlow) {
                this.speed *= 0.96;
            }
            if (BlockUtils.isInLiquid()) {
                this.speed = 0.12;
            }
            if (Minecraft.thePlayer.moveForward != 0.0f || Minecraft.thePlayer.moveStrafing != 0.0f) {
                this.setMotion(e2, this.speed);
                ++stage;
                }

            }else if(mc.thePlayer.hurtTime == 0 && !BlockUtils.isInLiquid() && MoveUtils.isOnGround(0.01) && PlayerUtil.isMoving2()) {Minecraft.thePlayer.jump();
                Minecraft.thePlayer.motionY = 0.42;
                EventMove.setY(0.42);}
        }
        if (mode.getValue() == SigmaSpeedMode.CnHypixel) {
            if (Minecraft.thePlayer.isInWater() && PlayerUtil.isMoving2()) {
                Minecraft.thePlayer.jump();
            }
            if (stage < 1) {
                ++stage;
                this.distance = 0.0;
                return;
            }
            if (this.canZoom() && stage == 2) {
                Minecraft.thePlayer.motionY = 0.418;
                EventMove.setY(0.418);
                this.movementSpeed *= 1.75;
            } else if (stage == 3) {
                double var11 = 0.66 * (this.distance - MathUtil.getBaseMovementSpeed());
                this.movementSpeed = this.distance - var11;
            } else {
                List<AxisAlignedBB> i2 = Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.boundingBox.offset(0.0, Minecraft.thePlayer.motionY, 0.0));
                if (i2.size() > 0 || Minecraft.thePlayer.isCollidedVertically && stage > 1) {
                    stage = Minecraft.thePlayer.moving() ? 1 : 0;
                }
                this.movementSpeed = 1.0;
                this.movementSpeed = this.distance - this.distance / 160.0;
            }
            this.movementSpeed = Math.max(this.movementSpeed, MathUtil.getBaseMovementSpeed());
            Minecraft.thePlayer.setMoveSpeed(e2, this.movementSpeed);
            if (Minecraft.thePlayer.moving()) {
                ++stage;
            }
            this.movementSpeed = Math.max(this.movementSpeed, this.getBaseMoveSpeed());
            float var12 = MovementInput.moveForward;
            float strafe = MovementInput.moveStrafe;
            float yaw = Minecraft.thePlayer.rotationYaw;
            Object event = null;
            if (var12 == 0.0f && strafe == 0.0f) {
                Minecraft.thePlayer.motionX *= (Minecraft.thePlayer.motionZ *= 0.0);
                EventMove.x = 0.0;
                EventMove.z = 0.0;
            } else if (var12 != 0.0f) {
                if (strafe >= 1.0f) {
                    yaw += var12 > 0.0f ? -45.0f : 45.0f;
                    strafe = 0.0f;
                } else if (strafe <= -1.0f) {
                    yaw += var12 > 0.0f ? 45.0f : -45.0f;
                    strafe = 0.0f;
                }
                if (var12 > 0.0f) {
                    var12 = 1.0f;
                } else if (var12 < 0.0f) {
                    var12 = -1.0f;
                }
            }
            double mx2 = Math.cos(Math.toRadians(yaw + 90.0f));
            double mz2 = Math.sin(Math.toRadians(yaw + 90.0f));
            EventMove.x = ((double)var12 * this.movementSpeed * mx2 + (double)strafe * this.movementSpeed * mz2) * 0.99;
            EventMove.z = ((double)var12 * this.movementSpeed * mz2 - (double)strafe * this.movementSpeed * mx2) * 0.99;
            if (var12 == 0.0f && strafe == 0.0f) {
                EventMove.x = 0.0;
                EventMove.z = 0.0;
                Minecraft.thePlayer.motionX *= (Minecraft.thePlayer.motionZ *= 0.0);
            } else if (var12 != 0.0f) {
                if (strafe >= 1.0f) {
                    float var10000 = yaw + (var12 > 0.0f ? -45.0f : 45.0f);
                    strafe = 0.0f;
                } else if (strafe <= -1.0f) {
                    float var10000 = yaw + (var12 > 0.0f ? 45.0f : -45.0f);
                    strafe = 0.0f;
                }
                if (var12 > 0.0f) {
                    var12 = 1.0f;
                } else if (var12 < 0.0f) {
                    var12 = -1.0f;
                }
            }
        }
    }

    public static boolean isMoving2() {
        Minecraft.getMinecraft();
        if (Minecraft.thePlayer.moveForward == 0.0f) {
            Minecraft.getMinecraft();
            if (Minecraft.thePlayer.moveStrafing == 0.0f) {
                return false;
            }
        }
        return true;
    }

    private boolean MovementInput() {
        if (!(Speed.mc.gameSettings.keyBindForward.pressed || Speed.mc.gameSettings.keyBindLeft.pressed || Speed.mc.gameSettings.keyBindRight.pressed || Speed.mc.gameSettings.keyBindBack.pressed)) {
            return false;
        }
        return true;
    }
    public boolean isOnGround(double height) {
        if (!this.mc.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0, - height, 0.0)).isEmpty()) {
            return true;
        }
        return false;
    }

    public void setMoveSpeed(EventMove event, double speed) {
        MovementInput movementInput = Minecraft.thePlayer.movementInput;
        double forward = MovementInput.moveForward;
        double strafe = MovementInput.moveStrafe;
        float yaw = Minecraft.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            EventMove.x = 0.0;
            EventMove.x = 0.0;
        } else {
            if (forward != 0.0) {
                Minecraft.thePlayer.setSpeed(0.279);
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            EventMove.x = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            EventMove.z = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }
    public static int randomNumber(int max, int min) {
        return Math.round((float)min + (float)Math.random() * (float)(max - min));
    }

    private double defaultSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public double round(double var1, int var3) {
        if (var3 < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal var4 = new BigDecimal(var1);
        var4 = var4.setScale(var3);
        return var4.doubleValue();
    }

    private void setMotion(EventMove em2, double speed) {
        double forward = MovementInput.moveForward;
        double strafe = MovementInput.moveStrafe;
        float yaw = Minecraft.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            em2.setX(0.0);
            em2.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            em2.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            em2.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }
        public int getSpeedEffect() {
            if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
                return Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
            }
            return 0;
        
    }

    double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    private double getHypixelSpeed(int stage) {
        double value = MoveUtils.defaultSpeed() + 0.028 * (double)MoveUtils.getSpeedEffect() + (double)MoveUtils.getSpeedEffect() / 15.0;
        double firstvalue = 0.4145 + (double)MoveUtils.getSpeedEffect() / 12.5;
        double decr = (double)stage / 500.0 * 2.0;
        if (stage == 0) {
            if (this.timer.delay(300.0f)) {
                this.timer.reset();
            }
            if (!this.lastCheck.delay(500.0f)) {
                if (!this.shouldslow) {
                    this.shouldslow = true;
                }
            } else if (this.shouldslow) {
                this.shouldslow = false;
            }
            value = 0.64 + ((double)MoveUtils.getSpeedEffect() + 0.028 * (double)MoveUtils.getSpeedEffect()) * 0.134;
        } else if (stage == 1) {
            value = firstvalue;
        } else if (stage >= 2) {
            value = firstvalue - decr;
        }
        if (this.shouldslow || !this.lastCheck.delay(500.0f) || this.collided) {
            value = 0.2;
            if (stage == 0) {
                value = 0.0;
            }
        }
        return Math.max(value, this.shouldslow ? value : MoveUtils.defaultSpeed() + 0.028 * (double)MoveUtils.getSpeedEffect());
    }

    static enum SigmaSpeedMode {
        LatestHypixel,
        Hypixel,
        CnHypixel,
        WatchDog;

    }
}

