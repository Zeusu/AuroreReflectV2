package net.aurore.reflect.util;

import java.io.File;
import java.net.URL;

public final class FileUtil {
	
	public static File fromUrl(URL url) {
		try {
			return new File(url.toURI());
		}catch(Exception e) {
			throw new RuntimeException("URL error:" + url.toString());
		}
	}

}
