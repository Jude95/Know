package com.jude.know.app;

import android.app.Application;

import com.jude.beam.Beam;
import com.jude.beam.expansion.BeamBaseActivity;
import com.jude.beam.expansion.list.ListConfig;
import com.jude.beam.expansion.overlay.ViewExpansionDelegate;
import com.jude.beam.expansion.overlay.ViewExpansionDelegateProvider;
import com.jude.know.BuildConfig;
import com.jude.know.R;
import com.jude.know.config.Dir;
import com.jude.utils.JFileManager;
import com.jude.utils.JUtils;

/**
 * Created by zhuchenxi on 15/6/7.
 */
public class APP extends Application {
    private static APP instance;
    public static APP getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        JUtils.initialize(this);
        JUtils.setDebug(BuildConfig.DEBUG, "knowDefault");

        JFileManager.getInstance().init(this, Dir.values());
        Beam.init(this);
        Beam.setViewExpansionDelegateProvider(new ViewExpansionDelegateProvider() {
            @Override
            public ViewExpansionDelegate createViewExpansionDelegate(BeamBaseActivity activity) {
                return new NewViewExpansion(activity);
            }
        });
        Beam.setActivityLifeCycleDelegateProvider(ActivityDelegate::new);
        ListConfig.setDefaultListConfig(new ListConfig()
                .setPaddingNavigationBarAble(true)
                .setRefreshAble(true)
                .setLoadmoreAble(true)
                .setContainerEmptyRes(R.layout.view_empty)
                .setContainerProgressRes(R.layout.view_progress)
                .setContainerLayoutRes(R.layout.activity_recyclerview));

    }

}
