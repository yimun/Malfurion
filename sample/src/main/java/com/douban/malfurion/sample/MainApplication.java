package com.douban.malfurion.sample;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;

import com.douban.frodo.BaseProjectModuleApplication;
import com.douban.frodo.FrodoVersionControl;
import com.douban.frodo.baseproject.ModuleManager;
import com.douban.frodo.fangorns.audio.AudioModuleApplication;
import com.douban.frodo.fangorns.newrichedit.DraftDeserializer;
import com.douban.frodo.httpdns.HttpDnsManager;
import com.douban.frodo.search.FrodoSearchManager;
import com.douban.frodo.search.SearchModuleApplication;
import com.douban.frodo.subject.SubjectModuleApplication;
import com.douban.frodo.subject.util.strategy.BookSearchStrategy;
import com.douban.frodo.subject.util.strategy.CelebritySearchStrategy;
import com.douban.frodo.subject.util.strategy.DramaSearchStrategy;
import com.douban.frodo.subject.util.strategy.EventSearchStrategy;
import com.douban.frodo.subject.util.strategy.IlmenMixedSearchStrategy;
import com.douban.frodo.subject.util.strategy.MovieTvSearchStrategy;
import com.douban.frodo.subject.util.strategy.MusicSearchStrategy;
import com.douban.frodo.utils.AppContext;
import com.douban.frodo.utils.BuildInfo;
import com.douban.frodo.utils.GsonHelper;
import com.douban.newrichedit.model.Entity;
import com.douban.push.PushClient;

/**
 * Created by linwei on 2017/6/4.
 */


public class MainApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        setupAppContext();
        initModules();

        GsonHelper.registerTypeAdapter(Entity.class, new DraftDeserializer());

        FrodoVersionControl.getInstance()
                .setVersionName("4.10.0")
                .setVersionCode(90);
//        SubjectFeatureSwitch.enableDoulist(false);
//        SubjectFeatureSwitch.enableForumTopicEdit(false);
//        SubjectFeatureSwitch.enableChat(false);
//        SubjectFeatureSwitch.enableGroup(false);

        boolean ismMainProcess = PushClient.Helper.shouldInitApp(this);

        // gson
        ModuleManager.getInstance().setupGson(this, ismMainProcess);

        // init modules 'onBeforeApplicationCreate'
        ModuleManager.getInstance().onBeforeApplicationCreate(this, BuildConfig.DEBUG, true, ismMainProcess);

        // init modules 'setupNetworkIndependentModules'
        ModuleManager.getInstance().setupNetworkIndependentModules(this, BuildConfig.DEBUG, ismMainProcess);

        // init modules `setupNetworkDependentModules`
        ModuleManager.getInstance().setupNetworkDependentModules(this, BuildConfig.DEBUG, ismMainProcess);

        // setup httpdns
        setupHttpDns();


        // init modules `onAfterApplicationCreate`
        ModuleManager.getInstance().onAfterApplicationCreate(this, BuildConfig.DEBUG, true, ismMainProcess);

        FrodoSearchManager.getInstance()
                .register(new MovieTvSearchStrategy())
                .register(new BookSearchStrategy())
                .register(new MusicSearchStrategy())
                .register(new IlmenMixedSearchStrategy())
                .register(new EventSearchStrategy())
                .register(new DramaSearchStrategy())
                .register(new CelebritySearchStrategy());

    }

    private void initModules() {
        ModuleManager.getInstance()
                .registerModule(BaseProjectModuleApplication.getInstance())
                .registerModule(SubjectModuleApplication.getInstance())
                .registerModule(SearchModuleApplication.getInstance())
                .registerModule(AudioModuleApplication.getInstance());
    }

    private void setupAppContext() {
        final BuildInfo info = new BuildInfo();
        info.debug = BuildConfig.DEBUG;
        info.appId = BuildConfig.APPLICATION_ID;
        info.buildType = BuildConfig.BUILD_TYPE;
        final PackageManager pm = getPackageManager();
        try {
            final PackageInfo pkgInfo = pm.getPackageInfo(getPackageName(),
                    PackageManager.GET_META_DATA);
            info.pkgName = pkgInfo.packageName;
            info.versionName = pkgInfo.versionName;
            info.versionCode = pkgInfo.versionCode;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        info.market = "subject";
        AppContext.setBuildInfo(info);
    }

    /**
     * 初始化httpdns
     */
    private void setupHttpDns() {
        HttpDnsManager.getInstance().setGson(GsonHelper.getGson());
        HttpDnsManager.getInstance().enable(false);
        if (true) {
            HttpDnsManager.getInstance().preLoadHttpDns(new String[]{
                    "frodo.douban.com",
                    "erebor.douban.com",
                    "api.douban.com"});
        }
    }
}