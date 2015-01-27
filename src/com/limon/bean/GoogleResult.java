package com.limon.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GoogleResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Results> results = new ArrayList<Results>();
	public List<Results> getResults() {
		return results;
	}

	public void setResults(List<Results> results) {
		this.results = results;
	}
	private String status;
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public class Results{
		private String formatted_address;
		private String[] types;		
		//private String partial_match;
		private List<Address_components> address_components= new ArrayList<Address_components>();
		//private List<Geometry> geometry = new ArrayList<Geometry>();
//		public List<Geometry> getGeometry() {
//			return geometry;
//		}
//		public void setGeometry(List<Geometry> geometry) {
//			this.geometry = geometry;
//		}
		public String[] getTypes() {
			return types;
		}
		public void setTypes(String[] types) {
			this.types = types;
		}
		public List<Address_components> getAddress_components() {
			return address_components;
		}
		public void setAddress_components(List<Address_components> address_components) {
			this.address_components = address_components;
		}

		public String getFormatted_address() {
			return formatted_address;
		}
		public void setFormatted_address(String formatted_address) {
			this.formatted_address = formatted_address;
		}
//		public String getPartial_match() {
//			return partial_match;
//		}
//		public void setPartial_match(String partial_match) {
//			this.partial_match = partial_match;
//		}
	
		
	}
	public class Address_components{
		private String long_name;
		private String short_name;
		private String[] types;
		
		public String getLong_name() {
			return long_name;
		}
		public void setLong_name(String long_name) {
			this.long_name = long_name;
		}
		public String getShort_name() {
			return short_name;
		}
		public void setShort_name(String short_name) {
			this.short_name = short_name;
		}
		public String[] getTypes() {
			return types;
		}
		public void setTypes(String[] types) {
			this.types = types;
		}	
	}
	public class Geometry{
		private List<Location> location = new ArrayList<Location>();
		private String location_type;
		private List<Viewport> viewport =new ArrayList<Viewport>();
		private List<Bounds> bounds =new ArrayList<Bounds>();
		public List<Location> getLocation() {
			return location;
		}
		public void setLocation(List<Location> location) {
			this.location = location;
		}
		public List<Bounds> getBounds() {
			return bounds;
		}
		public void setBounds(List<Bounds> bounds) {
			this.bounds = bounds;
		}

		public String getLocation_type() {
			return location_type;
		}
		public void setLocation_type(String location_type) {
			this.location_type = location_type;
		}
		public List<Viewport> getViewport() {
			return viewport;
		}
		public void setViewport(List<Viewport> viewport) {
			this.viewport = viewport;
		}		
	}
	public class Viewport{
		private List<Northeast> northeast=new ArrayList<Northeast>();
		private List<Southwest> southwest=new ArrayList<Southwest>();
		public List<Northeast> getNortheast() {
			return northeast;
		}
		public void setNortheast(List<Northeast> northeast) {
			this.northeast = northeast;
		}
		public List<Southwest> getSouthwest() {
			return southwest;
		}
		public void setSouthwest(List<Southwest> southwest) {
			this.southwest = southwest;
		}		
	}
	public class Bounds{
		private List<Northeast> northeast=new ArrayList<Northeast>();
		private List<Southwest> southwest=new ArrayList<Southwest>();
		public List<Northeast> getNortheast() {
			return northeast;
		}
		public void setNortheast(List<Northeast> northeast) {
			this.northeast = northeast;
		}
		public List<Southwest> getSouthwest() {
			return southwest;
		}
		public void setSouthwest(List<Southwest> southwest) {
			this.southwest = southwest;
		}		
	}
	public class Location{
		private double lat;
		private double lng;
		public double getLat() {
			return lat;
		}
		public void setLat(double lat) {
			this.lat = lat;
		}
		public double getLng() {
			return lng;
		}
		public void setLng(double lng) {
			this.lng = lng;
		}

	}
	public class Southwest{
		private double lat;
		private double lng;
		public double getLat() {
			return lat;
		}
		public void setLat(double lat) {
			this.lat = lat;
		}
		public double getLng() {
			return lng;
		}
		public void setLng(double lng) {
			this.lng = lng;
		}
	}
	public class Northeast{
		private double lat;
		private double lng;
		public double getLat() {
			return lat;
		}
		public void setLat(double lat) {
			this.lat = lat;
		}
		public double getLng() {
			return lng;
		}
		public void setLng(double lng) {
			this.lng = lng;
		}
	}
}
