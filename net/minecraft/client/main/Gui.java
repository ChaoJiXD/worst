package net.minecraft.client.main;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Window.Type;
import java.awt.Color;
import javax.swing.JPanel;

public class Gui extends JFrame{
	public Gui() {
		setUndecorated(true);
		getContentPane().setBackground(Color.BLACK);
		
		JLabel text = new JLabel("Account verification...");
		text.setForeground(Color.cyan);
		text.setFont(new Font("Verdana", Font.BOLD, 26));
		getContentPane().add(text, BorderLayout.CENTER);
		setBounds(100, 100, 340, 53);
		setVisible(true);
		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setLocation( (int) (width - this.getWidth()) / 2,(int) (height - this.getHeight()) / 2);
	}
	
	
	
}
