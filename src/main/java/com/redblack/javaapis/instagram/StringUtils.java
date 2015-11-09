package com.redblack.javaapis.instagram;

import java.util.Iterator;

public class StringUtils {

    public static final String EMPTY = "";

    public static String join(final Iterator<?> iterator, final String separator) {

        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return EMPTY;
        }
        final Object first = iterator.next();
        if (!iterator.hasNext()) {
            final String result = first == null ? "" : first.toString();
            return result;
        }

        // two or more elements
        final StringBuilder buf = new StringBuilder(256); // Java default is 16,
                                                          // probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            final Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

}
