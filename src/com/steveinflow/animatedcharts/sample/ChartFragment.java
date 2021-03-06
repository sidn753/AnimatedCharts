package com.steveinflow.animatedcharts.sample;

import java.util.HashMap;
import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.steveinflow.animatedcharts.linechart.LineChart;
import com.steveinflow.animatedcharts.linechart.LineChart.LinePoint;
import com.steveinflow.animatedcharts.linechart.LineChartDataset;
import com.steveinflow.animatedcharts.piechart.PieChart;
import com.steveinflow.animatedcharts.piechart.PieChartDataset;
import com.steveinflow.animcharts.R;

public class ChartFragment extends android.support.v4.app.Fragment implements LineChart.LineChartParent{
	PieChart pc;
	LineChart lc;
	ViewGroup container;
	Random rand;
	PieChartDataset pieData;
	HashMap<String, Double> myMap;
	boolean pieChartShowing = false;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.chart_container, container, false);
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		init();
	}

	public void init(){
		container = (RelativeLayout)getView().findViewById(R.id.container);
		container.setBackgroundColor(Color.WHITE);
		initCharts(null);
	}
	
	public void initCharts(Point p) {
		
		myMap = generateData();
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point(); 
		display.getSize(size);
		
		int containerWidth = size.x;
		int containerHeight = size.y;
		int smaller = Math.min(containerWidth, containerHeight);
		
		
		int pieX = smaller / 4;
		int pieY = smaller * 100/1080;
		int pieRadius = (int) (smaller * 400/1080);
		
		int lineX = smaller * 100/1080;
		int lineY = lineX;
		int lineWidth = (int) (smaller * 800/1080);
		int lineHeight = lineWidth/2;
		
		PieChartDataset pieData = new PieChartDataset(myMap);
		LineChartDataset lineData = new LineChartDataset(myMap);
		
		//context, x, y, width, height,  dataset
		pc = new PieChart(this, pieX, pieY, pieRadius, pieRadius,  pieData);
		lc = new LineChart(this, lineX, lineY, lineWidth, lineHeight,  lineData);
		if(p != null) lc.setPointToExpandFrom(p);
		showChart();
		
		Log.d("dimens", String.format("container dimens: width %s, height %s ", containerWidth, containerHeight));
		Log.d("dimens", String.format("pieR %s, pieX %s, pieY %s",pieRadius, pieX, pieY));
		Log.d("dimens", String.format("lineW %s, lineH %s, lineX %s, lineY %s", lineWidth, lineHeight, lineX, lineY));
		((MainActivity)getActivity()).setData(myMap);
	}

	private void showChart() {
		if(pieChartShowing){
			container.removeAllViews();
			container.addView(pc);
			pc.replayAnimation();
		}
		else{
			container.removeAllViews();
			container.addView(lc);
			lc.replayAnimation();
		}
		
	}

	private HashMap<String, Double> generateData() {
		myMap =  new HashMap<String, Double>();
		rand = new Random();
		final int powerOf10 = rand.nextInt(4) + 1;
		final int numItems = rand.nextInt(3) + 3;
		
		for(int i = 0; i < numItems; i++){
			Double d = ((double)rand.nextInt((int)Math.pow(10, powerOf10)));
			String s = "Item " + (i +1) ;
			myMap.put(s, d);
		}
		
		return myMap;
	}
	
	public HashMap<String, Double> getData(){
		return myMap;
	}
	
	public void arcClicked(int arcIndex){
		((MainActivity)getActivity()).chartItemClicked(arcIndex);
	}
	
	public void inflateItem(int item){
		if(pieChartShowing){
			pc.inflateItem(item);
		}
		else lc.inflatePoint(item);
	}
	
	public void newData(){
		initCharts(null);
	}
	
	@Override
	public void LineChartItemClicked(int pointIndex) {
		((MainActivity)getActivity()).chartItemClicked(pointIndex);
	}

	@Override
	public Context getContext() {
		return getActivity();
	}

	public void changeChart() {
		pieChartShowing = !pieChartShowing;
		showChart();
	}


	@Override
	public void LineChartItemDoubleClicked(LinePoint where) {
		initCharts(where.getLocation());
	}
}
