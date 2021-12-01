package simon.tuke;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 所有的写入读取操作都是这个类实现的
 */
public class TukeImpl {
    private static String handleKey(String key){
        key=key.replaceAll("/","|");
        key+=".dat";
        return key;
    }
    /**
     * 对象的write
     *
     * @param key   键
     * @param value 值
     * @param <T>   值的类型
     */
    public static <T extends Serializable> void write(Tuke.Config config, String key, T value) {
        key=handleKey(key);
        if (value == null)
            throw new RuntimeException("Tuke:Your value is null!Do you forget?");
        try {
            long time = System.currentTimeMillis();
            File file=new File(config.path + config.name + key);
            if (!file.getParentFile().exists())file.getParentFile().mkdirs();
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
            Log.e("write",file.getAbsolutePath());
            stream.writeObject(value);
            stream.close();
            time = System.currentTimeMillis() - time;
            if (config.memoryCache!=null)
            config.memoryCache.add(key, value, (int) time);
        } catch (IOException e) {
            if (config.catcher!=null)
            config.catcher.onError(e.getClass().getSimpleName(), e);
        }
    }

    /**
     * 对象的get
     *
     * @param key 键
     * @param <T> 值的类型
     */
    public static <T extends Serializable> T get(Tuke.Config config, String key, boolean useMemory) {
        key=handleKey(key);
        T value = useMemory && config.memoryCache!=null && config.memoryCache.get(key) != null ? (T) ((TukePackage) config.memoryCache.get(key)).obj : null;
        if (value == null)
            try {
                long time = System.currentTimeMillis();
                File file=new File(config.path + config.name + key);
                if (!file.getParentFile().exists())file.getParentFile().mkdirs();
                ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));
                Log.d("get",file.getAbsolutePath());
                value = (T) stream.readObject();
                stream.close();
                time = System.currentTimeMillis() - time;
                if (config.memoryCache!=null)
                config.memoryCache.add(key, value, (int) time);
            } catch (IOException | ClassNotFoundException e) {
                if (config.catcher!=null)
                config.catcher.onError(e.getClass().getSimpleName(), e);
            }
        return value;
    }

    /**
     * 删除某个对象
     *
     * @param key 键
     * @return 是否删除成功
     */
    public static boolean remove(Tuke.Config config, String key) {
        key=handleKey(key);
        if (config.memoryCache!=null)
        config.memoryCache.remove(key);
        File file = new File(config.path + config.name + key);
        return file.delete();
    }

    public static void removeAll(Tuke.Config config) {
        if (config.memoryCache!=null)
        config.memoryCache.removeAll();
        String path = config.path + config.name;
        File dirFile = new File(path.substring(0, path.length() - 1));
        for (File one : dirFile.listFiles()) {
            one.delete();
        }
    }
}
