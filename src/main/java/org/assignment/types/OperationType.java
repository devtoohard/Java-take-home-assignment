package org.assignment.types;

import org.apache.commons.lang3.StringUtils;

//constants class
public enum OperationType {
    COMPRESS("compress"),
    DECOMPRESS("decompress");

    private String optionValue;

    OperationType(String optionValue) {
        this.optionValue = optionValue;
    }

    public String getOptionValue() {
        return optionValue;
    }

    //return operation type depending on what optionValue was passed in as parameter.
    public static OperationType getByOptionValue(String optionValue) {
        for(OperationType operationType: OperationType.values()) {
            if(StringUtils.equals(optionValue, operationType.getOptionValue())) {
                return operationType;
            }
        }
        return null;
    }
}
