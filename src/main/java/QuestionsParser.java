import java.io.*;

public class QuestionsParser {
    private String[] words = new String[10000];

    QuestionsParser() {
        try {
            File file = new File("./src/main/resources/ResultWords.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            for (int i = 0; i < 7827; i++) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    words[i] = line;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

    }

    public String[] createResultWords() {
        int j = (int)(Math.random()*7827);
        String resultEn = words[j].replaceAll("[а-я]","")
                .replaceAll(",","")
                .replaceAll(" ","")
                .replaceAll(";","");
        String resultRus = words[j].replaceAll("[a-z]","");

        return new String[] {resultEn,resultRus};
    }
}
