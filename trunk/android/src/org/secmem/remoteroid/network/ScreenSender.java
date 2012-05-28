package org.secmem.remoteroid.network;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.secmem.remoteroid.network.PacketHeader.OpCode;

import android.util.Log;


/**
 * Send screen shot data to host
 * @author ssm
 */
public class ScreenSender extends PacketSender{
		
	private static final int MAXDATASIZE = 4090;

	private byte[] sendBuffer = new byte[MAXDATASIZE];
	
	public ScreenSender(OutputStream out){
		super(out);		
	}
	
		
	public void screenTransmission(byte[] jpgData) throws IOException{
		int jpgTotalSize = jpgData.length;
		int transmittedSize = 0;		
		
		//First send jpg size information to host
		byte [] jpgSizeInfo = String.valueOf(jpgTotalSize).getBytes();		
		Packet jpgInfoPacket = new Packet(OpCode.JPGINFO_SEND, jpgSizeInfo, jpgSizeInfo.length);				
		
		send(jpgInfoPacket);
		
		//Next send jpg data to host
		while(jpgTotalSize > transmittedSize){
			int CurTransSize = (jpgTotalSize-transmittedSize) > MAXDATASIZE ? 
					MAXDATASIZE : (jpgTotalSize-transmittedSize);
			System.arraycopy(jpgData, transmittedSize, sendBuffer, 0, CurTransSize);
			transmittedSize += CurTransSize;
			
			Packet jpgDataPacket = new Packet(OpCode.JPGDATA_SEND, sendBuffer, CurTransSize);
			
			send(jpgDataPacket);
		}		
	}
}
