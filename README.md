# Tuke——基于 ObjectStream 的 key-value 存储组件

Tuke 是基于 ObjectStream 的 key-value 存储组件，稳定性强，使用方便。从 2019 年至今在我个人的软件上使用。经过了3年的磨练，我写出了 Tuke 3.0 版本，现在一起阅读使用说明吧！

## 为什么不使用 SharedPerference 或 MMKV

传统的 SharedPerference 的诟病已为人熟知，这里不再列举。早在 2017 年，腾讯就推出了性能更高的 MMKV 组件，其数据储存的方法是增量更新，但由于它使用了二进制结构，导致了文件只能增不能减。同时，Linux 内核的 mmap 虽然高效，但不稳定。举个例子：有一个极大的数据，数据写一半的时候进程突然崩溃或者被杀死，当再次读取数据时所有数据均已丢失。

## 为什么使用 ObjStream

安卓上的 ObjectStream 使用的是既传统又不传统的 I/O 操作，它的效率比其他 Java 平台高。如果深入追踪安卓的 I/O 操作后，你会发现安卓已经对 Java 原始的 I/O 操作进行了改动，这也是为什么它是既是传统又是不传统的 I/O 的原因。同时，如果你不想使用安卓的 I/O，Tuke 允许你轻松地修改，这将会在下文的`进阶用法2`中提及。

## Tuke 简介

- **数据组织**

  Tuke 采用“一个键对应一个文件、文件内储存值”的策略。即 Linux 的`万物均是文件`的思想。

- **读写优化**

  Tuke 提供了一个全局的缓存策略，还提供接口允许开发者使用自己的缓存策略。当写入或读取一个文件时，会记录操作的时长，以此作为缓存重要性的指标，Tuke 内部使用了 lruCache，一旦缓存的大小超过 13*60ms，将会回收使用次数最少的值。

- **Kotlin 语法糖**

   Kotlin 提供了丰富的语法，通过`by`可以代理属性的读写。利用它，TukeKtx 实现了对属性的自动保存和恢复。

## 安装引入

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

## 快速上手

Tuke 提供全局储存和分区储存。

### 全局储存

使用全局储存前需要进行初始化

```java
Tuke.init(Context)
// or
Tuke.init(Tuke.Config)
```

使用 Context 作为参数时，Tuke 会把`data/data/<package_name>/no_backup`作为储存位置，同时使用全局缓存，并记录读写出现的异常（tukeError:xxxxxxxx);(w)

使用 Config 作为参数时，开发者可以在 Config 实例化时传入储存位置和数据库名称，并可以链式添加异常记录器和缓存策略

读写删操作

```java
Tuke.tukeWrite(String key, Object value);
Tuke.tukeGet(String key, Object def, boolean useMemory);
// 其中 get 方法的 def 和 useMemory 是可选参数
Tuke.tukeRemove(String key);
Tuke.tukeRemoveAll();
```

### 分区储存

使用分区储存时，需要 new 一个 Tuke 实例

```java
Tuke tuke = new Tuke(Tuke.Config);
```

读写删操作

`ps：下文中的 tuke 不是 Tuke，tuke 是一个对象，而 Tuke 是类`

```
tuke.write(String key, Object value);
tuke.get(String key, Object def, boolean useMemory);
// 其中 get 方法的 def 和 useMemory 是可选参数
tuke.remove(String key);
tuke.removeAll();
```

## 进阶用法

### 用法1：使用 TukeKtx 组件，以通过代理变量实现自动储存读取


使用

```kotlin
var cookie: String by tuke(def: V?, key: String?, useMemory: Boolean, config: Tuke.Config?)
```

by tuke() 提供4个参数，每个参数均可省略。下为参数详解
 参数 |介绍
---|:--
def |  无法取到值时的默认值，若未使用此参数，默认 null
key |   Tuke 取值使用的键，若未设置此参数，默认 "当前类名.字段名"
useMemory | 是否使用缓存，若未设置此参数，默认 false
config  | 使用某个配置，若未设置此参数，默认全局储存

### 用法2：自定义储存方式

上文已述，开发者可以自行修改 Tuke 的源码以满足自己对性能/安全的需求。

Tuke 最终的 I/O 操作并非在 Tuke 类，而是交给`TukeImpl`实现的，开发者可以自行修改其内部 write/get 的 Stream。

## 问题 & 反馈

如果你遇到了问题，并且想联系我，欢迎提[issues](https://github.com/networm6/Tuke/issues)。
