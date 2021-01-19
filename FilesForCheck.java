import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class FilesForCheck extends FileTemplate{
   public String pathToInputFile;
   public File inputFile;
   public List<File> directoryWithInputFiles;

    public FilesForCheck(String pathToInputFile) {
        this.pathToInputFile = pathToInputFile;
        File file = new File(pathToInputFile);
         if (file.isDirectory()){

              this.directoryWithInputFiles = Arrays.asList(file.listFiles());

         }
         else{

             this.inputFile = file;
             this.directoryWithInputFiles = new ArrayList<>();
         }
    }

    public Properties getProperties(File file){
        FileInputStream fileInputStream = null;
        Properties properties = new Properties();
        try {
            fileInputStream = new FileInputStream(file);

            properties.load(fileInputStream);

        }

        catch (IOException e) {
            e.printStackTrace();
        }
          return properties;

    }

    public String lineForComparison(String line, Properties propertiesInput){
        String[]parameters = line.split("\r\n");
        List<String> listInputFile = new ArrayList<>();
        for (String parametr: parameters) {

           String[] lineForReplace = parametr.split(":");
                 if (lineForReplace.length==2){

                     String parametrAfterReplace =  parametr.replace(lineForReplace[1], propertiesInput.getProperty(lineForReplace[0]));
                     listInputFile.add(parametrAfterReplace);
                 }
                 else{

                     listInputFile.add(parametr);
                 }

            }

            return String.join("\r\n", listInputFile) +"\r\n";
        }



    // метод записи данных в файл (параметр path - путь к файлу, data - записываемые данные)
    public void writeDataInFile(String path, String data) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write(data);
            bw.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
