/* 
 * NOTICE OF LICENSE
 * 
 * This source file is subject to the Open Software License (OSL 3.0) that is 
 * bundled with this package in the file LICENSE.txt. It is also available 
 * through the world-wide-web at http://opensource.org/licenses/osl-3.0.php
 * If you did not receive a copy of the license and are unable to obtain it 
 * through the world-wide-web, please send an email to pdiffenderfer@gmail.com 
 * so we can send you a copy immediately. If you use any of this software please
 * notify me via my website or email, your feedback is much appreciated. 
 * 
 * @copyright   Copyright (c) 2011 Magnos Software (http://www.magnos.org)
 * @license     http://opensource.org/licenses/osl-3.0.php
 * 				Open Software License (OSL 3.0)
 */

package org.magnos.data;

import java.nio.ByteBuffer;

import sun.nio.ch.DirectBuffer;

/**
 * A utility for converting between primitive data types and an array of bytes.
 * 
 * @author Philip Diffenderfer
 *
 */
public class Bits 
{

	/**
	 * Given an array of bytes, the first 4 bytes of the array are used to
	 * compute a floating-point value.
	 * 
	 * @param b
	 * 		The array of bytes.
	 * @return
	 * 		A float taken from the bytes.
	 */
	public static boolean getBoolean(byte b) 
	{
		return (b != 0);
	}
	
	/**
	 * Given a boolean this will convert it to its byte value.
	 * 
	 * @param x
	 * 		The boolean to convert.
	 * @return
	 * 		The byte representation of x.
	 */
	public static byte getBooleanBytes(boolean x) 
	{
		return (byte)(x ? 1 : 0);
	}

	/**
	 * Given an array of bytes, the first byte of the array are used to
	 * compute a short value.
	 * 
	 * @param b
	 * 		The array of bytes.
	 * @return
	 * 		A short taken from the bytes.
	 */
	public static short getUByte(byte[] b) 
	{
		return (short)(b[0] & 0xFF);
	}

	/**
	 * Given a short and an array of bytes, the first byetshort of the array are
	 * set to the bytes that make up the short.
	 * 
	 * @param x
	 * 		The short to place in the byte array.
	 * @param b
	 * 		The byte array to place the bytes. 
	 */
	public static void getUByteBytes(short x, byte[] b) 
	{
		b[0] = (byte)(x & 0xFF);
	}

	/**
	 * Given a short an array of bytes will be returned representing the short.
	 * 
	 * @param x
	 * 		The short to return as a byte array.
	 * @return
	 * 		An array of bytes (length 1).
	 */
	public static byte[] getUByteBytes(short x) 
	{
		byte[] b = new byte[1];
		getUByteBytes(x, b);
		return b;
	}
	
	/**
	 * Given an array of bytes, the first 2 bytes of the array are used to
	 * compute a char value.
	 * 
	 * @param b
	 * 		The array of bytes.
	 * @return
	 * 		A char taken from the bytes.
	 */
	public static char getChar(byte[] b) 
	{
		char x = 0;
		x |= (char)((b[0] << 8) & 0xFF00);
		x |= (char)((b[1] << 0) & 0x00FF);
		return x;
	}
	
	/**
	 * Given a char and an array of bytes, the first 2 bytes of the array are
	 * set to the bytes that make up the char.
	 * 
	 * @param x
	 * 		The char to place in the byte array.
	 * @param b
	 * 		The byte array to place the bytes. 
	 */
	public static void getCharBytes(char x, byte[] b) 
	{
		b[0] = (byte)((x >>> 8) & 0xFF);
		b[1] = (byte)((x >>> 0) & 0xFF);
	}
	
	/**
	 * Given a char an array of bytes will be returned representing the char.
	 * 
	 * @param x
	 * 		The char to return as a byte array.
	 * @return
	 * 		An array of bytes (length 2).
	 */
	public static byte[] getCharBytes(char x) 
	{
		byte[] b = new byte[2];
		getCharBytes(x, b);
		return b;
	}

	/**
	 * Given an array of bytes, the first 2 bytes of the array are used to
	 * compute a short value.
	 * 
	 * @param b
	 * 		The array of bytes.
	 * @return
	 * 		A short taken from the bytes.
	 */
	public static short getShort(byte[] b) 
	{
		short x = 0;
		x |= (short)((b[0] << 8) & 0xFF00);
		x |= (short)((b[1] << 0) & 0x00FF);
		return x;
	}
	
	/**
	 * Given a short and an array of bytes, the first 2 bytes of the array are
	 * set to the bytes that make up the short.
	 * 
	 * @param x
	 * 		The short to place in the byte array.
	 * @param b
	 * 		The byte array to place the bytes. 
	 */
	public static void getShortBytes(short x, byte[] b) 
	{
		b[0] = (byte)((x >>> 8) & 0xFF);
		b[1] = (byte)((x >>> 0) & 0xFF);
	}

