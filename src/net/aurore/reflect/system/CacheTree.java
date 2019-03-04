package net.aurore.reflect.system;

import java.net.URL;

public abstract class CacheTree {
	
	protected final ClassLoader contextLoader;
	
	public CacheTree(final ClassLoader cl, final URL url) {
		this.contextLoader = cl;
		build(Directory.fromURL(url));
	}
	
	protected abstract void build(Directory root);
	

}
