package com.codez.collar.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by codez on 2017/12/24.
 * Description:
 */

public class RomUtils {
    private static final String TAG = "RomUtils";
    //判断是否是Miui
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    //判断是否是Flyme
    private static final String KEY_BUILD_USER = "ro.build.user";//若系统是flyme，则此值为flyme
    private static final String KEY_FLYME_PUBISHED = "ro.flyme.published";
    private static final String KEY_PERSIST_SYS_USE_FLYME_ICON = "persist.sys.use.flyme.icon";

    public static boolean isMIUI() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }

    //判断是否是Flyme
    public static boolean isFlyme() {
//        //只适用于flyme5.1以前
//        try {
//            // Invoke Build.hasSmartBar()
//            final Method method = Build.class.getMethod("hasSmartBar");
//            return method != null;
//        } catch (final Exception e) {
//            return false;
//        }
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return (prop.getProperty(KEY_BUILD_USER, null) != null && "flyme".equals(prop.getProperty(KEY_BUILD_USER)))
                    || prop.getProperty(KEY_FLYME_PUBISHED, null) != null
                    || prop.getProperty(KEY_PERSIST_SYS_USE_FLYME_ICON, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }

    public static void printBuildProperties() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            Log.i(TAG, "printBuildProperties:" + properties.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.w(TAG, "printBuildProperties:" + e.toString());
        }
    }

    public static class BuildProperties {

        private final Properties properties;

        private BuildProperties() throws IOException {
            properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        }

        public boolean containsKey(final Object key) {
            return properties.containsKey(key);
        }

        public boolean containsValue(final Object value) {
            return properties.containsValue(value);
        }

        public Set<Map.Entry<Object, Object>> entrySet() {
            return properties.entrySet();
        }

        public String getProperty(final String name) {
            return properties.getProperty(name);
        }

        public String getProperty(final String name, final String defaultValue) {
            return properties.getProperty(name, defaultValue);
        }

        public boolean isEmpty() {
            return properties.isEmpty();
        }

        public Enumeration<Object> keys() {
            return properties.keys();
        }

        public Set<Object> keySet() {
            return properties.keySet();
        }

        public int size() {
            return properties.size();
        }

        public Collection<Object> values() {
            return properties.values();
        }

        public static BuildProperties newInstance() throws IOException {
            return new BuildProperties();
        }

    }
}
