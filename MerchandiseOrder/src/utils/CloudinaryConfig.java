package utils;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.util.Map;

public class CloudinaryConfig {
    private Cloudinary cloudinary;

    // Hàm khởi tạo, thiết lập tài khoản Cloudinary
    public CloudinaryConfig() {
        // Thay thế bằng thông tin tài khoản của bạn từ Cloudinary Dashboard
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "doetmtxxi",
                "api_key", "265835987149587",
                "api_secret", "R-geOaIdXgQ0SxxwBGvX6qlLvtE"));
    }

    // Hàm upload file lên Cloudinary
    public String uploadImage(String filePath) {
        try {
            File file = new File(filePath);
            // Thêm tham số 'folder' để upload vào thư mục "avatar"
            Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap(
                    "folder", "avatar" // Chỉ định thư mục upload là "avatar"
            ));
            // Trả về URL của ảnh đã upload
            return uploadResult.get("url").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
