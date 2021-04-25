package ir.edusa.parents.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
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

import java.util.ArrayList;

import fontdroid.TextView;
import ir.edusa.parents.Adapters.GooglePlacesAutoCompleteAdapter;
import ir.edusa.parents.Dialogs.ErrorDialog;
import ir.edusa.parents.Fragments.AlarmPointsListFragment;
import ir.edusa.parents.Fragments.TextInputFragment;
import ir.edusa.parents.Helpers.HashHelper;
import ir.edusa.parents.Helpers.ValidatorHelper;
import ir.edusa.parents.Interfaces.OnAlarmPointClickListener;
import ir.edusa.parents.Interfaces.OnAutoCompleteStateChanged;
import ir.edusa.parents.Interfaces.OnTextEnteredListener;
import ir.edusa.parents.Managers.UserManager;
import ir.edusa.parents.Models.AlarmLocationPoint;
import ir.edusa.parents.Models.GooglePlaceModel;
import ir.edusa.parents.Models.Passenger;
import ir.edusa.parents.Models.SchoolService;
import ir.edusa.parents.Network.ApiCallerClass;
import ir.edusa.parents.Network.ApiConstants;
import ir.edusa.parents.Network.Requests.PassengerInfoRequest;
import ir.edusa.parents.Network.Requests.SetAlarmPointRequest;
import ir.edusa.parents.Network.Responses.AbstractResponse;
import ir.edusa.parents.Network.Responses.UserInfoResponse;
import ir.edusa.parents.R;
import ir.edusa.parents.Utils.InstantAutoComplete;
import ir.edusa.parents.Utils.Log;
import ir.edusa.parents.Utils.MyLocationHandler;


public class AlarmPointsActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private GoogleApiClient googleApiClient;
    private MapView mapView;
    public GoogleMap googleMap;

    private InstantAutoComplete autoCompleteTextView;
    private ViewGroup lytAutoCompleteTextView;
    private ProgressWheel progressWheel;

    private TextView txtNoResult;
    private ViewGroup lytPin;
    private ViewGroup lytAddPoint;
    private ViewGroup lytPointsList;
    private ViewGroup lytConfirmPoint;

    private Location userLocation;
    private ArrayList<AlarmLocationPoint> alarmLocationPoints;
    private boolean addMode = false;


    @Override
    protected String getPageTitle() {
        return "نقاط هشدار";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_points);
        txtNoResult =(TextView) findViewById(R.id.txtNoResult);
        lytPin = (ViewGroup)findViewById(R.id.lytPin);
        lytAddPoint =(ViewGroup) findViewById(R.id.lytAddPoint);
        lytPointsList = (ViewGroup)findViewById(R.id.lytPointsList);
        lytConfirmPoint =(ViewGroup) findViewById(R.id.lytConfirmPoint);

        mapView =(MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap gm) {
                initializeGoogleApiClient();
                initializeAutocomplete();
                googleMap = gm;
                googleMap.setMyLocationEnabled(true);
                getUserInfo();
            }
        });


    }

    public void initializeAutocomplete() {
        autoCompleteTextView =(InstantAutoComplete) findViewById(R.id.autoCompleteTextView);
        lytAutoCompleteTextView =(ViewGroup) findViewById(R.id.lytAutoCompleteTextView);
        progressWheel =(ProgressWheel) findViewById(R.id.progressWheel);

        lytAutoCompleteTextView.setVisibility(View.GONE);
        final GoogleApiClient placesApiClient = new GoogleApiClient.Builder(this, this, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        placesApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                lytAutoCompleteTextView.setVisibility(View.VISIBLE);

                autoCompleteTextView.setAdapter(new GooglePlacesAutoCompleteAdapter(AlarmPointsActivity.this, R.id.autoCompleteTextView, placesApiClient,
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

    public void initializeUi() {
        manageViewModes(false);
        lytAddPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageViewModes(true);
            }
        });
        lytConfirmPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputFragment textInputFragment = TextInputFragment.newInstance("نام", "شما می توانید در صورت تمایل، یک نام به محل انتخاب شده نسبت دهید.",
                        "تایید", new OnTextEnteredListener() {
                            @Override
                            public void onSubmit(String s, SchoolService service) {
                                if (alarmLocationPoints == null) {
                                    alarmLocationPoints = new ArrayList<AlarmLocationPoint>();
                                }
                                sendAlarmPoint(s, googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude, (getNewPointCode()) + "");
                            }
                        });
                textInputFragment.show(getSupportFragmentManager(), TextInputFragment.class.getSimpleName());
            }
        });
        lytPointsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmPointsListFragment alarmPointsListFragment = AlarmPointsListFragment.newInstance(alarmLocationPoints, new OnAlarmPointClickListener() {
                    @Override
                    public void onItemSelect(AlarmLocationPoint point) {
                        navigateToPosition(point.getLat(), point.getLon());
                    }

                    @Override
                    public void onItemDelete(AlarmLocationPoint point) {
                        deleteAlarmPoint(point.getPoint_Code());
                    }

                });
                alarmPointsListFragment.show(getSupportFragmentManager(), AlarmPointsListFragment.class.getSimpleName());
            }
        });
    }

    public int getNewPointCode() {
        if (alarmLocationPoints == null) {
            return 1;
        }
        for (int i = 1; i < 6; i++) {
            boolean hasCurrent = false;
            for (AlarmLocationPoint point : alarmLocationPoints) {
                if (point.getPoint_Code().equals(i + "")) {
                    hasCurrent = true;
                }
            }
            if (!hasCurrent) {
                return i;
            }
        }
        return 1;
    }

    public void manageViewModes(boolean addMode) {
        if (googleMap != null) {
            googleMap.clear();
        }
        this.addMode = addMode;
        lytPointsList.setVisibility(View.VISIBLE);
        txtNoResult.setVisibility(View.VISIBLE);
        lytAddPoint.setVisibility(View.VISIBLE);
        lytConfirmPoint.setVisibility(View.VISIBLE);
        lytPin.setVisibility(View.VISIBLE);
        if (alarmLocationPoints == null || alarmLocationPoints.size() == 0) {
            lytPointsList.setVisibility(View.GONE);
        } else {
            txtNoResult.setVisibility(View.GONE);
            if (alarmLocationPoints.size() > 4) {
                lytAddPoint.setVisibility(View.GONE);
            }
            for (AlarmLocationPoint point : alarmLocationPoints) {
                addMarker(point);
            }
            if (userLocation == null) {
                navigateToPosition(alarmLocationPoints.get(0).getLat(), alarmLocationPoints.get(0).getLon());
            }
        }

        if (addMode) {
            txtNoResult.setVisibility(View.GONE);
            lytAddPoint.setVisibility(View.GONE);
        } else {
            lytConfirmPoint.setVisibility(View.GONE);
            lytPin.setVisibility(View.GONE);
        }
    }

