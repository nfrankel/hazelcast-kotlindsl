package org.hazelcast.configdsl

import java.util.*
import com.hazelcast.config.*
import com.hazelcast.core.*
import com.hazelcast.map.listener.MapListener

fun getOrCreateHazelcastInstance(name: String, init: Config.() -> Unit): HazelcastInstance =
    with(Config(name).apply(init)) {
        return Hazelcast.getOrCreateHazelcastInstance(this)
    }

fun newHazelcastInstance(name: String, init: Config.() -> Unit): HazelcastInstance =
    with(Config(name).apply(init)) {
        return Hazelcast.newHazelcastInstance(this)
    }

fun Config.map(name: String, init: MapConfig.() -> Unit = {}) =
    MapConfig(name)
        .apply(init)
        .apply {
            this@map.addMapConfig(this)
        }

fun Config.manCenterConfig(scriptingEnabled: Boolean,
                           init: ManagementCenterConfig.() -> Unit = {}) =
    ManagementCenterConfig()
        .apply(init)
        .apply {
            isScriptingEnabled = scriptingEnabled
            this@manCenterConfig.managementCenterConfig = this
        }

fun QueryCacheConfig.entryListener(className: String, local: Boolean, includeValue: Boolean) =
    EntryListenerConfig(className, local, includeValue)
        .apply { this@entryListener.addEntryListenerConfig(this) }

fun <K, V> QueryCacheConfig.entryListener(implementation: EntryListener<K, V>, local: Boolean, includeValue: Boolean) =
    EntryListenerConfig(implementation, local, includeValue)
        .apply { this@entryListener.addEntryListenerConfig(this) }

fun QueryCacheConfig.entryListener(implementation: MapListener, local: Boolean, includeValue: Boolean) =
    EntryListenerConfig(implementation, local, includeValue)
        .apply { this@entryListener.addEntryListenerConfig(this) }

fun QueryCacheConfig.index(type: IndexType = IndexType.SORTED,
                           vararg attributes: String,
                           init: IndexConfig.() -> Unit = {}) =
    IndexConfig(type, *attributes)
        .apply(init)
        .apply { this@index.addIndexConfig(this) }

fun Config.property(key: String, value: String): Config = setProperty(key, value)
operator fun Config.set(key: String, value: String) = property(key, value)

class ConfigProperties {
    internal val properties = Properties()
    operator fun set(key: String, value: String) = properties.set(key, value)
}

fun Config.properties(init: ConfigProperties.() -> Unit) =
    ConfigProperties()
        .apply(init)
        .apply { this@properties.properties = properties }