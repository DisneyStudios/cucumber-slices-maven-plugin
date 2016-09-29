package com.disney.studio.cucumber.slices.plugin.utils

class StandardOutLogger {
    static info(String message) {
        sendMessageToStdOut('[INFO] ' + message)
    }

    static warn(String message) {
        sendMessageToStdOut('[WARNING] ' + message)
    }

    private static void sendMessageToStdOut(String message) {
        println message
    }
}
