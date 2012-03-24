package ru.goos_gbt.tests;


import ru.goos_gbt.Main;

import static ru.goos_gbt.tests.FakeAuctionServer.XMPP_HOSTNAME;
import static ru.goos_gbt.MainWindow.*;

/**
 * User: dima
 * Date: 04/03/2012
 */
public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPER_XMPP_ID = SNIPER_ID + "@m4/Auction";

    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer auction) {
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();

        driver = new AuctionSniperDriver(1000);
        driver.showsSniperStatus(STATUS_JOINING);
    }

    public void showSniperHasLostAuction() {
        driver.showsSniperStatus(STATUS_LOST);
    }

    public void hasShownSniperIsBidding() {
        driver.showsSniperStatus(STATUS_BIDDING);
    }

    public void hasShownSniperIsWinning() {
        driver.showsSniperStatus(STATUS_WINNING);
    }

    public void showsSniperHasWonAuction() {
        driver.showsSniperStatus(STATUS_HAS_WON);
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }
}