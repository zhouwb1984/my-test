package com.zcode.demo.mytest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author zhouwb
 * @since 2019/3/8
 */
public class GraphicsTest {

    /**
     * 导入本地图片到缓冲区
     */
    public BufferedImage loadImageLocal(String imgName) {
        try {
            return ImageIO.read(new File(imgName));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public BufferedImage modifyImagetogeter(BufferedImage car, BufferedImage dog) {

        try {
            Font font = new Font("SansSerif", Font.BOLD, 11);

            Graphics2D g = car.createGraphics();
            g.drawImage(dog, 0, 0, dog.getWidth(), dog.getHeight(), null);
            g.setFont(font);
            g.setColor(new Color(255, 0, 0));
            g.drawString("你好，我是graphics 2d!...666...", 20, 200);
            g.setColor(new Color(0, 150, 0));
            g.drawString("你好。。。", 20, 230);
            g.dispose();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return car;
    }

    /**
     * 生成新图片到本地
     */
    public void writeImageLocal(String newImage, BufferedImage img) {
        if (newImage != null && img != null) {
            try {
                File outputfile = new File(newImage);
                ImageIO.write(img, "jpg", outputfile);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {

        GraphicsTest gt = new GraphicsTest();

        BufferedImage dog = gt.loadImageLocal("D:/2.jpg");
        BufferedImage car = gt.loadImageLocal("D:/1.jpg");

        gt.writeImageLocal("D:/new10.jpg", gt.modifyImagetogeter(car, dog));
        //将多张图片合在一起
        System.out.println("success");

    }
}
