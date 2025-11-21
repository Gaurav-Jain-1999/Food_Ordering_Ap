package com.burrito.king.burrito_king_3.models;

import java.util.HashMap;
import java.util.LinkedList;

public class Order {
	private LinkedList<FoodItem> items;

	public Order() {
		items = new LinkedList<>();
	}

	public void addFoodItem(FoodItem newItem) {
		for (FoodItem item : items) {
			if (item.getClass().getName().equals(newItem.getClass().getName())) {
				item.addQuantity(newItem.getQuantity());
				return;
			}
		}
		items.add(newItem);
	}

	public LinkedList<FoodItem> getItems() {
		return this.items;
	}

	public double getTotalPrice() {
		double sum = 0.0;
		for (FoodItem item : items) {
			sum += item.getTotalPrice();
		}
		return sum;
	}

	public double getPrepTime(Restaurant restaurant) {
		HashMap<String, Integer> cookables = this.mapToCookables();
		double cookTimeForBurritos = new Burrito(Restaurant.getPrice("Burrito"), cookables.get("Burritos")).getPreparationTime(restaurant);
		double cookTimeForFries = new Fries(Restaurant.getPrice("Fries"), cookables.get("Fries")).getPreparationTime(restaurant);
		return Math.max(cookTimeForFries, cookTimeForBurritos);
	}

	public HashMap<String, Integer> mapToCookables() {
		int numOfBurritos = 0;
		int numOfFries = 0;
		for (FoodItem item : items) {
			if (item instanceof Burrito) {
				numOfBurritos += item.getQuantity();
			} else if (item instanceof Fries) {
				numOfFries += item.getQuantity();
			}
		}
		HashMap<String, Integer> mapped = new HashMap<>();
		mapped.put("Burritos", numOfBurritos);
		mapped.put("Fries", numOfFries);
		return mapped;
	}
}
