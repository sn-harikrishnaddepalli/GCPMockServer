package com.gcp.mockserver.utils.gcp;

import static com.gcp.mockserver.utils.gcp.Constants.zoneList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.api.services.compute.model.MachineType;
import com.google.api.services.compute.model.MachineTypeAggregatedList;
import com.google.api.services.compute.model.MachineTypesScopedList;
import com.google.api.services.compute.model.Zone;
import com.google.gson.Gson;

public class GenerateMachineTypesData {

	private static int machineTypesCount = 1;
	private static List<String> azList = new ArrayList<>();

	public static MachineTypeAggregatedList generateMachineType(final String projectName,
	                                                            final int maxResults,
	                                                            final String pageToken) {

		if (pageToken == null) {
			azList = new ArrayList<>();
			machineTypesCount = 1;
		}

		MachineTypeAggregatedList machineTypeAggregatedList = new MachineTypeAggregatedList();
		machineTypeAggregatedList.setKind("compute#machineTypeAggregatedList");
		machineTypeAggregatedList.setId("projects/" + projectName + "/aggregated/machineTypes");
		machineTypeAggregatedList.setSelfLink(Constants.gcpUrl + projectName + "/aggregated/machineTypes");

		MachineTypesScopedList machineTypesScopedList;
		Map<String, MachineTypesScopedList> machineTypesScopedListMap = new HashMap<>();

		for (Zone zone : zoneList) {
			List<MachineType> machineTypeList = new ArrayList<>();
			machineTypesScopedList = new MachineTypesScopedList();

			if (azList.contains(zone)) {
				MachineTypesScopedList.Warning warning = new MachineTypesScopedList.Warning();
				warning.setMessage("There are no results for scope 'zones/'" + zone + " on this page.");
				warning.setCode("NO_RESULTS_ON_PAGE");
				machineTypesScopedList.setWarning(warning);
				machineTypesScopedListMap.put("zones/" + zone, machineTypesScopedList);
			} else {

				for (Map.Entry<String, MachineType> entry : Constants.machineTypeMap.entrySet()) {

					if (machineTypesCount > maxResults) {
						break;
					}
					MachineType machineType = new MachineType();
					machineType.setId(entry.getValue().getId());
					machineType.setName(entry.getValue().getName());
					machineType.setIsSharedCpu(entry.getValue().getIsSharedCpu());
					machineType.setZone(zone.getName());
					machineType.setImageSpaceGb(entry.getValue().getImageSpaceGb());
					machineType.setMemoryMb(entry.getValue().getMemoryMb());
					machineType.setGuestCpus(entry.getValue().getGuestCpus());
					machineType.setDescription(entry.getValue().getDescription());
					machineType.setKind(entry.getValue().getKind());
					machineType.setMaximumPersistentDisks(entry.getValue().getMaximumPersistentDisks());
					machineType.setMaximumPersistentDisksSizeGb(entry.getValue().getMaximumPersistentDisksSizeGb());
					machineTypeList.add(machineType);
					machineTypesCount++;
				}

				if (machineTypeList.isEmpty()) {
					MachineTypesScopedList.Warning warning = new MachineTypesScopedList.Warning();
					warning.setCode("NO_RESULTS_ON_PAGE");
					warning.setMessage("There are no results for scope 'zones/'" + zone + " on this page.");
					machineTypesScopedList.setWarning(warning);
					machineTypesScopedListMap.put("zones/" + zone, machineTypesScopedList);
				} else {
					azList.add(zone.getName());
					machineTypesScopedList.setMachineTypes(machineTypeList);
					machineTypesScopedListMap.put("zones/" + zone, machineTypesScopedList);
				}
			}
		}
		machineTypeAggregatedList.setItems(machineTypesScopedListMap);
		if (machineTypesCount >= maxResults) {
			machineTypeAggregatedList.setNextPageToken("pageToken" + new Random().nextLong());
		}
		machineTypesCount = 1;
		return machineTypeAggregatedList;
	}

	public static String generateMachineTypeList(final String projectName, final int maxResults, final String pageToken) {
		Gson gson = new Gson();
		String json = gson.toJson(generateMachineType(projectName, maxResults, pageToken));
		return json;
	}

}
