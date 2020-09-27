package org.assignment;

import org.assignment.compress.Compressor;
import org.assignment.decompress.Decompressor;
import org.assignment.Utils.TestUtils;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */

    public static final double MAXFILESIZE = 3.0;

    final String inputDirectoryForCompression = "src/test/resources/test1";
    final String outputDirectoryForCompression = "src/test/resources/test2";
    final String outputDirectoryForDecompression = "src/test/resources/test3";


    @Test
    public void UnitTest() throws Exception {
        Compressor compressor = new Compressor();
        Decompressor decompressor = new Decompressor();
        compressor.compress(inputDirectoryForCompression,outputDirectoryForCompression,MAXFILESIZE);
        decompressor.decompress(outputDirectoryForCompression,outputDirectoryForDecompression);
        assertTrue( "Files in Directories are Different!",ValidateContentOfDirectory());
        assertTrue( "Files size different!",fileSizeCheck());
    }

    private boolean ValidateContentOfDirectory(){
        List<File> inputFiles = TestUtils.fetchAllFiles(inputDirectoryForCompression);
        List<File> outputFiles = TestUtils.fetchAllFiles(outputDirectoryForDecompression);
        if (inputFiles.size() != outputFiles.size()) {
            return false;
        }
        for (int i = 0; i < inputFiles.size(); i++) {
            if (inputFiles.get(i).length() != outputFiles.get(i).length()) {
                return false;
            }
        }
        return true;
    }

    private boolean fileSizeCheck() {
        return TestUtils.fetchAllFiles(outputDirectoryForCompression)
                .stream().noneMatch(file -> file.length() > (MAXFILESIZE * 1024 * 1024D));
    }



}
