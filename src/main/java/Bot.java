import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    private final String TOKEN = "5154928569:AAHrksvlAFGLxWOuokYPDLXMvh81zdEnzaY";
    private final String BOT_NAME = "FIrstJavaTest_bot";
    QuestionsParser questionsParser = new QuestionsParser();
    ServiceCommands serviceCommands = new ServiceCommands();
    RegistUser registUser = new RegistUser();
    Points points = new Points();
    private boolean firstPermission = false;
    private boolean secondPermission = false;
    private boolean registerPermission = false;
    private String[] words = new String[20];

    private String[] wordNow = new String[100];
    private int count = 0;
    private int i;
    private Message uniqueMessage;



    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        Long chatId = msg.getChatId();
        if(firstPermission){
            sendAnswerModeFirst(chatId);
            boolean coincidence = false;
            for (String s : wordNow) {
                if (msg.getText().equals(s)) {
                    sendAnswer(chatId, "Хорошая работа, продолжаем!");
                    countingCoincidence();
                    checkCounting();
                    sendAnswer(chatId, points.givePoint(chatId));
                    coincidence = true;
                    if(words.length==0) {
                        giveWords(chatId);
                    }
                }
                if(!coincidence) {
                    sendAnswer(chatId, "Неудачная попытка, попробуй снова!");
                    String answer = "";
                    for(int j = 0; j < wordNow.length; j++) {
                        answer += wordNow[j] + " или";
                    }
                    sendAnswer(chatId,"Верный ответ: " + answer.substring(0, answer.length()-4));
                }
            }

        }
        if(secondPermission) {
            if(msg.getText().equals(wordNow[0])){
                sendAnswer(chatId,"Хорошая работа, продолжаем!");
                sendAnswer(chatId, points.givePoint(chatId));
                sendAnswerModeSecond(chatId);
                checkCounting();
                if(words.length==0) {
                    giveWords(chatId);
                }
            }
            else if(msg.getText().equals("Готов")) {
                //
            }
            else {
                sendAnswer(chatId,"Неудачная попытка, попробуй снова.");
            }
        }
        if(registerPermission) {
            String userName = msg.getText();
            sendAnswer(chatId,registUser.registerUser(userName,chatId));
            registerPermission = false;

        }
        if(msg.getText().equals("/start")) {
            createStartButtons(chatId);
        }
        else if(msg.getText().equals("Начало")) {
            sendAnswer(chatId,serviceCommands.startCommand());
        }
        else if(msg.getText().equals("Информация")) {
            sendAnswer(chatId,serviceCommands.helpCommand());
        }
        else if(msg.getText().equals("Английский -> Русский")) {
            if(!registUser.checkChatId(chatId)) {
                createReadyButton(chatId);
                giveWords(chatId);
                sendAnswer(chatId,serviceCommands.goFirst());
                firstPermission = true;
                secondPermission = false;
            }
            else {
                sendAnswer(chatId, "Для начала зарегистрируйся /register");
            }
        }
        else if(msg.getText().equals("Русский -> Английский")) {
            if(!registUser.checkChatId(chatId)) {
                giveWords(chatId);
                sendAnswer(chatId,serviceCommands.goSecond());
                sendAnswerModeSecond(chatId);
                secondPermission = true;
                firstPermission = false;
            }
            else {
                sendAnswer(chatId, "Для начала зарегистрируйся /register");
            }
        }
        else if(msg.getText().equals("Готов")) {
            deleteMessage(chatId);
        }
        else if(msg.getText().equals("Регистрация")){
            sendAnswer(chatId,serviceCommands.register());
            registerPermission = true;
        }
    }

    private void sendAnswer(Long chatId, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try{
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void giveWords(long chatId) {
        String[] newWords = new String[20];
        String sms = "";
        for(int i = 0; i < newWords.length; i+=2) {
            String[] resultWords = questionsParser.createResultWords();
            newWords[i] = resultWords[0];
            newWords[i+1] = resultWords[1];
        }
        for(int i = 0; i < newWords.length; i+=2) {
            sms += newWords[i] + " - " + newWords[i+1] + "\n";
        }
        words = newWords;
        sendUniqueAnswer(chatId,sms);
    }

    private void sendUniqueAnswer(Long chatId, String text){
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            uniqueMessage = execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    private void deleteMessage(Long chatId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId.toString());
        deleteMessage.setMessageId(uniqueMessage.getMessageId());
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendAnswerModeFirst(Long chatId) {
        i = (int)(Math.random()*words.length);
        String answer;
        if(i%2 == 0) {
            answer = words[i];
            wordNow = words[i+1].split(",");
        }
        else {
            answer = words[i-1];
            wordNow = words[i].split(",");
        }
        for (int j = 0; j < wordNow.length; j++) {
            wordNow[j] = wordNow[j].replaceAll(" ","");
        }
        System.out.println(wordNow[0]);
        sendAnswer(chatId,answer);

    }

    private void sendAnswerModeSecond(Long chatId) {
        i = (int)(Math.random()*words.length);
        String answer;
        try {
            if(i%2 == 0) {
                answer=words[i+1];
                wordNow[0]=words[i];
            }
            else{
                answer=words[i];
                wordNow[0]=words[i-1];
            }
            sendAnswer(chatId,answer);
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    private void countingCoincidence() {
        count+=1;
    }

    private void checkCounting() {
        if(count == 1) {
            List<String> list = new ArrayList<>(Arrays.asList(words));
            if(i%2==0) {
                list.remove(list.get(i));
                list.remove(list.get(i));
            }
            else {
                list.remove(list.get(i));
                list.remove(list.get(i-1));
            }
            words = list.toArray(new String[list.size()]);
            count = 0;
        }
    }

    private void createStartButtons(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardRowFirst = new KeyboardRow();
        keyboardRowFirst.add("Начало");
        keyboardRowFirst.add("Информация");
        keyboardRowFirst.add("Регистрация");

        KeyboardRow keyboardRowSecond = new KeyboardRow();
        keyboardRowSecond.add("Английский -> Русский");
        keyboardRowSecond.add("Русский -> Английский");

        keyboard.add(keyboardRowFirst);
        keyboard.add(keyboardRowSecond);
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setText("В твоем распоряжении целое меню!");
        sendMessage.setChatId(chatId.toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void createReadyButton(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRowFirst = new KeyboardRow();
        keyboardRowFirst.add("Готов");
        keyboard.add(keyboardRowFirst);
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setText("test");
        sendMessage.setChatId(chatId.toString());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
