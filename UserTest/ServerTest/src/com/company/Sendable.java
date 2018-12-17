package com.company;

import java.io.IOException;
import java.io.OutputStream;

/**
 * every class we want to send out we implement this interface
 */

public interface Sendable {
    void write(OutputStream outputStream) throws IOException;
}
