package org.assignment;

import org.apache.commons.lang3.StringUtils;
import org.assignment.compress.Compressor;
import org.assignment.decompress.Decompressor;
import org.assignment.types.OperationType;


public class App {
    public static void main( String[] args ) {
        String operation = System.getProperty("operation");
        String source = System.getProperty("source");
        String target = System.getProperty("target");
        double maxPerFile = Double.parseDouble(System.getProperty("maxPerFile", "0"));

        if(!validate(operation, source, target, maxPerFile)) {
            System.out.println(getErrorMessage());
        } else {
            try {
                //swtich actions based on operation type.
                switch (OperationType.getByOptionValue(operation)) {
                    case COMPRESS:
                        Compressor.compress(source, target, maxPerFile);
                        break;
                    case DECOMPRESS:
                        Decompressor.decompress(source, target);
                        break;
                }
            } catch (Exception ex) {
                System.out.println("Exception while processing: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    //validate if all paramters passed in are correct.
    private static boolean validate(String operation, String source, String target, double maxPerFile) {
        if(StringUtils.isEmpty(operation) || StringUtils.isEmpty(source) || StringUtils.isEmpty(target)) {
            return false;
        }

        if(null == OperationType.getByOptionValue(operation)) {
            return false;
        }

        if(StringUtils.equals("compress", operation) && maxPerFile <= 0) {
            return false;
        }
        return true;
    }

    private static String getErrorMessage() {
        String message = "Incorrect Usage: \n";
        message += "Expected params are: \n";
        message += "\toperation: The operation to be performed. Valid values: compress, decompress\n";
        message += "\tsource: Path to source directory\n";
        message += "\ttarget: Path to target directory\n";
        message += "\tmaxPerFile: Max size of a compressed file part in MB (applicable only for compress operation)\n";

        return message;
    }
}
