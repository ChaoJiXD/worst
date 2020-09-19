package net.minecraft.realm;

public class RealmsServerPing
{
    public volatile String nrOfPlayers = "0";
    public volatile long lastPingSnapshot = 0L;
    public volatile String playerList = "";
}
