package net.minecraft.command.server;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServers;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

public class CommandListBans extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getCommandName()
    {
        return "banlist";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return (MinecraftServers.getServer().getConfigurationManager().getBannedIPs().isLanServer() || MinecraftServers.getServer().getConfigurationManager().getBannedPlayers().isLanServer()) && super.canCommandSenderUseCommand(sender);
    }

    /**
     * Gets the usage string for the command.
     */
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.banlist.usage";
    }

    /**
     * Callback when the command is invoked
     */
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length >= 1 && args[0].equalsIgnoreCase("ips"))
        {
            sender.addChatMessage(new ChatComponentTranslation("commands.banlist.ips", new Object[] {Integer.valueOf(MinecraftServers.getServer().getConfigurationManager().getBannedIPs().getKeys().length)}));
            sender.addChatMessage(new ChatComponentText(joinNiceString(MinecraftServers.getServer().getConfigurationManager().getBannedIPs().getKeys())));
        }
        else
        {
            sender.addChatMessage(new ChatComponentTranslation("commands.banlist.players", new Object[] {Integer.valueOf(MinecraftServers.getServer().getConfigurationManager().getBannedPlayers().getKeys().length)}));
            sender.addChatMessage(new ChatComponentText(joinNiceString(MinecraftServers.getServer().getConfigurationManager().getBannedPlayers().getKeys())));
        }
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] {"players", "ips"}): null;
    }
}
