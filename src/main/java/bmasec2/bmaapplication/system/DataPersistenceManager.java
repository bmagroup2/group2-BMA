package bmasec2.bmaapplication.system;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataPersistenceManager {

    private static final String DATA_DIR = "data";

    static {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static <T extends Serializable> void saveObjects(List<T> objects, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_DIR + File.separator + filename))) {
            oos.writeObject(objects);
            System.out.println("Objects saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving objects to " + filename + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static <T extends Serializable> List<T> loadObjects(String filename) {
        File file = new File(DATA_DIR + File.separator + filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<T> objects = (List<T>) ois.readObject();
            System.out.println("Objects loaded from " + filename);
            return objects;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading objects from " + filename + ": " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}