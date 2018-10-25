package it.unical.scalab.parsoda.mapping;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import it.unical.scalab.parsoda.common.AbstractMapFunction;
import it.unical.scalab.parsoda.common.Metadata;
import it.unical.scalab.parsoda.common.model.AbstractGeotaggedItem;
import it.unical.scalab.parsoda.common.util.GeoUtils;
import it.unical.scalab.parsoda.common.util.KMLUtils;
import it.unical.scalab.parsoda.common.util.Roi;

public class FindPoI extends AbstractMapFunction {

	private static List<Roi> rois = null;

	public FindPoI() {
		super();
	}

	public FindPoI(String options) {
		super(options);
	}

	@Override
	public AbstractGeotaggedItem apply(AbstractGeotaggedItem g) {

		try {
			if (g.getLocation() == null)
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (rois == null) {
				loadRois();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Roi tmpRoi = null;

		// Search RoI
		for (Roi r : rois) {
			if (GeoUtils.isContained(g.getLocation(), r.getShape())) {
				if (tmpRoi == null || tmpRoi.getAreaSquaredKm() > r.getAreaSquaredKm()) {
					tmpRoi = r;
				}
				break;
			}
		}

		if (tmpRoi != null) {
			g.put(Metadata.LOCATION_NAME, tmpRoi.getName());
			g.put(Metadata.LATITUDE, tmpRoi.getShape().getCenter().getY() + "");
			g.put(Metadata.LONGITUDE, tmpRoi.getShape().getCenter().getX() + "");
		} else {
			return null;
		}
		return g;

	}

	private void loadRois() throws IOException {
		URI uri = null;
		try {
			if (context.getCacheFiles() != null && context.getCacheFiles().length > 0) {
				URI[] uris = context.getCacheFiles();
				uri = uris[0];
			} else {
				throw new Exception("Poi file not found!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		rois = KMLUtils.loadRoi(uri.getRawPath());
	}

}
