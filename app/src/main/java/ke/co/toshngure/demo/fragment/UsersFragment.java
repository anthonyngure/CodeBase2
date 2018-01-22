/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.demo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.jaychang.srv.SimpleRecyclerView;
import com.loopj.android.http.AsyncHttpClient;

import ke.co.toshngure.basecode.dataloading.DataLoadingConfig;
import ke.co.toshngure.basecode.dataloading.ModelCursor;
import ke.co.toshngure.basecode.dataloading.ModelListFragment;
import ke.co.toshngure.basecode.view.NetworkImage;
import ke.co.toshngure.demo.basecode.R;
import ke.co.toshngure.demo.cell.UserCell;
import ke.co.toshngure.demo.model.User;
import ke.co.toshngure.demo.network.Client;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends ModelListFragment<User, UserCell> {


    public UsersFragment() {
        // Required empty public constructor
    }

    public static UsersFragment newInstance() {
        Bundle args = new Bundle();
        UsersFragment fragment = new UsersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected UserCell onCreateCell(User item) {
        return new UserCell(item);
    }

    @Override
    protected DataLoadingConfig getDataLoadingConfig() {
        return new DataLoadingConfig()
                .setUrl("http://toshngure.kstars.co.ke/basecodeapp/public/api/v1/users")
                .setCacheEnabled(false)
                .setCursorsEnabled(true)
                .setDebugEnabled(true)
                .setPerPage(10);
    }

    @Override
    protected AsyncHttpClient getClient() {
        return Client.getInstance().getClient();
    }

    @Override
    protected Class<User> getModelClass() {
        return User.class;
    }

    @Override
    protected void setUpSimpleRecyclerView(SimpleRecyclerView simpleRecyclerView) {
        super.setUpSimpleRecyclerView(simpleRecyclerView);
        simpleRecyclerView.showDivider();
    }

    @Override
    protected void setUpTopView(FrameLayout topViewContainer) {
        super.setUpTopView(topViewContainer);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_users_top_view, null);
        NetworkImage topViewNI = view.findViewById(R.id.topViewNI);
        topViewContainer.addView(view);
        topViewNI.loadImageFromNetwork("https://lorempixel.com/400/400/cats/?33483");
    }


    /*@Override
    protected int getFreshLoadView() {
        return R.layout.custom_fresh_load_view;
    }*/

    @Override
    protected void setUpBottomView(FrameLayout bottomViewContainer) {
        super.setUpBottomView(bottomViewContainer);
        /*View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_users_top_view, null);
        BaseNetworkImage topViewNI = view.findViewById(R.id.topViewNI);
        bottomViewContainer.addView(view);
        topViewNI.loadImageFromNetwork("https://lorempixel.com/400/400/cats/?33483");*/
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_users, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_change_cursors:
                ModelCursor modelCursor = new ModelCursor();
                modelCursor.setAfter(30);
                modelCursor.setBefore(50);
                updateModelCursor(modelCursor);
                getSimpleRecyclerView().getAllCells().clear();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
