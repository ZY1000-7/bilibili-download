//import okhttp3.*;

//import java.io.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * BiliBili的API请求
 */
public class BiliBiliApiRequest {

    /**
     * 功能描述:   申请二维码URL及扫码密钥（web端）
     * 创建时间:  2021/4/12 20:50
     *
     * @return void
     * @author zengy
     */
    public static void getOAuthKeyAndUrl() throws RequestError {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://passport.bilibili.com/qrcode/getLoginUrl")
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                StringBuffer sb = new StringBuffer("");
                String line;

                BufferedReader bf = new BufferedReader(new InputStreamReader(response.body().byteStream(), StandardCharsets.UTF_8));

                while ((line = bf.readLine()) != null) {
                    sb.append(line + "\r\n");
                }

                HashMap responseBody = JSONObject.parseObject(sb.toString(), HashMap.class);
                HashMap data = JSONObject.parseObject(responseBody.get("data").toString(), HashMap.class);

                GlobalVariable.webQRCodeUrl = (String) data.get("url");
                GlobalVariable.oauthKey = (String) data.get("oauthKey");

            } else {
                throw new RequestError("请求失败请重试!");
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    /**
     * 功能描述:   扫描QRCode验证码登录
     * 创建时间:  2021/4/12 21:05
     *
     * @return void
     * @author zengy
     */
    public static void getLoginByScanQrCode() throws RequestError {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "oauthKey=" + GlobalVariable.oauthKey);
        Request request = new Request.Builder()
                .url("http://passport.bilibili.com/qrcode/getLoginInfo")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                StringBuffer sb = new StringBuffer("");
                String line = null;

                BufferedReader bf = new BufferedReader(new InputStreamReader(response.body().byteStream(), StandardCharsets.UTF_8));

                while ((line = bf.readLine()) != null) {
                    sb.append(line + "\r\n");
                }

                HashMap responseBody = JSONObject.parseObject(sb.toString(), HashMap.class);

                if (!(boolean) responseBody.get("status")) {
                    JOptionPane.showMessageDialog(null, responseBody.get("message"), "tip", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String pattern = "SESSDATA=(.*)(?=&bili_jct)";

                    Pattern r = Pattern.compile(pattern);
                    Matcher m = r.matcher(responseBody.get("data").toString());

                    while (m.find()) {
                        GlobalVariable.SESSDATA = m.group().replace("SESSDATA=", "");
                        JOptionPane.showMessageDialog(null, "登录成功", "top", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } else {
                throw new RequestError("请求失败请重试!");
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    /**
     * 功能描述:   通过视频的bvId获取视频的具体信息,包括(剧集,清晰度等)。
     * 创建时间:  2021/4/13 20:18
     *
     * @return void
     * @author zengy
     */
    public static void getVideoPartByBvId() throws RequestError {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://api.bilibili.com/x/web-interface/view?bvid=" + GlobalVariable.bvId)
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                StringBuffer sb = new StringBuffer("");
                String line;

                BufferedReader bf = new BufferedReader(new InputStreamReader(response.body().byteStream(), StandardCharsets.UTF_8));

                while ((line = bf.readLine()) != null) {
                    sb.append(line + "\r\n");
                }

                HashMap responseBody = JSONObject.parseObject(sb.toString(), HashMap.class);
                HashMap data = JSONObject.parseObject(responseBody.get("data").toString(), HashMap.class);
                List<String> pages = JSONObject.parseArray(data.get("pages").toString(), String.class);

                // cId
                ArrayList<String> cIds = new ArrayList<>();
                // 剧集情况
                ArrayList<String> parts = new ArrayList<>();
                for (String page : pages) {
                    HashMap pageInfo = JSONObject.parseObject(page, HashMap.class);
                    parts.add(pageInfo.get("part").toString());
                    cIds.add(pageInfo.get("cid").toString());
                }
                // 存放到全全局变量
                GlobalVariable.partTitles = parts.toArray(new String[0]);
                GlobalVariable.cIds = cIds.toArray(new String[0]);
            } else {
                throw new RequestError("请求失败请重试!");
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    /**
     * 功能描述:  获取视频可选择的清晰度
     * 创建时间:  2021/4/13 21:07
     *
     * @return void
     * @author zengy
     */
    public static void getVideoClarity() throws RequestError {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://api.bilibili.com/x/player/playurl?bvid=" + GlobalVariable.bvId
                        + "&qn=" + GlobalVariable.qn
                        + "&cid=" + GlobalVariable.cIds[0]
                        + "&fnval=" + GlobalVariable.fnval
                        + "&fourk=" + GlobalVariable.fourk)
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                StringBuffer sb = new StringBuffer("");
                String line;

                BufferedReader bf = new BufferedReader(new InputStreamReader(response.body().byteStream(), StandardCharsets.UTF_8));

                while ((line = bf.readLine()) != null) {
                    sb.append(line + "\r\n");
                }

                HashMap responseBody = JSONObject.parseObject(sb.toString(), HashMap.class);

                HashMap data = JSONObject.parseObject(responseBody.get("data").toString(), HashMap.class);
                GlobalVariable.defaultQn = (Integer) data.get("quality");

                String accept_quality = data.get("accept_quality").toString();
                String[] qualities = accept_quality
                        .substring(1, accept_quality.length() - 1)
                        .split(",");

                ArrayList<String> opt = new ArrayList<>();
                for (String quality : qualities) {
                    String definitionCode = new String("");
                    int i = Integer.parseInt(quality);
                    if (i == 16) definitionCode = "360P";
                    else if (i == 32) definitionCode = "480P";
                    else if (i == 64) definitionCode = "720P";
                    else if (i == 74) definitionCode = "720P60";
                    else if (i == 80) definitionCode = "1080P";
                    else if (i == 112) definitionCode = "1080P+";
                    else if (i == 116) definitionCode = "1080P60";
                    else if (i == 120) definitionCode = "4K";
                    else definitionCode = "240P";
                    opt.add(definitionCode);
                }

                GlobalVariable.definitionCodes = opt.toArray(new String[0]);

            } else {
                throw new RequestError("请求失败请重试!");
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    /**
     * 功能描述:   获取具体集数的下载视频流URL
     * 创建时间:  2021/4/14 15:23
     *
     * @param cIdIndex 下载视频下标
     * @return void
     * @author zengy
     */
    public static void getDownloadVideoStreamUrl(Integer cIdIndex) throws RequestError {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        String url = String.format("http://api.bilibili.com/x/player/playurl?bvid=%s&qn=%d&cid=%s&fnval=0&fnver=0&fourk=1",
                GlobalVariable.bvId, GlobalVariable.qn, GlobalVariable.cIds[cIdIndex]);
        System.out.println(url);
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("Cookie", "SESSDATA=" + GlobalVariable.SESSDATA + ";")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                StringBuffer sb = new StringBuffer("");
                String line;

                BufferedReader bf = new BufferedReader(new InputStreamReader(response.body().byteStream(), StandardCharsets.UTF_8));

                while ((line = bf.readLine()) != null) {
                    sb.append(line + "\r\n");
                }

                HashMap responseBody = JSONObject.parseObject(sb.toString(), HashMap.class);
                HashMap data = JSONObject.parseObject(responseBody.get("data").toString(), HashMap.class);
                String[] durlList = JSONArray.parseObject(data.get("durl").toString(), String[].class);

                for (String durlStr : durlList) {
                    HashMap durl = JSONObject.parseObject(durlStr, HashMap.class);

                    VideoInfo videoInfo = new VideoInfo();
                    videoInfo.setUrl(durl.get("url").toString());
                    videoInfo.setSize((Integer) durl.get("size"));
                    videoInfo.setTitle(GlobalVariable.partTitles[cIdIndex]);

                    GlobalVariable.downloadVideoInfoQueue.add(videoInfo);
                }

            } else {
                throw new RequestError("请求失败请重试!");
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    /**
     * 功能描述:  下载start开始到end结束字节的视频
     * 创建时间:  2021/4/14 14:44
     *
     * @param start:    下载视频流的起始位置
     * @param end:      下载视频流的结束位置
     * @param threadId: 线程ID
     * @param url:      下载视频的URL
     * @param savePath: 保存目录
     * @return void
     * @author zengy
     */
    public static void downloadVideo(Integer start, Integer end, Integer threadId,
                                     String url, String savePath)
            throws RequestError {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("referer", "https://www.bilibili.com")
                .build();
        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {

                File saveFile = new File(savePath);

                BufferedInputStream bis = new BufferedInputStream(response.body().byteStream());
                bis.skip(start);

                RandomAccessFile raf = new RandomAccessFile(saveFile, "rw");

                byte[] buffer = new byte[1024];
                int len = 0;
                int pos = start;
                int frequency = (end - start) % 1024 == 0 ? (end - start) / 1024 : (end - start) / 1024 + 1;

                for (int i = 0; i < frequency; i++) {
                    len = bis.read(buffer);
                    synchronized (raf) {
                        raf.seek(pos);
                        raf.write(buffer, 0, len);
                    }
                    pos += len;
                    GlobalVariable.completeByte += len;
                }

                raf.close();
                bis.close();
                GlobalVariable.flagDownload -= 1;

                if (GlobalVariable.flagDownload == 0) GlobalVariable.isComplete = true;

            } else {
                throw new RequestError("下载失败!请重试");
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
