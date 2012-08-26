package com.jimmoores.alife;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.SimpleHistogramDataset;

public class AlifeFrame extends JFrame {

  private static HistogramDataset _ageDataset;
  private static HistogramDataset _energyDataset;
  private WorldPanel _worldPanel;
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
  
  private static final ChartFrame createAgeHistogram() {
    HistogramDataset dataset = new HistogramDataset();
    _ageDataset = dataset;
    JFreeChart histogram = ChartFactory.createHistogram("Age Distribution", "Age", "Frequency", dataset, PlotOrientation.HORIZONTAL, false, false, false);
    ChartFrame frame = new ChartFrame("Age", histogram);
    return frame;
  }
  
  private static final ChartFrame createEnergyHistogram() {
    HistogramDataset dataset = new HistogramDataset();
    _energyDataset = dataset;
    JFreeChart histogram = ChartFactory.createHistogram("Energy Distribution", "Energy", "Frequency", dataset, PlotOrientation.HORIZONTAL, false, false, false);
    ChartFrame frame = new ChartFrame("Energy", histogram);
    return frame;
  }
  
  private static void createStatsFrames() {
    final ChartFrame ageHistogram = createAgeHistogram();
    ageHistogram.pack();
    ageHistogram.setVisible(true);
    final ChartFrame energyHistogram = createEnergyHistogram();
    energyHistogram.pack();
    energyHistogram.setVisible(true);
  }
  
  private static void updateStatsFrames(Statistics stats) {
    _ageDataset.
  }
  /**
   * @param args
   */
  public static void main(String[] args) {
    final AlifeFrame alifeFrame = new AlifeFrame();
    alifeFrame.addWindowListener(new WindowAdapter() {
      public void windowClosed(WindowEvent e) {
        System.exit(0);
      }
    });
    alifeFrame.setVisible(true);

    Thread background = new Thread(new Runnable() {
      public void run() {
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
        World world = new World();
        while (true) {
          world.executeWorldCycle();
          Statistics statistics = world.getStatistics();
          alifeFrame.getWorldPanel().paintWorld(world);
        }
      }
    });
    background.start();
  }

}
