package net.minecraft.client.main;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.swing.JOptionPane;

import viamcp.ViaMCP;
import xyz.WorstClient.DiscordRP;
import xyz.WorstClient.utils.HWIDUtils;
import xyz.WorstClient.utils.WebUtils;


public class Main
{
	public static DiscordRP discordRP = new DiscordRP();
	public static void main(String[] p_main_0_)
	{
		try
		{
			ViaMCP.getInstance().start();
			// Only use one of the following
			ViaMCP.getInstance().initAsyncSlider(); // For top left aligned slider
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		discordRP.start();
	ModifyMain.launch(p_main_0_); 
	
	}
}


