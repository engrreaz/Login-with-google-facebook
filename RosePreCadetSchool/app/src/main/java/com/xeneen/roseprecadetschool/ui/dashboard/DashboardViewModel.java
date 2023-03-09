package com.xeneen.roseprecadetschool.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private static MutableLiveData<String> mText = null;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("33333");
    }

    public static LiveData<String> getText() {
        return mText;
    }
}