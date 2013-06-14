/*
 * project 	Java1Project
 * 
 * package 	com.fullsail.lib
 * 
 * @author 	William Saults
 * 
 * date 	Jun 13, 2013
 */
package com.fullsail.lib;

public enum VenueEnum {
	VINDEX0(0),
	VINDEX1(1),
	VINDEX2(2);
	
	@SuppressWarnings("unused")
	private final int value;
	VenueEnum(int value) {
		this.value = value;
	}
}
