package net.aurore.reflect.system;

public class NotADirectoryException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3321719737219339687L;
	
	private String name;
	
	public NotADirectoryException(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": File " + name + " is not a directory.";
	}
	
	
}
