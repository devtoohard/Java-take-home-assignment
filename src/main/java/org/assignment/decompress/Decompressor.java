package org.assignment.decompress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.assignment.Utils.DecompressUtils;

public class Decompressor {


    private static String pattern = ".*\\.[Zz][Ii][Pp]\\.p(\\d+)";
    //regex pattern to match for .ZIP or .zip files followed by number.
    private static Pattern r = Pattern.compile(pattern);

    public static void decompress(final String sourceDir, final String targetDir) throws Exception {
        File targetDirFile = new File(targetDir);
        File source = new File(sourceDir);

        String sourceZipFiles[] = source.list();

        Map<String, List<String>> fileMap = new HashMap<>();

        for(String file: sourceZipFiles) {
            String parts[] = file.split("\\.zip");
            //split files created in zip
            if(!fileMap.containsKey(parts[0])) {
                //whatever file names started with similar letters should be merged together.
                fileMap.put(parts[0], new ArrayList<>());
            }

            fileMap.get(parts[0]).add(file);
        }

        //
        for(List<String> filesList: fileMap.values()) {
            filesList.sort(new Comparator<String>() {
                int getFileSeq(String name) {

                    Matcher m = r.matcher(name);
                    int part = 0;
                    if (m.find()) {
                        //extract part number
                        part = Integer.parseInt(m.group(1));
                    }
                    return part;
                }
//return which string is larger which string is smaller.
                @Override
                public int compare(String o1, String o2) {

                    return getFileSeq(o1) - getFileSeq(o2);
                }
            });

            String tempFile = DecompressUtils.merge(filesList, sourceDir);

            DecompressUtils.decompressZip(tempFile, targetDirFile);

            File f = new File(tempFile); //deletion of temp file after decompression.
            f.delete();
        }
    }

    //decompression - merge files to get the original zip file.
    //take the parts and merge it into a bigger zip file and then decompress it into a normal file.



}
