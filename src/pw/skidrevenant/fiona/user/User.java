package pw.skidrevenant.fiona.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.utils.CancelType;
import pw.skidrevenant.fiona.utils.CustomLocation;

public class User
{
    private Player player;
    private UUID uuid;
    private Map<Checks, Integer> vl;
    private ArrayList<String> logList;
    private int AirTicks;
    private int GroundTicks;
    private int IceTicks;
    private int inva;
    private int invb;
    private boolean hasSwung;
    private boolean hasAlerts;
    private CancelType isCancelled;
    private long lastSwing;
    private int swingPackets;
    private int usePackets;
    private long lastPacket;
    private long teleported;
    private double lastYawDifference;
    private long lastFlightChange;
    private long loginMillis;
    private double lastPitchDifferenceAimC;
    private double lastPitchAimC;
    private long lastMove;
    private long lastServerKeepAlive;
    private long lastClientKeepAlive;
    private long lastMoveTime;
    private long lastHeal;
    private long lastPotionSplash;
    private long isHit;
    private long lastKeepAlive;
    private int lastPing;
    private int averagePing;
    private int posPackets;
    private double deltaXZ;
    private double deltaY;
    private boolean placedBlock;
    private long tookVelocity;
    private Checks lastCheckSetOff;
    private Entity lastHitPlayer;
    private int blockTicks;
    private double realFallDistance;
    private long lastBow;
    private long lastAttack;
    private Location setbackLocation;
    private double lastScaffoldYaw;
    private double veryLastScaffoldYaw;
    private double lastScaffoldPitch;
    private double veryLastScaffoldPitch;
    private long lastBlockPlace;
    private Block blockPlaced;
    private Block lastBlockPlaced;
    private double rotation;
    private int cancelTicks;
    public List<Long> packets;
    public long lastPingSpoofDifference;
    private int jumpTicks;
    private double lastKillauraYaw;
    private long lastScaffoldPlace;
    public List<Long> scaffoldDifferences;
    private long lastScaffoldDifference;
    private boolean movementCancel;
    private Vector lastVelocity;
    private double lastMotion;
    private List<CustomLocation> last20MovePackets;
    private double packetsFromLag;
    private int loginTicks;
    private double lastDirPosPacket;
    private double directionViolations;
    private double lastSpecificYaw;
    private double lastSpecificYawDif;
    private int waterTicks;
    private int speedTicks;
    private int lastSpeedLevel;
    private double healhBeforeGround;
    private Checks checkCancelled;
    private double lastJesusY;
    private double lastSpeedDifference;
    private int blockCancelled;
    private long lastVerticalPlace;
    private Block lastVerticalBlock;
    private boolean bypass;
    private boolean teleportedPhase;
    private double lastY;
    private boolean debugMode;
    private double lastYawAim;
    private long lastDamage;
    private int cactusTicks;
    private int sneakVL;
    private long sneak1;
    private long sneak2;
    private int lastPackets;
    private int packetLoss;
    private double slimeDistance;
    private int sprintTicks;
    private long lastPosPacket;
    private long packetPosDifference;
    private long lastRegen;
    private long foodClick;
    private int foodSprintTicks;
    private int soulSandTicks;
    private int webTicks;
    private double lastYVelocity;
    private int fluxAACVerbose;
    public List<Double> speedValues;
    private int leftClicks;
    private int rightClicks;
    
