package com.gcp.mockserver.utils.gcp;

import static com.gcp.mockserver.utils.gcp.Constants.GCP_TOTAL_INSTANCE_COUNT;
import static com.gcp.mockserver.utils.gcp.Constants.gcpUrl;
import static com.gcp.mockserver.utils.gcp.Constants.zoneDCMap;
import static com.gcp.mockserver.utils.gcp.Constants.zoneList;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.api.services.compute.model.AccessConfig;
import com.google.api.services.compute.model.AttachedDisk;
import com.google.api.services.compute.model.Instance;
import com.google.api.services.compute.model.InstanceAggregatedList;
import com.google.api.services.compute.model.InstancesScopedList;
import com.google.api.services.compute.model.Metadata;
import com.google.api.services.compute.model.NetworkInterface;
import com.google.api.services.compute.model.Scheduling;
import com.google.api.services.compute.model.ServiceAccount;
import com.google.api.services.compute.model.Tags;
import com.google.api.services.compute.model.Zone;
import com.google.gson.Gson;

public class GenerateVMData {

	private static int vmCount = 1;
	private static int vmPageIteration = 1;

	public static InstanceAggregatedList generateInstanceList(final String projectName,
	                                                          final int maxResults,
	                                                          final String pageToken) {

		InstanceAggregatedList instanceAggregatedList = new InstanceAggregatedList();
		Map<String, InstancesScopedList> instancesScopedListMap = new HashMap<>();

		int counter = maxResults / zoneList.size();
		counter++;

		for (Zone zone : zoneList) {
			instanceAggregatedList.setKind("compute#instanceAggregatedList");
			instanceAggregatedList.setId("projects/" + projectName + "/aggregated/instances");
			instanceAggregatedList.setSelfLink(gcpUrl + projectName + "/aggregated/instances");

			InstancesScopedList instancesScopedList = new InstancesScopedList();

			if (vmCount > maxResults * vmPageIteration) {
				InstancesScopedList.Warning warning = new InstancesScopedList.Warning();
				warning.setCode("NO_RESULTS_ON_PAGE");
				warning.setMessage("There are no results for scope 'zones/'" + zone + " on this page.");
				instancesScopedList.setWarning(warning);
			} else {
				List<Instance> instanceList = generateVM(projectName,
						maxResults, counter, zone.getName(), zoneDCMap.get(zone));
				if (instanceList.isEmpty()) {
					InstancesScopedList.Warning warning = new InstancesScopedList.Warning();
					warning.setCode("NO_RESULTS_ON_PAGE");
					warning.setMessage("There are no results for scope 'zones/'" + zone + " on this page.");
					instancesScopedList.setWarning(warning);
				} else {
					instancesScopedList.setInstances(instanceList);
				}
			}
			instancesScopedListMap.put("zones/" + zone, instancesScopedList);
		}

		if (GCP_TOTAL_INSTANCE_COUNT > vmCount) {
			instanceAggregatedList.setNextPageToken("pageToken" + new Random().nextLong());
			vmPageIteration++;
		} else {
			vmCount = 1;
			vmPageIteration = 1;
		}

		instanceAggregatedList.setItems(instancesScopedListMap);
		return instanceAggregatedList;
	}

