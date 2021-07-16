package org.sitebay.android.fluxc.utils;

import com.yarolegovich.wellsql.WellSql;

import org.sitebay.android.fluxc.model.PostModel;
import org.sitebay.android.fluxc.model.TermModel;

public class WellSqlUtils {
    public static int getTotalPostsCount() {
        return WellSql.select(PostModel.class).getAsCursor().getCount();
    }

    public static int getTotalTermsCount() {
        return WellSql.select(TermModel.class).getAsCursor().getCount();
    }
}
