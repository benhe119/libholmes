// This file is part of libholmes.
// Copyright 2018 Graham Shaw.
// Distribution and modification are permitted within the terms of the
// GNU General Public License (version 3 or any later version).

package org.libholmes;

/** An exception class to represent an unrecoverable error while attempting
 * to parse an artefact.
 */
public class ParseException extends Exception {
    /** Construct ParseException (no message, no cause). */
    public ParseException() { }

    /** Construct ParseException (message only).
     * @param message a message describing the cause of the exception.
     */
    public ParseException(String message) {
        super(message);
    }

    /** Construct ParseException (cause only).
     * @param cause the cause of the exception
     */
    public ParseException(Throwable cause) {
        super(cause);
    }

    /** Construct ParseException (message and cause).
     * @param message a message describing the cause of the exception.
     * @param cause the cause of the exception
     */
    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
