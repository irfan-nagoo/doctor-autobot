package org.acme.autobot.exception;

/**
 * @author irfan.nagoo
 */
public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(String msg) {
        super(msg);
    }
}
