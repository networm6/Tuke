# Tuke-Sp替代方案
## 更新需知
```
TUKE去掉了内存缓存，这东西没实际作用。修正了一些写法，也修复了一些bug。
```
### 权限
```
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```
### 初始化数据库
```
File file = new File(Environment.getExternalStorageDirectory().toString() );
Tuke.init("数据库名",file.toString());//初始化Tuke
或者
Tuke.init(Context);
此时数据被放到应用私有目录，数据库名为TUKE
```
### 删除数据库
```
Tuke.clearDisk(key);//删除单个key
Tuke.clearDisk();//删除所有数据
```
## 代码介绍
方法名 | 作用 | 参数
---|---|---
  Tuke.setException(Tuke.OnException) | 异常处理 | Tuke.OnException 
  Tuke.write(String,T) | 写操作 | key值 数据  
  Tuke.putBitmapAsync(String,Bitmap,Tuke.Callback) | 异步写位图操作 |数据 Tuke.Callback 
  Tuke.get(String) | 读操作 | key值 
  Tuke.get(String, T) | 读操作 |  key值 默认返回值 
  Tuke.getBitmap(String, Bitmap) | Bitmap 专用读 | key值 默认返回值 
  Tuke.write(String, Bitmap) | Bitmap 专用写 | key值 位图
  Tuke.clearDisk(String) | 清除磁盘 | key值 
  Tuke.clearDisk() | 删库 | 无参数
