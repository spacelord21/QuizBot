import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
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
    private int i;
    private String wordNow;


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
            if(msg.getText().equals(wordNow)){
                sendAnswer(chatId,"Хорошая работа, продолжаем!");
                sendAnswer(chatId, points.givePoint(chatId));
                sendAnswerModeFirst(chatId);
            }
            else {
                sendAnswer(chatId,"Неудачная попытка, попробуй снова.");
            }
        }
        if(secondPermission) {
            if(msg.getText().equals(wordNow)){
                sendAnswer(chatId,"Хорошая работа, продолжаем!");
                sendAnswer(chatId, points.givePoint(chatId));
                sendAnswerModeSecond(chatId);
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
            buttonsAnswer(chatId);
        }
        else if(msg.getText().equals("Начало")) {
            sendAnswer(chatId,serviceCommands.startCommand());
        }
        else if(msg.getText().equals("Информация")) {
            sendAnswer(chatId,serviceCommands.helpCommand());
        }
        else if(msg.getText().equals("Английский -> Русский")) {
            if(!registUser.checkChatId(chatId)) {
                sendAnswer(chatId,serviceCommands.goFirst());
                sendAnswerModeFirst(chatId);
                firstPermission = true;
                secondPermission = false;
            }
            else {
                sendAnswer(chatId, "Для начала зарегистрируйся /register");
            }
        }
        else if(msg.getText().equals("Русский -> Английский")) {
            if(!registUser.checkChatId(chatId)) {
                sendAnswer(chatId,serviceCommands.goSecond());
                sendAnswerModeSecond(chatId);
                secondPermission = true;
                firstPermission = false;
            }
            else {
                sendAnswer(chatId, "Для начала зарегистрируйся /register");
            }
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

    private void buttonsAnswer(Long chatId) {
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

//    private void createRecentWords() {
//        String[] recentWords = new String[10];
//        for(int i = 0; i < recentWords.length; i++) {
//            int j = (int) (Math.random() * 7827);
//
//        }
//    }

    private void sendAnswerModeFirst(Long chatId) {
        i = (int)(Math.random()*2000);
        String[] words = questionsParser.createResultWords(i);
        String resultEng = words[0];
        wordNow = words[1];
        sendAnswer(chatId,resultEng);
        System.out.println(wordNow);
    }

    private void sendAnswerModeSecond(Long chatId) {
        i = (int)(Math.random()*2000);
        String[] words = questionsParser.createResultWords(i);
        String resultRus = words[1];
        wordNow = words[0];
        sendAnswer(chatId,resultRus);
        System.out.println(wordNow);
    }
}
