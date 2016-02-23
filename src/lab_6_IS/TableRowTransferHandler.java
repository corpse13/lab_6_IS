package lab_6_IS;

import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSource;
import java.util.ArrayList;

import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

//Class to handle data transfer between rows

public class TableRowTransferHandler extends TransferHandler {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final DataFlavor localObjectFlavor = new DataFlavor(Integer.class, "Integer Row Index");
    private JTable table = null;

    public TableRowTransferHandler(JTable table) {
        this.table = table;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        assert (c == table);
        return new DataHandler(new Integer(table.getSelectedRow()), localObjectFlavor.getMimeType());
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport info) {
        boolean b = info.getComponent() == table && info.isDrop() && info.isDataFlavorSupported(localObjectFlavor);
        table.setCursor(b ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);
        return b;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY_OR_MOVE;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport info) {
        JTable target = (JTable) info.getComponent();
        JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
        int index = dl.getRow();
        int max = table.getModel().getRowCount();
        if (index < 0 || index > max) {
            index = max;
        }
        target.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        try {
            Integer rowFrom = (Integer) info.getTransferable().getTransferData(localObjectFlavor);
            if (rowFrom != -1 && rowFrom != index) {

                int[] rows = table.getSelectedRows();
                int dist = 0;
                for (int row : rows) {
                    if (index > row) {
                        dist++;
                    }
                }
                index -= dist;

                ArrayList<Object[]> list = TableUtil.getSelectedList(table);
                if(Main.isShiftPressed() == false)
                	TableUtil.removeSelected(table);
                ArrayList<Integer> sels = new ArrayList<Integer>();
                for (Object[] obj : list) {
                    sels.add(index);
                    TableUtil.addRowAt(table, obj, index++);
                }
                TableUtil.selectMultipleRow(table, sels);


                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void exportDone(JComponent c, Transferable t, int act) {
        if (act == TransferHandler.MOVE) {
            table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
}
