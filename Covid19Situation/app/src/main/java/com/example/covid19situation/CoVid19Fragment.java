package com.example.covid19situation;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.covid19situation.databinding.Covid19Binding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class CoVid19Fragment extends Fragment {
    Covid19Binding binding;
    String jsonString = "https://code.junookyo.xyz/api/ncov-moh/data.json?fbclid=IwAR1_-joqTilpqCRuCSSL4FJOy2raB0M54--s6a-5NBKwCly9n9h9-4wkH1Q";

    public static CoVid19Fragment newInstance() {

        Bundle args = new Bundle();

        CoVid19Fragment fragment = new CoVid19Fragment();
        fragment.setArguments( args );
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate( inflater, R.layout.covid19, container, false );
        new DoGetData().execute();
        return binding.getRoot();
    }

    public class DoGetData extends AsyncTask<Void, Void, Void> {
        String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.progess.setVisibility( View.VISIBLE );
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL( jsonString );
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                int byteCharacter;
                while ((byteCharacter = inputStream.read()) != -1) {
                    result += (char) byteCharacter;
                }
                Log.d( "TAG", "doInBackground: " + result );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute( aVoid );
            binding.progess.setVisibility( View.GONE );
            try {
                JSONObject jsonObject = new JSONObject( result );
                JSONObject jsonObject1 = jsonObject.getJSONObject( "data" );
                JSONObject jsonGlobal = jsonObject1.getJSONObject( "global" );
                JSONObject jsonVietNam = jsonObject1.getJSONObject( "vietnam" );
                int cases = jsonGlobal.getInt( "cases" );
                int deaths = jsonGlobal.getInt( "deaths" );
                int recovered = jsonGlobal.getInt( "recovered" );
                int cases1 = jsonVietNam.getInt( "cases" );
                int deaths1 = jsonVietNam.getInt( "deaths" );
                int recovered1 = jsonVietNam.getInt( "recovered" );
                binding.tvCasesGlobal.setText( formatNumber( cases ) );
                binding.tvCasesVietNam.setText( formatNumber( cases1 ) );
                binding.tvDeadthsGlobal.setText( formatNumber( deaths ) );
                binding.tvDeadthsVietNam.setText( formatNumber( deaths1 ) );
                binding.tvRecoveredGlobal.setText( formatNumber( recovered ) );
                binding.tvRecoveredVietNam.setText( formatNumber( recovered1 ) );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String formatNumber(int num) {
            String str = String.valueOf( num );
            String result = "";
            int index = str.length();
            if (index <= 3) return str;
            while (index > 3) {
                result = "," + str.substring( index - 3 ) + result;
                index -= 3;
                str = str.substring( 0, index );
            }
            result = str + result;
            return result;
        }
    }
}
