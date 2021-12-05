package com.jobesk.thesecretpsychics.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jobesk.thesecretpsychics.ClientActivities.ClientDrawerActivity;
import com.jobesk.thesecretpsychics.ClientActivities.ClientHostProfileActivity;
import com.jobesk.thesecretpsychics.ClientActivities.ClientFragments.ClientPagerFragment;
import com.jobesk.thesecretpsychics.Model.ClientPsychicModel;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.Urls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class ClientPsychicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ClientPsychicModel> arrayList;
    private Context context;
    private Activity activity;
    private FragmentManager fragmentManger;
    private ArrayList<ClientPsychicModel> featurePsychicArrayList;

    public ClientPsychicAdapter(Activity activity, Context context, ArrayList<ClientPsychicModel> arrayList, FragmentManager fragmentManger, ArrayList<ClientPsychicModel> featurePsychicArrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.featurePsychicArrayList = featurePsychicArrayList;
        this.activity = activity;
        this.fragmentManger = fragmentManger;
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case ClientPsychicModel.TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_header_phschic_row, parent, false);
                return new HeaderViewHolder(view);
            case ClientPsychicModel.TYPE_ROW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_row_psychic_cardview, parent, false);
                return new RowViewHolder(view);

        }
        return null;


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        final ClientPsychicModel object = arrayList.get(listPosition);
        if (object != null) {
            switch (object.getType()) {
                case ClientPsychicModel.TYPE_HEADER:
//                    ((HeaderViewHolder) holder).txtType.setText(object.text);


                    setupViewPager(holder, ((HeaderViewHolder) holder).viewPager);
                    break;
                case ClientPsychicModel.TYPE_ROW:

                    int width = ClientDrawerActivity.width / 2 - 5;

                    ViewGroup.LayoutParams layoutParams = ((RowViewHolder) holder).rootLayout.getLayoutParams();
                    layoutParams.width = width;
                    ((RowViewHolder) holder).rootLayout.setLayoutParams(layoutParams);


                    Picasso.with(context).load(Urls.IMAGE_BASEURL + object.getImage()).fit().centerCrop().into(((RowViewHolder) holder).imageview);

                    ((RowViewHolder) holder).title.setText(object.getScreenName());


                    double a = Double.valueOf(object.getRatting());
                    double rateValue = round(a, 1);

                    ((RowViewHolder) holder).ratingBar.setRating(Float.valueOf((float) rateValue));
                    ((RowViewHolder) holder).designation_tv.setText(object.getServiceName());


//                    ((RowViewHolder) holder).cardView.setLayoutParams(new CardView.LayoutParams(
//                            ClientDrawerActivity.screenWidth, ClientDrawerActivity.screenHeight));


                    String onlineUser = object.getIsOnline();

                    if (onlineUser.equalsIgnoreCase("1")) {
                        ((RowViewHolder) holder).online_img.setImageResource(R.drawable.ic_online);
                    } else {
                        ((RowViewHolder) holder).online_img.setImageResource(R.drawable.ic_offline);

                    }


                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Intent profileIntent = new Intent(context, ClientHostProfileActivity.class);
                            Bundle b = new Bundle();
                            b.putString("id", object.getPsychicID());
                            profileIntent.putExtras(b);
                            context.startActivity(profileIntent);


                        }
                    });


                    if (object.getIsLiveChat().equalsIgnoreCase("1")) {

                        ((RowViewHolder) holder).chat_img.setVisibility(View.VISIBLE);

                    } else {

                        ((RowViewHolder) holder).chat_img.setVisibility(View.GONE);
                    }


                    if (object.getIsLiveVideo().equalsIgnoreCase("1")) {
                        ((RowViewHolder) holder).ic_video.setVisibility(View.VISIBLE);
                    } else {

                        ((RowViewHolder) holder).ic_video.setVisibility(View.GONE);
                    }


