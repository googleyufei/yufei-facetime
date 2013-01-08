package com.facetime.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.facetime.core.file.IOUtils;

/**
 * Network utilities. （from jodd library）
 */
public class NetUtils {

	static private Pattern macPattern = Pattern.compile(".*((:?[0-9a-f]{2}[-:]){5}[0-9a-f]{2}).*",
			Pattern.CASE_INSENSITIVE);

	static final String[] windowsCommand = { "ipconfig", "/all" };

	static final String[] linuxCommand = { "/sbin/ifconfig", "-a" };

	// ---------------------------------------------------------------- download

	/**
	 * Downloads resource as byte array.
	 */
	public static byte[] downloadBytes(String url) throws IOException {
		HttpURLConnection conection = (HttpURLConnection) new URL(url).openConnection();
		return IOUtils.readBytes(conection.getInputStream());
	}

	/**
	 * Downloads resource as String.
	 */
	public static String downloadString(String url) throws IOException {
		HttpURLConnection conection = (HttpURLConnection) new URL(url).openConnection();
		return new String(IOUtils.readChars(conection.getInputStream()));
	}

	/**
	 * Downloads resource as String.
	 */
	public static String downloadString(String url, String encoding) throws IOException {
		HttpURLConnection conection = (HttpURLConnection) new URL(url).openConnection();
		return new String(IOUtils.readChars(conection.getInputStream(), encoding));
	}

	public static String getMacAddress() throws IOException {
		return getMacAddress(0);
	}

	public static String getMacAddress(final int nicIndex) throws IOException {
		final BufferedReader reader = getMacAddressesReader();
		int nicCount = 0;
		for (String line = null; (line = reader.readLine()) != null;) {
			final Matcher matcher = macPattern.matcher(line);
			if (matcher.matches()) {
				if (nicCount == nicIndex) {
					reader.close();
					return matcher.group(1).replaceAll("[-:]", "");
				}
				nicCount++;
			}
		}
		reader.close();
		return null;
	}

	public static List<String> getMacAddresses() throws IOException {
		final List<String> macAddressList = new ArrayList<String>();
		final BufferedReader reader = getMacAddressesReader();
		for (String line = null; (line = reader.readLine()) != null;) {
			final Matcher matcher = macPattern.matcher(line);
			if (matcher.matches())
				macAddressList.add(matcher.group(1).replaceAll("[-:]", ""));
		}
		reader.close();
		return macAddressList;
	}

	/**
	 * Resolves hostname and returns ip address as a string.
	 */
	public static String resolveHost(String hostname) {
		try {
			InetAddress addr = InetAddress.getByName(hostname);
			byte[] ipAddr = addr.getAddress();
			StringBuilder ipAddrStr = new StringBuilder(15);
			for (int i = 0; i < ipAddr.length; i++) {
				if (i > 0)
					ipAddrStr.append('.');
				ipAddrStr.append(ipAddr[i] & 0xFF);
			}
			return ipAddrStr.toString();
		} catch (UnknownHostException uhex) {
			return null;
		}
	}

	/**
	 * Resolves ip address and returns host getName as a string.
	 */
	public static String resolveIp(byte[] ip) {
		try {
			InetAddress addr = InetAddress.getByAddress(ip);
			return addr.getHostName();
		} catch (UnknownHostException uhex) {
			return null;
		}
	}

	/**
	 * Resolves string ip address and returns host getName as a string.
	 */
	public static String resolveIp(String ip) {
		try {
			InetAddress addr = InetAddress.getByName(ip);
			return addr.getHostName();
		} catch (UnknownHostException uhex) {
			return null;
		}
	}

	private static BufferedReader getMacAddressesReader() throws IOException {
		final String[] command;
		final String os = System.getProperty("os.getName");

		if (os.startsWith("Windows"))
			command = windowsCommand;
		else if (os.startsWith("Linux"))
			command = linuxCommand;
		else
			throw new IOException("Unknown operating system: " + os);
		final Process process = Runtime.getRuntime().exec(command);
		new Thread() {
			@Override
			public void run() {
				try {
					final InputStream errorStream = process.getErrorStream();
					while (errorStream.read() != -1)
						;
					errorStream.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}

		}.start();

		return new BufferedReader(new InputStreamReader(process.getInputStream()));
	}
}