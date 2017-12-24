package ir.edusa.parents.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import fontdroid.TextView;
import ir.edusa.parents.Dialogs.ErrorDialog;
import ir.edusa.parents.Fragments.ServiceChooserFragment;
import ir.edusa.parents.Fragments.TimePeriodPickerFragment;
import ir.edusa.parents.Helpers.DateHelper;
import ir.edusa.parents.Helpers.HashHelper;
import ir.edusa.parents.Helpers.Toast;
import ir.edusa.parents.Interfaces.OnDriverRouteTimePicked;
import ir.edusa.parents.Interfaces.OnServiceSelected;
import ir.edusa.parents.Managers.UserManager;
import ir.edusa.parents.Models.LivePointNotification;
import ir.edusa.parents.Models.SchoolService;
import ir.edusa.parents.Models.TrackingPoint;
import ir.edusa.parents.Network.ApiCallerClass;
import ir.edusa.parents.Network.Requests.DriverLastLocationRequest;
import ir.edusa.parents.Network.Requests.GetDriverRouteRequest;
import ir.edusa.parents.Network.Requests.StartLiveTrackingRequest;
import ir.edusa.parents.Network.Requests.StopLiveTrackingRequest;
import ir.edusa.parents.Network.Responses.AbstractResponse;
import ir.edusa.parents.Network.Responses.DriverLastLocationResponse;
import ir.edusa.parents.Network.Responses.DriverRouteResponse;
import ir.edusa.parents.R;


public class MapActivity extends BaseActivity {

    private final int LAST_MODE = 0;
    private final int LIVE_MODE = 1;
    private final int PATH_MODE = 2;

    private MapView mapView;
    private ViewGroup lytLiveLocation;
    private ViewGroup lytLastLocation;
    private ViewGroup lytPath;
    private ViewGroup lytChooseTime;
    private TextView txtLastUpdate;
    private TextView txtDriverName;

    private GoogleMap googleMap;
    private Polyline polyline;
    private int current_mode;
    private Marker liveMarker;
    private Date pickerDate;
    private int pickerTimeToMin;
    private int pickerTimeToHour;
    private int pickerTimeFromMin;
    private int pickerTimeFromHour;
    private Timer liveTimer;
    private SchoolService selectedService;

    @Override
    protected void onResume() {
        super.onResume();
        //Is EventBus Register
        if (!EventBus.getDefault().isRegistered(this)) {
            //Register EventBus
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Un Register EventBus
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected String getPageTitle() {
        return "مشاهده سرویس";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        lytLiveLocation =(ViewGroup) findViewById(R.id.lytLiveLocation);
        lytLastLocation = (ViewGroup)findViewById(R.id.lytLastLocation);
        lytPath =(ViewGroup) findViewById(R.id.lytPath);
        lytChooseTime = (ViewGroup)findViewById(R.id.lytChooseTime);
        txtLastUpdate = (TextView) findViewById(R.id.txtLastUpdate);
        txtDriverName = (TextView)findViewById(R.id.txtDriverName);
        final ArrayList<SchoolService> services = UserManager.getPassenger().getServices();
        if (services.size() == 0) {
            Toast.makeText(this, "هنوز سرویسی به شما اختصاص نیافته است", Toast.LENGTH_SHORT).show();
            finishWithAnimation();
            return;
        }
        selectedService = services.get(0);
        if (services.size() > 1) {
            txtDriverName.setVisibility(View.VISIBLE);
            txtDriverName.setText("مربوط به راننده : " + selectedService.getDriverFullName());
            txtDriverName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ServiceChooserFragment chooserFragment = ServiceChooserFragment.newInstance(new OnServiceSelected() {
                        @Override
                        public void onItemSelect(SchoolService service) {
                            selectedService = service;
                            txtDriverName.setText("مربوط به راننده : " + selectedService.getDriverFullName());
                        }
                    }, services);
                    chooserFragment.show(getSupportFragmentManager(), ServiceChooserFragment.class.getSimpleName());
                }
            });
        }else{
            txtDriverName.setVisibility(View.GONE);
        }


