package app;


/**The code in this file is derived from www.codejava.net*/

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class UnzipUtility {

    private static final int BUFFER_SIZE = 4096;
    private static String UNZIP_DESTINATION = "unzipped_cache";


    public static List<File> getFileListFromZip(String zipFilePath) throws IOException {
        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            return Collections.list(zipFile.entries())
                    .stream()
                    .map(o -> new File(o.getName()))
                    .collect(Collectors.toList());
        }
    }

    public static List<ZipEntry> getEntriesFromZip(String zipFilePath) throws IOException {
        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            return Collections.list(zipFile.entries())
                    .stream()
                    .collect(Collectors.toList());
        }
    }

    //fileName ex: Server.java
    public static File unzipFileFromZip(String zipFilePath, String fileName) throws IOException {
        File destDir = new File(UNZIP_DESTINATION);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        String filePath = null;
        // iterates over entries in the zip file
        while (entry != null) {
            String entrySimpleName = new File(entry.getName()).getName();
            if (entrySimpleName.equals(fileName)) {
                filePath = UNZIP_DESTINATION + File.separator + entrySimpleName;
                break;
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        assert (filePath != null);
        extractFile(zipIn, filePath);
        zipIn.close();
        return new File(filePath);
    }


    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}