package net.aurore.reflect.util;

import java.util.LinkedList;
import java.util.Queue;

public final class PathUtil {

	public static Queue<String> pathAsQueue(String path){
		Queue<String> result = new LinkedList<>();
		String[] sPath = path.split("\\.");
		for(String s : sPath) {
			result.offer(s);
		}
		return result;
	}
	
}
