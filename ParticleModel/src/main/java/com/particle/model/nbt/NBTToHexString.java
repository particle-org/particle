package com.particle.model.nbt;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class NBTToHexString {

    private static final Logger log = LoggerFactory.getLogger(NBTToHexString.class);

    public static String convertToHexString(NBTBase tag, boolean blank) throws IOException {
        byte[] src = NBTToByteArray.convertToByteArray(tag, false);

        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }

        for (byte b : src) {
            if (!(stringBuilder.length() == 0) && blank) {
                stringBuilder.append(" ");
            }
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }

    public static NBTBase convertToTag(String hexString) throws IOException {
        if (StringUtils.isEmpty(hexString)) {
            return null;
        }

        String str = "0123456789ABCDEF";
        hexString = hexString.toUpperCase().replace(" ", "");
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (((byte) str.indexOf(hexChars[pos]) << 4) | ((byte) str.indexOf(hexChars[pos + 1])));
        }

        return NBTToByteArray.convertToTag(d, false);
    }
}
