package com.example.stek3.carparking.Interfaces;

import com.example.stek3.carparking.Parks.Park;

import java.util.List;

/**
 * Created by Stek3 on 09-Apr-18.
 */

public interface ParkRepoResponse {

    void PostExecuteFinish(List<Park> OutPut);

    void onPostExecute(List<Park> OutPut);
}
