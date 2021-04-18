/**
 * 视频基本信息
 */
public class VideoInfo {
    private String title;
    private String url;
    private Integer size;

    public VideoInfo() {
    }

    public VideoInfo(String title, String url, Integer size) {
        this.title = title;
        this.url = url;
        this.size = size;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", size=" + size +
                '}';
    }
}
