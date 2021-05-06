package valueobject;

import java.util.Arrays;

public class InstrumentFile {

    private String instrumentType;
    private String fileName;
    private Byte[] fileData;

    public InstrumentFile(String instrumentType, String fileName, Byte[] fileData) {
        this.instrumentType = instrumentType;
        this.fileName = fileName;
        this.fileData = fileData;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Byte[] getFileData() {
        return fileData;
    }

    public void setFileData(Byte[] fileData) {
        this.fileData = fileData;
    }

    @Override
    public String toString() {
        return "InstrumentFile{" +
                "instrumentType='" + instrumentType + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileData=" + Arrays.toString(fileData) +
                '}';
    }
}
