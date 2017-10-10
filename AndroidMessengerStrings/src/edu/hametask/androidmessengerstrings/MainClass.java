package edu.hametask.androidmessengerstrings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

public class MainClass {

	private static final byte[] key = "MyDifficultPassw".getBytes(); //128 bit - AES
	private static final String transformation = "AES";
	
	public SealedObject myEncrypt(Serializable object) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
	{
		SealedObject sealedObject=null;
	    try {
	        // Length is 16 byte
	        SecretKeySpec sks = new SecretKeySpec(key, transformation);

	        // Create cipher
	        Cipher cipher = Cipher.getInstance(transformation);
	        cipher.init(Cipher.ENCRYPT_MODE, sks);
	        sealedObject = new SealedObject(object, cipher);
	    } 
	    catch (IllegalBlockSizeException e) 
	    {
	        e.printStackTrace();
	    }
	    return sealedObject;
	}

	public static void encrypt(Serializable object, OutputStream ostream) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
	{
	    try 
	    {
	        // Length is 16 byte
	        SecretKeySpec sks = new SecretKeySpec(key, transformation);

	        // Create cipher
	        Cipher cipher = Cipher.getInstance(transformation);
	        cipher.init(Cipher.ENCRYPT_MODE, sks);
	        SealedObject sealedObject = new SealedObject(object, cipher);

	        // Wrap the output stream
	        CipherOutputStream cos = new CipherOutputStream(ostream, cipher);
	        ObjectOutputStream outputStream = new ObjectOutputStream(cos);
	        outputStream.writeObject(sealedObject);
	        outputStream.close();
	    } 
	    catch (IllegalBlockSizeException e)
	    {
	        e.printStackTrace();
	    }
	}
	
	public Object myDecrypt(SealedObject sealedObject) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
	{
	    SecretKeySpec sks = new SecretKeySpec(key, transformation);
	    Cipher cipher = Cipher.getInstance(transformation);
	    cipher.init(Cipher.DECRYPT_MODE, sks);

	    try 
	    {
	        return sealedObject.getObject(cipher);
	    } 
	    catch (ClassNotFoundException | IllegalBlockSizeException | BadPaddingException e)
	    {
	        e.printStackTrace();
	        return null;
	    }
	}

	public static Object decrypt(InputStream istream) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
	{
	    SecretKeySpec sks = new SecretKeySpec(key, transformation);
	    Cipher cipher = Cipher.getInstance(transformation);
	    cipher.init(Cipher.DECRYPT_MODE, sks);

	    CipherInputStream cipherInputStream = new CipherInputStream(istream, cipher);
	    ObjectInputStream inputStream = new ObjectInputStream(cipherInputStream);
	    SealedObject sealedObject;
	    try {
	        sealedObject = (SealedObject) inputStream.readObject();
	        return sealedObject.getObject(cipher);
	    } catch (ClassNotFoundException | IllegalBlockSizeException | BadPaddingException e)
	    {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, FileNotFoundException, IOException
	{
		MyObject obj = new MyObject("hello world");
		encrypt(obj, new FileOutputStream("./file"));
		System.out.println("ecrypted");
		
		MyObject obj2 = (MyObject) decrypt(new FileInputStream("./file"));
		//for(String s:obj2.data)
		//{
			//System.out.println(s);
			System.out.println(obj2.data);
		//}
	}
}

class MyObject implements Serializable
{
	public String data;

	public MyObject(String data)
	{
		this.data = data;
	}
	
	public String getData()
	{
		return data;
	}
}

