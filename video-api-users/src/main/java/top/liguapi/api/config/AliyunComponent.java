package top.liguapi.api.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * @Description
 * @Author lww
 * @Date 2022/5/18 15:59
 */
@Component
@ConfigurationProperties(prefix = "aliyun")
@Data
public class AliyunComponent {
    String accessKeyId;
    String accessKeySecret;
    Oss oss;

    public OSS getOSSClient() {
        OSS ossClient = new OSSClientBuilder().build(oss.getEndPoint(), accessKeyId, accessKeySecret);
        return ossClient;
    }

    public String fileUpload(String path, String filename, InputStream inputStream) {
        OSS ossClient = getOSSClient();
        String key = path + "/" + filename;
        ossClient.putObject(oss.getBucket(), key, inputStream);
        ossClient.shutdown();
        return "https://" + oss.getBucket() + "." + oss.getEndPoint() + "/" + key;
    }
}
