package xyz.WorstClient.management;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;

import net.minecraft.client.Minecraft;
import xyz.WorstClient.Client;

import java.io.File;

public class FileManager
{
    private static File dir;
    private static final File ALT;
    private static final File LASTALT;
    
    static {
        final File mcDataDir = Minecraft.getMinecraft().mcDataDir;
        Client.instance.getClass();
        FileManager.dir = new File(mcDataDir, Client.instance.name);
        ALT = getConfigFile("Alts");
        LASTALT = getConfigFile("LastAlt");
    }
    
    public FileManager() {
        super();
    }
    
    public static void loadLastAlt() {
        try {
            if (!FileManager.LASTALT.exists()) {
                final PrintWriter printWriter = new PrintWriter(new FileWriter(FileManager.LASTALT));
                printWriter.println();
                printWriter.close();
            }
            else if (FileManager.LASTALT.exists()) {
                final BufferedReader bufferedReader = new BufferedReader(new FileReader(FileManager.LASTALT));
                String s;
                while ((s = bufferedReader.readLine()) != null) {
                    if (s.contains("\t")) {
                        s = s.replace("\t", "    ");
                    }
                    if (s.contains("    ")) {
                        final String[] parts = s.split("    ");
                        final String[] account = parts[1].split(":");
                        if (account.length == 2) {
                        }
                        else {
                            String pw = account[1];
                            for (int i = 2; i < account.length; ++i) {
                                pw = String.valueOf(pw) + ":" + account[i];
                            }
                        }
                    }
                    else {
                        final String[] account2 = s.split(":");
                        if (account2.length == 1) {
                        }
                        else if (account2.length == 2) {
                        }
                        else {
                            String pw2 = account2[1];
                            for (int j = 2; j < account2.length; ++j) {
                                pw2 = String.valueOf(pw2) + ":" + account2[j];
                            }
                        }
                    }
                }
                bufferedReader.close();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }
    
    public static void saveLastAlt() {
        try {
            final PrintWriter printWriter = new PrintWriter(FileManager.LASTALT);
            printWriter.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static void loadAlts() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(FileManager.ALT));
            if (!FileManager.ALT.exists()) {
                final PrintWriter printWriter = new PrintWriter(new FileWriter(FileManager.ALT));
                printWriter.println();
                printWriter.close();
            }
            else if (FileManager.ALT.exists()) {
                String s;
                while ((s = bufferedReader.readLine()) != null) {
                    if (s.contains("\t")) {
                        s = s.replace("\t", "    ");
                    }
                    if (s.contains("    ")) {
                        final String[] parts = s.split("    ");
                        final String[] account = parts[1].split(":");
                        if (account.length == 2) {
                        }
                        else {
                            String pw = account[1];
                            for (int i = 2; i < account.length; ++i) {
                                pw = String.valueOf(pw) + ":" + account[i];
                            }
                        }
                    }
                    else {
                        final String[] account2 = s.split(":");
                        if (account2.length == 1) {
                        }
                        else if (account2.length == 2) {
                            try {
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            String pw2 = account2[1];
                            for (int j = 2; j < account2.length; ++j) {
                                pw2 = String.valueOf(pw2) + ":" + account2[j];
                            }
                        }
                    }
                }
            }
            bufferedReader.close();
        }
        catch (Exception ex) {}
    }
    
    public static void saveAlts() {
        try {
            System.out.println("skrt");
            final PrintWriter printWriter = new PrintWriter(FileManager.ALT);
            printWriter.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static File getConfigFile(final String name) {
        final File file = new File(FileManager.dir, String.format("%s.txt", name));
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException ex) {}
        }
        return file;
    }
    
    public static void init() {
        if (!FileManager.dir.exists()) {
            FileManager.dir.mkdir();
        }
        loadLastAlt();
        loadAlts();
    }
    
    public static List<String> read(final String file) {
        final List<String> out = new ArrayList<String>();
        try {
            if (!FileManager.dir.exists()) {
                FileManager.dir.mkdir();
            }
            final File f = new File(FileManager.dir, file);
            if (!f.exists()) {
                f.createNewFile();
            }
            Throwable t = null;
            try {
                final FileInputStream fis = new FileInputStream(f);
                try {
                    final InputStreamReader isr = new InputStreamReader(fis);
                    try {
                        final BufferedReader br = new BufferedReader(isr);
                        try {
                            String line = "";
                            while ((line = br.readLine()) != null) {
                                out.add(line);
                            }
                        }
                        finally {
                            if (br != null) {
                                br.close();
                            }
                        }
                        if (isr != null) {
                            isr.close();
                        }
                    }
                    finally {
                        if (t == null) {
                            final Throwable t2 = null;
                            t = t2;
                        }
                        else {
                            final Throwable t2 = null;
                            if (t != t2) {
                                t.addSuppressed(t2);
                            }
                        }
                        if (isr != null) {
                            isr.close();
                        }
                    }
                    if (fis != null) {
                        fis.close();
                        return out;
                    }
                }
                finally {
                    if (t == null) {
                        final Throwable t3 = null;
                        t = t3;
                    }
                    else {
                        final Throwable t3 = null;
                        if (t != t3) {
                            t.addSuppressed(t3);
                        }
                    }
                    if (fis != null) {
                        fis.close();
                    }
                }
            }
            finally {
                if (t == null) {
                    final Throwable t4 = null;
                    t = t4;
                }
                else {
                    final Throwable t4 = null;
                    if (t != t4) {
                        t.addSuppressed(t4);
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }
    
    public static void save(final String file, final String content, final boolean append) {
        try {
            final File f = new File(FileManager.dir, file);
            if (!f.exists()) {
                f.createNewFile();
            }
            Throwable t = null;
            try {
                final FileWriter writer = new FileWriter(f, append);
                try {
                    writer.write(content);
                }
                finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
            }
            finally {
                if (t == null) {
                    final Throwable t2 = null;
                    t = t2;
                }
                else {
                    final Throwable t2 = null;
                    if (t != t2) {
                        t.addSuppressed(t2);
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
