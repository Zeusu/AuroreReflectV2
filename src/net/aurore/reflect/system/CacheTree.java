package net.aurore.reflect.system;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import net.aurore.reflect.util.Classpath;
import net.aurore.reflect.util.FileUtil;
import net.aurore.reflect.util.PathUtil;

public abstract class CacheTree {
	
	protected final ClassLoader contextLoader;

	
	protected final Map<String,CacheTree> branchs;
	protected final Set<String> leaves;
	protected String context;
	protected String name;
	
	private final boolean isRoot;
	
	public CacheTree(final ClassLoader cl, final URL url) {
		contextLoader = cl;
		branchs = new HashMap<>();
		leaves = new HashSet<>();
		context = "";
		isRoot = true;
		build(FileUtil.fromUrl(url));
	}
	
	protected CacheTree(final ClassLoader cl, final String context) {
		contextLoader = cl;
		branchs = new HashMap<>();
		leaves = new HashSet<>();
		isRoot = false;
		this.context = context;
	}
	
	//ACTIVE METHODS
	
	public Class<?> findClass(String path) throws ClassNotFoundException{
		return findClass(PathUtil.pathAsQueue(path));
	}
	
	private Class<?> findClass(Queue<String> path) throws ClassNotFoundException{
		String item = path.poll();
		if(path.isEmpty() && leaves.contains(item)) {
			return contextLoader.loadClass(getFullName() + "." + item);
		}else if (branchs.containsKey(item)) {
			return branchs.get(item).findClass(path);
		}
		return null;
	}
	
	public Set<Class<?>> findClasses(String path) throws ClassNotFoundException{
		return findClasses(PathUtil.pathAsQueue(path));
	}
	
	private Set<Class<?>> findClasses(Queue<String> path) throws ClassNotFoundException{
		CacheTree tree = findTree(path);
		Set<Class<?>> result = new HashSet<Class<?>>();
		if(tree != null)
			tree.loadClasses(result);
		return result;
	}
	
	private CacheTree findTree(Queue<String> path){
		final String name = path.poll();
		if(branchs.containsKey(name)) {
			CacheTree next = branchs.get(name);
			if(path.isEmpty())
				return next;
			return next.findTree(path);
		}
		return null;
	}
	
	private void loadClasses(Set<Class<?>> result) throws ClassNotFoundException{
		for(String className : leaves) {
			result.add(contextLoader.loadClass(getFullName() + "." + className));
		}
		for(Entry<String,CacheTree> e : branchs.entrySet()) {
			e.getValue().loadClasses(result);
		}
	}
		
	
	//CONSTRUCTIONS METHODS

	protected abstract void build(File target);

	protected final void buildChild(File child) {
		switch (FileType.typeOf(child)) {
			case DIR:
				branchs.put(child.getName(), new DirectoryCacheTree(contextLoader,new Directory(child),getFullName()));
				break;
			case JAR:
				branchs.put(child.getName(), new JarCacheTree(contextLoader, context, child));
				break;
			case CLASS:
				leaves.add(child.getName().replace(FileType.CLASS_EXTENSION, ""));
				break;
			case OTHER:
				break;
		}
	}
	
	
	
	//UTILITY METHODS
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	public final String getName() {
		return name;
	}
	
	public final String getFullName() {
		if(isRoot)
			return "";
		if(!context.equals("")) {
			return context + "." + name;
		}
		return name;
	}
	
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		return toString(buffer," ");
	}
	
	public String toString(StringBuffer buffer, String prefix) {
		buffer.append(prefix);
		buffer.append(getFullName());
		buffer.append('[');
		buffer.append(leaves.size());
		buffer.append(']');
		buffer.append('\n');
		prefix += prefix.charAt(0);
		for(String s : leaves) {
			buffer.append(prefix);
			buffer.append(s);
			buffer.append('\n');
		}
		for(Map.Entry<String, CacheTree> e : branchs.entrySet()) {
			e.getValue().toString(buffer,prefix);
		}
		return buffer.toString();
	}
	
	public static void main(String[] args) {
		Classpath.register(Classpath.contexctCL());
		Classpath.register(ClassLoader.getSystemClassLoader());
		for(URL url : Classpath.getUrls()) {
			try {
				System.out.println(url);
				CacheTree tree = new DirectoryCacheTree(Classpath.contexctCL(), url);
				System.out.println(tree);
				/*System.out.println(tree.findClass("net.aurore.reflect.util.Classpath"));
				System.out.println(tree.findClasses("net.aurore.reflect.system"));*/
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
