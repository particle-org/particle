package com.particle.util.compress.impl;

import com.particle.util.compress.ICompressTool;
import org.xerial.snappy.Snappy;

import java.io.IOException;

public class SNAPPYTool implements ICompressTool {
    public byte[] compress(byte srcBytes[]) throws IOException {
        return Snappy.compress(srcBytes);
    }

    public byte[] uncompress(byte[] bytes) throws IOException {
        return Snappy.uncompress(bytes);
    }
}
