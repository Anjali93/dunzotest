package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.models.MachineConfig;

import java.io.File;
import java.io.IOException;

public class ConfigParser {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public MachineConfig parseMachineConfig(String fileName) throws IOException {
        return objectMapper.readValue(new File(fileName), MachineConfig.class);
    }
}
