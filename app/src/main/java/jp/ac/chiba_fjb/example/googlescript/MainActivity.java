package jp.ac.chiba_fjb.example.googlescript;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.services.script.model.Operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, dialog_newCreate.OnDialogButtonListener {

    private String mEditValue;
    private String mTextValue;
    private boolean textViewFlag = false;
    final String[] SCOPES = {
            "https://www.googleapis.com/auth/drive",
            "https://www.googleapis.com/auth/script.storage",
            "https://www.googleapis.com/auth/spreadsheets"};

    private GoogleScript mGoogleScript;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);


        ImageView edit =(ImageView)findViewById(R.id.imageView2);
        edit.setOnClickListener(this);
        ImageView copy =(ImageView)findViewById(R.id.imageView3);
        copy.setOnClickListener(this);
        ImageView add =(ImageView)findViewById(R.id.imageView4);
        add.setOnClickListener(this);
        ImageView syukei =(ImageView)findViewById(R.id.imageView5);
        syukei.setOnClickListener(this);
        ImageView saiten =(ImageView)findViewById(R.id.imageView6);
        saiten.setOnClickListener(this);



//    //解答名一覧取得
//        mGoogleScript = new GoogleScript(this,SCOPES);
//        //強制的にアカウントを切り替える場合
//        mGoogleScript.resetAccount();
//
//        //送信パラメータ
//        List<Object> params = new ArrayList<>();
//        params.add(null);
//
//        //ID,ファンクション名,結果コールバック
//        mGoogleScript.execute("1R--oj7xaQwzKf0Lk33pHyCh8hSGLG85nqUVQDVwM1TYrMqq61jWCEQro", "init",
//                params, new GoogleScript.ScriptListener() {
//                    @Override
//                    public void onExecuted(GoogleScript script, Operation op) {
//                        //   TextView textView = (TextView) findViewById(R.id.textMessage);
//
//                        if(op == null || op.getError() != null) {
//                            System.out.println("Script:error"); //       textView.append("Script結果:エラー\n");
//                        }else {
//                            //戻ってくる型は、スクリプト側の記述によって変わる
//                            Map<String, Object> r = op.getResponse();
//                            String s = (String)r.get("result");
//                            String[] ans = s.split(",", 0);
//                            int i =0;
//                            while(i<ans.length){
//                                setText(ans[i],ans[i+1]);
//                                i+=2;
//                            }
//                        }
//                    }
//                });

        }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.imageView4) {
            //Dialogフラグメント
            dialog_newCreate f = new dialog_newCreate();
            f.setOnDialogButtonListener(this);
            f.show(getSupportFragmentManager(), "");

        }else if(id == R.id.imageView2 && textViewFlag) {   //解答編集画面へ
            Bundle kaitou = new Bundle();
            kaitou.putString("textTag",mTextValue); //fragmentにタグを渡す
        }else if(id == R.id.imageView5) { //集計画面
        }else if(id == R.id.imageView6) { //スキャナー画面
            //以下、テキスト選択
        }else{
            LinearLayout ll = (LinearLayout) findViewById(R.id.layout1);
            int i, iCount;
            iCount = ll.getChildCount();
            for (i = 0; i < iCount; i++) {
                View view;
                String s;
                view = ll.getChildAt(i);
                s = v.getClass().getName();
                if (s.endsWith("TextView") == true) {
                    view.setBackgroundResource(R.drawable.border);
                    v.setBackgroundResource(R.drawable.tap);
                    textViewFlag = true;
                    mTextValue = (String)v.getTag();
                }
            }
        }
    }

    @Override
    public void onDialogButton(int value,String editValue) {
        if(value == 0){
            mEditValue = editValue;
           ansCreate(mEditValue);
        }else{
            Toast.makeText(this, "作成されませんでした", Toast.LENGTH_LONG).show();
        }
    }

    //解答追加
    public void setText(String textValue,String gdrive_fileId){
        LinearLayout layout = (LinearLayout)findViewById(R.id.layout1);
        TextView textView = new TextView(this);             //インスタンスの生成(引数はActivityのインスタンス)
        textView.setTag(gdrive_fileId);                      //GoogleDrive上のファイル区別用IDをタグとして設定
        textView.setText(""+textValue);                     //テキストの内容設定
        int paddingDpt = 30;  // dpを指定
        int paddingDpr = 10;  // dpを指定
        float scale = getResources().getDisplayMetrics().density; //画面のdensityを指定。
        int paddingPxt = (int) (paddingDpt * scale);
        int paddingPxr = (int) (paddingDpr * scale);
        textView.setPadding(paddingPxt,paddingPxr,paddingPxt,paddingPxr);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        textView.setBackgroundResource(R.drawable.border);  //背景色の設定
        System.out.println("タグ結果:"+textView.getTag());
        layout.addView(textView);
        textView.setOnClickListener(this);

    }

    protected void ansCreate (String textValue){
        String s = "初期値";


        mGoogleScript = new GoogleScript(this,SCOPES);
        //強制的にアカウントを切り替える場合
         mGoogleScript.resetAccount();

        //送信パラメータ
        List<Object> params = new ArrayList<>();
        params.add(textValue);

        //ID,ファンクション名,結果コールバック
        mGoogleScript.execute("1R--oj7xaQwzKf0Lk33pHyCh8hSGLG85nqUVQDVwM1TYrMqq61jWCEQro", "init",
                params, new GoogleScript.ScriptListener() {
                    @Override
                    public void onExecuted(GoogleScript script, Operation op) {
                     //   TextView textView = (TextView) findViewById(R.id.textMessage);

                        if(op == null || op.getError() != null) {
                           System.out.println("Script:error"); //       textView.append("Script結果:エラー\n");
                        }else {

                            //戻ってくる型は、スクリプト側の記述によって変わる
                            Map<String, Object> r = op.getResponse();
                           String s = (String)r.get("result");
                            System.out.println("Script:"+s);
                     //       textView.append("Script結果:"+ s+"\n");
                        }
                    }
                });
        setText(textValue,s);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGoogleScript.onActivityResult(requestCode, resultCode, data);
    }
}

