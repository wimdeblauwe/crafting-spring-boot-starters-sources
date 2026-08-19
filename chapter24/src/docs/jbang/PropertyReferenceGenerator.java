///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21
//DEPS tools.jackson.core:jackson-databind:3.0.0

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

public final class PropertyReferenceGenerator {

    public static void main(String[] args) throws Exception {
        Path metadata = Path.of(args[0]);
        Path output = Path.of(args[1]);

        JsonMapper mapper = new JsonMapper();
        JsonNode root = mapper.readTree(metadata.toFile());

        List<JsonNode> properties = new ArrayList<>();
        root.path("properties").forEach(properties::add);
        properties.sort(Comparator.comparing(p -> p.path("name").asString()));

        StringBuilder out = new StringBuilder();
        out.append("[cols=\"1,1,2\",options=\"header\"]\n");
        out.append("|===\n");
        out.append("| Property | Default | Description\n\n");
        for (JsonNode p : properties) {
            String name = p.path("name").asString();
            String defaultValue = p.has("defaultValue")
                    ? p.path("defaultValue").asString()
                    : "—";
            String description = p.path("description").asString("");
            out.append("| `").append(name).append("`\n");
            out.append("| `").append(defaultValue).append("`\n");
            out.append("| ").append(description).append("\n\n");
        }
        out.append("|===\n");

        Files.createDirectories(output.getParent());
        Files.writeString(output, out);
    }
}
