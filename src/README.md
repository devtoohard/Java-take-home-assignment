## Agoda File Compression Assignment (JAVA)

This program compresses files and folders into a set of compressed files such that of each
compressed file doesn’t exceed a maximum size. This program can be used for
decompressing the files that it has generated earlier. The output of the decompression should be
identical to the original input of the compression process.

### Development

I wrote this Java program using IntelliJ IDE and used some resources to help me write the program.
_BONUS_ I managed to implement the compression process to run in parallel using ThreadPoolExecutor in Java 8.
https://howtodoinjava.com/java/multi-threading/java-thread-pool-executor-example/
I have wrote several comments in the code, but please feel free to reach out to me if you have any doubts, I am very happy to clarify.

<br />

### Useful Resources that I Used

https://docs.oracle.com/javase/8/docs/api/java/util/zip/ZipEntry.html
https://docs.oracle.com/javase/7/docs/api/java/io/FileInputStream.html
https://docs.oracle.com/javase/7/docs/api/java/io/FileOutputStream.html
https://www.tutorialspoint.com/javazip/javazip_zipoutputstream_write.htm
https://www.tutorialspoint.com/javazip/index.htm
https://www.baeldung.com/java-path
https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Future.html

### How to Use

If you are using IntelliJ IDE, pass in the following arguments in VMOptions for compression under Edit Configurations when running the Main method in the program.

e.g.

```
-Doperation="compress" -Dsource="C:\Users\User\Pictures\test1" -Dtarget="C:\Users\User\Pictures\test2" -DmaxPerFile=100
```

Likewise for decompression, pass in the following arguments in VMOptions for decompression under Edit Configurations when running the Main method in the program.

e.g.

```
-Doperation="decompress" -Dsource="C:\Users\User\Pictures\test2" -Dtarget="C:\Users\User\Pictures\test3"
```

<br />

### How it works

Compression:
Use recursion to iterate over all the files in a directory and compresses them. Makes sure to check that for each file it does not exceed the maximum bytes limit.
For the threads to run in parallel, I created used ThreadPoolExecutor and in this case I passed in DEFAULT_THREAD = 2. I implemented a Future Arraylist that waits for the threads in the threadpool to finish executing then appends the result to it once the computation is finished, which in this case would be the split up Zipped files after compression.

Decompression:
Wrote a regex pattern to look for zipped file names.
Iterate over all the zipped files in the source directory and then merges them into a normal file(those with the same names) - wrote a comparator to merge the file in the correct sequence when merging. Then we delete the tempFile.
<br />

### Testing

I have written Unit Tests in JUnit in the tests folder. Please feel free to put in your own folders and test them. Just run AppTest under test folder. My code checks for the size of each and every file as well as the content for both directories to make sure that the output is the same after compression and decompression.
