package com.code4betterhackathon.nutroblast.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.Segment;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;
import com.code4betterhackathon.nutroblast.MainActivity;
import com.code4betterhackathon.nutroblast.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class AnalyticsFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ArrayList<Integer> colorList = new ArrayList<Integer>();
    private PieChart pie;
    private Segment s1;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AnalyticsFragment newInstance(int sectionNumber) {
        AnalyticsFragment fragment = new AnalyticsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public AnalyticsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_analytics, container, false);

        String[] labelArray = new String[] {"dicks", "butts", "dickbutts"};
        Number[][] valuesArray = new Number[][] {
                {1, 8, 5, 2, 7, 4},
                {4, 6, 3, 8, 2, 10},
                {1, 2, 3, 4, 5, 6}};
        drawXYGraph("MySpecialGraph", labelArray, valuesArray, savedInstanceState, rootView);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public void drawXYGraph(String graphTitle, String[] labelArray, Number[][] valuesArray, Bundle savedInstanceState, View rootView) {
        if(labelArray.length!=valuesArray.length) {
            new Exception("labelArray must be equal in length to valuesArray");
        }

        if (savedInstanceState != null){
            colorList = savedInstanceState.getIntegerArrayList("KEY_COLORLIST");
        } else {
            Random random = new Random();
            for(int i=0; i<labelArray.length*3; i++) {
                colorList.add(random.nextInt(256));
            }
        }

        // initialize our XYPlot reference:
        XYPlot plot = (XYPlot) rootView.findViewById(R.id.mySimpleXYPlot);

        for(int i=0, j=0; i<labelArray.length; i++, j+=3) {
            XYSeries series = new SimpleXYSeries(Arrays.asList(valuesArray[i]), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, labelArray[i]);

            LineAndPointFormatter seriesFormat = new LineAndPointFormatter(
                    Color.argb(255, colorList.get(j), colorList.get(j+1), colorList.get(j+2)),
                    Color.argb(255, colorList.get(j), colorList.get(j+1), colorList.get(j+2)),
                    Color.argb(0, 0, 0, 0),
                    new PointLabelFormatter());
            plot.addSeries(series, seriesFormat);
        }
        plot.setTitle(graphTitle);
        plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putIntegerArrayList("KEY_COLORLIST", colorList);
    }

//    protected void updateDonutText() {
//        donutSizeTextView.setText(donutSizeSeekBar.getProgress() + "%");
//    }
}
