package emailproject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Services {
    
    private static final Logger LOGGER = Logger.getLogger(ImprovedScheduleService.class.getName());
    private static final String ERROR_MESSAGE = "Пожалуйста, Попробуйте Снова";
    private static final String WEEKEND_MESSAGE = "В выходные занятий нет";
    private static final Map<String, String> ENGLISH_TO_RUSSIAN_DAYS = Map.of(
        "MONDAY", "понедельник",
        "TUESDAY", "вторник",
        "WEDNESDAY", "среда",
        "THURSDAY", "четверг",
        "FRIDAY", "пятница",
        "SATURDAY", "суббота",
        "SUNDAY", "воскресенье"
    );
    private static final Set<String> WEEKDAYS = Set.of(
        "понедельник", "вторник", "среда", "четверг", "пятница"
    );
    private static final Set<String> WEEKEND_DAYS = Set.of("суббота", "воскресенье");
    private static final Set<String> MAJORS = Set.of("м", "пм", "ис", "иб");
    private static final Map<String, Integer> MONTH_MAP = Map.ofEntries(
        Map.entry("январ", 1), Map.entry("феврал", 2), Map.entry("март", 3),
        Map.entry("апрел", 4), Map.entry("мая", 5), Map.entry("июн", 6),
        Map.entry("июл", 7), Map.entry("август", 8), Map.entry("сентябр", 9),
        Map.entry("октябр", 10), Map.entry("ноябр", 11), Map.entry("декабр", 12)
    );
    private final File scheduleFile;
    private final String normalizedQuery;
    private final JSONParser jsonParser;
    
    public Services(String query, String scheduleFilePath) {
        this.scheduleFile = new File(scheduleFilePath);
        this.normalizedQuery = normalizeQuery(query);
        this.jsonParser = new JSONParser();
    }
    
    public String processQuery() {
        try {
            QueryInfo queryInfo = this.parseQuery(this.normalizedQuery);
            return queryInfo.isWeekendQuery() ? WEEKEND_MESSAGE :
                   queryInfo.hasConflictingDays() ? ERROR_MESSAGE :
                   this.generateResponse(queryInfo);
        } catch (InvalidQueryException e) {
            LOGGER.log(Level.WARNING, "Invalid query: " + e.getMessage());
            return ERROR_MESSAGE;
        }
    }
    
    private String normalizeQuery(String query) {
        String normalized = query.toLowerCase().trim();
        normalized = normalized.replace("сегодня", "сейчас");
        normalized = this.handleRelativeDate(normalized, "завтра", 1);
        normalized = this.handleRelativeDate(normalized, "вчера", -1);
        normalized = this.handleSpecificDate(normalized);
        return normalized;
    }
    
    private String handleRelativeDate(String query, String keyword, int daysOffset) {
        return query.contains(keyword) ?
            query.replace(keyword, this.getWeekdayInRussian(LocalDate.now().plusDays(daysOffset))) :
            query;
    }
    
    private String handleSpecificDate(String query) {
        for (Map.Entry<String, Integer> entry : MONTH_MAP.entrySet()) {
            String monthPattern = entry.getKey();
            if (query.contains(monthPattern)) {
                int monthNumber = entry.getValue();
                Integer dayNumber = this.extractDayNumber(query, monthPattern);
                if (dayNumber != null) {
                    try {
                        LocalDate date = LocalDate.of(LocalDate.now().getYear(), monthNumber, dayNumber);
                        String weekday = this.getWeekdayInRussian(date);
                        query = query.replaceFirst("\\d+\\s*" + monthPattern + "\\w*", weekday);
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Invalid date", e);
                    }
                }
            }
        }
        return query;
    }
    
    private Integer extractDayNumber(String query, String monthPattern) {
        Pattern pattern = Pattern.compile("(\\d{1,2})\\s*" + monthPattern);
        Matcher matcher = pattern.matcher(query);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : null;
    }
    
    private String getWeekdayInRussian(LocalDate date) {
        String englishDay = date.getDayOfWeek().name();
        return ENGLISH_TO_RUSSIAN_DAYS.getOrDefault(englishDay, "");
    }
    
    private QueryInfo parseQuery(String query) throws InvalidQueryException {
        QueryInfo info = new QueryInfo();
        info.setDay(this.extractDay(query));
        info.setLectureNumber(this.extractLectureNumber(query));
        this.extractCourseAndMajor(query, info);
        info.setQueryType(
            info.getDay() != null && info.getLectureNumber() == null && 
            info.getCourseNumber() == null && info.getMajor() == null ? QueryType.FULL_DAY :
            info.getLectureNumber() != null || info.getCourseNumber() != null ? QueryType.FILTERED :
            QueryType.ALL_LECTURES
        );
        return info;
    }
    
    private String extractDay(String query) {
        if (query.equals("сейчас") || query.startsWith("сейчас ") && 
            WEEKDAYS.stream().noneMatch(query::contains)) {
            return getCurrentWeekday();
        }
        return WEEKDAYS.stream().filter(query::contains).findFirst()
            .orElse(WEEKEND_DAYS.stream().filter(query::contains).findFirst().orElse(null));
    }
    
    private String getCurrentWeekday() {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        return ENGLISH_TO_RUSSIAN_DAYS.get(today.name());
    }
    
    private Integer extractLectureNumber(String query) {
        Pattern pattern = Pattern.compile("(\\d)\\s*пара|пара\\s*(\\d)");
        Matcher matcher = pattern.matcher(query);
        if (matcher.find()) {
            String num = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
            return Integer.parseInt(num);
        }
        return null;
    }
    
    private void extractCourseAndMajor(String query, QueryInfo info) {
        if (containsOnlyMajor(query, "м") && !query.contains("пм")) {
            info.setMajor("М");
            info.setCourseNumber(extractCourseNumber(query, "м"));
        } else if (query.contains("пм")) {
            info.setMajor("ПМ");
            info.setCourseNumber(extractCourseNumber(query, "пм"));
            info.setSubgroupNumber(extractSubgroupNumber(query, "пм"));
        } else if (query.contains("ис")) {
            info.setMajor("ИС");
            info.setCourseNumber(extractCourseNumber(query, "ис"));
            info.setSubgroupNumber(extractSubgroupNumber(query, "ис"));
        } else if (query.contains("иб")) {
            info.setMajor("ИБ");
            info.setCourseNumber(extractCourseNumber(query, "иб"));
        }
    }
    
    private boolean containsOnlyMajor(String query, String major) {
        Pattern pattern = Pattern.compile("\\d\\s*" + major + "(?!\\p{L})");
        return pattern.matcher(query).find();
    }
    
    private Integer extractCourseNumber(String query, String major) {
        Pattern pattern = Pattern.compile("(\\d)\\s*" + major);
        Matcher matcher = pattern.matcher(query);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : null;
    }
    
    private Integer extractSubgroupNumber(String query, String major) {
        Pattern pattern = Pattern.compile(major + "\\s*(\\d)");
        Matcher matcher = pattern.matcher(query);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : null;
    }
    
    private String generateResponse(QueryInfo queryInfo) {
        try {
            JSONObject schedule = this.loadSchedule();
            return queryInfo.getQueryType() == QueryType.FULL_DAY ? 
                   this.generateFullDaySchedule(schedule, queryInfo.getDay()) :
                   queryInfo.getQueryType() == QueryType.FILTERED ?
                   this.generateFilteredSchedule(schedule, queryInfo) :
                   this.generateAllLectures(schedule, queryInfo);
        } catch (IOException | ParseException e) {
            LOGGER.log(Level.SEVERE, "Error loading schedule", e);
            return ERROR_MESSAGE;
        }
    }
    
    private JSONObject loadSchedule() throws IOException, ParseException {
        try (FileReader reader = new FileReader(this.scheduleFile)) {
            return (JSONObject) this.jsonParser.parse(reader);
        }
    }
    
    private String generateFullDaySchedule(JSONObject schedule, String day) {
        StringBuilder result = new StringBuilder();
        JSONObject daySchedule = (JSONObject) schedule.get(day);
        if (daySchedule == null) {
            return ERROR_MESSAGE;
        }
        Map<String, Object> sortedLectures = new TreeMap<>(daySchedule);
        for (Map.Entry<String, Object> lectureEntry : sortedLectures.entrySet()) {
            result.append(lectureEntry.getKey()).append(":\n");
            this.appendLectureDetails(result, lectureEntry.getValue(), 1);
        }
        return result.toString().trim();
    }
    
    private void appendLectureDetails(StringBuilder sb, Object lectureData, int depth) {
        try {
            JSONObject lectureObj = (JSONObject) this.jsonParser.parse(lectureData.toString());
            Map<String, Object> sortedData = new TreeMap<>(lectureObj);
            for (Map.Entry<String, Object> entry : sortedData.entrySet()) {
                String indent = "  ".repeat(depth);
                sb.append(indent).append(entry.getKey()).append(":\n");
                if (entry.getValue() instanceof String) {
                    sb.append(indent).append("  ").append(entry.getValue()).append("\n");
                } else {
                    this.appendLectureDetails(sb, entry.getValue(), depth + 1);
                }
            }
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, "Error parsing lecture details", e);
        }
    }
    
    private String generateFilteredSchedule(JSONObject schedule, QueryInfo info) {
        try {
            JSONObject daySchedule = (JSONObject) schedule.get(info.getDay());
            return daySchedule == null ? ERROR_MESSAGE :
                   info.getLectureNumber() != null ?
                   this.getSpecificLecture(daySchedule, info) :
                   this.getAllLecturesForCourse(daySchedule, info);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error generating filtered schedule", e);
            return ERROR_MESSAGE;
        }
    }
    
    private String getSpecificLecture(JSONObject daySchedule, QueryInfo info) {
        String lectureKey = "пара" + info.getLectureNumber();
        JSONObject lecture = (JSONObject) daySchedule.get(lectureKey);
        if (lecture == null) return ERROR_MESSAGE;
        if (info.getCourseNumber() == null) {
            StringBuilder result = new StringBuilder();
            this.appendLectureDetails(result, lecture, 0);
            return result.toString().trim();
        }
        String courseKey = "курс" + info.getCourseNumber();
        JSONObject course = (JSONObject) lecture.get(courseKey);
        return course == null ? ERROR_MESSAGE : this.formatCourseInfo(course, info);
    }
    
    private String getAllLecturesForCourse(JSONObject daySchedule, QueryInfo info) {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= 4; i++) {
            String lectureKey = "пара" + i;
            JSONObject lecture = (JSONObject) daySchedule.get(lectureKey);
            if (lecture != null) {
                result.append("Пара ").append(i).append(":\n");
                String courseKey = "курс" + info.getCourseNumber();
                JSONObject course = (JSONObject) lecture.get(courseKey);
                if (course != null) {
                    result.append(formatCourseInfo(course, info)).append("\n");
                }
            }
        }
        return result.toString().trim();
    }
    
    private String formatCourseInfo(JSONObject course, QueryInfo info) {
        try {
            String majorKey = this.determineMajorKey(info);
            JSONObject majorData = (JSONObject) course.get(majorKey);
            return majorData == null ? ERROR_MESSAGE :
                String.format("Время: %s\nПредмет: %s\nПреподаватель: %s\nНомер Аудитории/Место: %s",
                    majorData.get("время"),
                    majorData.get("предмет"),
                    majorData.get("препод"),
                    majorData.get("номер")
                );
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error formatting course info", e);
            return ERROR_MESSAGE;
        }
    }
    
    private String determineMajorKey(QueryInfo info) {
        return info.getSubgroupNumber() != null ?
            info.getMajor() + info.getSubgroupNumber() :
            info.getMajor();
    }
    
    private String generateAllLectures(JSONObject schedule, QueryInfo info) {
        String day = info.getDay() != null ? info.getDay() : this.getCurrentWeekday();
        return this.generateFullDaySchedule(schedule, day);
    }
    
    enum QueryType {
        FULL_DAY,
        FILTERED,
        ALL_LECTURES
    }
    
    static class QueryInfo {
        private String day;
        private Integer lectureNumber;
        private Integer courseNumber;
        private String major;
        private Integer subgroupNumber;
        private QueryType queryType;
        private String normalizedQuery;
        
        public boolean isWeekendQuery() {
            return this.day != null && WEEKEND_DAYS.contains(this.day);
        }
        
        public boolean hasConflictingDays() {
            long dayCount = WEEKDAYS.stream()
                .filter(d -> this.normalizedQuery != null && this.normalizedQuery.contains(d))
                .count();
            return dayCount > 1;
        }
        
        public String getDay() { return this.day; }
        
        public void setDay(String day) { this.day = day; }
        
        public Integer getLectureNumber() { return this.lectureNumber; }
        
        public void setLectureNumber(Integer lectureNumber) { 
            this.lectureNumber = lectureNumber; 
        }
        
        public Integer getCourseNumber() { return this.courseNumber; }
        
        public void setCourseNumber(Integer courseNumber) { 
            this.courseNumber = courseNumber; 
        }
        
        public String getMajor() { return this.major; }
        
        public void setMajor(String major) { this.major = major; }
        
        public Integer getSubgroupNumber() { return this.subgroupNumber; }
        
        public void setSubgroupNumber(Integer subgroupNumber) { 
            this.subgroupNumber = subgroupNumber; 
        }
        
        public QueryType getQueryType() { return this.queryType; }
        
        public void setQueryType(QueryType queryType) { this.queryType = queryType; }
    }
    
    static class InvalidQueryException extends Exception {
        public InvalidQueryException(String message) {
            super(message);
        }
    }
}
