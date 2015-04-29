package com.manhua.toolbox.showInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoContent {

	public static List<InfoItem> ITEMS = new ArrayList<InfoItem>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static Map<Integer, InfoItem> ITEM_MAP = new HashMap<Integer, InfoItem>();

/*	static {
		// Add 3 sample items.
		addItem(new InfoItem("1", "Item 1"));
		addItem(new InfoItem("2", "Item 2"));
		addItem(new InfoItem("3", "Item 3"));
	}*/

	public static void addItem(InfoItem item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}
	
	public static void clearItems() {
		ITEMS.clear();;
		ITEM_MAP.clear();;
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class InfoItem {
		public int id;
		public String content;

		public InfoItem(int id, String content) {
			this.id = id;
			this.content = content;
		}

		@Override
		public String toString() {
			return content;
		}
	}
}
