package com.lee.utils;

import java.util.Arrays;
import java.util.List;

public class TransformStringUtil {

    public static final List<String> MARKDOWN_IDENTIFIERS = Arrays.asList("\\*", "#", "\\+\\+", "~", "==",
            "^", ":::", "hljs-left", "hljs-center", "hljs-right", ">", "- ", "```", " ");

    public static String getIntroduction(String content, Boolean hasHeadImg) {
        for (String identifier : MARKDOWN_IDENTIFIERS) {
            content = content.replaceAll(identifier, "");
        }

        String result;
        if (hasHeadImg) {
            if (content.length() > 250) {
                result = content.substring(0, 250) + "...";
            } else {
                result = content;
            }
        } else {
            if (content.length() > 300) {
                result = content.substring(0, 300) + "...";
            } else {
                result = content;
            }
        }
        return result;
    }
}
