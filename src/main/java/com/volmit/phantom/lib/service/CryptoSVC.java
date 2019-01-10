package com.volmit.phantom.lib.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.volmit.phantom.api.lang.VIO;
import com.volmit.phantom.api.lang.VoidOutputStream;
import com.volmit.phantom.api.service.IService;

public class CryptoSVC implements IService
{
	@Override
	public void onStart()
	{

	}

	@Override
	public void onStop()
	{

	}

	public String hashFile(File f, String alg) throws NoSuchAlgorithmException, IOException
	{
		FileInputStream fin = new FileInputStream(f);
		MessageDigest d = MessageDigest.getInstance(alg);
		DigestInputStream din = new DigestInputStream(fin, d);
		VIO.fullTransfer(din, new VoidOutputStream(), 8192);

		return byteArrayToHex(d.digest());
	}

	public String byteArrayToHex(byte[] a)
	{
		StringBuilder sb = new StringBuilder(a.length * 2);
		for(byte b : a)
		{
			sb.append(String.format("%02x", b));
		}

		return sb.toString();
	}
}
