package com.gcp.mockserver.utils.gcp;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.google.api.services.compute.model.UrlMap;
import com.google.api.services.compute.model.UrlMapList;
import com.google.gson.Gson;


public class GenerateUrlMaps 
{
	private static int urlMapsCount = 0;
	public static void main(String[] args)
	{   
	    UrlMapList urlMapList = new UrlMapList();
	    String projectId = "flask-app-228012";
	    urlMapList.setItems(new GenerateUrlMaps().getUrlMap(projectId,10));
	    urlMapList.setKind("compute#urlMapList");
	    urlMapList.setId("projects/" + projectId +"/global/urlMaps");
	    if(Constants.GCP_HTTP_LB_COUNT > urlMapsCount)
	    {
	    	urlMapList.setNextPageToken("pageToken" + new Random().nextLong());
	    }
	    String urlMapJson = new Gson().toJson(urlMapList);
	    System.out.println(urlMapJson);
	    
	}
	
	public String getUrlMapList(String projectId,int maxResults)
	{
		UrlMapList urlMapList = new UrlMapList();
	    urlMapList.setItems(new GenerateUrlMaps().getUrlMap(projectId,maxResults));
	    urlMapList.setKind("compute#urlMapList");
	    urlMapList.setId("projects/" + projectId +"/global/urlMaps");
	    if(Constants.GCP_HTTP_LB_COUNT > urlMapsCount)
	    {
	    	urlMapList.setNextPageToken("pageToken" + new Random().nextLong());
	    }
	    String urlMapJson = new Gson().toJson(urlMapList);
	    System.out.println(urlMapJson);
	    return urlMapJson;
	}
	
	private List<UrlMap> getUrlMap(String projectId,int maxResults)
	{
		List<UrlMap> urlMapItems = new ArrayList<UrlMap>();
		if (maxResults != 0)
		{
			for(int i=0;i<maxResults;i++)
		{
			if(urlMapsCount >= Constants.GCP_HTTP_LB_COUNT)
			{
				break;
			}
			UrlMap urlMap = new UrlMap();
			String name = new GenerateUrlMaps().getAlphanumericString(16);
			urlMap.setCreationTimestamp(new Date().toGMTString());
		    urlMap.setDefaultService("https://www.googleapis.com/compute/v1/projects/" + projectId+ "/global/backendServices/http-lb-"+ name + "-backend-" + String.valueOf(new Random().nextInt(5)));
		    urlMap.setFingerprint(new GenerateUrlMaps().getAlphanumericString(7));
		    urlMap.setName(name);
		    urlMap.setSelfLink("https://www.googleapis.com/compute/v1/projects/flask-app-228012/global/urlMaps/http-lb-e00854430395401e-url-map");
		    urlMap.setId(new BigInteger(String.valueOf(new Random().nextLong())));
		    urlMap.setKind("compute#urlMap");
		    urlMapItems.add(urlMap);
		    urlMapsCount++;
		}
	
		}
		return urlMapItems;
	    
	}
	
	private String getAlphanumericString(int n)
	{
		String AlphanumericString = "0123456789" + "abcdefghijklmnopqrstuvxyz";
		StringBuilder sb = new StringBuilder(n);
		for(int i=0;i<n;i++)
		{
			int index = (int) (AlphanumericString.length()* Math.random());
			sb.append(AlphanumericString.charAt(index));
		}
		return sb.toString();
	}

}
