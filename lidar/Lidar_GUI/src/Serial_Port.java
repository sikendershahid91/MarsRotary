/*
 * By.....
 * 	   The Most Awesome Bird in Senior Design
 * 									   Alamin Ahmed 
 * 											(also his crews)
 * */

import java.io.PrintWriter;
import java.util.Scanner;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;


public class Serial_Port {
	public SerialPort ports[];
	public String ports_string[];
	public SerialPort chosen_port = null;
	
	public byte[] DS_Buffer = new byte[7];
	public boolean DS_Pass = false;
	public boolean DS = false;
	
	public boolean DX = false;
	public boolean DX_Pass = false;
	
	public int counter = 0;
	
	public int errorCounter = 0;
	
	Scanner data = null;
	PrintWriter serialOut = null;
	
	public boolean writeToPort(String st){
		if(serialOut != null){
			
			if(!DS && st.equals("DS")) {
				DS = true;
				DX = false;
				System.out.println("DS");
			}else if(st.equals("DX")){
				DS_Pass = false;
				DS = false;
				DX = true;
				counter = 0;
				System.out.println("DX");
				serialOut.println(st);
				serialOut.flush();
				try {Thread.sleep(50);} catch (InterruptedException e) {System.out.println("sleep problem");}
				while(DX){
					try {Thread.sleep(50);} catch (InterruptedException e) {System.out.println("sleep problem");}
					serialOut.println(st);
					serialOut.flush();
				}
				
			}else if(!DS){
				if(st.equals("MZ")){
					System.out.println("MZ");
				}else if(st.equals("MI")){
					System.out.println("MI");
				}else if(st.equals("LI")){
					System.out.println("LI");
				}else if(st.equals("IV")){
					System.out.println("IV");
				}else if(st.equals("ID")){
					System.out.println("ID");
				}else if(st.equals("RR")){
					System.out.println("RR");
				}
				
					
				
			}
			
			serialOut.println(st);
			serialOut.flush();
			return true;
			
			
		}
		return false;
	}
	
	public Serial_Port(){
		
	}
	public String[] getPorts(){
		ports = SerialPort.getCommPorts();
		ports_string = new String[ports.length];
		for(int i = 0; i < ports.length; i++){
			ports_string[i] = ports[i].getDescriptivePortName();
			//System.out.println(i+1 + ") " + ports[i].getDescriptivePortName());
			//System.out.println(i+1 + ") " + ports[i].getDescriptivePortName());
		}
		return ports_string;
	}
	public boolean openPort(int num){
		if(chosen_port != null) chosen_port.closePort();
		chosen_port = ports[num];
		if(chosen_port.openPort()){
			//data = new Scanner(chosen_port.getInputStream());
			serialOut = new PrintWriter(chosen_port.getOutputStream());
			
			chosen_port.setBaudRate(115200);
			
			
			chosen_port.addDataListener(new SerialPortPacketListener(){

				@Override
				public int getListeningEvents() {
					// TODO Auto-generated method stub
					return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
				}

				@Override
				public void serialEvent(SerialPortEvent event) {
					byte[] newData = event.getReceivedData();
//					for (int i = 0; i < newData.length; ++i){
//					System.out.println(newData.length);
//					}
					if(DS){
						if(!DS_Pass){
							if(counter == 6){
								if((char)DS_Buffer[0] == 'D' & (char)DS_Buffer[1] == 'S' & (char)DS_Buffer[2] == '0' & (char)DS_Buffer[3] == '0' & (char)DS_Buffer[4] == 'P'){
									System.out.println("DS Passed");
									DS_Pass = true;
								}
								counter = 0;
								DS_Buffer[counter++] = newData[0];
								//System.out.println(newData[0] & 0xFF);
							}else{
								DS_Buffer[counter++] = newData[0];
								//System.out.println(newData[0]);
							}
						}else{
							if(counter == 7){
								int angle_int = ((DS_Buffer[2] << 8)& 0xFF) + ((DS_Buffer[1])& 0xFF);
								double degree = angle_int/16.0;
								int distance = ((DS_Buffer[4] << 8)& 0xFF) + ((DS_Buffer[3])& 0xFF);
								
								int calculatedSum = 0;
								for(int i = 0; i < 6; i++){
								    //add each status byte to the running sum
								    calculatedSum += (DS_Buffer[i] & 0xFF);
								}
								calculatedSum = calculatedSum % 255;
								int expectedSumVal = (DS_Buffer[6] & 0xFF);
								//return calculatedSum === expectedSumVal;
								
								if(degree >= 0 && degree <= 3){
									
									System.out.println("Error: " + (DS_Buffer[0] & 0xFF) +" angle: " + degree + " distance: " + distance +" Strength: "+ (DS_Buffer[5] & 0xFF) + " checksum: " + (calculatedSum == expectedSumVal));
								}
								//System.out.println("Error: "+DS_Buffer[0]+" angle: " + degree + " distance: " + distance +" Strength: "+DS_Buffer[5] + " checksum: " + (calculatedSum == expectedSumVal));
								counter = 0;
								DS_Buffer[counter++] = newData[0];
								//System.out.println(newData[0] & 0xFF);
							}else{
								DS_Buffer[counter++] = newData[0];
								//System.out.println(newData[0] & 0xFF);
							}
						}
					}else if(DX){
						//counter = 0;
						
							if(counter == 6){
								if((char)DS_Buffer[0] == 'D' & (char)DS_Buffer[1] == 'X' & (char)DS_Buffer[2] == '0' & (char)DS_Buffer[3] == '0' & (char)DS_Buffer[4] == 'P'){
									System.out.println("DX Passed");
									DX = false;
								}
								counter = 0;
								//DS_Buffer[counter++] = newData[0];
								//System.out.println(newData[0] & 0xFF);
							}else{
								DS_Buffer[counter++] = newData[0];
								//System.out.println(newData[0]);
							}
						
						
					}else{
						for (int i = 0; i < newData.length; ++i){
							System.out.print((char)newData[i]);
						}
					}
					

				}

				@Override
				public int getPacketSize() {
					// TODO Auto-generated method stub
					//if(!first) return 5;
					return 1;
				}
			});
					
			return true;	
		}
		
		if(serialOut != null)serialOut.close();
		if(data != null)data.close();
		
		data = null;
		serialOut = null;
		
		//if(chosen_port != null)chosen_port.closePort();
		chosen_port = null;
		return false;
	}
	
	public class Worker extends Thread {

	    @Override
	    public void run() {
	        
	        // Loop for ten iterations.
//	    	while(true){
//		    	while(data.hasNext()){
//					System.out.println(data.next());
//				}
//	    	}

	    }

	}
}


