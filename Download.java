import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * BiliBili-Download工具的主页面
 */
public class Download {
    private JFrame downloadJFrame;
    private JPanel downloadJPanel;
    private JPanel bvIdJPanel;
    private JPanel buttonJPanel;
    private JPanel logJPanel;
    // bvIdJPanel中的组件
    private JLabel bvIdJLabel;
    private JTextField bvIdJTextField;
    private JComboBox bvIdPartJComboBox;
    private JComboBox bvIdDefinitionJComboBox;
    // buttonJPanel中的组件
    private JButton selectDirJButton;
    private JButton feedJButton;
    private JButton searchJButton;
    private JButton loginButton;
    private JButton downloadButton;
    // logJPanel中的组件
    private JScrollPane logJScrollPane;
    private JTextArea logJTextArea;


    /**
     * 功能描述:   初始化bvIdJPanel
     * 创建时间:  2021/4/11 19:02
     *
     * @return void
     * @author zengy
     */
    public void initBvIdJPanel() {
        bvIdJPanel = new JPanel();
        bvIdJPanel.setPreferredSize(new Dimension(70, 35));
        bvIdJPanel.setBackground(Color.WHITE);

        GridBagLayout layout = new GridBagLayout();
        bvIdJPanel.setLayout(layout);

        // 创建组件
        bvIdJLabel = new JLabel("bvId :");
        bvIdJPanel.add(bvIdJLabel);

        bvIdJTextField = new JTextField();
        bvIdJTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                GlobalVariable.bvId = bvIdJTextField.getText().trim();
            }
        });
        bvIdJPanel.add(bvIdJTextField);


        bvIdPartJComboBox = new JComboBox<String>(GlobalVariable.partTitles);
        bvIdPartJComboBox.setPreferredSize(new Dimension(120, 30));
        bvIdPartJComboBox.setBackground(Color.WHITE);
        bvIdJPanel.add(bvIdPartJComboBox);

        bvIdDefinitionJComboBox = new JComboBox<String>(GlobalVariable.definitionCodes);
        bvIdDefinitionJComboBox.setPreferredSize(new Dimension(90, 30));
        bvIdDefinitionJComboBox.setBackground(Color.WHITE);
        bvIdJPanel.add(bvIdDefinitionJComboBox);

        // GridBagLayout布局
        GridBagConstraints s = new GridBagConstraints();
        s.fill = GridBagConstraints.BOTH;
        s.weighty = 0;

        s.gridwidth = 1;
        s.weightx = 0;
        s.insets = new Insets(0, 0, 0, 32);
        layout.setConstraints(bvIdJLabel, s);

        s.gridwidth = 6;
        s.weightx = 1;
        layout.setConstraints(bvIdJTextField, s);

        s.gridwidth = 4;
        s.weightx = 0;
        s.insets = new Insets(0, 0, 0, 32);
        layout.setConstraints(bvIdPartJComboBox, s);

        s.gridwidth = 2;
        s.weightx = 0;
        s.insets = new Insets(0, 0, 0, 0);
        layout.setConstraints(bvIdDefinitionJComboBox, s);
    }

    /**
     * 功能描述:   创建圆角按钮
     * 创建时间:  2021/4/11 21:51
     *
     * @param text:按钮文本
     * @return javax.swing.JButton
     * @author zengy
     */
    public JButton createJButton(String text) {
        JButton jButton = new JButton(text);
        jButton.setBackground(Color.WHITE);
        jButton.setPreferredSize(new Dimension(70, 30));
        jButton.setBorder(new RoundedBorder(10));
        return jButton;
    }

    /**
     * 功能描述:   初始化buttonJPanel
     * 创建时间:  2021/4/11 21:50
     *
     * @return void
     * @author zengy
     */
    public void initButtonJPanel() {
        buttonJPanel = new JPanel();
        buttonJPanel.setPreferredSize(new Dimension(70, 35));
        buttonJPanel.setBackground(Color.WHITE);

        GridBagLayout layout = new GridBagLayout();
        buttonJPanel.setLayout(layout);

        selectDirJButton = createJButton("目录选择");
        feedJButton = createJButton("投食");
        searchJButton = createJButton("搜索");
        loginButton = createJButton("登录");
        downloadButton = createJButton("下载");

        buttonJPanel.add(selectDirJButton);
        selectDirJButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();

            chooser.setCurrentDirectory(new File(GlobalVariable.defaultSavePath));
            chooser.setSelectedFile(new File(""));
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int result = chooser.showOpenDialog(null);
            if (JFileChooser.APPROVE_OPTION == result) {
                GlobalVariable.selectSavePath = chooser.getSelectedFile().getPath();
            }
        });

        buttonJPanel.add(feedJButton);
        feedJButton.addActionListener(e -> JOptionPane.showMessageDialog(null, "功能待开发!", "feed", JOptionPane.INFORMATION_MESSAGE));

        buttonJPanel.add(searchJButton);
        searchJButton.addActionListener(e -> {
            try {
                // 获取视频信息
                BiliBiliApiRequest.getVideoPartByBvId();
                // 重置剧集选择下拉列表
                bvIdPartJComboBox.removeAllItems();
                for (String part : GlobalVariable.partTitles) {
                    bvIdPartJComboBox.addItem(part);
                }
                bvIdPartJComboBox.addItem("All");
                // 获取视频可选清晰度信息
                BiliBiliApiRequest.getVideoClarity();
                // 重置清晰度选择下拉列表
                bvIdDefinitionJComboBox.removeAllItems();
                for (String definitionCode : GlobalVariable.definitionCodes) {
                    bvIdDefinitionJComboBox.addItem(definitionCode);
                }
            } catch (RequestError requestError) {
                JOptionPane.showMessageDialog(null, "请重新搜索",
                        "fail", JOptionPane.INFORMATION_MESSAGE);
                requestError.printStackTrace();
            }
        });

        buttonJPanel.add(loginButton);
        loginButton.addActionListener(e -> {
            // 设置保存路径

            try {
                GlobalVariable.PATH = new File("").getCanonicalPath() + "\\QRCode.png";
                BiliBiliApiRequest.getOAuthKeyAndUrl();
            } catch (RequestError requestError) {
                JOptionPane.showMessageDialog(null, requestError.getMessage(),
                        "fail", JOptionPane.INFORMATION_MESSAGE);
                requestError.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            QrCodeUtils.createQrCode();
            QrCode qrCode = new QrCode();
            qrCode.initQrCodeFrame();
        });

        buttonJPanel.add(downloadButton);
        downloadButton.addActionListener(e -> {

            System.out.println("Click");

            try {
                // 获取下载视频的Url
                Integer cIdIndex = bvIdPartJComboBox.getSelectedIndex();
                GlobalVariable.getQn(bvIdDefinitionJComboBox.getSelectedItem().toString());
                BiliBiliApiRequest.getDownloadVideoStreamUrl(cIdIndex);
            } catch (RequestError requestError) {
                JOptionPane.showMessageDialog(null, "请求异常请稍后再试",
                        "fail", JOptionPane.INFORMATION_MESSAGE);
                requestError.printStackTrace();
            }

            // 开启下载
            if (GlobalVariable.downloadUtilThread == null) {
                GlobalVariable.downloadUtilThread = new Thread(() -> {
                    while (true) {
                        System.out.println("队列下载数 : " + GlobalVariable.downloadVideoInfoQueue.size());
                        if (!GlobalVariable.downloadVideoInfoQueue.isEmpty() && GlobalVariable.flagDownload == 0) {
                            // 设置为未完成状态
                            GlobalVariable.isComplete = false;
                            // 清空之前下载byte的数据
                            GlobalVariable.completeByte = 0;
                            // 下载视频
                            GlobalVariable.curVideoInfo = GlobalVariable.downloadVideoInfoQueue.poll();
                            // 初始化下载标志
                            GlobalVariable.flagDownload += GlobalVariable.threadNum;

                            String saveFile = GlobalVariable.selectSavePath + "\\" + GlobalVariable.curVideoInfo.getTitle();
                            if (GlobalVariable.qn == 6 || GlobalVariable.qn == 16) saveFile += ".mp4";
                            else saveFile += ".flv";

                            Integer preSize = GlobalVariable.curVideoInfo.getSize() / GlobalVariable.threadNum;

                            for (int i = 0; i < GlobalVariable.threadNum; i++) {
                                int start = i * preSize;
                                int end = (i + 1) * preSize;
                                if (end > GlobalVariable.curVideoInfo.getSize())
                                    end = GlobalVariable.curVideoInfo.getSize();
                                DownloadThread downloadThread = new DownloadThread(start, end, i, GlobalVariable.curVideoInfo.getUrl(), saveFile);
                                new Thread(downloadThread).start();
                            }
                        }
                    }
                });
                GlobalVariable.downloadUtilThread.start();
            }

            // 开始日志
            if (GlobalVariable.logThread == null) {
                //启动一个新的线程用于更新下载进度
                GlobalVariable.logThread = new Thread(() -> {
                    while (true) {
                        // 当视频未下载完成时
                        while (GlobalVariable.flagDownload != 0) {
                            String logText = logJTextArea.getText() + "\r\n" + GlobalVariable.curVideoInfo.getTitle() + " : " + GlobalVariable.getLoading();
                            logJTextArea.setText(logText);
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }
                        if (GlobalVariable.isComplete) {
                            logJTextArea.setText(GlobalVariable.curVideoInfo.getTitle() + ",下载完成!");
                            GlobalVariable.isComplete = false;
                        }
                    }
                });
                GlobalVariable.logThread.start();
            }

        });

        GridBagConstraints s = new GridBagConstraints();
        s.fill = GridBagConstraints.BOTH;

        s.gridwidth = 1;
        s.weightx = 1;
        s.weighty = 0;
        s.insets = new Insets(0, 0, 0, 25);

        layout.setConstraints(selectDirJButton, s);

        layout.setConstraints(feedJButton, s);

        layout.setConstraints(searchJButton, s);

        layout.setConstraints(loginButton, s);

        s.insets = new Insets(0, 0, 0, 0);
        layout.setConstraints(downloadButton, s);
    }

    /**
     * 功能描述:   初始化logJPanel
     * 创建时间:  2021/4/12 11:11
     *
     * @return void
     * @author zengy
     */
    public void initLogJPanel() {
        logJPanel = new JPanel();
        logJPanel.setPreferredSize(new Dimension(70, 35));
        logJPanel.setBackground(Color.WHITE);

        GridBagLayout layout = new GridBagLayout();
        logJPanel.setLayout(layout);

        // 创建日志组件
        logJScrollPane = new JScrollPane();

        logJTextArea = new JTextArea("");
        logJTextArea.setEditable(false);

        logJScrollPane.setViewportView(logJTextArea);
        logJScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        logJScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        logJPanel.add(logJScrollPane);

        GridBagConstraints s = new GridBagConstraints();
        s.fill = GridBagConstraints.BOTH;
        s.gridwidth = 0;
        s.weightx = 1;
        s.weighty = 1;
        layout.setConstraints(logJScrollPane, s);
    }

    /**
     * 功能描述:  创建BiliBili下载工具主窗口
     * 创建时间:  2021/4/11 17:09
     *
     * @return void
     * @author zengy
     */
    public void initDownloadJPanel() {
        downloadJPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        downloadJPanel.setLayout(layout);

        // 设置边距
        downloadJPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        downloadJPanel.setBackground(Color.WHITE);

        // 添加JPanel组件
        initBvIdJPanel();
        initButtonJPanel();
        initLogJPanel();
        downloadJPanel.add(bvIdJPanel);
        downloadJPanel.add(buttonJPanel);
        downloadJPanel.add(logJPanel);

        // 设置JPanel的布局
        GridBagConstraints s = new GridBagConstraints(); //定义一个GridBagConstraints
        s.fill = GridBagConstraints.BOTH;

        s.gridwidth = GridBagConstraints.REMAINDER;
        s.gridheight = 1;
        s.weightx = 1;
        s.weighty = 0;
        s.insets = new Insets(0, 0, 15, 0);
        layout.setConstraints(bvIdJPanel, s);

        s.gridwidth = GridBagConstraints.REMAINDER;
        s.gridheight = 1;
        s.weightx = 1;
        s.weighty = 0;
        s.insets = new Insets(0, 0, 15, 0);
        layout.setConstraints(buttonJPanel, s);

        s.gridwidth = GridBagConstraints.REMAINDER;
        s.gridheight = 4;
        s.weightx = 1;
        s.weighty = 1;
        s.insets = new Insets(0, 0, 0, 0);
        layout.setConstraints(logJPanel, s);
    }

    /**
     * 功能描述:   初始化Frame窗口
     * 创建时间:  2021/4/11 17:12
     *
     * @return void
     * @author zengy
     */
    public void initFrame() {
        downloadJFrame = new JFrame("BiliBili-Download");
        initDownloadJPanel();
        downloadJFrame.add(downloadJPanel);
        downloadJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        downloadJFrame.setLocation(400, 100);
        downloadJFrame.setSize(600, 350);
        downloadJFrame.setMinimumSize(new Dimension(600, 350));
        downloadJFrame.setVisible(true);
    }


    public static void main(String[] args) {
        Download downLog = new Download();
        downLog.initFrame();
    }

}
