package com.example.healthfinder.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthfinder.R;
import com.example.healthfinder.entities.Consultation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DashboardFragment extends Fragment {

    private RecyclerView chatList;
    private TextView activeText;

    private List<Consultation> chats;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_dashboard, container, false);

        chatList = (RecyclerView) view.findViewById(R.id.chatList);
        activeText = (TextView) view.findViewById(R.id.activeText);
        chats = new ArrayList<Consultation>();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(chats.isEmpty()){
            chatList.setVisibility(View.GONE);
            activeText.setVisibility(View.VISIBLE);
            activeText.setText("No Active Chats");
        }
        else{
            chatList.setVisibility(View.VISIBLE);
            activeText.setVisibility(View.GONE);
        }
    }

}
