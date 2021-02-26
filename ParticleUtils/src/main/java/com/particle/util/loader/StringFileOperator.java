package com.particle.util.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class StringFileOperator {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringFileOperator.class);

    public StringFileOperator() {
    }

    public static String readConfigFiles(File configFile) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), "utf8"));
            Throwable var2 = null;

            try {
                StringBuilder stringBuilder = new StringBuilder();
                String temp = "";

                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp);
                    stringBuilder.append("\r\n");
                }

                String var5 = stringBuilder.toString();
                return var5;
            } catch (Throwable var15) {
                var2 = var15;
                throw var15;
            } finally {
                if (bufferedReader != null) {
                    if (var2 != null) {
                        try {
                            bufferedReader.close();
                        } catch (Throwable var14) {
                            var2.addSuppressed(var14);
                        }
                    } else {
                        bufferedReader.close();
                    }
                }

            }
        } catch (IOException var17) {
            LOGGER.error("Fail to load file {}.", configFile);
            return null;
        }
    }

    public static void writeConfigFiles(File configFile, String data) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), "utf8"));
            Throwable var3 = null;

            try {
                bufferedWriter.write(data);
                bufferedWriter.flush();
            } catch (Throwable var13) {
                var3 = var13;
                throw var13;
            } finally {
                if (bufferedWriter != null) {
                    if (var3 != null) {
                        try {
                            bufferedWriter.close();
                        } catch (Throwable var12) {
                            var3.addSuppressed(var12);
                        }
                    } else {
                        bufferedWriter.close();
                    }
                }

            }
        } catch (IOException var15) {
            LOGGER.error("Fail to save file {}.", configFile);
        }
    }
}
