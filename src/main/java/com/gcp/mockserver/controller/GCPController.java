package com.gcp.mockserver.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Optional;

import com.gcp.mockserver.config.RestConfig;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gcp.mockserver.utils.gcp.Constants;
import com.gcp.mockserver.utils.gcp.GenerateImageData;
import com.gcp.mockserver.utils.gcp.GenerateInstanceTemplateData;
import com.gcp.mockserver.utils.gcp.GenerateMachineTypesData;
import com.gcp.mockserver.utils.gcp.GenerateRegionData;
import com.gcp.mockserver.utils.gcp.GenerateStorageData;
import com.gcp.mockserver.utils.gcp.GenerateUrlMaps;
import com.gcp.mockserver.utils.gcp.GenerateVMData;
import com.gcp.mockserver.utils.gcp.GenerateZoneData;

@RestController
public class GCPController {

	@ResponseBody
	@RequestMapping(value = "/compute/v1/projects/{project}/regions", method = GET, produces = "application/json")
	public ResponseEntity<?> getRegionDetails(@PathVariable("project") String project,
	                                          @RequestParam(value = "maxResults") Optional<Integer> maxResults,
	                                          @RequestParam(value = "pageToken") Optional<String> pageToken,
	                                          @RequestParam(value = "filter") Optional<String> filter,
	                                          @RequestParam(value = "orderBy") Optional<String> orderBy) {
		System.out.println();
		System.out.println("=======Getting Region==========  " + project);
		System.out.println("Project Name ==> " + project);
		System.out.println();
		return new ResponseEntity(GenerateRegionData.generateRegionsList(project),
				RestConfig.getResponseStatus(Integer.parseInt("200")));
	}

	@ResponseBody
	@RequestMapping(value = "/compute/v1/projects/{project}/zones", method = GET, produces = "application/json")
	public ResponseEntity<?> getZoneDetails(@PathVariable("project") String project,
	                                        @RequestParam(value = "maxResults") Optional<Integer> maxResults,
	                                        @RequestParam(value = "pageToken") Optional<String> pageToken,
	                                        @RequestParam(value = "filter") Optional<String> filter,
	                                        @RequestParam(value = "orderBy") Optional<String> orderBy) {
		System.out.println();
		System.out.println("=======Getting Zone==========  " + project);
		System.out.println("Project Name ==> " + project);
		System.out.println();
		return new ResponseEntity(GenerateZoneData.generateZonesList(project),
				RestConfig.getResponseStatus(Integer.parseInt("200")));
	}

	@ResponseBody
	@RequestMapping(value = "/compute/v1/projects/{project}/global/images", method = GET, produces = "application/json")
	public ResponseEntity<?> getImageDetails(@PathVariable("project") String project,
	                                         @RequestParam(value = "maxResults") Optional<Integer> maxResults,
	                                         @RequestParam(value = "pageToken") Optional<String> pageToken,
	                                         @RequestParam(value = "filter") Optional<String> filter,
	                                         @RequestParam(value = "orderBy") Optional<String> orderBy) {
		System.out.println();
		System.out.println("=======Getting Images==========  " + project);
		System.out.println("Project Name ==> " + project);
		System.out.println();
		return new ResponseEntity(GenerateImageData.generateImageDetails(project),
				RestConfig.getResponseStatus(Integer.parseInt("200")));
	}

	@ResponseBody
	@RequestMapping(value = "/compute/v1/projects/{project}/global/instanceTemplates", method = GET, produces = "application/json")
	public ResponseEntity<?> getInstanceTemplateDetails(@PathVariable("project") String project,
	                                                    @RequestParam(value = "maxResults") Optional<Integer> maxResults,
	                                                    @RequestParam(value = "pageToken") Optional<String> pageToken,
	                                                    @RequestParam(value = "filter") Optional<String> filter,
	                                                    @RequestParam(value = "orderBy") Optional<String> orderBy) {
		System.out.println();
		System.out.println("=======Getting Instance Templates==========  " + project);
		System.out.println("Project Name ==> " + project);
		System.out.println();
		return new ResponseEntity(GenerateInstanceTemplateData.generateInstanceTemplateDetails(project),
				RestConfig.getResponseStatus(Integer.parseInt("200")));
	}

	@ResponseBody
	@RequestMapping(value = "/compute/v1/projects/{project}/aggregated/instances", method = GET, produces = "application/json")
	public ResponseEntity<?> getInstanceDetails(@PathVariable("project") String project,
	                                            @RequestParam(value = "maxResults") Optional<Integer> maxResults,
	                                            @RequestParam(value = "pageToken") Optional<String> pageToken,
	                                            @RequestParam(value = "filter") Optional<String> filter,
	                                            @RequestParam(value = "orderBy") Optional<String> orderBy) {

		int maxGCPResults = 500;
		String pageGCPToken = null;

		if (maxResults.isPresent()) {
			if (maxResults.get() != 0 || maxResults.get() < 500) {
				maxGCPResults = maxResults.get();
			}
		}

		if (pageToken.isPresent()) {
			pageGCPToken = pageToken.get();
		}

		System.out.println();
		System.out.println("=======Getting VM Instance==========  " + project);
		System.out.println("Project Name ==> " + project);
		System.out.println("Max Results  ==> " + maxResults);
		System.out.println("Page Token   ==> " + pageToken);
		System.out.println();

		return new ResponseEntity(GenerateVMData.generateInstancesList(project, maxGCPResults, pageGCPToken),
				RestConfig.getResponseStatus(Integer.parseInt("200")));
	}

