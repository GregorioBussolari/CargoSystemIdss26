package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import domain.Hold.Coord;

public class HoldManifestReader {

    public static Properties read(String path) {
        Properties p = new Properties();
        try (InputStream in = new FileInputStream(path)) {
            p.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read hold manifest: " + path, e);
        }
        return p;
    }
    
    // 1. Estrae le dimensioni [width, length, D]
    public static int[] extractDimensions(Properties p) {
        int width = Integer.parseInt(p.getProperty("hold.width", "8"));
        int length = Integer.parseInt(p.getProperty("hold.length", "6"));
        int d = Integer.parseInt(p.getProperty("hold.D", "1"));
        return new int[]{width, length, d};
    }

    // Estrae solo i NOMI degli slot dinamici (slot1..slot4), ignorando i valori
    public static List<String> extractSlotNames(Properties p) {
        return p.stringPropertyNames().stream()
                .filter(k -> k.matches("coord\\.slot\\d+"))
                .map(k -> k.substring("coord.".length()))
                .sorted()
                .collect(Collectors.toList());
    }

    // Estrae TUTTE le coordinate (ioport, slot5, slot1..4)
    public static Map<String, Coord> extractCoordinates(Properties p) {
        Map<String, Coord> m = new LinkedHashMap<>();
        for (String key : p.stringPropertyNames()) {
            if (key.startsWith("coord.")) {
                String name = key.substring("coord.".length());
                String[] xy = p.getProperty(key).split(",");
                m.put(name, new Coord(Integer.parseInt(xy[0].trim()),
                                       Integer.parseInt(xy[1].trim())));
            }
        }
        return m;
    }
}
