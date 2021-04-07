package com.gcp.mockserver.utils.gcp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.api.services.compute.model.MachineType;
import com.google.api.services.compute.model.Region;
import com.google.api.services.compute.model.Zone;
import com.gcp.mockserver.gcp.GetGCPDetails;

public class Constants {

	public static final String gcpUrl = "https://www.googleapis.com/compute/v1/projects/";
	public static final List<Zone> zoneList;
	public static final List<Region> dataCenterList;
	public static final List<String> machineList = new ArrayList<>();
	public static final Map<String, String> zoneDCMap;
	public static final Map<String, String> dcZoneMap;
	public static final Map<String, MachineType> machineTypeMap;
	public static final String GOOGLE_CLOUD_ACCOUNT_ID = "deductive-reach-207607";
	public static int GCP_IMAGE_COUNT = 100;
	public static int GCP_INSTANCE_TEMPLATE_COUNT = 100;
	public static int GCP_STORAGE_COUNT = 1000;
	public static int GCP_TOTAL_INSTANCE_COUNT = 1000;
	public static int GCP_HTTP_LB_COUNT = 1000;

	static {
		zoneList = GetGCPDetails.getAZList();
		dataCenterList = GetGCPDetails.getDataCenterList();
		zoneDCMap = GetGCPDetails.getAZDCMap();
		dcZoneMap = GetGCPDetails.getDCAZMap();
		machineTypeMap = GetGCPDetails.getHardwareDetails();
	}

	public static final class CREDENTIALS_PATH {
		public static final String URL = System.getProperty("user.dir") + "/config/";
		public static final String FILE_NAME = "account.json";
	}

}
