package ysaak.hexgame.rules;

import ysaak.hexgame.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class GameRules {
	private GameRules() { /**/ }

	private static List<Long> SPAWNABLE_VALUES_LIST = Arrays.asList(1L, 2L, 4L, 8L);
	private static int MIN_SPAWNABLE_ITEM = 3;
	public static int MAX_SPAWNABLE_ITEM = 5;

	public static List<Long> generateItemList() {
		Random random = new Random();

		int nbItem = random.ints(MIN_SPAWNABLE_ITEM, MAX_SPAWNABLE_ITEM + 1)
				.limit(1).findFirst().orElse(MIN_SPAWNABLE_ITEM);

		return IntStream.range(0, nbItem)
				.mapToLong(i -> CollectionUtils.randomItem(SPAWNABLE_VALUES_LIST))
				.boxed()
				.collect(Collectors.toList());
	}
}
