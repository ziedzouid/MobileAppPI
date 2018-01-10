/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.demos.kitchen;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.renderers.SimpleSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.PieChart;
import com.codename1.ui.Form;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.Style;

import com.codename1.ui.plaf.UIManager;

/**
 *
 * @author Ahmed
 */
public class ApiStat {
     /**
     * Creates a renderer for the specified colors.
     */
    private DefaultRenderer buildCategoryRenderer(int[] colors) {
        DefaultRenderer renderer = new DefaultRenderer();
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setMargins(new int[]{20, 30, 15, 0});
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

    /**
     * Builds a category series using the provided values.
     *
     * @param titles the series titles
     * @param values the values
     * @return the category series
     */
    protected CategorySeries buildCategoryDataset(String title, double[] values) {
        CategorySeries series = new CategorySeries(title);
        int k = 0;
        //ServiceRandonnee sv = new ServiceRandonnee();
        
 for (double value : values) {
            series.add("comment " + ++k, value);
        }

        return series;
    }


      
    
    public Form createPieChartForm() {

        // Generate the values
        double[] values = new double[]{12000, 7000, 10124, 51140, 45619};

        // Set up the renderer
        int[] colors = new int[]{ColorUtil.BLUE, ColorUtil.GREEN, ColorUtil.MAGENTA, ColorUtil.YELLOW, ColorUtil.CYAN};
        DefaultRenderer renderer = buildCategoryRenderer(colors);
        renderer.setZoomButtonsVisible(true);
        renderer.setZoomEnabled(true);
        renderer.setChartTitleTextSize(20);
        renderer.setDisplayValues(true);
        renderer.setShowLabels(true);
        SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);
        r.setGradientEnabled(true);
        r.setGradientStart(0, ColorUtil.BLUE);
        r.setGradientStop(0, ColorUtil.GREEN);
        r.setHighlighted(true);

        // Create the chart ... pass the values and renderer to the chart object.
        PieChart chart = new PieChart(buildCategoryDataset("prix", values), renderer);

        // Wrap the chart in a Component so we can add it to a form
        ChartComponent c = new ChartComponent(chart);

      
        // Create a form and show it.
        Form f = new Form("Statistique");
        f.setLayout(new BorderLayout());
        f.addComponent(BorderLayout.CENTER, c);
       
        //SeeAllRandonnees cc= new SeeAllRandonnees();
          //Toolbar
        Toolbar.setGlobalToolbar(true);
         
        Style s = UIManager.getInstance().getComponentStyle("TitleCommand");
        //FontImage icon = FontImage.createMaterial(FontImage.MATERIAL_HOME, s);
        
    
        //f.getToolbar().addCommandToLeftBar("Home", icon, (e) -> {
            
            
            
           // Log.p("Clicked");
           //     cc.show();
  
        
                
                
              //  });
      
    
        
     return f;
    }  
    
}
