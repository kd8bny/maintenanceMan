/*package com.kd8bny.maintenanceman.charts;

if (vehicleHist != null) {
        headerColors = view.getResources().obtainTypedArray(R.array.header_color);
        TextView vTitle = (TextView) view.findViewById(R.id.card_info_title);
        ImageView imageView = (ImageView) view.findViewById(R.id.card_info_iv);
        imageView.setBackgroundColor(headerColors.getColor(1, 0));

        vTitle.setText(view.getResources().getString(R.string.header_chart));
        vTitle.setMinWidth(width / 3);
        vTitle.setMaxWidth(width);

        view.animate();

        HorizontalBarChart mChart = (HorizontalBarChart) view.findViewById(R.id.chart);
        mChart.setDrawValueAboveBar(true);
        //mChart.setDrawHighlightArrow(true);
        mChart.setDescription(null);
        mChart.getLegend().setEnabled(false);
        mChart.getAxisLeft().setEnabled(false);
        mChart.getAxisRight().setEnabled(false);
        mChart.setGridBackgroundColor(Color.TRANSPARENT);
        mChart.setTouchEnabled(false);
        //mChart.setFitBars(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setGridLineWidth(0.3f);
        xAxis.setTextColor(ContextCompat.getColor(view.getContext(), R.color.secondary_text));
        xAxis.setTextSize(18f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new MyXAxisValueFormatter(xLegend));

        try {
        for (int i = 0; i < vehicleHist.size(); i++) {
        ArrayList<String> tempEvent = vehicleHist.get(i);
        String[] dateArray = tempEvent.get(0).split("/");
        int month = Integer.parseInt(dateArray[0]);
        int year = Integer.parseInt(dateArray[2]);
        int yearOffset = Calendar.getInstance().get(Calendar.YEAR) - year;

        if (yearOffset != 0) {
        //Lets take that old boy and put it in a mod 12
        month += (12 * yearOffset);
        }

        if (costHistory.containsKey(month)) {
        costHistory.put(month, costHistory.get(month) + Float.parseFloat(tempEvent.get(1)));
        } else {
        costHistory.put(month, Float.parseFloat(tempEvent.get(1)));
        }
        }

        int i = 0;
        for (int key : costHistory.keySet()) {
        if (i > 3) {
        break;
        }

        xLegend.add(months[(key - 1) % 12]);
        barEntries.add(new BarEntry(i, costHistory.get(key)));
        i++;
        }



        Log.d(TAG, costHistory.toString());
        Log.d(TAG, barEntries.toString());
        Log.d(TAG, xLegend.toString());

        BarDataSet barDataSet = new BarDataSet(barEntries, null);
        barDataSet.setColor(headerColors.getColor(6, 0));

        BarData data = new BarData(barDataSet);
        data.setValueTextSize(10f);
        data.setBarWidth(0.9f);

        mChart.setData(data);
        mChart.animateY(2500);

        } catch (NumberFormatException e) {
        //e.printStackTrace();
        Log.i(TAG, "No date found");
        }
        }
        }*/