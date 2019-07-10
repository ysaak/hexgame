package ysaak.hexgame.rules;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestGameRules {

	@Test
	public void testGenerateItemList() {
		// Given
		// --

		// When
		List<Long> itemList = GameRules.generateItemList();

		// Then
		Assert.assertNotNull(itemList);
	}
}
