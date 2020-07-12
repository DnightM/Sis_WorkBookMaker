package ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class UserInterface {
    private JFrame window = new JFrame();
    private String[] inputFiles = null;

    public void run() {
        JPanel jp = new JPanel();
        
        
        window.setVisible(true);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        dragAndDrop();
    }

    private DropTarget dragAndDrop() {
        return new DropTarget(window, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent e) { // 파일 드레그 & 드랍
                if ((e.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
                    String getFirstFilePath = null;
                    e.acceptDrop(e.getDropAction());
                    Transferable Droptr = e.getTransferable();
                    try {
                        getFirstFilePath = Droptr.getTransferData(DataFlavor.javaFileListFlavor).toString();
                    } catch (Exception e1) {
                    }
                    inputFiles = getFirstFilePath.substring(1, getFirstFilePath.length() - 1).split(", ");
                }
            }
        }, true, null);
    }
}
