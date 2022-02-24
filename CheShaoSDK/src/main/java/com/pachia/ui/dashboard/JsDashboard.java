package com.pachia.ui.dashboard;

import android.util.Log;
import android.webkit.JavascriptInterface;

public class JsDashboard {
    Listener listener;

    private interface CommandJS {
        String openLink = "chels_call_link";
        String closeWindow = "chels_close_window";
        String openWindow = "chels_open_window";
        String backtoWindow = "chels_back_window";
        String openFanPage = "chels_open_page_fb";
        String openGroup = "chels_open_group_fb";
        String loadUrl = "chels_url_load";
        String actionConnectFb = "chels_connect_facebook";
        String openBrowser = "chels_browser_show";
        String getImageFromDevice = "chels_get_img";
        String deleteImage = "chels_delete_image";
        String deleteAllImage = "chels_delete_all_img";
    }

    public JsDashboard(Listener listener) {
        this.listener = listener;
    }

    @JavascriptInterface
    public void mobAppSDKexecute(String command, String params) {
        Log.d("AppSDKexecute,", "command: " + command + "\n params: " + params);
        switch (command) {
            case CommandJS.openFanPage:
                if (listener != null) {
                    listener.openFanPage(params);
                }
                break;
            case CommandJS.openGroup:
                if (listener != null) {
                    listener.openGroup(params);
                }
                break;
            case CommandJS.openBrowser:
                if (listener != null) {
                    listener.openBrowser(params);
                }
                break;
            case CommandJS.getImageFromDevice:
                if (listener != null) {
                    listener.getImageFromDevice(params);
                }
                break;
            case CommandJS.deleteImage:
                if (listener != null) {
                    listener.deleteImage(params);
                }
                break;
            case CommandJS.deleteAllImage:
                if (listener != null) {
                    listener.deleteAllImage(params);
                }
                break;
            case CommandJS.loadUrl:
                if (listener != null) {
                    listener.loadUrl(params);
                }
                break;
            case CommandJS.closeWindow:
                if (listener != null) {
                    listener.onCloseWindow();
                }
                break;
            case CommandJS.openWindow:
                if (listener != null) {
                    listener.onOpenWindow(params);
                }
                break;
            case CommandJS.backtoWindow:
                if (listener != null) {
                    listener.onBackToWindow();
                }
                break;
            case CommandJS.actionConnectFb:
                if (listener != null) {
                    listener.onConnectFacebook(params);
                }
                break;
            case CommandJS.openLink:
                if (listener != null) {
                    listener.onConnectFacebook(params);
                }
                break;
            default:
                break;
        }

    }

    public interface Listener {
        void openFanPage(String param);

        void openGroup(String param);

        void openBrowser(String param);

        void getImageFromDevice(String param);

        void deleteImage(String param);

        void deleteAllImage(String param);

        void loadUrl(String param);

        void onCloseWindow();

        void onOpenWindow(String param);

        void onOpenLink(String param);

        void onBackToWindow();

        void onConnectFacebook(String param);
    }
}
