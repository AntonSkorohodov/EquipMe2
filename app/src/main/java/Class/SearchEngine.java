package Class;

import android.util.Log;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Utils.Category;

public class SearchEngine {
    private String free_field;
    private Category category = Category.camera;
    private boolean specified_search = true;

    public SearchEngine(String free_field,String category){
        this.free_field = free_field;
        setCategory(category);
        Log.d("Pttt","specified search = "+specified_search);
    }

    private void setCategory(String category) {
        this.category = Category.valueOf(category);
        if(category.equals(Category.any.toString()))
            this.specified_search = false;
    }

    //Search Logic: depending on if searching specified category or any runs thru all of equipment, match between search field to description field in lower case and returns new List of matching equipment.
    public List startSearch(QuerySnapshot queryDocumentSnapshots){
        List<HashMap<String,Object>> found_maps = new ArrayList<HashMap<String,Object>>();
        if(specified_search){
            for(QueryDocumentSnapshot query_document : queryDocumentSnapshots){
                Map<String,Object> temp = (HashMap<String, Object>)query_document.getData();
                if(temp.get("category").toString().equals(category.toString())){
                    if(temp.get("part_name").toString().toLowerCase().contains(free_field.toLowerCase()))
                    found_maps.add((HashMap<String, Object>) temp);
                }
            }
        }else{
            for(QueryDocumentSnapshot query_document : queryDocumentSnapshots){
                Map<String,Object> temp = (HashMap<String, Object>)query_document.getData();
                if(temp.get("part_name").toString().toLowerCase().contains(free_field.toLowerCase()))
                    found_maps.add((HashMap<String, Object>) temp);
            }
        }

        Log.d("Pttt", "Free field was:" + free_field);
        Log.d("Pttt","Number of matching equipment: " +found_maps.size());
        return found_maps;
    }


}
