package com.pachia.ui.other;

import com.pachia.comon.presenter.BaseView;
import com.pachia.comon.presenter.InteractorCallback;

public class GamePresenterImpl implements IGamePresenter {

    private IGameInteractor mInteractor;
    private BaseView mBaseView;

    public GamePresenterImpl(BaseView mBaseView) {
        this.mBaseView = mBaseView;
        mInteractor = new GameInteractorImpl(mCallback);
    }

    private final InteractorCallback<Object> mCallback = new InteractorCallback<Object>() {
        @Override
        public void success(Object x) {
            if (mBaseView != null) {
                mBaseView.hideProgress();
                mBaseView.success(x);
            }
        }

        @Override
        public void error(Object o) {
            if (mBaseView != null) {
                mBaseView.hideProgress();
                mBaseView.error(o);
            }
        }
    };

    @Override
    public void cancelRequest(String... tags) {
        mInteractor.cancelRequest();
    }


    @Override
    public void getMessagesInGame() {
        mInteractor.cancelRequest();
        mInteractor.getMessagesInGame();
    }

    @Override
    public void saveCharactor(String roleId, String areaId) throws Exception {
        mInteractor.cancelRequest();
        mInteractor.saveCharactor(roleId, areaId);
        if (mBaseView != null) {
            mBaseView.showProgress("");
        }
    }

    @Override
    public void saveFCM(String userId, String token) throws Exception {
        mInteractor.cancelRequest();
        mInteractor.saveFCM(userId, token);
        if (mBaseView != null) {
            mBaseView.showProgress("");
        }
    }

    @Override
    public void connectFaceBook(String token) throws Exception {
        mInteractor.cancelRequest();
        mInteractor.connectFaceBook(token);
        if (mBaseView != null) {
            mBaseView.showProgress("");
        }
    }


}
