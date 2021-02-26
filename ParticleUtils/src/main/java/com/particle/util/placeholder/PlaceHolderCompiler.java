package com.particle.util.placeholder;

public class PlaceHolderCompiler {

    /**
     * 单例对象
     */
    private static final PlaceHolderCompiler INSTANCE = new PlaceHolderCompiler();

    /**
     * 获取单例
     */
    public static PlaceHolderCompiler getInstance() {
        return PlaceHolderCompiler.INSTANCE;
    }

    public String compile(String placeHolder, String playerName, String defaultStr) {
        int index = placeHolder.indexOf(":");
        if (index >= 0 && index < placeHolder.length()) {
            CompileInterface compileInterface = CompileDB.get(placeHolder.substring(0, index));

            String compile = compileInterface.compile(placeHolder.substring(index + 1), playerName);

            if (compile == null) {
                return defaultStr;
            } else {
                return compile;
            }
        } else {
            return defaultStr;
        }
    }
}
