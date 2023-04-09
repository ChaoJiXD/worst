package xyz.WorstClient;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.callbacks.ReadyCallback;

import static net.minecraft.client.main.Main.discordRP;

public class DiscordRP {

    private boolean running = true;
    private  long created = 0;
    public void start() {
        this.created = System.currentTimeMillis();

        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(new ReadyCallback() {
            @Override
            public void apply(DiscordUser discordUser) {
                System.out.println("welcome" + discordUser.username + "m" + discordUser.discriminator + "!");
                update("Booting up", "");
            }
        }).build();

        DiscordRPC.discordInitialize("1094422807686959164", handlers, running);
        new Thread("Discord RPC Callback"){
            @Override
            public void run() {
                while (running) {
                    DiscordRPC.discordRunCallbacks();
                }
            }
        }.start();

    }
    public void shutdown() {
        running = false;
        DiscordRPC.discordShutdown();


    }
    public void update(String firstLine, String secondLine) {
        DiscordRichPresence.Builder b = new DiscordRichPresence.Builder(secondLine);
        b.setBigImage("logo", "");
                b.setDetails(firstLine);
                b.setStartTimestamps(created);
                DiscordRPC.discordUpdatePresence(b.build());


    }
}
