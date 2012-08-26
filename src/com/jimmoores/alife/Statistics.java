package com.jimmoores.alife;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jfree.data.statistics.HistogramDataset;

public class Statistics {
  private static final int HISTORY_SIZE = 100;
  private static final int ENERGY_BUCKETS = 30;
  private static Statistics _instance;
  public static Statistics getInstance() {
    if (_instance == null) {
      _instance = new Statistics();
    }
    return _instance;
  }
  
  private int[] _directionCounts;
  private int[][] _directionCountsHistory;
  private int[] _instructionCounts;
  private int[][] _instructionCountsHistory;
  private int[] _diversityRaw; // holds hashes of 'dna' for for post processing
  private int _diversityRawCount = 0; // num items in array;
  private int[] _diversity; // number of distinct species (cycle)
  private int[] _diversityBuckets; // bottom 16 bits of each hash split into buckets and plotted in 2D  
  private double[] _energyRaw; // holds energy for (x, y) for post processing
  private int _energyRawCount = 0;
  private double _energyMax = 0;
  private int[] _energy; // history of buckets (energy bucket, cycle)
  private int[] _ageBuckets;
  
  private HistogramDataset _energyData = new HistogramDataset();
  
  private Statistics() {
    _directionCounts = new int[Direction.values().length];
    _directionCountsHistory = new int[HISTORY_SIZE][Direction.values().length];
    _instructionCounts = new int[ExecutionEnvironment.getNumRegisteredInstructions()];
    _instructionCountsHistory = new int[HISTORY_SIZE][ExecutionEnvironment.getNumRegisteredInstructions()];
    _diversityRaw = new int[World.WIDTH * World.HEIGHT];
    _diversity = new int[HISTORY_SIZE];
    _diversityBuckets = new int[65536]; // we'll split into 256*256 or something.
    _energyRaw = new double[World.HEIGHT * World.WIDTH];
    _energy = new int[ENERGY_BUCKETS];
    _ageBuckets = new int[50];
  }
  
  public void countInstruction(Instruction instruction) {
    _instructionCounts[instruction.getInstructionIndex()]++;
  }
  
  public void countDirection(Direction direction) {
    _directionCounts[direction.ordinal()]++;
  }
  
  public void countDNA(int signature) {
    _diversityRaw[_diversityRawCount++] = signature;
  }
  
  public void analyseDNA(byte[] dna) { }
  
  public void countEnergy(double amount) {
    _energyRaw[_energyRawCount++] = amount;
    _energyMax = Math.max(amount, _energyMax);
  }
  
  public void countAge(int age) {
    if (age >= _ageBuckets.length) {
      int[] temp = _ageBuckets;
      _ageBuckets = new int[age+1];
      System.arraycopy(temp, 0, _ageBuckets, 0, temp.length);
    }
    _ageBuckets[age]++;
  }
  
  public void processResults() {
    // process energy stats into a distribution
    double bucketSize = (_energyMax * 1.001) / ENERGY_BUCKETS; // 1.001 means we never get the extra top bucket.
    for (int i=0; i<_energyRawCount; i++) {
      int index = Math.max(0, (int)(_energyRaw[i] / bucketSize));
      _energy[index]++;
    }
    Arrays.fill(_energyRaw, 0d);
    _energyRawCount = 0;
    // process directions
    storeHistory(_directionCounts, _directionCountsHistory);
    // process instructions
    storeHistory(_instructionCounts, _instructionCountsHistory);
    // process diversity
    Arrays.fill(_diversityBuckets, 0);
    for (int i=0; i<_diversityRawCount; i++) {
      _diversityBuckets[_diversityRaw[i] & 0xffff]++;
    }
    Arrays.fill(_diversityRaw, 0);
    _diversityRawCount = 0;
  }
  
  
  public void storeHistory(int[] current, int[][] history) {
    shiftHistoryArray(history);
    System.arraycopy(current, 0, history[0], 0, current.length);
    Arrays.fill(current, 0);
  }
  public void shiftHistoryArray(int[][] arr) {
    for (int i=arr.length-1; i>0; i--) {
      arr[i-1] = arr[i];  
    }
  }
  
  public int[] getAgeDistribution() {
    return _ageBuckets;
  }
  
  public int[] getEnergyDistribution() {
    return _energy;
  }
}
