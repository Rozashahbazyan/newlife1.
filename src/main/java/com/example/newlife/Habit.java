package com.example.newlife;
import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;
import java.util.Collections;
public class Habit implements Parcelable {
    private long id;
    private String name;
    private static int hour;
    private static int minute;
    private List<Boolean> days;
    private boolean isCompleted;
    private List<Boolean> completionHistory;
    private long startDate;
    private List<Long> completionDates;

    // Constructor
    public Habit(String name, int hour, int minute, List<Boolean> days) {
        this.id = System.currentTimeMillis(); // Default ID
        this.name = name;
        this.hour = hour;
        this.minute = minute;
        this.days = days != null ? new ArrayList<>(days) : new ArrayList<>();
        this.isCompleted = false;
        this.completionHistory = new ArrayList<>();
        this.startDate = System.currentTimeMillis();
        this.completionDates = new ArrayList<>();
    }

    // Parcelable Constructor
    protected Habit(Parcel in) {
        id = in.readLong();
        name = in.readString();
        hour = in.readInt();
        minute = in.readInt();
        isCompleted = in.readByte() != 0;
        days = new ArrayList<>();
        in.readList(days, Boolean.class.getClassLoader());
        completionHistory = new ArrayList<>();
        in.readList(completionHistory, Boolean.class.getClassLoader());
        startDate = in.readLong();
        completionDates = new ArrayList<>();
        in.readList(completionDates, Long.class.getClassLoader());
    }
    private int habitDaysDone;

    public int getHabitDaysDone() {
        return habitDaysDone;
    }

    public void setHabitDaysDone(int habitDaysDone) {
        this.habitDaysDone = habitDaysDone;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeByte((byte) (isCompleted ? 1 : 0));
        dest.writeList(days);
        dest.writeList(completionHistory);
        dest.writeLong(startDate);
        dest.writeList(completionDates);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Habit> CREATOR = new Creator<Habit>() {
        @Override
        public Habit createFromParcel(Parcel in) {
            return new Habit(in);
        }

        @Override
        public Habit[] newArray(int size) {
            return new Habit[size];
        }
    };

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public static int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public List<Boolean> getDays() {
        return new ArrayList<>(days); // Return defensive copy
    }

    public void setDays(List<Boolean> days) {
        this.days = days != null ? new ArrayList<>(days) : new ArrayList<>();
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }



    public List<Boolean> getCompletionHistory() {
        return new ArrayList<>(completionHistory); // Return defensive copy
    }

    public void setCompletionHistory(List<Boolean> completionHistory) {
        this.completionHistory = completionHistory != null ? new ArrayList<>(completionHistory) : new ArrayList<>();
    }

    public long getStartDate() {
        return startDate;
    }

    public List<Long> getCompletionDates() {
        return new ArrayList<>(completionDates); // Return defensive copy
    }

    // Utility Methods
    public String getTimeString() {
        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
    }

    public String getDaysString() {
        if (days == null || days.isEmpty()) {
            return "";
        }

        String[] dayNames = DateFormatSymbols.getInstance().getWeekdays();
        List<String> selectedDays = new ArrayList<>();

        for (int i = 0; i < Math.min(days.size(), 7); i++) {
            if (days.get(i) && i + 1 < dayNames.length) {
                selectedDays.add(dayNames[i + 1]);
            }
        }
        return String.join(", ", selectedDays);
    }

    public int getCompletionRate() {
        if (completionHistory == null || completionHistory.isEmpty()) {
            return 0;
        }

        long completedDays = completionHistory.stream().filter(Boolean::booleanValue).count();
        return (int) ((completedDays * 100) / completionHistory.size());
    }

    public int getCurrentStreak() {
        if (completionDates == null || completionDates.isEmpty()) {
            return 0;
        }

        List<Long> sortedDates = new ArrayList<>(completionDates);
        Collections.sort(sortedDates);

        int streak = 0;
        long expectedDate = getStartOfDay(System.currentTimeMillis());
        long oneDayInMillis = 24 * 60 * 60 * 1000;

        for (int i = sortedDates.size() - 1; i >= 0; i--) {
            long date = getStartOfDay(sortedDates.get(i));
            if (date == expectedDate) {
                streak++;
                expectedDate -= oneDayInMillis;
            } else if (date < expectedDate) {
                break;
            }
        }
        return streak;
    }

    private long getStartOfDay(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public void markAsCompletedToday() {
        long today = getStartOfDay(System.currentTimeMillis());
        if (!completionDates.contains(today)) {
            completionDates.add(today);
            completionHistory.add(true);
            isCompleted = true;
        }
    }

    public void markAsNotCompletedToday() {
        long today = getStartOfDay(System.currentTimeMillis());
        int index = completionDates.indexOf(today);
        if (index != -1) {
            completionDates.remove(index);
            completionHistory.remove(index);
        }
        isCompleted = false;
    }

    // JSON Conversion
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", name);
        json.put("hour", hour);
        json.put("minute", minute);
        json.put("isCompleted", isCompleted);
        json.put("startDate", startDate);

        JSONArray daysArray = new JSONArray();
        for (Boolean day : days) {
            daysArray.put(day != null && day);
        }
        json.put("days", daysArray);

        JSONArray historyArray = new JSONArray();
        for (Boolean completed : completionHistory) {
            historyArray.put(completed != null && completed);
        }
        json.put("completionHistory", historyArray);

        JSONArray datesArray = new JSONArray();
        for (Long date : completionDates) {
            datesArray.put(date != null ? date : 0L);
        }
        json.put("completionDates", datesArray);

        return json;
    }

    public static Habit fromJSON(JSONObject json) throws JSONException {
        String name = json.getString("name");
        int hour = json.getInt("hour");
        int minute = json.getInt("minute");

        JSONArray daysArray = json.getJSONArray("days");
        List<Boolean> days = new ArrayList<>();
        for (int i = 0; i < daysArray.length(); i++) {
            days.add(daysArray.optBoolean(i));
        }

        Habit habit = new Habit(name, hour, minute, days);
        habit.id = json.optLong("id", System.currentTimeMillis());
        habit.isCompleted = json.optBoolean("isCompleted", false);
        habit.startDate = json.optLong("startDate", System.currentTimeMillis());

        if (json.has("completionHistory")) {
            JSONArray historyArray = json.getJSONArray("completionHistory");
            for (int i = 0; i < historyArray.length(); i++) {
                habit.completionHistory.add(historyArray.optBoolean(i));
            }
        }

        if (json.has("completionDates")) {
            JSONArray datesArray = json.getJSONArray("completionDates");
            for (int i = 0; i < datesArray.length(); i++) {
                habit.completionDates.add(datesArray.optLong(i));
            }
        }

        return habit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Habit habit = (Habit) o;
        return id == habit.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}