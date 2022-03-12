import java.io.*;

public class QuestionsParser {
    private String[] words = new String[7827];

    QuestionsParser() {
        try {
            File file = new File("./src/main/resources/Words.txt");
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
        }

    }

    public String[] createResultWords(int i) {
        String resultRus = words[i].replaceAll("[a-z]","")
                .replaceAll("\\(","")
                .replaceAll("\\)","")
                .replaceAll("[\'\",]","")
                .replaceAll(" ","");
        String resultEn = words[i].replaceAll("[а-я]","")
                .replaceAll("\\(","")
                .replaceAll("\\)","")
                .replaceAll("[\'\",]","")
                .replaceAll(" ","");

        return new String[]{resultEn, resultRus};
    }
}
