package com.init.d_installer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.res.AssetManager;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import android.widget.Toast; //Toast
import com.init.d_installer.R;

public class MainActivity extends Activity {

	Button button;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		addListenerOnBtnInstall();
		addListenerOnBtnUninstall();
	}
	
	public void copyAssets() { //Copies all files from /assets/ folder of .APK into /data/data/com.init.d/istaller/
	    AssetManager assetManager = getAssets();
	    String[] files = null;
	    try {
	        files = assetManager.list("");
	    } catch (IOException e) {
	    }
	    for(String filename : files) {
	        InputStream in = null;
	        OutputStream out = null;
	        try {
	          in = assetManager.open(filename); //Ignore warning about hardcoding to /data/data/.
	          out = new FileOutputStream("/data/data/" + this.getPackageName() + "/" + filename);
	          copyFile(in, out);
	          in.close();
	          in = null;
	          out.flush();
	          out.close();
	          out = null;
	        } catch(IOException e) {
	        }       
	    }
	}
	private void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}

	public void addListenerOnBtnInstall() { //This is the Install Button.

		button = (Button) findViewById(R.id.btnInstall);

		button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) { // On Button Click Do:
				
			
				
				try{
				Process p = Runtime.getRuntime().exec("su"); //Asking Root Permission
				     
				String mount = "mount -ro remount,rw /system";
				String mkdir = "mkdir /system/etc/init.d/";
				String sysinit = "cat /data/data/com.init.d_installer/sysinit > /system/bin/sysinit";
				String install = "cat /data/data/com.init.d_installer/install-recovery.sh > /system/etc/install-recovery.sh";
				String remove = "rm /system/xbin/run-parts";
				String symlink = "ln -s /system/xbin/busybox /system/xbin/run-parts";
				String busybox = "cat /data/data/com.init.d_installer/busybox > /system/xbin/busybox";
				String permissions1 = "chmod 6755 /system/bin/sysinit";
				String permissions2 = "chmod 6755 /system/xbin/busybox";
				String permissions3 = "chmod 6755 /system/etc/install-recovery.sh";
				
				
				DataOutputStream os = new DataOutputStream(p.getOutputStream());
				os.writeBytes(mount + "\n"); //os.writeBytes is like "adb shell"
				os.writeBytes(mkdir + "\n");
   				os.writeBytes(sysinit + "\n");
				os.writeBytes(install + "\n");
				os.writeBytes(remove + "\n");
				os.writeBytes(permissions2 + "\n");
				os.writeBytes(symlink + "\n");
				os.writeBytes(busybox + "\n");
				os.writeBytes(permissions1 + "\n");
				os.writeBytes(permissions2 + "\n");
				os.writeBytes(permissions3 + "\n");
				Toast.makeText(MainActivity.this, "Init.d Installed! Rebooting.", Toast.LENGTH_LONG).show();
				os.writeBytes("reboot\n"); //Reboots the phone.
				os.writeBytes("exit\n");
				os.flush();			
				
				
				} catch (IOException e) { 
					 
				}finally{ 
					
				}
			}
			
			
			
		});

}
	
	public void addListenerOnBtnUninstall() { //This is the Uninstall Button.

		button = (Button) findViewById(R.id.btnUninstall);

		button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) { // On Button Click Do:
				
			
				
				try{
				Process p = Runtime.getRuntime().exec("su"); //Asking Root Permission
				     
				
				String mount = "mount -ro remount,rw /system";
				String mkdir = "rmdir /system/etc/init.d/";
				String sysinit = "rm /system/bin/sysinit";
				String install = "rm /system/etc/install-recovery.sh";
				String remove = "rm /system/xbin/run-parts";
				//We do not remove /system/xbin/busybox because it might be used by another app
				
				
				DataOutputStream os = new DataOutputStream(p.getOutputStream());
				os.writeBytes(mount + "\n");	//os.writeBytes is like "adb shell"
				os.writeBytes(mkdir + "\n");
   				os.writeBytes(sysinit + "\n");
				os.writeBytes(install + "\n");
				os.writeBytes(remove + "\n");
				Toast.makeText(MainActivity.this, "Init.d Uninstalled! Rebooting.", Toast.LENGTH_LONG).show();
				os.writeBytes("reboot\n"); //Reboots the phone.
				os.writeBytes("exit\n");
				os.flush();			
				

				} catch (IOException e) { 
					 
				}finally{ 
					
				}
			}
			
			
			
		});

}

}

