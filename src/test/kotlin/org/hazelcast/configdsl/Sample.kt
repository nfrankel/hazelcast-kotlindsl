package org.hazelcast.configdsl

import com.hazelcast.config.InMemoryFormat.OBJECT
import com.hazelcast.config.IndexType
import com.hazelcast.core.EntryAdapter
import com.hazelcast.map.impl.MapListenerAdapter
import com.hazelcast.map.listener.MapPartitionLostListener

fun main() {
    microserviceSample()
}

fun microserviceSample() {
    getOrCreateHazelcastInstance("hazelcastInstance") {
        map("database")
        map("service") {
            inMemoryFormat = OBJECT
        }
        map("empty_session_replication")
        manCenterConfig(true)
        property("jmx.enabled", "true")
    }
}

fun exhaustiveSample() {
    newHazelcastInstance("hazelcastInstance") {
        map("just_a_name")
        map("configure_simple_properties") {
            inMemoryFormat = OBJECT
        }
        map("a_lot_of_stuff") {
            index(attributes = *arrayOf("foo"))
            attribute(name = "my_attr", extractor = "com.hazelcast.extractor.foo")
            entryListener("com.hazelcast.listener.foo", true, true)
            entryListener(EntryAdapter<String, String>(), true, true)
            entryListener(MapListenerAdapter<String, Any>(), true, true)
            partitionLostListener("com.hazelcast.listener.bar")
            partitionLostListener(MapPartitionLostListener { println(it) })
            partitionLostListener { println(it) }
            queryCache("baz") {
                entryListener("com.hazelcast.listener.qux", true, true)
                index(attributes = *arrayOf("qux"))
            }
            hotRestart {
                isFsync = true
            }
        }
        map("multiple") {
            attributes {
                attribute("one", "com.hazelcast.extractor.one")
                attribute(name = "two", extractor = "com.hazelcast.extractor.two")
            }
            indices {
                index(attributes = *arrayOf("one"))
                index(IndexType.SORTED, "two")
                index(IndexType.SORTED, "three")
            }
            entryListeners {
                entryListener("com.hazelcast.listener.foo", true, true)
                entryListener(EntryAdapter<String, String>(), true, true)
                entryListener(MapListenerAdapter<String, Any>(), true, true)
            }
            partitionLostListeners {
                partitionLostListener("com.hazelcast.listener.bar")
                partitionLostListener(MapPartitionLostListener { println(it) })
            }
            queryCaches {
                queryCache("I") {
                    index(attributes = *arrayOf("II")) {
                        batchSize = 5
                    }
                }
                queryCache("III", attributes = *arrayOf("IV")) {
                    bufferSize = 1
                }
                this@map.queryCache("V",IndexType.SORTED, "VI")
            }
            wanReplicationRef("foo", "bar", listOf(), true)
        }
        manCenterConfig(scriptingEnabled = true)
        properties {
            this["jmx.enabled"] = "true"
        }
    }
}