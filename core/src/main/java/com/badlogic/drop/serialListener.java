package com.badlogic.drop;


import com.fazecast.jSerialComm.SerialPort;
import java.util.Scanner;

public class serialListener implements Runnable{

    SerialPort port;
    Scanner data;
    int number = 0;

    public int serialListener(){
        return number;
    }

    @Override
    public void run() {
        port = SerialPort.getCommPort("COM3");
        if (port.openPort()) {
            System.out.println("Port opened");
        } else {
            System.out.println("Not opened");
        }
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        data = new Scanner(port.getInputStream());
        while (data.hasNextLine()) {
            try {
                number = Integer.parseInt(data.nextLine());
            } catch (Exception e) {
            }
        }
    }
}
