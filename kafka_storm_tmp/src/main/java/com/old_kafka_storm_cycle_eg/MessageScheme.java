package com.old_kafka_storm_cycle_eg;

import org.apache.storm.spout.Scheme;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;

public class MessageScheme implements Scheme {
 
	public List<Object> deserialize(ByteBuffer ser) {
		//String msg = new String(ser, "UTF-8");
		String msg = byteBufferToString(ser);
		return new Values(msg);
	}
 
	public Fields getOutputFields() {
		// TODO Auto-generated method stub
		return new Fields("msg");
	}
 
	public static String byteBufferToString(ByteBuffer buffer) {
		CharBuffer charBuffer = null;
		try {
			Charset charset = Charset.forName("UTF-8");
			CharsetDecoder decoder = charset.newDecoder();
			charBuffer = decoder.decode(buffer);
			buffer.flip();
			return charBuffer.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
 
}
