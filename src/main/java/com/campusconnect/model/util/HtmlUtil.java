
package com.campusconnect.model.util;

/**
 * Utility class for safely escaping text before displaying it in HTML.
 */
public final class HtmlUtil {

    private HtmlUtil() {
        // Prevent object creation.
    }

    public static String escape(String value) {
        if (value == null) {
            return "";
        }

        StringBuilder result = new StringBuilder(value.length() + 16);

        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);

            switch (ch) {
                case '&':
                    result.append("&amp;");
                    break;
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case '"':
                    result.append("&quot;");
                    break;
                case '\'':
                    result.append("&#39;");
                    break;
                default:
                    result.append(ch);
                    break;
            }
        }

        return result.toString();
    }
}
