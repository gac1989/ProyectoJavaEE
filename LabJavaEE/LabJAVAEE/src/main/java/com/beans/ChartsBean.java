package com.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.core.Response;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

import com.google.gson.Gson;
import com.model.Chart;
import com.utils.ClientControl;

@ManagedBean(name="ChartsBean")
@RequestScoped
public class ChartsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private BarChartModel barModel;
	private BarChartModel barModel2;
	
	

    public BarChartModel getBarModel() {
		return barModel;
	}

	public void setBarModel(BarChartModel barModel) {
		this.barModel = barModel;
	}

	@PostConstruct
    public void init() {
        createBarModel2();
        createBarModel();
    }

    public List<Chart> obtenerDatosFecha(){
    	String urlRestService = "http://localhost:8080/rest-lab/api/recursos/datosfecha";
		Response response = new ClientControl().realizarPeticion(urlRestService, "GET", null);
		String response2 = response.readEntity(String.class);
        Chart[] c = null;
        if(response2!=null && !response2.isEmpty()){
        	c = new Gson().fromJson(response2, Chart[].class);
        }
        List<Chart> datos = null;
        if(c!=null) {
        	datos = Arrays.asList(c);
        }
		return datos;
    }
    
    public List<Chart> obtenerIngresosFecha(){
    	String urlRestService = "http://localhost:8080/rest-lab/api/recursos/ingresosfecha";
		Response response = new ClientControl().realizarPeticion(urlRestService, "GET", null);
		String response2 = response.readEntity(String.class);
        Chart[] c = null;
        if(response2!=null && !response2.isEmpty()){
        	c = new Gson().fromJson(response2, Chart[].class);
        }
        List<Chart> datos = null;
        if(c!=null) {
        	datos = Arrays.asList(c);
        }
		return datos;
    }
    
    public void createBarModel() {
        barModel = new BarChartModel();
        ChartData data = new ChartData();
        List<Chart> datos = obtenerIngresosFecha();
        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel("Ingresos por mes");
        barDataSet.setBackgroundColor("rgba(17, 193, 56, 0.2)");
        barDataSet.setBorderColor("rgb(17, 193, 56)");
        barDataSet.setBorderWidth(1);
        List<Number> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        for(Chart dato : datos) {
        	values.add(dato.getTotal());
        	labels.add(dato.getMonth());
        }
        barDataSet.setData(values);
        data.addChartDataSet(barDataSet);
        data.setLabels(labels);
        barModel.setData(data);
        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        ticks.setMin(0);
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Bar Chart");
        options.setTitle(title);
        barModel.setOptions(options);
    }
    
    public void createBarModel2() {
        barModel2 = new BarChartModel();
        ChartData data = new ChartData();
        List<Chart> datos = obtenerDatosFecha();
        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel("Ventas por mes");
        barDataSet.setBackgroundColor("rgba(255, 99, 132, 0.2)");
        barDataSet.setBorderColor("rgb(255, 99, 132)");
        barDataSet.setBorderWidth(1);
        List<Number> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        for(Chart dato : datos) {
        	values.add(dato.getTotal());
        	labels.add(dato.getMonth());
        }
        barDataSet.setData(values);
        data.addChartDataSet(barDataSet);
        data.setLabels(labels);
        barModel2.setData(data);
        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        ticks.setMin(0);
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Bar Chart");
        options.setTitle(title);
        barModel2.setOptions(options);
    }


    public BarChartModel getBarModel2() {
        return barModel2;
    }

    public void setBarModel2(BarChartModel barModel2) {
        this.barModel2 = barModel2;
    }

}