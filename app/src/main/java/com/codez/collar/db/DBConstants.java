package com.codez.collar.db;

/**
 * Created by codez on 2018/4/19.
 * Description:
 */

public class DBConstants {
    public static final int DB_VERSION = 1;
    public static final String TABLE_CONFIG = "config";
    public static final String CONFIG_COLUMN_ID = "_id";
    public static final String CONFIG_COLUMN_KEY = "key";
    public static final String CONFIG_COLUMN_VALUE = "value";
    public static final String CONFIG_UPGRADE = "upgrade";

    public static final String TABLE_LIKE = "like";
    public static final String LIKE_COLUMN_STATUS_ID = "status_id";
    public static final String LIKE_COLUMN_REMARK = "remark";

    public static final String TABLE_STATUS = "status";
    public static final String STATUS_COLUMN_ID = "status_id";
    public static final String STATUS_COLUMN_CONTENT = "content";
    public static final String STATUS_COLUMN_TYPE = "type";
    public static final String STATUS_COLUMN_REMARK = "remark";

    public static final String STATUS_TYPE_HOME = "home";
    public static final String STATUS_TYPE_PUBLIC = "public";
    public static final String STATUS_TYPE_MENTION = "mention";
    public static final String STATUS_TYPE_USER = "user";

    public static final String TABLE_USER = "user";
    public static final String USER_COLUMN_ID = "user_id";
    public static final String USER_COLUMN_CONTENT = "content";
    public static final String USER_COLUMN_TYPE = "type";
    public static final String USER_COLUMN_REMARK = "remark";

    public static final String TABLE_COMMENT = "comment";
    public static final String COMMENT_COLUMN_ID = "comment_id";
    public static final String COMMENT_COLUMN_CONTENT = "content";
    public static final String COMMENT_COLUMN_TYPE = "type";
    public static final String COMMENT_COLUMN_REMARK = "remark";

    public static final String TABLE_MESSAGE = "message";
    public static final String MESSAGE_COLUMN_ID = "message_id";
    public static final String MESSAGE_COLUMN_CONTENT = "content";
    public static final String MESSAGE_COLUMN_TYPE = "type";
    public static final String MESSAGE_COLUMN_REMARK = "remark";

}
