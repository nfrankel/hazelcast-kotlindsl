package org.hazelcast.configdsl

import com.hazelcast.config.*
import com.hazelcast.core.EntryListener
import com.hazelcast.map.MapPartitionLostEvent
import com.hazelcast.map.listener.MapListener
import com.hazelcast.map.listener.MapPartitionLostListener

fun MapConfig.attribute(name: String, extractor: String) =
    AttributeConfig(name, extractor)
        .apply { this@attribute.addAttributeConfig(this) }

fun MapConfig.index(type: IndexType = IndexType.SORTED, vararg attributes: String) =
    IndexConfig(type, *attributes)
        .apply { this@index.addIndexConfig(this) }

fun MapConfig.entryListener(className: String,
                            local: Boolean,
                            includeValue: Boolean) =
    EntryListenerConfig(className, local, includeValue)
        .apply { this@entryListener.addEntryListenerConfig(this) }

fun <K, V> MapConfig.entryListener(implementation: EntryListener<K, V>,
                                   local: Boolean,
                                   includeValue: Boolean) =
    EntryListenerConfig(implementation, local, includeValue)
        .apply { this@entryListener.addEntryListenerConfig(this) }

fun MapConfig.entryListener(implementation: MapListener,
                            local: Boolean,
                            includeValue: Boolean) =
    EntryListenerConfig(implementation, local, includeValue)
        .apply { this@entryListener.addEntryListenerConfig(this) }

fun MapConfig.partitionLostListener(className: String) =
    MapPartitionLostListenerConfig(className)
        .apply { this@partitionLostListener.addMapPartitionLostListenerConfig(this) }

fun MapConfig.partitionLostListener(listener: MapPartitionLostListener) =
    MapPartitionLostListenerConfig(listener)
        .apply { this@partitionLostListener.addMapPartitionLostListenerConfig(this) }

fun MapConfig.partitionLostListener(listener: (MapPartitionLostEvent) -> Unit) =
    MapPartitionLostListenerConfig(listener)
        .apply { this@partitionLostListener.addMapPartitionLostListenerConfig(this) }

fun MapConfig.queryCache(name: String, init: QueryCacheConfig.() -> Unit) =
    QueryCacheConfig(name)
        .apply(init)
        .apply { this@queryCache.addQueryCacheConfig(this) }

fun MapConfig.queryCache(name: String, type: IndexType = IndexType.SORTED, vararg attributes: String): QueryCacheConfig =
    QueryCacheConfig(name)
        .addIndexConfig(IndexConfig(type, *attributes))
        .apply { this@queryCache.addQueryCacheConfig(this) }

fun MapConfig.hotRestart(init: HotRestartConfig.() -> Unit) =
    HotRestartConfig()
        .apply { isEnabled = true }
        .apply(init)
        .apply { this@hotRestart.hotRestartConfig = this }

fun MapConfig.wanReplicationRef(init: WanReplicationRef.() -> Unit) =
    WanReplicationRef()
        .apply(init)
        .apply { this@wanReplicationRef.wanReplicationRef = this }

fun MapConfig.wanReplicationRef(name: String,
                                mergePolicy: String,
                                filters: List<String>,
                                republishingEnabled: Boolean) =
    WanReplicationRef(name, mergePolicy, filters, republishingEnabled)
        .apply { this@wanReplicationRef.wanReplicationRef = this }

class AttributeConfigs {
    internal val attributes = mutableListOf<AttributeConfig>()
    fun attribute(name: String, extractorClassName: String) =
        AttributeConfig(name, extractorClassName)
            .apply { attributes.add(this) }
}

fun MapConfig.attributes(init: AttributeConfigs.() -> Unit) =
    AttributeConfigs()
        .apply(init)
        .apply { this@attributes.attributeConfigs = attributes }

class IndexConfigs {
    internal val indices = mutableListOf<IndexConfig>()
    fun index(type: IndexType = IndexType.SORTED, vararg attributes: String) =
        IndexConfig(type, *attributes)
            .apply { indices.add(this) }
}

fun MapConfig.indices(init: IndexConfigs.() -> Unit) =
    IndexConfigs()
        .apply(init)
        .apply { this@indices.indexConfigs = indices }

class EntryListenerConfigs {

    internal val entryListeners = mutableListOf<EntryListenerConfig>()

    fun entryListener(className: String,
                      local: Boolean,
                      includeValue: Boolean) =
        EntryListenerConfig(className, local, includeValue)
            .apply { entryListeners.add(this) }

    fun <K, V> entryListener(implementation: EntryListener<K, V>,
                             local: Boolean,
                             includeValue: Boolean) =
        EntryListenerConfig(implementation, local, includeValue)
            .apply { entryListeners.add(this) }

    fun entryListener(implementation: MapListener,
                      local: Boolean,
                      includeValue: Boolean) =
        EntryListenerConfig(implementation, local, includeValue)
            .apply { entryListeners.add(this) }
}

fun MapConfig.entryListeners(init: EntryListenerConfigs.() -> Unit) =
    EntryListenerConfigs()
        .apply(init)
        .apply { this@entryListeners.entryListenerConfigs = entryListeners }

class MapPartitionLostListenerConfigs {

    internal val mapPartitionLostListeners = mutableListOf<MapPartitionLostListenerConfig>()

    fun partitionLostListener(className: String) =
        MapPartitionLostListenerConfig(className)
            .apply { mapPartitionLostListeners.add(this) }

    fun partitionLostListener(listener: MapPartitionLostListener) =
        MapPartitionLostListenerConfig(listener)
            .apply { mapPartitionLostListeners.add(this) }
}

fun MapConfig.partitionLostListeners(init: MapPartitionLostListenerConfigs.() -> Unit) =
    MapPartitionLostListenerConfigs()
        .apply(init)
        .apply { this@partitionLostListeners.partitionLostListenerConfigs = mapPartitionLostListeners }

class QueryCacheConfigs {
    internal val queryCaches = mutableListOf<QueryCacheConfig>()
    fun queryCache(name: String, init: QueryCacheConfig.() -> Unit) =
        QueryCacheConfig(name)
            .apply(init)
            .apply { queryCaches.add(this) }

    fun queryCache(name: String,
                   type: IndexType = IndexType.SORTED,
                   vararg attributes: String,
                   init: QueryCacheConfig.() -> Unit): QueryCacheConfig =
        QueryCacheConfig(name)
            .addIndexConfig(IndexConfig(type, *attributes))
            .apply(init)
            .apply { queryCaches.add(this) }

}

fun MapConfig.queryCaches(init: QueryCacheConfigs.() -> Unit) =
    QueryCacheConfigs()
        .apply(init)
        .apply { this@queryCaches.queryCacheConfigs = queryCaches }