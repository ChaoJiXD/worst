/*    */ package xyz.WorstClient.utils;
/*    */ 

/*    */ import java.awt.Color;
/*    */ import java.awt.Font;
/*    */ import java.io.InputStream;
/*    */ import java.util.ArrayList;
/*    */ import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ClientUtil

/*    */ {
/* 20 */   INSTANCE;
/*    */   
/*    */   ClientUtil() {

/*    */   } 
/*
/*    */   

/*    */   
/*    */   public static int reAlpha(int color, float alpha) {
/* 72 */     Color c = new Color(color);
/* 73 */     float r = 0.003921569F * c.getRed();
/* 74 */     float g = 0.003921569F * c.getGreen();
/* 75 */     float b = 0.003921569F * c.getBlue();
/* 76 */     return (new Color(r, g, b, alpha)).getRGB();
/*    */   }
/*    */ }


/* Location:              X:\MobileFile\external\External.jar!\com\enjoytheba\\utils\ClientUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */