package com.jude.know.model.server;

import com.jude.know.model.AccountModel;
import com.jude.know.model.ImageModel;
import com.jude.know.model.QuestionModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by zhuchenxi on 16/1/25.
 */
@Singleton
@Component(modules = {ServiceAPIModule.class})
public interface ServiceModelComponent {
    void inject(AccountModel model);
    void inject(QuestionModel model);
    void inject(ImageModel model);
}
