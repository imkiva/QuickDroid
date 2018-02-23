package com.imkiva.quickdroid.database.statement;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.imkiva.quickdroid.database.type.FieldDataMapper;

import java.text.MessageFormat;

/**
 * @author kiva
 */

class StatementArgumentHelper {
    static String formatArgs(@NonNull String sql, @Nullable Object... args) {
        if (args == null || args.length == 0) {
            return sql;
        }

        String[] argStrings = new String[args.length];
        for (int i = 0; i < args.length; ++i) {
            argStrings[i] = FieldDataMapper.mapToString(args[i]);
        }

        return MessageFormat.format(sql, (Object[]) argStrings);
    }
}
