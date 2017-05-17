package com.javarush.task.task39.task3913;

import com.javarush.task.task39.task3913.query.IPQuery;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogParser implements IPQuery {
    private Path logDir;
    private List<LogObjects> logList;


    //директория с логами (логов может быть несколько, все они должны иметь расширение log)
    public LogParser(Path logDir){
        this.logDir = logDir;
        logList = getLogObjectsList();
    }

    //заполняем список, состоящий из объектов елементов лога
    private List<LogObjects> getLogObjectsList(){
        ArrayList<String> list = new ArrayList<>();
        //формируем список файлов, состоящий только из логов в указанной dir
        File[] files = logDir.toFile().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".log");
            }
        });

        for (File file : files) {
            try {
                list.addAll(Files.readAllLines(file.toPath(), Charset.defaultCharset()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        ArrayList<LogObjects> listLogObject = new ArrayList<>();
        for (String line : list){
            try {
                String[] logElement = line.split("\\t");
                String ip = logElement[0];
                String userName = logElement[1];
                Date date = dateFormat.parse(logElement[2]);
                Event event = Event.valueOf(logElement[3].split(" ")[0]);
                Status status = Status.valueOf(logElement[4]);

                LogObjects log = new LogObjects(ip, userName, date, event, status);


                listLogObject.add(log);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return listLogObject;
    }

    //возвращает список из объектов елементов лога, даты которых входят в диапазон
    private List<LogObjects> includeToInterval(Date after, Date before){
        ArrayList<LogObjects> list = new ArrayList<>();

        for (LogObjects element : logList){
            try {
                if (includeToInterval(element.date, after, before))
                    list.add(element);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    //метод проверяет вхождение даты лога в нужный интервал
    private boolean includeToInterval(Date date, Date after, Date before){
        boolean isInclude = true;

        if (after != null && before !=null){
            isInclude = date.getTime() >= after.getTime() && date.getTime() <= before.getTime();
        }
        else if (after != null && before == null){
            isInclude = date.getTime() >= after.getTime();
        }
        else if (after == null && before != null){
            isInclude = date.getTime() <= before.getTime();
        }

        return isInclude;
    }

    //метод возвращает множество из уникальных IP, подходящих под критерии
    // критерии:
    // --входящие в интервал дат
    // --подходящие по объекту поиска (user, event or status)
    private Set<String> getIPSet(Object fieldLog, Date after, Date before){
        Set<String> setIp = new HashSet<>();

        for (LogObjects element : includeToInterval(after, before)){
            if (isFieldMatch(fieldLog, element))
                setIp.add(element.IP);
        }

        return setIp;
    }

    //метод проверяет подходит ли лог под критерий поиска по объекту (user, event, status)
    private boolean isFieldMatch(Object fieldLog, LogObjects logElement){
        boolean matched = false;

        if (fieldLog == null)
            return true;
        if (fieldLog instanceof String) //for username
            return logElement.username.equals(fieldLog);
        if (fieldLog instanceof Event) //for event
            return logElement.event.equals(fieldLog);
        if (fieldLog instanceof Status) //for status
            return logElement.status.equals(fieldLog);

        return matched;
    }

    //возвращает количество уникальных IP адресов за выбранный период
    @Override
    public int getNumberOfUniqueIPs(Date after, Date before) {
        return getUniqueIPs(after, before).size();
    }

    //возвращает множество, содержащее все не повторяющиеся IP
    @Override
    public Set<String> getUniqueIPs(Date after, Date before) {
        return getIPSet(null, after, before);
    }

    //возвращает IP, с которых работал переданный пользователь.
    @Override
    public Set<String> getIPsForUser(String user, Date after, Date before) {
        return getIPSet(user, after, before);
    }

    //возвращает IP, с которых было произведено переданное событие
    @Override
    public Set<String> getIPsForEvent(Event event, Date after, Date before) {
        return getIPSet(event, after, before);
    }

    //возвращает IP, события с которых закончилось переданным статусом.
    @Override
    public Set<String> getIPsForStatus(Status status, Date after, Date before) {
        return getIPSet(status, after, before);
    }

    //класс в котором хранятся елементы лога
    private class LogObjects{
        String IP;
        String username;
        Date date;
        Event event;
        Status status;

        public LogObjects(String IP, String username, Date date, Event event, Status status) {
            this.IP = IP;
            this.username = username;
            this.date = date;
            this.event = event;
            this.status = status;
        }
    }

}