package top.liguapi.api.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Description 用于生成默认头像的工具类
 * @Author lww
 * @Date 2022/5/17 20:23
 */
public class AvatarUtils {

    private static List<String> photos = new ArrayList<>();

    static {
        photos.add("https://s1.ax1x.com/2022/05/14/O6MdxJ.jpg");
        photos.add("https://s1.ax1x.com/2022/05/14/O60niQ.jpg");
    }

    public static String getPhoto() {
        int random = new Random().nextInt(photos.size());
        return photos.get(random);
    }
}
