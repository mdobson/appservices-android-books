package me.mdobs.books;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.apigee.sdk.ApigeeClient;
import com.apigee.sdk.data.client.callbacks.ApiResponseCallback;
import com.apigee.sdk.data.client.callbacks.ClientAsyncTask;
import com.apigee.sdk.data.client.entities.Entity;
import com.apigee.sdk.data.client.response.ApiResponse;

public class BooksListViewActivity extends Activity {

	public ApigeeClient client;
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list_view);
        getBooks();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	getBooks();
    	
    }


	public void getBooks() {
		final ArrayList<String> titles;
        titles = new ArrayList<String>();
        
        final ListView listView = (ListView) findViewById(R.id.listview);
        this.client = new ApigeeClient("mdobson","sandbox", this);
        final BooksArrayAdapter adapter = new BooksArrayAdapter(this, android.R.layout.simple_list_item_1, titles);
        adapter.setNotifyOnChange(true);
        listView.setAdapter(adapter);
        this.getEntitiesAsync("books", "select *", new ApiResponseCallback(){
        	
        	@Override
        	public void onException(Exception ex) {
        		Log.i("Error", ex.getMessage());
        	}
        	
        	@Override
        	public void onResponse(ApiResponse response) {
        		List<Entity> books = response.getEntities();
                
                for (int j = 0; j < books.size(); j++) {
                    Entity book = books.get(j);
                    String bookTitle = book.getStringProperty("title");
                    adapter.add(bookTitle);
                }
                adapter.notifyDataSetChanged();
        	}
        	
        });
	}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) { 
    	case R.id.action_add_book:
    		openBookForm();
    		return true;
    	default:
    		return false;
    	}
    	
    }
    
    public void openBookForm(){
    	Intent intent = new Intent(this, NewBookActivity.class);
    	this.startActivity(intent);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.books_list_view, menu);
        return true;
    }
    
    public void getEntitiesAsync(final String type, final String query, final ApiResponseCallback callback) {
    	final ApigeeClient apiClient = this.client;
    	(new ClientAsyncTask<ApiResponse>(callback){
    		@Override
    		public ApiResponse doTask(){
    			return apiClient.getDataClient().getEntities(type, query);
    		}
    	}).execute();
    }

    private class BooksArrayAdapter extends ArrayAdapter<String> {

        public BooksArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
        }
    }
}
