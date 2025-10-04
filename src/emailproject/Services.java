package emailproject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Services {

    private final String today = LocalDate.now().getDayOfWeek().name();
    private final File jsonFile;
    private final String content;
    public static int i = 1;
    private String space = "";

    public Services(String content, String timetable) {
        this.jsonFile = new File(timetable);
        content = this.makeOperationsFirst(content);
        this.content = content;
    }

    @SuppressWarnings("empty-statement")
    public String messageAnalyzing() {
        String text = this.content.toLowerCase();
        String reply = "Пожалуйста, Попробуйте Снова";
        String getDay = "";
        if (this.today.equalsIgnoreCase("Monday")) {
            getDay = "понедельник";
        } else if (this.today.equalsIgnoreCase("Tuesday")) {
            getDay = "вторник";
        } else if (this.today.equalsIgnoreCase("Wednesday")) {
            getDay = "среда";
        } else if (this.today.equalsIgnoreCase("Thursday")) {
            getDay = "четверг";
        } else if (this.today.equalsIgnoreCase("Friday")) {
            getDay = "пятница";
        }
        if (!text.contains("суббота") && !text.contains("воскресенье")) {
            if (!this.equalsSichas(text.trim()) && !text.trim().equals("понедельник") && !text.trim().equals("вторник") && !text.trim().equals("среда") && !text.trim().equals("четверг") && !text.trim().equals("пятница")) {
                if (text.contains("сейчас") && (!text.contains("понедельник") && !text.contains("вторник") && !text.contains("среда")
                        && !text.contains("четверг") && !text.contains("пятница"))) {
                    reply = this.getDay(getDay);
                } else if ((text.contains("сейчас") && (text.contains("понедельник") || text.contains("вторник") || text.contains("среда")
                        || text.contains("четверг") || text.contains("пятница"))) || (text.contains("понедельник") && (text.contains("сейчас") || text.contains("вторник") || text.contains("среда") || text.contains("четверг")
                        || text.contains("пятница"))) || (text.contains("вторник") && (text.contains("сейчас") || text.contains("понедельник") || text.contains("среда") || text.contains("четверг")
                        || text.contains("пятница"))) || (text.contains("среда") && (text.contains("сейчас") || text.contains("понедельник") || text.contains("вторник") || text.contains("четверг")
                        || text.contains("пятница"))) || (text.contains("четверг") && (text.contains("сейчас") || text.contains("понедельник") || text.contains("вторник") || text.contains("среда")
                        || text.contains("пятница"))) || (text.contains("пятница") && (text.contains("сейчас") || text.contains("понедельник") || text.contains("вторник") || text.contains("среда")
                        || text.contains("четверг")))) {
                    reply = "Пожалуйста, Попробуйте Снова";
                } else if (!text.contains("сейчас") && (text.contains("понедельник") || text.contains("вторник") || text.contains("среда")
                        || text.contains("четверг") || text.contains("пятница"))) {
                    reply = this.getDay();
                } else if (!text.contains("сейчас") && !text.contains("понедельник") && !text.contains("вторник") && !text.contains("среда")
                        && !text.contains("четверг") && !text.contains("пятница")) {
                    if (text.contains("пара") || text.contains("м") || text.contains("пм") || text.contains("ис") || text.contains("иб")) {
                        try {
                            reply = "";
                            reply += "Понедельник:" + "\n" + this.getDay("понедельник") + "\n";
                            reply += "Вторник:" + "\n" + this.getDay("вторник") + "\n";
                            reply += "Среда:" + "\n" + this.getDay("среда") + "\n";
                            reply += "Четверг:" + "\n" + this.getDay("четверг") + "\n";
                            reply += "Пятница:" + "\n" + this.getDay("пятница");
                        } catch (StringIndexOutOfBoundsException ex) {
                            reply = "Пожалуйста, Попробуйте Снова";
                        }
                    }
                }
            } else {
                if (this.equalsSichas(text.trim())) {
                    reply = this.getFullDay(getDay);
                } else if (text.trim().equals("понедельник")) {
                    reply = this.getFullDay("понедельник");
                } else if (text.trim().equals("вторник")) {
                    reply = this.getFullDay("вторник");
                } else if (text.trim().equals("среда")) {
                    reply = this.getFullDay("среда");
                } else if (text.trim().equals("четверг")) {
                    reply = this.getFullDay("четверг");
                } else if (text.trim().equals("пятница")) {
                    reply = this.getFullDay("пятница");
                } else {
                    reply = "Пожалуйста, Попробуйте Снова";
                }
            }
        }
        return reply;
    }

    private String getFullDay(String dayName) {
        String reply = "";
        try {
            JSONParser jsonparser = new JSONParser();
            Object object = jsonparser.parse(new FileReader(this.jsonFile.getAbsolutePath()));
            JSONObject jsonobject = (JSONObject) object;
            JSONObject day = (JSONObject) jsonobject.get(dayName);
            Map info = (Map) day;
            Map sortedMap1 = new TreeMap(info);
            Iterator<Map.Entry> itr = sortedMap1.entrySet().iterator();
            while (itr.hasNext()) {
                try {
                    Map.Entry pair1 = itr.next();
                    reply += pair1.getKey() + " : " + "\n";
                    Object obj1 = jsonparser.parse(pair1.getValue().toString());
                    JSONObject jsonObject1 = (JSONObject) obj1;
                    Map info1 = (Map) jsonObject1;
                    Map sortedMap2 = new TreeMap(info1);
                    Iterator<Map.Entry> itr1 = sortedMap2.entrySet().iterator();
                    while (itr1.hasNext()) {
                        Map.Entry pair2 = itr1.next();
                        reply += pair2.getKey() + " : " + "\n";
                        Object obj2 = jsonparser.parse(pair2.getValue().toString());
                        JSONObject jsonObject2 = (JSONObject) obj2;
                        Map info2 = (Map) jsonObject2;
                        Map sortedMap3 = new TreeMap(info2);
                        Iterator<Map.Entry> itr2 = sortedMap3.entrySet().iterator();
                        while (itr2.hasNext()) {
                            Map.Entry pair3 = itr2.next();
                            reply += pair3.getKey() + " : " + "\n";
                            Object obj3 = jsonparser.parse(pair3.getValue().toString());
                            JSONObject jsonObject3 = (JSONObject) obj3;
                            Map info3 = (Map) jsonObject3;
                            Map sortedMap4 = new TreeMap(info3);
                            Iterator<Map.Entry> itr3 = sortedMap4.entrySet().iterator();
                            while (itr3.hasNext()) {
                                Map.Entry pair4 = itr3.next();
                                reply += pair4.getKey() + " : " + pair4.getValue() + "\n";
                            }
                        }
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException | ParseException ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
        }
        return reply;
    }

    private String getDay() {
        String text = this.content.toLowerCase();
        String reply = "";
        if (text.contains("понедельник")) {
            reply = this.getDay("понедельник");
        } else if (text.contains("вторник")) {
            reply = this.getDay("вторник");
        } else if (text.contains("среда")) {
            reply = this.getDay("среда");
        } else if (text.contains("четверг")) {
            reply = this.getDay("четверг");
        } else if (text.contains("пятница")) {
            reply = this.getDay("пятница");
        } else if (!text.contains("понедельник") && !text.contains("вторник") && !text.contains("среда") && !text.contains("четверг") && !text.contains("пятница")) {
            reply = "Пожалуйста, Попробуйте Снова";
        }
        return reply;
    }

    private String getDay(String dayName) {
        Object object;
        JSONObject jsonobject;
        JSONObject day;
        JSONObject lecture = null;
        JSONObject course = null;
        JSONObject major = null;
        int lectureNumber = 0;
        int courseNumber = 0;
        int subGroupNumber = 0;
        String text = this.content.toLowerCase();
        String reply = "";
        boolean m = false;
        boolean bm = false;
        boolean is = false;
        boolean ib = false;
        boolean twoGroups = false;
        boolean after = false;
        boolean before = false;
        String time;
        String displine;
        String teacher;
        String aud;
        JSONParser jsonparser = new JSONParser();
        try {
            object = jsonparser.parse(new FileReader(this.jsonFile.getAbsolutePath()));
            jsonobject = (JSONObject) object;
            day = (JSONObject) jsonobject.get(dayName);
            if (text.contains("пара")) {
                try {
                    lectureNumber = Integer.parseInt(text.substring(text.indexOf("пара") - 1, text.indexOf("пара")));
                    before = true;
                } catch (NumberFormatException | StringIndexOutOfBoundsException ex1) {
                    try {
                        lectureNumber = Integer.parseInt(text.substring(text.indexOf("пара") - 2, text.indexOf("пара") - 1));
                        try {
                            if ((text.charAt(text.indexOf("пара") - 3) == 'м' || text.charAt(text.indexOf("пара") - 3) == 'с' || text.charAt(text.indexOf("пара") - 3) == 'б'
                                    || text.charAt(text.indexOf("пара") - 4) == 'м' || text.charAt(text.indexOf("пара") - 4) == 'с' || text.charAt(text.indexOf("пара") - 4) == 'б') && this.hasDigit(text.substring(text.indexOf("пара") + 4, text.length()))) {
                                throw new NumberFormatException();
                            }
                            before = true;
                        } catch (StringIndexOutOfBoundsException ex) {
                        }
                    } catch (NumberFormatException | StringIndexOutOfBoundsException ex2) {
                        try {
                            lectureNumber = Integer.parseInt(text.substring(text.indexOf("пара") + 4, text.indexOf("пара") + 5));
                            after = true;
                        } catch (NumberFormatException | StringIndexOutOfBoundsException ex3) {
                            try {
                                lectureNumber = Integer.parseInt(text.substring(text.indexOf("пара") + 5, text.indexOf("пара") + 6));
                                after = true;
                            } catch (NumberFormatException | StringIndexOutOfBoundsException ex4) {
                                reply = "Пожалуйста, Попробуйте Снова";
                            }
                        }
                    }
                }
                try {
                    lecture = (JSONObject) day.get("пара" + String.valueOf(lectureNumber));
                    try {
                        if (this.takeMOnly(text) && !text.contains("ис") && !text.contains("иб") && !text.contains("пм")) {
                            try {
                                courseNumber = Integer.parseInt(text.substring(text.indexOf("м") - 1, text.indexOf("м")));
                                m = true;
                            } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
                                try {
                                    courseNumber = Integer.parseInt(text.substring(text.indexOf("м") - 2, text.indexOf("м") - 1));
                                    m = true;
                                } catch (NumberFormatException | StringIndexOutOfBoundsException ex1) {
                                    reply = "Пожалуйста, Попробуйте Снова";
                                }
                            }
                        } else if (text.contains("пм") && !this.takeMOnly(text) && !text.contains("ис") && !text.contains("иб")) {
                            try {
                                courseNumber = Integer.parseInt(text.substring(text.indexOf("пм") - 1, text.indexOf("пм")));
                                bm = true;
                            } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
                                try {
                                    courseNumber = Integer.parseInt(text.substring(text.indexOf("пм") - 2, text.indexOf("пм") - 1));
                                    bm = true;
                                } catch (NumberFormatException | StringIndexOutOfBoundsException ex1) {
                                    reply = "Пожалуйста, Попробуйте Снова";
                                }
                            }
                        } else if (text.contains("ис") && !this.takeMOnly(text) && !text.contains("пм") && !text.contains("иб")) {
                            try {
                                courseNumber = Integer.parseInt(text.substring(text.indexOf("ис") - 1, text.indexOf("ис")));
                                is = true;
                            } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
                                try {
                                    courseNumber = Integer.parseInt(text.substring(text.indexOf("ис") - 2, text.indexOf("ис") - 1));
                                    is = true;
                                } catch (NumberFormatException | StringIndexOutOfBoundsException ex1) {
                                    reply = "Пожалуйста, Попробуйте Снова";
                                }
                            }
                        } else if (text.contains("иб") && !this.takeMOnly(text) && !text.contains("ис") && !text.contains("пм")) {
                            try {
                                courseNumber = Integer.parseInt(text.substring(text.indexOf("иб") - 1, text.indexOf("иб")));
                                ib = true;
                            } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
                                try {
                                    courseNumber = Integer.parseInt(text.substring(text.indexOf("иб") - 2, text.indexOf("иб") - 1));
                                    ib = true;
                                } catch (NumberFormatException | StringIndexOutOfBoundsException ex1) {
                                    reply = "Пожалуйста, Попробуйте Снова";
                                }
                            }
                        }
                    } catch (StringIndexOutOfBoundsException ex) {
                        reply = "Пожалуйста, Попробуйте Снова";
                    }
                    if ((this.takeMOnly(text) && (text.contains("ис") || text.contains("иб") || text.contains("пм")))
                            || (text.contains("пм") && (this.takeMOnly(text) || text.contains("ис") || text.contains("иб")))
                            || (text.contains("ис") && (this.takeMOnly(text) || text.contains("пм") || text.contains("иб")))
                            || (text.contains("иб") && (this.takeMOnly(text) || text.contains("ис") || text.contains("пм")))) {
                        twoGroups = true;
                    }
                    course = (JSONObject) lecture.get("курс" + String.valueOf(courseNumber));
                    if (m) {
                        if (this.hasSubGroup(text, "м") != 0) {
                            if (text.indexOf(String.valueOf(this.hasSubGroup(text, "м"))) > text.indexOf("пара")
                                    || (text.indexOf(String.valueOf(this.hasSubGroup(text, "м"))) < text.indexOf("пара") && after)) {
                                reply = "Пожалуйста, Попробуйте Снова";
                            } else if (text.indexOf(String.valueOf(this.hasSubGroup(text, "м"))) < text.indexOf("пара") && before) {
                                major = (JSONObject) course.get("М");
                                time = (String) major.get("время");
                                displine = (String) major.get("предмет");
                                teacher = (String) major.get("препод");
                                aud = (String) major.get("номер");
                                reply = "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud;
                            }
                        } else {
                            major = (JSONObject) course.get("М");
                            time = (String) major.get("время");
                            displine = (String) major.get("предмет");
                            teacher = (String) major.get("препод");
                            aud = (String) major.get("номер");
                            reply = "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud;
                        }
                    } else if (bm) {
                        if (courseNumber != 1) {
                            if (this.hasSubGroup(text, "пм") != 0) {
                                if (text.indexOf(String.valueOf(this.hasSubGroup(text, "пм"))) > text.indexOf("пара")
                                        || (text.indexOf(String.valueOf(this.hasSubGroup(text, "пм"))) < text.indexOf("пара") && after)) {
                                    reply = "Пожалуйста, Попробуйте Снова";
                                } else if (text.indexOf(String.valueOf(this.hasSubGroup(text, "пм"))) < text.indexOf("пара") && before) {
                                    major = (JSONObject) course.get("ПМ");
                                    time = (String) major.get("время");
                                    displine = (String) major.get("предмет");
                                    teacher = (String) major.get("препод");
                                    aud = (String) major.get("номер");
                                    reply = "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud;
                                }
                            } else {
                                major = (JSONObject) course.get("ПМ");
                                time = (String) major.get("время");
                                displine = (String) major.get("предмет");
                                teacher = (String) major.get("препод");
                                aud = (String) major.get("номер");
                                reply = "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud;
                            }
                        } else {
                            try {
                                subGroupNumber = Integer.parseInt(text.substring(text.indexOf("пм") + 2, text.indexOf("пм") + 3));
                                if (text.indexOf(String.valueOf(this.hasSubGroup(text, "пм"))) < text.indexOf("пара") && before) {
                                    throw new NumberFormatException();
                                }
                            } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
                                try {
                                    subGroupNumber = Integer.parseInt(text.substring(text.indexOf("пм") + 3, text.indexOf("пм") + 4));
                                    if (text.indexOf(String.valueOf(this.hasSubGroup(text, "пм"))) < text.indexOf("пара") && before) {
                                        throw new NumberFormatException();
                                    }
                                } catch (NumberFormatException | StringIndexOutOfBoundsException ex1) {
                                    reply = String.valueOf(courseNumber) + "ПМ1:" + "\n";
                                    major = (JSONObject) course.get("ПМ1");
                                    time = (String) major.get("время");
                                    displine = (String) major.get("предмет");
                                    teacher = (String) major.get("препод");
                                    aud = (String) major.get("номер");
                                    reply += "\n" + "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud + "\n";
                                    reply += String.valueOf(courseNumber) + "ПМ2:" + "\n";
                                    major = (JSONObject) course.get("ПМ2");
                                    time = (String) major.get("время");
                                    displine = (String) major.get("предмет");
                                    teacher = (String) major.get("препод");
                                    aud = (String) major.get("номер");
                                    reply += "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud;
                                    return reply;
                                }
                            }
                            major = (JSONObject) course.get("ПМ" + String.valueOf(subGroupNumber));
                            time = (String) major.get("время");
                            displine = (String) major.get("предмет");
                            teacher = (String) major.get("препод");
                            aud = (String) major.get("номер");
                            reply = "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud;
                        }
                    } else if (is) {
                        if (courseNumber != 1 && courseNumber != 2) {
                            if (this.hasSubGroup(text, "ис") != 0) {
                                if (text.indexOf(String.valueOf(this.hasSubGroup(text, "ис"))) > text.indexOf("пара")
                                        || (text.indexOf(String.valueOf(this.hasSubGroup(text, "ис"))) < text.indexOf("пара") && after)) {
                                    reply = "Пожалуйста, Попробуйте Снова";
                                } else if (text.indexOf(String.valueOf(this.hasSubGroup(text, "ис"))) < text.indexOf("пара") && before) {
                                    major = (JSONObject) course.get("ИС");
                                    time = (String) major.get("время");
                                    displine = (String) major.get("предмет");
                                    teacher = (String) major.get("препод");
                                    aud = (String) major.get("номер");
                                    reply = "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud;
                                }
                            } else {
                                major = (JSONObject) course.get("ИС");
                                time = (String) major.get("время");
                                displine = (String) major.get("предмет");
                                teacher = (String) major.get("препод");
                                aud = (String) major.get("номер");
                                reply = "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud;
                            }
                        } else {
                            try {
                                subGroupNumber = Integer.parseInt(text.substring(text.indexOf("ис") + 2, text.indexOf("ис") + 3));
                                if (text.indexOf(String.valueOf(this.hasSubGroup(text, "ис"))) < text.indexOf("пара") && before) {
                                    throw new NumberFormatException();
                                }
                            } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
                                try {
                                    subGroupNumber = Integer.parseInt(text.substring(text.indexOf("ис") + 3, text.indexOf("ис") + 4));
                                    if (text.indexOf(String.valueOf(this.hasSubGroup(text, "ис"))) < text.indexOf("пара") && before) {
                                        throw new NumberFormatException();
                                    }
                                } catch (NumberFormatException | StringIndexOutOfBoundsException ex1) {
                                    reply = String.valueOf(courseNumber) + "ИС1:" + "\n";
                                    major = (JSONObject) course.get("ИС1");
                                    time = (String) major.get("время");
                                    displine = (String) major.get("предмет");
                                    teacher = (String) major.get("препод");
                                    aud = (String) major.get("номер");
                                    reply += "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud + "\n";
                                    reply += "\n" + String.valueOf(courseNumber) + "ИС2:" + "\n";
                                    major = (JSONObject) course.get("ИС2");
                                    time = (String) major.get("время");
                                    displine = (String) major.get("предмет");
                                    teacher = (String) major.get("препод");
                                    aud = (String) major.get("номер");
                                    reply += "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud;
                                    return reply;
                                }
                            }
                            major = (JSONObject) course.get("ИС" + String.valueOf(subGroupNumber));
                            time = (String) major.get("время");
                            displine = (String) major.get("предмет");
                            teacher = (String) major.get("препод");
                            aud = (String) major.get("номер");
                            reply = "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud;
                        }
                    } else if (ib) {
                        if (this.hasSubGroup(text, "иб") != 0) {
                            if (text.indexOf(String.valueOf(this.hasSubGroup(text, "иб"))) > text.indexOf("пара")
                                    || (text.indexOf(String.valueOf(this.hasSubGroup(text, "иб"))) < text.indexOf("пара") && after)) {
                                reply = "Пожалуйста, Попробуйте Снова";
                            } else if (text.indexOf(String.valueOf(this.hasSubGroup(text, "иб"))) < text.indexOf("пара") && before) {
                                major = (JSONObject) course.get("ИБ");
                                time = (String) major.get("время");
                                displine = (String) major.get("предмет");
                                teacher = (String) major.get("препод");
                                aud = (String) major.get("номер");
                                reply = "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud;
                            }
                        } else {
                            major = (JSONObject) course.get("ИБ");
                            time = (String) major.get("время");
                            displine = (String) major.get("предмет");
                            teacher = (String) major.get("препод");
                            aud = (String) major.get("номер");
                            reply = "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud;
                        }
                    } else if (!m && !bm && !is && !ib && !twoGroups) {
                        JSONObject allCourses = (JSONObject) day.get("пара" + String.valueOf(lectureNumber));
                        Map info = (Map) allCourses;
                        Map sortedMap1 = new TreeMap(info);
                        Iterator<Map.Entry> itr = sortedMap1.entrySet().iterator();
                        while (itr.hasNext()) {
                            try {
                                Map.Entry pair1 = itr.next();
                                reply += pair1.getKey() + " : " + "\n";
                                Object obj1 = jsonparser.parse(pair1.getValue().toString());
                                JSONObject jsonObject1 = (JSONObject) obj1;
                                Map info1 = (Map) jsonObject1;
                                Map sortedMap2 = new TreeMap(info1);
                                Iterator<Map.Entry> itr1 = sortedMap2.entrySet().iterator();
                                while (itr1.hasNext()) {
                                    try {
                                        Map.Entry pair2 = itr1.next();
                                        reply += pair2.getKey() + " : " + "\n";
                                        Object obj2 = jsonparser.parse(pair2.getValue().toString());
                                        JSONObject jsonObject2 = (JSONObject) obj2;
                                        Map info2 = (Map) jsonObject2;
                                        Map sortedMap3 = new TreeMap(info2);
                                        Iterator<Map.Entry> itr2 = sortedMap3.entrySet().iterator();
                                        while (itr2.hasNext()) {
                                            Map.Entry pair3 = itr2.next();
                                            reply += pair3.getKey() + " : " + pair3.getValue() + "\n";
                                        }
                                    } catch (ParseException ex) {
                                        Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            } catch (ParseException ex) {
                                Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else {
                        reply = "Пожалуйста, Попробуйте Снова";
                    }
                } catch (NullPointerException ex) {
                    reply = "Пожалуйста, Попробуйте Снова";
                }
            } else if (!text.contains("пара")) {
                reply = this.getLecture(text, day, lecture, course, major, jsonparser);
            }
        } catch (IOException | ParseException ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reply;
    }

    private String getLecture(String text, JSONObject day, JSONObject lecture, JSONObject course, JSONObject major, JSONParser jsonparser) {
        int courseNumber = 0;
        int subGroupNumber = 0;
        String reply = "";
        boolean m = false;
        boolean bm = false;
        boolean is = false;
        boolean ib = false;
        String time;
        String displine;
        String teacher;
        String aud;
        String sub = "";
        for (int i = 1; i <= 4; i++) {
            reply += "Пара " + String.valueOf(i) + ":" + "\n";
            lecture = (JSONObject) day.get("пара" + String.valueOf(i));
            try {
                if (this.takeMOnly(text) && !text.contains("ис") && !text.contains("иб") && !text.contains("пм")) {
                    try {
                        courseNumber = Integer.parseInt(text.substring(text.indexOf("м") - 1, text.indexOf("м")));
                        m = true;
                    } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
                        try {
                            courseNumber = Integer.parseInt(text.substring(text.indexOf("м") - 2, text.indexOf("м") - 1));
                            m = true;
                        } catch (NumberFormatException | StringIndexOutOfBoundsException ex1) {
                            reply = "Пожалуйста, Попробуйте Снова";
                        }
                    }
                } else if (text.contains("пм") && !this.takeMOnly(text) && !text.contains("ис") && !text.contains("иб")) {
                    try {
                        courseNumber = Integer.parseInt(text.substring(text.indexOf("пм") - 1, text.indexOf("пм")));
                        bm = true;
                    } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
                        try {
                            courseNumber = Integer.parseInt(text.substring(text.indexOf("пм") - 2, text.indexOf("пм") - 1));
                            bm = true;
                        } catch (NumberFormatException | StringIndexOutOfBoundsException ex1) {
                            reply = "Пожалуйста, Попробуйте Снова";
                        }
                    }
                } else if (text.contains("ис") && !this.takeMOnly(text) && !text.contains("пм") && !text.contains("иб")) {
                    try {
                        courseNumber = Integer.parseInt(text.substring(text.indexOf("ис") - 1, text.indexOf("ис")));
                        is = true;
                    } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
                        try {
                            courseNumber = Integer.parseInt(text.substring(text.indexOf("ис") - 2, text.indexOf("ис") - 1));
                            is = true;
                        } catch (NumberFormatException | StringIndexOutOfBoundsException ex1) {
                            reply = "Пожалуйста, Попробуйте Снова";
                        }
                    }
                } else if (text.contains("иб") && !this.takeMOnly(text) && !text.contains("ис") && !text.contains("пм")) {
                    try {
                        courseNumber = Integer.parseInt(text.substring(text.indexOf("иб") - 1, text.indexOf("иб")));
                        ib = true;
                    } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
                        try {
                            courseNumber = Integer.parseInt(text.substring(text.indexOf("иб") - 2, text.indexOf("иб") - 1));
                            ib = true;
                        } catch (NumberFormatException | StringIndexOutOfBoundsException ex1) {
                            reply = "Пожалуйста, Попробуйте Снова";
                        }
                    }
                }
            } catch (StringIndexOutOfBoundsException ex) {
                reply = "Пожалуйста, Попробуйте Снова";
            }
            try {
                course = (JSONObject) lecture.get("курс" + String.valueOf(courseNumber));
                if (m) {
                    if (this.hasSubGroup(text, "м") != 0) {
                        reply = "Пожалуйста, Попробуйте Снова";
                    } else {
                        major = (JSONObject) course.get("М");
                        time = (String) major.get("время");
                        displine = (String) major.get("предмет");
                        teacher = (String) major.get("препод");
                        aud = (String) major.get("номер");
                        reply += "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud + "\n";
                    }
                } else if (bm) {
                    if (courseNumber != 1) {
                        if (this.hasSubGroup(text, "пм") != 0) {
                            reply = "Пожалуйста, Попробуйте Снова";
                        } else {
                            major = (JSONObject) course.get("ПМ");
                            time = (String) major.get("время");
                            displine = (String) major.get("предмет");
                            teacher = (String) major.get("препод");
                            aud = (String) major.get("номер");
                            reply += "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud + "\n";
                        }
                    } else {
                        try {
                            subGroupNumber = Integer.parseInt(text.substring(text.indexOf("пм") + 2, text.indexOf("пм") + 3));
                        } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
                            try {
                                subGroupNumber = Integer.parseInt(text.substring(text.indexOf("пм") + 3, text.indexOf("пм") + 4));
                            } catch (NumberFormatException | StringIndexOutOfBoundsException ex1) {
                                if (i == 1) {
                                    reply = String.valueOf(courseNumber) + "ПМ1:" + "\n" + "Пара " + String.valueOf(i) + ":" + "\n";
                                }
                                major = (JSONObject) course.get("ПМ1");
                                time = (String) major.get("время");
                                displine = (String) major.get("предмет");
                                teacher = (String) major.get("препод");
                                aud = (String) major.get("номер");
                                reply += "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud + "\n";
                                if (i == 1) {
                                    sub += String.valueOf(courseNumber) + "ПМ2:" + "\n";
                                }
                                sub += "Пара " + String.valueOf(i) + ":" + "\n";
                                major = (JSONObject) course.get("ПМ2");
                                time = (String) major.get("время");
                                displine = (String) major.get("предмет");
                                teacher = (String) major.get("препод");
                                aud = (String) major.get("номер");
                                sub += "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud + "\n";
                                if (i == 4) {
                                    sub += "\n";
                                    reply += sub;
                                }
                                continue;
                            }
                        }
                        major = (JSONObject) course.get("ПМ" + String.valueOf(subGroupNumber));
                        time = (String) major.get("время");
                        displine = (String) major.get("предмет");
                        teacher = (String) major.get("препод");
                        aud = (String) major.get("номер");
                        reply += "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud + "\n";
                    }
                } else if (is) {
                    if (courseNumber != 1 && courseNumber != 2) {
                        if (this.hasSubGroup(text, "ис") != 0) {
                            reply = "Пожалуйста, Попробуйте Снова";
                        } else {
                            major = (JSONObject) course.get("ИС");
                            time = (String) major.get("время");
                            displine = (String) major.get("предмет");
                            teacher = (String) major.get("препод");
                            aud = (String) major.get("номер");
                            reply += "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud + "\n";
                        }
                    } else {
                        try {
                            subGroupNumber = Integer.parseInt(text.substring(text.indexOf("ис") + 2, text.indexOf("ис") + 3));
                        } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
                            try {
                                subGroupNumber = Integer.parseInt(text.substring(text.indexOf("ис") + 3, text.indexOf("ис") + 4));
                            } catch (NumberFormatException | StringIndexOutOfBoundsException ex1) {
                                if (i == 1) {
                                    reply = String.valueOf(courseNumber) + "ИС1:" + "\n" + "Пара " + String.valueOf(i) + ":" + "\n";
                                }
                                major = (JSONObject) course.get("ИС1");
                                time = (String) major.get("время");
                                displine = (String) major.get("предмет");
                                teacher = (String) major.get("препод");
                                aud = (String) major.get("номер");
                                reply += "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud + "\n";
                                if (i == 1) {
                                    sub += String.valueOf(courseNumber) + "ИС2:" + "\n";
                                }
                                sub += "Пара " + String.valueOf(i) + ":" + "\n";
                                major = (JSONObject) course.get("ИС2");
                                time = (String) major.get("время");
                                displine = (String) major.get("предмет");
                                teacher = (String) major.get("препод");
                                aud = (String) major.get("номер");
                                sub += "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud + "\n";
                                if (i == 4) {
                                    sub += "\n";
                                    reply += sub;
                                }
                                continue;
                            }
                        }
                        major = (JSONObject) course.get("ИС" + String.valueOf(subGroupNumber));
                        time = (String) major.get("время");
                        displine = (String) major.get("предмет");
                        teacher = (String) major.get("препод");
                        aud = (String) major.get("номер");
                        reply += "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud + "\n";
                    }
                } else if (ib) {
                    if (this.hasSubGroup(text, "иб") != 0) {
                        reply = "Пожалуйста, Попробуйте Снова";
                    } else {
                        major = (JSONObject) course.get("ИБ");
                        time = (String) major.get("время");
                        displine = (String) major.get("предмет");
                        teacher = (String) major.get("препод");
                        aud = (String) major.get("номер");
                        reply += "Время: " + time + "\n" + "Предмет: " + displine + "\n" + "Преподаватель: " + teacher + "\n" + "Номер Аудитории/Место: " + aud + "\n";
                    }
                } else {
                    reply = "Пожалуйста, Попробуйте Снова";
                }
            } catch (NullPointerException ex) {
                reply = "Пожалуйста, Попробуйте Снова";
            }
        }
        return reply;
    }

    private boolean equalsSichas(String text) {
        char[] sichas = {'с', 'е', 'й', 'ч', 'а', 'с'};
        try {
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) != sichas[i]) {
                    return false;
                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            return false;
        }
        return true;
    }

    private int hasSubGroup(String text, String group) {
        int subGroupNumber = 0;
        try {
            subGroupNumber = Integer.parseInt(text.substring(text.indexOf(group) + group.length(), text.indexOf(group) + group.length() + 1));
        } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
            try {
                subGroupNumber = Integer.parseInt(text.substring(text.indexOf(group) + group.length() + 1, text.indexOf(group) + group.length() + 2));
            } catch (NumberFormatException | StringIndexOutOfBoundsException ex1) {
                subGroupNumber = 0;
            }
        }
        return subGroupNumber;
    }

    private boolean takeMOnly(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == 'м') {
                if (text.charAt(i - 1) != 'п') {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasDigit(String subText) {
        for (int i = 0; i < subText.length(); i++) {
            if (Character.isDigit(subText.charAt(i))) {
                return true;
            }
        }
        return false;
    }
    
    private String makeOperationsFirst(String content) {
        content = content.toLowerCase();
        String tomorrow;
        String getTomorrow = "";
        if (content.contains("сегодня")) {
            content = content.replace("сегодня", "сейчас");
        }
        if (content.contains("завтра")) {
            System.out.println(content);
            tomorrow = LocalDate.now().plusDays(1).getDayOfWeek().name();
            if (tomorrow.equalsIgnoreCase("Monday")) {
                getTomorrow = "понедельник";
            } else if (tomorrow.equalsIgnoreCase("Tuesday")) {
                getTomorrow = "вторник";
            } else if (tomorrow.equalsIgnoreCase("Wednesday")) {
                getTomorrow = "среда";
            } else if (tomorrow.equalsIgnoreCase("Thursday")) {
                getTomorrow = "четверг";
            } else if (tomorrow.equalsIgnoreCase("Friday")) {
                getTomorrow = "пятница";
            } else if (tomorrow.equalsIgnoreCase("Saturday")) {
                getTomorrow = "суббота";
            } else if (tomorrow.equalsIgnoreCase("Sunday")) {
                getTomorrow = "воскресенье";
            }
            content = content.replace("завтра", getTomorrow);
        }
        if (content.contains("вчера")) {
            tomorrow = LocalDate.now().minusDays(1).getDayOfWeek().name();
            if (tomorrow.equalsIgnoreCase("Monday")) {
                getTomorrow = "понедельник";
            } else if (tomorrow.equalsIgnoreCase("Tuesday")) {
                getTomorrow = "вторник";
            } else if (tomorrow.equalsIgnoreCase("Wednesday")) {
                getTomorrow = "среда";
            } else if (tomorrow.equalsIgnoreCase("Thursday")) {
                getTomorrow = "четверг";
            } else if (tomorrow.equalsIgnoreCase("Friday")) {
                getTomorrow = "пятница";
            } else if (tomorrow.equalsIgnoreCase("Saturday")) {
                getTomorrow = "суббота";
            } else if (tomorrow.equalsIgnoreCase("Sunday")) {
                getTomorrow = "воскресенье";
            }
            content = content.replace("вчера", getTomorrow);
        }
        if (content.contains("январ") || content.contains("феврал") || content.contains("март")
                || content.contains("апрел") || content.contains("мая") || content.contains("июн")
                || content.contains("сентябр") || content.contains("октябр") || content.contains("ноябр")
                || content.contains("декабр")) {
            int numberOfDay, numberOfMonth = 0;
            String getDay, getMonth = "";
            if (content.contains("январ")) {
                numberOfMonth = 1;
                getMonth = "января";
            } else if (content.contains("феврал")) {
                numberOfMonth = 2;
                getMonth = "февраля";
            } else if (content.contains("март")) {
                numberOfMonth = 3;
                getMonth = "марта";
            } else if (content.contains("апрел")) {
                numberOfMonth = 4;
                getMonth = "апреля";
            } else if (content.contains("мая")) {
                numberOfMonth = 5;
                getMonth = "мая";
            } else if (content.contains("июн")) {
                numberOfMonth = 6;
                getMonth = "июня";
            } else if (content.contains("июл")) {
                numberOfMonth = 7;
                getMonth = "июля";
            } else if (content.contains("август")) {
                numberOfMonth = 8;
                getMonth = "август";
            } else if (content.contains("сентябр")) {
                numberOfMonth = 9;
                getMonth = "сентября";
            } else if (content.contains("октябр")) {
                numberOfMonth = 10;
                getMonth = "октября";
            } else if (content.contains("ноябр")) {
                numberOfMonth = 11;
                getMonth = "ноября";
            } else if (content.contains("декабр")) {
                numberOfMonth = 12;
                getMonth = "декабря";
            } else {
                content = "ошибка";
            }
            numberOfDay = this.getDayOfMonth(content, getMonth);
            getDay = LocalDate.of(LocalDate.now().getYear(), numberOfMonth, numberOfDay).getDayOfWeek().name();
            if (getDay.equalsIgnoreCase("Monday")) {
                getDay = "понедельник";
            } else if (getDay.equalsIgnoreCase("Tuesday")) {
                getDay = "вторник";
            } else if (getDay.equalsIgnoreCase("Wednesday")) {
                getDay = "среда";
            } else if (getDay.equalsIgnoreCase("Thursday")) {
                getDay = "четверг";
            } else if (getDay.equalsIgnoreCase("Friday")) {
                getDay = "пятница";
            } else if (getDay.equalsIgnoreCase("Saturday")) {
                getDay = "суббота";
            } else if (getDay.equalsIgnoreCase("Sunday")) {
                getDay = "воскресенье";
            }
            content = content.replace(String.valueOf(numberOfDay) + this.space + getMonth, getDay);
        }
        System.out.println(content);
        return content;
    }

    private int getDayOfMonth(String content, String month) {
        int day;
        try {
            day = Integer.parseInt(content.substring(content.indexOf(month) - 3, content.indexOf(month) - 1));
            this.space = " ";
        } catch (NumberFormatException ex) {
            try {
                day = Integer.parseInt(content.substring(content.indexOf(month) - 2, content.indexOf(month) - 1));
                this.space = " ";
            } catch (NumberFormatException ex1) {
                try {
                    day = Integer.parseInt(content.substring(content.indexOf(month) - 2, content.indexOf(month)));
                } catch (NumberFormatException ex2) {
                    try {
                        day = Integer.parseInt(content.substring(content.indexOf(month) - 1, content.indexOf(month)));
                    } catch (NumberFormatException ex3) {
                        day = 0;
                    }
                }
            }
        }
        return day;
    }
}
