package org.example;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MainController {
    private MyTelegramBot myTelegramBot;
   String[] months = {"January" , "February" , "March" , "April", "May",
           "June", "July", "August", "September", "October",
           "November", "December"};

   private HashMap<Long, Profile> userMap = new HashMap<>();
   // private HashMap<Long,UserStep> userStep = new HashMap<>();
   // private HashMap<Long,userStep> userStep = new HashMap<>();

    public MainController(MyTelegramBot myTelegramBot) {
        this.myTelegramBot = myTelegramBot;
    }

    public void handleCallBack(CallbackQuery callbackQuery)
    {
      String text = callbackQuery.getData();
      Message message = callbackQuery.getMessage();
      EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(message.getChatId());
        editMessageText.setMessageId(message.getMessageId());
      String[] arr = text.split("/");

      switch (arr[1])
      {
          case "prev":
              int yearPrev = Integer.parseInt(arr[2]);
              editMessageText.setText("Choose your birth year");
              editMessageText.setReplyMarkup(makeYearKeyboard(yearPrev-16));
              myTelegramBot.send(editMessageText);
              break;
          case "next":
            int yearNext = Integer.parseInt(arr[2]);
            editMessageText.setText("Choose your birth year");
            editMessageText.setReplyMarkup(makeYearKeyboard(yearNext));
              myTelegramBot.send(editMessageText);
              break;
           case "year":
              int year = Integer.parseInt(arr[2]);
              Profile profile = userMap.get(message.getChatId());
              profile.setYear(year);
              profile.setStep(UserStep.MONTH);
              SendMessage sendMessage = new SendMessage();
              sendMessage.setChatId(message.getChatId());
              sendMessage.setText("Choose your birth month");
              sendMessage.setReplyMarkup(makeMonthKeyboard());
              myTelegramBot.send(sendMessage);
              break;
          case"day":
              int day = Integer.parseInt(arr[2]);
              Profile profile1 = userMap.get(message.getChatId());
              profile1.setDay(day);
              profile1.setStep(UserStep.DAY);
              SendMessage sendMessage1 = new SendMessage();
              sendMessage1.setChatId(message.getChatId());
              sendMessage1.setText(UserCalculation(profile1));
              myTelegramBot.send(sendMessage1);


      }

      /*String step = userMap.get(message.getChatId()).getStep().name();
      if(step.equals("YEAR"));
      else if(step.equals(""))
       */
    }
    public void handle(Message message) {
        User user = message.getFrom();
        String text = message.getText();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getId());

        if (text.equals("/start") || text.equals("start")) {
            sendMessage.setText("Ismingni krit bro.");
            userMap.remove(user.getId());
            Profile profile = new Profile(user.getId(), UserStep.NAME);
            userMap.put(user.getId(), profile);
            myTelegramBot.send(sendMessage);
        }else if(userMap.containsKey(message.getChatId())){
            Profile profile = userMap.get(user.getId());
            if (profile.getStep().equals(UserStep.NAME)){
                  profile.setName(text);
                  profile.setStep(UserStep.YEAR);
                  sendMessage.setText("Choose your birth year");
                  sendMessage.setReplyMarkup(makeYearKeyboard(1990));
                    myTelegramBot.send(sendMessage);

            } else if (profile.getStep().equals(UserStep.MONTH)) {
                if(indexOfMonth(text)==-1){
                    sendMessage.setText("wrong month");
                    myTelegramBot.send(sendMessage);
                    return;}
                profile.setMonth(indexOfMonth(text)+1);
                profile.setStep(UserStep.DAY);
                ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
                replyKeyboardRemove.setRemoveKeyboard(true);
                sendMessage.setReplyMarkup(replyKeyboardRemove);
                sendMessage.setText("Removed Replykeyboardmarkup");
                myTelegramBot.send(sendMessage);
                sendMessage.setText("Enter your birth day: ");
                sendMessage.setReplyMarkup(makeDayKeyboard(message));
                myTelegramBot.send(sendMessage);
            }else if(profile.getStep().equals(UserStep.YEAR))  {
                sendMessage.setText("Choose your birth year");
                sendMessage.setReplyMarkup(makeYearKeyboard(1990));
                myTelegramBot.send(sendMessage);
            } else {sendMessage.setText("mavjud emassssss");
                myTelegramBot.send(sendMessage);}



        }else {
            sendMessage.setText("mavjud emas");
            myTelegramBot.send(sendMessage);
        }


   }

   public InlineKeyboardMarkup makeYearKeyboard(int year)
   {

       int initYear = year;
       List<List<InlineKeyboardButton>> rowCollection = new LinkedList<>();
       for(int i = 0; i<4;i++) {
           List<InlineKeyboardButton> row = new LinkedList<>();
           for (int j = 0; j < 4; j++) {
             //  InlineKeyboardButton btn = button(String.valueOf(year),"/year/"+year);
               row.add( button(String.valueOf(year),"/year/"+year));
               year++;
           }

           rowCollection.add(row);

       }

          List<InlineKeyboardButton> lastRow = new LinkedList<>();

         //  InlineKeyboardButton next= button(">>","/year/next");
          // InlineKeyboardButton prev = button("<<","/year/prev");


           lastRow.add(button("<<","/prev/"+initYear));
           lastRow.add(button(">>","/next/"+year));
           rowCollection.add(lastRow);
           InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
           keyboard.setKeyboard(rowCollection);

       return keyboard;
   }

   public ReplyKeyboardMarkup makeMonthKeyboard()
   {
       ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

       int index =0;
       List<KeyboardRow> keyboardRows = new LinkedList<>();
       for (int i = 0; i <3 ; i++) {

           KeyboardRow row = new KeyboardRow();
           for (int j = 0; j < 4; j++) {
               KeyboardButton btn = new KeyboardButton();
               btn.setText(months[index++]);
               row.add(btn);
           }

         keyboardRows.add(row);
       }

       replyKeyboardMarkup.setKeyboard(keyboardRows);
       return replyKeyboardMarkup;
   }

   public InlineKeyboardMarkup makeDayKeyboard(Message message)
   {
       Profile profile = userMap.get(message.getChatId());
       int year = profile.getYear();
       int month = profile.getMonth();
       LocalDate localDate = LocalDate.of(year,month,5);

       int maximumDay = localDate.lengthOfMonth();
       int count = 1;
       List<List<InlineKeyboardButton>> rowCol = new LinkedList<>();
       for (int i = 0; i <5; i++) {
           List<InlineKeyboardButton> row = new LinkedList<>();
           for (int j = 0; j <7; j++) {
               if(count>maximumDay)
               {
                   i=4;
                   break;
               }
               InlineKeyboardButton btn = new InlineKeyboardButton();
               btn.setText(String.valueOf(count));
               btn.setCallbackData("/day/"+count);
               row.add(btn);
               count++;
           }
           rowCol.add(row);
       }

       InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
       keyboard.setKeyboard(rowCol);
       return keyboard;

   }

   public InlineKeyboardButton button(String text, String callBackData)
   {
       InlineKeyboardButton btn = new InlineKeyboardButton();
       btn.setText(text);
       btn.setCallbackData(callBackData);

       return btn;
   }

   public int indexOfMonth(String month)
   {
       int ind=0;
       for (String s: months)
       {
           if(month.equals(s))
               return ind;

          ind++;
       }
       return  -1;
   }

   public String UserCalculation(Profile profile)
   {
       LocalDate localDate1 = LocalDate.now();
       LocalDate localDate2 = LocalDate.of(profile.getYear(), profile.getMonth(), profile.getDay());
       Period period = Period.between(localDate2, localDate1);

      return period.getYears() + " years," + period.getMonths() + " months," + period.getDays() + " days";
   }




}
