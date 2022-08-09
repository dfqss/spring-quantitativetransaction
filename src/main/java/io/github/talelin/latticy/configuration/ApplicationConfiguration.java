package io.github.talelin.latticy.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "file-config")
public class ApplicationConfiguration {

    private List<String> batchFilesPath;

    private List<String> fileTypes;

    private Map<String,String> uploadFilesMap;
}