    public User(final Player player) {
        this.logList = new ArrayList<String>();
        this.AirTicks = 0;
        this.GroundTicks = 0;
        this.IceTicks = 0;
        this.inva = 0;
        this.invb = 0;
        this.hasSwung = false;
        this.hasAlerts = false;
        this.isCancelled = CancelType.NONE;
        this.lastSwing = 0L;
        this.swingPackets = 0;
        this.usePackets = 0;
        this.lastPacket = 0L;
        this.teleported = 0L;
        this.lastYawDifference = 1.0;
        this.lastFlightChange = 0L;
        this.loginMillis = 0L;
        this.lastMove = 0L;
        this.lastServerKeepAlive = 0L;
        this.lastClientKeepAlive = 0L;
        this.lastMoveTime = 0L;
        this.lastHeal = 0L;
        this.lastPotionSplash = 0L;
        this.isHit = 0L;
        this.lastKeepAlive = -1L;
        this.lastPing = 0;
        this.averagePing = 0;
        this.posPackets = 0;
        this.deltaXZ = 0.0;
        this.deltaY = 0.0;
        this.placedBlock = false;
        this.tookVelocity = 0L;
        this.blockTicks = 0;
        this.realFallDistance = 0.0;
        this.lastBow = 0L;
        this.lastAttack = 0L;
        this.lastScaffoldYaw = -1.0;
        this.veryLastScaffoldYaw = 1.0;
        this.lastScaffoldPitch = -1.0;
        this.veryLastScaffoldPitch = 1.0;
        this.lastBlockPlace = 0L;
        this.rotation = 0.0;
        this.cancelTicks = 0;
        this.lastPingSpoofDifference = 0L;
        this.jumpTicks = 0;
        this.lastKillauraYaw = 0.0;
        this.lastScaffoldPlace = 0L;
        this.lastScaffoldDifference = 0L;
        this.movementCancel = false;
        this.lastMotion = 0.0;
        this.last20MovePackets = new ArrayList<CustomLocation>();
        this.packetsFromLag = 0.0;
        this.loginTicks = 0;
        this.lastDirPosPacket = 0.0;
        this.directionViolations = 0.0;
        this.lastSpecificYaw = 0.0;
        this.lastSpecificYawDif = 0.0;
        this.waterTicks = 0;
        this.speedTicks = 0;
        this.lastSpeedLevel = 0;
        this.healhBeforeGround = 0.0;
        this.lastJesusY = 0.0;
        this.lastSpeedDifference = 0.0;
        this.blockCancelled = 0;
        this.lastVerticalPlace = 0L;
        this.bypass = false;
        this.teleportedPhase = false;
        this.lastY = 0.0;
        this.debugMode = false;
        this.lastYawAim = 0.0;
        this.lastDamage = 0L;
        this.cactusTicks = 0;
        this.sneakVL = 0;
        this.sneak1 = 0L;
        this.sneak2 = 0L;
        this.lastPackets = 0;
        this.packetLoss = 0;
        this.slimeDistance = 0.0;
        this.sprintTicks = 0;
        this.lastPosPacket = 0L;
        this.packetPosDifference = 0L;
        this.lastRegen = 0L;
        this.foodClick = 0L;
        this.foodSprintTicks = 0;
        this.soulSandTicks = 0;
        this.webTicks = 0;
        this.lastYVelocity = 0.0;
        this.fluxAACVerbose = 0;
        this.player = player;
        this.uuid = player.getUniqueId();
        this.vl = new HashMap<Checks, Integer>();
        this.packets = new ArrayList<Long>();
        this.speedValues = new ArrayList<Double>();
        this.scaffoldDifferences = new ArrayList<Long>();
        this.lastVelocity = new Vector(0, 0, 0);
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public boolean isStaff() {
        return this.player.hasPermission("fiona.staff") || this.player.isOp();
    }
    
    public void setLastPitchAimC(final double pitch) {
        this.lastPitchAimC = pitch;
    }
    
    public double getLastPitchAimC() {
        return this.lastPitchAimC;
    }
    
    public UUID getUUID() {
        return this.uuid;
    }
    
    public int getVL(final Checks check) {
        return this.vl.getOrDefault(check, 0);
    }
    
    public void setVL(final Checks check, final int vl) {
        if (!Fiona.getAC().whitelisted.contains(this.getPlayer())) {
            this.vl.put(check, vl);
        }
    }
    
    public Map<Checks, Integer> getVLs() {
        return this.vl;
    }
    
    public boolean isMovementCancel() {
        return this.movementCancel;
    }
    
    public void setMovementCancel(final boolean movementCancel) {
        this.movementCancel = movementCancel;
    }
    
    public int getLastSpeedLevel() {
        return this.lastSpeedLevel;
    }
    
    public void setLastSpeedLevel(final int lastSpeedLevel) {
        this.lastSpeedLevel = lastSpeedLevel;
    }
    
    public long getLastPingSpoofDifference() {
        return this.lastPingSpoofDifference;
    }
    
    public void setLastPingSpoofDifference(final long l) {
        this.lastPingSpoofDifference = l;
    }
    
    public boolean needBan(final Checks check) {
        return this.getVL(check) > check.getWeight();
    }
    
    public Vector getLastVelocity() {
        return this.lastVelocity;
    }
    
    public void setLastVelocity(final Vector lastVelocity) {
        this.lastVelocity = lastVelocity;
    }
    
    public double getLastKillauraYaw() {
        return this.lastKillauraYaw;
    }
    
    public void setLastKillauraYaw(final double lastKillauraYaw) {
        this.lastKillauraYaw = lastKillauraYaw;
    }
    
    public long getLastScaffoldDifference() {
        return this.lastScaffoldDifference;
    }
    
    public void setLastScaffoldDifference(final long lastScaffoldDifference) {
        this.lastScaffoldDifference = lastScaffoldDifference;
    }
    
    public int getLoginTicks() {
        return this.loginTicks;
    }
    
    public void setLoginTicks(final int loginTicks) {
        this.loginTicks = loginTicks;
    }
    
    public double getRotation() {
        return this.rotation;
    }
    
    public void setRotation(final double rotation) {
        this.rotation = rotation;
    }
    
    public long getLastAttack() {
        return this.lastAttack;
    }
    
    public void setLastAttack(final long lastAttack) {
        this.lastAttack = lastAttack;
    }
    
    public long getLastScaffoldPlace() {
        return this.lastScaffoldPlace;
    }
    
    public void setLastScaffoldPlace(final long lastScaffoldPlace) {
        this.lastScaffoldPlace = lastScaffoldPlace;
    }
    
    public int clearVL(final Checks check) {
        return this.getVLs().put(check, 0);
    }
    
    public double getLastYawDifference() {
        return this.lastYawDifference;
    }
    
    public void setLastYawDifference(final double lastYawDifference) {
        this.lastYawDifference = lastYawDifference;
    }
    
    public boolean isTeleportedPhase() {
        return this.teleportedPhase;
    }
    
    public void setTeleportedPhase(final boolean teleportedPhase) {
        this.teleportedPhase = teleportedPhase;
    }
    
    public int getCactusTicks() {
        return this.cactusTicks;
    }
    
    public void setCactusTicks(final int cactusTicks) {
        this.cactusTicks = cactusTicks;
    }
    
    public boolean isDebugMode() {
        return this.debugMode;
    }
    
    public void setDebugMode(final boolean debugMode) {
        this.debugMode = debugMode;
    }
    
    public int getAveragePing() {
        return this.averagePing;
    }
    
    public void setAveragePing(final int averagePing) {
        this.averagePing = averagePing;
    }
    
    public double getLastY() {
        return this.lastY;
    }
    
    public void setLastY(final double lastY) {
        this.lastY = lastY;
    }
    
    public int getJumpTicks() {
        return this.jumpTicks;
    }
    
    public void setJumpTicks(final int jumpTicks) {
        this.jumpTicks = jumpTicks;
    }
    
    public double getLastSpecificYaw() {
        return this.lastSpecificYaw;
    }
    
    public void setLastSpecificYaw(final double lastSpecificYaw) {
        this.lastSpecificYaw = lastSpecificYaw;
    }
    
    public double getLastYawAim() {
        return this.lastYawAim;
    }
    
    public void setLastYawAim(final double lastYawAim) {
        this.lastYawAim = lastYawAim;
    }
    
    public double getLastSpecificYawDif() {
        return this.lastSpecificYawDif;
    }
    
    public void setLastSpecificYawDif(final double lastSpecificYawDif) {
        this.lastSpecificYawDif = lastSpecificYawDif;
    }
    
    public int getCancelTicks() {
        return this.cancelTicks;
    }
    
    public void setCancelTicks(final int cancelTicks) {
        this.cancelTicks = cancelTicks;
    }
    
    public int getSoulSandTicks() {
        return this.soulSandTicks;
    }
    
    public void setSoulSandTicks(final int soulSandTicks) {
        this.soulSandTicks = soulSandTicks;
    }
    
    public void clearData() {
        this.player = null;
        this.uuid = null;
        this.vl.clear();
        this.setAirTicks(0);
        this.setGroundTicks(0);
        this.setIceTicks(0);
        this.setRightClicks(0);
        this.setLeftClicks(0);
    }
    
    public int getWaterTicks() {
        return this.waterTicks;
    }
    
    public void setWaterTicks(final int waterTicks) {
        this.waterTicks = waterTicks;
    }
    
    public int getLastPing() {
        return this.lastPing;
    }
    
    public void setLastPing(final int lastPing) {
        this.lastPing = lastPing;
    }
    
    public long getPacketPosDifference() {
        return this.packetPosDifference;
    }
    
    public void setPacketPosDifference(final long packetPosDifference) {
        this.packetPosDifference = packetPosDifference;
    }
    
    public double getLastScaffoldYaw() {
        return this.lastScaffoldYaw;
    }
    
    public void setLastScaffoldYaw(final double lastScaffoldYaw) {
        this.lastScaffoldYaw = lastScaffoldYaw;
    }
    
    public long getFoodClick() {
        return this.foodClick;
    }
    
    public void setFoodClick(final long foodClick) {
        this.foodClick = foodClick;
    }
    
    public double getHealhBeforeGround() {
        return this.healhBeforeGround;
    }
    
    public void setHealhBeforeGround(final double healhBeforeGround) {
        this.healhBeforeGround = healhBeforeGround;
    }
    
    public double getVeryLastScaffoldYaw() {
        return this.veryLastScaffoldYaw;
    }
    
    public void setVeryLastScaffoldYaw(final double veryLastScaffoldYaw) {
        this.veryLastScaffoldYaw = veryLastScaffoldYaw;
    }
    
    public double getLastYVelocity() {
        return this.lastYVelocity;
    }
    
    public void setLastYVelocity(final double lastYVelocity) {
        this.lastYVelocity = lastYVelocity;
    }
    
    public double getLastScaffoldPitch() {
        return this.lastScaffoldPitch;
    }
    
    public void setLastScaffoldPitch(final double lastScaffoldPitch) {
        this.lastScaffoldPitch = lastScaffoldPitch;
    }
    
    public double getVeryLastScaffoldPitch() {
        return this.veryLastScaffoldPitch;
    }
    
    public void setVeryLastScaffoldPitch(final double veryLastScaffoldPitch) {
        this.veryLastScaffoldPitch = veryLastScaffoldPitch;
    }
    
    public double getLastMotion() {
        return this.lastMotion;
    }
    
    public void setLastMotion(final double lastMotion) {
        this.lastMotion = lastMotion;
    }
    
    public double getLastSpeedDifference() {
        return this.lastSpeedDifference;
    }
    
    public void setLastSpeedDifference(final double lastSpeedDifference) {
        this.lastSpeedDifference = lastSpeedDifference;
    }
    
    public Block getBlockPlaced() {
        return this.blockPlaced;
    }
    
    public void setBlockPlaced(final Block blockPlaced) {
        this.blockPlaced = blockPlaced;
    }
    
    public Block getLastBlockPlaced() {
        return this.lastBlockPlaced;
    }
    
    public void setLastBlockPlaced(final Block lastBlockPlaced) {
        this.lastBlockPlaced = lastBlockPlaced;
    }
    
    public CustomLocation getLastMovePacket(final int i) {
        if (this.last20MovePackets.size() <= i - 1) {
            return null;
        }
        return this.last20MovePackets.get(this.last20MovePackets.size() - i);
    }
    
    public Block getLastVerticalBlock() {
        return this.lastVerticalBlock;
    }
    
    public void setLastVerticalBlock(final Block lastVerticalBlock) {
        this.lastVerticalBlock = lastVerticalBlock;
    }
    
    public long getLastDamage() {
        return this.lastDamage;
    }
    
    public void setLastDamage(final long lastDamage) {
        this.lastDamage = lastDamage;
    }
    
    public int getFluxAACVerbose() {
        return this.fluxAACVerbose;
    }
    
    public void setFluxAACVerbose(final int fluxAACVerbose) {
        this.fluxAACVerbose = fluxAACVerbose;
    }
    
    public double getSlimeDistance() {
        return this.slimeDistance;
    }
    
    public void setSlimeDistance(final double slimeDistance) {
        this.slimeDistance = slimeDistance;
    }
    
    public long getMovePacketAverage() {
        final ArrayList<Long> diffs = new ArrayList<Long>();
        for (int i = 0; i < this.last20MovePackets.size(); ++i) {
            final long FIRST_DIFF = this.last20MovePackets.get(i).getTimeStamp();
            if (this.last20MovePackets.size() != i + 1) {
                diffs.add(this.last20MovePackets.get(i + 1).getTimeStamp() - FIRST_DIFF);
                ++i;
            }
        }
        long average = 0L;
        for (final long g : diffs) {
            average += g;
        }
        return average / this.last20MovePackets.size();
    }
    
    public void addMovePacket(final CustomLocation customLocation) {
        if (this.last20MovePackets.size() >= 20) {
            this.last20MovePackets.remove(0);
        }
        this.last20MovePackets.add(customLocation);
    }
    
    public double getLastDirPosPacket() {
        return this.lastDirPosPacket;
    }
    
    public void setLastDirPosPacket(final double lastDirPosPacket) {
        this.lastDirPosPacket = lastDirPosPacket;
    }
    
    public long getLastRegen() {
        return this.lastRegen;
    }
    
    public void setLastRegen(final long lastRegen) {
        this.lastRegen = lastRegen;
    }
    
    public int getSprintTicks() {
        return this.sprintTicks;
    }
    
    public void setSprintTicks(final int sprintTicks) {
        this.sprintTicks = sprintTicks;
    }
    
    public double getDirectionViolations() {
        return this.directionViolations;
    }
    
    public void setDirectionViolations(final double directionViolations) {
        this.directionViolations = directionViolations;
    }
    
    public boolean placedBlock() {
        return this.placedBlock;
    }
    
    public void setPlacedBlock(final boolean placedBlock) {
        this.placedBlock = placedBlock;
    }
    
    public void addUsePackets() {
        ++this.usePackets;
    }
    
    public void resetUsePackets() {
        this.usePackets = 0;
    }
    
    public long getLastServerKeepAlive() {
        return this.lastServerKeepAlive;
    }
    
    public void setLastServerKeepAlive(final long lastServerKeepAlive) {
        this.lastServerKeepAlive = lastServerKeepAlive;
    }
    
    public long getLastPosPacket() {
        return this.lastPosPacket;
    }
    
    public void setLastPosPacket(final long lastPosPacket) {
        this.lastPosPacket = lastPosPacket;
    }
    
    public long getLastClientKeepAlive() {
        return this.lastClientKeepAlive;
    }
    
    public void setLastClientKeepAlive(final long lastClientKeepAlive) {
        this.lastClientKeepAlive = lastClientKeepAlive;
    }
    
    public int getUsePackets() {
        return this.usePackets;
    }
    
    public int getBlockCancelled() {
        return this.blockCancelled;
    }
    
    public void setBlockCancelled(final int blockCancelled) {
        this.blockCancelled = blockCancelled;
    }
    
    public long getLastPacket() {
        return this.lastPacket;
    }
    
    public void setLastPacket(final long millis) {
        this.lastPacket = millis;
    }
    
    public long getLastVerticalPlace() {
        return this.lastVerticalPlace;
    }
    
    public void setLastVerticalPlace(final long lastVerticalPlace) {
        this.lastVerticalPlace = lastVerticalPlace;
    }
    
    public void addSwingPackets() {
        ++this.swingPackets;
    }
    
    public void resetSwingPackets() {
        this.swingPackets = 0;
    }
    
    public Checks getCheckCancelled() {
        return this.checkCancelled;
    }
    
    public int getSwingPackets() {
        return this.swingPackets;
    }
    
    public int getSpeedTicks() {
        return this.speedTicks;
    }
    
    public void setSpeedTicks(final int speedTicks) {
        this.speedTicks = speedTicks;
    }
    
    public int getWebTicks() {
        return this.webTicks;
    }
    
    public void setWebTicks(final int webTicks) {
        this.webTicks = webTicks;
    }
    
    public double getRealFallDistance() {
        return this.realFallDistance;
    }
    
    public void setRealFallDistance(final double realFallDistance) {
        this.realFallDistance = realFallDistance;
    }
    
    public void setLastMove(final long millis) {
        this.lastMove = millis;
    }
    
    public long getLastMove() {
        return this.lastMove;
    }
    
    public long getLastBlockPlace() {
        return this.lastBlockPlace;
    }
    
    public void setLastBlockPlace(final long lastBlockPlace) {
        this.lastBlockPlace = lastBlockPlace;
    }
    
    public int getFoodSprintTicks() {
        return this.foodSprintTicks;
    }
    
    public void setFoodSprintTicks(final int foodSprintTicks) {
        this.foodSprintTicks = foodSprintTicks;
    }
    
    public Entity getLastHitPlayer() {
        return this.lastHitPlayer;
    }
    
    public void setLastHitPlayer(final Entity lastHitPlayer) {
        this.lastHitPlayer = lastHitPlayer;
    }
    
    public void setLastMoveTime(final long millis) {
        this.lastMoveTime = millis;
    }
    
    public long getLastMoveTime() {
        return this.lastMoveTime;
    }
    
    public long getLastFlightChange() {
        return this.lastFlightChange;
    }
    
    public void setLastFlightChange(final long millis) {
        this.lastFlightChange = millis;
    }
    
    public int getPacketLoss() {
        return this.packetLoss;
    }
    
    public void setPacketLoss(final int packetLoss) {
        this.packetLoss = packetLoss;
    }
    
    public int getLastPackets() {
        return this.lastPackets;
    }
    
    public void setLastPackets(final int lastPackets) {
        this.lastPackets = lastPackets;
    }
    
    public double getPacketsFromLag() {
        return this.packetsFromLag;
    }
    
    public void setPacketsFromLag(final double packetsFromLag) {
        this.packetsFromLag = packetsFromLag;
    }
    
    public int getPosPackets() {
        return this.posPackets;
    }
    
    public void setPosPacket(final int number) {
        this.posPackets = number;
    }
    
    public Checks getLastCheck() {
        return this.lastCheckSetOff;
    }
    
    public void setLastCheck(final Checks check) {
        this.lastCheckSetOff = check;
    }
    
    public int getBlockTicks() {
        return this.blockTicks;
    }
    
    public void setBlockTicks(final int blockTicks) {
        this.blockTicks = blockTicks;
    }
    
    public Location getSetbackLocation() {
        return this.setbackLocation;
    }
    
    public void setSetbackLocation(final Location setbackLocation) {
        this.setbackLocation = setbackLocation;
    }
    
    public long getLastPotionSplash() {
        return this.lastPotionSplash;
    }
    
    public void setLastPotionSplash(final long millis) {
        this.lastPotionSplash = millis;
    }
    
    public int getAirTicks() {
        return this.AirTicks;
    }
    
    public void setAirTicks(final int airTicks) {
        this.AirTicks = airTicks;
    }
    
    public void setLoginMillis(final long millis) {
        this.loginMillis = millis;
    }
    
    public long getLoginMIllis() {
        return this.loginMillis;
    }
    
    public void setDeltaXZ(final double offset) {
        this.deltaXZ = offset;
    }
    
    public double getDeltaXZ() {
        return this.deltaXZ;
    }
    
    public void setDeltaY(final double offset) {
        this.deltaY = offset;
    }
    
    public int getSneakVL() {
        return this.sneakVL;
    }
    
    public void setSneakVL(final int sneakVL) {
        this.sneakVL = sneakVL;
    }
    
    public long getSneak1() {
        return this.sneak1;
    }
    
    public void setSneak1(final long sneak1) {
        this.sneak1 = sneak1;
    }
    
    public long getSneak2() {
        return this.sneak2;
    }
    
    public void setSneak2(final long sneak2) {
        this.sneak2 = sneak2;
    }
    
    public double getDeltaY() {
        return this.deltaY;
    }
    
    public void setLastHeal(final long millis) {
        this.lastHeal = millis;
    }
    
    public long getLastHeal() {
        return this.lastHeal;
    }
    
    public void setTeleported(final long teleported) {
        this.teleported = teleported;
    }
    
    public long isTeleported() {
        return this.teleported;
    }
    
    public long getLastBow() {
        return this.lastBow;
    }
    
    public void setLastBow(final long lastBow) {
        this.lastBow = lastBow;
    }
    
    public long isVelocity() {
        return this.tookVelocity;
    }
    
    public void addToList(final String string) {
        this.logList.add(string);
    }
    
    public void clearList() {
        this.logList.clear();
    }
    
    public ArrayList<String> getList() {
        return this.logList;
    }
    
    public void setTookVelocity(final long took) {
        this.tookVelocity = took;
    }
    
    public void setLastYaw(final double yaw) {
        this.lastYawDifference = yaw;
    }
    
    public double getLastYaw() {
        return this.lastYawDifference;
    }
    
    public void setLastKeepAlive(final long millis) {
        this.lastKeepAlive = millis;
    }
    
    public long lastKeepAlive() {
        return this.lastKeepAlive;
    }
    
    public void setLastPitchDifferenceAimC(final double pitchDifference) {
        this.lastPitchDifferenceAimC = pitchDifference;
    }
    
    public double getLastPitchDifferenceAimC() {
        return this.lastPitchDifferenceAimC;
    }
    
    public int getGroundTicks() {
        return this.GroundTicks;
    }
    
    public boolean isBypass() {
        return this.bypass;
    }
    
    public void setBypass(final boolean bypass) {
        this.bypass = bypass;
    }
    
    public void setCancelled(final Checks check, final CancelType type) {
        if (!Fiona.getAC().whitelisted.contains(this.getPlayer())) {
            if (check == null) {
                this.isCancelled = type;
                this.checkCancelled = null;
                this.setCancelTicks(5);
                return;
            }
            if (Fiona.getAC().getConfig().getBoolean("checks." + check.getName() + ".cancelled")) {
                this.isCancelled = type;
                this.checkCancelled = check;
                this.setCancelTicks(5);
            }
        }
    }
    
    public void setCancelled(final Checks check, final CancelType type, final int ticks) {
        if (check == null) {
            this.isCancelled = type;
            this.checkCancelled = null;
            this.setCancelTicks(ticks);
            return;
        }
        if (Fiona.getAC().getConfig().getBoolean("checks." + check.getName() + ".cancelled")) {
            this.setCancelTicks(ticks);
            this.checkCancelled = check;
            this.isCancelled = type;
        }
    }
    
    public CancelType isCancelled() {
        return this.isCancelled;
    }
    
    public long isHit() {
        return this.isHit;
    }
    
    public void setIsHit(final long isHit) {
        this.isHit = isHit;
    }
    
    public void setGroundTicks(final int groundTicks) {
        this.GroundTicks = groundTicks;
    }
    
    public int getIceTicks() {
        return this.IceTicks;
    }
    
    public void setIceTicks(final int iceTicks) {
        this.IceTicks = iceTicks;
    }
    
    public boolean isHasAlerts() {
        return this.hasAlerts;
    }
    
    public void setHasAlerts(final boolean hasAlerts) {
        this.hasAlerts = hasAlerts;
    }
    
    public int getLeftClicks() {
        return this.leftClicks;
    }
    
    public void setLeftClicks(final int leftClicks) {
        this.leftClicks = leftClicks;
    }
    
    public int getRightClicks() {
        return this.rightClicks;
    }
    
    public void setRightClicks(final int rightClicks) {
        this.rightClicks = rightClicks;
    }
    
    public int getInva() {
        return this.inva;
    }
    
    public void setInva(final int inva) {
        this.inva = inva;
    }
    
    public int getInvb() {
        return this.invb;
    }
    
    public void setInvb(final int invb) {
        this.invb = invb;
    }
    
    public boolean isHasSwung() {
        return this.hasSwung;
    }
    
    public void setHasSwung(final boolean hasSwung) {
        this.hasSwung = hasSwung;
        if (hasSwung) {
            this.lastSwing = System.currentTimeMillis();
        }
    }
    
    public long getLastSwing() {
        return this.lastSwing;
    }
}
