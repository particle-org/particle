package com.particle.util.compress;

import java.io.IOException;

public interface ICompressTool {
    byte[] compress(byte input[]) throws IOException;

    byte[] uncompress(byte[] input) throws IOException;
}
