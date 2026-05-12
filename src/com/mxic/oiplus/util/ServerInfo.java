package com.mxic.oiplus.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerInfo {

	private static String hostName = null;

	static {
	    InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			hostName = addr.getHostName();
		} catch (Exception e) {
			hostName = "UNKNOWN";
		}

	}

	public static String getHostName() {
		return hostName;
	}

	public static void main(String[] argv) {
		System.out.println(getHostName());
	}

}
