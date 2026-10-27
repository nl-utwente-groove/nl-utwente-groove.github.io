package user;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import nl.utwente.groove.annotation.UserOperation;
import nl.utwente.groove.annotation.UserType;

/** A calendar date, as a user-defined GROOVE type. */
@UserType
public record Date(int day, int month, int year) {
    /** Returns a readable representation of this date. */
    @UserOperation
    public String toString() {
        return "" + day() + " " + MONTHS[month() - 1] + " " + year();
    }

    /** Returns the number of days between this date and today. */
    @UserOperation(indeterminate = true)
    public int daysSince() {
        var cal = Calendar.getInstance();
        cal.set(year(), month() - 1, day());
        long diff = Math.abs(new java.util.Date().getTime() - cal.getTime().getTime());
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    /** Returns the date following this one. */
    @UserOperation
    public Date next() {
        int day = day() + 1, month = month(), year = year();
        if (day > daysInMonth()) {
            day = 1;
            month += 1;
            if (month > 12) {
                month = 1;
                year += 1;
            }
        }
        return new Date(day, month, year);
    }

    /** Returns the number of days in this date's month. Not a user operation. */
    private int daysInMonth() {
        return switch (month()) {
        case 1, 3, 5, 7, 8, 10, 12 -> 31;
        case 2 -> 28 + (isLeapYear(year()) ? 1 : 0);
        default -> 30;
        };
    }

    static private final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    /** Tests whether a given year is a leap year. */
    @UserOperation
    static public boolean isLeapYear(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    /** Returns today's date. */
    @UserOperation(indeterminate = true)
    static public Date today() {
        var today = Calendar.getInstance();
        return new Date(today.get(Calendar.DAY_OF_MONTH), today.get(Calendar.MONTH) + 1,
            today.get(Calendar.YEAR));
    }
}
