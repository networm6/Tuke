package simon.tuke

import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 注意：键名, 默认使用 "当前类名.字段名", 顶层字段没有类名
 * 吐槽：kt是真的方便
 */
inline fun <reified V : Serializable> tuke(
    def: V? = null,//value的默认值
    key: String? = null,//key 键名, 默认使用 "当前类名.字段名", 顶层字段没有类名
    useMemory: Boolean = false,//是否使用缓存
    config: Tuke.Config? = null//使用的tuke配置
) = object : ReadWriteProperty<Any?, V> {
    @Volatile
    private var value: V? = null
    override fun getValue(thisRef: Any?, property: KProperty<*>): V {
        return synchronized(this) {
            if (value == null) {
                value = run {
                    val className = thisRef?.javaClass?.name
                    var adjustKey = key ?: property.name
                    if (className != null) adjustKey = "${className}.${adjustKey}"
                    if (config != null) {
                        val tuke = Tuke(config)
                        tuke.get(adjustKey, def, useMemory)
                    } else {
                        Tuke.tukeGet(adjustKey, def, useMemory)
                    }
                }
                value as V
            } else value as V
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: V) {
        this.value = value
        val className = thisRef?.javaClass?.name
        var adjustKey = key ?: property.name
        if (className != null) adjustKey = "${className}.${adjustKey}"
        if (config != null) {
            val tuke = Tuke(config)
            tuke.write(adjustKey, value)
        } else {
            Tuke.tukeWrite(adjustKey, value)
        }

    }
}