package org.assignment.compress;

import org.apache.commons.collections4.ListUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.assignment.Utils.CompressUtils;

public class Compressor {

    final static int DEFAULT_THREAD = 2;

    public static void compress(final String sourcePath, final String target, Double maxPerFile) throws Exception {
        compress(sourcePath, target, maxPerFile, DEFAULT_THREAD);
    }

    //parallelism
    public static void compress(final String sourcePath, final String target, Double maxPerFile, int threads) throws Exception {
       //take one of the N threads and start execution. Threadpool takes care of assinging of jobs.
        //https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ThreadPoolExecutor.html
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);
        File sourceDir = new File(sourcePath);
        if (!sourceDir.isDirectory()) {
            throw new Exception("Source path is not a directory");
        }
        List<File> fileList = Arrays.asList(sourceDir.listFiles()); //return lists of all files.may represent file or directory.
        int n = (int) Math.ceil(fileList.size() / threads); //split the num of files by num of threads.
        if(n == 0){
            n = 1;
        }
//e.g. 5 jobs per thread executed in parallel.
        List<List<File>> fList = ListUtils.partition(fileList, n);

        List<Future> futures = new ArrayList<>();
        for (int i = 0; i < fList.size(); i++) {
            int tCount = i;
            //Submit jobs to threadpool
            //lambda function. creeate instance of runnable and running it.
            //one threads will pick this up and execute in parallel. Asynchronous.
            //Future - returns a promise, maintain a list of futures
            //each job storing a future promise.
            futures.add(executor.submit(() -> {
                        try {
                            String destFileName = target + "/" + sourceDir.getName() + tCount + ".zip";
                            CompressUtils.compressFileOrDirectory(fList.get(tCount), destFileName);
                            CompressUtils.split(destFileName, maxPerFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    })
            );
        }

        //Wait for threads to complete - then this loop will exit.
        for(int i =0 ;i<fList.size(); i++) {
            //blocking, block the program. check if first thread is complete or not, then 2nd, then 3rd thread.
            //if 1st one completed, wait for the others
            futures.get(i);
        }
        //once all threads have finished - Shutdown threadpool
        executor.shutdown();

    }
}
