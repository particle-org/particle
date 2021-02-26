package com.particle.model.nbt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class NBTToByteArray {

    private static final Logger log = LoggerFactory.getLogger(NBTToJsonObject.class);

    public static byte[] convertToByteArray(NBTBase tag, boolean isNetwork) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataOutput dataOutput = new LittleEndianDataOutputStream2(outputStream, isNetwork);

        CompressedStreamTools.writeTag(tag, dataOutput);

        return outputStream.toByteArray();
    }

    public static NBTBase convertToTag(byte[] array, boolean isNetwork) throws IOException {
        if (array == null || array.length == 0) {
            return null;
        }
        return CompressedStreamTools.read(new LittleEndianDataInputStream2(new ByteArrayInputStream(array), isNetwork), NBTSizeTracker.INFINITE);
    }

    public static byte[] convertToByteArrayWithBigEndian(NBTBase tag) throws IOException {
        if (tag instanceof NBTTagCompound) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            DataOutput dataOutput = new DataOutputStream(outputStream);

            CompressedStreamTools.write((NBTTagCompound) tag, dataOutput);

            return outputStream.toByteArray();
        } else {
            return null;
        }
    }

    public static NBTBase convertToTagWithBigEndian(byte[] array) throws IOException {
        if (array == null || array.length == 0) {
            return null;
        }
        return CompressedStreamTools.read(new DataInputStream(new ByteArrayInputStream(array)), NBTSizeTracker.INFINITE);
    }
}
