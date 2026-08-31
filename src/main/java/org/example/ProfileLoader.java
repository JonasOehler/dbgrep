package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Collections;
import java.util.Map;

public class ProfileLoader {
    public static Map<String, DatabaseConfig> loadProfiles(String filename) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(
                    new File(filename),
                    new TypeReference<>() {}
            );
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }
}