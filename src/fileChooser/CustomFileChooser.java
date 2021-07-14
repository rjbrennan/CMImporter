package fileChooser;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CustomFileChooser extends JFileChooser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String extension;

	public CustomFileChooser(String extension) {
		super();
		this.extension = extension;
		addChoosableFileFilter(
				new FileNameExtensionFilter(String.format("%1$s Images (*.%1$s)", extension), extension));
	}

	@Override
	public File getSelectedFile() {
		File selectedFile = super.getSelectedFile();

		if (selectedFile != null) {
			String name = selectedFile.getName();
			if (!name.contains("."))
				selectedFile = new File(selectedFile.getParentFile(), name + '.' + extension);
		}

		return selectedFile;
	}

	@Override
	public void approveSelection() {
		if (getDialogType() == SAVE_DIALOG) {
			File selectedFile = getSelectedFile();
			if ((selectedFile != null) && selectedFile.exists()) {
				int response = JOptionPane.showConfirmDialog(this,
						"The file " + selectedFile.getName()
								+ " already exists. Do you want to replace the existing file?",
						"Ovewrite file", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (response != JOptionPane.YES_OPTION)
					return;
			}
		}

		super.approveSelection();
	}
}