package com.douban.malfurion.sample

import android.content.pm.PackageManager
import android.support.multidex.MultiDexApplication
import com.douban.frodo.BaseProjectModuleApplication
import com.douban.frodo.FrodoVersionControl
import com.douban.frodo.baseproject.ModuleManager
import com.douban.frodo.fangorns.audio.AudioModuleApplication
import com.douban.frodo.httpdns.HttpDnsManager
import com.douban.frodo.search.FrodoSearchManager
import com.douban.frodo.search.SearchModuleApplication
import com.douban.frodo.subject.SubjectModuleApplication
import com.douban.frodo.subject.util.strategy.*
import com.douban.frodo.utils.AppContext
import com.douban.frodo.utils.BuildInfo
import com.douban.frodo.utils.GsonHelper
import com.douban.push.PushClient

/**
 * Created by linwei on 2017/6/4.
 */


class MainApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        setupAppContext()
        initModules()

//        GsonHelper.registerTypeAdapter(Entity<*>::class.java, DraftDeserializer())

        FrodoVersionControl.getInstance()
                .setVersionName("4.10.0").versionCode = 90
        //        SubjectFeatureSwitch.enableDoulist(false);
        //        SubjectFeatureSwitch.enableForumTopicEdit(false);
        //        SubjectFeatureSwitch.enableChat(false);
        //        SubjectFeatureSwitch.enableGroup(false);

        val ismMainProcess = PushClient.Helper.shouldInitApp(this)

        // gson
        ModuleManager.getInstance().setupGson(this, ismMainProcess)

        // init modules 'onBeforeApplicationCreate'
        ModuleManager.getInstance().onBeforeApplicationCreate(this, BuildConfig.DEBUG, true, ismMainProcess)

        // init modules 'setupNetworkIndependentModules'
        ModuleManager.getInstance().setupNetworkIndependentModules(this, BuildConfig.DEBUG, ismMainProcess)

        // init modules `setupNetworkDependentModules`
        ModuleManager.getInstance().setupNetworkDependentModules(this, BuildConfig.DEBUG, ismMainProcess)

        // setup httpdns
        setupHttpDns()


        // init modules `onAfterApplicationCreate`
        ModuleManager.getInstance().onAfterApplicationCreate(this, BuildConfig.DEBUG, true, ismMainProcess)

        FrodoSearchManager.getInstance()
                .register(MovieTvSearchStrategy())
                .register(BookSearchStrategy())
                .register(MusicSearchStrategy())
                .register(IlmenMixedSearchStrategy())
                .register(EventSearchStrategy())
                .register(DramaSearchStrategy())
                .register(CelebritySearchStrategy())

    }

    private fun initModules() {
        ModuleManager.getInstance()
                .registerModule(BaseProjectModuleApplication.getInstance())
                .registerModule(SubjectModuleApplication.getInstance())
                .registerModule(SearchModuleApplication.getInstance())
                .registerModule(AudioModuleApplication.getInstance())
    }

    private fun setupAppContext() {
        val info = BuildInfo()
        info.debug = BuildConfig.DEBUG
        info.appId = BuildConfig.APPLICATION_ID
        info.buildType = BuildConfig.BUILD_TYPE
        val pm = packageManager
        try {
            val pkgInfo = pm.getPackageInfo(packageName,
                    PackageManager.GET_META_DATA)
            info.pkgName = pkgInfo.packageName
            info.versionName = pkgInfo.versionName
            info.versionCode = pkgInfo.versionCode
        } catch (ignored: PackageManager.NameNotFoundException) {
        }

        info.market = "subject"
        AppContext.setBuildInfo(info)
    }

    /**
     * 初始化httpdns
     */
    private fun setupHttpDns() {
        HttpDnsManager.getInstance().gson = GsonHelper.getGson()
        HttpDnsManager.getInstance().enable(false)
        if (true) {
            HttpDnsManager.getInstance().preLoadHttpDns(arrayOf("frodo.douban.com", "erebor.douban.com", "api.douban.com"))
        }
    }
}