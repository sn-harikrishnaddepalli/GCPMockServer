package com.gcp.mockserver.utils.gcp;

import com.google.api.services.compute.model.ZoneList;
import com.google.gson.Gson;

public class GenerateZoneData {

	public static ZoneList generateZoneList(final String projectName) {
		ZoneList zoneList = new ZoneList();
		zoneList.setKind("compute#zoneList");
		zoneList.setId("projects/" + projectName + "/zones");
		zoneList.setItems(Constants.zoneList);
		zoneList.setSelfLink(Constants.gcpUrl + projectName + "/zones");
		return zoneList;
	}

	public static String generateZonesList(final String projectName) {
		Gson gson = new Gson();
		String json = gson.toJson(generateZoneList(projectName));
		return json;
	}
}
