package team.se.Img;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;


public class Img_refresh {

    public void accpetServer(ImageView imageView, XXX.LoadHandler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Socket socket = new Socket("192.168.0.106",1234);
                    System.out.println("socket is: "+socket);
                    InetAddress address = InetAddress.getLocalHost();
                    String ip = address.getHostAddress();
                    System.out.println("客户端  "+ip+" 接入");

                    BufferedReader bufferedReader = null;
                    InputStream inputStream = socket.getInputStream();
                    System.out.println("length is "+inputStream.available());

                    int width=320,height=240;
                    Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);

                    byte[] bytes = new byte[407200];
                    int length,off=0;

                    while ((length=inputStream.read(bytes,off,4000))!=-1){
                        off=off+length;
                        System.out.println("lenght is "+off);
                        if(off>=307200){
                            off=off-307200;
                            System.out.printf("%d %d %d %d\n",bytes[0],(int)bytes[1],(int)bytes[2],(int)bytes[3]);
                            bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(bytes,0,307200));
                            handler.obtainMessage(0,bitmap).sendToTarget();
                        }
                    }
                    //     System.out.println("the off is "+off);

                    socket.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

}