//    public void addMode(boolean enable) {
//        addMode = enable;
//        if (enable) {
//            txtNoResult.setVisibility(View.GONE);
//            lytAddPoint.setVisibility(View.GONE);
//            lytConfirmPoint.setVisibility(View.VISIBLE);
//            lytPin.setVisibility(View.VISIBLE);
//        } else {
//            lytConfirmPoint.setVisibility(View.GONE);
//            lytPin.setVisibility(View.GONE);
//            lytAddPoint.setVisibility(View.VISIBLE);
//            if (alarmLocationPoints == null || alarmLocationPoints.size() == 0) {
//                lytPointsList.setVisibility(View.GONE);
//                txtNoResult.setVisibility(View.VISIBLE);
//            } else {
//                if (alarmLocationPoints.size() > 4) {
//                    lytAddPoint.setVisibility(View.GONE);
//                }
//            }
//        }
//    }

    public void addMarker(AlarmLocationPoint locationPoint) {
        if (googleMap == null) {
            return;
        }
        googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_pin))
                .position(new LatLng(locationPoint.getLat(), locationPoint.getLon()))
                .title(ValidatorHelper.isValidString(locationPoint.getPoint_Descript()) ? locationPoint.getPoint_Descript() : "نقظه شماره " + locationPoint.getPoint_Code()));

    }

    public void getUserInfo() {
        ApiCallerClass.with(this).hasLoading(true).call(new PassengerInfoRequest(UserManager.getDeviceCode(),
                        HashHelper.Calculate_Password(UserManager.getPrivateApiKey(), UserManager.getToken()))
                , new ApiCallerClass.CallerOnResponse() {
                    @Override
                    public void onResponse(AbstractResponse result, JSONObject rawResponse) {
                        final UserInfoResponse userInfoResponse = (UserInfoResponse) result;
                        alarmLocationPoints = userInfoResponse.getData().getAlarm_Location_Points();
                        initializeUi();
                    }

                    @Override
                    public void onError(String message, int code) {
                        ErrorDialog errorDialog = new ErrorDialog(AlarmPointsActivity.this, message);
                        errorDialog.show();
                        errorDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                finishWithAnimation();
                            }
                        });
                    }
                }, "");
    }

    public void sendAlarmPoint(final String description, final double lat, final double lon, final String code) {
        Passenger passenger = UserManager.getPassenger();
        String driverCode = "";
        for (SchoolService service : passenger.getServices()) {
            driverCode += service.getDriver_Code() + ",";
        }
        if (ValidatorHelper.isValidString(driverCode)) {
            driverCode = driverCode.substring(0, driverCode.length() - 1);
        }

        ApiCallerClass.with(this).hasLoading(true).call(new SetAlarmPointRequest(HashHelper.Calculate_Password(UserManager.getPrivateApiKey(), UserManager.getToken()),
                        driverCode, UserManager.getDeviceCode(), passenger.getCode(), code, description, lat, lon)
                , new ApiCallerClass.CallerOnResponse() {
                    @Override
                    public void onResponse(AbstractResponse result, JSONObject rawResponse) {
                        AlarmLocationPoint point = new AlarmLocationPoint(code, description, lat, lon);

                        alarmLocationPoints.add(point);
                        manageViewModes(false);
                        addMarker(point);
                        navigateToPosition(lat, lon);
                        UserManager.setAlarmPoints(alarmLocationPoints);
                    }

                    @Override
                    public void onError(String message, int code) {
                        ErrorDialog errorDialog = new ErrorDialog(AlarmPointsActivity.this, message);
                        errorDialog.show();
                    }
                }, "");
    }

    public void deleteAlarmPoint(final String code) {
        Passenger passenger = UserManager.getPassenger();
        String driverCode = "";
        for (SchoolService service : passenger.getServices()) {
            driverCode += service.getDriver_Code() + ",";
        }
        if (ValidatorHelper.isValidString(driverCode)) {
            driverCode = driverCode.substring(0, driverCode.length() - 1);
        }
        ApiCallerClass.with(this).hasLoading(true).call(new SetAlarmPointRequest(HashHelper.Calculate_Password(UserManager.getPrivateApiKey(), UserManager.getToken()),
                        driverCode, UserManager.getDeviceCode(), passenger.getCode(), code, "", 0, 0)
                , new ApiCallerClass.CallerOnResponse() {
                    @Override
                    public void onResponse(AbstractResponse result, JSONObject rawResponse) {
                        AlarmLocationPoint point = new AlarmLocationPoint(code, "", 0, 0);
                        alarmLocationPoints.remove(point);
                        UserManager.setAlarmPoints(alarmLocationPoints);
                        manageViewModes(false);
                    }

                    @Override
                    public void onError(String message, int code) {
                        ErrorDialog errorDialog = new ErrorDialog(AlarmPointsActivity.this, message);
                        errorDialog.show();
                    }
                }, "");
    }

    public void initializeGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(AlarmPointsActivity.this).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            Log.e("LOCATION", "LocationSettingsStatusCodes.SUCCESS:");
                            MyLocationHandler.LocationResult locationResult = new MyLocationHandler.LocationResult() {
                                @Override
                                public void gotLocation(final Location loc) {
                                    navigateToPosition(loc.getLatitude(), loc.getLongitude());
                                    userLocation = loc;
                                }
                            };
                            MyLocationHandler myLocation = new MyLocationHandler();
                            myLocation.getLocation(AlarmPointsActivity.this, locationResult);
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.e("LOCATION", "LocationSettingsStatusCodes.RESOLUTION_REQUIRED:");
                            try {
                                status.startResolutionForResult(AlarmPointsActivity.this, ApiConstants.ENABLE_GPS_CODE);
                            } catch (IntentSender.SendIntentException e) {
                                Toast.makeText(AlarmPointsActivity.this, "خطا در دریافت مختصات جغرافیایی", Toast.LENGTH_SHORT).show();

                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Log.e("LOCATION", "LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:");
                    }
                }
            });
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ApiConstants.ENABLE_GPS_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Log.e("LOCATION", "LocationSettingsStatusCodes.RESULT_OK:");
                    MyLocationHandler.LocationResult locationResult = new MyLocationHandler.LocationResult() {
                        @Override
                        public void gotLocation(final Location loc) {
                            userLocation = loc;
                            navigateToPosition(loc.getLatitude(), loc.getLongitude());
                        }
                    };
                    MyLocationHandler myLocation = new MyLocationHandler();
                    myLocation.getLocation(AlarmPointsActivity.this, locationResult);
                    break;
                default:
                    break;
            }
        }
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
    public void finishWithAnimation() {
        if (addMode) {
            manageViewModes(false);
            return;
        }
        super.finishWithAnimation();
    }
}