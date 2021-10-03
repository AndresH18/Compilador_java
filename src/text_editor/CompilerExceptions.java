package text_editor;

import java.io.IOException;

/**
 * <p>Class containing posible used exceptions </p>
 */
public abstract class CompilerExceptions {
    /**
     * <p>File was not found</p>
     */
    public static final class FileNotFoundException extends java.io.FileNotFoundException {
        /**
         * <p>Empty Constructor</p>
         */
        public FileNotFoundException() {
        }

        /**
         * <p>Constructor, pass String argument</p>
         *
         * @param s exception message
         */
        public FileNotFoundException(String s) {
            super(s);
        }
    }

    /**
     * <p>Unexpected Exceptions</p>
     */
    public static final class UnexpectedException extends IOException {
        /**
         * @param message exception message
         */
        public UnexpectedException(String message) {
            super(message);
        }

        /**
         * @param message exception message
         * @param cause   exception cause
         */
        public UnexpectedException(String message, Throwable cause) {
            super(message, cause);
        }

        /**
         * @param cause exception cause
         */
        public UnexpectedException(Throwable cause) {
            super(cause);
        }
    }

    /**
     * <p>Thrown to indicate that a symbol or character is not valid.</p>
     */
    public static final class IllegalSymbolException extends IllegalArgumentException {
        /**
         * <p>Constructs an {@link IllegalSymbolException} with the specified detail message.</p>
         *
         * @param s the detail message
         */
        public IllegalSymbolException(String s) {
            super(s);
        }

        /**
         * <p>Constructs and {@link IllegalSymbolException} with no detailed messaged.</p>
         */
        public IllegalSymbolException() {
        }
    }
}
