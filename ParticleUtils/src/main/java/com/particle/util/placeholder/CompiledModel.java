package com.particle.util.placeholder;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串预编译，之后直接搜索hashmap生成目标字符串即可
 */
public class CompiledModel {
    private static Pattern pattern = Pattern.compile("\\$\\{[\\u4e00-\\u9fa5\\w:\\-]+}");

    private static final PlaceHolderCompiler PLACE_HOLDER_COMPILER = PlaceHolderCompiler.getInstance();

    private Set<Integer> compiledIndex;
    private List<String> separatedStr;

    public static CompiledModel compile(String str) {
        return new CompiledModel(str);
    }

    public static String quickCompile(String str, String playerName) {
        int lastAppear = 0;

        StringBuilder stringBuilder = new StringBuilder();

        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            // 黏贴find之前的内容
            if (start != lastAppear) {
                stringBuilder.append(str, lastAppear, start);
            }

            // 替换 find到的内容
            String placeHolder = str.substring(start + 2, end - 1);
            stringBuilder.append(PLACE_HOLDER_COMPILER.compile(placeHolder, playerName, "-"));

            lastAppear = end;
        }

        if (lastAppear != str.length()) stringBuilder.append(str.substring(lastAppear));

        return stringBuilder.toString();
    }

    public String doCompile(String playerName) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < this.separatedStr.size(); i++) {
            if (compiledIndex.contains(i)) {
                String placeHolder = this.separatedStr.get(i);
                stringBuilder.append(PLACE_HOLDER_COMPILER.compile(placeHolder, playerName, "-"));
            } else {
                stringBuilder.append(separatedStr.get(i));
            }
        }

        return stringBuilder.toString();
    }

    public CompiledModel preCompile() {
        Set<Integer> compiledIndex = new HashSet<>();
        List<String> separatedStr = new LinkedList<>();

        for (int i = 0; i < this.separatedStr.size(); i++) {
            String placeHolder = this.separatedStr.get(i);

            if (this.compiledIndex.contains(i)) {
                String compileResult = PLACE_HOLDER_COMPILER.compile(placeHolder, null, null);
                if (compileResult == null) {
                    separatedStr.add(placeHolder);
                    compiledIndex.add(i);
                } else {
                    separatedStr.add(compileResult);
                }
            } else {
                separatedStr.add(placeHolder);
            }
        }

        return new CompiledModel(compiledIndex, separatedStr);
    }

    private CompiledModel(String str) {
        this.compiledIndex = new HashSet<>();
        this.separatedStr = new LinkedList<>();

        int lastAppear = 0;

        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            if (start != lastAppear) separatedStr.add(str.substring(lastAppear, start));

            separatedStr.add(str.substring(start + 2, end - 1));
            compiledIndex.add(separatedStr.size() - 1);

            lastAppear = end;
        }

        if (lastAppear != str.length()) separatedStr.add(str.substring(lastAppear));
    }

    private CompiledModel(Set<Integer> compiledIndex, List<String> separatedStr) {
        this.compiledIndex = compiledIndex;
        this.separatedStr = separatedStr;
    }
}
