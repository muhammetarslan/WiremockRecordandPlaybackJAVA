package utilities;

import java.io.FileReader;
import java.util.Properties;

public class PropertyReader {
      private  static Properties properties;

    private static Properties loadProperty(){
        FileReader reader=null;
         properties = new Properties();
        try {
            reader = new FileReader(System.getProperty("user.dir") +
                    "\\src\\test\\resources\\application.properties");
            properties.load(reader);
            reader.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return properties;

    }
    public static String getProperty(String key){
        if(properties!=null){
            return properties.getProperty(key);
        } else {
            return loadProperty().getProperty(key);
        }
    }
}
