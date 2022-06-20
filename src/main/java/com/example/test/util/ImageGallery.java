package com.example.test.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ImageGallery {
    public  static String randImage(){
        String[] images={"https://picsum.photos/id/103/2592/1936",
                "https://picsum.photos/id/1035/5854/3903",
                "https://picsum.photos/id/1036/4608/3072",
                "https://picsum.photos/id/1040/4496/3000",
                "https://picsum.photos/id/1043/5184/3456"};
        Random random=new Random();
        return images[random.nextInt(4)];
    }
    public  static String randAvatarImage(){
        String[] images={"https://s4.ax1x.com/2022/02/16/HhQD00.jpg",
        "https://s4.ax1x.com/2022/02/16/HhQchF.jpg",
        "https://s4.ax1x.com/2022/02/16/HhQ2p4.jpg",
        "https://s4.ax1x.com/2022/02/16/HhQR1J.jpg",
        "https://s4.ax1x.com/2022/02/16/HhQWc9.jpg",
        "https://s4.ax1x.com/2022/02/16/HhQfXR.jpg"};
        Random random=new Random();
        return images[random.nextInt(5)];
    }
}
