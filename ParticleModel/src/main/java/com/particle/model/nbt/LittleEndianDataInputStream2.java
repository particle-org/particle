package com.particle.model.nbt;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

@Beta
@GwtIncompatible
public final class LittleEndianDataInputStream2 extends FilterInputStream implements DataInput {

    private boolean isNetwork = false;

    /**
     * Creates a {@code LittleEndianDataInputStream} that wraps the given stream.
     *
     * @param in the stream to delegate to
     */
    public LittleEndianDataInputStream2(InputStream in) {
        super(Preconditions.checkNotNull(in));
    }

    public LittleEndianDataInputStream2(InputStream in, boolean isNetwork) {
        super(Preconditions.checkNotNull(in));

        this.isNetwork = isNetwork;
    }

    /**
     * This method will throw an {@link UnsupportedOperationException}.
     */
    @Override
    public String readLine() {
        throw new UnsupportedOperationException("readLine is not supported");
    }

    @Override
    public void readFully(byte[] b) throws IOException {
        ByteStreams.readFully(this, b);
    }

    @Override
    public void readFully(byte[] b, int off, int len) throws IOException {
        ByteStreams.readFully(this, b, off, len);
    }

    @Override
    public int skipBytes(int n) throws IOException {
        return (int) in.skip(n);
    }

    @Override
    public int readUnsignedByte() throws IOException {
        int b1 = in.read();
        if (0 > b1) {
            throw new EOFException();
        }

        return b1;
    }

    /**
     * Reads an unsigned {@code short} as specified by {@link DataInputStream#readUnsignedShort()},
     * except using little-endian byte order.
     *
     * @return the next two bytes of the input stream, interpreted as an unsigned 16-bit integer in
     * little-endian byte order
     * @throws IOException if an I/O error occurs
     */
    @Override
    public int readUnsignedShort() throws IOException {
        byte b1 = readAndCheckByte();
        byte b2 = readAndCheckByte();

        return Ints.fromBytes((byte) 0, (byte) 0, b2, b1);
    }

    /**
     * Reads an integer as specified by {@link DataInputStream#readInt()}, except using little-endian
     * byte order.
     *
     * @return the next four bytes of the input stream, interpreted as an {@code int} in little-endian
     * byte order
     * @throws IOException if an I/O error occurs
     */
    @Override
    public int readInt() throws IOException {
        if (this.isNetwork) {
            long out = 0L;
            int bytes = 0;

            byte in;
            do {
                in = this.readAndCheckByte();
                out = out | (((long) in & 127) << bytes++ * 7);
                if (bytes > 10) {
                    throw new RuntimeException("VarLong too big");
                }
            } while ((in & 128) == 128);

            return (int) (out >> 1) ^ -((int) (out & 1L));
        } else {
            byte b1 = readAndCheckByte();
            byte b2 = readAndCheckByte();
            byte b3 = readAndCheckByte();
            byte b4 = readAndCheckByte();

            return Ints.fromBytes(b4, b3, b2, b1);
        }
    }

    /**
     * Reads a {@code long} as specified by {@link DataInputStream#readLong()}, except using
     * little-endian byte order.
     *
     * @return the next eight bytes of the input stream, interpreted as a {@code long} in
     * little-endian byte order
     * @throws IOException if an I/O error occurs
     */
    @Override
    public long readLong() throws IOException {
        if (this.isNetwork) {
            BigInteger result = BigInteger.ZERO;
            int offset = 0;

            while (offset < 10) {
                int b = this.readAndCheckByte();
                result = result.or(BigInteger.valueOf((((long) b & 127) << offset * 7)));
                ++offset;
                if ((b & 128) <= 0) {
                    break;
                }
            }

            BigInteger left = result.shiftRight(1);
            BigInteger right = result.and(BigInteger.ONE).negate();
            return left.xor(right).longValue();
        } else {
            byte b1 = readAndCheckByte();
            byte b2 = readAndCheckByte();
            byte b3 = readAndCheckByte();
            byte b4 = readAndCheckByte();
            byte b5 = readAndCheckByte();
            byte b6 = readAndCheckByte();
            byte b7 = readAndCheckByte();
            byte b8 = readAndCheckByte();

            return Longs.fromBytes(b8, b7, b6, b5, b4, b3, b2, b1);
        }
    }

