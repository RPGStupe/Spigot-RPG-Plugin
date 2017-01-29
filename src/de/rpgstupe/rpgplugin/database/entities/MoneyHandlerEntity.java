package de.rpgstupe.rpgplugin.database.entities;

import org.mongodb.morphia.annotations.Id;

import de.rpgstupe.rpgplugin.player.MoneyHandler;

public class MoneyHandlerEntity {
	
	@Id
	public int id;
	
	public int moneySmallAmount;
	public int moneyMediumAmount;
	public int moneyLargeAmount;
	
	public MoneyHandlerEntity() {
		
	}
	
	public MoneyHandlerEntity(MoneyHandler moneyHandler) {
		this(moneyHandler.getMoneySmallAmount(), moneyHandler.getMoneyMediumAmount(), moneyHandler.getMoneyLargeAmount());
	}

	public MoneyHandlerEntity(int moneySmallAmount, int moneyMediumAmount, int moneyLargeAmount) {
		this.moneySmallAmount = moneySmallAmount;
		this.moneyMediumAmount = moneyMediumAmount;
		this.moneyLargeAmount = moneyLargeAmount;
	}
}