	/**
	 * Given a short an array of bytes will be returned representing the short.
	 * 
	 * @param x
	 * 		The short to return as a byte array.
	 * @return
	 * 		An array of bytes (length 2).
	 */
	public static byte[] getShortBytes(short x) 
	{
		byte[] b = new byte[2];
		getShortBytes(x, b);
		return b;
	}
	

	/**
	 * Given an array of bytes, the first 2 bytes of the array are used to
	 * compute a short value.
	 * 
	 * @param b
	 * 		The array of bytes.
	 * @return
	 * 		A short taken from the bytes.
	 */
	public static int getUShort(byte[] b) 
	{
		return getShort(b) & 0xFFFF;
	}
	
	/**
	 * Given a short and an array of bytes, the first 2 bytes of the array are
	 * set to the bytes that make up the short.
	 * 
	 * @param x
	 * 		The short to place in the byte array.
	 * @param b
	 * 		The byte array to place the bytes. 
	 */
	public static void getUShortBytes(int x, byte[] b) 
	{
		getShortBytes((short)(x & 0xFFFF), b);
	}

	/**
	 * Given a short an array of bytes will be returned representing the short.
	 * 
	 * @param x
	 * 		The short to return as a byte array.
	 * @return
	 * 		An array of bytes (length 2).
	 */
	public static byte[] getUShortBytes(int x) 
	{
		byte[] b = new byte[2];
		getUShortBytes(x, b);
		return b;
	}
	
	
	/**
	 * Given an array of bytes, the first 4 bytes of the array are used to
	 * compute an integer value.
	 * 
	 * @param b
	 * 		The array of bytes.
	 * @return
	 * 		An integer taken from the bytes.
	 */
	public static int getInt(byte[] b) 
	{
		int x = 0;
		x |= (b[0] << 24) & 0xFF000000;
		x |= (b[1] << 16) & 0x00FF0000;
		x |= (b[2] <<  8) & 0x0000FF00;
		x |= (b[3] <<  0) & 0x000000FF;
		return x;
	}
	
	/**
	 * Given an integer and an array of bytes, the first 4 bytes of the array 
	 * are set to the bytes that make up the integer.
	 * 
	 * @param x
	 * 		The integer to place in the byte array.
	 * @param b
	 * 		The byte array to place the bytes. 
	 */
	public static void getIntBytes(int x, byte[] b) 
	{
		b[0] = (byte)((x >>> 24) & 0xFF);
		b[1] = (byte)((x >>> 16) & 0xFF);
		b[2] = (byte)((x >>>  8) & 0xFF);
		b[3] = (byte)((x >>>  0) & 0xFF);
	}
	
	/**
	 * Given an integer an array of bytes will be returned representing the integer.
	 * 
	 * @param x
	 * 		The integer to return as a byte array.
	 * @return
	 * 		An array of bytes (length 4).
	 */
	public static byte[] getIntBytes(int x) 
	{
		byte[] b = new byte[4];
		getIntBytes(x, b);
		return b;
	}
	

	/**
	 * Given an array of bytes, the first 4 bytes of the array are used to
	 * compute an integer value.
	 * 
	 * @param b
	 * 		The array of bytes.
	 * @return
	 * 		An integer taken from the bytes.
	 */
	public static long getUInt(byte[] b) 
	{
		return getInt(b) & 0xFFFFFFFFL;
	}
	
	/**
	 * Given an integer and an array of bytes, the first 4 bytes of the array 
	 * are set to the bytes that make up the integer.
	 * 
	 * @param x
	 * 		The integer to place in the byte array.
	 * @param b
	 * 		The byte array to place the bytes. 
	 */
	public static void getUIntBytes(long x, byte[] b) 
	{
		getIntBytes((int)(x & 0xFFFFFFFF), b);
	}
	
	/**
	 * Given an integer an array of bytes will be returned representing the integer.
	 * 
	 * @param x
	 * 		The integer to return as a byte array.
	 * @return
	 * 		An array of bytes (length 4).
	 */
	public static byte[] getUIntBytes(long x) 
	{
		byte[] b = new byte[4];
		getUIntBytes(x, b);
		return b;
	}

