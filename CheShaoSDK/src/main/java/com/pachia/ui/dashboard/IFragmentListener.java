package com.pachia.ui.dashboard;

import android.content.Intent;

public interface IFragmentListener {
    void onActivityRessult(int requestCode, int resultCode, Intent data);
}
