package xyz.WorstClient.command.commands;

import xyz.WorstClient.command.Command;
import xyz.WorstClient.utils.Helper;
import xyz.WorstClient.utils.WebUtils;

import java.io.IOException;

public class HuJi extends Command {
    public HuJi(){super("huji", new String[]{"HuJi"}, "", "huji");}
    @Override
    public String execute(String[] args) {
        String List="";
        try {
            List= WebUtils.get("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (args.length == 0) {
            Helper.sendMessageWithoutPrefix("Name:LiMimgShu, AoneHax");
            Helper.sendMessageWithoutPrefix("Phone_Number:18910800763");
        } else {
            Helper.sendMessage("esu");
        }
        return null;
    }
}
