package org.hazelcast.configdsl

import com.hazelcast.config.AttributeConfig
import com.hazelcast.config.IndexConfig
import com.hazelcast.config.IndexType
import com.hazelcast.config.MapConfig
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
}