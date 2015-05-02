/*
 * Copyright (C) 2015 75py
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nagopy.android.mypkgs;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nagopy.android.mypkgs.constants.Constants;
import com.nagopy.android.mypkgs.constants.ShareType;
import com.nagopy.android.mypkgs.manager.SettingManager;
import com.nagopy.android.mypkgs.model.AppData;
import com.nagopy.android.mypkgs.model.event.ListItemSelectedEvent;
import com.nagopy.android.mypkgs.model.event.MultiChoiceModeListenerEvent;
import com.nagopy.android.mypkgs.model.loader.ApplicationLoader;
import com.nagopy.android.mypkgs.util.EventBus;
import com.nagopy.android.mypkgs.util.Logic;
import com.nagopy.android.mypkgs.view.adapter.ApplicationListAdapter;
import com.nagopy.android.mypkgs.view.adapter.SectionsPagerAdapter;
import com.squareup.otto.Subscribe;
import com.viewpagerindicator.PageIndicator;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnPageChange;


public class MainActivity extends AppCompatActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;

    @InjectView(R.id.pager)
    ViewPager mViewPager;

    @InjectView(R.id.indicator)
    PageIndicator pageIndicator;

    ActionMode actionMode;
    AppData reloadAppData;

    @InjectView(R.id.adView)
    AdView adView;

    @Inject
    SharedPreferences sp;

    @Inject
    SettingManager settingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        ((MyApplication) getApplicationContext()).getComponent().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getApplicationContext(),
                settingManager.getFilters());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        pageIndicator.setViewPager(mViewPager);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("F3D630FD4B16A430A0CB29123A096F71")
                .build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adView.resume();
    }

    @Override
    protected void onPause() {
        adView.pause();
        super.onPause();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        adView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (reloadAppData != null) {
            ApplicationListAdapter applicationListAdapter = getApplicationListAdapter();

            String packageName = reloadAppData.packageName.split(":")[0];
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = Logic.getApplicationInfo(packageManager, packageName);
            if (applicationInfo == null) {
                // パッケージが存在しない場合
                applicationListAdapter.removeApplication(reloadAppData);
            } else {
                reloadAppData.load(getApplicationContext(), applicationInfo);
            }
            applicationListAdapter.doFilter();

            reloadAppData = null;
        }

        if (sp.getBoolean(Constants.KEY_UPDATE_FLG, false)) {
            mViewPager.setCurrentItem(0);
            // 再度初期化
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getApplicationContext(),
                    settingManager.getFilters());
            mViewPager.setAdapter(mSectionsPagerAdapter);
            pageIndicator.notifyDataSetChanged();

            // フラグをオフに
            sp.edit().putBoolean(Constants.KEY_UPDATE_FLG, false).apply();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionMode != null) {
            // 非表示になっているメニューは何もしない
            return true;
        }

        ApplicationListAdapter listAdapter = getApplicationListAdapter();
        switch (item.getItemId()) {
            case R.id.action_share_label:
                sendIntent(getString(listAdapter.filterType.titleResourceId), ShareType.LABEL.makeShareString(listAdapter.filteredData));
                break;
            case R.id.action_share_package_name:
                sendIntent(getString(listAdapter.filterType.titleResourceId), ShareType.PACKAGE.makeShareString(listAdapter.filteredData));
                break;
            case R.id.action_share_label_and_package_name:
                sendIntent(getString(listAdapter.filterType.titleResourceId), ShareType.LABEL_AND_PACKAGE.makeShareString(listAdapter.filteredData));
                break;
            case R.id.action_share_csv:
                sendIntent(getString(listAdapter.filterType.titleResourceId), ShareType.CSV.makeShareString(listAdapter.filteredData));
                break;
            case R.id.action_reload:
                ApplicationLoader.getInstance().clearCache();
                getApplicationListFragment().showProgressBar().loadApplications();
                break;
            case R.id.action_preference:
                Intent preferenceActivity = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(preferenceActivity);
                break;
            case R.id.action_about:
                Intent aboutActivity = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(aboutActivity);
                break;
            case 0:
                // skip
                break;
            default:
                throw new RuntimeException("unknown id:" + item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * ViewPagerで表示中のFragmentを取得する.
     *
     * @return ApplicationListFragment
     */
    private ApplicationListFragment getApplicationListFragment() {
        PagerAdapter adapter = mViewPager.getAdapter();
        return (ApplicationListFragment) adapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
    }

    /**
     * ViewPagerで表示中のListFragmentのアダプターを取得する.
     *
     * @return ApplicationListAdapter
     */
    private ApplicationListAdapter getApplicationListAdapter() {
        ApplicationListFragment listFragment = getApplicationListFragment();
        return (ApplicationListAdapter) listFragment.getListAdapter();
    }

    /**
     * リストの要素が選択されたとき
     *
     * @param event ListItemSelectedEvent
     */
    @SuppressWarnings("unused")
    @Subscribe
    public void onListItemSelected(ListItemSelectedEvent event) {
        AppData appData = event.appData;
        String packageName = appData.packageName.split(":")[0];
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + packageName));
        reloadAppData = appData;
        startActivity(intent);
    }

    @OnPageChange(R.id.pager)
    public void onPageSelected(int position) {
        ApplicationListFragment fragment = (ApplicationListFragment) mSectionsPagerAdapter.getItem(position);
        ApplicationListAdapter adapter = (ApplicationListAdapter) fragment.getListAdapter();
        if (adapter != null) {
            adapter.doFilter();
        }

        if (actionMode != null) {
            actionMode.finish();
            actionMode = null;
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onCreateActionMode(MultiChoiceModeListenerEvent.CreateActionModeEvent event) {
        actionMode = event.actionMode;
        actionMode.getMenuInflater().inflate(R.menu.menu_main_multi, event.menu);
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onItemCheckedStateChanged(MultiChoiceModeListenerEvent.ItemCheckedStateChangedEvent event) {
        ActionMode mode = event.actionMode;
        int checkedItemCount = event.checkedItemCount;
        mode.getMenu().findItem(R.id.action_search).setVisible(checkedItemCount == 1);
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onActionItemClicked(MultiChoiceModeListenerEvent.ActionItemClickedEvent event) {
        int id = event.menuItem.getItemId();
        List<AppData> checkedItemList = event.checkedItemList;
        switch (id) {
            case R.id.action_search:
                if (checkedItemList.isEmpty()) {
                    throw new RuntimeException("Checked item is empty!");
                }
                AppData selected = checkedItemList.get(0);
                if (canLaunchImplicitIntent(Intent.ACTION_WEB_SEARCH)) {
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY, makeSearchQuery(selected));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(makeSearchUrl(selected)));
                    startActivity(intent);
                }
                break;
            case R.id.action_share_label:
                sendIntent(ShareType.LABEL.makeShareString(checkedItemList));
                break;
            case R.id.action_share_package_name:
                sendIntent(ShareType.PACKAGE.makeShareString(checkedItemList));
                break;
            case R.id.action_share_label_and_package_name:
                sendIntent(ShareType.LABEL_AND_PACKAGE.makeShareString(checkedItemList));
                break;
            case R.id.action_share_csv:
                sendIntent(ShareType.CSV.makeShareString(checkedItemList));
                break;
            case 0:
                // skip
                break;
            default:
                throw new RuntimeException("unknown id:" + id);
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onDestroyActionMode(MultiChoiceModeListenerEvent.DestroyActionModeEvent event) {
        actionMode = null;
    }

    void sendIntent(String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(Constants.MINE_TYPE_TEXT_PLAIN);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(intent);
    }

    void sendIntent(String text) {
        sendIntent(null, text);
    }

    boolean canLaunchImplicitIntent(@NonNull String action) {
        Intent intent = new Intent(action);
        return canLaunchImplicitIntent(intent);
    }

    boolean canLaunchImplicitIntent(@NonNull Intent intent) {
        PackageManager packageManager = getPackageManager();
        return !packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isEmpty();
    }

    String makeSearchQuery(@NonNull AppData appData) {
        return appData.label + '+' + appData.packageName;
    }

    String makeSearchUrl(@NonNull AppData appData) {
        return "http://www.google.com/search?q=" + makeSearchQuery(appData);
    }

}
