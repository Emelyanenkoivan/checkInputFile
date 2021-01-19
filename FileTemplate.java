import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileTemplate {

   public String pathToTemplateFile;
   public String lineFromTemplate;

   public FileTemplate() {};

    public FileTemplate(String pathToTemplateFile) {
        this.pathToTemplateFile = pathToTemplateFile;
        this.lineFromTemplate = getLineFromInputFile(pathToTemplateFile);
    }

    // метод считывания всех строк из файла в одну строку через список и разделитель \r\n
    public String getLineFromInputFile(String pathToInputFile){

        List<String> listInput = new ArrayList<>();

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToInputFile))) {
            String line = null;

            while((line= bufferedReader.readLine())!=null){

                listInput.add(line);
            }
            bufferedReader.close();

        }

        catch (IOException e) {
            e.printStackTrace();
        }

        String lineInput = String.join("\r\n", listInput);
        return lineInput+"\r\n";
    }


}
