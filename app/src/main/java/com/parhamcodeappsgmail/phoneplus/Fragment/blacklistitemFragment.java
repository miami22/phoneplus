package com.parhamcodeappsgmail.phoneplus.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parhamcodeappsgmail.phoneplus.MyblacklistitemRecyclerViewAdapter;
import com.parhamcodeappsgmail.phoneplus.R;
import com.parhamcodeappsgmail.phoneplus.Tools.TinyDB;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class blacklistitemFragment extends Fragment {
    RecyclerView recyclerView;
    Context context;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    TinyDB tinyDB;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public blacklistitemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static blacklistitemFragment newInstance(int columnCount) {
        blacklistitemFragment fragment = new blacklistitemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tinyDB=new TinyDB(getActivity());

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blacklistitem_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
             context = view.getContext();
            recyclerView = (RecyclerView) view;
            createlist();
        }
        return view;
    } 

    void createlist(){
        ArrayList<String> list=tinyDB.getListString("blacklistname");
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new MyblacklistitemRecyclerViewAdapter(list, context));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint())
        {
            return;
        }
            createlist();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        //void onListFragmentInteraction(DummyItem item);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed())
        {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }}
}
