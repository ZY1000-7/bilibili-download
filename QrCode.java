import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * 二维码扫描界面
 */
public class QrCode {
    private JFrame qrCodeFrame;
    private JPanel qrCodePanel;
    private JLabel qrCodeImgJLabel;
    private JLabel qrCodeInfoJLabel;
    private JButton qrCodeRefreshJButton;
    private JButton qrCodeOkJButton;
    private String PATH = "";


    /**
     * 功能描述:   初始化qrCodeFrame
     * 创建时间:  2021/4/12 11:26
     *
     * @return void
     * @author zengy
     */
    public void initQrCodeFrame() {
        qrCodeFrame = new JFrame("QRCode");
        qrCodeFrame.setSize(300, 420);
        qrCodeFrame.setLocation(100, 70);
        qrCodeFrame.setMinimumSize(new Dimension(300, 350));
        initQrCodePanel();
        qrCodeFrame.add(qrCodePanel);
        qrCodeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        qrCodeFrame.setVisible(true);
    }

    /**
     * 功能描述:   初始化qrCodePanel面板
     * 创建时间:  2021/4/12 15:48
     *
     * @return void
     * @author zengy
     */
    public void initQrCodePanel() {
        qrCodePanel = new JPanel();
        qrCodePanel.setBackground(Color.WHITE);
        // 设置边距
        qrCodePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagLayout layout = new GridBagLayout();
        qrCodePanel.setLayout(layout);

        // 创建组件
        qrCodeImgJLabel = new JLabel(new ImageIcon(GlobalVariable.PATH), SwingConstants.CENTER);
        qrCodeImgJLabel.setVerticalAlignment(SwingConstants.CENTER);
        qrCodeInfoJLabel = new JLabel("每 180 S 需刷新一次", SwingConstants.CENTER);

        qrCodeRefreshJButton = new JButton("刷新");
        qrCodeRefreshJButton.setPreferredSize(new Dimension(110, 30));
        qrCodeRefreshJButton.setBackground(Color.WHITE);
        qrCodeRefreshJButton.setBorder(new RoundedBorder(10));
        qrCodeRefreshJButton.addActionListener(e -> {
            try {
                BiliBiliApiRequest.getOAuthKeyAndUrl();
            } catch (RequestError requestError) {
                JOptionPane.showMessageDialog(null, requestError.getMessage(),
                        "fail", JOptionPane.INFORMATION_MESSAGE);
                requestError.printStackTrace();
            }
            QrCodeUtils.createQrCode();
            try {
                qrCodeImgJLabel.setIcon(new ImageIcon(ImageIO.read(new File(GlobalVariable.PATH))));
                qrCodeImgJLabel.repaint();
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(null,
                        "出现未知错误请重试",
                        "fail", JOptionPane.INFORMATION_MESSAGE);
                ioException.printStackTrace();
            }
        });

        qrCodeOkJButton = new JButton("确定");
        qrCodeOkJButton.setPreferredSize(new Dimension(110, 30));
        qrCodeOkJButton.setBackground(Color.WHITE);
        qrCodeOkJButton.setBorder(new RoundedBorder(10));
        qrCodeOkJButton.addActionListener(e -> {
            try {
                BiliBiliApiRequest.getLoginByScanQrCode();
            } catch (RequestError requestError) {
                JOptionPane.showMessageDialog(null, requestError.getMessage(),
                        "fail", JOptionPane.INFORMATION_MESSAGE);
                requestError.printStackTrace();
            }
        });


        qrCodePanel.add(qrCodeImgJLabel);
        qrCodePanel.add(qrCodeInfoJLabel);
        qrCodePanel.add(qrCodeRefreshJButton);
        qrCodePanel.add(qrCodeOkJButton);

        // GridBagLayout布局
        GridBagConstraints s = new GridBagConstraints();
        s.fill = GridBagConstraints.BOTH;

        s.gridwidth = GridBagConstraints.REMAINDER;
        s.weighty = 0;
        s.weightx = 0;
        s.gridheight = 20;
        s.insets = new Insets(0, 0, (int) (qrCodeFrame.getHeight() * 0.05), 0);
        layout.setConstraints(qrCodeImgJLabel, s);

        s.gridwidth = GridBagConstraints.REMAINDER;
        s.weighty = 0;
        s.weightx = 0;
        s.gridheight = 2;
        s.insets = new Insets(0, 0, (int) (qrCodeFrame.getHeight() * 0.05), 0);
        layout.setConstraints(qrCodeInfoJLabel, s);

        s.gridwidth = 4;
        s.weighty = 0;
        s.weightx = 0;
        s.gridheight = 3;
        s.insets = new Insets(0, 0, 0, 40);
        layout.setConstraints(qrCodeRefreshJButton, s);


        s.gridwidth = 4;
        s.weighty = 0;
        s.weightx = 0;
        s.gridheight = 3;
        s.insets = new Insets(0, 0, 0, 0);
        layout.setConstraints(qrCodeOkJButton, s);
    }

}
