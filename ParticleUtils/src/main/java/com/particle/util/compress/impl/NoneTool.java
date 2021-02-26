package com.particle.util.compress.impl;

import com.particle.util.compress.ICompressTool;

import java.io.IOException;

public class NoneTool implements ICompressTool {
    @Override
    public byte[] compress(byte[] input) throws IOException {
        return input;
    }

    @Override
    public byte[] uncompress(byte[] input) throws IOException {
        return input;
    }
}