	@ResponseBody
	@RequestMapping(value = "/compute/v1/projects/{project}/aggregated/machineTypes", method = GET, produces = "application/json")
	public ResponseEntity<?> getMachineTypeDetails(@PathVariable("project") String project,
	                                               @RequestParam(value = "maxResults") Optional<Integer> maxResults,
	                                               @RequestParam(value = "pageToken") Optional<String> pageToken,
	                                               @RequestParam(value = "filter") Optional<String> filter,
	                                               @RequestParam(value = "orderBy") Optional<String> orderBy) {

		int maxGCPResults = 500;
		String pageGCPToken = null;

		if (maxResults.isPresent()) {
			if (maxResults.get() != 0 || maxResults.get() < 500) {
				maxGCPResults = maxResults.get();
			}
		}

		if (pageToken.isPresent()) {
			pageGCPToken = pageToken.get();
		}

		System.out.println();
		System.out.println("=======Getting Machine Types==========  " + project);
		System.out.println("Project Name ==> " + project);
		System.out.println("Max Results  ==> " + maxResults);
		System.out.println("Page Token   ==> " + pageToken);
		System.out.println();

		return new ResponseEntity(GenerateMachineTypesData.generateMachineTypeList(project, maxGCPResults, pageGCPToken),
				RestConfig.getResponseStatus(Integer.parseInt("200")));
	}

	@ResponseBody
	@RequestMapping(value = "/compute/v1/projects/{project}/aggregated/disks", method = GET, produces = "application/json")
	public ResponseEntity<?> getDiskDetails(@PathVariable("project") String project,
	                                        @RequestParam(value = "maxResults") Optional<Integer> maxResults,
	                                        @RequestParam(value = "pageToken") Optional<String> pageToken,
	                                        @RequestParam(value = "filter") Optional<String> filter,
	                                        @RequestParam(value = "orderBy") Optional<String> orderBy) {

		int maxGCPResults = 500;
		String pageGCPToken = null;

		if (maxResults.isPresent()) {
			if (maxResults.get() != 0 || maxResults.get() < 500) {
				maxGCPResults = maxResults.get();
			}
		}

		if (pageToken.isPresent()) {
			pageGCPToken = pageToken.get();
		}

		System.out.println();
		System.out.println("=======Getting Disks==========  " + project);
		System.out.println("Project Name ==> " + project);
		System.out.println("Max Results  ==> " + maxResults);
		System.out.println("Page Token   ==> " + pageToken);
		System.out.println();

		return new ResponseEntity(GenerateStorageData.generateDisksData(project, maxGCPResults, pageGCPToken),
				RestConfig.getResponseStatus(Integer.parseInt("200")));
	}

	@ResponseBody
	@RequestMapping(value = "/set_compute_count", method = GET, produces = "application/json")
	public ResponseEntity<?> updateVMCount(@RequestParam(value = "vm_count") Optional<Integer> vmCount,
	                                       @RequestParam(value = "disk_count") Optional<Integer> diskCount,
	                                       @RequestParam(value = "image_count") Optional<Integer> imageCount,
	                                       @RequestParam(value = "instance_template_count") Optional<Integer> instanceCount) {
		if (vmCount.isPresent()) {
			Constants.GCP_TOTAL_INSTANCE_COUNT = vmCount.get();
		}

		if (diskCount.isPresent()) {
			Constants.GCP_STORAGE_COUNT = diskCount.get();
		}

		if (imageCount.isPresent()) {
			Constants.GCP_IMAGE_COUNT = imageCount.get();
		}

		if (instanceCount.isPresent()) {
			Constants.GCP_INSTANCE_TEMPLATE_COUNT = instanceCount.get();
		}

		return new ResponseEntity("Success",
				RestConfig.getResponseStatus(Integer.parseInt("200")));
	}
	
	@ResponseBody
	@RequestMapping(value = "/compute/v1/projects/{project}/global/urlMaps", method = GET, produces = "application/json")
	public ResponseEntity<String> getUrlMaps(@PathVariable("project") String project,
            @RequestParam(value = "maxResults") Optional<Integer> maxResults,
            @RequestParam(value = "pageToken") Optional<String> pageToken,
            @RequestParam(value = "filter") Optional<String> filter,
            @RequestParam(value = "orderBy") Optional<String> orderBy) {
		
		if(maxResults.isPresent())
		{
		return new ResponseEntity<String>( new GenerateUrlMaps().getUrlMapList(project, Integer.valueOf(maxResults.get())),
					RestConfig.getResponseStatus(Integer.parseInt("200")));
		}
		else 
		{
			return new ResponseEntity<String>( new GenerateUrlMaps().getUrlMapList(project,10),
					RestConfig.getResponseStatus(Integer.parseInt("200")));
			
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/setLbCount", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> setLbCount(@RequestParam String lbCount)
	{
		try {
				int loadBalancerCount = Integer.parseInt(lbCount);
				if(loadBalancerCount !=0 )
					{
						Constants.GCP_HTTP_LB_COUNT = loadBalancerCount;
						JSONObject lbCountJson = new JSONObject();
						lbCountJson.put("LoadBalancerCount ", lbCount);
						return new ResponseEntity<String>(lbCountJson.toString(), 
								RestConfig.getResponseStatus(Integer.parseInt("200")));
					}
				else
				{
				return new ResponseEntity<String>("{\"msg\" :\"please provide a non zero value\"}", 
						RestConfig.getResponseStatus(Integer.parseInt("400")));
				}
		   }
		catch(Exception e)
		{
			return new ResponseEntity<String>("{\"msg\" :\"please provide a valid non zero value\"}", 
					RestConfig.getResponseStatus(Integer.parseInt("400")));
			
		}
	}
	

}
