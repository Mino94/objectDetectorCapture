package keti.example.com.myapplication;

import android.content.ContentValues;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private TextView tv_outPut;

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 위젯에 대한 참조.
        tv_outPut = (TextView)findViewById(R.id.tv_outPut);
        image = (ImageView)findViewById(R.id.image);

//        imageVIewInput = (ImageView)findViewById(R.id.imageViewInput);

        //URL 설정.
        String url = "http://192.168.0.49:8000/machine/new";

        // AsyncTask를 통해 HttpURLConnection 수행.
        NetworkTask networkTask = new NetworkTask(url, null);
        networkTask.execute();
    }



    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        NetworkTask(String url, ContentValues values){
            this.url = url;
            this.values = values;
        }
        // Use a WeakReference to ensure the ImageView can be garbage collected

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            //progress bar를 보여주는 등등의 행위
        }
        // Decode image in background.

        @Override
        protected String doInBackground(Void... params) {
            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.


            return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
        }
        // Once complete, see if ImageView is still around and set bitmap.

        @Override
        protected void onPostExecute(String s) {
            // 통신이 완료되면 호출됩니다.
            // 결과에 따른 UI 수정 등은 여기서 합니다.
            super.onPostExecute(s);
////
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();

            //new String(Base64.decode(String.valueOf(tv_outPut), 0));
            //tv_outPut.setText(s);

            imageBytes = Base64.decode(s, Base64.DEFAULT);
            Log.e("imageByte", imageBytes.toString());
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            image.setImageBitmap(decodedImage);

            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();


        }
    }


}