//                    ((RowViewHolder) holder).chat_img.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            String clientID = GlobalClass.getPref("clientID", activity);
//
//
//                            Intent intent = new Intent(activity, ClientGetChat.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString("sendTo", object.getAdvisorID());
//                            bundle.putString("sendBy",clientID   );
//                            bundle.putString("preName", object.getName());
//                            bundle.putString("preImage", object.getImage());
//                            bundle.putString("preOnline", object.getIsOnline());
//                            intent.putExtras(bundle);
//                            activity.startActivity(intent);
//
//
//                        }
//                    });
//
//                    ((RowViewHolder) holder).ic_video.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            String videoLink = object.getProfileVideo();
//
//                            Intent videoIntent = new Intent(activity, AdvisorShowVideo.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString("videoLink", Urls.BASEURL + "" + videoLink);
//                            videoIntent.putExtras(bundle);
//                            activity.startActivity(videoIntent);
//
//
//                        }
//                    });


                    break;

            }
        }

    }

    public static double round(double value, int places) {
        //here 2 means 2 places after decimal
        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        CircleIndicator indicator;

        //        TextView txtType;
        ViewPager viewPager;

        public HeaderViewHolder(View itemView) {
            super(itemView);

//            this.txtType = (TextView) itemView.findViewById(R.id.type);
            indicator = (CircleIndicator) itemView.findViewById(R.id.indicator);
            viewPager = (ViewPager) itemView.findViewById(R.id.pager);
        }

    }

    public static class RowViewHolder extends RecyclerView.ViewHolder {


        public TextView title, designation_tv;
        public TextView designation;
        public ImageView imageview;
        public CardView cardView;
        private RatingBar ratingBar;
        public LinearLayout rootLayout;
        private ImageView chat_img, ic_video, online_img;

        public RowViewHolder(View itemView) {
            super(itemView);

            this.title = (TextView) itemView.findViewById(R.id.title);
            this.designation_tv = (TextView) itemView.findViewById(R.id.designation_tv);
            this.designation = (TextView) itemView.findViewById(R.id.designation);
            this.imageview = (ImageView) itemView.findViewById(R.id.imageViewUser);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating_bar);
            this.rootLayout = (LinearLayout) itemView.findViewById(R.id.rootLayout);
            this.chat_img = (ImageView) itemView.findViewById(R.id.chat_img);
            this.ic_video = (ImageView) itemView.findViewById(R.id.ic_video);
            this.online_img = (ImageView) itemView.findViewById(R.id.online_img);


        }

    }

    @Override
    public int getItemViewType(int position) {

        switch (arrayList.get(position).getType()) {
            case 0:
                return ClientPsychicModel.TYPE_HEADER;
            case 1:
                return ClientPsychicModel.TYPE_ROW;

            default:
                return -1;
        }


    }

    private void setupViewPager(RecyclerView.ViewHolder holder, ViewPager viewPager) {
//

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(fragmentManger);
        viewPagerAdapter.notifyDataSetChanged();
//        viewPagerAdapter.addFragment(new ClientPagerFragment(), "Fourth", "its Description", sampleimage, "4");


        for (int i = 0; i < featurePsychicArrayList.size(); i++) {


            String name = featurePsychicArrayList.get(i).getName();
            String description = featurePsychicArrayList.get(i).getDescription();
            String imageURl = featurePsychicArrayList.get(i).getImage();
            String ratting = featurePsychicArrayList.get(i).getRatting();
            String id = featurePsychicArrayList.get(i).getPsychicID();
            String isOnline = featurePsychicArrayList.get(i).getIsOnline();
            String serviceName = featurePsychicArrayList.get(i).getServiceName();
            String videoLink = featurePsychicArrayList.get(i).getProfileVideo();
            String isliveChat = featurePsychicArrayList.get(i).getIsLiveChat();
            String isVideoMsg = featurePsychicArrayList.get(i).getIsLiveVideo();
            String screenName = featurePsychicArrayList.get(i).getScreenName();
            viewPagerAdapter.addFragment(new ClientPagerFragment(), id, name, description, imageURl, ratting, isOnline, serviceName, videoLink, isliveChat, isVideoMsg,screenName);


        }


        viewPager.setAdapter(viewPagerAdapter);

        viewPager.setOffscreenPageLimit(featurePsychicArrayList.size());
        ((HeaderViewHolder) holder).indicator.setViewPager(viewPager);


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

        public void addFragment(Fragment fragment, String id, String name, String description, String image, String ratting, String isOnline, String serviceName, String videoLink, String isLiveChat, String isVideoMsg,String screenName) {
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putString("title", name);
            bundle.putString("image", image);
            bundle.putString("description", description);
            bundle.putString("ratting", ratting);
            bundle.putString("isOnline", isOnline);
            bundle.putString("serviceName", serviceName);
            bundle.putString("videoLink", videoLink);
            bundle.putString("isLiveChat", isLiveChat);
            bundle.putString("isVideoMsg", isVideoMsg);
            bundle.putString("screenName", screenName);

            fragment.setArguments(bundle);
            fragmentList.add(fragment);
            fragmentTitles.add(name);
        }
    }
}
