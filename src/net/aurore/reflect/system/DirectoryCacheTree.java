package net.aurore.reflect.system;

import java.io.File;
import java.net.URL;

public class DirectoryCacheTree extends CacheTree {
	
	public DirectoryCacheTree(ClassLoader cl, URL url) {
		super(cl,url);
	}
	
	protected DirectoryCacheTree(ClassLoader cl, Directory dir, String context) {
		super(cl,context);
		buildFromDir(dir);
	}

	@Override
	protected void build(File target) {
		buildFromDir(new Directory(target));
	}
	
	private void buildFromDir(Directory dir) {
		this.name = dir.getName();
		for(File f : dir.listFiles()) {
			buildChild(f);
		}
	}

}
