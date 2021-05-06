package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileUtils {

    public static Byte[] readFileBytes(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] fileData = Files.readAllBytes(path);
        Byte[] data = new Byte[fileData.length];
        for (int i = 0; i < fileData.length; i++) {
            data[i] = Byte.valueOf(fileData[i]);
        }
        return data;
    }

    public static String getFileName(String filePath) {
        return new File(filePath).getName();
    }

    public static void writeFile(Byte[] fileData, String filePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File(filePath));
        byte[] data = new byte[fileData.length];
        for (int i = 0; i < fileData.length; i++) {
            data[i] = Byte.valueOf(fileData[i]);
        }
        fos.write(data);
    }

}
