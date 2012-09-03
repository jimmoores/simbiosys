package com.jimmoores.alife;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.lang.model.type.NullType;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.SeriesChangeEvent;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.SimpleHistogramDataset;

public class AlifeFrame extends JFrame {

  private static SimpleHistogramDataset _ageDataset;
  private static SimpleHistogramDataset _energyDataset;
  private static JFreeChart _energyHistogram;
  private static JFreeChart _ageHistogram;
  private WorldPanel _worldPanel;
  private static ChartFrame _energyFrame;
  private static ChartFrame _ageFrame;
  private static DefaultCategoryDataset _instructionDataset;
  private static JFreeChart _instructionBarChart;
  private static ChartFrame _instructionFrame;
  private static JFreeChart _directionBarChart;
  private static ChartFrame _directionFrame;
  private static DefaultCategoryDataset _directionDataset;
  public AlifeFrame() {
    setLayout(new BorderLayout());
    _worldPanel = new WorldPanel();
    JScrollPane scrollPane = new JScrollPane(_worldPanel);
    JPanel topPanel = new JPanel(new BorderLayout(), false);
    topPanel.add(scrollPane, BorderLayout.CENTER);

    add(topPanel, BorderLayout.CENTER);
    
    setPreferredSize(new Dimension(World.WIDTH, World.HEIGHT));
    setMaximumSize(new Dimension(World.WIDTH, World.HEIGHT));
  }
  
  public WorldPanel getWorldPanel() {
    return _worldPanel;
  }
  
  private static final ChartFrame createAgeHistogram(Statistics statistics) {
    _ageHistogram = createAgeHistogramComponent(statistics);
    _ageFrame = new ChartFrame("Age", _ageHistogram);
    return _ageFrame;
  }

  private static JFreeChart createAgeHistogramComponent(Statistics statistics) {
    SimpleHistogramDataset dataset = ((ChartStatistics) statistics).getAgeDataset();
    _ageDataset = dataset;
    return ChartFactory.createHistogram("Age Distribution", "Age", "Frequency", dataset, PlotOrientation.HORIZONTAL, false, false, false);
  }
  
  private static final ChartFrame createEnergyHistogram(Statistics statistics) {
    _energyHistogram = createEnergyHistogramComponent(statistics);
    _energyFrame = new ChartFrame("Energy", _energyHistogram);
    return _energyFrame;
  }

  private static JFreeChart createEnergyHistogramComponent(Statistics statistics) {
    SimpleHistogramDataset dataset = ((ChartStatistics) statistics).getEnergyDataset();
    _energyDataset = dataset;
    return ChartFactory.createHistogram("Energy Distribution", "Energy", "Frequency", dataset, PlotOrientation.HORIZONTAL, false, false, false);
  }
  
  private static final ChartFrame createInstructionBarChart(Statistics statistics) {
    _instructionBarChart = createInstructionBarChartComponent(statistics);
    _instructionFrame = new ChartFrame("Instructions", _instructionBarChart);
    return _instructionFrame;
  }
  
  private static JFreeChart createInstructionBarChartComponent(Statistics statistics) {
    DefaultCategoryDataset instructionsDataset = ((ChartStatistics) statistics).getInstructionsDataset();
    _instructionDataset = instructionsDataset;
    return ChartFactory.createBarChart("Instruction Use", "Instruction", "Frequency", instructionsDataset, PlotOrientation.HORIZONTAL, false, false, false);
  }
  
  private static final ChartFrame createDirectionBarChart(Statistics statistics) {
    _directionBarChart = createDirectionBarChartComponent(statistics);
    _directionFrame = new ChartFrame("Directions", _directionBarChart);
    return _directionFrame;
  }
  
  private static JFreeChart createDirectionBarChartComponent(Statistics statistics) {
    DefaultCategoryDataset directionDataset = ((ChartStatistics) statistics).getDirectionDataset();
    _directionDataset = directionDataset;
    return ChartFactory.createBarChart("Direction Use", "Directions", "Frequency", directionDataset, PlotOrientation.HORIZONTAL, false, false, false);
  }
  
