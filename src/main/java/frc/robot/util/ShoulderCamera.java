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

    private Mat cameraMat = new Mat(); 

    private boolean isEnabled = false; 
    
    private Thread cameraUpdateThread = new Thread(() -> {
            // This cannot be 'true'. The program will never exit if it is. This
            // lets the robot stop this thread when restarting robot code or
            // deploying.
            while (!Thread.interrupted()) {

                if (!isEnabled) continue;
                // Tell the CvSink to grab a frame from the camera and put it
                // in the source mat.  If there is an error notify the output.
                if (sink.grabFrame(cameraMat) == 0) {
                    // Send the output the error.
                    outputStream.notifyError(sink.getError());
                    // skip the rest of the current iteration
                    continue;
                }

                processCameraFrame(cameraMat);

                outputStream.putFrame(cameraMat);
            }
    }); 

    public ShoulderCamera(String name, int port, int width, int height) {
        this.camera = CameraServer.startAutomaticCapture(port); 
        this.sink = CameraServer.getVideo(camera); 

        this.outputStream = CameraServer.putVideo(name, width, height); 

        setEnabled(true);

        cameraUpdateThread.start();
    }

    private void processCameraFrame(Mat mat) {
        Imgproc.rectangle(mat, new Point(
            SmartDashboard.getNumber("x0", 0), 
            SmartDashboard.getNumber("y0", 0)
            ), new Point(
                SmartDashboard.getNumber("x1", 50), 
                SmartDashboard.getNumber("y1", 50)
                ), new Scalar(255, 255, 255));
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
