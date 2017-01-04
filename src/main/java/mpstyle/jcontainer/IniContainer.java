package mpstyle.jcontainer;

import java.io.File;
import org.ini4j.Profile;
import org.ini4j.Wini;

/**
 * Utility class to load a container from a INI file which collects definition.
 */
public class IniContainer {
  public static Container from(String filename) {
    return from(new File(filename));
  }

  public static Container from(File file) {
    try {
      Wini ini = new Wini(file);
      Container c = new Container();

      for (String sectionName : ini.keySet()) {
        Profile.Section section = ini.get(sectionName);
        for (String optionKey : section.keySet()) {
          Class key = Class.forName(optionKey);
          Class clazz = Class.forName(section.get(optionKey));
          c.addDefinition(key, clazz);
        }
      }

      return c;
    } catch (Exception e) {
      return null;
    }
  }
}
