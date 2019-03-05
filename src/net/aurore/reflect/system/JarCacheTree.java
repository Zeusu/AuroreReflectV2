package net.aurore.reflect.system;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarCacheTree extends CacheTree{
	
	public JarCacheTree(ClassLoader cl, URL url) {
		super(cl,url);
	}

	protected JarCacheTree(ClassLoader cl, String context, File jarFile) {
		super(cl, context);
		build(jarFile);
	}

	@Override
	protected void build(File target) {
		try {
			JarFile jF = new JarFile(target);
			Enumeration<JarEntry> entries = jF.entries();
			while(entries.hasMoreElements()) {
				JarEntry jEntry = entries.nextElement();
				System.out.println(jEntry.getName());
			}
			jF.close();
		} catch (IOException e) {
			throw new RuntimeException("Jar not found: " + target.getAbsolutePath());
		}		
	}

}
