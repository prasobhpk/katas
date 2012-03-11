package ru.goos_gbt;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
* User: dima
* Date: 06/03/2012
*/
public class MainWindow extends JFrame {
    public static final String SNIPER_STATUS_NAME = "Sniper status";
    private static final String STATUS_JOINING = "Joining";

    private final JLabel sniperStatus = createLabel(STATUS_JOINING);

    MainWindow() throws HeadlessException {
        super("Auction sniper");
        setName(Main.MAIN_WINDOW_NAME);
        add(sniperStatus);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private static JLabel createLabel(String initialText) {
        JLabel result = new JLabel(initialText);
        result.setName(SNIPER_STATUS_NAME);
        result.setBorder(new LineBorder(Color.black));
        return result;
    }
}