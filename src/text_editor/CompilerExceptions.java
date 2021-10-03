package text_editor;

import java.io.IOException;

/**
 * <p>Class containing posible used exceptions </p>
 */
public abstract class CompilerExceptions {
    /**
     * <p>Exception thrown to indicate that a file was not found.</p>
     */
    public static final class FileNotFoundException extends java.io.FileNotFoundException {
        /**
         * <p>Constructs an instance without a detailed message</p>
         */
        public FileNotFoundException() {
        }

        /**
         * <p>Constructs an instance with a detailed message</p>
         *
         * @param s the detailed message
         */
        public FileNotFoundException(String s) {
            super(s);
        }
    }

    /**
     * <p>Exception thrown when there are unexpected events.</p>
     */
    public static final class UnexpectedException extends IOException {
        /**
         * <p>Constructs an instance with a detailed Message.</p>
         *
         * @param message exception message
         */
        public UnexpectedException(String message) {
            super(message);
        }

        /**
         * <p>Constructs an instance with a detailed message and a cause</p>
         *
         * @param message the detailed message
         * @param cause   the cause
         */
        public UnexpectedException(String message, Throwable cause) {
            super(message, cause);
        }

        /**
         * <p>Constructs an instance without a detailed messaged but with a cause</p>
         *
         * @param cause the cause
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
         * <p>Constructs a {@link IllegalSymbolException} with no detailed messaged.</p>
         */
        public IllegalSymbolException() {
        }
    }
}
