package net.aurore.reflect.util;

import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public final class Classpath {
	
	private static final Classpath INSTANCE = new Classpath();
	
	private final Set<ClassLoader> loaders = new HashSet<>();
	
	private Classpath() {}
	
	public static void register(ClassLoader cl) {
		INSTANCE.loaders.add(cl);
	}
	
	public static Set<URL> getUrls(){
		Set<URL> result = new HashSet<URL>();
		for(ClassLoader cl : INSTANCE.loaders) {
			try {
				Enumeration<URL> urls = cl.getResources("");
				while(urls.hasMoreElements()) {
					result.add(urls.nextElement());
				}
			}catch(Exception e) {}
		}
		return result;
	}
	
	public static ClassLoader contexctCL() {
		return Thread.currentThread().getContextClassLoader();
	}

	@Override
	public String toString() {
		return getUrls().toString();
	}

}
	
