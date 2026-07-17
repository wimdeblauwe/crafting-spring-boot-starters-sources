///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21
//DEPS com.fasterxml.jackson.core:jackson-databind:2.18.2

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class PropertyReferenceGenerator {

    public static void main(String[] args) throws Exception {
        Path metadata = Path.of(args[0]);
        Path output = Path.of(args[1]);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(metadata.toFile());

        List<JsonNode> properties = new ArrayList<>();
        root.path("properties").forEach(properties::add);
        properties.sort(Comparator.comparing(p -> p.path("name").asText()));

        StringBuilder out = new StringBuilder();
        out.append("[cols=\"1,1,2\",options=\"header\"]\n");
        out.append("|===\n");
        out.append("| Property | Default | Description\n\n");
        for (JsonNode p : properties) {
            String name = p.path("name").asText();
            String defaultValue = p.has("defaultValue")
                    ? p.path("defaultValue").asText()
                    : "—";
            String description = p.path("description").asText("");
            out.append("| `").append(name).append("`\n");
            out.append("| `").append(defaultValue).append("`\n");
            out.append("| ").append(description).append("\n\n");
        }
        out.append("|===\n");

        Files.createDirectories(output.getParent());
        Files.writeString(output, out);
    }
}
