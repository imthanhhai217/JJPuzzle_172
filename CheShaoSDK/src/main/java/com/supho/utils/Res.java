package com.supho.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.pachia.comon.config.GameConfigs;
import com.pachia.comon.utils.DeviceUtils;
import com.quby.R;

public class Res {
	
	public static String string(Context context, int key) {
		try{
			String currentLang = Constants.LANG_EN;
			if (GameConfigs.getInstance().getLang() != null) {
				currentLang = GameConfigs.getInstance().getLang();
				Preference.save(context , "languages" , currentLang);
			}else{
				currentLang = DeviceUtils.getLanguage();
			}
            Log.d("TAG" , "currentLang : " + currentLang);
			if(R.string.title_force_logout == key){
				currentLang = Preference.getString(context , "languages");
			}

			if (Constants.LANG_VI.equalsIgnoreCase(currentLang)) {

				if (R.string.hello == key) return "Chào";
				if (R.string.reload == key) return "Tải lại";
				if (R.string.connection_lost == key) return "Mất kết nối";
				if (R.string.something_went_wrong == key) return "Có lỗi xảy ra";
				if (R.string.connection_error == key) return "Lỗi kết nối";
				if (R.string.unable_to_connect == key) return "Không thể kết nối tới server";
				if (R.string.dialog_tips_title == key) return "Bạn có thể hiển thị nút điều khiển như sau";
				if (R.string.dont_ask_again == key) return "Không hiển thị lại";
				if (R.string.dismiss == key) return "Kéo vào đây để ẩn";
				if (R.string.remind_me == key) return "Bỏ qua";
				if (R.string.retry == key) return "Thử lại";
				if (R.string.exit == key) return "Thoát";
				if (R.string.please_wait == key) return "Xin đợi";
				if (R.string.logging_in == key) return "Đang đăng nhập";
				if (R.string.connecting_facebook == key) return "Đang kết nối tới Facebook của bạn";
				if (R.string.connecting_google == key) return "Đang kết nối tới tài khoản Google của bạn";
				if (R.string.connecting_google_play == key) return "Đang kết nối tới Google Play";
				if (R.string.connecting_server == key) return "Đang kết nối tới server";
				if (R.string.copied == key) return "Đã copy!";
				if (R.string.copy == key) return "Copy";
				if (R.string.complete_now == key) return "Hoàn thành ngay";
				if (R.string.notice == key) return "Thông báo";
				if (R.string.bind_account == key) return "Nối tài khoản ngay";
				if (R.string.attention_overlay == key) return "Chú ý";
				if (R.string.attention_permission == key) return "Chú ý";
				if (R.string.attention_message == key) return "Bạn cần quyền truy cập bộ nhớ để giải nén tài nguyên game.";
				if (R.string.draw_over_apps_message == key) return "Bạn cần cấp thêm quyền để game hoạt động tốt nhất";
				if (R.string.hide == key) return "Ẩn";
				if (R.string.cancel == key) return "Hủy";
				if (R.string.warning == key) return "Chú ý";
				if (R.string.message_warning == key) return "Bạn cần cấp quyền để có thể trải nghiệm ứng dụng tốt nhất";
				if (R.string.do_you_want_close == key) return "Bạn có muốn đóng không ?";
				if (R.string.message_exit == key) return "Bạn có muốn thoát không ?";
				if (R.string.close == key) return "Đóng";
				if (R.string.alert_image_validate == key) return "Chỉ được chọn tối đa 3 ảnh";
				if (R.string.images_selected == key) return "Ảnh đã chọn";

				if (R.string.logout == key) return "Đăng xuất";
				if (R.string.title_force_logout == key) return "Phiên đăng nhập của bạn đã hết hạn. \n    Vui lòng mở lại game để tiếp tục.";
			} else if (Constants.LANG_EN.equalsIgnoreCase(currentLang)) {

				if (R.string.hello == key) return "Hello";
				if (R.string.reload == key) return "Reload";
				if (R.string.connection_lost == key) return "Oops! Connection lost";
				if (R.string.something_went_wrong == key) return "Oops! Something went wrong";
				if (R.string.connection_error == key) return "Connection error";
				if (R.string.unable_to_connect == key) return "Cannot connect to server";
				if (R.string.dialog_tips_title == key) return "You can show up the control button as following";
				if (R.string.dont_ask_again == key) return "Don't ask again";
				if (R.string.dismiss == key) return "Drop here to hide";
				if (R.string.remind_me == key) return "Remind me";
				if (R.string.retry == key) return "Retry";
				if (R.string.exit == key) return "Exit";
				if (R.string.please_wait == key) return "Please wait";
				if (R.string.logging_in == key) return "Logging in";
				if (R.string.connecting_facebook == key) return "Connecting to your Facebook account";
				if (R.string.connecting_google == key) return "Connecting to your Google account";
				if (R.string.connecting_google_play == key) return "Connecting to your Google Play account";
				if (R.string.connecting_server == key) return "Connecting to server";
				if (R.string.copied == key) return "Copied!";
				if (R.string.copy == key) return "Copy";
				if (R.string.complete_now == key) return "Complete now";
				if (R.string.notice == key) return "Notice";
				if (R.string.bind_account == key) return "Bind account";
				if (R.string.attention_overlay == key) return "Notice";
				if (R.string.attention_permission == key) return "Attention";
				if (R.string.attention_message == key) return "We need read and write permissions to extract game resource.";
				if (R.string.draw_over_apps_message == key) return "We need to grant Draw Over Apps permission to enable game to perform its full features";
				if (R.string.hide == key) return "Hide";
				if (R.string.cancel == key) return "Cancel";
				if (R.string.warning == key) return "Attention";
				if (R.string.message_warning == key) return "We need more permission to deliver the best experience";
				if (R.string.do_you_want_close == key) return "Do you want close ?";
				if (R.string.message_exit == key) return "Do you want to exit ?";
				if (R.string.close == key) return "Close";
				if (R.string.alert_image_validate == key) return "You can only select upto 3 photos";
				if (R.string.images_selected == key) return "Images selected";

				if (R.string.logout == key) return "Logout";
				if (R.string.title_force_logout == key) return "      Your session has expired.\nPlease restart game to continue.";
			} else if (Constants.LANG_ID.equalsIgnoreCase(currentLang)) {

				if (R.string.hello == key) return "Halo";
				if (R.string.reload == key) return "Memuat ulang";
				if (R.string.something_went_wrong == key) return "Oops! Terjadi kesalahan";
				if (R.string.connection_error == key) return "Koneksi error";
				if (R.string.unable_to_connect == key) return "Tidak dapat terhubung ke server";
				if (R.string.dialog_tips_title == key) return "Tampilkan tombol";
				if (R.string.dont_ask_again == key) return "Jangan tanya lagi";
				if (R.string.dismiss == key) return "Abaikan";
				if (R.string.remind_me == key) return "Ingatkan";
				if (R.string.retry == key) return "Coba lagi";
				if (R.string.exit == key) return "Keluar";
				if (R.string.please_wait == key) return "Harap tunggu";
				if (R.string.logging_in == key) return "Masuk";
				if (R.string.connecting_facebook == key) return "Terhubung ke akun Facebook";
				if (R.string.connecting_google == key) return "Terhubung ke akun Google";
				if (R.string.connecting_google_play == key) return "Terhubung ke akun Google Play";
				if (R.string.connecting_server == key) return "Terhubung ke server";
				if (R.string.copied == key) return "Disalin!";
				if (R.string.copy == key) return "Disalin";
				if (R.string.complete_now == key) return "Selesai";
				if (R.string.notice == key) return "Pemberitahuan";
				if (R.string.bind_account == key) return "Ikat akun";
				if (R.string.attention_overlay == key) return "Notice";
				if (R.string.attention_permission == key) return "Attention";
				if (R.string.attention_message == key) return "We need read and write permission to extract game resource.";
				if (R.string.draw_over_apps_message == key) return "We need to grant Draw Over Apps permission to enable game to perform its full features";
				if (R.string.hide == key) return "Hide";
				if (R.string.cancel == key) return "Cancel";
				if (R.string.warning == key) return "Attention";
				if (R.string.message_warning == key) return "We need more permission to deliver the best experience";
				if (R.string.do_you_want_close == key) return "Do you want close ?";
				if (R.string.message_exit == key) return "Do you want exit ?";
				if (R.string.close == key) return "Close";
				if (R.string.alert_image_validate == key) return "You can only select upto 3 photos";
				if (R.string.images_selected == key) return "Images selected";

				if (R.string.logout == key) return "Logout";
				if (R.string.title_force_logout == key) return "      Your session has expired.\nPlease restart game to continue.";
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	public static int drawableResource(Context context, int id) {
		String currentLang = Constants.LANG_EN;
		if (GameConfigs.getInstance().getLang() != null) {
			currentLang = GameConfigs.getInstance().getLang();
		}
		if (Constants.LANG_VI.equalsIgnoreCase(currentLang)) {
			
			if (R.drawable.float_button == id) return R.drawable.float_button;
			if (R.drawable.bar_header == id) return R.drawable.bar_header_vn;
			if (R.drawable.float_hide_tips_animation == id) return R.drawable.float_hide_tips_animation_vn;

		}
		return id;
	}
	
	public static Drawable drawable(Context context, int id) {
		return ContextCompat.getDrawable(context, drawableResource(context, id));
	}

}
