package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyTelegramBot extends TelegramLongPollingBot {

    private  MainController mainController;

    public MyTelegramBot()
    {
        this.mainController = new MainController(this);
    }
    @Override
    public String getBotUsername() {
        return "ulmas_gg13_bot";
    }

    @Override
    public String getBotToken() {
        return "5676288448:AAFPk-jee_JT9b65m-DJeQldownZ2cuVK5E";
    }

    @Override
    public void onUpdateReceived(Update update) {


        try {
            if (update.hasMessage())
            {
                Message message = update.getMessage();
                if(message.hasText())
                {
                    mainController.handle(message);
                }else
                {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(message.getChatId());
                    sendMessage.setText("Unknown");
                    send(sendMessage);
                }

            } else if (update.hasCallbackQuery()) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                mainController.handleCallBack(callbackQuery);

            }


        }catch (RuntimeException e)
        {
            e.printStackTrace();
        }
    }

    public void send(SendMessage sms)
    {
        try {
            execute(sms);
        } catch (TelegramApiException e) {
            // throw new RuntimeException(e);
            e.printStackTrace();
        }
    }

    public void send(EditMessageText sms)
    {
        try {
            execute(sms);
        } catch (TelegramApiException e) {
            // throw new RuntimeException(e);
            e.printStackTrace();
        }
    }

    public void send(SendPhoto sendPhoto)
    {
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            // throw new RuntimeException(e);
            e.printStackTrace();
        }
    }
}
