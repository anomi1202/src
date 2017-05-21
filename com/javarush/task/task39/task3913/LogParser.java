package com.javarush.task.task39.task3913;

import com.javarush.task.task39.task3913.query.DateQuery;
import com.javarush.task.task39.task3913.query.EventQuery;
import com.javarush.task.task39.task3913.query.IPQuery;
import com.javarush.task.task39.task3913.query.UserQuery;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogParser implements IPQuery, UserQuery, DateQuery, EventQuery {
    private Path logDir;

    //директория с логами (логов может быть несколько, все они должны иметь расширение log)
    public LogParser(Path logDir) {
        this.logDir = logDir;
    }

    /*
     * other methods
     */
    //класс, в полях объектов которого будут храниться параметра логов (ip, userName, date, event and status)
    private class LogObjects {
        private String ip;
        private String user;
        private Date date;
        private Event event;
        private String taskNumber;
        private Status status;

        public LogObjects(String ip, String user, Date date, Event event, Status status) {
            this.ip = ip;
            this.user = user;
            this.date = date;
            this.event = event;
            this.status = status;
        }

        public LogObjects(String logLine) {
            String[] strings = logLine.split("\\t");
            this.ip = strings[0];
            this.user = strings[1];
            SimpleDateFormat dateFormat = new SimpleDateFormat("d.M.yyyy H:m:s");
            try {
                date = dateFormat.parse(strings[2]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String eventAndParam[] = strings[3].split(" ");
            event = Event.valueOf(eventAndParam[0]);
            if (eventAndParam.length > 1)
                taskNumber = eventAndParam[1];
            status = Status.valueOf(strings[4]);
        }

        public String getIp() {
            return ip;
        }

        public String getUser() {
            return user;
        }

        public Date getDate() {
            return date;
        }

        public Event getEvent() {
            return event;
        }

        public String getTaskNumber() {
            return taskNumber;
        }

        public Status getStatus() {
            return status;
        }
    }

    //метод возвращает список, состоящий из объектов LogObjects
    private List<LogObjects> getListLogObject() {
        List<LogObjects> listLog = new ArrayList<>();
        try {
            File[] files = logDir.toFile().listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".log");
                }
            });
            for (File file : files) {
                for (String record : Files.readAllLines(file.toPath(), Charset.defaultCharset())) {
                    listLog.add(new LogObjects(record));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listLog;
    }

    //метод проверяет вхождение даты лога в нужный интервал
    private boolean isDateInside(Date date, Date after, Date before) {
        if (after != null) {
            if (date.getTime() < after.getTime())
                return false;
        }
        if (before != null) {
            if (date.getTime() > before.getTime())
                return false;
        }
        return true;
    }

    /*
     * methods of IPQuery
     */
    //метод проверяет, подходит ли лог под объект поиска (user, event or status)
    private boolean isFieldMatch(Object findLog, LogObjects logElement) {
        boolean isMatched = false;

        if (findLog == null)
            return true;
        if (findLog instanceof String) //for username
            isMatched = logElement.getUser().equals(findLog);
        else if (findLog instanceof Event)  //for event
            isMatched = logElement.getEvent().equals(findLog);
        else if (findLog instanceof Status) //for status
            isMatched = logElement.getStatus().equals(findLog);
        return isMatched;
    }

    //возвращает список уникальных IP подходящих под критерий поиска
    private Set<String> getIpSet(Object findLog, Date after, Date before) {
        Set<String> ipSet = new HashSet<>();
        for (LogObjects logObjects : getListLogObject()) {
            if (isDateInside(logObjects.getDate(), after, before) && isFieldMatch(findLog, logObjects)) {
                ipSet.add(logObjects.getIp());
            }
        }
        return ipSet;
    }

    //возвращает количество уникальных IP адресов за выбранный период
    @Override
    public int getNumberOfUniqueIPs(Date after, Date before) {
        return getUniqueIPs(after, before).size();
    }

    //возвращает множество, содержащее все не повторяющиеся IP
    @Override
    public Set<String> getUniqueIPs(Date after, Date before) {
        return getIpSet(null, after, before);
    }

    //возвращает IP, с которых работал переданный пользователь.
    @Override
    public Set<String> getIPsForUser(String user, Date after, Date before) {
        return getIpSet(user, after, before);
    }

    //возвращает IP, с которых было произведено переданное событие
    @Override
    public Set<String> getIPsForEvent(Event event, Date after, Date before) {
        return getIpSet(event, after, before);
    }

    //возвращает IP, события с которых закончилось переданным статусом.
    @Override
    public Set<String> getIPsForStatus(Status status, Date after, Date before) {
        return getIpSet(status, after, before);
    }

    /*
     * methods of UserQuery
     */
    //возвращает всех пользователей
    @Override
    public Set<String> getAllUsers() {
        Set<String> userList = new HashSet<>();
        for (LogObjects logObjects : getListLogObject()){
            userList.add(logObjects.getUser());
        }
        return userList;
    }

    //возвращает количество уникальных пользователей
    @Override
    public int getNumberOfUsers(Date after, Date before) {
        Set<String> userList = new HashSet<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before))
                userList.add(logObjects.getUser());
        }
        return userList.size();
    }

    //возвращает количество событий от определенного пользователя.
    @Override
    public int getNumberOfUserEvents(String user, Date after, Date before) {
        Set<Event> listEvent = new HashSet<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) &&
                    logObjects.getUser().contains(user) &&
                    logObjects.getEvent() != null) {
                listEvent.add(logObjects.getEvent());
            }
        }
        return listEvent.size();
    }

    //возвращает пользователей с определенным IP. Несколько пользователей могут использовать один и тот же IP
    @Override
    public Set<String> getUsersForIP(String ip, Date after, Date before) {
        Set<String> userList = new HashSet<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) && logObjects.getIp().equals(ip))
                userList.add(logObjects.getUser());
        }
        return userList;
    }

    //возвращает пользователей, которые были залогинены
    @Override
    public Set<String> getLoggedUsers(Date after, Date before) {
        Set<String> userList = new HashSet<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) && logObjects.getEvent().equals(Event.LOGIN))
                userList.add(logObjects.getUser());
        }
        return userList;
    }

    //возвращает пользователей, которые скачали плагин
    @Override
    public Set<String> getDownloadedPluginUsers(Date after, Date before) {
        Set<String> userList = new HashSet<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) && logObjects.getEvent().equals(Event.DOWNLOAD_PLUGIN))
                userList.add(logObjects.getUser());
        }
        return userList;
    }

    //возвращает пользователей, которые отправили сообщение
    @Override
    public Set<String> getWroteMessageUsers(Date after, Date before) {
        Set<String> userList = new HashSet<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) && logObjects.getEvent().equals(Event.WRITE_MESSAGE))
                userList.add(logObjects.getUser());
        }
        return userList;
    }

    //возвращает пользователей, которые решали любую задачу
    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before) {
        Set<String> userList = new HashSet<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) && logObjects.getEvent().equals(Event.SOLVE_TASK))
                userList.add(logObjects.getUser());
        }
        return userList;
    }

    //возвращает пользователей, которые решали задачу с номером task
    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before, int task) {
        Set<String> userList = new HashSet<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) &&
                    logObjects.getEvent().equals(Event.SOLVE_TASK) &&
                    task == Integer.parseInt(logObjects.getTaskNumber())) {
                    userList.add(logObjects.getUser());
            }
        }
        return userList;
    }

    //возвращает пользователей, которые решали любую задачу
    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before) {
        Set<String> userList = new HashSet<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) && logObjects.getEvent().equals(Event.DONE_TASK))
                userList.add(logObjects.getUser());
        }
        return userList;
    }

    //возвращает пользователей, которые решали задачу с номером task
    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before, int task) {
        Set<String> userList = new HashSet<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) &&
                    Event.DONE_TASK.equals(logObjects.getEvent()) &&
                    task == Integer.parseInt(logObjects.getTaskNumber())){
                userList.add(logObjects.getUser());
            }
        }
        return userList;
    }

    /*
     * methods of DateQuery
     */
    //возвращает даты, когда определенный пользователь произвел определенное событие
    @Override
    public Set<Date> getDatesForUserAndEvent(String user, Event event, Date after, Date before) {
        Set<Date> listDate = new HashSet<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) &&
                    logObjects.getUser().equals(user) &&
                    logObjects.getEvent().equals(event)){
                listDate.add(logObjects.getDate());
            }
        }
        return listDate;
    }

    //возвращает даты, когда любое событие не выполнилось (статус FAILED
    @Override
    public Set<Date> getDatesWhenSomethingFailed(Date after, Date before) {
        Set<Date> listDate = new HashSet<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) &&
                    logObjects.getStatus().equals(Status.FAILED)){
                listDate.add(logObjects.getDate());
            }
        }
        return listDate;
    }

    //возвращает даты, когда любое событие закончилось ошибкой (статус ERROR)
    @Override
    public Set<Date> getDatesWhenErrorHappened(Date after, Date before) {
        Set<Date> listDate = new HashSet<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) &&
                    logObjects.getStatus().equals(Status.ERROR)){
                listDate.add(logObjects.getDate());
            }
        }
        return listDate;
    }

    //возвращает дату, когда пользователь залогинился впервые за указанный период. Если такой даты в логах нет — null.
    @Override
    public Date getDateWhenUserLoggedFirstTime(String user, Date after, Date before) {
        Date fistTimeLogginDate = null;
        Set<Date> listSortDate = new TreeSet<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) &&
                    logObjects.getUser().equals(user) &&
                    logObjects.getEvent().equals(Event.LOGIN)){
                listSortDate.add(logObjects.getDate());
            }
        }

        if (listSortDate.size() > 0) {
            for (Date date : listSortDate) {
                return date;
            }
        }

        return fistTimeLogginDate;
    }

    //возвращает дату, когда пользователь впервые попытался решить определенную задачу. Если такой даты в логах нет — null.
    @Override
    public Date getDateWhenUserSolvedTask(String user, int task, Date after, Date before) {
        Date fistTimeLogginDate = null;
        Set<Date> listSortDate = new TreeSet<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) &&
                    logObjects.getUser().equals(user) &&
                    logObjects.getEvent().equals(Event.SOLVE_TASK) &&
                    Integer.parseInt(logObjects.getTaskNumber()) == task){
                listSortDate.add(logObjects.getDate());
            }
        }

        if (listSortDate.size() > 0) {
            for (Date date : listSortDate) {
                return date;
            }
        }

        return fistTimeLogginDate;
    }

    //возвращает дату, когда пользователь впервые решил определенную задачу. Если такой даты в логах нет — null.
    @Override
    public Date getDateWhenUserDoneTask(String user, int task, Date after, Date before) {
        Date fistTimeLogginDate = null;
        Set<Date> listSortDate = new TreeSet<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) &&
                    logObjects.getUser().equals(user) &&
                    logObjects.getEvent().equals(Event.DONE_TASK) &&
                    Integer.parseInt(logObjects.getTaskNumber()) == task){
                listSortDate.add(logObjects.getDate());
            }
        }

        if (listSortDate.size() > 0) {
            for (Date date : listSortDate) {
                return date;
            }
        }

        return fistTimeLogginDate;
    }

    //возвращает даты, когда пользователь написал сообщение.
    @Override
    public Set<Date> getDatesWhenUserWroteMessage(String user, Date after, Date before) {
        Set<Date> listDate = new TreeSet<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) &&
                    logObjects.getUser().equals(user) &&
                    logObjects.getEvent().equals(Event.WRITE_MESSAGE)){
                listDate.add(logObjects.getDate());
            }
        }

        return listDate;
    }

    //возвращает даты, когда пользователь скачал плагин.
    @Override
    public Set<Date> getDatesWhenUserDownloadedPlugin(String user, Date after, Date before) {
        Set<Date> listDate = new TreeSet<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) &&
                    logObjects.getUser().equals(user) &&
                    logObjects.getEvent().equals(Event.DOWNLOAD_PLUGIN)){
                listDate.add(logObjects.getDate());
            }
        }

        return listDate;
    }

    /*
     * methods of EventQuery
     */
    @Override
    public int getNumberOfAllEvents(Date after, Date before) {
        return 0;
    }

    @Override
    public Set<Event> getAllEvents(Date after, Date before) {
        return null;
    }

    @Override
    public Set<Event> getEventsForIP(String ip, Date after, Date before) {
        return null;
    }

    @Override
    public Set<Event> getEventsForUser(String user, Date after, Date before) {
        return null;
    }

    @Override
    public Set<Event> getFailedEvents(Date after, Date before) {
        return null;
    }

    @Override
    public Set<Event> getErrorEvents(Date after, Date before) {
        return null;
    }

    @Override
    public int getNumberOfAttemptToSolveTask(int task, Date after, Date before) {
        return 0;
    }

    @Override
    public int getNumberOfSuccessfulAttemptToSolveTask(int task, Date after, Date before) {
        return 0;
    }

    @Override
    public Map<Integer, Integer> getAllSolvedTasksAndTheirNumber(Date after, Date before) {
        return null;
    }

    @Override
    public Map<Integer, Integer> getAllDoneTasksAndTheirNumber(Date after, Date before) {
        return null;
    }
}
