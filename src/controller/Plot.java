package controller;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

public class Plot extends JFrame {

    public Plot(final String title, final XYSeries series) {
        super(title);

        final NumberAxis xAxis = new NumberAxis("Errors part");
        final LogAxis yAxis = new LogAxis("SNR (Db)");

        yAxis.setBase(2);
        yAxis.setTickUnit(new NumberTickUnit(1));

        final XYPlot plot = new XYPlot(new XYSeriesCollection(series), xAxis, yAxis, new XYLineAndShapeRenderer(true, false));
        final JFreeChart chart = new JFreeChart("SNR/Error dependency", JFreeChart.DEFAULT_TITLE_FONT, plot, false);

        setContentPane(new ChartPanel(chart));
    }

}
