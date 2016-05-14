import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


public class FolderChooser extends JPanel {
    private JFileChooser chooser;
    private String choosertitle;
    private String result = null;

    public void show() {
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle(choosertitle);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //
        // disable the "All files" option.
        //
        chooser.setAcceptAllFileFilterUsed(false);
        //
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            result = chooser.getSelectedFile().toString();
        }
        Main.cdl.countDown();
    }

    public String getResult() {
        return result;
    }
}