package com.jobesk.thesecretpsychics.AdvisorActivities.EditProfile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.EventBuses.EditProfileBus;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

public class AdvisorEditFragPaymentDetails extends Fragment {
    private LinearLayout next_btn;
    private EditText time_rate_et, text_chat_rate_et, name_et, address_et, zipcode_et, day_et, month_et, year_et, country_et, city_et, bank_details_et, threshold_et;
    private String timeRate = "9.99", chatRate, name, address, zipcode, day, month, year, country, city, threshold, bankDetails;
    private String dateOfBirth;
    private TextView chat_rate_disCount_tv;
    private float percentageVal = 40;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_advisor_payment_details, container, false);


        AdvisorEditProfile.toolbar_title.setText(getActivity().getResources().getString(R.string.payment_details));


        time_rate_et = rootView.findViewById(R.id.time_rate_et);
        time_rate_et.setClickable(false);
        time_rate_et.setEnabled(false);
        text_chat_rate_et = rootView.findViewById(R.id.text_chat_rate_et);
        name_et = rootView.findViewById(R.id.name_et);
        address_et = rootView.findViewById(R.id.address_et);
        zipcode_et = rootView.findViewById(R.id.zipcode_et);
        day_et = rootView.findViewById(R.id.day_et);
        month_et = rootView.findViewById(R.id.month_et);
        year_et = rootView.findViewById(R.id.year_et);
        country_et = rootView.findViewById(R.id.country_et);
        city_et = rootView.findViewById(R.id.city_et);
        bank_details_et = rootView.findViewById(R.id.bank_details_et);
        threshold_et = rootView.findViewById(R.id.threshold_et);
        threshold_et.setEnabled(false);
        chat_rate_disCount_tv = rootView.findViewById(R.id.chat_rate_disCount);


        String currentString = AdvisorEditProfile.dateOfBirth;
        String[] separated = currentString.split("/");

        String yearPre = separated[0];
        String monthPre = separated[1];
        String dayPre = separated[2];

        time_rate_et.setText(AdvisorEditProfile.timeRate);
        text_chat_rate_et.setText(AdvisorEditProfile.TextChatRate);
        name_et.setText(AdvisorEditProfile.legalName);
        address_et.setText(AdvisorEditProfile.permanentAddress);
        zipcode_et.setText(AdvisorEditProfile.zipCode);
        day_et.setText(dayPre);
        month_et.setText(monthPre);
        year_et.setText(yearPre);
        country_et.setText(AdvisorEditProfile.country);
        city_et.setText(AdvisorEditProfile.city);
        bank_details_et.setText(AdvisorEditProfile.bankDetails);
        threshold_et.setText(AdvisorEditProfile.paymentThreshold);


        float userValue = Float.valueOf(String.valueOf(AdvisorEditProfile.TextChatRate));
        float percentage = userValue * percentageVal / 100;
        chat_rate_disCount_tv.setText(getActivity().getResources().getString(R.string.you_will_earn_with_colon) + " " + getActivity().getResources().getString(R.string.pound) + percentage);


        text_chat_rate_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                String value = s.toString().trim();

                if (value.length() == 1 && value.equals(".")) {

                    text_chat_rate_et.setText("");
                    return;

                }

                if (String.valueOf(s).equalsIgnoreCase("") || s.length() <= 0) {
                    chat_rate_disCount_tv.setText(getActivity().getResources().getString(R.string.you_will_earn_with_colon) + " " + getActivity().getResources().getString(R.string.pound) + "0");
                } else {
                    float userValue = Float.valueOf(String.valueOf(s));
                    float percentage = userValue * percentageVal / 100;
                    chat_rate_disCount_tv.setText(getActivity().getResources().getString(R.string.you_will_earn_with_colon) + " " + getActivity().getResources().getString(R.string.pound) + percentage);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        next_btn = rootView.findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timeRate = time_rate_et.getText().toString().trim();
                chatRate = text_chat_rate_et.getText().toString().trim();
                name = name_et.getText().toString().trim();
                address = address_et.getText().toString().trim();
                zipcode = zipcode_et.getText().toString().trim();
                day = day_et.getText().toString().trim();
                month = month_et.getText().toString().trim();
                year = year_et.getText().toString().trim();
                country = country_et.getText().toString().trim();
                city = city_et.getText().toString().trim();
                bankDetails = bank_details_et.getText().toString().trim();
                threshold = "50";


                if (chatRate.equals("")) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_chat_rate), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (chatRate.equals("0")) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_chat_rate), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (name.equals("")) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_individual_name), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (day.equals("")) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_individual_name), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Integer.valueOf(day) > 31 || Integer.valueOf(day) == 0) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_valid_day), Toast.LENGTH_SHORT).show();
                    return;
                }


                if (month.equals("")) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_month), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Integer.valueOf(month) > 12 || Integer.valueOf(month) == 0) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_valid_month), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (year.equals("")) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_year), Toast.LENGTH_SHORT).show();
                    return;
                }

                int yearCurrent = Calendar.getInstance().get(Calendar.YEAR);

                if (Integer.valueOf(year) > 1950 && Integer.valueOf(year) > yearCurrent) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_valid_year), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (country.equals("")) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_country), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (city.equals("")) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_city), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (bankDetails.equals("")) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_bank_Details), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (GlobalClass.emailValidator(bankDetails) == false) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_valid_paypal_address), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (threshold.equals("")) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_bank_threshold), Toast.LENGTH_SHORT).show();
                    return;
                }


                dateOfBirth = year + "/" + month + "/" + day;


                AdvisorEditProfile.threshold = threshold;
                AdvisorEditProfile.zipcode = zipcode;
                AdvisorEditProfile.address = address;
                AdvisorEditProfile.name = name;
                AdvisorEditProfile.chatRate = chatRate;
                AdvisorEditProfile.bankDetails = bankDetails;
                AdvisorEditProfile.city = city;
                AdvisorEditProfile.zipCode = zipcode;
                AdvisorEditProfile.country = country;
                AdvisorEditProfile.dateOfBirth = dateOfBirth;
                AdvisorEditProfile.timeRate = timeRate;


                EventBus.getDefault().post(new EditProfileBus());


            }
        });


        return rootView;
    }
}
