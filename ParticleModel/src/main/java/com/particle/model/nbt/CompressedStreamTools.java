package com.particle.model.nbt;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressedStreamTools {
    /**
     * Load the gzipped compound from the inputstream.
     */
    public static NBTTagCompound readCompressed(InputStream is) throws IOException {
        DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(is)));
        NBTTagCompound nbttagcompound;

        try {
            nbttagcompound = read(datainputstream, NBTSizeTracker.INFINITE);
        } finally {
            datainputstream.close();
        }

        return nbttagcompound;
    }

    /**
     * Write the compound, gzipped, to the outputstream.
     */
    public static void writeCompressed(NBTTagCompound p_74799_0_, OutputStream outputStream) throws IOException {
        DataOutputStream dataoutputstream = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(outputStream)));

        try {
            write(p_74799_0_, dataoutputstream);
        } finally {
            dataoutputstream.close();
        }
    }

    public static void safeWrite(NBTTagCompound p_74793_0_, File p_74793_1_) throws IOException {
        File file1 = new File(p_74793_1_.getAbsolutePath() + "_tmp");

        if (file1.exists()) {
            file1.delete();
        }

        write(p_74793_0_, file1);

        if (p_74793_1_.exists()) {
            p_74793_1_.delete();
        }

        if (p_74793_1_.exists()) {
            throw new IOException("Failed to delete " + p_74793_1_);
        } else {
            file1.renameTo(p_74793_1_);
        }
    }

    /**
     * Reads from a CompressedStream.
     */
    public static NBTTagCompound read(DataInputStream inputStream) throws IOException {
        /**
         * Reads the given DataInput, constructs, and returns an NBTTagCompound with the data from the DataInput
         */
        return read(inputStream, NBTSizeTracker.INFINITE);
    }

    /**
     * Reads the given DataInput, constructs, and returns an NBTTagCompound with the data from the DataInput
     */
    public static NBTTagCompound read(DataInput dataInput, NBTSizeTracker nbtSizeTracker) throws IOException {
        NBTBase nbtbase = parseNBT(dataInput, 0, nbtSizeTracker);

        if (nbtbase instanceof NBTTagCompound) {
            return (NBTTagCompound) nbtbase;
        } else {
            throw new IOException("Root tag must be a named compound tag");
        }
    }

    public static void write(NBTTagCompound nbtTagCompound, DataOutput dataOutput) throws IOException {
        writeTag(nbtTagCompound, dataOutput);
    }

    public static void writeTag(NBTBase nbtBase, DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(nbtBase.getId());

        if (nbtBase.getId() != 0) {
            dataOutput.writeUTF("");
            nbtBase.write(dataOutput);
        }
    }

    private static NBTBase parseNBT(DataInput dataInput, int depth, NBTSizeTracker nbtSizeTracker) throws IOException {
        byte id = dataInput.readByte();
        nbtSizeTracker.read(8); // Forge: Count everything!

        if (id == 0) {
            return new NBTTagEnd();
        } else {
            NBTSizeTracker.readUTF(nbtSizeTracker, dataInput.readUTF()); //Forge: Count this string.
            nbtSizeTracker.read(32); //Forge: 4 extra bytes for the object allocation.
            NBTBase nbtbase = NBTBase.createNewByType(id);

            nbtbase.read(dataInput, depth, nbtSizeTracker);
            return nbtbase;
        }
    }

    public static void write(NBTTagCompound p_74795_0_, File p_74795_1_) throws IOException {
        DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(p_74795_1_));

        try {
            write(p_74795_0_, dataoutputstream);
        } finally {
            dataoutputstream.close();
        }
    }

    public static NBTTagCompound read(File p_74797_0_) throws IOException {
        if (!p_74797_0_.exists()) {
            return null;
        } else {
            DataInputStream datainputstream = new DataInputStream(new FileInputStream(p_74797_0_));
            NBTTagCompound nbttagcompound;

            try {
                nbttagcompound = read(datainputstream, NBTSizeTracker.INFINITE);
            } finally {
                datainputstream.close();
            }

            return nbttagcompound;
        }
    }
}