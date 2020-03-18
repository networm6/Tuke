
# Tuke-Sp替代方案
## 简单使用
### 权限
```
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```
### 初始化数据库
```
File file = new File(Environment.getExternalStorageDirectory().toString() );
Tuke.init("数据库名",file.toString());//初始化Tuke
```
### 内存占用解决方案
```
@Override
public void onLowMemory()//重写onLowMemory方法,在里面清除Tuke的内存
{
	Tuke.clearMemory();
	Tuke.clearMemory("key");//清除指定key的内存储存
	super.onLowMemory();
}
```
## 代码介绍
方法名 | 作用 | 参数
---|---|---
  Tuke.init(String, String) |  初始化 | 数据库名称，数据库路径 
  Tuke.SetException(Tuke.OnException) | 异常处理 | Tuke.OnException 
  Tuke.write(Boolean, String, T) | 写操作 | 是否存入内存 key值 数据  
  Tuke.putAsync(Boolean, String, T, Tuke.Callback) | 异步写操作 | 是否存入内存 key值 数据 Tuke.Callback 
  Tuke.get(Boolean, String) | 读操作 | 是否存入内存 key值 
  Tuke.get(Boolean, String, T) | 读操作 | 是否存入内存 key值 默认返回值 
  Tuke.getBitmap(Boolean, String, Bitmap) | Bitmap 专用读 | 是否存入内存 key值 默认返回值 
  Tuke.write(Boolean, String, Bitmap) | Bitmap 专用写 | 是否存入内存 key值 bitmap数据 
  Tuke.clearMemory() | 清除内存 | 无参数 
  Tuke.clearDisk(String) | 清除磁盘 | key值 
  Tuke.clearMemory(String) | 清除内存 | key值 
