/*******************************************************************************
 * Copyright (c) 2019 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the LICENSE
 * which accompanies this distribution
 ******************************************************************************/
package dbwr.widgets;

import java.util.concurrent.atomic.AtomicInteger;

import org.w3c.dom.Element;

import dbwr.parser.XMLUtil;

public class XYPlotWidget extends Widget
{
    private static final AtomicInteger id = new AtomicInteger();

    public XYPlotWidget(final ParentWidget parent, final Element xml) throws Exception
	{
		super(parent, xml, "xyplot", 400, 300);

		// Flot lib needs an ID to place plot
		attributes.put("id", "plot" + id.incrementAndGet());

		final Element traces = XMLUtil.getChildElement(xml, "traces");
		if (traces == null)
		    return;

		// Place PV names into data-pvx0, pvy0, pvx1, pvy1, ...
		int i=0;
		for (final Element trace : XMLUtil.getChildElements(traces, "trace"))
		{
		    final String x_pv = XMLUtil.getChildString(parent, trace, "x_pv").orElse("");
		    final String y_pv = XMLUtil.getChildString(parent, trace, "y_pv").orElse("");
            attributes.put("data-pvx" + i, x_pv);
            attributes.put("data-pvy" + i, y_pv);
            attributes.put("data-color" + i, XMLUtil.getColor(trace, "color").orElse(Integer.toString(0)));

            // Use points instead of lines?
            if (XMLUtil.getChildInteger(trace, "point_type").orElse(0) != 0)
                attributes.put("data-pointsize" + i, XMLUtil.getChildInteger(trace, "point_size").orElse(10).toString());
            ++i;
		}
	}
}
