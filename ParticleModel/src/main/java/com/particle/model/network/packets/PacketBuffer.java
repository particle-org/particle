package com.particle.model.network.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class PacketBuffer {
    private static final BigInteger UNSIGNED_LONG_MAX_VALUE = new BigInteger("FFFFFFFFFFFFFFFF", 16);

    private ByteBuf buf;

    public PacketBuffer(int initSize) {
        this.buf = Unpooled.buffer(initSize);
    }

    public PacketBuffer() {
        this.buf = Unpooled.buffer();
    }

    public PacketBuffer(ByteBuf byteBuf) {
        this.buf = byteBuf;
    }

    //----- Buf操作 -----
    public ByteBuf getBuffer() {
        return this.buf.slice(0, buf.writerIndex());
    }

    public ByteBuf getBuf() {
        return buf;
    }

    public void setBuffer(ByteBuf buf) {
        this.buf = buf.slice(0, buf.writerIndex());
    }

    public void append(ByteBuf buffer) {
        this.buf = Unpooled.wrappedBuffer(this.buf, buffer);
    }

    public ByteBuf readSlice(int length) {
        return buf.readSlice(length);
    }

    public ByteBuf subSlice(int length) {
        ByteBuf byteBuf = this.buf.readSlice(length);

        this.buf.readerIndex(this.buf.readerIndex() - length);

        return byteBuf;
    }

    public ByteBuf slice() {
        return buf.slice();
    }

    public int getReadIndex() {
        return this.buf.readerIndex();
    }

    public void setReadIndex(int index) {
        this.buf.readerIndex(index);
    }

    public int getWriteIndex() {
        return this.buf.writerIndex();
    }

    public void reset() {
        this.buf = Unpooled.buffer();
    }

    public boolean canRead() {
        return this.buf.readerIndex() < this.buf.writerIndex();
    }

    public int readableLength() {
        return this.buf.writerIndex() - this.buf.readerIndex();
    }

    public boolean hasData() {
        return this.buf.readerIndex() < this.buf.writerIndex();
    }

    //----- 转换类库 ------
    private long encodeZigZag32(int v) {
        return (long) (v << 1 ^ v >> 31);
    }

    private int decodeZigZag32(long v) {
        return (int) (v >> 1) ^ -((int) (v & 1L));
    }

    private BigInteger encodeZigZag64(long v) {
        BigInteger origin = BigInteger.valueOf(v);
        BigInteger left = origin.shiftLeft(1);
        BigInteger right = origin.shiftRight(63);
        return left.xor(right);
    }

    private BigInteger decodeZigZag64(long v) {
        return this.decodeZigZag64(BigInteger.valueOf(v).and(UNSIGNED_LONG_MAX_VALUE));
    }

    private BigInteger decodeZigZag64(BigInteger v) {
        BigInteger left = v.shiftRight(1);
        BigInteger right = v.and(BigInteger.ONE).negate();
        return left.xor(right);
    }

    //----- 高级操作 -----
    public int readUnsignedVarInt() {
        int out = 0;
        int bytes = 0;

        byte in;
        do {
            in = this.readByte();
            out |= (in & 127) << bytes++ * 7;
            if (bytes > 6) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((in & 128) == 128);

        return out;
    }

    public int readSignedVarInt() {
        long val = this.readUnsignedVarLong();
        return this.decodeZigZag32(val);
    }

    public long readUnsignedVarLong() {
        long out = 0L;
        int bytes = 0;

        byte in;
        do {
            in = this.readByte();
            out = out | (((long) in & 127) << bytes++ * 7);
            if (bytes > 10) {
                throw new RuntimeException("VarLong too big");
            }
        } while ((in & 128) == 128);

        return out;
    }

    public BigInteger readSignedVarLong() {
        BigInteger val = this.readVarNumber(10);
        return this.decodeZigZag64(val);
    }

    private BigInteger readVarNumber(int length) {
        BigInteger result = BigInteger.ZERO;
        int offset = 0;

        while (offset < length) {
            int b = this.readByte();
            result = result.or(BigInteger.valueOf((((long) b & 127) << offset * 7)));
            ++offset;
            if ((b & 128) <= 0) {
                return result;
            }
        }

        throw new IllegalArgumentException("Var Number too big");
    }

    public String readString() {
        int length = this.readUnsignedVarInt();
        byte[] strBytes = new byte[length];
        this.readBytes(strBytes);

        return new String(strBytes);
    }


    public void writeUnsignedVarInt(int value) {
        while ((value & -128) != 0) {
            this.writeByte((byte) (value & 127 | 128));
            value >>>= 7;
        }

        this.writeByte((byte) value);
    }

    public void writeSignedVarInt(int value) {
        long signedValue = this.encodeZigZag32(value);
        this.writeUnsignedVarLong(signedValue);
    }

    public void writeUnsignedVarLong(long value) {
        while ((value & -128L) != 0L) {
            this.writeByte((byte) ((int) (value & 127L | 128L)));
            value >>>= 7;
        }

        this.writeByte((byte) ((int) value));
    }

    public void writeSignedVarLong(long value) {
        BigInteger signedLong = this.encodeZigZag64(value);
        this.writeVarNumber(signedLong);
    }

    private void writeVarNumber(BigInteger value) {
        if (value.compareTo(UNSIGNED_LONG_MAX_VALUE) > 0) {
            throw new IllegalArgumentException("The value is too big");
        } else {
            value = value.and(UNSIGNED_LONG_MAX_VALUE);
            BigInteger i = BigInteger.valueOf(-128L);
            BigInteger BIX7F = BigInteger.valueOf(127L);

            for (BigInteger BIX80 = BigInteger.valueOf(128L); !value.and(i).equals(BigInteger.ZERO); value = value.shiftRight(7)) {
                this.writeByte(value.and(BIX7F).or(BIX80).byteValue());
            }

            this.writeByte(value.byteValue());
        }
    }

    public void writeString(String v) {
        byte[] ascii = v.getBytes(StandardCharsets.UTF_8);
        this.writeUnsignedVarInt(ascii.length);
        this.writeBytes(ascii);
    }

    public byte[] readAll() {
        int i = this.buf.readerIndex();

        byte[] bytes = new byte[this.buf.writerIndex()];
        this.buf.resetReaderIndex();
        this.buf.readBytes(bytes);

        this.buf.readerIndex(i);

        return bytes;
    }

    public byte[] readLeft() {
        byte[] bytes = new byte[this.buf.writerIndex() - this.buf.readerIndex()];
        this.buf.readBytes(bytes);

        return bytes;
    }

    //----- 读写操作 -----
    public void skipReadIndex(int length) {
        this.buf.skipBytes(length);
    }

    public void readBytes(byte[] v) {
        buf.readBytes(v);
    }

    public byte[] readBytesWithLength() {
        int length = this.readUnsignedVarInt();
        byte[] bytes = new byte[length];
        this.readBytes(bytes);

        return bytes;
    }

    public byte readByte() {
        return this.buf.readByte();
    }

    public short readShort() {
        return this.buf.readShort();
    }

    public int readUShort() {
        return this.buf.readUnsignedShort();
    }

    public short readLShort() {
        return this.buf.readShortLE();
    }

    public float readFloat() {
        return this.buf.readFloat();
    }

    public float readLFloat() {
        return this.buf.readFloatLE();
    }

    public int readInt() {
        return this.buf.readInt();
    }

    public long readUInt() {
        return this.buf.readUnsignedInt();
    }

    public int readLInt() {
        return this.buf.readIntLE();
    }

    public double readDoublt() {
        return this.buf.readDouble();
    }

    public long readLong() {
        return this.buf.readLong();
    }

    public long readLLong() {
        return this.buf.readLongLE();
    }

    public boolean readBoolean() {
        return this.buf.readByte() == 0x01;
    }

    public void writeBytesWithLength(byte[] v) {
        this.writeUnsignedVarInt(v.length);
        this.writeBytes(v);
    }

    public void writeBytes(byte[] v) {
        buf.writeBytes(v);
    }

    public void writeBytes(byte[] v, int srcIndex, int length) {
        buf.writeBytes(v, srcIndex, length);
    }

    public void writeBytes(ByteBuf byteBuf) {
        buf.writeBytes(byteBuf);
    }

    public void writeByte(byte v) {
        this.buf.writeByte(v);
    }

    public void writeShort(short v) {
        this.buf.writeShort(v);
    }

    public void writeLShort(short v) {
        this.buf.writeShortLE(v);
    }

    public void writeFloat(float v) {
        this.buf.writeFloat(v);
    }

    public void writeLFloat(float v) {
        this.buf.writeFloatLE(v);
    }

    public void writeInt(int v) {
        this.buf.writeInt(v);
    }

    public void writeLInt(int v) {
        this.buf.writeIntLE(v);
    }

    public void writeDouble(double v) {
        this.buf.writeDouble(v);
    }

    public void writeLong(long v) {
        this.buf.writeLong(v);
    }

    public void writeLLong(long v) {
        this.buf.writeLongLE(v);
    }

    public void writeBoolean(boolean v) {
        this.buf.writeByte(v ? 1 : 0);
    }
}
