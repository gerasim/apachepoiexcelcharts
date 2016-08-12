package org.devgateway.ocds.web.excelcharts.data;

import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.xmlbeans.XmlObject;
import org.devgateway.ocds.web.excelcharts.CustomChartSeries;
import org.devgateway.ocds.web.excelcharts.util.XSSFChartUtil;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTAxDataSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTNumDataSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;

import java.util.List;

/**
 * @author idobre
 * @since 8/8/16
 *
 * Holds data for a XSSF Pie Chart
 */
public class XSSFPieChartData extends AbstarctXSSFChartData {
    protected CustomChartSeries createNewSerie(int id, int order, ChartDataSource<?> categories,
                                               ChartDataSource<? extends Number> values) {
        return new AbstractSeries(id, order, categories, values) {
            public void addToChart(XmlObject ctChart) {
                CTPieChart ctPieChart = (CTPieChart) ctChart;
                CTPieSer ctPieSer = ctPieChart.addNewSer();
                ctPieSer.addNewIdx().setVal(id);
                ctPieSer.addNewOrder().setVal(order);

                CTAxDataSource catDS = ctPieSer.addNewCat();
                XSSFChartUtil.buildAxDataSource(catDS, categories);

                CTNumDataSource valueDS = ctPieSer.addNewVal();
                XSSFChartUtil.buildNumDataSource(valueDS, values);

                if (isTitleSet()) {
                    ctPieSer.setTx(getCTSerTx());
                }
            }
        };
    }

    public void fillChart(Chart chart, ChartAxis... axis) {
        if (!(chart instanceof XSSFChart)) {
            throw new IllegalArgumentException("Chart must be instance of XSSFChart");
        }

        XSSFChart xssfChart = (XSSFChart) chart;
        CTPlotArea plotArea = xssfChart.getCTChart().getPlotArea();
        CTPieChart pieChart = plotArea.addNewPieChart();
        pieChart.addNewVaryColors().setVal(true);

        for (CustomChartSeries s : series) {
            s.addToChart(pieChart);
        }
    }
}
