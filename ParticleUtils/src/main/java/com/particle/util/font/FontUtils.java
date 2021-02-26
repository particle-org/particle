package com.particle.util.font;

import org.apache.commons.lang3.StringUtils;

import java.awt.*;

public class FontUtils {

    /**
     * 根据指定字体获取宽度
     *
     * @param display
     * @return
     */
    @Deprecated
    public static int getDisplayWidth(String display) {
        Font font = new Font("黑体", Font.PLAIN, 12);
        return getDisplayWidth(font, display);
    }

    /**
     * 获取计分板的显示长度
     * 需要区分粗体和斜体的不同长度
     *
     * @param display
     * @return
     */
    public static int getScoreboardDisplayWidth(String display) {
        if (StringUtils.isEmpty(display)) {
            return 0;
        }
        String[] splitResult = TextFormat.splitFormatDisplay(display);
        int size = 0;
        String clean;
        for (int index = 0; index < splitResult.length; index++) {
            // 粗体
            if (splitResult[index].startsWith(TextFormat.BOLD.toString())) {
                clean = TextFormat.clean(splitResult[index]);
                Font font = new Font(DisplayFontType.ScoreboardType.getFamilyName(),
                        Font.BOLD, DisplayFontType.ScoreboardType.getSize());
                size += getDisplayWidth(font, clean);
            } else if (splitResult[index].startsWith(TextFormat.ITALIC.toString())) {
                // 斜体
                clean = TextFormat.clean(splitResult[index]);
                Font font = new Font(DisplayFontType.ScoreboardType.getFamilyName(),
                        Font.ITALIC, DisplayFontType.ScoreboardType.getSize());
                size += getDisplayWidth(font, clean);
            } else {
                // 纯文本
                clean = TextFormat.clean(splitResult[index]);
                Font font = new Font(DisplayFontType.ScoreboardType.getFamilyName(),
                        Font.PLAIN, DisplayFontType.ScoreboardType.getSize());
                size += getDisplayWidth(font, clean);
            }
        }
        return size;
    }

    /**
     * 指定字体，获取显示宽度
     *
     * @param font
     * @param display
     * @return
     */
    public static int getDisplayWidth(Font font, String display) {
        FontMetrics fm = sun.font.FontDesignMetrics.getMetrics(font);
        int width = fm.stringWidth(display);
        return width;
    }


}
