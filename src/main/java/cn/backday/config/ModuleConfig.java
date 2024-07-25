package cn.backday.config;

import cn.backday.Client;
import cn.backday.module.Module;
import cn.backday.value.Value;
import cn.backday.value.impl.BoolValue;
import cn.backday.value.impl.FloatValue;
import cn.backday.value.impl.IntValue;
import cn.backday.value.impl.ListValue;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ModuleConfig {
    private final ArrayList<File> configs = new ArrayList<>();

    private int prevConfigs;
    private double scrollY;

    public ModuleConfig() {
        this.loadConfigs();
        this.load();
    }

    public void loadConfigs() {
        if (prevConfigs != Objects.requireNonNull(Client.INSTANCE.getFileManager().getbConfigDir().listFiles()).length) {

            prevConfigs = Objects.requireNonNull(Client.INSTANCE.getFileManager().getbConfigDir().listFiles()).length;

            configs.clear();

            scrollY = 0;

            FilenameFilter filter = (file, str) -> str.endsWith("cfg");

            File[] fileArray = Client.INSTANCE.getFileManager().getbConfigDir().listFiles(filter);

            if (fileArray != null) {
                Collections.addAll(configs, fileArray);
            }
        }
    }

    public void save(File file) {
        ArrayList<String> toSave = new ArrayList<>();

        for (Module module : Client.INSTANCE.getModuleManager().getModules()) {
            toSave.add("ModuleName:" + module.getModuleName() + ":" + module.getToggled() + ":" + module.getKeybind());
            // toSave.add("ModulePos:");
        }

        for (Module module : Client.INSTANCE.getModuleManager().getModules()) {
            if (!module.getValues().isEmpty()) {
                for (Value<?> value : module.getValues()) {
                    toSave.add("SET:" + module.getModuleName() + ":" + value.getName() + ":" + value.get());
                }
            }
        }

        try {
            PrintWriter pw = new PrintWriter(file);
            for (String str : toSave) {
                pw.println(str);
            }
            pw.close();
        } catch (FileNotFoundException e) {
            // Don't give crackers clues...
            if (Client.INSTANCE.isDev())
                throw new RuntimeException(e);
        }
    }

    public void load(File file) {
        ArrayList<String> lines = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            // Don't give crackers clues...
            if (Client.INSTANCE.isDev())
                throw new RuntimeException(e);
        }

        try {
            for (String s : lines) {

                String[] args = s.split(":");

                if (s.toLowerCase().startsWith("modulename:")) {
                    Module m = Client.INSTANCE.getModuleManager().getModule(args[1]);
                    if (m != null) {
                        m.toggled = Boolean.parseBoolean(args[2]);
                        // Load config is later than init modules
                        Client.INSTANCE.getModuleManager().setKeybind(m, Integer.parseInt(args[3]));
                    }
                } else if (s.toLowerCase().startsWith("set:")) {
                    Module m = Client.INSTANCE.getModuleManager().getModule(args[1]);
                    if (m != null) {
                        Value<?> set = m.getValueByName(args[2]);
                        if (set != null) {
                            if (set instanceof BoolValue) {
                                ((BoolValue) set).set(Boolean.parseBoolean(args[3]));
                            }
                            if (set instanceof ListValue) {
                                ((ListValue) set).set(args[3]);
                            }
                            if (set instanceof FloatValue) {
                                ((FloatValue) set).set(Float.parseFloat(args[3]));
                            }
                            if (set instanceof IntValue) {
                                ((IntValue) set).set(Integer.parseInt(args[3]));
                            }
                        }
                    }
                    // PlayerUtils.tellPlayer("args[1]: " + args[1] + " args[2]: " + args[2] + " args[3]: " + args[3]);
                }
            }
        } catch (Exception exception) {
            if (Client.INSTANCE.isDev()) {
                throw new RuntimeException(exception);
            }
        }
    }

    public void save() {
        this.save(Client.INSTANCE.getFileManager().getbConfigFile());
    }

    public void load() {
        this.load(Client.INSTANCE.getFileManager().getbConfigFile());
    }

    public ArrayList<File> getConfigs() {
        return configs;
    }

    public double getScrollY() {
        return scrollY;
    }

    public void setScrollY(double scrollY) {
        this.scrollY = scrollY;
    }
}
