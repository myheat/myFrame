package com.myheat.frame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author myheat
 * @version 2014年9月17日下午5:28:56
 */
public class MainActivity extends Activity  implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		((Button)findViewById(R.id.httpBtn)).setOnClickListener(this);
		((Button)findViewById(R.id.xListViewBtn)).setOnClickListener(this);
		((Button)findViewById(R.id.mutiDownBtn)).setOnClickListener(this);
		((Button)findViewById(R.id.mutiUploadBtn)).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
		case R.id.httpBtn:
			startActivity(new Intent(this,HttpTestActivity.class));
			break;
		case R.id.xListViewBtn:
			startActivity(new Intent(this,XListViewActivity.class));
			break;
		case R.id.mutiDownBtn:
			startActivity(new Intent(this,MutiDownActivity.class));
			break;
		case R.id.mutiUploadBtn:
			startActivity(new Intent(this,MutiUploadActivity.class));
			break;
		};
		
	}

	
}
