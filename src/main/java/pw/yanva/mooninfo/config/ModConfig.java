package pw.yanva.mooninfo.config;

import com.mojang.datafixers.util.Pair;
import pw.yanva.mooninfo.MoonInfoMod;

import java.io.*;
import java.util.Scanner;

public class ModConfig {

    String filename;
    public int hudPosition;

    public ModConfig() {
        this.filename = MoonInfoMod.MOD_ID + "_config.properties";
    }

    private void saveConfig(String config) throws IOException {
        FileWriter configFile = new FileWriter(this.filename);
        configFile.write(config);
        configFile.close();
    }

    private Pair<String, Integer> loadConfig() throws IOException {
        FileReader configFile = new FileReader(this.filename);
        Scanner scanner = new Scanner(configFile);
        String config = scanner.nextLine();
        configFile.close();
        return new Pair<String, Integer>(config.split("=")[0], Integer.parseInt(config.split("=")[1]));
    }
    public void load() throws IOException  {
        File file = new File(this.filename);
        if (file.createNewFile()) {
            hudPosition = 4;
            this.saveConfig("hud_position=4");
        } else {
            hudPosition = this.loadConfig().getSecond();
        }
    }

    public void save(int position) throws IOException {
        File file = new File(this.filename);
        file.createNewFile();
        hudPosition = position;
        this.saveConfig("hud_position="+position);
    }
}