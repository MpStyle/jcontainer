package mpstyle.jcontainer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

/**
 * Utility class to load a container from a YAML file which collects definition.
 */
public class YamlContainer {
  public static Container from(String filename) {
    return from(new File(filename));
  }

  public static Container from(File file) {
    try {
      InputStream input = new FileInputStream(file);
      Yaml yaml = new Yaml();
      Map<String, String> map = (Map<String, String>) yaml.load(input);
      Container c = new Container();

      for (Map.Entry<String, String> entry : map.entrySet()) {
        Class key = Class.forName(entry.getKey());
        Class clazz = Class.forName(entry.getValue());
        c.addDefinition(key, clazz);
      }

      return c;
    } catch (Exception e) {
      return null;
    }
  }
}
