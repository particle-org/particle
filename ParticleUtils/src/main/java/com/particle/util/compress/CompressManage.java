package com.particle.util.compress;

import com.particle.util.compress.impl.*;

import java.io.IOException;

public class CompressManage {
    private ICompressTool defaultCompressTool;
    private CompressType defaultCompressType;

    private static CompressManage instance = new CompressManage(CompressType.LZ4JNI);

    public static CompressManage getInstance() {
        return CompressManage.instance;
    }

    public CompressManage(CompressType compressType) {
        this.defaultCompressType = compressType;
        switch (compressType) {
            case LZ4:
                defaultCompressTool = new LZ4Tool();
                break;
            case LZ4JNI:
                defaultCompressTool = new LZ4JNITool();
                break;
            case SNAPPY:
                defaultCompressTool = new SNAPPYTool();
                break;
            case DEFLATE:
                defaultCompressTool = new DeflaterTool();
                break;
            default:
                defaultCompressTool = new NoneTool();
        }
    }

    public byte[] compress(byte input[]) throws IOException {
        return defaultCompressTool.compress(input);
    }

    public byte[] uncompress(byte[] input, int type) throws IOException {
        if (type == defaultCompressType.value()) {
            return defaultCompressTool.uncompress(input);
        } else {
            switch (CompressType.valueOf(type)) {
                case LZ4:
                    return new LZ4Tool().uncompress(input);
                case LZ4JNI:
                    return new LZ4JNITool().uncompress(input);
                case SNAPPY:
                    return new SNAPPYTool().uncompress(input);
                case DEFLATE:
                    return new DeflaterTool().uncompress(input);
            }
        }

        return null;
    }

    public CompressType getDefaultCompressType() {
        return defaultCompressType;
    }
}
