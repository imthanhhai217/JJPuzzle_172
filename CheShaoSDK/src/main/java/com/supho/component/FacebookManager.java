package com.supho.component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.supho.utils.Constants;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.model.GameRequestContent;
import com.facebook.share.model.GameRequestContent.ActionType;
import com.facebook.share.model.GameRequestContent.Builder;
import com.facebook.share.model.GameRequestContent.Filters;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.GameRequestDialog;
import com.facebook.share.widget.GameRequestDialog.Result;
import com.facebook.share.widget.ShareDialog;
import com.facebook.share.widget.ShareDialog.Mode;
import com.pachia.comon.utils.LogUtils;
import com.pachia.comon.utils.Utils;
import com.quby.R;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class FacebookManager {

	// private final static String TAG = FacebookManager.class.getSimpleName();
	public static FacebookManager INSTANCE;

	private CallbackManager callbackManager;

	public static FacebookManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new FacebookManager();
		}
		return INSTANCE;
	}

	private FacebookManager() {
	}

	public void init() {
		callbackManager = CallbackManager.Factory.create();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.REQUEST_CODE_FACEBOOK_LOGIN
				|| requestCode == Constants.REQUEST_CODE_FACEBOOK_SHARE
				|| requestCode == Constants.REQUEST_CODE_FACEBOOK_INVITE
				|| requestCode == Constants.REQUEST_CODE_FACEBOOK_INVITE_GAME) {
			if (callbackManager != null) {
				callbackManager.onActivityResult(requestCode, resultCode, data);
			}
		}
	}



	public void login(Activity activity, final LoginCallback callback) {
		try {
			LoginManager.getInstance().registerCallback(callbackManager,
					new FacebookCallback<LoginResult>() {
						@Override
						public void onSuccess(LoginResult result) {
							LogUtils.d("Facebook Manager","Success" + result);
							if (callback != null) {
								LogUtils.d("Facebook Manager", "onSuccess: " + callback.toString() );
								callback.onSuccess(result.getAccessToken(),
										result.getRecentlyGrantedPermissions(),
										result.getRecentlyDeniedPermissions());
							}
						}

						@Override
						public void onCancel() {
							Log.d("Facebook Manager","cancel ");
							if (callback != null) {
								callback.onCancel();
							}
						}

						@Override
						public void onError(FacebookException e) {
							Log.d("Facebook Manager","error");
							if (callback != null) {
								callback.onError(e);
							}
						}
					});
			LoginManager.getInstance().logOut();
			LoginManager.getInstance().logInWithReadPermissions(activity,
					Arrays.asList("public_profile", "email"));
		}catch (Exception e){
			LogUtils.e("Facebook Manager", "login: catch "  +e.getMessage());
			e.printStackTrace();
		}
	}

	public void logout() {
		if (!FacebookSdk.isInitialized()) {
			return;
		}
		LoginManager.getInstance().logOut();
	}

	public void shareLink(Activity activity, ShareContent params) {
		shareLink(activity, params, null);
	}

	public void shareLink(final Activity activity, ShareContent params,
			final ShareCallback callback) {

		try {
			if (Utils.isOnline(activity)) {
				if (params == null) {
					if (callback != null) {
						callback.onError(new InvalidParameterException());
					}
					return;
				}

				if (!ShareDialog.canShow(ShareLinkContent.class)
						&& !isFacebookInstalled(activity)) {
					LoginManager.getInstance().logInWithReadPermissions(
							activity, Collections.singletonList("email"));
				}
				if (ShareDialog.canShow(ShareLinkContent.class)) {
					ShareLinkContent.Builder builder = new ShareLinkContent.Builder();
					if (!TextUtils.isEmpty(params.getContentDescription())) {
						builder.setContentDescription(params
								.getContentDescription());
					}
					if (!TextUtils.isEmpty(params.getContentTitle())) {
						builder.setContentTitle(params.getContentTitle());
					}
					if (!TextUtils.isEmpty(params.getContentUrl())) {
						builder.setContentUrl(Uri.parse(params.getContentUrl()));
					}
					if (!TextUtils.isEmpty(params.getImageUrl())) {
						builder.setImageUrl(Uri.parse(params.getImageUrl()));
					}
					if (params.getPeopleIds() != null) {
						builder.setPeopleIds(params.getPeopleIds());
					}
					if (!TextUtils.isEmpty(params.getPlaceId())) {
						builder.setPlaceId(params.getPlaceId());
					}
					if (!TextUtils.isEmpty(params.getRef())) {
						builder.setRef(params.getRef());
					}
					ShareLinkContent linkContent = builder.build();

					ShareDialog shareDialog = new ShareDialog(activity);
					// this part is optional
					shareDialog.registerCallback(callbackManager,
							new FacebookCallback<Sharer.Result>() {

								@Override
								public void onSuccess(Sharer.Result result) {
									if (callback != null) {
										callback.onSuccess(result.getPostId());
										Toast.makeText(activity,
												"Success!!!", Toast.LENGTH_LONG)
												.show();
									}
								}

								@Override
								public void onCancel() {
									if (callback != null) {
										callback.onCancel();
										Toast.makeText(activity,
												"Error!!!", Toast.LENGTH_LONG)
												.show();
									}
								}

								@Override
								public void onError(FacebookException e) {
									if (callback != null) {
										callback.onError(e);
										Toast.makeText(activity,
												"Cancel!!!", Toast.LENGTH_LONG)
												.show();
									}
								}
							});

					shareDialog.show(linkContent);
				}
			} else {
				Toast.makeText(
						activity,
						activity.getResources().getString(
								R.string.error_network), Toast.LENGTH_LONG)
						.show();
			}

		} catch (Exception e) {
			if (callback != null) {
				callback.onError(e);
			}
		}
	}

	@SuppressLint("WrongConstant")
	public void sharePhoto(Activity activity, Bitmap bitmap,
						   final ShareCallback callback) {

		try {
			if (Utils.isOnline(activity)) {
				if (bitmap == null) {
					if (callback != null) {
						callback.onError(new InvalidParameterException());
					}
					return;
				}

				if (!ShareDialog.canShow(SharePhotoContent.class)
						&& !isFacebookInstalled(activity)) {
					LoginManager.getInstance().logInWithReadPermissions(
							activity, Collections.singletonList("email"));
				}
				SharePhoto photo = new SharePhoto.Builder().setBitmap(bitmap)
						.build();
				SharePhotoContent content = new SharePhotoContent.Builder()
						.addPhoto(photo).build();

				ShareDialog shareDialog = new ShareDialog(activity);
				shareDialog.registerCallback(callbackManager,
						new FacebookCallback<Sharer.Result>() {

							@Override
							public void onSuccess(Sharer.Result result) {
								if (callback != null) {
									callback.onSuccess(result.getPostId());
								}
							}

							@Override
							public void onCancel() {
								if (callback != null) {
									callback.onCancel();
								}
							}

							@Override
							public void onError(FacebookException e) {
								if (callback != null) {
									callback.onError(e);
								}
							}
						});

				shareDialog.show(content, Mode.AUTOMATIC);
			} else {
				Toast.makeText(
						activity,
						activity.getResources().getString(
								R.string.error_network), 1).show();
			}

		} catch (Exception e) {
			if (callback != null) {
				callback.onError(e);
			}
		}
	}

	public void invite(Activity activity, InviteContent params) {
		invite(activity, params, null);
	}

	public void invite(final Activity activity, InviteContent params,
					   final InviteCallback callback) {

		try {
			if (params == null) {
				if (callback != null) {
					callback.onError(new InvalidParameterException());
				}
				return;
			}

			if (!AppInviteDialog.canShow() && !isFacebookInstalled(activity)) {
				LoginManager.getInstance().logInWithReadPermissions(activity,
						Collections.singletonList("email"));
			}
			if (AppInviteDialog.canShow()) {
				AppInviteContent.Builder builder = new AppInviteContent.Builder();
				if (!TextUtils.isEmpty(params.getApplinkUrl())) {
					builder.setApplinkUrl(params.getApplinkUrl());
				}
				if (!TextUtils.isEmpty(params.getPreviewImageUrl())) {
					builder.setPreviewImageUrl(params.getPreviewImageUrl());
				}
				final AppInviteContent content = builder.build();

				AppInviteDialog appInviteDialog = new AppInviteDialog(activity);
				appInviteDialog.registerCallback(callbackManager,
						new FacebookCallback<AppInviteDialog.Result>() {

							@Override
							public void onSuccess(AppInviteDialog.Result result) {
								if (callback != null) {

									callback.onSuccess(result.getData());
									Toast.makeText(activity , "Invite success" , Toast.LENGTH_LONG).show();
								}
							}

							@Override
							public void onCancel() {
								if (callback != null) {
									callback.onCancel();
									Toast.makeText(activity , "Invite cancel" , Toast.LENGTH_LONG).show();
								}
							}

							@Override
							public void onError(FacebookException e) {
								if (callback != null) {
									callback.onError(e);
									Toast.makeText(activity , "Invite error" , Toast.LENGTH_LONG).show();
								}
							}
						});

				appInviteDialog.show(content);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void inviteGameRequest(final Activity activity, InviteGameContent params,
			final InviteGameCallback callback) {
		try {
			if (params == null) {
				if (callback != null) {
					callback.onError(new InvalidParameterException());
				}
			}
			if (!AppInviteDialog.canShow() && !isFacebookInstalled(activity)) {
				LoginManager.getInstance().logInWithReadPermissions(activity,
						Collections.singletonList("email"));
				return;
			}
			if (!GameRequestDialog.canShow()) {
				if (callback != null) {
					callback.onError(new Exception(
							"Game Request Dialog can't show"));
				}
				return;
			}

			Builder contentBuilder = new GameRequestContent.Builder()
					.setTitle(Objects.requireNonNull(params).getTitle());
			if (params.getActionType() != null) {
				contentBuilder.setActionType(params.getActionType());
			}
			if (!TextUtils.isEmpty(params.getData())) {
				contentBuilder.setData(params.getData());
			}
			if (params.getFilters() != null) {
				contentBuilder.setFilters(params.getFilters());
			}
			if (!TextUtils.isEmpty(params.getMessage())) {
				contentBuilder.setMessage(params.getMessage());
			}
			if (!TextUtils.isEmpty(params.getObjectId())) {
				contentBuilder.setObjectId(params.getObjectId());
			}
			if (params.getRecipients() != null) {
				contentBuilder.setRecipients(params.getRecipients());
			}
			if (params.getSuggestions() != null) {
				contentBuilder.setSuggestions(params.getSuggestions());
			}
			if (!TextUtils.isEmpty(params.getTitle())) {
				contentBuilder.setTitle(params.getTitle());
			}
			if (!TextUtils.isEmpty(params.getTo())) {
				contentBuilder.setTo(params.getTo());
			}

			GameRequestContent dialogContent = contentBuilder.build();
			
			GameRequestDialog gameDialog = new GameRequestDialog(activity);
			gameDialog.registerCallback(callbackManager, new FacebookCallback<GameRequestDialog.Result>() {
				
				@SuppressLint("WrongConstant")
				@Override
				public void onSuccess(Result result) {
					if(callback != null){
						callback.onSuccess(result);
						Toast.makeText(activity, "invite success", 1).show();
					}
				}
				
				@SuppressLint("WrongConstant")
				@Override
				public void onError(FacebookException error) {
					if(callback != null){
						callback.onError(error);
						Toast.makeText(activity, "invite error", 1).show();
					}
				}
				
				@SuppressLint("WrongConstant")
				@Override
				public void onCancel() {
					if(callback != null){
						callback.onCancel();
						Toast.makeText(activity, "invite cancel", 1).show();
					}
				}
			});
			gameDialog.show(dialogContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public interface InviteGameCallback extends BaseInviteCallback<GameRequestDialog.Result>{
	}
	public interface InviteCallback extends BaseInviteCallback<Bundle>{
	}
	public interface BaseInviteCallback<T>{
		public void onSuccess(T data);
		public void onCancel();
		public void onError(Throwable t);
	}
	private boolean isFacebookInstalled(Context c) {
		try {
			c.getPackageManager().getApplicationInfo("com.facebook.katana", 0);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}

	public interface LoginCallback {
		public void onSuccess(AccessToken accessToken,
				Set<String> recentlyGrantedPermissions,
				Set<String> recentlyDeniedPermissions);

		public void onCancel();

		public void onError(Throwable t);
	}

	public interface ShareCallback {
		public void onSuccess(String postId);

		public void onCancel();

		public void onError(Throwable t);
	}

	public static class ShareContent {

		private String contentUrl;
		private String contentTitle;
		private String contentDescription;
		private String imageUrl;
		private List<String> peopleIds;
		private String placeId;
		private String ref;

		public String getContentUrl() {
			return contentUrl;
		}

		public ShareContent setContentUrl(String url) {
			this.contentUrl = url;
			return this;
		}

		public String getContentTitle() {
			return contentTitle;
		}

		public ShareContent setContentTitle(String title) {
			this.contentTitle = title;
			return this;
		}

		public String getContentDescription() {
			return contentDescription;
		}

		public ShareContent setContentDescription(String content) {
			this.contentDescription = content;
			return this;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public ShareContent setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
			return this;
		}

		public List<String> getPeopleIds() {
			return peopleIds;
		}

		public ShareContent setPeopleIds(List<String> peopleIds) {
			this.peopleIds = peopleIds;
			return this;
		}

		public String getPlaceId() {
			return placeId;
		}

		public ShareContent setPlaceId(String placeId) {
			this.placeId = placeId;
			return this;
		}

		public String getRef() {
			return ref;
		}

		public ShareContent setRef(String ref) {
			this.ref = ref;
			return this;
		}

	}

	public static class InviteContent {

		private String applinkUrl;
		private String previewImageUrl;

		public InviteContent() {
		}

		public String getApplinkUrl() {
			return applinkUrl;
		}

		public InviteContent setApplinkUrl(String applinkUrl) {
			this.applinkUrl = applinkUrl;
			return this;
		}

		public String getPreviewImageUrl() {
			return previewImageUrl;
		}

		public InviteContent setPreviewImageUrl(String previewImageUrl) {
			this.previewImageUrl = previewImageUrl;
			return this;
		}

	}
	
	public static class InviteGameContent{
		private String message;
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public List<String> getRecipients() {
			return recipients;
		}
		public void setRecipients(List<String> recipients) {
			this.recipients = recipients;
		}
		public String getData() {
			return data;
		}
		public void setData(String data) {
			this.data = data;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public ActionType getActionType() {
			return actionType;
		}
		public void setActionType(ActionType actionType) {
			this.actionType = actionType;
		}
		public Filters getFilters() {
			return filters;
		}
		public void setFilters(Filters filters) {
			this.filters = filters;
		}
		public String getObjectId() {
			return objectId;
		}
		public void setObjectId(String objectId) {
			this.objectId = objectId;
		}
		public List<String> getSuggestions(){
			return suggestions;
		}
		public void setSuggestions(List<String> suggestions){
			this.suggestions = suggestions;
		}
		public String getTo(){
			return to;
		}
		@Deprecated
		public void setTo(String to){
			this.to = to;
		}
		private List<String> recipients;
		private String data;
		private String title;
		private ActionType actionType;
		private Filters filters; 
		private String objectId;
		private List<String> suggestions;
		private String to;

	}

}
