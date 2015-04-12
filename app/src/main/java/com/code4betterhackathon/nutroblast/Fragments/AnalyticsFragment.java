package com.code4betterhackathon.nutroblast.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
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

    private TextView donutSizeTextView;
    private SeekBar donutSizeSeekBar;

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

        String[] labelArray = new String[] {"Calorie Intake", "Iron Intake", "Glucose Intake"};
        Number[][] valuesArray = new Number[][] {
                {1, 8, 5, 2, 7, 4},
                {4, 6, 3, 8, 2, 10},
                {1, 2, 3, 4, 5, 6}};
        drawXYGraph("My Nutrient Records", labelArray, valuesArray, savedInstanceState, rootView);

//        String[] labelArray = new String[] {"dicks", "butts", "dickbutts"};
//        Number[] valuesArray = new Number[] {1, 4, 10};
//        drawPieChart("MySpecialGraph", labelArray, valuesArray, savedInstanceState, rootView);

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

    public void drawPieChart(String graphTitle, String[] labelArray, Number[] valuesArray, Bundle savedInstanceState, View rootView) {
        if (labelArray.length != valuesArray.length) {
            new Exception("labelArray must be equal in length to valuesArray");
        }

        if (savedInstanceState != null) {
            colorList = savedInstanceState.getIntegerArrayList("KEY_COLORLIST");
        } else {
            Random random = new Random();
            for (int i = 0; i < labelArray.length * 3; i++) {
                colorList.add(random.nextInt(256));
            }
        }

//        setContentView(R.layout.pie_chart);

        // initialize our XYPlot reference:
        pie = (PieChart) rootView.findViewById(R.id.mySimplePieChart);

        // detect segment clicks:
        pie.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                PointF click = new PointF(motionEvent.getX(), motionEvent.getY());
                if (pie.getPieWidget().containsPoint(click)) {
                    Segment segment = pie.getRenderer(PieRenderer.class).getContainingSegment(click);
                    if (segment != null) {
                        // handle the segment click...for now, just print
                        // the clicked segment's title to the console:
                        System.out.println("Clicked Segment: " + segment.getTitle());
                    }
                }
                return false;
            }
        });

        donutSizeSeekBar = (SeekBar) rootView.findViewById(R.id.donutSizeSeekBar);

        donutSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                pie.getRenderer(PieRenderer.class).setDonutSize(seekBar.getProgress() / 100f,
                        PieRenderer.DonutMode.PERCENT);
                pie.redraw();
                updateDonutText();
            }
        });

        donutSizeTextView = (TextView) rootView.findViewById(R.id.donutSizeTextView);
        updateDonutText();

        EmbossMaskFilter emf = new EmbossMaskFilter(
                new float[]{1, 1, 1}, 0.4f, 10, 8.2f);

        for (int i = 0, j = 0; i < labelArray.length; i++, j += 3) {
            s1 = new Segment(labelArray[i], valuesArray[i]);
            SegmentFormatter sf1 = new SegmentFormatter(
                    Color.argb(255, colorList.get(j), colorList.get(j + 1), colorList.get(j + 2)),
                    255);
//            sf1.configure(getApplicationContext(), R.xml.pie_segment_formatter1);

            sf1.getFillPaint().setMaskFilter(emf);
            pie.addSeries(s1, sf1);
        }

        pie.setTitle(graphTitle);
        pie.getBorderPaint().setColor(Color.TRANSPARENT);
        pie.getBackgroundPaint().setColor(Color.TRANSPARENT);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putIntegerArrayList("KEY_COLORLIST", colorList);
    }

    protected void updateDonutText() {
        donutSizeTextView.setText(donutSizeSeekBar.getProgress() + "%");
    }
}
