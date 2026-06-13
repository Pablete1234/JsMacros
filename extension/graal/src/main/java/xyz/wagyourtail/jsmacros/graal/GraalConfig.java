package xyz.wagyourtail.jsmacros.graal;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import xyz.wagyourtail.jsmacros.core.config.Option;
import xyz.wagyourtail.jsmacros.core.config.OptionType;

import java.util.HashMap;
import java.util.Map;

public class GraalConfig {

    @Option(translationKey = "jsmacros.settings.languages.extragraaloptions", group = {"jsmacros.settings.languages", "jsmacros.settings.languages.graaloptions"}, type = @OptionType("string"))
    public Map<String, String> extraGraalOptions = new HashMap<>();

    @Deprecated
    public void fromV1(JsonObject v1) {
        JsonObject obj = v1.getAsJsonObject("extraJsOptions");
        if (obj == null) {
            return;
        }
        for (Map.Entry<String, JsonElement> el : obj.entrySet()) {
            extraGraalOptions.put(el.getKey(), el.getValue().getAsString());
        }
        v1.remove("extraJsOptions");
    }

    @Deprecated
    public void fromV2(JsonObject v2) {
        JsonObject v1 = v2.getAsJsonObject("js");
        if (v1 != null && v1.isJsonObject()) {
            JsonObject options = v1.getAsJsonObject("extraGraalOptions");
            if (options != null) {
                for (Map.Entry<String, JsonElement> el : options.entrySet()) {
                    extraGraalOptions.put(el.getKey(), el.getValue().getAsString());
                }
            }
        }
        v2.remove("js");
    }

}
