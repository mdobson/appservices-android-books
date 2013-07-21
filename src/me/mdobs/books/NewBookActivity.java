package me.mdobs.books;

import java.util.HashMap;
import java.util.Map;

import com.apigee.sdk.ApigeeClient;
import com.apigee.sdk.data.client.callbacks.ApiResponseCallback;
import com.apigee.sdk.data.client.response.ApiResponse;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Entity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class NewBookActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_book);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_book, menu);
		return true;
	}

	public void createBook(View view){
		ApigeeClient client;
		client = new ApigeeClient("mdobson", "sandbox", this);
			
		
		EditText title = (EditText)findViewById(R.id.title);
		String bookTitle = title.getText().toString();
		
		EditText author = (EditText)findViewById(R.id.author);
		String bookAuthor = author.getText().toString();
		
		Map<String, Object> entity = new HashMap<String,Object>();
		entity.put("type", "books");
		entity.put("author", bookAuthor);
		entity.put("title", bookTitle);
		
		client.getDataClient().createEntityAsync(entity, new ApiResponseCallback(){
			@Override
			public void onException(Exception ex) {
				Log.i("NewBook", ex.getMessage());
			}
			
			@Override
			public void onResponse(ApiResponse response) {
				finish();
				
			}
		});
		
		
	}
	
}
