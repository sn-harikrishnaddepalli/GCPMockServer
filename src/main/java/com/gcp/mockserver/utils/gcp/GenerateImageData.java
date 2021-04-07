package com.gcp.mockserver.utils.gcp;

import static com.gcp.mockserver.utils.gcp.Constants.GCP_IMAGE_COUNT;
import static com.gcp.mockserver.utils.gcp.Constants.zoneList;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.api.services.compute.model.Image;
import com.google.api.services.compute.model.ImageList;
import com.google.gson.Gson;

public class GenerateImageData {

	public static ImageList generateImageList(final String projectName) {
		ImageList imageList = new ImageList();
		imageList.setKind("compute#imageList");
		imageList.setId("projects/" + projectName + "/global/images");

		List<Image> imageLists = new ArrayList<>();

		for (int count = 0; count < GCP_IMAGE_COUNT; count++) {

			String imageName = "IMG-" + count;

			Image image = new Image();
			image.setName(imageName);
			image.setId(new BigInteger(64, new Random()));
			image.setSelfLink(Constants.gcpUrl + projectName + "/global/images/" + imageName);
			image.setSourceDisk(Constants.gcpUrl + projectName + "/zones/" + zoneList.get(count % zoneList.size()) + "/disks/disk-" + count);

			image.setKind("compute#image");
			image.setCreationTimestamp("2019-01-21T04:07:37.387-08:00");
			image.setDescription("");
			image.setStatus("READY");

			image.setSourceType("RAW");
			image.setDiskSizeGb(10L);

			image.setSourceDiskId("" + new BigInteger(15, new Random()));
			image.setLabelFingerprint("42WmSpB8rSM=");

			imageLists.add(image);
		}
		imageList.setItems(imageLists);
		imageList.setSelfLink(Constants.gcpUrl + projectName + "/global/images");
		return imageList;
	}

	public static String generateImageDetails(final String projectName) {
		Gson gson = new Gson();
		String json = gson.toJson(generateImageList(projectName));
		return json;
	}

}
