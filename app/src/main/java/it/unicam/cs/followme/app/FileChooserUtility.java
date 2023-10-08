package it.unicam.cs.followme.app;

import javafx.stage.FileChooser;

import java.io.File;

/**  This Class offer a methods that can be used to get a file from a directory,
 *  and check if its extension is  .txt
 The logic is encapsulated in this class to promote reusability.
 The use of the method getPropery from the System class is necessary to build the absolute path
 needed for the default initial directory.
 */
public class FileChooserUtility {
    FileChooser chooser;

    public FileChooserUtility(){
        chooser = new FileChooser();
    }

    /** Select and get the file from the given path
     * @param title that appear in the window
     * @param relativePath relativePath of the project **/
    public File chooseFileFromFolder(final String title, final String relativePath) {
        chooser.setTitle(title);
        String currentDir = System.getProperty("user.dir");
        File initialDir = new File(currentDir, relativePath);
        chooser.setInitialDirectory(initialDir);
        return chooser.showOpenDialog(null);
    }

    /** This method check if the extension of the given file is .txt
     * @param file file to check*/
    public boolean checkIfIsTextFile(final File file){
        return file.getName().toLowerCase().endsWith(".txt");
    }

}
