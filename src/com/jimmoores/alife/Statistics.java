package com.jimmoores.alife;

public interface Statistics {

	public abstract void countInstruction(Instruction instruction);

	public abstract void countDirection(Direction direction);

	public abstract void countDNA(int signature);

	public abstract void countEnergy(double amount);

	public abstract void countAge(int age);

	public abstract void processResults();

  public abstract void reset();

}