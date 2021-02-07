package com.web.curation.util;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {

    static final int maxWidth = 1000;

    public static void resize(String type, File file) throws Exception {
        String imgFormat = "jpg";

        Image image = ImageIO.read(file);
        int newWidth = image.getWidth(null);
        int newHeigt = image.getHeight(null);

        if ("profile".equals(type)) {
            newHeigt = 300;
            newWidth = 300;
        } else if (newWidth > maxWidth) {
            newHeigt = maxWidth * newWidth / newHeigt;
            newWidth = maxWidth;
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
        ImageIO.write(newImage, imgFormat, file);
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
