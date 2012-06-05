package ru.goos_gbt;

import javax.swing.table.AbstractTableModel;

/**
 * User: dima
 * Date: 20/03/2012
 */
public class SnipersTableModel extends AbstractTableModel implements SniperListener {

    private static final SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING);
    private static final String[] STATUS_TEXT = {
            "Joining",
            "Bidding",
            "Winning",
            "Lost",
            "Won"
    };
    
    public enum Column {
        ITEM_IDENTIFIER("Item") {
            @Override public Object valueIn(SniperSnapshot snapshot) {
                return snapshot.itemId;
            }
        },
        LAST_PRICE("Last Price") {
            @Override public Object valueIn(SniperSnapshot snapshot) {
                return snapshot.lastPrice;
            }
        },
        LAST_BID("Last Bid") {
            @Override public Object valueIn(SniperSnapshot snapshot) {
                return snapshot.lastBid;
            }
        },
        SNIPER_STATE("State") {
            @Override public Object valueIn(SniperSnapshot snapshot) {
                return textFor(snapshot.state);
            }
        };

        public final String name;

        private Column(String name) {
            this.name = name;
        }

        public static Column at(int offset) {
            return values()[offset];
        }

        public abstract Object valueIn(SniperSnapshot snapshot);
    }

    private SniperSnapshot snapshot = STARTING_UP;

    @Override public int getRowCount() {
        return 1;
    }

    @Override public int getColumnCount() {
        return Column.values().length;
    }

    @Override public Object getValueAt(int rowIndex, int columnIndex) {
        return Column.at(columnIndex).valueIn(snapshot);
    }

    @Override public String getColumnName(int columnIndex) {
        return Column.at(columnIndex).name;
    }

    @Override public void sniperStateChanged(SniperSnapshot newSnapshot) {
        this.snapshot = newSnapshot;
        fireTableRowsUpdated(0, 0);
    }

    private static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }
}
