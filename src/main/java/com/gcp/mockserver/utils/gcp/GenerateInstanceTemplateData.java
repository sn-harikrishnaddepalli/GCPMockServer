package com.gcp.mockserver.utils.gcp;

import static com.gcp.mockserver.utils.gcp.Constants.GCP_INSTANCE_TEMPLATE_COUNT;
import static com.gcp.mockserver.utils.gcp.Constants.gcpUrl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.api.services.compute.model.AccessConfig;
import com.google.api.services.compute.model.AttachedDisk;
import com.google.api.services.compute.model.AttachedDiskInitializeParams;
import com.google.api.services.compute.model.InstanceProperties;
import com.google.api.services.compute.model.InstanceTemplate;
import com.google.api.services.compute.model.InstanceTemplateList;
import com.google.api.services.compute.model.Metadata;
import com.google.api.services.compute.model.NetworkInterface;
import com.google.api.services.compute.model.Scheduling;
import com.google.api.services.compute.model.ServiceAccount;
import com.google.gson.Gson;

public class GenerateInstanceTemplateData {

	public static InstanceTemplateList generateInstanceTemplateData(final String projectName) {

		List<InstanceTemplate> templateList = new ArrayList<>();

		InstanceTemplateList instanceTemplateList = new InstanceTemplateList();
		instanceTemplateList.setKind("compute#instanceTemplateList");
		instanceTemplateList.setId("projects/" + projectName + "/global/instanceTemplates");
		instanceTemplateList.setSelfLink(gcpUrl + projectName + "/global/instanceTemplates");

		for (int count = 0; count < GCP_INSTANCE_TEMPLATE_COUNT; count++) {

			InstanceTemplate instanceTemplate = new InstanceTemplate();
			instanceTemplate.setKind("compute#instanceTemplate");
			instanceTemplate.setId(new BigInteger(64, new Random()));
			instanceTemplate.setName("Instance-Template-" + new Random().nextLong());
			instanceTemplate.setDescription("");
			instanceTemplate.setCreationTimestamp("");

			InstanceProperties instanceProperties = new InstanceProperties();

			/*Setting the Network Interface*/
			NetworkInterface networkInterface = new NetworkInterface();
			networkInterface.setKind("compute#networkInterface");
			networkInterface.setNetwork(gcpUrl + projectName + "/global/networks/default");
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
			instanceProperties.setNetworkInterfaces(networkInterfaceList);

			Metadata metadata = new Metadata();
			metadata.setKind("compute#metadata");
			metadata.setFingerprint("oLhAB603BBc=");

			instanceProperties.setMetadata(metadata);

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
			instanceProperties.setServiceAccounts(serviceAccountList);

			Scheduling scheduling = new Scheduling();
			scheduling.setOnHostMaintenance("MIGRATE");
			scheduling.setAutomaticRestart(true);
			scheduling.setPreemptible(false);

			instanceProperties.setScheduling(scheduling);

			AttachedDisk attachedDisk = new AttachedDisk();
			attachedDisk.setKind("compute#attachedDisk");
			attachedDisk.setType("PERSISTENT");
			attachedDisk.setMode("READ_WRITE");
			attachedDisk.setDeviceName("persistent-disk-0");
			attachedDisk.setIndex(0);
			attachedDisk.setBoot(true);
			attachedDisk.setAutoDelete(true);

			AttachedDiskInitializeParams attachedDiskInitializeParams = new AttachedDiskInitializeParams();
			attachedDiskInitializeParams.setSourceImage("projects/" + projectName + "/global/images/IMG-" + count);
			attachedDiskInitializeParams.setDiskSizeGb(10L);
			attachedDiskInitializeParams.setDiskType("pd-standard");
			attachedDisk.setInitializeParams(attachedDiskInitializeParams);

			List<AttachedDisk> attachedDiskList = new ArrayList<>();
			attachedDiskList.add(attachedDisk);

			instanceProperties.setDisks(attachedDiskList);

			instanceProperties.setMachineType("" + Constants.machineList.get(count % Constants.machineList.size()));

			instanceTemplate.setProperties(instanceProperties);
			templateList.add(instanceTemplate);
		}
		instanceTemplateList.setItems(templateList);
		return instanceTemplateList;
	}

	public static String generateInstanceTemplateDetails(final String projectName) {
		Gson gson = new Gson();
		String json = gson.toJson(generateInstanceTemplateData(projectName));
		return json;
	}

}
