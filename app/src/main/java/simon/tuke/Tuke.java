package simon.tuke;
import android.os.AsyncTask;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.security.auth.callback.Callback;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Tuke {

private static DiskCache disk;
private static MemoryCache memory;
private static OnException error;
    public static void init(String name, String path){
		disk = new DiskCache(name, path);
		memory = new MemoryCache();
	}
	public static void SetException(OnException in){
		error=in;
	}
	public static <T extends Serializable> void write(boolean ismemory,String key,T in) {
		key=keytonew(key);
		try{
		disk.write(key,in);
		if(ismemory)
		memory.put(key,in);
		}catch(IOException e){
			if(error!=null)
			error.error(e);
		}
	}

	public static void write(boolean ismemory,String key,Bitmap bit){
		key=keytonew(key);
		try
		{
			disk.saveBitmap(key, bit);
			if(ismemory)
			memory.put(key,bit);
		}
		catch (IOException e)
		{
			error.error(e);
		}
		memory.put(key,bit);
	}
	public static Bitmap getBitmap(boolean ismemory,String key,Bitmap def){
		key=keytonew(key);
		Bitmap a= memory.get(key);
		if(a!=null)
			return a;
		else{
			Bitmap b=disk.getBitmap(key);
			if(b==null)
				return def;
			else{
				if(ismemory)
				memory.put(key,b);
				return b;
			}
		}
	}
	private static String keytonew(String key){
		return key.replaceAll(File.separator,"|");
	}
	public static <T extends Serializable> T get(boolean ismemory,String key,T def)  {
		try{
			String mkey=keytonew(key);
		T a=memory.get(mkey);
		
		if(a!=null)
			return a;
		else{
			T b=disk.get(mkey);
			if(b!=null){
				if(ismemory)
				memory.put(mkey,b);
				return b;
			}else
				return def;
		}
		}catch(IOException e){
			if(error!=null)
				error.error(e);
		return def;
		}catch(ClassNotFoundException e){
			if(error!=null)
				error.error(e);
		return def;
		}
	}
	public static <T extends Serializable> T get(boolean ismemory,String key) {
		return get(ismemory,key,null);
	}
	public static void clearMemory(){
		memory.removeAll();
	}
	public static void clearMemory(String key){
		key=keytonew(key);
		memory.delete(key);
	}
	public static void clearDisk(String key){
		key=keytonew(key);
		disk.delete(key);
	}
	public interface Callback {
        void apply();
    }
	public interface OnException<T extends java.lang.Exception>{
		void error(T e);
	}
	public static <T extends Serializable> void putAsync(final boolean ismemory,final String key, final T value, final Callback callback) {
        new AsyncTask<Void, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params)  {
                Tuke.write(ismemory,key,value);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPreExecute();
                if (callback != null) {
                    callback.apply();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
}
