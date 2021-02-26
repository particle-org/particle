package com.particle.model.command;

public enum CmdCharEscap {
    SPACE(" ", "&#32;"),
    LINE("\n", "&#10;");

    private String source;

    private String escape;

    CmdCharEscap(String source, String escape) {
        this.source = source;
        this.escape = escape;
    }

    public String getSource() {
        return source;
    }

    public String getEscape() {
        return escape;
    }
}
