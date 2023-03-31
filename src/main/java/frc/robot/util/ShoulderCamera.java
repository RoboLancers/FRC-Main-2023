package frc.robot.util;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShoulderCamera {
    private CvSink sink; 
    private UsbCamera camera; 

    private CvSource outputStream; 

    private Mat cameraMat; 

    private String name; 

    private int xLeft; 
    private int xRight; 

    private boolean isEnabled = false; 
    
    private Thread cameraUpdateThread = new Thread(() -> {
            while (!Thread.interrupted()) {
                if (!isEnabled) continue;
                if (sink.grabFrame(cameraMat) == 0) {
                    outputStream.notifyError(sink.getError());
                    continue;
                }

                processCameraFrame(cameraMat);

                outputStream.putFrame(cameraMat);
            }
    }); 

    public ShoulderCamera(String name, int port, int width, int height, int xLeft, int xRight) {
        this.camera = CameraServer.startAutomaticCapture(port); 
        this.sink = CameraServer.getVideo(camera); 
        this.name = name; 
        this.xLeft = xLeft;
        this.xRight = xRight;

        this.outputStream = CameraServer.putVideo(name, width, height); 

        cameraMat = new Mat(); 

        setEnabled(true);

        cameraUpdateThread.start();

        SmartDashboard.putNumber("x0-" + name, xLeft); 
        SmartDashboard.putNumber("y0-" + name, 0); 
        SmartDashboard.putNumber("x1-" + name, xRight); 
        SmartDashboard.putNumber("y1-" + name, 180); 
    }

    private void processCameraFrame(Mat mat) {
        Imgproc.rectangle(mat, new Point(
            SmartDashboard.getNumber("x0-" + name, xLeft), 
            SmartDashboard.getNumber("y0-" + name, 0)
            ), new Point(
                SmartDashboard.getNumber("x1-" + name, xRight), 
                SmartDashboard.getNumber("y1-" + name, 180)
                ), new Scalar(50, 50, 255));
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            camera.setFPS(15);
        } else {
            camera.setFPS(1); 
        } 
        this.isEnabled = enabled; 
    }
}
