package it.unical.scalab.parsoda.acquisition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.SearchParameters;
import com.google.gson.Gson;

public class FlickrCrawler extends AbstractCrawlerFunction {

	public FlickrCrawler() {
		super();
	}

	public FlickrCrawler(String params) {
		super(params);
	}

	Flickr f;
	OutputStream out = null;
	Gson gson;
	Set<String> extras;
	int numTotalPhotos;
	int numPartialPhotos;
	int numPerFlush;
	int photosPerFile;

	SimpleDateFormat dateFormat;
	String fileId;
	PrintWriter pwStatus;
	SearchParameters searchParameters;
	PhotoList<Photo> list;
	Date maxTakenDate, minTakenDate;
	Set<String> photoIds;
	int countDistinctPhotos = 0;
	int error_count = 0;
	int duplicatedCount = 0;
	int maxDuplicatedCount = 250;
	int maxPhotosPerRun = 4000;

	private void init() {
		numTotalPhotos = 0;
		numPartialPhotos = 0;
		numPerFlush = 250;
		photosPerFile = 10000;

		dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		photoIds = new HashSet<String>();

		maxTakenDate = new Date();

		gson = new Gson();

		extras = new HashSet<String>(Arrays.asList("description", "date_upload", "date_taken", "owner_name",
				"last_update", "geo", "tags", "machine_tags", "views", "media"));

		fileId = this.outputPath + dateFormat.format(new Date()) + ".txt";
		initPrintWriter();

		f = new Flickr(this.options.getString("apiKey"), this.options.getString("secret"), new REST());

		searchParameters = new SearchParameters();
		searchParameters.setHasGeo(true);
		searchParameters.setLatitude(this.options.getString("lat"));
		searchParameters.setLongitude(this.options.getString("lng"));
		searchParameters.setRadius(this.options.getInteger("radius"));
		searchParameters.setExtras(extras);
		searchParameters.setRadiusUnits("km");
		searchParameters.setSort(SearchParameters.DATE_TAKEN_DESC);

		try {
			maxTakenDate = dateFormat.parse(this.options.getString("startDate"));
			minTakenDate = dateFormat.parse(this.options.getString("endDate"));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		searchParameters.setMaxTakenDate(maxTakenDate);
		searchParameters.setMinTakenDate(minTakenDate);
	}

	public void collect() {
		init();
		do {

			int page = 1;
			boolean error = false;
			error_count = 0;
			do {
				do {
					try {
						list = f.getPhotosInterface().search(searchParameters, 250, page);
						page++;
						error = false;
						error_count = 0;
					} catch (FlickrException e) {
						e.printStackTrace();
						error = true;
						error_count++;
						System.out.println("Error: restarting last job! Retry n. " + error_count);
						searchParameters.setMaxTakenDate(maxTakenDate);
					}
				} while (error);

				for (Photo photo : list) {

					if (!photoIds.contains(photo.getId())) {
						numTotalPhotos++;
						numPartialPhotos++;
						duplicatedCount = 0;

						pwStatus.println(gson.toJson(photo));

						if (numPartialPhotos % numPerFlush == 0) {
							System.out.println(" #" + numTotalPhotos + " - " + numPartialPhotos + " - "
									+ photo.getDateTaken() + " - Flushed up to " + numTotalPhotos);
							pwStatus.flush();
						}

						if (numPartialPhotos >= photosPerFile)
							initPrintWriter();

						maxTakenDate = photo.getDateTaken();

						photoIds.add(photo.getId());
						countDistinctPhotos++;
					} else {
						duplicatedCount++;
					}
					if (duplicatedCount >= maxDuplicatedCount || countDistinctPhotos == maxPhotosPerRun) {
						page = 1;
						numPartialPhotos = 0;
						pwStatus.flush();
						break;
					}
				}

			} while (list.getPage() != list.getPages() && countDistinctPhotos < maxPhotosPerRun
					&& duplicatedCount < maxDuplicatedCount);

			searchParameters.setMaxTakenDate(maxTakenDate);

		} while (canContinue());

		pwStatus.close();

	}

	private boolean canContinue() {
		return error_count < 10 && (countDistinctPhotos < maxPhotosPerRun && duplicatedCount < maxDuplicatedCount);
	}

	private void initPrintWriter() {
		numPartialPhotos = 0;
		fileId = this.outputPath + "/flickr_" + dateFormat.format(new Date()) + ".txt";
		try {
			File myFile = new File(fileId);
			myFile.getParentFile().mkdirs();
			pwStatus = new PrintWriter(myFile);
			pwStatus.flush();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

	}

}
