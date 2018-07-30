package car.wuba.saas.cache.core;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * 作者:  wangweiqiang
 * 时间:  2018/7/18
 * 说明:  对于SharePreference 用法api的封装
 */
class PreferenceProvider {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final int DEFAULT_INT = 0;
    private static final float DEFAULT_FLOAT = 0f;
    private static final long DEFAULT_LONG = 0l;
    private static final String DEFAULT_STRING = "";
    private static final boolean DEFAULT_BOOLEAN = false;

    public PreferenceProvider(Context context, String name) {
        this.preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        this.editor = preferences.edit();
    }

    /**
     * 存储Integer类型数据
     *
     * @param key
     * @param data
     */
    protected void putInt(String key, int data) {
        editor.putInt(key, data);
    }

    /**
     * 存储String类型数据
     *
     * @param key
     * @param data
     */
    protected void putString(String key, String data) {
        editor.putString(key, data);
        editor.apply();
    }

    /**
     * 存储Boolean类型数据
     *
     * @param key
     * @param data
     */
    protected void putBoolean(String key, boolean data) {
        editor.putBoolean(key, data);
        editor.apply();
    }

    /**
     * 存储Long类型数据
     *
     * @param key
     * @param data
     */
    protected void putLong(String key, Long data) {
        editor.putLong(key, data);
        editor.apply();
    }

    /**
     * 存储Set<String>类型数据
     *
     * @param key
     * @param data
     */
    protected void putStringSet(String key, Set<String> data) {
        editor.putStringSet(key, data);
        editor.apply();
    }

    /**
     * 存储Float类型数据
     *
     * @param key
     * @param data
     */
    protected void putFloat(String key, float data) {
        editor.putFloat(key, data);
        editor.apply();
    }

    /**
     * 获取String数据
     *
     * @param key
     * @return
     */
    protected String getString(String key) {
        return preferences.getString(key, DEFAULT_STRING);
    }

    /**
     * 获取Integer数据
     *
     * @param key
     * @return
     */
    protected Integer getInt(String key) {
        return preferences.getInt(key, DEFAULT_INT);
    }

    /**
     * 获取Float数据
     *
     * @param key
     * @return
     */
    protected Float getFloat(String key) {
        return preferences.getFloat(key, DEFAULT_FLOAT);
    }

    /**
     * 获取Boolean数据
     *
     * @param key
     * @return
     */
    protected Boolean getBoolean(String key) {
        return preferences.getBoolean(key, DEFAULT_BOOLEAN);
    }

    /**
     * 获取Long数据
     *
     * @param key
     * @return
     */
    protected Long getLong(String key) {
        return preferences.getLong(key, DEFAULT_LONG);
    }

    /**
     * 获取Set<String>数据
     *
     * @param key
     * @return
     */
    protected Set<String> getStringSet(String key) {
        return preferences.getStringSet(key, null);
    }

    /**
     * 清除某个数据
     * @param key
     */
    protected void remove(String key){
        editor.remove(key);
        editor.apply();
    }

    /**
     * 清除所有数据
     */
    protected void removeAll(){
        editor.clear();
        editor.apply();
    }




}
