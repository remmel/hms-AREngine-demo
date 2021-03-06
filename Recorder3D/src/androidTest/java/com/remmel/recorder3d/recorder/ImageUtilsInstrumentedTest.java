package com.remmel.recorder3d.recorder;

import android.graphics.Bitmap;

import androidx.test.InstrumentationRegistry;

import org.junit.Test;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class ImageUtilsInstrumentedTest {

    private static final String TAG = ImageUtilsInstrumentedTest.class.getSimpleName();

    private static final File DIR = InstrumentationRegistry.getInstrumentation().getTargetContext().getExternalFilesDir("test");
//    private static final String DIR = "/storage/emulated/0/Android/data/com.remmel.recorder3d/files/test";
    private static final String IMAGE_YUVNV21 =  DIR + "/00000012_image.bin";
    private static final String TUMDEPTH = DIR + "/tum/1305031102.160407.png";
    private static final int IMG_W = 1440;
    private static final int IMG_H = 1080;


    // InputStream is = getClass().getResourceAsStream("toto.txt"); to avoid copying the image in Android phone folder
    @Test
    public void writeYuvToJpeg() throws IOException {
        byte[] bytesYuvNv21 = Files.readAllBytes(Paths.get(IMAGE_YUVNV21));
        IoUtils.writeYUV(new File(DIR + "/tmp_image_writeYuvToJpeg.jpg"), bytesYuvNv21, IMG_W, IMG_H);
    }

    @Test
    //Cannot save bin file on memory as too big (3x)
    public void binToBitmap() throws IOException {
        byte[] bytesYuvNv21 = Files.readAllBytes(Paths.get(IMAGE_YUVNV21));
        Bitmap bitmap = ImageUtils.n21ToBitmapViaDecode(bytesYuvNv21, IMG_W, IMG_H);
        IoUtils.writeBitmapAsPng(new File(DIR + "/tmp_image_writeYuvManualDecode.png"), bitmap); //just to check, we don't care it's PNG

        Bitmap bitmap2 = ImageUtils.n21ToBitmapViaJpg(bytesYuvNv21, IMG_W, IMG_H);
        IoUtils.writeBitmapAsPng(new File(DIR + "/tmp_image_writeYuvViaJpg.png"), bitmap2); //just to check, we don't care it's PNG
    }


    @Test
    public void testOpenCVAndroid() {
        org.bytedeco.javacpp.Loader.load(org.bytedeco.javacpp.opencv_java.class);
        Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
        assertEquals("dump string size", 47, mat.dump().length());
    }

    @Test
    public void bin2png16bAndroid() throws IOException {
        org.bytedeco.javacpp.Loader.load(org.bytedeco.javacpp.opencv_java.class);
        String depth16 = DIR + "/00003795_depth16.bin"; // DEPTH16;
        ImageUtils.writeDepth16binInPng16GrayscaleTum(depth16, 240, 180, depth16+".png");
    }
}