  private static void createStatsFrames(Statistics statistics) {
    final ChartFrame ageHistogram = createAgeHistogram(statistics);
    ageHistogram.pack();
    ageHistogram.setVisible(true);
    final ChartFrame energyHistogram = createEnergyHistogram(statistics);
    energyHistogram.pack();
    energyHistogram.setVisible(true);
    final ChartFrame instructionBarChart = createInstructionBarChart(statistics);
    instructionBarChart.pack();
    instructionBarChart.setVisible(true);
    final ChartFrame directionBarChart = createDirectionBarChart(statistics);
    directionBarChart.pack();
    directionBarChart.setVisible(true);
  }
  
  private static void updateStatsFrames(final Statistics stats) {
    SwingWorker<NullType, NullType> worker = new SwingWorker<NullType, NullType>() {
      @Override
      protected NullType doInBackground() throws Exception {
        _ageFrame.getChartPanel().setChart(createAgeHistogramComponent(stats));
        _ageFrame.pack();
        _energyFrame.getChartPanel().setChart(createEnergyHistogramComponent(stats));
        _energyFrame.pack();
        _instructionFrame.getChartPanel().setChart(createInstructionBarChartComponent(stats));
        _instructionFrame.pack();
        _directionFrame.getChartPanel().setChart(createDirectionBarChartComponent(stats));
        _directionFrame.pack();
        //_ageHistogram.get
        //_energyHistogram.fireChartChanged();
        //_ageHistogram.fireChartChanged();
        return null;
      }
    };
    worker.execute();
  }
  /**
   * @param args
   */
  public static void main(String[] args) {
    final AlifeFrame alifeFrame = new AlifeFrame();
    alifeFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    alifeFrame.setFocusableWindowState(true);
    alifeFrame.pack();
    alifeFrame.setVisible(true);
    alifeFrame.setTitle("Simbiosys");
    
    ExecutionEnvironment.registerInstruction(new EatInstruction());
    ExecutionEnvironment.registerInstruction(new AsexualReproduceInstruction());
    ExecutionEnvironment.registerInstruction(new AttackInstruction());
    ExecutionEnvironment.registerInstruction(new BaskInstruction());
    ExecutionEnvironment.registerInstruction(new ClearConditionInstruction());
    ExecutionEnvironment.registerInstruction(new CompareRegistersInstruction());
    ExecutionEnvironment.registerInstruction(new ConditionalBranchInstruction());
    ExecutionEnvironment.registerInstruction(new DecrementIndexInstruction());
    ExecutionEnvironment.registerInstruction(new IncrementIndexInstruction());
    ExecutionEnvironment.registerInstruction(new LoadIndexInstruction());
    ExecutionEnvironment.registerInstruction(new MoveInstruction());
    ExecutionEnvironment.registerInstruction(new NullInstruction());
    ExecutionEnvironment.registerInstruction(new SetConditionInstruction());
    ExecutionEnvironment.registerInstruction(new SexualReproduceInstruction());
    ExecutionEnvironment.registerInstruction(new SmellInstruction());
    ExecutionEnvironment.registerInstruction(new TransferInstruction());
    
    final World world = new World();
    final Statistics statistics = world.getStatistics();
    
    createStatsFrames(statistics);
    Thread background = new Thread(new Runnable() {
      public void run() {
        long cycleNum = 0L;
        while (true) {
          statistics.reset();
          world.executeWorldCycle();
          statistics.processResults();
          world.getWorldParameters().setCycleNumber(++cycleNum);
          System.out.println("Bask delta = " + world.getWorldParameters().getBaskDelta());
          updateStatsFrames(statistics);
          alifeFrame.getWorldPanel().paintWorld(world);
        }
      }
    });
    background.start();
  }

}
