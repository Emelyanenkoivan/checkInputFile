import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class StartCheck {
    public static void main(String[] args) throws IOException {
        // путь до входного файла
        String pathToInputFile = args[0];
        // путь до файла-шаблона для проверки входного файлов
         String pathToTemplateInputFile = args[1];
         // создаём объект файл-шаблон
        FileTemplate fileTemplate = new FileTemplate(pathToTemplateInputFile);
         // создаём объект входные файлы для проверки
        FilesForCheck filesForcheck = new FilesForCheck(pathToInputFile);
                   // если список пустой (работаем с файлом):
        if (filesForcheck.directoryWithInputFiles.size() == 0) {

              // получаем строку из входного файла
            String lineFromInputFile =  filesForcheck.getLineFromInputFile(filesForcheck.pathToInputFile);

           //получаем файл-свойств, соответствующий этому входному файлу
           Properties inputProperties = filesForcheck.getProperties(filesForcheck.inputFile);
              //строка-шаблон в результате замены переменных, взятых из файла-свойств
            String lineAsResultReplace =  filesForcheck.lineForComparison(fileTemplate.lineFromTemplate, inputProperties);


            if (lineFromInputFile.equals(lineAsResultReplace)){

                int index = pathToInputFile.lastIndexOf(".");
                String pathLogFile = pathToInputFile.substring(0, index+1)+"log";
                filesForcheck.writeDataInFile(pathLogFile, "Ошибок во входном файле нет: " + pathToInputFile.substring(pathToInputFile.lastIndexOf("\\")+1, pathToInputFile.length()));

            }

            else{

                int index = pathToInputFile.lastIndexOf(".");
                String pathLogFile = pathToInputFile.substring(0, index+1)+"log";
                filesForcheck.writeDataInFile(pathLogFile,pathToInputFile.substring(pathToInputFile.lastIndexOf("\\")+1, pathToInputFile.length()) + ": этот входной файл некорректный (есть ошибки)");

                String fullNameForBackFile = pathToInputFile.substring(0, index) + "_back." + pathToInputFile.substring(index + 1, pathToInputFile.length());
                File fileBack = new File(fullNameForBackFile);

                   if (fileBack.createNewFile()) {

                       filesForcheck.writeDataInFile(fullNameForBackFile, lineFromInputFile);
                       filesForcheck.writeDataInFile(pathToInputFile, lineAsResultReplace);
                   }

            }


        }
          // если работаем с директорией с входными файлами
        else {
            // список, в который будем записывать в каких входных файлах были ошибки (несоответствие DP)
            List<String> listWithErrorInputFile = new ArrayList<>();

            String path_to_ECP = pathToInputFile + "\\to_ECP";
          // создаём директорию to_ECP через метод mkdir() для того, чтобы затем туда положить корректные файлы для отправки в Китай:
            File directoryForCorrectInputFile = new File(path_to_ECP);
               if (!directoryForCorrectInputFile.exists()) {

                   directoryForCorrectInputFile.mkdir();
               }

               String path_toFailFile = pathToInputFile + "\\fail";
               File infoAboutFailFile = new File(path_toFailFile);

                  if (!infoAboutFailFile.exists()){

                      infoAboutFailFile.mkdir();
                  }

                String pathToLogFile = infoAboutFailFile.getAbsolutePath() + "\\" + "not correct files.log";
                  File fileLogfordirectory = new File(pathToLogFile);
                     if (fileLogfordirectory.createNewFile()){

                     }


              for (File file : filesForcheck.directoryWithInputFiles){
                  String lineFromInputFile =  filesForcheck.getLineFromInputFile(file.getAbsolutePath());

                  if (lineFromInputFile.contains("HEADER DESCRIPTION")) {
                      Properties inputProperty = filesForcheck.getProperties(file);
                      //строка-шаблон в результате замены переменных, взятых из файла-свойств
                      String lineAfterReplaceInTemplate = filesForcheck.lineForComparison(fileTemplate.lineFromTemplate, inputProperty);

                      int indexlocalNameFile = file.getAbsolutePath().lastIndexOf("\\");
                      String localNameFile = file.getAbsolutePath().substring(indexlocalNameFile, file.getAbsolutePath().length());

                      String nameCorrectInputFile = directoryForCorrectInputFile.getAbsolutePath() + localNameFile;

                      File correctFile = new File(nameCorrectInputFile);

                      // создаём пустые файлы для записи
                      if (correctFile.createNewFile()) {


                          if (lineFromInputFile.equals(lineAfterReplaceInTemplate)) {
                              filesForcheck.writeDataInFile(correctFile.getAbsolutePath(), lineFromInputFile);

                          } else {
                              filesForcheck.writeDataInFile(correctFile.getAbsolutePath(), lineAfterReplaceInTemplate);
                              String failInputFile = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\") + 1, file.getAbsolutePath().length());

                              listWithErrorInputFile.add("Was changed input file:" + failInputFile);
                          }

                      }

                  }

                  else{
                      continue;
                  }

            }

            if (!listWithErrorInputFile.isEmpty()){

                      String dataWithfailFile = String.join("\n", listWithErrorInputFile);
                filesForcheck.writeDataInFile(pathToLogFile, dataWithfailFile);

            }

        }
    }
}