	/**
	 * Given an array of bytes, the first 8 bytes of the array are used to
	 * compute a long value.
	 * 
	 * @param b
	 * 		The array of bytes.
	 * @return
	 * 		A long taken from the bytes.
	 */
	public static long getLong(byte[] b) 
	{
		long x = 0;
		x |= ((long)b[0] << 56) & 0xFF00000000000000L;
		x |= ((long)b[1] << 48) & 0x00FF000000000000L;
		x |= ((long)b[2] << 40) & 0x0000FF0000000000L;
		x |= ((long)b[3] << 32) & 0x000000FF00000000L;
		x |= ((long)b[4] << 24) & 0x00000000FF000000L;
		x |= ((long)b[5] << 16) & 0x0000000000FF0000L;
		x |= ((long)b[6] <<  8) & 0x000000000000FF00L;
		x |= ((long)b[7] <<  0) & 0x00000000000000FFL;
		return x;
	}

	/**
	 * Given a long and an array of bytes, the first 8 bytes of the array are
	 * set to the bytes that make up the long.
	 * 
	 * @param x
	 * 		The long to place in the byte array.
	 * @param b
	 * 		The byte array to place the bytes. 
	 */
	public static void getLongBytes(long x, byte[] b) 
	{
		b[0] = (byte)((x >>> 56) & 0xFF);
		b[1] = (byte)((x >>> 48) & 0xFF);
		b[2] = (byte)((x >>> 40) & 0xFF);
		b[3] = (byte)((x >>> 32) & 0xFF);
		b[4] = (byte)((x >>> 24) & 0xFF);
		b[5] = (byte)((x >>> 16) & 0xFF);
		b[6] = (byte)((x >>>  8) & 0xFF);
		b[7] = (byte)((x >>>  0) & 0xFF);
	}

	/**
	 * Given a long an array of bytes will be returned representing the long.
	 * 
	 * @param x
	 * 		The long to return as a byte array.
	 * @return
	 * 		An array of bytes (length 8).
	 */
	public static byte[] getLongBytes(long x) 
	{
		byte[] b = new byte[8];
		getLongBytes(x, b);
		return b;
	}
	
	/**
	 * Given an array of bytes, the first 4 bytes of the array are used to
	 * compute a floating-point value.
	 * 
	 * @param b
	 * 		The array of bytes.
	 * @return
	 * 		A float taken from the bytes.
	 */
	public static float getFloat(byte[] b) 
	{
		return Float.intBitsToFloat(getInt(b));
	}
	
	/**
	 * Given a float and an array of bytes, the first 4 bytes of the array are
	 * set to the bytes that make up the float.
	 * 
	 * @param x
	 * 		The float to place in the byte array.
	 * @param b
	 * 		The byte array to place the bytes. 
	 */
	public static void getFloatBytes(float x, byte[] b) 
	{
		getIntBytes(Float.floatToIntBits(x), b);
	}
	
	/**
	 * Given a float an array of bytes will be returned representing the float.
	 * 
	 * @param x
	 * 		The float to return as a byte array.
	 * @return
	 * 		An array of bytes (length 4).
	 */
	public static byte[] getFloatBytes(float x) 
	{
		return getIntBytes(Float.floatToIntBits(x));
	}
	
	/**
	 * Given an array of bytes, the first 8 bytes of the array are used to
	 * compute a double value.
	 * 
	 * @param b
	 * 		The array of bytes.
	 * @return
	 * 		A double taken from the bytes.
	 */
	public static double getDouble(byte[] b) 
	{
		return Double.longBitsToDouble(getLong(b));
	}

	/**
	 * Given a double and an array of bytes, the first 8 bytes of the array are
	 * set to the bytes that make up the double.
	 * 
	 * @param x
	 * 		The double to place in the byte array.
	 * @param b
	 * 		The byte array to place the bytes. 
	 */
	public static void getDoubleBytes(double x, byte[] b) 
	{
		getLongBytes(Double.doubleToLongBits(x), b);
	}

	/**
	 * Given a double an array of bytes will be returned representing the double.
	 * 
	 * @param x
	 * 		The double to return as a byte array.
	 * @return
	 * 		An array of bytes (length 8).
	 */
	public static byte[] getDoubleBytes(double x) 
	{
		return getLongBytes(Double.doubleToLongBits(x));
	}

	/**
	 * Explicitly frees the given buffer from memory if it is a DirectBuffer. 
	 * The data in a DirectBuffer lies outside of JVM space, a DirectBuffer is
	 * either a DirectByteBuffer or a MappedByteBuffer. The given buffer should
	 * never be used again, if it is - serious errors can occur.
	 * 
	 * @param buffer
	 * 		The buffer to forcably free from memory if possible.
	 */
	public static void free(ByteBuffer buffer) 
	{
		if (buffer instanceof DirectBuffer) {
			DirectBuffer dbuffer = (DirectBuffer)buffer;
			if (dbuffer.cleaner() != null) {
				dbuffer.cleaner().clean();
			}
		}
	}
	
}