	public static List<Instance> generateVM(final String projectName,
	                                        final int maxResults,
	                                        final int count,
	                                        final String zone,
	                                        final String region) {
		List<Instance> instanceList = new ArrayList<>();

		for (int counter = 0; counter < count; counter++) {

			if (GCP_TOTAL_INSTANCE_COUNT < vmCount) {
				return instanceList;
			}

			if (vmCount > maxResults * vmPageIteration) {
				return instanceList;
			}
			String vmName = "VM-" + vmCount;

			Instance instance = new Instance();
			instance.setKind("compute#instance");
			instance.setId(new BigInteger(64, new Random()));
			instance.setCreationTimestamp("2019-01-21T04:07:37.387-08:00");
			instance.setName(vmName);
			instance.setMachineType(gcpUrl + projectName + "/zones/" + zone + "/machineTypes/n1-standard-1");
			instance.setCanIpForward(true);
			instance.setSelfLink(gcpUrl + projectName + "/zones/" + zone + "/instances/" + vmName);
			instance.setCpuPlatform("Intel Broadwell");
			instance.setLabelFingerprint("2ixRno2sGuM=");
			instance.setMinCpuPlatform("Intel Broadwell");
			instance.setStartRestricted(false);
			instance.setDeletionProtection(false);
			instance.setZone(gcpUrl + projectName + "/zones/" + zone);
			instance.setStatus("RUNNING");

			Tags tags = new Tags();
			tags.setFingerprint("zvPnTToVeHw=");
			List<String> tagsList = new ArrayList<>();
			tagsList.add(vmName + "-Node");

			instance.setTags(tags);

			/*Setting the Network Interface*/
			NetworkInterface networkInterface = new NetworkInterface();
			networkInterface.setKind("compute#networkInterface");
			networkInterface.setNetwork(gcpUrl + projectName + "/global/networks/default");
			networkInterface.setSubnetwork(gcpUrl + projectName + "/regions/" + region + "/subnetworks/default");
			networkInterface.setNetworkIP("10.128.15.215");
			networkInterface.setName("nic0");
			networkInterface.setFingerprint("db5u4verK8I=");

			/*Setting the Access Config*/
			AccessConfig accessConfig = new AccessConfig();
			accessConfig.setKind("compute#accessConfig");
			accessConfig.setType("ONE_TO_ONE_NAT");
			accessConfig.setName("external-nat");
			accessConfig.setNatIP("35.222.62.148");
			accessConfig.setNetworkTier("PREMIUM");
			List<AccessConfig> accessConfigList = new ArrayList<>();
			accessConfigList.add(accessConfig);
			networkInterface.setAccessConfigs(accessConfigList);

			List<NetworkInterface> networkInterfaceList = new ArrayList<>();
			networkInterfaceList.add(networkInterface);
			instance.setNetworkInterfaces(networkInterfaceList);

			AttachedDisk attachedDisk = new AttachedDisk();
			attachedDisk.setKind("compute#attachedDisk");
			attachedDisk.setType("PERSISTENT");
			attachedDisk.setMode("READ_WRITE");
			attachedDisk.setSource(gcpUrl + projectName + "/zones/" + zone + "/disks/disk-" + vmCount);
			attachedDisk.setDeviceName("persistent-disk-0");
			attachedDisk.setIndex(0);
			attachedDisk.setBoot(true);
			attachedDisk.setAutoDelete(true);

			List<String> licenses = new ArrayList<>();
			licenses.add(gcpUrl + "/cos-cloud/global/licenses/cos");
			licenses.add(gcpUrl + "/cos-cloud/global/licenses/cos-pcid");
			licenses.add(gcpUrl + "/gke-node-images/global/licenses/gke-node");
			attachedDisk.setLicenses(licenses);
			attachedDisk.setInterface("SCSI");

			List<AttachedDisk> attachedDiskList = new ArrayList<>();
			attachedDiskList.add(attachedDisk);

			instance.setDisks(attachedDiskList);

			Metadata metadata = new Metadata();
			metadata.setKind("compute#metadata");
			metadata.setFingerprint("oLhAB603BBc=");

			List<Metadata.Items> itemsList = new ArrayList<>();

			Metadata.Items items = new Metadata.Items();
			items.setKey("instance-template");
			items.setValue("projects/617327892084/global/instanceTemplates/vm-default-pool-e499505e");
			itemsList.add(items);

			items = new Metadata.Items();
			items.setKey("created-by");
			items.setValue("projects/617327892084/zones/" + zone + "/instanceGroupManagers/vm-grp");
			itemsList.add(items);

			items = new Metadata.Items();
			items.setKey("gci-update-strategy");
			items.setValue("update_disabled");
			itemsList.add(items);

			items = new Metadata.Items();
			items.setKey("configure-sh");
			items.setValue("");
			itemsList.add(items);

			items = new Metadata.Items();
			items.setKey("gci-ensure-gke-docker");
			items.setValue("true");
			itemsList.add(items);

			items = new Metadata.Items();
			items.setKey("google-compute-enable-pcid");
			items.setValue("true");
			itemsList.add(items);

			items = new Metadata.Items();
			items.setKey("user-data");
			items.setValue("");
			itemsList.add(items);

			items = new Metadata.Items();
			items.setKey("kube-env");
			items.setValue("");
			itemsList.add(items);

			items = new Metadata.Items();
			items.setKey("enable-oslogin");
			items.setValue("false");
			itemsList.add(items);

			items = new Metadata.Items();
			items.setKey("cluster-name");
			items.setValue("yuval-cluster");
			itemsList.add(items);

			items = new Metadata.Items();
			items.setKey("cluster-uid");
			items.setValue("9392818a37779ea22ba1f683c105791c14dad1c9f4dca0c7bb3a0377eb6b291b");
			itemsList.add(items);

			items = new Metadata.Items();
			items.setKey("cluster-location");
			items.setValue("us-central1-a");
			itemsList.add(items);

			metadata.setItems(itemsList);

			instance.setMetadata(metadata);

			ServiceAccount serviceAccount = new ServiceAccount();
			serviceAccount.setEmail("617327892084-compute@developer.gserviceaccount.com");

			List<String> scopeList = new ArrayList<>();
			scopeList.add("https://www.googleapis.com/auth/devstorage.read_only");
			scopeList.add("https://www.googleapis.com/auth/logging.write");
			scopeList.add("https://www.googleapis.com/auth/monitoring");
			scopeList.add("https://www.googleapis.com/auth/servicecontrol");
			scopeList.add("https://www.googleapis.com/auth/service.management.readonly");
			scopeList.add("https://www.googleapis.com/auth/trace.append");

			serviceAccount.setScopes(scopeList);

			List<ServiceAccount> serviceAccountList = new ArrayList<>();
			serviceAccountList.add(serviceAccount);

			instance.setServiceAccounts(serviceAccountList);

			Scheduling scheduling = new Scheduling();
			scheduling.setOnHostMaintenance("MIGRATE");
			scheduling.setAutomaticRestart(true);
			scheduling.setPreemptible(false);

			instance.setScheduling(scheduling);

			Map<String, String> labels = new HashMap<>();
			labels.put("goog-gke-node", "");

			instance.setLabels(labels);

			instanceList.add(instance);
			vmCount++;

		}
		return instanceList;
	}

	public static String generateInstancesList(final String projectName, final int maxResults, final String pageToken) {
		try {
			Gson gson = new Gson();
			String json = gson.toJson(generateInstanceList(projectName, maxResults, pageToken));
			return json;
		} catch (Exception error) {
			error.printStackTrace();
		}
		return null;
	}

}
