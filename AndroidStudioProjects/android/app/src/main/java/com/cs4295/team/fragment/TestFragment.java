package com.cs4295.team.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.cs4295.team.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestFragment extends Fragment {

    private String[] title = {
            "title 1","title2","title3"
    };
    private String[][] reply = {
            {"reply1.1","reply1.2"},
            {"reply2.1","reply2.2","reply2.3"},
            {"reply3.1","reply3.2","reply3.3","reply3.4","reply3.5"}
    };

    public TestFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_test, container, false);
        ExpandableListView lv = (ExpandableListView) rootview.findViewById(R.id.expandableListView);
        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
        for (int i = 0; i < title.length; i++) {
            Map<String, String> curGroupMap = new HashMap<String, String>();
            groupData.add(curGroupMap);
            curGroupMap.put("NAME", title[i]);

            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
            for (int j = 0; j <reply[i].length ; j++) {
                Map<String, String> curChildMap = new HashMap<String, String>();
                children.add(curChildMap);
                curChildMap.put("NAME", reply[i][j]);
            }
            childData.add(children);
        }
        SimpleExpandableListAdapter mAdapter = new SimpleExpandableListAdapter(

                this.getActivity(),
                groupData,
                R.layout.fragment_test,
                new String[]{"NAME"},
                new int[]{android.R.id.text1},
                childData,
                R.layout.fragment_test,
                new String[]{"NAME"},
                new int[]{android.R.id.text1}
        );
        return rootview;
    }


}
