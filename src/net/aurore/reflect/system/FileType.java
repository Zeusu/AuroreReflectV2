package net.aurore.reflect.system;

import java.io.File;

public enum FileType {
	JAR( "(.)*\\.jar"),
	CLASS("(.)*\\.class"),
	DIR(null),
	OTHER(null);
	
	public static final String CLASS_EXTENSION = ".class";
	
	private final String regex;
	
	private FileType(String regex) {
		this.regex = regex;
	}
	
	public static FileType typeOf(File f) {
		final String name = f.getName();
		for(FileType t : FileType.values()) {
			if(t.regex != null && name.matches(t.regex)) {
				return t;
			}
		}
		if(f.isDirectory())
			return DIR;
		return OTHER;
	}
	
	public boolean isTypeOf(File file) {
		return file.getName().matches(regex);
	}
	
}
