package com.example.weibodemo;


import java.io.BufferedOutputStream;
import java.io.Flushable;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.content.SharedPreferences;

public class MainActivity extends Activity {
	private Button mButton;
	private WeiboAuth mWeiboAuth;
	private Oauth2AccessToken mAccessToken;
	private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
	private void initView() {
		sp=getSharedPreferences("com_weibo_sdk_android", Activity.MODE_PRIVATE);
		String uid=sp.getString("uid",null);
		if(uid!=null){
			System.out.println(uid);
        	new Thread(){
				@Override
				public void run() {
					BufferedOutputStream out=null;
					InputStream in=null;
					try {
		        		StringBuffer buffer=new StringBuffer();
		        		buffer.append("access_token=2.00zTukcCVroUMD69e018e22fLzJjPD");
		        		buffer.append("&uid=2404946643");
						URL url=new URL("https://api.weibo.com/2/users/show.json?"+buffer.toString());
						System.out.println("https://api.weibo.com/2/users/show.json?"+buffer.toString());
						
						HttpURLConnection conn=(HttpURLConnection) url.openConnection();
						System.out.println(conn.getResponseCode());
						in=conn.getInputStream();
						out=new BufferedOutputStream(System.out);
						int len =0;
						byte[] size=new byte[1024];
						while((len=in.read(size))!=-1){
							//System.out.write(size, 0, len);
							out.write(size, 0,len);
							out.flush();
						}
						in.close();
						out.close();
					} catch (Exception e) {

						e.printStackTrace();
					}
					
					super.run();
				}
				
        		
        	}.start();
        	
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		mButton=(Button) findViewById(R.id.bt_auth);
		mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mWeiboAuth.anthorize(new AuthListener());	
			}
		});
		
		 mAccessToken = AccessTokenKeeper.readAccessToken(this);
	
	}
	
	
	
	 class AuthListener implements WeiboAuthListener {
	        
	        @Override
	        public void onComplete(Bundle values) {
	            // 从 Bundle 中解析 Token
	            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
	            if (mAccessToken.isSessionValid()) {

	                // 保存 Token 到 SharedPreferences
	                AccessTokenKeeper.writeAccessToken(MainActivity.this, mAccessToken);
	                
	                
	                
	               
	            } else {
	                // 以下几种情况，您会收到 Code：
	                // 1. 当您未在平台上注册的应用程序的包名与签名时；
	                // 2. 当您注册的应用程序包名与签名不正确时；
	                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
	                String code = values.getString("code");
	             
	            }
	        }

	        @Override
	        public void onCancel() {
	            
	        }

	        @Override
	        public void onWeiboException(WeiboException e) {
	            Toast.makeText(MainActivity.this, 
	                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
	        }
	    }

	
    
    
}
