package com.jalasoft.filestorage.controller.exceptions;

public class InvalidSequence extends Exception {
    private static final String ERROR_MESSAGE = "Filename contains invalid path sequence: '%s'";

    public InvalidSequence(final String sequence) {
        super(String.format(ERROR_MESSAGE, sequence));
    }
}
