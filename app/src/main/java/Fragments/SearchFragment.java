package Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.equipme.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.List;

import Class.SearchEngine;


public class SearchFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner search_SPN_Categories;
    Button search_BTN_search;
    EditText search_LBL_free;
    ProgressBar search_PRB_progress;
    private SearchEngine search;
    private String spinner_position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        findViews(view);
        Context activity = getActivity().getBaseContext();
        presentCategories(activity);

        search_BTN_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_PRB_progress.setVisibility(View.VISIBLE);
                CollectionReference collection_reference = FirebaseFirestore.getInstance().collection("equipment");
                collection_reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        search = new SearchEngine(search_LBL_free.getText().toString(), spinner_position);
                        List found_list = search.startSearch(queryDocumentSnapshots);
                        search_PRB_progress.setVisibility(View.GONE);
                        if (found_list.isEmpty()) {
                            Toast.makeText(getContext(),"No items found",Toast.LENGTH_SHORT ).show();
                        } else {
                            FragmentTransaction fragment_transaction = getFragmentManager().beginTransaction();
                            Bundle bundle_object = new Bundle();
                            bundle_object.putString("json_list", new Gson().toJson(found_list));
                            SearchedListFragment frag = new SearchedListFragment();
                            frag.setArguments(bundle_object);
                            fragment_transaction.addToBackStack(null);
                            getFragmentManager().beginTransaction().replace(R.id.main_CNT_fragment, frag, "SEARCHED_LIST").commit();
                        /*Intent intent = new Intent(getActivity(), SearchListActivity.class);
                        intent.putExtra("json_list",new Gson().toJson(found_list));
                        startActivity(intent);*/
                        }
                    }
                });
            }
        });

        return view;
    }

    private void presentCategories(Context activity) {
        String category_item[] = getActivity().getResources().getStringArray(R.array.category);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(activity, android.R.layout.simple_spinner_dropdown_item, category_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        search_SPN_Categories.setAdapter(adapter);
        search_SPN_Categories.setOnItemSelectedListener(this);
    }

    private void findViews(View view) {
        search_SPN_Categories = view.findViewById(R.id.search_SPN_Categories);
        search_BTN_search = view.findViewById(R.id.search_BTN_search);
        search_LBL_free = view.findViewById(R.id.search_LBL_free);
        search_PRB_progress = view.findViewById(R.id.search_PRB_progress);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinner_position = parent.getItemAtPosition(position).toString();
        Log.d("Pttt", "Spinner selected: " + spinner_position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
