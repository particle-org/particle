package com.particle.model.utils;

import java.util.regex.Pattern;

public final class ProphetPatterns {

    public static final Pattern COMMA = Pattern.compile(",");

    public static final Pattern PERCENTAGE = Pattern.compile("%", Pattern.LITERAL);

    public static final Pattern NEWLINE = Pattern.compile("\n");

    public static final Pattern DASH = Pattern.compile("-");

    public static final Pattern UNDERSCORE = Pattern.compile("_");

    public static final Pattern SPACE = Pattern.compile(" ");

    public static final Pattern SEMICOLON = Pattern.compile(";");

    public static final Pattern COLON = Pattern.compile(":");

    public static final Pattern COLONEQUALS = Pattern.compile("([:=])");

    public static final Pattern PIPE = Pattern.compile("\\|");

    public static final Pattern NON_ALPHA_NUMERIC = Pattern.compile("[^a-zA-Z0-9]");

    public static final Pattern INTEGER = Pattern.compile("^[0-9]+$");

    public static final Pattern VALID_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_$]{1,16}$");

    public static final Pattern NON_PRINTABLE_CHARACTERS = Pattern.compile("[^\\x20-\\x7F]");

    public static final Pattern EQUALS = Pattern.compile("=");

    public static final Pattern FORMATTER = Pattern.compile("<c(?<color>\\d+)>(?<msg>.*?)</c\\1>", Pattern.CASE_INSENSITIVE);

    public static final Pattern I18N_STRING = Pattern.compile("\\{@@(?<key>.+?)}", Pattern.CASE_INSENSITIVE);

}
