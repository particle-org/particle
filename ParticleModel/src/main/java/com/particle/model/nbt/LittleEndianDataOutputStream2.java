/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.particle.model.nbt;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Longs;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

@Beta
@GwtIncompatible
public final class LittleEndianDataOutputStream2 extends FilterOutputStream implements DataOutput {

    private boolean isNetwork = false;

    private static final BigInteger UNSIGNED_LONG_MAX_VALUE = new BigInteger("FFFFFFFFFFFFFFFF", 16);

    /**
     * Creates a {@code LittleEndianDataOutputStream} that wraps the given stream.
     *
     * @param out the stream to delegate to
     */
    public LittleEndianDataOutputStream2(OutputStream out) {
        super(new DataOutputStream(Preconditions.checkNotNull(out)));
    }

    public LittleEndianDataOutputStream2(OutputStream out, boolean isNetwork) {
        super(new DataOutputStream(Preconditions.checkNotNull(out)));

        this.isNetwork = isNetwork;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        // Override slow FilterOutputStream impl
        out.write(b, off, len);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        ((DataOutputStream) out).writeBoolean(v);
    }

    @Override
    public void writeByte(int v) throws IOException {
        ((DataOutputStream) out).writeByte(v);
    }

    /**
     * @deprecated The semantics of {@code writeBytes(String s)} are considered dangerous. Please use
     * {@link #writeUTF(String s)}, {@link #writeChars(String s)} or another write method instead.
     */
    @Deprecated
    @Override
    public void writeBytes(String s) throws IOException {
        ((DataOutputStream) out).writeBytes(s);
    }

    /**
     * Writes a char as specified by {@link DataOutputStream#writeChar(int)}, except using
     * little-endian byte order.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void writeChar(int v) throws IOException {
        writeShort(v);
    }

    /**
     * Writes a {@code String} as specified by {@link DataOutputStream#writeChars(String)}, except
     * each character is written using little-endian byte order.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void writeChars(String s) throws IOException {
        for (int i = 0; i < s.length(); i++) {
            writeChar(s.charAt(i));
        }
    }

    /**
     * Writes a {@code double} as specified by {@link DataOutputStream#writeDouble(double)}, except
     * using little-endian byte order.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void writeDouble(double v) throws IOException {
        writeLong(Double.doubleToLongBits(v));
    }

    /**
     * Writes a {@code float} as specified by {@link DataOutputStream#writeFloat(float)}, except using
     * little-endian byte order.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void writeFloat(float v) throws IOException {
        writeInt(Float.floatToIntBits(v));
    }

    /**
     * Writes an {@code int} as specified by {@link DataOutputStream#writeInt(int)}, except using
     * little-endian byte order.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void writeInt(int v) throws IOException {
        if (this.isNetwork) {
            long value = (long) (v << 1 ^ v >> 31);

            while ((value & -128L) != 0L) {
                out.write((byte) ((int) (value & 127L | 128L)));
                value >>>= 7;
            }

            out.write((byte) ((int) value));
        } else {
            out.write(0xFF & v);
            out.write(0xFF & (v >> 8));
            out.write(0xFF & (v >> 16));
            out.write(0xFF & (v >> 24));
        }
    }

    /**
     * Writes a {@code long} as specified by {@link DataOutputStream#writeLong(long)}, except using
     * little-endian byte order.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void writeLong(long v) throws IOException {
        if (this.isNetwork) {
            BigInteger origin = BigInteger.valueOf(v);
            BigInteger left = origin.shiftLeft(1);
            BigInteger right = origin.shiftRight(63);
            BigInteger value = left.xor(right);

            if (value.compareTo(UNSIGNED_LONG_MAX_VALUE) > 0) {
                throw new IllegalArgumentException("The value is too big");
            } else {
                value = value.and(UNSIGNED_LONG_MAX_VALUE);
                BigInteger i = BigInteger.valueOf(-128L);
                BigInteger BIX7F = BigInteger.valueOf(127L);

                for (BigInteger BIX80 = BigInteger.valueOf(128L); !value.and(i).equals(BigInteger.ZERO); value = value.shiftRight(7)) {
                    out.write(value.and(BIX7F).or(BIX80).byteValue());
                }

                out.write(value.byteValue());
            }
        } else {
            byte[] bytes = Longs.toByteArray(Long.reverseBytes(v));
            write(bytes, 0, bytes.length);
        }
    }

    /**
     * Writes a {@code short} as specified by {@link DataOutputStream#writeShort(int)}, except using
     * little-endian byte order.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void writeShort(int v) throws IOException {
        out.write(0xFF & v);
        out.write(0xFF & (v >> 8));
    }

    @Override
    public final void writeUTF(String str) throws IOException {
        if (isNetwork) {
            writeNetworkUTF(str, this);
        } else {
            writeUTF(str, this);
        }
    }

    static int writeUTF(String str, DataOutput out) throws IOException {
        int strlen = str.length();
        int utflen = 0;
        int c, count = 0;

        /* use charAt instead of copying String to char array */
        for (int i = 0; i < strlen; i++) {
            c = str.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                utflen++;
            } else if (c > 0x07FF) {
                utflen += 3;
            } else {
                utflen += 2;
            }
        }

        if (utflen > 65535)
            throw new UTFDataFormatException(
                    "encoded string too long: " + utflen + " bytes");

        byte[] bytearr = null;
        bytearr = new byte[utflen + 2];

        bytearr[count++] = (byte) ((utflen >>> 0) & 0xFF);
        bytearr[count++] = (byte) ((utflen >>> 8) & 0xFF);

        int i = 0;
        for (i = 0; i < strlen; i++) {
            c = str.charAt(i);
            if (!((c >= 0x0001) && (c <= 0x007F))) break;
            bytearr[count++] = (byte) c;
        }

        for (; i < strlen; i++) {
            c = str.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                bytearr[count++] = (byte) c;

            } else if (c > 0x07FF) {
                bytearr[count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                bytearr[count++] = (byte) (0x80 | ((c >> 6) & 0x3F));
                bytearr[count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
            } else {
                bytearr[count++] = (byte) (0xC0 | ((c >> 6) & 0x1F));
                bytearr[count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
            }
        }
        out.write(bytearr, 0, utflen + 2);
        return utflen + 2;
    }

    static void writeNetworkUTF(String str, DataOutput out) throws IOException {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);

        long length = bytes.length;
        while ((length & -128) != 0) {
            out.write((byte) (length & 127 | 128));
            length >>>= 7;
        }

        out.write((byte) length);

        out.write(bytes);
    }

    // Overriding close() because FilterOutputStream's close() method pre-JDK8 has bad behavior:
    // it silently ignores any exception thrown by flush(). Instead, just close the delegate stream.
    // It should flush itself if necessary.
    @Override
    public void close() throws IOException {
        out.close();
    }
}
