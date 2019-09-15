package com.br.chart;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.List;

public class ChartManager extends JFrame {

    /**
     * Método responsável por criar o gráfico e mostrá-lo na tela
     * @param title Título do gráfico
     * @param xAxis Lista de valores que serão mostrados no eixo X
     * @param yAxis Lista de valores que serão mostrados no eixo Y
     */
    public ChartManager(String title, List<Double> xAxis, List<Double> yAxis) {
        super(title);

        // Create dataset
        XYDataset dataset = createDataset(xAxis,yAxis);

        // Create chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "Precisão",
                "Revocação",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        // Create Panel
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    /**
     * Encapsula os dados e retorna um objeto que será usado para construção do gráfico. (Interno)
     * @param xAxis
     * @param yAxis
     * @return
     */
    private XYDataset createDataset(List<Double> xAxis, List<Double> yAxis) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries series = new XYSeries("");
        for (int i = 0; i < xAxis.size(); i++) {
            series.add(xAxis.get(i), yAxis.get(i));
        }

        //Add series to dataset
        dataset.addSeries(series);

        return dataset;
    }
}