package org.hazelcast.configdsl

import com.hazelcast.config.AttributeConfig
import com.hazelcast.config.IndexConfig
import com.hazelcast.config.IndexType
import com.hazelcast.config.MapConfig
import com.hazelcast.config.EntryListenerConfig
import com.hazelcast.core.EntryAdapter
import com.hazelcast.map.impl.MapListenerAdapter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MapConfigDslTest {
    private lateinit var mapConfig: MapConfig

    @BeforeEach
    private fun setUp() {
        mapConfig = MapConfig()
    }

    @Nested
    inner class Attribute {
        @Test
        fun `MapConfig can add attribute`() {
            val attribute = AttributeConfig("test", "ext")

            mapConfig.attribute(attribute.name, attribute.extractorClassName)

            assertThat(mapConfig.attributeConfigs).containsExactly(attribute)
        }

        @Test
        fun `MapConfig can add attributes`() {
            val attribute1 = AttributeConfig("test", "ext")
            val attribute2 = AttributeConfig("test2", "ext2")

            mapConfig.attributes {
                attribute(attribute1.name, attribute1.extractorClassName)
                attribute(attribute2.name, attribute2.extractorClassName)
            }

            assertThat(mapConfig.attributeConfigs).containsExactly(attribute1, attribute2)
        }
    }

    @Nested
    inner class Index {
        @Test
        fun `MapConfig can add index`() {
            val index = IndexConfig(IndexType.BITMAP, "1", "2", "3")

            mapConfig.index(index.type, *(index.attributes).toTypedArray())

            assertThat(mapConfig.indexConfigs).containsExactly(index)
        }

        @Test
        fun `MapConfig can add indices`() {
            val index1 = IndexConfig(IndexType.BITMAP, "1", "2", "3")
            val index2 = IndexConfig(IndexType.HASH, "4", "5", "6", "7")

            mapConfig.indices {
                index(index1.type, *(index1.attributes).toTypedArray())
                index(index2.type, *(index2.attributes).toTypedArray())
            }

            assertThat(mapConfig.indexConfigs).containsExactly(index1, index2)
        }
    }

    @Nested
    inner class EntryListener {
        @Test
        fun `MapConfig can add entryListener given className, local, and includeValue`() {
            val entryListener = EntryListenerConfig("className", true, true)

            mapConfig.entryListener(entryListener.className, entryListener.isLocal, entryListener.isIncludeValue)

            assertThat(mapConfig.entryListenerConfigs).containsExactly(entryListener)
        }

        @Test
        fun `MapConfig can add entryListener given implementation as EntryListener, local, and includeValue`() {
            val entryListener = EntryListenerConfig(EntryAdapter<String, String>(), true, false)

            mapConfig.entryListener(entryListener.implementation, entryListener.isLocal, entryListener.isIncludeValue)

            assertThat(mapConfig.entryListenerConfigs).containsExactly(entryListener)
        }

        @Test
        fun `MapConfig can add entryListener given implementation as MapListener, local, and includeValue`() {
            val entryListener = EntryListenerConfig(MapListenerAdapter<Int, String>(), false, false)

            mapConfig.entryListener(entryListener.implementation, entryListener.isLocal, entryListener.isIncludeValue)

            assertThat(mapConfig.entryListenerConfigs).containsExactly(entryListener)
        }

        @Test
        fun `MapConfig can add entryListeners`() {
            val entryListener1 = EntryListenerConfig("className", true, true)
            val entryListener2 = EntryListenerConfig(EntryAdapter<Int, String>(), false, true)
            val entryListener3 = EntryListenerConfig(MapListenerAdapter<Any, String>(), true, false)

            mapConfig.entryListeners {
                entryListener(entryListener1.className, entryListener1.isLocal, entryListener1.isIncludeValue)
                entryListener(entryListener2.implementation, entryListener2.isLocal, entryListener2.isIncludeValue)
                entryListener(entryListener3.implementation, entryListener3.isLocal, entryListener3.isIncludeValue)
            }

            assertThat(mapConfig.entryListenerConfigs).containsExactly(entryListener1, entryListener2, entryListener3)
        }
    }
}