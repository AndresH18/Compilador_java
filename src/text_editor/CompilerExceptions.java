package text_editor;

import java.io.IOException;

public class CompilerExceptions {
    public static final class FileNotFoundException extends java.io.FileNotFoundException {
        public FileNotFoundException() {
        }

        public FileNotFoundException(String s) {
            super(s);
        }
    }

    public static final class UnexpectedException extends IOException {

        public UnexpectedException(String message) {
            super(message);
        }

        public UnexpectedException(String message, Throwable cause) {
            super(message, cause);
        }

        public UnexpectedException(Throwable cause) {
            super(cause);
        }
    }
}
