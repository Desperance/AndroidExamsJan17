package com.example.user.androidexamsjan17;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MerchantFragment extends Fragment {

    MerchantAdapter merchantAdapter;
    private ArrayAdapter<String> mExamplesListAdapter;

    public MerchantFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
        fetchMerchants();
    }

    private void fetchMerchants(){
        FetchMerchantsTask fetchMerchantsTask = new FetchMerchantsTask(merchantAdapter);
        fetchMerchantsTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        mExamplesListAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.list_item_merchant,
                        R.id.list_item_merchant_textview, new ArrayList<String>());

        ListView examplesListView = (ListView) rootView.findViewById(R.id.listview_merchants);
        examplesListView.setAdapter(mExamplesListAdapter);




        return rootView;
    }

    public class FetchMerchantsTask extends AsyncTask<String,Void,ArrayList<Merchant>> {

        private final String LOG_TAG = FetchMerchantsTask.class.getSimpleName();
        private MerchantAdapter merchantAdapter;
        public static final String YUMMY_BASE_DOMAIN = "http://dev.savecash.gr:3000";

        public FetchMerchantsTask( MerchantAdapter merchantAdapter){
            this.merchantAdapter = merchantAdapter;
        }

        private ArrayList<Merchant> getMerchantsFromJson(String merchantJsonStr) throws JSONException {
            ArrayList<Merchant> merchants = new ArrayList<>();
            final String YUMMY_merchantCategory = "merchantCategory";
            final String YUMMY_name = "name";
            final String YUMMY_aggregateRating = "aggregateRating";
            final String YUMMY_ratingValue= "ratingValue";
            final String YUMMY_legalName= "legalName";
            final String YUMMY_contactpoint = "contactPoint";
            final String YUMMy_streetAddress = "streetAddress";

            int numOfResults = 10; //gia emfanhsh 10 apotelesmatwn

            try{
                JSONArray merchantsArray = new JSONArray(merchantJsonStr);
                //.....
                for (int i =0; i < numOfResults; i++){
                    String legalname;
                    String name;
                    String ratingValue;
                    String categoryname;
                    String aggregateRating;
                    String contantPoint;


                    JSONObject resultMerchant = merchantsArray.getJSONObject(i);
                    legalname = resultMerchant.getString(YUMMY_legalName);

                    JSONObject merchantCatergoryObject = resultMerchant.getJSONObject(YUMMY_merchantCategory);
                    categoryname = merchantCatergoryObject.getString(YUMMY_name);

                    JSONObject aggregateRatingObject = resultMerchant.getJSONObject(YUMMY_aggregateRating);
                    aggregateRating = merchantCatergoryObject.getString(YUMMY_ratingValue);

                    JSONObject contantPointObject = resultMerchant.getJSONObject(YUMMY_contactpoint);
                    contantPoint = merchantCatergoryObject.getString(YUMMy_streetAddress);

                    Merchant iMerchant = new Merchant();

                    iMerchant.setAddress(contantPoint);
                    iMerchant.setCategory(categoryname);
                    iMerchant.setLegalName(legalname);
                    iMerchant.setReview(aggregateRating);

                    merchants.add(iMerchant);
                }





                //......
                Log.d(LOG_TAG, "Merchant Fetching Complete. " + merchants.size() + "merchants inserted");
                return  merchants;
            }catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
                return  merchants;
            }
        }

        @Override
        protected ArrayList<Merchant> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String merchantJsonStr = null;

            try {
                final String YUMMY_MERCHANTS_URL =
                        "/Merchant/index.json?$orderby=dateCreated%20desc";

                Uri builtUri = Uri.parse(YUMMY_BASE_DOMAIN+YUMMY_MERCHANTS_URL);

                URL url = new URL(builtUri.toString());

                // Create the request to Yummy Wallet server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                merchantJsonStr = buffer.toString();
                return  getMerchantsFromJson(merchantJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Merchant> merchants) {
            if(merchants.size() > 0){
                this.merchantAdapter.clear();
                //.....

                int duration = Toast.LENGTH_LONG;


                /*for (ArrayList<Merchant> amMerch : merchants) {
                    merchantAdapter.add(amMerch);
                }*/
            }
        }
    }
}
