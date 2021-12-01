package simon.tuke;

import android.util.LruCache;
import java.io.Serializable;

/**
 * 全局的内存缓存
 */
public class GlobalMemoryCache implements TukeCache {
    private static LruCache<String,TukePackage> cache;//使用获取对象所用的时间来判断是否需要缓存
    public GlobalMemoryCache(){
        if (cache!=null)return;
        cache=new LruCache<String,TukePackage>(13*60){//13为60hz下一次刷新允许的最大时间，13*60就是一秒
            @Override
            protected int sizeOf(String key, TukePackage value) {
                return value.size;
            }
        };
    }
    @Override
    public void add(String key, Serializable value,int time) {
        if (time<4&&cache.get(key)!=null)return;//如果在1/3帧内就获取到了，说明没必要缓存
        TukePackage packedValue=new TukePackage();
        packedValue.obj=value;
        packedValue.size=time;
        cache.put(key,packedValue);
    }

    @Override
    public Serializable get(String key) {
        return (Serializable) cache.get(key);
    }

    @Override
    public void remove(String key) {
        cache.remove(key);
    }

    @Override
    public void removeAll() {
        cache.evictAll();
    }
}