        mapView = (MapView)findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap gm) {
                googleMap = gm;
                googleMap.setMyLocationEnabled(true);
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                pickerDate = date;
                pickerTimeFromMin = calendar.get(Calendar.MINUTE);
                pickerTimeFromHour = (calendar.get(Calendar.HOUR_OF_DAY) - 4) % 24;
                pickerTimeToMin = calendar.get(Calendar.MINUTE);
                pickerTimeToHour = calendar.get(Calendar.HOUR_OF_DAY);

                lytChooseTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final TimePeriodPickerFragment pickerFragment = TimePeriodPickerFragment.newInstance(pickerDate, pickerTimeFromHour,
                                pickerTimeFromMin, pickerTimeToHour, pickerTimeToMin, new OnDriverRouteTimePicked() {
                                    @Override
                                    public void onTimePicked(Date date, int fromTimeH, int fromTimeM, int toTimeH, int toTimeM) {
                                        pickerDate = date;
                                        pickerTimeFromHour = fromTimeH;
                                        pickerTimeFromMin = fromTimeM;
                                        pickerTimeToHour = toTimeH;
                                        pickerTimeToMin = toTimeM;
                                        if (current_mode == PATH_MODE) {
                                            getDriverRoute(false);
                                        }
                                    }
                                });
                        pickerFragment.show(getSupportFragmentManager(), TimePeriodPickerFragment.class.getSimpleName());
                    }
                });


                lytLiveLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchMode(LIVE_MODE);
                    }
                });
                lytPath.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchMode(PATH_MODE);
                    }
                });
                lytLastLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchMode(LAST_MODE);
                    }
                });
                switchMode(LAST_MODE);

            }
        });
    }

    public void setButtonsMode(int mode) {
        lytLastLocation.setBackgroundColor(ContextCompat.getColor(MapActivity.this, R.color.colorPrimary));
        lytLiveLocation.setBackgroundColor(ContextCompat.getColor(MapActivity.this, R.color.colorPrimary));
        lytPath.setBackgroundColor(ContextCompat.getColor(MapActivity.this, R.color.colorPrimary));
        switch (mode) {
            case LIVE_MODE:
                lytLiveLocation.setBackgroundColor(ContextCompat.getColor(MapActivity.this, R.color.colorPrimaryDark));
                break;
            case LAST_MODE:
                lytLastLocation.setBackgroundColor(ContextCompat.getColor(MapActivity.this, R.color.colorPrimaryDark));
                break;
            case PATH_MODE:
                lytPath.setBackgroundColor(ContextCompat.getColor(MapActivity.this, R.color.colorPrimaryDark));
                break;
        }
    }

    public void switchMode(int mode) {
        if (googleMap == null)
            return;
        if (current_mode == LIVE_MODE) {
            stopLiveTracking();
        }
        current_mode = mode;
        googleMap.clear();
        setButtonsMode(mode);
        if (liveTimer != null) {
            liveTimer.cancel();
        }
        lytChooseTime.setVisibility(View.GONE);
        txtLastUpdate.setVisibility(View.GONE);

        switch (mode) {
            case LIVE_MODE:
                startLiveTracking();
                txtLastUpdate.setVisibility(View.VISIBLE);
                updateLiveLocationCountDown(true);
                break;
            case LAST_MODE:
                getDriverLastLocation();
                break;
            case PATH_MODE:
                lytChooseTime.setVisibility(View.VISIBLE);
                getDriverRoute(true);
                break;
        }
    }

    int countSec = 0;

    public void updateLiveLocationCountDown(boolean never) {
        if (liveTimer != null) {
            liveTimer.cancel();
        }
        String text = "آخرین بروزرسانی" + "\n";
        if (never) {
            text += "هیچ وقت";
        } else {
            countSec = 0;
            final String finalText = text;
            liveTimer = new Timer();
            liveTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            countSec++;
                            int min = countSec / 60;
                            if (min > 59) {
                                txtLastUpdate.setText(finalText + "بیش از یک ساعت قبل");
                            } else {
                                int sec = countSec % 60;
                                txtLastUpdate.setText(finalText + DateHelper.formatTime(min, sec, ":") + " قبل");
                            }
                        }
                    });
                }
            }, 0, 1000);
        }
        txtLastUpdate.setText(text);
    }

    public void getDriverLastLocation() {
        ApiCallerClass.with(this).hasLoading(true).call(new DriverLastLocationRequest(
                UserManager.getDeviceCode(), selectedService.getDriver_Code(),
                HashHelper.Calculate_Password(UserManager.getPrivateApiKey(), UserManager.getToken())), new ApiCallerClass.CallerOnResponse() {
            @Override
            public void onResponse(AbstractResponse result, JSONObject rawResponse) {
                DriverLastLocationResponse response = (DriverLastLocationResponse) result;
                if (response.getData() == null || response.getData().getPoint() == null) {
                    Toast.makeText(MapActivity.this, "موقعیت کاربر موجود نمی باشد", Toast.LENGTH_SHORT).show();
                    return;
                }
                googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_pin))
                        .position(new LatLng(response.getData().getPoint().getLat(), response.getData().getPoint().getLon())).title("آخرین محل"));
                navigateToPosition(response.getData().getPoint().getLat(), response.getData().getPoint().getLon());
            }

            @Override
            public void onError(String message, int code) {
                ErrorDialog errorDialog = new ErrorDialog(MapActivity.this, message);
                errorDialog.show();
            }
        }, "");
    }

    public void startLiveTracking() {
        ApiCallerClass.with(this).hasLoading(true).call(new StartLiveTrackingRequest(UserManager.getDeviceCode(),
                selectedService.getDriver_Code(), 30, HashHelper.Calculate_Password(UserManager.getPrivateApiKey(),
                UserManager.getToken())), new ApiCallerClass.CallerOnResponse() {
            @Override
            public void onResponse(AbstractResponse result, JSONObject rawResponse) {

            }

            @Override
            public void onError(String message, int code) {
                ErrorDialog errorDialog = new ErrorDialog(MapActivity.this, message);
                errorDialog.show();
            }
        }, "");
    }

    public void stopLiveTracking() {
        ApiCallerClass.with(this).hasLoading(true).call(new StopLiveTrackingRequest(UserManager.getDeviceCode(),
                selectedService.getDriver_Code(), HashHelper.Calculate_Password(UserManager.getPrivateApiKey(),
                UserManager.getToken())), new ApiCallerClass.CallerOnResponse() {
            @Override
            public void onResponse(AbstractResponse result, JSONObject rawResponse) {

            }

            @Override
            public void onError(String message, int code) {
                ErrorDialog errorDialog = new ErrorDialog(MapActivity.this, message);
                errorDialog.show();
            }
        }, "");
    }

    public void getDriverRoute(final boolean firstTime) {
        ApiCallerClass.with(this).hasLoading(true).call(new GetDriverRouteRequest(UserManager.getDeviceCode(),
                selectedService.getDriver_Code(), DateHelper.formatDate(pickerDate, ""), DateHelper.formatTime(pickerTimeFromHour, pickerTimeFromMin, "")
                , DateHelper.formatTime(pickerTimeToHour, pickerTimeToMin, ""), HashHelper.Calculate_Password(UserManager.getPrivateApiKey(),
                UserManager.getToken())), new ApiCallerClass.CallerOnResponse() {
            @Override
            public void onResponse(AbstractResponse result, JSONObject rawResponse) {
                DriverRouteResponse driverRouteResponse = (DriverRouteResponse) result;
                if (driverRouteResponse.getData() != null) {
                    ArrayList<TrackingPoint> points = driverRouteResponse.getData().getPoints();

                    drawPoliline(driverRouteResponse.getData().getPoints());
                    TrackingPoint lastPoint = points.get(points.size() - 1);
                    navigateToPosition(lastPoint.getLat(), lastPoint.getLon());
                } else {
                    if (firstTime) {
                        ErrorDialog errorDialog = new ErrorDialog(MapActivity.this, "در 4 ساعت گذشته اطلاعاتی یافت نشد");
                        errorDialog.show();
                    } else {
                        ErrorDialog errorDialog = new ErrorDialog(MapActivity.this, "در این بازه زمانی اطلاعاتی یافت نشد");
                        errorDialog.show();
                    }
                }
            }

            @Override
            public void onError(String message, int code) {
                ErrorDialog errorDialog = new ErrorDialog(MapActivity.this, message);
                errorDialog.show();
            }
        }, "");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActiveOrderReceived(LivePointNotification livePointNotification) {
        if (current_mode != LIVE_MODE || googleMap == null) {
            return;
        }
        if (liveMarker == null || !liveMarker.isVisible()) {
            googleMap.clear();
            liveMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_pin))
                    .position(new LatLng(livePointNotification.getLatitude(), livePointNotification.getLongitude())).title("محل فعلی"));
        } else {
            liveMarker.setPosition(new LatLng(livePointNotification.getLatitude(), livePointNotification.getLongitude()));
        }
        navigateToPosition(livePointNotification.getLatitude(), livePointNotification.getLongitude());
        updateLiveLocationCountDown(false);

    }

    @Override
    protected void onDestroy() {
        if (mapView != null) {
            mapView.onDestroy();
        }
        super.onDestroy();
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

    private void drawPoliline(ArrayList<TrackingPoint> points) {
        if (googleMap == null) {
            return;
        }
        PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.BLUE);
        for (TrackingPoint trackingPoint : points) {
            rectLine.add(new LatLng(trackingPoint.getLat(), trackingPoint.getLon()));
        }

        if (polyline != null)
            polyline.remove();
        polyline = googleMap.addPolyline(rectLine);
    }
}
