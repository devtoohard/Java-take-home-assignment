package org.assignment.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DecompressUtils {
    final static int BUFFER = 102400; // 100KB

    //merge the decompressed files that were split.
    public static String merge(List<String> files, String dir) throws IOException {
        byte fileRAW[] = new byte[BUFFER];
        String tempFile = dir + File.separator + "temp.zip";
        int count = -1;
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            for (String file : files) {
                try (FileInputStream fis = new FileInputStream(dir + File.separator + file)) {
                    while ((count = fis.read(fileRAW, 0, BUFFER)) != -1) {
                        fos.write(fileRAW, 0, count);
                    }
                }
            }
        }
        return tempFile;
    }

    //iterates through all zip file and decompresses them by createing a new file.
    public static void decompressZip(final String fileZip, final File destDir) throws IOException {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            //create a new directory - need not write anything just create a directory.
            if (zipEntry.isDirectory()) {
                newFile.mkdirs();
            } else {
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                //read maximum buffer size. returns how many bytes have been written. if no bytes read returns -1;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }


    //put new file in destination directory.
    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }


}
