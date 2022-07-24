package com.example.movietrackerv2.ui.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.movietrackerv2.R;
import com.example.movietrackerv2.data.component.MovieEvent;
import com.example.movietrackerv2.data.component.User;
import com.example.movietrackerv2.data.file_storage.converter.ObjectConverter;
import com.example.movietrackerv2.data.file_storage.converter.XmlObjectConverter;
import com.example.movietrackerv2.data.file_storage.locator.FileLocator;
import com.example.movietrackerv2.data.file_storage.locator.UserScheduleListLocator;
import com.example.movietrackerv2.data.file_storage.reader.FileReaderInterface;
import com.example.movietrackerv2.data.file_storage.reader.FileReader;
import com.example.movietrackerv2.data.file_storage.writer.ObjectWriter;
import com.example.movietrackerv2.data.file_storage.writer.XmlObjectWriter;
import com.example.movietrackerv2.data.collection.MovieEventList;
import com.example.movietrackerv2.data.collection.UserScheduleList;
import com.example.movietrackerv2.data.utility.TimeMethodsInterface;
import com.example.movietrackerv2.data.utility.TimeUtility;
import com.thoughtworks.xstream.XStream;

import java.util.ArrayList;
import java.util.List;

// a view model responsible for the logged in user's schedule list information
public class UserScheduleViewModel extends AndroidViewModel implements ScheduleInterface {

    private MutableLiveData<UserScheduleList> mUserScheduleList;
    private MutableLiveData<MovieEventList> mMonthlyEvents;

    private ObjectWriter writer;
    private FileReaderInterface reader;
    private ObjectConverter converter;
    private TimeMethodsInterface timeUtility;

    private Integer previousMonth;

    public UserScheduleViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<UserScheduleList> getUserScheduleList(User loggedInUser) {
        if (mUserScheduleList == null) {
            mUserScheduleList = new MutableLiveData<>();
        }
        loadUserSchedule(loggedInUser);
        return mUserScheduleList;
    }

    // loads the logged in user's schedule from file and stores the result in mUserScheduleList.
    private void loadUserSchedule(User loggedInUser) {
        if (reader == null) {
            reader = new FileReader();
        }
        if (converter == null) {
            converter = new XmlObjectConverter(new XStream());
        }

        FileLocator fileLocator = new UserScheduleListLocator(getApplication().getApplicationContext(), loggedInUser);

        String content = reader.readFormFile(fileLocator);
        UserScheduleList list = (UserScheduleList) converter.convertToObject(content);
        if (list == null) {
            mUserScheduleList.setValue(new UserScheduleList());
        } else {
            mUserScheduleList.setValue(list);
        }
    }

    // adds the given event to the logged in user's schedule list and calls writeList
    @Override
    public boolean addEvent(MovieEvent event, User loggedInUser) {
        if (getUserScheduleList(loggedInUser).getValue().containsEvent(event)) {
            return false;
        }
        getUserScheduleList(loggedInUser).getValue().addEvent(event);
        writeList(loggedInUser);
        return true;
    }

    // writes the logged in user's schedule list to file.
    private void writeList(User loggedInUser) {
        if (writer == null) {
            writer = new XmlObjectWriter(new XStream());
        }
        FileLocator fileLocator = new UserScheduleListLocator(getApplication().getApplicationContext(), loggedInUser);

        writer.writeToFile(mUserScheduleList.getValue(), fileLocator);
    }

    // returns a list of strings containing information about the logged in user's schedule lists contents from the given date. If there is nothign to be done, return null
    @Override
    public List<String> getListOfEventDatesByMonth(int year, int month, User loggedInUser) {
        month += 1; // begins at 0 so +1 is necessary

        if (loggedInUser == null) {
            return null;
        }
        if (previousMonth != null) {
            if (previousMonth == month) {
                return null;
            }
        }
        previousMonth = month;

        ArrayList<String> dates = new ArrayList<>();

        if (mMonthlyEvents == null) {
            mMonthlyEvents = new MutableLiveData<>();
            mMonthlyEvents.setValue(new MovieEventList());
        }
        mMonthlyEvents.getValue().clearList();

        for (MovieEvent me : getUserScheduleList(loggedInUser).getValue().getEventList()) {
            int monthNum = -1;
            int yearNum = -1;

            try {
                int[] nums = timeUtility.splitDate(me.getDate());
                monthNum = nums[1];
                yearNum = nums[2];
            } catch (NumberFormatException e) {
                Log.e(null, "Number format exception at getListOEventDatesByMonth, UserScheduleViewModel");
            }
            // check if the date of the event matches the given date and add the event if it does
            if (monthNum == month && yearNum == year) {
                dates.add(me.getDate());
                mMonthlyEvents.getValue().addEvent(me);
            }
        }

        return dates;
    }


    // returns an array of strings which each contain information about a movie event from a date matching the parameters.
    @Override
    public ArrayList<String> getEventsAsStringByDate(int year, int month, int dayOfMonth) {
        ArrayList<String> events = new ArrayList<>();
        for (MovieEvent me : mMonthlyEvents.getValue().getList()) {
            int[] dateNums = timeUtility.splitDate(me.getDate());
            if (dateNums[0] == dayOfMonth && dateNums[1] == month && dateNums[2] == year) {
                Context context = getApplication().getApplicationContext();
                StringBuilder sb = new StringBuilder();
                sb.append(context.getResources().getText(R.string.event_dialog_title_text)).append(" ").append(me.getMovieTitle()).append("\n");
                sb.append(context.getResources().getText(R.string.event_dialog_theatre_text)).append(" ").append(me.getTheatreName()).append("\n");
                sb.append(context.getResources().getText(R.string.event_dialog_date_text)).append(" ").append(me.getDate()).append("\n");
                sb.append(context.getResources().getText(R.string.event_dialog_start_text)).append(" ").append(me.getStartTime()).append("\n");
                sb.append(context.getResources().getText(R.string.event_dialog_end_text)).append(" ").append(me.getEndTime());
                events.add(sb.toString());
            }
        }
        return events;
    }

    // returns a list of strings representing the content that should be displayed when the user opens the user fragment
    @Override
    public List<String> getInitialContent(User loggedInUser) {
        if (timeUtility == null) {
            timeUtility = new TimeUtility();
        }
        int[] dateNums = timeUtility.getCurrentDateNumbers();
        List<String> content = getListOfEventDatesByMonth(dateNums[2], dateNums[1] - 1, loggedInUser);
        previousMonth = -1;
        return content;
    }
}