    /**
     * Reads a {@code float} as specified by {@link DataInputStream#readFloat()}, except using
     * little-endian byte order.
     *
     * @return the next four bytes of the input stream, interpreted as a {@code float} in
     * little-endian byte order
     * @throws IOException if an I/O error occurs
     */
    @Override
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    /**
     * Reads a {@code double} as specified by {@link DataInputStream#readDouble()}, except using
     * little-endian byte order.
     *
     * @return the next eight bytes of the input stream, interpreted as a {@code double} in
     * little-endian byte order
     * @throws IOException if an I/O error occurs
     */
    @Override
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @Override
    public final String readUTF() throws IOException {
        if (this.isNetwork) {
            return readNetUTF(this);
        } else {
            return readUTF(this);
        }
    }

    public final static String readUTF(DataInput in) throws IOException {
        int utflen = in.readUnsignedShort();
        byte[] bytearr = null;
        char[] chararr = null;
        bytearr = new byte[utflen];
        chararr = new char[utflen];

        int c, char2, char3;
        int count = 0;
        int chararr_count = 0;

        in.readFully(bytearr, 0, utflen);

        while (count < utflen) {
            c = (int) bytearr[count] & 0xff;
            if (c > 127) break;
            count++;
            chararr[chararr_count++] = (char) c;
        }

        while (count < utflen) {
            c = (int) bytearr[count] & 0xff;
            switch (c >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    /* 0xxxxxxx*/
                    count++;
                    chararr[chararr_count++] = (char) c;
                    break;
                case 12:
                case 13:
                    /* 110x xxxx   10xx xxxx*/
                    count += 2;
                    if (count > utflen)
                        throw new UTFDataFormatException(
                                "malformed input: partial character at end");
                    char2 = (int) bytearr[count - 1];
                    if ((char2 & 0xC0) != 0x80)
                        throw new UTFDataFormatException(
                                "malformed input around byte " + count);
                    chararr[chararr_count++] = (char) (((c & 0x1F) << 6) |
                            (char2 & 0x3F));
                    break;
                case 14:
                    /* 1110 xxxx  10xx xxxx  10xx xxxx */
                    count += 3;
                    if (count > utflen)
                        throw new UTFDataFormatException(
                                "malformed input: partial character at end");
                    char2 = (int) bytearr[count - 2];
                    char3 = (int) bytearr[count - 1];
                    if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
                        throw new UTFDataFormatException(
                                "malformed input around byte " + (count - 1));
                    chararr[chararr_count++] = (char) (((c & 0x0F) << 12) |
                            ((char2 & 0x3F) << 6) |
                            ((char3 & 0x3F) << 0));
                    break;
                default:
                    /* 10xx xxxx,  1111 xxxx */
                    throw new UTFDataFormatException(
                            "malformed input around byte " + count);
            }
        }
        // The number of chars produced may be less than utflen
        return new String(chararr, 0, chararr_count);
    }

    public final static String readNetUTF(DataInput in) throws IOException {
        int out = 0;
        int bytes = 0;

        byte temp;
        do {
            temp = in.readByte();
            out |= (temp & 127) << bytes++ * 7;
            if (bytes > 6) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((temp & 128) == 128);

        byte[] str = new byte[out];

        in.readFully(str);

        return new String(str, StandardCharsets.UTF_8);
    }

    /**
     * Reads a {@code short} as specified by {@link DataInputStream#readShort()}, except using
     * little-endian byte order.
     *
     * @return the next two bytes of the input stream, interpreted as a {@code short} in little-endian
     * byte order.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public short readShort() throws IOException {
        return (short) readUnsignedShort();
    }

    /**
     * Reads a char as specified by {@link DataInputStream#readChar()}, except using little-endian
     * byte order.
     *
     * @return the next two bytes of the input stream, interpreted as a {@code char} in little-endian
     * byte order
     * @throws IOException if an I/O error occurs
     */
    @Override
    public char readChar() throws IOException {
        return (char) readUnsignedShort();
    }

    @Override
    public byte readByte() throws IOException {
        return (byte) readUnsignedByte();
    }

    @Override
    public boolean readBoolean() throws IOException {
        return readUnsignedByte() != 0;
    }

    /**
     * Reads a byte from the input stream checking that the end of file (EOF) has not been
     * encountered.
     *
     * @return byte read from input
     * @throws IOException  if an error is encountered while reading
     * @throws EOFException if the end of file (EOF) is encountered.
     */
    private byte readAndCheckByte() throws IOException, EOFException {
        int b1 = in.read();
        return (byte) b1;
    }
}
