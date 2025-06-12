package com.balugaq.rb.implementation.initialization;

import com.balugaq.rb.implementation.cfgparse.annotations.Key;
import com.balugaq.rb.implementation.cfgparse.annotations.Parsable;
import com.balugaq.rb.implementation.initialization.parts.ResearchConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
@Data
public class ResearchConfigurations implements Parsable {
    @Key(Key.ALL_KEY)
    List<ResearchConfiguration> configurations;

    public static String[] fieldNames() {
        return Parsable.fieldNames(ResearchConfigurations.class);
    }
}
