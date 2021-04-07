package com.gcp.mockserver.utils.gcp;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.api.services.compute.model.Disk;
import com.google.api.services.compute.model.DiskAggregatedList;
import com.google.api.services.compute.model.DisksScopedList;
import com.google.api.services.compute.model.GuestOsFeature;
import com.google.api.services.compute.model.Zone;
import com.google.gson.Gson;

public class GenerateStorageData {

	private static int diskCount = 1;
	private static int diskPageIteration = 1;

	public static DiskAggregatedList generateDiskData(final String projectName, final int maxResults, final String pageToken) {

		DiskAggregatedList diskAggregatedList = new DiskAggregatedList();
		diskAggregatedList.setKind("compute#diskAggregatedList");
		diskAggregatedList.setId("projects/" + projectName + "/aggregated/disks");
		diskAggregatedList.setSelfLink(Constants.gcpUrl + projectName + "/aggregated/disks");

		Map<String, DisksScopedList> disksScopedListMap = new HashMap<>();

		int counter = maxResults / Constants.zoneList.size();
		counter++;

		for (Zone zone : Constants.zoneList) {

			DisksScopedList disksScopedList = new DisksScopedList();

			if (diskCount > maxResults * diskPageIteration) {
				DisksScopedList.Warning warning = new DisksScopedList.Warning();
				warning.setCode("NO_RESULTS_ON_PAGE");
				warning.setMessage("There are no results for scope 'zones/'" + zone + " on this page.");
				disksScopedList.setWarning(warning);
			} else {
				List<Disk> diskList = generateDisk(projectName, maxResults, counter, zone.getName());
				if (diskList.isEmpty()) {
					DisksScopedList.Warning warning = new DisksScopedList.Warning();
					warning.setCode("NO_RESULTS_ON_PAGE");
					warning.setMessage("There are no results for scope 'zones/'" + zone + " on this page.");
					disksScopedList.setWarning(warning);
				} else {
					disksScopedList.setDisks(diskList);
				}
			}
			disksScopedListMap.put("zones/" + zone, disksScopedList);
		}

		if (Constants.GCP_STORAGE_COUNT > diskCount) {
			diskPageIteration++;
			diskAggregatedList.setNextPageToken("pageToken" + new Random().nextLong());
		} else {
			diskPageIteration = 1;
			diskCount = 1;
		}

		diskAggregatedList.setItems(disksScopedListMap);
		return diskAggregatedList;
	}

	public static List<Disk> generateDisk(final String projectName, final int maxResults, final int count, final String zone) {
		List<Disk> diskList = new ArrayList<>();

		for (int counter = 0; counter < count; counter++) {

			if (Constants.GCP_STORAGE_COUNT < diskCount) {
				return diskList;
			}

			if (diskCount > maxResults * diskPageIteration) {
				return diskList;
			}

			Disk disk = new Disk();

			disk.setKind("compute#disk");
			disk.setId(new BigInteger(64, new Random()));
			disk.setCreationTimestamp("2019-02-21T00:15:52.670-08:00");
			disk.setSizeGb(10L);
			disk.setZone(Constants.gcpUrl + projectName + "/zones/" + zone);
			disk.setStatus("READY");
			disk.setSelfLink(Constants.gcpUrl + projectName + "/zones/" + zone + "/disks/" + "disk-" + diskCount);
			disk.setSourceImage("https://www.googleapis.com/compute/v1/projects/ubuntu-os-cloud/global/images/ubuntu-1604-xenial-v20190212");
			disk.setSourceImageId("" + new BigInteger(64, new Random()));
			disk.setType(Constants.gcpUrl + projectName + "/zones/" + zone + "/diskTypes/pd-standard");
			disk.setName("disk-" + diskCount);

			List<String> licenses = new ArrayList<>();
			licenses.add("https://www.googleapis.com/compute/v1/projects/ubuntu-os-cloud/global/licenses/ubuntu-1604-xenial");

			disk.setLicenses(licenses);

			List<GuestOsFeature> guestOsFeatureList = new ArrayList<>();
			GuestOsFeature guestOsFeature = new GuestOsFeature();
			guestOsFeature.setType("VIRTIO_SCSI_MULTIQUEUE");
			guestOsFeatureList.add(guestOsFeature);

			disk.setGuestOsFeatures(guestOsFeatureList);
			disk.setLastAttachTimestamp("2019-02-21T00:15:52.671-08:00");

			List<String> users = new ArrayList<>();
			users.add(Constants.gcpUrl + projectName + "/zones/" + zone + "/instances/VM-" + diskCount);

			disk.setUsers(users);

			disk.setLabelFingerprint("42WmSpB8rSM=");

			List<Long> licensesCode = new ArrayList<>();
			licensesCode.add(1000201L);

			disk.setLicenseCodes(licensesCode);
			diskList.add(disk);
			diskCount++;
		}
		return diskList;
	}

	public static String generateDisksData(final String projectName, final int maxResults, final String pageToken) {
		Gson gson = new Gson();
		String json = gson.toJson(generateDiskData(projectName, maxResults, pageToken));
		return json;
	}

}
