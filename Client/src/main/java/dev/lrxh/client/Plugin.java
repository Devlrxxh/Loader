package dev.lrxh.client;

import lombok.Getter;

@Getter
public class Plugin {
    private final String mainClass;
    private final byte[] bytes;
    private final int length;

    public Plugin(String mainClass, byte[] bytes) {
        this.mainClass = mainClass;
        this.bytes = bytes;
        this.length = bytes.length;
    }
}
