package cn.backday.config;

import cn.backday.utils.misc.FileUtils;
import net.minecraft.client.Minecraft;

import java.io.File;

public class FileManager {
    private final File backdayDir;
    private final File bConfigDir;
    private final File bConfigFile;

    public FileManager() {
        Minecraft mc = Minecraft.getMinecraft();
        backdayDir = new File(mc.mcDataDir, "Backday");
        bConfigDir = new File(backdayDir, "Configs");
        bConfigFile = new File(bConfigDir, "Default.cfg");

        FileUtils.createDir(backdayDir);
        FileUtils.createDir(bConfigDir);
        FileUtils.createFile(bConfigFile);
    }

    public File getBackdayDir() {
        return backdayDir;
    }

    public File getbConfigDir() {
        return bConfigDir;
    }

    public File getbConfigFile() {
        return bConfigFile;
    }
}
