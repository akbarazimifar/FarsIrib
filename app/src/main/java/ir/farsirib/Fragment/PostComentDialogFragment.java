package ir.farsirib.Fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.farsirib.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostComentDialogFragment extends DialogFragment {

    TextInputLayout comment_layout;
    EditText name_text;
    EditText email_text;
    EditText tel_text;
    EditText web_text;
    EditText comment_text;
    Button submit_btn;

    JSONObject new_comment;

    public PostComentDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_post_comment, container, false);

        //getDialog().setTitle("Simple Dialog");
        name_text = rootView.findViewById(R.id.name_text);
        name_text.requestFocus();

        comment_layout = rootView.findViewById(R.id.comment_layout);
        comment_text = rootView.findViewById(R.id.comment_text);

        email_text = rootView.findViewById(R.id.email_text);
        tel_text = rootView.findViewById(R.id.tel_text);
        web_text = rootView.findViewById(R.id.web_text);

        submit_btn = rootView.findViewById(R.id.submit_bt);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "yooohoooooooooo" , Toast.LENGTH_SHORT).show();
                Boolean is_validate=true;

                if(comment_text.getText().toString().trim().length()>=5)
                {

                    comment_layout.setErrorEnabled(false);


                }else
                {

                    comment_layout.setError("متن ارسالی نمی تواند خالی باشد");
                    comment_layout.setErrorEnabled(true);

                    is_validate=false;

                }

                if (is_validate) {


                    new_comment = new JSONObject();

                    try {
                        new_comment.put("name",name_text.getText().toString().trim());
                        new_comment.put("email",email_text.getText().toString().trim());
                        new_comment.put("tel_num",tel_text.getText().toString().trim());
                        new_comment.put("web_address",web_text.getText().toString().trim());
                        new_comment.put("comment_txt",comment_text.getText().toString().trim());

                        new_comment.put("command","new_comment");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new send_comment().execute();
                }
                else
                {
                    Toast.makeText(getContext(),"خطا در ورود اطلاعات",Toast.LENGTH_SHORT).show();
                }
            }
        });


        return rootView;
    }

    public class send_comment extends AsyncTask<Void,Void,String>
    {

        ProgressDialog pd=new ProgressDialog(getContext());

        protected  void onPreExecute()
        {
            super.onPreExecute();

            pd.setMessage("در حال ارسال نظر");
            pd.show();


        }

        @Override
        protected String doInBackground(Void... voids) {


            ArrayList<NameValuePair> namevaluepairs=new ArrayList<NameValuePair>();

            namevaluepairs.add(new BasicNameValuePair("myjson",new_comment.toString()));

            try
            {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost=new HttpPost("http://www.shahreraz.com/mob/Farsirib/webservice/command.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));
                HttpResponse httpresponse=httpclient.execute(httppost);

                String response= EntityUtils.toString(httpresponse.getEntity());

                if (response.startsWith("<farsirib_app>")&&response.endsWith("</farsirib_app>")) {//response is valid

                    response = response.replace("<farsirib_app>", "").replace("</farsirib_app>", "");


                    if(response.trim().equals("ok"))
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getContext(),"ارسال نظر با موفقیت ارسال شد",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getActivity().getBaseContext(),"خطا در ارسال نظر",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getActivity().getBaseContext(),"خطا در ارسال نظر",Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }catch(Exception e)
            {
                e.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getActivity().getBaseContext(),"خطا در ارسال نظر",Toast.LENGTH_SHORT).show();


                    }
                });
            }
            return null;
        }

        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            pd.hide();
            pd.dismiss();

        }
    }

}
