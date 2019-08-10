package com.example.timekeepers.Dashboard;

import java.util.Date;

public class SaveWorkEntry {

    // Save the work entry to: /Jobs/[user]/Users_Jobs/[jobID]/Job_Entries/[entryID]

    /**
     * Items to save:
     *   Job_Title
     *   Start_Time
     *   End_Time
     *   Break_Time
     *   Hours_Worked
     *   Pay
     *   Notes (Eventually)
     */
    public SaveWorkEntry(String jobTitle, Date startTime, Date endTime, double breakTime,
                         double hoursWorked, double pay, String notes) {

    }

}
