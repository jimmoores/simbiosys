package com.jimmoores.alife;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;

public class ChartStatistics implements Statistics {
  private static final double AGE_MAX = 4000d;
  private static final double ENERGY_MAX = 4000d;
  private static final int HISTORY_SIZE = 100;
  private static final int ENERGY_BUCKETS = 30;
  private static final double ENERGY_BIN_SIZE = 10;
  private static final int AGE_BIN_SIZE = 10;
  private static Statistics _instance;
  private int[] _instructionCounts;
  private int[] _directionCounts;
  private SimpleHistogramDataset _energyDataset;
  private SimpleHistogramDataset _ageDataset;
  private DefaultCategoryDataset _instructionsDataset;
  private DefaultCategoryDataset _directionDataset;
  
  private ChartStatistics() {
    init();
  }
  
  private void init() {
    _instructionCounts = new int[ExecutionEnvironment.getNumRegisteredInstructions()];
    _directionCounts = new int[Direction.values().length];
    _instructionsDataset = new DefaultCategoryDataset();
    _directionDataset = new DefaultCategoryDataset();
    _energyDataset = new SimpleHistogramDataset("Energy");
//    for (int i = -200; i < 4000; i+=100) {
//      _energyDataset.addBin(new SimpleHistogramBin((double)i, (double)i+100, true, false));
//    }
    _ageDataset = new SimpleHistogramDataset("Age");
//    for (int i = 0; i < 4000; i+=100) {
//      _ageDataset.addBin(new SimpleHistogramBin((double)i, (double)i+100, true, false));
//    }
  }
  
  public static Statistics getInstance() {
    if (_instance == null) {
      _instance = new ChartStatistics();
    }
    return _instance;
  }
  
  @Override
  public void reset() {
    init();
//    Arrays.fill(_instructionCounts, 0);
//    Arrays.fill(_directionCounts, 0);
//    
//    _energyDataset.setAdjustForBinSize(false);
//    _energyDataset.clearObservations();
//    _ageDataset.setAdjustForBinSize(false);
//    _ageDataset.clearObservations();
//    _instructionsDataset.clear();
//    _directionDataset.clear();
  }
  
  @Override
  public void countInstruction(Instruction instruction) {
    _instructionCounts[instruction.getInstructionIndex()]++;
  }
  
  @Override
  public void countDirection(Direction direction) {
  	_directionCounts[direction.ordinal()]++;
  }
  
  @Override
  public void countDNA(int signature) {
  	// TODO Auto-generated method stub
  	
  }
  
  @Override
  public void countEnergy(double amount) {
    //if (amount < ENERGY_MAX) {
      try {
        _energyDataset.addObservation(amount, false);
      } catch (RuntimeException rte) {
        double floor = (amount - (amount % ENERGY_BIN_SIZE));
        if (floor > amount) { // deal with -ve values
          floor -= ENERGY_BIN_SIZE;
        }
        _energyDataset.addBin(new SimpleHistogramBin(floor, floor + ENERGY_BIN_SIZE, true, false));
        _energyDataset.addObservation(amount, false);
      }
    //}
  }
  
  @Override
  public void countAge(int age) {
   // if (age < AGE_MAX) {
      try {
        _ageDataset.addObservation(age, false);
      } catch (RuntimeException rte) {
        double floor = (age - (age % AGE_BIN_SIZE));
        _ageDataset.addBin(new SimpleHistogramBin(floor, floor + AGE_BIN_SIZE, true, false));
        _ageDataset.addObservation(age, false);
      }
  	  //_ageDataset.addObservation(age, false);
   // }
  }
  
  @Override
  public void processResults() {
    Instruction[] registeredInstructions = ExecutionEnvironment.getRegisteredInstructions();
    for (int i = 0; i < ExecutionEnvironment.getNumRegisteredInstructions(); i++) {
      Instruction instruction = registeredInstructions[i];
      int count = _instructionCounts[i];
      _instructionsDataset.addValue(count, "Instructions", instruction.getClass().getSimpleName());
    }
    for (Direction direction : Direction.values()) {
      int count = _directionCounts[direction.ordinal()];
      _directionDataset.addValue(count, "Directions", direction.name());
    }
    _energyDataset.setAdjustForBinSize(true);
    _ageDataset.setAdjustForBinSize(true);
  }
  
  public SimpleHistogramDataset getEnergyDataset() {
    return _energyDataset;
  }
  public SimpleHistogramDataset getAgeDataset() {
    return _ageDataset;
  }
  public DefaultCategoryDataset getInstructionsDataset() {
    return _instructionsDataset;
  }
  public DefaultCategoryDataset getDirectionDataset() {
    return _directionDataset;
  }

}
