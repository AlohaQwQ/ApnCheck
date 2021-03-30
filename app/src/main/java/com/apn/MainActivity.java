package com.apn;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.umcrash.UMCrash;
import com.wp.imsiauthsdk.AccessCodeListener;
import com.wp.imsiauthsdk.AuthHandler;
import com.wp.imsiauthsdk.Constants;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQ_ID = 22;

    // permission WRITE_EXTERNAL_STORAGE is not mandatory for Agora RTC SDK, just incase if you wanna save logs to external sdcard
/*    private static final String[] REQUESTED_PERMISSIONS = {Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.CHANGE_WIFI_STATE,Manifest.permission.READ_PHONE_STATE};*/

    private AuthHandler authHandler = null;
    private String mobileNetwork = "";
    private String accessCode = "";
    private TextView text_view1;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private RadioButton radioButtonCm;
    private RadioButton radioButtonCu;
    private RadioButton radioButtonCt;

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;

    private String choose = "中国移动";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("本机核验演示Demo");

        text_view1 = (TextView) findViewById(R.id.textView1);
        /**
         * @author Aloha
         * @date 2021/2/3 22:10
         * @explain 长按复制
         */
        text_view1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!TextUtils.isEmpty(text_view1.getText().toString())){
                    copyCodeToClipboard(text_view1.getText().toString());
                    Toast.makeText(MainActivity.this, "已复制",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        radioGroup = (RadioGroup)findViewById(R.id.cmRadioGroup);
        radioButton = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (radioButtonCm.isChecked() || radioButtonCu.isChecked() || radioButtonCt.isChecked()) {
                    if (id == radioButtonCm.getId()) {
                        choose = "中国移动";
                        editText3.setEnabled(true);
                        editText3.setFocusableInTouchMode(true);
                        editText3.setClickable(true);
                    } else if (id == radioButtonCu.getId()) {
                        choose = "中国联通";
                        editText3.setEnabled(false);
                        editText3.setFocusableInTouchMode(false);
                        editText3.setClickable(false);
                    } else if (id == radioButtonCt.getId()) {
                        choose = "中国电信";
                        editText3.setEnabled(false);
                        editText3.setFocusableInTouchMode(false);
                        editText3.setClickable(false);
                    }
                }
            }
        });

        radioButtonCm = (RadioButton)findViewById(R.id.cmRadioButton);
        radioButtonCu = (RadioButton)findViewById(R.id.cuRadioButton);
        radioButtonCt = (RadioButton)findViewById(R.id.ctRadioButton);


        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String editString = editable.toString().trim();
                if(!TextUtils.isEmpty(editString)){

                }
            }
        });

        /**
         * @author Aloha
         * @date 2021/1/30 23:16
         * @explain 友盟
         */
        UMConfigure.init(this, "60157579f1eb4f3f9b7db568", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        UMConfigure.setLogEnabled(true);

        /*if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1],PERMISSION_REQ_ID) &&
                        checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)){
        }*/
        authHandler = AuthHandler.getInstance(MainActivity.this);
    }

    /*public boolean checkSelfPermission(String permission, int requestCode) {
        Log.i("cd1", "checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,REQUESTED_PERMISSIONS,requestCode);
            return false;
        }
        return true;
    }*/

    public void apnCheck(View v){     // 按钮对应的 onclick 响应
        //TextView text_view = (TextView) findViewById(R.id.textView);
    }

    public void preAuth(View v){     // 按钮对应的 onclick 响应
        text_view1.setText("");

        if(radioButton!=null){
            //String text = radioButton.getText().toString();
            if("中国移动".equals(choose)){
                mobileNetwork="CM";
                if(!TextUtils.isEmpty(editText1.getText().toString().trim()))
                    Constants.CM_APP_ID = editText1.getText().toString().trim();
                if(!TextUtils.isEmpty(editText2.getText().toString().trim()))
                    Constants.CM_APP_KEY = editText2.getText().toString().trim();
                if(!TextUtils.isEmpty(editText3.getText().toString().trim()))
                    Constants.CM_CUSTID = editText3.getText().toString().trim();
            }
            if("中国联通".equals(choose)){
                mobileNetwork="CU";
                if(!TextUtils.isEmpty(editText1.getText().toString().trim()))
                    Constants.CU_APP_ID = editText1.getText().toString().trim();
                if(!TextUtils.isEmpty(editText2.getText().toString().trim()))
                    Constants.CU_APP_KEY = editText2.getText().toString().trim();
            }
            if("中国电信".equals(choose)){
                mobileNetwork="CT";
                if(!TextUtils.isEmpty(editText1.getText().toString().trim()))
                    Constants.CT_APP_ID = editText1.getText().toString().trim();
                if(!TextUtils.isEmpty(editText2.getText().toString().trim())){
                    if(editText2.getText().toString().trim().length()<18){
                        Toast.makeText(MainActivity.this, "CT_APP_KEY参数小于18位",Toast.LENGTH_SHORT).show();
                    } else {
                        Constants.CT_APP_KEY = editText2.getText().toString().trim();
                    }
                }
            }
            AccessCodeListener listener = new AccessCodeListener() {
                @Override
                public void onRequestAccessCodeComplete(Map<String, String> resultMap) {
                    text_view1.setText("code : " + resultMap.get("code")+"\n accessCode : " + resultMap.get("accessCode")+"\n msg : " + resultMap.get("msg"));
                    accessCode = resultMap.get("accessCode");
                    //Toast.makeText(MainActivity.this, "accessCode :" + resultMap.get("accessCode"),Toast.LENGTH_SHORT).show();
                }
            };
            //authHandler.requestAccessCode(mobileNetwork,listener);

            try {
                authHandler.requestAccessCode(mobileNetwork,listener);
            } catch (Exception e) {
                //MobclickAgent.reportError(MainActivity.this, (Throwable) e);
                //MobclickAgent.reportError(MainActivity.this, e.toString());
                UMCrash.generateCustomLog(e, "UmengException");
                e.printStackTrace();
            }
            //NetWorkUtil.getNetState(MainActivity.this,NetWorkUtil.PREAUTH,null);
            //text_view1.setText(NetWorkUtil.PREAUTH_RESULT);
        }
    }

    public void validate(View v){     // 按钮对应的 onclick 响应
        final TextView text_view2 = (TextView) findViewById(R.id.textView2);
        EditText edit_text = (EditText) findViewById(R.id.mobileText);
        text_view2.setText("");
        //NetWorkUtil.getNetState(MainActivity.this,NetWorkUtil.VALIDATE,edit_text.getText().toString(),mobileNetwork,accessCode,text_view2);
        //text_view2.setText(NetWorkUtil.VALIDATE_RESULT);
    }

    /**
     * @author Aloha
     * @date 2021/2/3 22:01
     * @explain 复制密钥到剪贴板
     */
    private void copyCodeToClipboard(String code){
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", code);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }

}
