package com.javarush.task.task39.task3913;

import com.javarush.task.task39.task3913.query.DateQuery;
import com.javarush.task.task39.task3913.query.EventQuery;
import com.javarush.task.task39.task3913.query.IPQuery;
import com.javarush.task.task39.task3913.query.UserQuery;
import sun.rmi.runtime.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static javafx.scene.input.KeyCode.V;

public class LogParser implements IPQuery, UserQuery, DateQuery, EventQuery {
    private Path logDir;

    //директория с логами (логов может быть несколько, все они должны иметь расширение log)
    public LogParser(Path logDir) {
        this.logDir = logDir;
    }

    /*
     * other methods end enums
     */
    private enum PartsLog {
        IP,
        USERNAME,
        DATE,
        EVENT,
        TASTNUMBER,
        STATUS
    }

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

    //метод заполняет список нужными объектами, согласно параметру addPartLog, подходящими под условия (интервал дат и искомый параметр)
    private void setListPartsOfLog(Object findElement, Set listEventResult, Date after, Date before, PartsLog findPartsLog, PartsLog addPartLog){
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) && equalsPartLog(logObjects, findElement, findPartsLog))
                addToListPartOfLogObjects(logObjects, listEventResult, addPartLog);
        }
    }

    //метод проверяет наличие в логе искомого параметра
    private boolean equalsPartLog(LogObjects logObjects,Object findElement, PartsLog partsLog){
        boolean isEquals = true;
        if (partsLog == null)
            return isEquals;

        switch (partsLog){
            case IP:
                isEquals = logObjects.getIp().equals(findElement);
                break;
            case USERNAME:
                isEquals = logObjects.getUser().equals(findElement);
                break;
            case DATE:
                Date date = (Date) findElement;
                isEquals = logObjects.getDate().getTime() == date.getTime();
                break;
            case EVENT:
                isEquals = logObjects.getEvent().equals((Event) findElement);
                break;
            case TASTNUMBER:
                isEquals = Integer.parseInt(logObjects.getTaskNumber()) == (int)findElement;
                break;
            case STATUS:
                isEquals = logObjects.getStatus().equals((Status) findElement);
                break;
            default:
                break;
        }

        return isEquals;
    }

    //метод добавляет в список объект лога, согласно указанной части лога
    private void addToListPartOfLogObjects(LogObjects logObjects, Set listEventResult, PartsLog partsLog){
        switch (partsLog){
            case IP:
                listEventResult.add(logObjects.getIp());
                break;
            case USERNAME:
                listEventResult.add(logObjects.getUser());
                break;
            case DATE:
                listEventResult.add(logObjects.getDate());
                break;
            case EVENT:
                listEventResult.add(logObjects.getEvent());
                break;
            case STATUS:
                listEventResult.add(logObjects.getStatus());
                break;
        }
    }

    //метод считает кол-во попыток решения (Solve) (или полного решения (Done)) задачи
    private int countSolveOrDoneTask(int task,  Event event, Date after, Date before){
        int count = 0;
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) &&
                    equalsPartLog(logObjects, event, PartsLog.EVENT) &&
                    Integer.parseInt(logObjects.getTaskNumber()) == task)
                count++;
        }

        return count;
    }

    /*
     * methods of IPQuery
     */
    //возвращает количество уникальных IP адресов за выбранный период
    @Override
    public int getNumberOfUniqueIPs(Date after, Date before) {
        return getUniqueIPs(after, before).size();
    }

    //возвращает множество, содержащее все не повторяющиеся IP
    @Override
    public Set<String> getUniqueIPs(Date after, Date before) {
        Set<String> listIP = new HashSet<>();
        setListPartsOfLog(null, listIP, after, before, null, PartsLog.IP);
        return listIP;
    }

    //возвращает IP, с которых работал переданный пользователь.
    @Override
    public Set<String> getIPsForUser(String user, Date after, Date before) {
        Set<String> listIP = new HashSet<>();
        setListPartsOfLog(user, listIP, after, before, PartsLog.USERNAME, PartsLog.IP);
        return listIP;
    }

    //возвращает IP, с которых было произведено переданное событие
    @Override
    public Set<String> getIPsForEvent(Event event, Date after, Date before) {
        Set<String> listIP = new HashSet<>();
        setListPartsOfLog(event, listIP, after, before, PartsLog.EVENT, PartsLog.IP);
        return listIP;
    }

    //возвращает IP, события с которых закончилось переданным статусом.
    @Override
    public Set<String> getIPsForStatus(Status status, Date after, Date before) {
        Set<String> listIP = new HashSet<>();
        setListPartsOfLog(status, listIP, after, before, PartsLog.STATUS, PartsLog.IP);
        return listIP;
    }


    /*
    * methods of UserQuery
    * */
    //возвращает всех пользователей
    @Override
    public Set<String> getAllUsers() {
        Set<String> listUser = new HashSet<>();
        setListPartsOfLog(null, listUser, null, null, null, PartsLog.USERNAME);
        return listUser;
    }

    //возвращает количество уникальных пользователей
    @Override
    public int getNumberOfUsers(Date after, Date before) {
        Set<String> listUser = new HashSet<>();
        setListPartsOfLog(null, listUser, after, before, null, PartsLog.USERNAME);
        return listUser.size();
    }

    //возвращает количество событий от определенного пользователя.
    @Override
    public int getNumberOfUserEvents(String user, Date after, Date before) {
        Set<String> listUser = new HashSet<>();
        setListPartsOfLog(user, listUser, after, before, PartsLog.USERNAME, PartsLog.EVENT);
        return listUser.size();
    }

    //возвращает пользователей с определенным IP. Несколько пользователей могут использовать один и тот же IP
    @Override
    public Set<String> getUsersForIP(String ip, Date after, Date before) {
        Set<String> listUser = new HashSet<>();
        setListPartsOfLog(ip, listUser, after, before, PartsLog.IP, PartsLog.USERNAME);
        return listUser;
    }

    //возвращает пользователей, которые были залогинены
    @Override
    public Set<String> getLoggedUsers(Date after, Date before) {
        Set<String> listUser = new HashSet<>();
        setListPartsOfLog(Event.LOGIN, listUser, after, before, PartsLog.EVENT, PartsLog.USERNAME);
        return listUser;
    }

    //возвращает пользователей, которые скачали плагин
    @Override
    public Set<String> getDownloadedPluginUsers(Date after, Date before) {
        Set<String> listUser = new HashSet<>();
        setListPartsOfLog(Event.DOWNLOAD_PLUGIN, listUser, after, before, PartsLog.EVENT, PartsLog.USERNAME);
        return listUser;
    }

    //возвращает пользователей, которые отправили сообщение
    @Override
    public Set<String> getWroteMessageUsers(Date after, Date before) {
        Set<String> listUser = new HashSet<>();
        setListPartsOfLog(Event.WRITE_MESSAGE, listUser, after, before, PartsLog.EVENT, PartsLog.USERNAME);
        return listUser;
    }

    //возвращает пользователей, которые решали любую задачу
    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before) {
        Set<String> listUser = new HashSet<>();
        setListPartsOfLog(Event.SOLVE_TASK, listUser, after, before, PartsLog.EVENT, PartsLog.USERNAME);
        return listUser;
    }

    //возвращает пользователей, которые решали задачу с номером task
    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before, int task) {
        Set<String> userList = new HashSet<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) &&
                    Event.SOLVE_TASK.equals(logObjects.getEvent()) &&
                    task == Integer.parseInt(logObjects.getTaskNumber())){
                userList.add(logObjects.getUser());
            }
        }
        return userList;
    }

    //возвращает пользователей, которые решили любую задачу
    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before) {
        Set<String> listUser = new HashSet<>();
        setListPartsOfLog(Event.DONE_TASK, listUser, after, before, PartsLog.EVENT, PartsLog.USERNAME);
        return listUser;
    }

    //возвращает пользователей, которые решили задачу с номером task
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
        setListPartsOfLog(Status.FAILED, listDate, after, before, PartsLog.STATUS, PartsLog.DATE);
        return listDate;
    }

    //возвращает даты, когда любое событие закончилось ошибкой (статус ERROR)
    @Override
    public Set<Date> getDatesWhenErrorHappened(Date after, Date before) {
        Set<Date> listDate = new HashSet<>();
        setListPartsOfLog(Status.ERROR, listDate, after, before, PartsLog.STATUS, PartsLog.DATE);
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


    //возвращает количество событий за указанный период
    @Override
    public int getNumberOfAllEvents(Date after, Date before) {
        return getAllEvents(after, before).size();
    }

    //возвращает все события за указанный период
    @Override
    public Set<Event> getAllEvents(Date after, Date before) {
        Set<Event> listEvent = new HashSet<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate() , after, before))
                listEvent.add(logObjects.getEvent());
        }
        return listEvent;
    }

    //возвращает события, которые происходили с указанного IP
    @Override
    public Set<Event> getEventsForIP(String ip, Date after, Date before) {
        Set<Event> listEvent = new HashSet<>();
        setListPartsOfLog(ip, listEvent, after, before, PartsLog.IP, PartsLog.EVENT);
        return listEvent;
    }

    //возвращает события, которые инициировал определенный пользователь.
    @Override
    public Set<Event> getEventsForUser(String user, Date after, Date before) {
        Set<Event> listEvent = new HashSet<>();
        setListPartsOfLog(user, listEvent, after, before, PartsLog.USERNAME, PartsLog.EVENT);
        return listEvent;
    }

    //возвращает события, которые не выполнились
    @Override
    public Set<Event> getFailedEvents(Date after, Date before) {
        Set<Event> listEvent = new HashSet<>();
        setListPartsOfLog(Status.FAILED, listEvent, after, before, PartsLog.STATUS, PartsLog.EVENT);
        return listEvent;
    }

    //возвращает  события, которые завершились ошибкой
    @Override
    public Set<Event> getErrorEvents(Date after, Date before) {
        Set<Event> listEvent = new HashSet<>();
        setListPartsOfLog(Status.ERROR, listEvent, after, before, PartsLog.STATUS, PartsLog.EVENT);
        return listEvent;
    }

    //возвращает количество попыток решить определенную задачу.
    @Override
    public int getNumberOfAttemptToSolveTask(int task, Date after, Date before) {
        return countSolveOrDoneTask(task, Event.SOLVE_TASK, after, before);
    }

    //возвращает количество успешных решений определенной задачи.
    @Override
    public int getNumberOfSuccessfulAttemptToSolveTask(int task, Date after, Date before) {
        return countSolveOrDoneTask(task, Event.DONE_TASK, after, before);
    }

    //возвращает мапу (номер_задачи : количество_попыток_решить_ее).
    @Override
    public Map<Integer, Integer> getAllSolvedTasksAndTheirNumber(Date after, Date before) {
        Map<Integer, Integer> mapTask= new HashMap<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) &&
                    logObjects.getEvent().equals(Event.SOLVE_TASK)){
                int taskNumber = Integer.parseInt(logObjects.getTaskNumber());
                if (mapTask.containsKey(taskNumber))
                    mapTask.put(taskNumber, mapTask.get(taskNumber) + 1);
                else
                    mapTask.put(taskNumber, 1);
            }
        }

        return mapTask;
    }

    //возвращает  мапу (номер_задачи : сколько_раз_ее_решили).
    @Override
    public Map<Integer, Integer> getAllDoneTasksAndTheirNumber(Date after, Date before) {
        Map<Integer, Integer> mapTask= new HashMap<>();
        for (LogObjects logObjects : getListLogObject()){
            if (isDateInside(logObjects.getDate(), after, before) &&
                    logObjects.getEvent().equals(Event.DONE_TASK)){
                int taskNumber = Integer.parseInt(logObjects.getTaskNumber());
                if (mapTask.containsKey(taskNumber))
                    mapTask.put(taskNumber, mapTask.get(taskNumber) + 1);
                else
                    mapTask.put(taskNumber, 1);
            }
        }

        return mapTask;
    }
}
