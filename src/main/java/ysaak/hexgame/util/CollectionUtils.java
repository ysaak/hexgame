package ysaak.hexgame.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public final class CollectionUtils {
	private CollectionUtils() { /**/	}

	public static <T> T randomItem(Collection<T> collection) {
		return randomItems(collection, 1).get(0);
	}

	public static <T> List<T> randomItems(Collection<T> collection, int nbItem) {
		final Random rand = new Random();
		final List<T> randomItemList = new ArrayList<>();

		List<T> givenList = new ArrayList<>(collection);

		for (int i = 0; i < nbItem; i++) {
			int randomIndex = rand.nextInt(givenList.size());

			T item = givenList.get(randomIndex);
			randomItemList.add(item);
			givenList.remove(item);
		}

		return randomItemList;
	}

	public static <T> boolean isEmpty(Collection<T> collection) {
		return collection != null && collection.isEmpty();
	}

	public static <T> boolean isNotEmpty(Collection<T> collection) {
		return collection != null && !collection.isEmpty();
	}
}
