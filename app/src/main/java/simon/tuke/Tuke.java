package simon.tuke;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.Serializable;

public class Tuke {
    private static volatile Config signalConfig;
    public static void init(Context context){
        signalConfig=new Config(context.getNoBackupFilesDir().toString(),"TUKE");
        signalConfig.setMemoryCache(new GlobalMemoryCache());
        signalConfig.setExceptionCatcher(new ExceptionCatcher() {
            @Override
            public <T extends Exception> void onError(String ExcClassName, T e) {
                Log.w("tukeError",ExcClassName+"     "+e.getMessage());
            }
        });
    }
    public static void init(Config config){
        signalConfig=config;
    }
    public static  <T extends Serializable> void tukeWrite(String key, T value) {
        TukeImpl.write(signalConfig, key, value);
    }
    public static <T extends Serializable> T tukeGet(String key) {
        return tukeGet(key, null, false);
    }

    public static <T extends Serializable> T tukeGet(String key, T def) {
        return tukeGet(key, def,false);
    }

    public static <T extends Serializable> T tukeGet(String key, boolean useMemory) {
        return tukeGet(key, null, useMemory);
    }

    public static <T extends Serializable> T tukeGet(String key, T def, boolean useMemory) {
        T v = TukeImpl.get(signalConfig, key, useMemory);
        if (v == null) return def;
        return v;
    }
    public static void tukeRemove(String key){
        TukeImpl.remove(signalConfig,key);
    }
    public static void tukeRemoveAll(){
        TukeImpl.removeAll(signalConfig);
    }
    /**
    ///////////////////////////////////////////上述是全局的Tuke配置/////////////////////////////////////////////////////
    ///////////////////////////////////////////上述是全局的Tuke配置/////////////////////////////////////////////////////
    ///////////////////////////////////////////上述是全局的Tuke配置/////////////////////////////////////////////////////
    */

    private final Config config;

    public Tuke(Config config) {
        this.config = config;
    }

    public <T extends Serializable> void write(String key, T value) {
        TukeImpl.write(config, key, value);
    }

    public <T extends Serializable> T get(String key) {
        return get(key, null, false);
    }

    public <T extends Serializable> T get(String key, T def) {
        return get(key, def);
    }

    public <T extends Serializable> T get(String key, boolean useMemory) {
        return get(key, null, useMemory);
    }

    public <T extends Serializable> T get(String key, T def, boolean useMemory) {
        T v = TukeImpl.get(config, key, useMemory);
        if (v == null) return def;
        return v;
    }

    public boolean remove(String key) {
        return TukeImpl.remove(config, key);
    }
    public void removeAll(){
        TukeImpl.removeAll(config);
    }
    /**
     * 异常捕获接口
     */
    public static interface ExceptionCatcher {
        /**
         * 处理异常的函数
         *
         * @param ExcClassName 异常的类型
         * @param e            异常
         */
        <T extends Exception> void onError(String ExcClassName, T e);
    }

    public static class Config {
        protected String path, name;
        protected ExceptionCatcher catcher;
        protected TukeCache memoryCache;

        public Config(String path, String name) {
            this.path = path + (!path.endsWith(File.separator) ? File.separator : "");
            this.name = name + (!name.endsWith(File.separator) ? File.separator : "");
        }

        /**
         * 设置异常捕获
         *
         * @param catcher 处理异常的接口
         * @return 链式调用
         */
        public Config setExceptionCatcher(ExceptionCatcher catcher) {
            if (catcher == null) {
                Log.w("tuke", "When you use the void named ‘setExceptionCatcher’, you gave it null");
                StackTraceElement[] stack = Thread.currentThread().getStackTrace();
                for (StackTraceElement stackTraceElement : stack) {
                    Log.w("tuke", stackTraceElement.getClassName() + " 。" + stackTraceElement.getMethodName() + "-----");
                }
                Log.w("tuke", "Although there is no matter, it is not recommended");
            }
            this.catcher = catcher;
            return this;
        }

        public Config setMemoryCache(TukeCache memoryCache) {
            this.memoryCache = memoryCache;
            return this;
        }
    }
}
