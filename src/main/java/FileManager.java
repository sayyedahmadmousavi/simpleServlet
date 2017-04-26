import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileManager {

	private String fileType;
	private String srcFile;
	private String fileName;
	private FileInputStream inf;
	private FileOutputStream onf;
	private RandomAccessFile randomAccessFile;

	public FileManager(String srcFile) {

		fileType = ".txt";
		setSrcFile(srcFile);

	}

	void write(byte[] b) {
		try {
			onf = new FileOutputStream(getSrcFile() + "/" + getFileName()
					+ getFileType());
			onf.write(b);
			onf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	String read() {
		String text = "";
		try {
			inf = new FileInputStream(getSrcFile() + "/" + getFileName()
					+ getFileType());

			randomAccessFile = new RandomAccessFile(getSrcFile() + "/"
					+ getFileName() + getFileType(), "rw");

			int d = (int) randomAccessFile.length();
			for (int i = 0; i < d; i++) {
				text += (char) inf.read();
			}

			inf.close();
			randomAccessFile.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;

	}

	public String getFileType() {
		return fileType;
	}

	public String getSrcFile() {
		return srcFile;
	}

	public void setSrcFile(String srcFile) {
		this.srcFile = srcFile;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
