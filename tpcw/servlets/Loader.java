
package servlets;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

/**
 * Automatically load properties file into static variables.
 *  
 * @author jop@di.uminho.pt
 */
public class Loader {
	/**
	 * Load properties static fields of a class from a properties file.
	 * 
	 * @param clz class to load
	 * @param file file name, found in the current class path
	 * @param prefix prefix added to variable names to get property names
	 */
	public static void load(Class<?> clz, String file, String prefix) {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
		Properties properties = new Properties();		
		try {
			properties.load(inputStream);
			
			for(Field f: clz.getDeclaredFields()) {
				if (!Modifier.isStatic(f.getModifiers()))
					continue;
				String key = f.getName();
				key=prefix+key.replace('_', '.');
				String value = properties.getProperty(key);
				if (value == null)
					continue;
				if (f.getType().equals(String.class))
					f.set(clz, value);
				else if (f.getType().equals(Integer.TYPE))
					f.set(clz, Integer.parseInt(value));
				else if (f.getType().equals(Boolean.TYPE))
					f.set(clz, Boolean.parseBoolean(value));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}
}
