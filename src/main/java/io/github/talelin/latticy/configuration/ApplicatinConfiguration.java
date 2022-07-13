package io.github.talelin.latticy.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "batchfilespath")
public class ApplicatinConfiguration {
    private List<String> filespaths;
    private List<String> filetypes;
    private Map<String,String> uploadfilesmap;
}
