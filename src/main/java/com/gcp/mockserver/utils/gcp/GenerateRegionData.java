package com.gcp.mockserver.utils.gcp;

import com.google.api.services.compute.model.RegionList;
import com.google.gson.Gson;

public class GenerateRegionData {

	public static RegionList generateRegionList(final String projectName) {
		RegionList regionList = new RegionList();
		regionList.setId("projects/" + projectName + "/regions");
		regionList.setKind("compute#regionList");
		regionList.setItems(Constants.dataCenterList);
		regionList.setSelfLink(Constants.gcpUrl + projectName + "/regions");
		return regionList;
	}

	public static String generateRegionsList(final String projectName) {
		Gson gson = new Gson();
		String json = gson.toJson(generateRegionList(projectName));
		return json;
	}
}
