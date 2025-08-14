package com.santa;

    import java.io.PrintStream;
    import java.io.OutputStream;

    public class Logger extends PrintStream {
        public static LogLevel level = LogLevel.WARN;
        public Logger(OutputStream out) {
            super(out);
        }

        @Override
        public void println(String s) {
            LogLevel msgLog;
            if (s.indexOf("INFO") != -1) {
                msgLog = LogLevel.INFO;
            }
            else if (s.indexOf("WARN") != -1) {
                msgLog = LogLevel.WARN;
            }
            else if (s.indexOf("ERROR") != -1) {
                msgLog = LogLevel.ERROR;
            }
            else if (s.indexOf("CRITICAL") != -1) {
                msgLog = LogLevel.CRITICAL;
            }
            else {
                msgLog = LogLevel.DEBUG;
            }

            if (msgLog.ordinal() >= level.ordinal()) {
                super.println("CUSTOM_LOG: "  + level + ", " + s);
            }
        }

        // You might need to override other print/println overloads as well
        @Override
        public void print(String s) {
            super.print("CUSTOM_PRINT: " + s);
        }
    }
