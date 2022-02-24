package com.pachia.ui.other;


import com.pachia.comon.presenter.BaseInteractor;

/**
 * Created by Dungnv
 */

public interface IGameInteractor extends BaseInteractor {
    void getMessagesInGame();

    void saveCharactor(String roleId, String areaId) throws Exception;

    void saveFCM(String userId, String token) throws Exception;

    void connectFaceBook(String token) throws Exception;
}
