package com.pachia.comon.js;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.pachia.comon.js.command.CmdDashboard;
import com.pachia.comon.js.command.CmdLogin;
import com.pachia.comon.utils.ToastUtils;
import com.supho.gui.dialog.SuphoDialogStartWebFragment;
import com.supho.gui.dialog.SuphoDialogWebviewFragment;
import com.supho.gui.dialog.SuphoHaveBackButtonFragment;

public class JsHandler {

    private static String TAG = JsHandler.class.getSimpleName();

    private Activity activity;

    private SuphoHaveBackButtonFragment webDialogHaveBackButton;
    private SuphoDialogWebviewFragment dialogWebviewFragment;
    private SuphoDialogStartWebFragment dialogStartWebFragment;

    private enum switchCommandJS {
        //SDK 5.0 ----------------------------------------------------------------------------------
        chels_login_done,
        chels_connect_facebook,
        chels_update_done,
        chels_open_page_fb,
        chels_open_group_fb,
        chels_contact_show,
        chels_browser_show,
        chels_call_mobile,
        chels_upload_img,
        chels_get_img,
        chels_delete_image,
        chels_delete_all_img,
        copyToClipboard,
        chels_call_link,
        chels_invite_done,
        chels_close_window,
        chels_open_window,
        chels_track_event,
    }

    public JsHandler(Activity activity) {
        this.activity = activity;
    }

    public JsHandler(Activity activity, SuphoDialogWebviewFragment dialogWebviewFragment) {
        this.activity = activity;
        this.dialogWebviewFragment = dialogWebviewFragment;
    }

    public JsHandler(Activity activity, SuphoDialogStartWebFragment dialogStartWebFragment) {
        this.activity = activity;
        this.dialogStartWebFragment = dialogStartWebFragment;
    }


    public JsHandler(Activity activity, SuphoHaveBackButtonFragment webDialogHaveBackButton) {
        this.activity = activity;
        this.webDialogHaveBackButton = webDialogHaveBackButton;
    }


    @JavascriptInterface
    public void mobAppSDKexecute(String command, String params) {
        try {
            Log.i(TAG, " AppSDKexecute: command = " + command + "; params = " + params);

            switch (switchCommandJS.valueOf(command)) {

                /**********  PAYMENT  **********/

                case chels_contact_show:
                    CmdDashboard.getInstance().openContact(activity, params);
                    break;
                case chels_browser_show:
                    CmdDashboard.getInstance().openBrowser(activity, params);
                    break;
                case chels_open_page_fb:
                    CmdDashboard.getInstance().openFBFanpage(activity, params);
                    break;
                case chels_open_group_fb:
                    CmdDashboard.getInstance().openFBGroup(activity, params);
                    break;
                case chels_call_mobile:
                    CmdDashboard.getInstance().makePhoneCall(activity, params);
                    break;
                case copyToClipboard:
                    CmdDashboard.getInstance().mobCopyToClipboard(activity, params);
                    break;
                case chels_get_img:
                    CmdDashboard.getInstance().selectImage2(activity, dialogWebviewFragment, params);
                    break;
                case chels_upload_img:
                    CmdDashboard.getInstance().getIssue(activity, dialogWebviewFragment, params);
                    break;
                case chels_delete_image:
//                    CmdDashboard.getInstance().deleteImageData(activity, webFragment, params);
                    break;
                case chels_delete_all_img:
                    CmdDashboard.getInstance().clearImageData(activity);
                    break;
                case chels_close_window:
//                    dialogWebviewFragment.dismiss();
                    CmdDashboard.getInstance().refreshBugForm(activity, params, dialogWebviewFragment);
                    break;
                case chels_invite_done:
                    CmdDashboard.getInstance().chooseFriend(activity, params);
                    break;
                case chels_call_link:
                    CmdDashboard.getInstance().openDialogWebView(activity, params);
                    break;
                case chels_track_event:
                    CmdDashboard.getInstance().trackingFromWeb(activity, params);
                    break;
                case chels_connect_facebook:
                    CmdLogin.getInstance().upgradeFacebook(activity, dialogWebviewFragment, dialogStartWebFragment, params);
                    break;
                case chels_update_done:
                    ToastUtils.showShortToast(activity, " updateSuccess Cho cả connect fb và update profile");
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}