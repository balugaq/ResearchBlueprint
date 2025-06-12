package com.balugaq.rb.implementation.initialization;

import com.balugaq.rb.api.cfgparse.annotations.Key;
import com.balugaq.rb.api.cfgparse.annotations.IParsable;
import com.balugaq.rb.implementation.initialization.parts.ResearchConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@ToString
@Data
public class ResearchConfigurations implements IParsable {
    public static final Map<String, ResearchConfiguration> byIdentifier = new HashMap<>();
    @Key(Key.ALL_KEY)
    List<ResearchConfiguration> configurations;

    public static String[] fieldNames() {
        return IParsable.fieldNames(ResearchConfigurations.class);
    }
}
