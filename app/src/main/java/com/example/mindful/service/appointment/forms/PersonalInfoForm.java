package com.example.mindful.service.appointment.forms;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.mindful.R;
import com.example.mindful.api.GeoNameApiHandler;
import com.example.mindful.service.appointment.AppointmentFragment;
import com.example.mindful.service.appointment.data.PersonalInfo;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalInfoForm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalInfoForm extends AppointmentFragment {

    private Button dobSetBtn;

    private TextView dobTV;

    private EditText personalInfoCityET;

    private PersonalInfo personalInfo;

    private GeoNameApiHandler geoNameApiHandler;

    private String TAG = "PersonalInfoForm";


    public PersonalInfoForm() {
        // Required empty public constructor
    }

    public PersonalInfoForm(String title, String description){
        super(title, description);
        this.fragmentTitle = title;
        this.fragmentDescription = description;
    }

    public PersonalInfoForm(String title, String description, PersonalInfo personalInfo){
        super(title, description);
        this.fragmentTitle = title;
        this.fragmentDescription = description;
        this.personalInfo = personalInfo;
    }


    public static PersonalInfoForm newInstance() {
        PersonalInfoForm fragment = new PersonalInfoForm();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_personal_info_form, container, false);

        //check if personInfo object has not been instantiated (a constructor with personalInfo param is used), and instantiate
        if(personalInfo == null){
            Log.d(TAG, "hi");
            personalInfo = new PersonalInfo();
        }

        //Make GeoName api object
        if(geoNameApiHandler == null){
            geoNameApiHandler = new GeoNameApiHandler();
        }

        dobSetBtn = rootView.findViewById(R.id.dob_set_btn);
        dobTV = rootView.findViewById(R.id.dob_tv);
        personalInfoCityET = rootView.findViewById(R.id.personal_info_city_et);

        dobSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "clicked date swet?");

               DatePickerDialog datePickerDialog = personalInfo.getDob().CreateDatePicker(requireActivity());
               datePickerDialog.show();
               if(datePickerDialog!= null){
                  datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                      @Override
                      public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                          //Set the value of the dob textview so that the user has feedback about the dob they just set
                          Log.d(TAG, "month: " + month + " day: " + dayOfMonth + " year: " + year );
                          personalInfo.getDob().setDate(month + 1, dayOfMonth, year);

//                          //personalInfo.getDob().getYear(), personalInfo.getDob().getMonth(), personalInfo.getDob().getDay()
                          LocalDate date = LocalDate.of(personalInfo.getDob().getYear(), personalInfo.getDob().getMonth(), personalInfo.getDob().getDay());
                          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

                          dobTV.setText(date.format(formatter));
                      }
                  });
               }


            }
        });


        View.OnClickListener onClickListener = new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Log.d(TAG, "Clicked citites");
                geoNameApiHandler.fetchAllUSACities();

            }
        };

        personalInfoCityET.setOnClickListener(onClickListener);


        return rootView;

    }




    public PersonalInfo getPersonalInfo() {
        return this.personalInfo;
    }
}