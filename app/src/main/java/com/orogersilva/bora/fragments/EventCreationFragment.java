package com.orogersilva.bora.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.places.Places;
import com.orogersilva.bora.R;
import com.orogersilva.bora.interfaces.OnDatePickerFragmentListener;
import com.orogersilva.bora.interfaces.OnFragmentTransactionListener;
import com.orogersilva.bora.interfaces.OnTimePickerFragmentListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by RogerSilva on 7/28/2015.
 */
public class EventCreationFragment extends Fragment
    implements OnDatePickerFragmentListener, OnTimePickerFragmentListener,
        ConnectionCallbacks, OnConnectionFailedListener {

    public static final String TAG = "EventCreationFragment";

    // region INSTANCE VARIABLES

    @Bind(R.id.event_date_edittext) EditText eventDateEditText;
    @Bind(R.id.event_time_edittext) EditText eventTimeEditText;
    private GoogleApiClient mGoogleApiClient;
    private OnFragmentTransactionListener mTransactionListener;

    // endregion

    // region GETTERS AND SETTERS

    private OnDatePickerFragmentListener getDatePickerFragmentListener() {

        return this;
    }

    private OnTimePickerFragmentListener getTimePickerFragmentListener() {

        return this;
    }

    // endregion

    // region FRAGMENT LIFECYCLE METHODS

    @Override public void onAttach(Activity activity) {

        super.onAttach(activity);

        try {

            mTransactionListener = (OnFragmentTransactionListener) activity;
        }
        catch (ClassCastException e) {

            throw new ClassCastException(activity.toString() + " must implement OnFragmentTransactionListener.");
        }
    }

    @Override public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_event_creation, container, false);

        ButterKnife.bind(this, fragmentView);

        return fragmentView;
    }

    @Override public void onStart() {

        super.onStart();

        mGoogleApiClient.connect();
    }

    @Override public void onResume() {

        super.onResume();

        Log.d(TAG, TAG + " onResumed.");
    }

    @Override public void onStop() {

        mGoogleApiClient.disconnect();

        super.onStop();
    }

    @Override public void onDestroyView() {

        super.onDestroyView();

        ButterKnife.unbind(this);
    }

    // endregion

    // region BUTTERKNIFE METHODS

    @OnClick(R.id.event_date_edittext)
    public void showEventDateDialog() {

        DialogFragment datePickerFragment = DatePickerFragment.newInstance(
                getDatePickerFragmentListener());
        datePickerFragment.show(getFragmentManager(), DatePickerFragment.TAG);
    }

    @OnClick(R.id.event_time_edittext)
    public void showEventTimeDialog() {

        DialogFragment timePickerFragment = TimePickerFragment.newInstance(
                getTimePickerFragmentListener());
        timePickerFragment.show(getFragmentManager(), TimePickerFragment.TAG);
    }

    // endregion

    // region OVERRIDES METHODS

    @Override public void onDateSet(Date date) {

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        String formattedDate = dateFormat.format(date);

        eventDateEditText.setText(formattedDate);
    }

    @Override public void onTimeSet(String time) {

        eventTimeEditText.setText(time);
    }

    @Override public void onConnected(Bundle bundle) {
    }

    @Override public void onConnectionSuspended(int i) {
    }

    @Override public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    // endregion
}