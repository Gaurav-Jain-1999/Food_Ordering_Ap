package com.burrito.king.burrito_king_3.models;

public class Burrito extends FoodItem implements Cookable {
	private static final int batchPrepTime = 9;
	private static final int batchSize = 2;

	public Burrito(double price, int quantity) {
		super(price, quantity);
	}

	@Override
	public int getPreparationTime(Restaurant restaurant) {
		return batchPrepTime * ((int) Math.ceil(this.getQuantity() / ((double) batchSize)));
	}

	@Override
	public int getActualQuantityCooked(Restaurant restaurant) {
		return this.getQuantity();
	}
}
