
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;

/**
 * QrCode验证码工具类
 */
public class QrCodeUtils {
    // 二维码的宽度
    static final int WIDTH = 264;
    // 二维码的高度
    static final int HEIGHT = 264;
    // 二维码的格式
    static final String FORMAT = "png";

    /**
     * 功能描述:   根据URl生成QrCode.png
     * 创建时间:  2021/4/12 13:57
     *
     * @return void
     * @author zengy
     */
    public static void createQrCode() {
        try {
            HashMap hashMap = new HashMap();
            // 设置二维码字符编码
            hashMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            // 设置二维码纠错等级
            hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            // 设置二维码边距
            hashMap.put(EncodeHintType.MARGIN, 2);

            // 开始生成二维码
            BitMatrix bitMatrix = new MultiFormatWriter().encode(GlobalVariable.webQRCodeUrl,
                    BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hashMap);


            // 导出到指定目录
            MatrixToImageWriter.writeToPath(bitMatrix, FORMAT, new File(GlobalVariable.PATH).toPath());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "生成二维码失败!", "fail", JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
        }
    }
}