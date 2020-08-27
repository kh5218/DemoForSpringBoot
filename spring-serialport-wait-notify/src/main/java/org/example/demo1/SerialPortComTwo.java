package org.example.demo1;


import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;

/**
 * @author halo.l
 * @date 2020/8/4
 */
@Data
public class SerialPortComTwo {

    @Autowired
    private WaitNotifyAll waitNotifyAll;

    /**
     * 串口号
     */
    private String portName;
    /**
     * 波特率
     */
    private Integer baudRate;
    /**
     * 数据位
     */
    private Integer dataBits;
    /**
     * 停止位
     */
    private Integer stopBits;
    /**
     * 校验位
     */
    private Integer parity;

    private SerialPort serialPort;

    public void init() throws SerialPortException {
        serialPort = new SerialPort(portName);
        //如果串口打开着，先关闭
        if(null != serialPort && serialPort.isOpened()) {
            serialPort.closePort();
        }
        //打开串口
        serialPort.openPort();
        serialPort.addEventListener(new SerialPortEventListener() {
            @SneakyThrows
            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                switch (serialPortEvent.getEventType()) {
                    case SerialPortEvent.RXCHAR:
                        byte[] bytes = {};
                        try {
                            bytes = serialPort.readBytes();
                        } catch (SerialPortException e) {
                            e.printStackTrace();
                        }
                        String data = new String(bytes, "GBK");
                        waitNotifyAll.notifyContinue(data);
                        break;
                    default:
                        break;
                }
            }
        });
        serialPort.setParams(baudRate, dataBits, stopBits, parity);
    }
}
