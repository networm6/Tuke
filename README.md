# Tuke——基于 ObjectStream的 key-value 存储组件

Tuke 是基于 ObjectStream的 key-value 存储组件，稳定性强，使用方便。从 2019 年至今在我个人的软件上使用，在经过了3年的磨练，我写出了Tuke3.0版本，现在一起阅读使用说明吧！

`ps:下文使用ObjS替代ObjectStream`

## 为什么不使用SharedPerference和MMKV

传统的SharedPerference的诟病已为人熟知，这里不再列举。早在17年，腾讯就推出了更高性能的MMKV组件，其数据储存的方法是增量更新，但因为使用的二进制结构原因，导致了文件只能增不能减。同时，linux内核的mmap虽是高效的，但也是不稳定的，举个例子：有一个极大的数据，数据写一半的时候突然进程崩溃或者被杀死。那当你再次读取数据的时候会发现所有数据均已丢失。

## 为什么使用ObjStream

安卓上的Objs使用的是既传统又不传统的I/O操作，他的效率比其他java平台高。当你深入追踪安卓的I/O操作后，你会发现安卓已经对java原始的I/O操作进行了改动，这也是为什么我说他是既传统有不传统的I/O。同时，如果你不喜欢使用安卓的I/O，Tuke可以很轻松的修改，这将会在下文的`进阶用法2`中提及。

## Tuke 简介

- **数据组织**
  Tuke采用一个键对应一个文件，文件内储存值。即linux的`万物均是文件`的思想。

- **读写优化**

  Tuke提供了一个全局的缓存策略，还提供接口允许开发者使用自己的缓存策略。当写入或读取一个文件时，会记录操作的时长，以此作为缓存重要性的依据，Tuke内部使用了lruCache，当缓存的大小超过13*60ms的时候，将会回收使用最少，最不频繁的值。

- **语法糖**
   Kotlin提供的丰富的语法，`by`作为其中的一员，可以代理属性的读写。利用它，TukeKtx实现了对属性的自动保存和恢复。

## Android指南

### 安装引入

Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
```grovey
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```grovey
	dependencies {
	        implementation 'com.gitee.simon9102:tuke:1.0.0'
	}
```

### 快速上手

Tuke分为全局储存和分区储存。

__下述介绍全局储存__

当使用全局储存的时候，需要进行初始化

```java
Tuke.init(Context)
//or
Tuke.init(Tuke.Config)
```

使用Context作为参数时，Tuke会把`data/data/包名/no_backup`作为储存位置，同时使用全局缓存，并记录读写出现的异常（tukeError:xxxxxxxx);(w)

使用Config作为参数时，开发者可以在Config实例化时传入储存位置和数据库名称，并可以链式添加异常记录器和缓存策略

读写删操作

```java
Tuke.tukeWrite(String key,Object value);
Tuke.tukeGet(String key, Object def, boolean useMemory);
//其中 get方法的def和useMemory是可选参数
Tuke.tukeRemove(String key);
Tuke.tukeRemoveAll();
```

__下述介绍分区储存__

使用分区储存时，需要new一个Tuke实例

```java
Tuke tuke=new Tuke(Tuke.Config);
```

读写删操作

`ps：下文中tuke不是Tuke，tuke是一个对象，Tuke是类`

```
tuke.write(String key,Object value);
tuke.get(String key, Object def, boolean useMemory);
//其中 get方法的def和useMemory是可选参数
tuke.remove(String key);
tuke.removeAll();
```

### 进阶用法

#### 用法1：使用TukeKtx组件，以通过代理变量实现自动储存读取


使用

```kotlin
var cookie: String by tuke(def: V?,key: String?,useMemory: Boolean,config: Tuke.Config?)
```

by tuke()提供4个参数，每个参数均可以省略。下介绍参数详解
 参数 |介绍
---|:--
def |  取不到值时的默认值，若未使用此参数，默认null
key |   tuke取值使用的键，若未设置此参数，默认 "当前类名.字段名"
useMemory | 是否使用缓存，若未设置此参数，默认false
config  | 使用某个配置，若未设置此参数，默认全局储存

#### 用法2：自定义储存方式

上文以述，开发者可以自行修改Tuke的源码以实现自己对性能/安全的需求。

Tuke最终的I/O操作并非在Tuke类，而是交给`TukeImpl`实现的，开发者可以自己修改内部write/get的Stream。

## 问题 & 反馈

如果你遇到了问题，并且想联系我，欢迎提[issues](https://github.com/networm6/Tuke/issues)。
