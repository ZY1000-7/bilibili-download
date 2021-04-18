/**
 * 视频下载线程
 */
public class DownloadThread implements Runnable {
    private Integer start;
    private Integer end;
    private Integer threadId;
    private String url;
    private String savePath;

    public DownloadThread(Integer start, Integer end, Integer threadId, String url, String savePath) {
        this.start = start;
        this.end = end;
        this.threadId = threadId;
        this.url = url;
        this.savePath = savePath;
    }

    @Override
    public void run() {
        try {
            BiliBiliApiRequest.downloadVideo(start, end, threadId, url, savePath);
        } catch (RequestError requestError) {
            System.out.println("下载失败");
            requestError.printStackTrace();
        }
    }
}
