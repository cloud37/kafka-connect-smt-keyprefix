package com.github.cloud37.kafka.connect.smt;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.transforms.Transformation;
import org.apache.kafka.connect.data.Schema;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.transforms.util.SimpleConfig;

import java.util.Map;
@Slf4j
public class KeyPrefix<S extends ConnectRecord<S>> implements Transformation<S> {

    public static final ConfigDef CONFIG_DEF;
    private String prefix;

    static {
        CONFIG_DEF = (new ConfigDef()).define("prefix", ConfigDef.Type.STRING, ConfigDef.NO_DEFAULT_VALUE, ConfigDef.Importance.MEDIUM, "Redis key prefix to add.");
    }

    @Override
    public void configure(Map<String, ?> props) {
        SimpleConfig config = new SimpleConfig(CONFIG_DEF, props);
        this.prefix = config.getString("prefix");
    }

    @Override
    public S apply(S record) {
        Object key = record.key();
        if (key instanceof String) {
            String keyPrefix = keyPrefix((String) key);
            return record.newRecord(
                    record.topic(),
                    record.kafkaPartition(),
                    Schema.STRING_SCHEMA,
                    keyPrefix,
                    record.valueSchema(),
                    record.value(),
                    record.timestamp()
            );
        } else {
            throw new ConfigException("Key is null or not a string, skipping transformation for record", record);
        }
    }

    @Override
    public ConfigDef config() {
        return CONFIG_DEF;
    }

    @Override
    public void close() {
        // Clean up if necessary
    }

    private String keyPrefix(String originalKey) {
        return prefix + originalKey;
    }
}
