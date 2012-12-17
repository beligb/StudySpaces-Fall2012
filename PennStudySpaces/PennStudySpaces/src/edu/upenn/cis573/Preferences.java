package edu.upenn.cis573;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Preferences class creates and maintains the Favorites
 * list for a user.
 */
public class Preferences implements Serializable {
	private static final long serialVersionUID = 1L;
	private HashMap<String, Boolean> favorites;
	
	public Preferences() {
		favorites = new HashMap<String, Boolean>();
	}
	
	//Need to test if the array actually doubles in size
	public void addFavorites(String name) {
			favorites.put(name, true);
	}
	
	public void removeFavorites(String name) {
		if(favorites.containsKey(name)) favorites.remove(name);
	}
	
	public boolean isFavorite(String name) {
		if(favorites.containsKey(name)) return true;
		return false;
	}
}
