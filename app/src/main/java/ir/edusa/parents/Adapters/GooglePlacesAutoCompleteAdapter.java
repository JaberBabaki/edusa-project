package ir.edusa.parents.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.concurrent.TimeUnit;

import fontdroid.TextView;
import ir.edusa.parents.Interfaces.OnAutoCompleteStateChanged;
import ir.edusa.parents.Models.GooglePlaceModel;
import ir.edusa.parents.R;
import ir.edusa.parents.Utils.Log;

import static com.google.android.gms.location.places.AutocompleteFilter.TYPE_FILTER_ADDRESS;


public class GooglePlacesAutoCompleteAdapter extends ArrayAdapter<GooglePlaceModel> {


    private Typeface typeface;
    private GoogleApiClient googleApiClient;
    private OnAutoCompleteStateChanged onAutoCompleteStateChanged;
    private String queryText = "";

    private Filter mFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            return ((GooglePlaceModel) resultValue).getFullText();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {


            if (constraint != null && constraint.length() > 0) {
                onAutoCompleteStateChanged.onProgressBarVisibityChange(true);
                queryText = constraint.toString();
                Log.e("SEARCH", constraint.toString());
                displayGoogleAutoCompleteResult(constraint.toString());
            } else {
                queryText = "";
                onAutoCompleteStateChanged.onProgressBarVisibityChange(false);
            }

            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            notifyDataSetChanged();
        }
    };

    public GooglePlacesAutoCompleteAdapter(Context context, int textViewResourceId, GoogleApiClient googleApiClient, OnAutoCompleteStateChanged onAutoCompleteStateChanged) {
        super(context, textViewResourceId);
        this.typeface = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSansMobile_Medium.ttf");
        this.googleApiClient = googleApiClient;
        this.onAutoCompleteStateChanged = onAutoCompleteStateChanged;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;

        if (view == null || view.findViewById(R.id.imgIcon) == null) {
            view = layoutInflater.inflate(R.layout.item_text, null);
        }

        ImageView imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
        TextView txtLabel = (TextView) view.findViewById(R.id.txtLabel);
        imgIcon.setVisibility(View.GONE);
        txtLabel.setText(getItem(position).getFullText());
        txtLabel.setTypeface(typeface);

        return view;

    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public void displayGoogleAutoCompleteResult(String query) {

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("IR")
                .setTypeFilter(TYPE_FILTER_ADDRESS)
                .build();
        final String fQuery = query;
        LatLngBounds bounds = new LatLngBounds( new LatLng(26.403906, 45.896563 ), new LatLng( 39.234152, 61.453203 ) );
        Places.GeoDataApi.getAutocompletePredictions(googleApiClient, fQuery, bounds , typeFilter)
                .setResultCallback(
                        new ResultCallback<AutocompletePredictionBuffer>() {
                            @Override
                            public void onResult(AutocompletePredictionBuffer buffer) {
                                if (buffer == null)
                                    return;

                                if (buffer.getStatus().isSuccess() && fQuery.equals(queryText)) {
                                    for (AutocompletePrediction prediction : buffer) {
                                        add(new GooglePlaceModel(prediction.getFullText(null).toString(), prediction.getPlaceId(),
                                                prediction.getPrimaryText(null).toString(), prediction.getSecondaryText(null).toString()));
                                    }
                                }
                                onAutoCompleteStateChanged.onProgressBarVisibityChange(false);

                                //Prevent memory leak by releasing buffer
                                buffer.release();
                            }
                        }, 60, TimeUnit.SECONDS);
    }

}
