import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 全局变量
 */
public class GlobalVariable {
    /*申请二维码URL及扫码密钥(web端)*/
    public static String webQRCodeUrl = "";
    public static String oauthKey = "";
    /*使用扫码登录（web端）*/
    public static String SESSDATA = "";
    /*二维码保存路径*/
    public static String PATH = "";
    /*获取视频详细信息(web端)*/
    public static String bvId = "";
    public static String aId = "";
    /*下载视频信息队列*/
    public static Queue<VideoInfo> downloadVideoInfoQueue = new LinkedList<>();
    /*获取视频流URL(web端)*/
    public static Integer fnval = 0;
    public static Integer fnver = 0;
    public static Integer fourk = 1;
    public static Integer qn = 32;
    /*视频可选清晰度*/
    public static String[] definitionCodes = new String[]{"360P", "480P", "720P", "720P60", "1080P", "1080P+", "1080P60 ", "4K"};
    /*默认清晰度*/
    public static Integer defaultQn = 16;
    /*视频可选剧集*/
    public static String[] partTitles = new String[]{"1P"};
    /*视频可选剧集的cId*/
    public static String[] cIds = new String[]{};
    /*线程数*/
    public static Integer threadNum = 3;
    /*当前下载的视频基本信息*/
    public static VideoInfo curVideoInfo = new VideoInfo();
    /*当前下载视频的大标题*/
    public static String title = "";
    /*判断是否是视频下载完成*/
    public static Boolean isComplete = false;
    /*当前下载了多少byte*/
    public static Integer completeByte = 0;
    /*当前下载的进度*/
    public static Double rate = 0.0;
    /*当前是否下载完成(默认为0:下载完成状态)*/
    public static Integer flagDownload = 0;
    /*公平锁*/
    public static ReentrantLock reentrantLock = new ReentrantLock();
    /*下载进度线程*/
    public static Thread logThread = null;
    /*下载管理线程*/
    public static Thread downloadUtilThread = null;

    /*默认保存目录*/
    public static String defaultSavePath = "e:/";
    /*用户选择保存目录*/
    public static String selectSavePath = "e:/";


    /**
     * 功能描述:   将清晰度选项框中的值转化为清晰度代码
     * 创建时间:  2021/4/13 21:25
     *
     * @param definitionCode: 清晰度选项框中的文本
     * @return java.lang.Integer
     * @author zengy
     */
    public static void getQn(String definitionCode) {
        switch (definitionCode) {
            case "360P":
                qn = 16;
                break;
            case "480P":
                qn = 32;
                break;
            case "720P":
                qn = 64;
                break;
            case "720P60":
                qn = 74;
                break;
            case "1080P":
                qn = 80;
                break;
            case "1080P+":
                qn = 112;
                break;
            case "1080P60":
                qn = 116;
                break;
            case "4K":
                qn = 120;
                break;
            default:
                qn = defaultQn;
                break;
        }
    }

    /**
     * 功能描述:   获取进度条的进度
     * 创建时间:  2021/4/14 19:33
     *
     * @return java.lang.String
     * @author zengy
     */
    public static String getLoading() {
        StringBuffer sb = new StringBuffer("");
        GlobalVariable.rate = GlobalVariable.completeByte * 1.0 / GlobalVariable.curVideoInfo.getSize();
        int pos = (int) (GlobalVariable.rate * 25);
        for (int i = 0; i < pos; i++) {
            sb.append("=");
        }
        for (int i = pos; i < 25; i++) {
            sb.append("  ");
        }
        sb.append("  " + (int) ((double) GlobalVariable.rate * 100) + "%");
        return sb.toString();
    }
}
