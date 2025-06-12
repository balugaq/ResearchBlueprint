package com.balugaq.rb.implementation.initialization.parts;

import com.balugaq.rb.api.cfgparse.annotations.Key;
import com.balugaq.rb.api.cfgparse.annotations.Parsable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
@Data
public class Regex implements Parsable {
    @Key(Key.ALL_KEY)
    List<RegexDefine> defines;

    public static String[] fieldNames() {
        return Parsable.fieldNames(Regex.class);
    }
}
