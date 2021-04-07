package com.gcp.mockserver.gcp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gcp.mockserver.utils.gcp.Constants;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.model.MachineType;
import com.google.api.services.compute.model.MachineTypeAggregatedList;
import com.google.api.services.compute.model.MachineTypesScopedList;
import com.google.api.services.compute.model.Region;
import com.google.api.services.compute.model.RegionList;
import com.google.api.services.compute.model.Zone;
import com.google.api.services.compute.model.ZoneList;

public class GetGCPDetails {

	private static List<Region> dataCenterList;
	private static List<Zone> availabilityZoneList;
	private static Map<String, String> dataCenterAZMap;
	private static Map<String, String> AZDCMap;
	private static Map<String, MachineType> machineTypeMap;

	public static List<Region> getDataCenterList() {
		if (dataCenterList == null || dataCenterList.isEmpty()) {
			try {
				Compute computeService = GoogleCompute.createComputeService();
				Compute.Regions.List request = computeService.regions().list(Constants.GOOGLE_CLOUD_ACCOUNT_ID);

				List<Region> dcList = new ArrayList<>();

				RegionList response;
				do {
					response = request.execute();
					if (response.getItems() == null) {
						continue;
					}
					for (Region region : response.getItems()) {
						dcList.add(region);
					}

					request.setPageToken(response.getNextPageToken());
				} while (response.getNextPageToken() != null);

				dataCenterList = dcList;
			} catch (Exception error) {
				error.printStackTrace();
			}
		}
		return dataCenterList;
	}

	public static List<Zone> getAZList() {
		if (availabilityZoneList == null || availabilityZoneList.isEmpty()) {
			try {
				Compute computeService = GoogleCompute.createComputeService();
				Compute.Zones.List request = computeService.zones().list(Constants.GOOGLE_CLOUD_ACCOUNT_ID);

				List<Zone> zoneList = new ArrayList<>();
				dataCenterAZMap = new HashMap<>();

				ZoneList response;
				do {
					response = request.execute();
					if (response.getItems() == null) {
						continue;
					}
					for (Zone zone : response.getItems()) {
						zoneList.add(zone);

						String[] split = zone.getRegion().split("regions/");
						dataCenterAZMap.put(zone.getName(), split[1]);
					}

					request.setPageToken(response.getNextPageToken());
				} while (response.getNextPageToken() != null);

				availabilityZoneList = zoneList;
			} catch (Exception error) {
				error.printStackTrace();
			}
		}
		return availabilityZoneList;
	}

	public static Map<String, String> getAZDCMap() {
		if (dataCenterAZMap == null || dataCenterAZMap.isEmpty()) {
			try {
				dataCenterAZMap = new HashMap<>();
				Compute computeService = GoogleCompute.createComputeService();
				Compute.Zones.List request = computeService.zones().list(Constants.GOOGLE_CLOUD_ACCOUNT_ID);

				ZoneList response;
				do {
					response = request.execute();
					if (response.getItems() == null) {
						continue;
					}
					for (Zone zone : response.getItems()) {
						String[] split = zone.getRegion().split("regions/");
						dataCenterAZMap.put(zone.getName(), split[1]);
					}

					request.setPageToken(response.getNextPageToken());
				} while (response.getNextPageToken() != null);
			} catch (Exception error) {
				error.printStackTrace();
			}
		}
		return dataCenterAZMap;
	}

	public static Map<String, String> getDCAZMap() {
		if (AZDCMap == null || AZDCMap.isEmpty()) {
			try {
				Compute computeService = GoogleCompute.createComputeService();
				Compute.Regions.List request = computeService.regions().list(Constants.GOOGLE_CLOUD_ACCOUNT_ID);
				AZDCMap = new HashMap<>();
				RegionList response;
				do {
					response = request.execute();
					if (response.getItems() == null) {
						continue;
					}
					for (Region region : response.getItems()) {
						String zones = "";
						for (String zone : region.getZones()) {
							zones = zone + ",";
						}
						AZDCMap.put(region.getName(), zones.substring(0, zones.length() - 2));
					}
					request.setPageToken(response.getNextPageToken());
				} while (response.getNextPageToken() != null);
			} catch (Exception error) {
				error.printStackTrace();
			}
		}
		return AZDCMap;
	}

	public static Map<String, MachineType> getHardwareDetails() {
		if (machineTypeMap == null || machineTypeMap.isEmpty()) {
			try {
				machineTypeMap = new HashMap<>();
				Compute computeService = GoogleCompute.createComputeService();
				Compute.MachineTypes.AggregatedList request = computeService.machineTypes().aggregatedList(Constants.GOOGLE_CLOUD_ACCOUNT_ID);

				MachineTypeAggregatedList response;
				do {

					response = request.execute();
					if (response.getItems() == null) {
						continue;
					}

					Iterator it = response.getItems().entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry pair = (Map.Entry) it.next();
						MachineTypesScopedList machineTypesScopedList = (MachineTypesScopedList) pair.getValue();

						if (machineTypesScopedList.getMachineTypes() != null) {
							for (MachineType machineType : machineTypesScopedList.getMachineTypes()) {
								if (machineType != null && machineType.getName() != null) {
									machineTypeMap.put(machineType.getName(), machineType);
								}
							}
						}
					}
					request.setPageToken(response.getNextPageToken());
				} while (response.getNextPageToken() != null);
			} catch (Exception error) {
				error.printStackTrace();
			}
		}
		return machineTypeMap;
	}

}
