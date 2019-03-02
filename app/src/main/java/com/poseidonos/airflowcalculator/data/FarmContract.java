package com.poseidonos.airflowcalculator.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class FarmContract {

    public static final String CONTENT_AUTHORITY = "com.poseidonos.airflowcalculator";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FARMS = "farms";

    private FarmContract(){}

    public static abstract class FarmEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FARMS);

        public static final String TABLE_NAME = "farms";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_FARM_NAME = "name_site";
        public static final String COLUMN_PANEL_GENERATION = "panel_generation";
        public static final String COLUMN_AVAILABLE_COMPRESSORS = "available_compressors";
        public static final String COLUMN_COMPRESSOR_OUTPUT = "compressor_output";
        public static final String COLUMN_ACTIVE_COMPRESSORS = "active_compressors";
        public static final String COLUMN_NUMBER_PENS = "number_pens";
        public static final String COLUMN_CHANNEL_PER_PEN = "channel_per_pen";
        public static final String COLUMN_ACTIVE_PENS = "active_pens";
        public static final String COLUMN_NUMBER_WALKWAY_CHANNELS = "number_walkway_channels";
        public static final String COLUMN_ACTIVE_WALKWAY_CHANNELS = "active_walkway_channels";
        public static final String COLUMN_READ_PRESSURE = "read_pressure";
        public static final String COLUMN_TARGET_FLOW_DISPLAY = "target_flow_display";
        public static final String COLUMN_COMPARATIVE_STANDARD_METER = "comparative_standard_meter";

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_FARMS;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_FARMS;
    }
}
