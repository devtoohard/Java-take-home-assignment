package org.assignment.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CompressUtils {

    final static int BUFFER = 1024;

    public static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        System.out.println("Compressing: " + fileToZip.getAbsolutePath());
        if (fileToZip.isDirectory()) {
            //read bytes of file in directory into ZipOutPutStream.
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            //recursion to iterate over all files in directory and subdirectories.
            for (File childFile : children) {
                zipFile(childFile, fileName + File.separator + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
    //iterates through all files and compresses files or directory

    public static void compressFileOrDirectory(final List<File> filesToZip, String destFileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(destFileName);
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for (File fileToZip : filesToZip) {
            zipFile(fileToZip, fileToZip.getName(), zipOut);
        }
        zipOut.close();
        fos.close();

    }

    //created a bigger file and splitting into smaller parts.
    public static void split(String fileName, Double maxSize) throws IOException {
        long maxBytes = (long) (Math.floor(maxSize * 1024 * 1024));
        File f = new File(fileName);
        if (f.length() <= maxBytes) {
            return;
        }
        FileInputStream fis = new FileInputStream(fileName);
        FileOutputStream fos = null;
        byte fileRAW[] = new byte[BUFFER];
        int count = -1;
        long currentBytes = maxBytes;
        int part = 0;
        try {
            //return counts of bytes that have been read.
            while ((count = fis.read(fileRAW, 0, BUFFER)) != -1) {
                if (currentBytes >= maxBytes) {
                    if (fos != null) {
                        fos.close(); // close the old one and create a new one.
                    }
                    fos = new FileOutputStream(fileName + ".p" + part);
                    currentBytes = 0;
                    part += 1;
                }
                currentBytes += count;
                fos.write(fileRAW, 0, count);
            }

        } finally {
            if (fos != null) {
                fos.close();
            }
            fis.close();
            f.delete();
        }

    }
}
