package com.jobesk.thesecretpsychics.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorChat.AdvisorGetChat;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorDrawerActivity;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorLanguage;
import com.jobesk.thesecretpsychics.ClientActivities.ClientDrawerActivity;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        String introValue = GlobalClass.getIntro("intro", getApplicationContext());

        if (introValue.equalsIgnoreCase("1")) {

            String AdvisorID = GlobalClass.getPref("advisorID", getApplicationContext());
            String clientID = GlobalClass.getPref("clientID", getApplicationContext());
            String advisorProfileStatus = GlobalClass.getPref("advisorProfileStatus", getApplicationContext());
            if (!AdvisorID.equals("")) {
                if (advisorProfileStatus.equals("0")) {

                    Intent i = new Intent(WelcomeActivity.this, AdvisorLanguage.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);


                }
                if (advisorProfileStatus.equals("1")) {

                    Intent intent = getIntent();

                    if (intent.hasExtra("type")) {
                        //go for notification


                        String type = intent.getExtras().getString("type");


                        if (type.equalsIgnoreCase("chat")) {

                            String sendTo = intent.getExtras().getString("sendTo");
                            String sendBy = intent.getExtras().getString("sendBy");
                            String preName = intent.getExtras().getString("preName");
                            String preChatRate = intent.getExtras().getString("preChatRate");
                            String preImage = intent.getExtras().getString("preImage");

                            // handle chat noti
                            Intent i = new Intent(WelcomeActivity.this, AdvisorDrawerActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("sendTo", sendTo);
                            bundle1.putString("sendBy", sendBy);
                            bundle1.putString("preName", preName);
                            bundle1.putString("preChatRate", preChatRate);
                            bundle1.putString("type", type);
                            bundle1.putString("preImage", preImage);
                            i.putExtras(bundle1);
                            getApplicationContext().startActivity(i);

                        } else {

                            // handle Order noti
                            Intent i = new Intent(WelcomeActivity.this, AdvisorDrawerActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("type", type);
                            i.putExtras(bundle1);
                            getApplicationContext().startActivity(i);

                        }


                    } else {

                        Intent i = new Intent(WelcomeActivity.this, AdvisorDrawerActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(i);

                    }


                }
                return;
            }


            if (!clientID.equals("")) {


                Intent intent = getIntent();

                if (intent.hasExtra("type")) {
                    //go for notification


                    String type = intent.getExtras().getString("type");


                    if (type.equalsIgnoreCase("chat")) {


                        String sendTo = intent.getExtras().getString("sendTo");
                        String sendBy = intent.getExtras().getString("sendBy");
                        String preName = intent.getExtras().getString("preName");
                        String preChatRate = intent.getExtras().getString("preChatRate");
                        String preImage = intent.getExtras().getString("preImage");
                        // handle chat noti
                        Intent clientIntent = new Intent(WelcomeActivity.this, ClientDrawerActivity.class);
                        clientIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Bundle ClientBundle = new Bundle();
                        ClientBundle.putString("sendTo", sendTo);
                        ClientBundle.putString("sendBy", sendBy);
                        ClientBundle.putString("preName", preName);
                        ClientBundle.putString("preChatRate", preChatRate);
                        ClientBundle.putString("type", type);
                        ClientBundle.putString("preImage", preImage);
                        clientIntent.putExtras(ClientBundle);
                        startActivity(clientIntent);

                    } else {

                        // handle Order noti
                        Intent i = new Intent(WelcomeActivity.this, ClientDrawerActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("type", type);
                        i.putExtras(bundle1);
                        startActivity(i);
                    }
                    return;

                }


                Intent i = new Intent(WelcomeActivity.this, ClientDrawerActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(i);

                return;
            }


            Intent i = new Intent(WelcomeActivity.this, WelcomeScreen_5.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(i);
        } else {
            viewPager = (ViewPager) findViewById(R.id.viewpager);

            setupViewPager(viewPager);


            CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
            indicator.setViewPager(viewPager);
        }


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new WelcomeScreen_1(), "First");
        viewPagerAdapter.addFragment(new WelcomeScreen_2(), "second");
        viewPagerAdapter.addFragment(new WelcomeScreen_3(), "third");
        viewPagerAdapter.addFragment(new WelcomeScreen_4(), "fourth");


        viewPager.setAdapter(viewPagerAdapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<>();
        List<String> fragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }

        public void addFragment(Fragment fragment, String name) {
            fragmentList.add(fragment);
            fragmentTitles.add(name);
        }
    }

    private void launchHomeScreen() {
        startActivity(new Intent(WelcomeActivity.this, WelcomeScreen_5.class));
        finish();
    }


}
