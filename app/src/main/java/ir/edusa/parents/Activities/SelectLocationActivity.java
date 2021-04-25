package ir.edusa.parents.Activities;

import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONObject;

import ir.edusa.parents.Adapters.GooglePlacesAutoCompleteAdapter;
import ir.edusa.parents.Dialogs.ErrorDialog;
import ir.edusa.parents.Dialogs.ShowLocationDialog;
import ir.edusa.parents.Helpers.HashHelper;
import ir.edusa.parents.Helpers.Toast;
import ir.edusa.parents.Helpers.ValidatorHelper;
import ir.edusa.parents.Interfaces.OnAutoCompleteStateChanged;
import ir.edusa.parents.Managers.UserManager;
import ir.edusa.parents.Models.GooglePlaceModel;
import ir.edusa.parents.Models.Passenger;
import ir.edusa.parents.Models.SchoolService;
import ir.edusa.parents.Network.ApiCallerClass;
import ir.edusa.parents.Network.Requests.SetAddressLocationRequest;
import ir.edusa.parents.Network.Responses.AbstractResponse;
import ir.edusa.parents.R;
import ir.edusa.parents.Utils.InstantAutoComplete;
import ir.edusa.parents.Utils.Log;


public class SelectLocationActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private InstantAutoComplete autoCompleteTextView;
    private ViewGroup lytAutoCompleteTextView;
    private ProgressWheel progressWheel;
    private MapView mapView;
    private ViewGroup lytAccept;
    private GoogleMap googleMap;

    @Override
    protected String getPageTitle() {
        return "موقعیت جغرافیایی";
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        if("0".equals(UserManager.getDeterminLocation())) {
            final ShowLocationDialog errorDialog = new ShowLocationDialog(SelectLocationActivity.this);
            errorDialog.show();
            errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    errorDialog.dismiss();
                    UserManager.setDeterminLocation("1");
                }
            });
        }

        lytAccept = (ViewGroup) findViewById(R.id.lytAccept);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap gm) {
                googleMap = gm;
                googleMap.setMyLocationEnabled(true);
                Passenger passenger = UserManager.getPassenger();
                Log.i("POS","getPos"+passenger.getAddress_Lat());
                if (passenger.getAddress_Lat() > 0 && passenger.getAddress_Lon() > 0) {
                    googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.student))
                      .position(new LatLng(passenger.getAddress_Lat(), passenger.getAddress_Lon())).title("محل فعلی تعیین شده دانش آموز"));
                    navigateToPosition(passenger.getAddress_Lat(), passenger.getAddress_Lon());
                } else {
                    Log.i("POS","getPos");
                    double lat=35.689197;
                    double lon=51.388974;

                    navigateToPosition(lat,lon);
                    //navigateToMyPosition(lat,lon);
                }
                initializeAutocomplete();
                lytAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        LatLng latLng = googleMap.getCameraPosition().target;
                        setAddressLocation(latLng.latitude, latLng.longitude);
                    }
                });

            }
        });

    }

    public void navigateToMyPosition() {
        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);
        if (myLocation != null) {
            navigateToPosition(myLocation.getLatitude(), myLocation.getLongitude());
        }
    }

    public void setAddressLocation(final double lat, final double lon) {
        Passenger passenger = UserManager.getPassenger();
        String driverCode = "";
        for (SchoolService service : passenger.getServices()) {
            driverCode += service.getDriver_Code() + ",";
        }
        if (ValidatorHelper.isValidString(driverCode)) {
            driverCode = driverCode.substring(0, driverCode.length() - 1);
        }
        ApiCallerClass.with(SelectLocationActivity.this).hasLoading(true).call(
                new SetAddressLocationRequest(HashHelper.Calculate_Password(UserManager.getPrivateApiKey(), UserManager.getToken()),
                        driverCode, UserManager.getPassenger().getCode(), UserManager.getDeviceCode(), lat, lon), new ApiCallerClass.CallerOnResponse() {
                    @Override
                    public void onResponse(AbstractResponse result, JSONObject rawResponse) {
                        Passenger passenger = UserManager.getPassenger();
                        passenger.setAddress_Lat(lat);
                        passenger.setAddress_Lon(lon);
                        UserManager.setPassenger(passenger);
                        Toast.makeText(SelectLocationActivity.this, "موقیت مشخض شده با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
                        finishWithAnimation();
                    }

                    @Override
                    public void onError(String message, int code) {
                        ErrorDialog errorDialog = new ErrorDialog(SelectLocationActivity.this, message);
                        errorDialog.show();
                    }
                }, "");
    }

    public void initializeAutocomplete() {
        autoCompleteTextView = (InstantAutoComplete) findViewById(R.id.autoCompleteTextView);
        lytAutoCompleteTextView = (ViewGroup) findViewById(R.id.lytAutoCompleteTextView);
        progressWheel = (ProgressWheel) findViewById(R.id.progressWheel);

        lytAutoCompleteTextView.setVisibility(View.GONE);
        final GoogleApiClient placesApiClient = new GoogleApiClient.Builder(this, this, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        placesApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                lytAutoCompleteTextView.setVisibility(View.VISIBLE);

                autoCompleteTextView.setAdapter(new GooglePlacesAutoCompleteAdapter(SelectLocationActivity.this, R.id.autoCompleteTextView, placesApiClient,
                        new OnAutoCompleteStateChanged() {
                            @Override
                            public void onProgressBarVisibityChange(final boolean visible) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (visible) {
                                            progressWheel.setVisibility(View.VISIBLE);
                                        } else {
                                            progressWheel.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            }
                        }));
                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        GooglePlaceModel item = (GooglePlaceModel) parent.getItemAtPosition(position);
                        Places.GeoDataApi.getPlaceById(placesApiClient, item.getPlaceId()).setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(@NonNull PlaceBuffer places) {
                                Place place = places.get(0);

                                if (place == null) {
                                    return;
                                }
                                LatLng latLng = place.getLatLng();

                                navigateToPosition(latLng.latitude, latLng.longitude);
                                hideKeyboard();
                                places.release();
                            }
                        });
                    }
                });

            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        });

        //Connect
        placesApiClient.connect();

    }

    public void navigateToPosition(final double lat, final double lon) {
        if (googleMap == null) {
            return;
        }
        CameraPosition position1 = CameraPosition.builder()
                .target(new LatLng(lat, lon))
                .zoom(15)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position1));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}