package net.aurore.reflect.system;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Directory extends File{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7927306330946071812L;

	public Directory(String pathname) {
		super(pathname);
		if(!this.isDirectory()) throw new NotADirectoryException(this.getAbsolutePath());
	}
	
	public Directory(URI uri) {
		super(uri);
		if(!this.isDirectory()) throw new NotADirectoryException(this.getAbsolutePath());
	}
	
	public Directory(File f) {
		this(f.toURI());
	}
	
	public static Directory fromURL(URL url) {
		try {
			return new Directory(url.toURI());
		} catch (URISyntaxException e) {
			throw new NotADirectoryException(url.toString());
		}
	}
	
}
