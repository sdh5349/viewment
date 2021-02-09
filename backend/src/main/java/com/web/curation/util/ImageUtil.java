package com.web.curation.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {

    static final int maxWidth = 1000;

    public static void save(String type, File file) throws Exception {
        BufferedImage originalImage = ImageIO.read(file);

        Metadata metadata = ImageMetadataReader.readMetadata(file);
        ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        JpegDirectory jpegDirectory = (JpegDirectory) metadata.getFirstDirectoryOfType(JpegDirectory.class);

        int orientation = 1;
        try {
            orientation = exifIFD0Directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        AffineTransform affineTransform = getAffineTransform(orientation, jpegDirectory);

        AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage destinationImage = new BufferedImage(originalImage.getHeight(), originalImage.getWidth(), originalImage.getType());
        destinationImage = affineTransformOp.filter(originalImage, destinationImage);

        ImageIO.write(resize(type, orientation, destinationImage), "jpg", file);
    }

    private static AffineTransform getAffineTransform(int orientation, JpegDirectory jpegDirectory) throws MetadataException {
        AffineTransform affineTransform = new AffineTransform();
        int width = jpegDirectory.getImageWidth();
        int height = jpegDirectory.getImageHeight();

        switch (orientation) {
            case 1:
                break;
            case 2: // Flip X
                affineTransform.scale(-1.0, 1.0);
                affineTransform.translate(-width, 0);
                break;
            case 3: // PI rotation
                affineTransform.translate(width, height);
                affineTransform.rotate(Math.PI);
                break;
            case 4: // Flip Y
                affineTransform.scale(1.0, -1.0);
                affineTransform.translate(0, -height);
                break;
            case 5: // - PI/2 and Flip X
                affineTransform.rotate(-Math.PI / 2);
                affineTransform.scale(-1.0, 1.0);
                break;
            case 6: // -PI/2 and -width
                affineTransform.translate(height, 0);
                affineTransform.rotate(Math.PI / 2);
                break;
            case 7: // PI/2 and Flip
                affineTransform.scale(-1.0, 1.0);
                affineTransform.translate(-height, 0);
                affineTransform.translate(0, width);
                affineTransform.rotate(3 * Math.PI / 2);
                break;
            case 8: // PI / 2
                affineTransform.translate(0, width);
                affineTransform.rotate(3 * Math.PI / 2);
                break;
            default:
                break;
        }

        return affineTransform;
    }

    private static BufferedImage resize(String type, int orientation, BufferedImage image) throws Exception {

        int newWidth = image.getWidth(null);
        int newHeigt = image.getHeight(null);

        switch (orientation) {
            case 5:
            case 6:
            case 7:
            case 8:
                newWidth = image.getHeight(null);
                newHeigt = image.getHeight(null);
        }


        if ("profile".equals(type)) {
            newHeigt = 200;
            newWidth = 200;
        } else if ("article".equals(type)) {
            if (newWidth > maxWidth) {
                newHeigt = maxWidth * newWidth / newHeigt;
                newWidth = maxWidth;
            }
        } else if ("thumbnail".equals(type)) {
            newHeigt = 400;
            newWidth = 400;
        }

        // 이미지 리사이즈
        // Image.SCALE_DEFAULT : 기본 이미지 스케일링 알고리즘 사용
        // Image.SCALE_FAST    : 이미지 부드러움보다 속도 우선
        // Image.SCALE_REPLICATE : ReplicateScaleFilter 클래스로 구체화 된 이미지 크기 조절 알고리즘
        // Image.SCALE_SMOOTH  : 속도보다 이미지 부드러움을 우선
        // Image.SCALE_AREA_AVERAGING  : 평균 알고리즘 사용

        Image resizeImage = image.getScaledInstance(newWidth, newHeigt, Image.SCALE_SMOOTH);

        BufferedImage newImage = new BufferedImage(newWidth, newHeigt, BufferedImage.TYPE_INT_RGB);
        Graphics g = newImage.getGraphics();
        g.drawImage(resizeImage, 0, 0, null);
        g.dispose();

        return newImage;
    }

    public static File multipartToFile(String path, MultipartFile multipartFile) throws IOException {
        File file = new File(path);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartFile.getBytes());
        fos.close();
        return file;
    }
}
