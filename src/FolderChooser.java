import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.awt.*;


public class FolderChooser extends JPanel {
    private JFileChooser chooser;
    private String choosertitle;
    private String result = null;
    private boolean fileChooser = false;

    public FolderChooser(boolean fileChooser) {
        this.fileChooser = fileChooser;
    }

    public void show() {
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle(choosertitle);
        if(!fileChooser)
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        else {
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV FILES", "csv", "csv");
            chooser.setFileFilter(filter);
        }
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