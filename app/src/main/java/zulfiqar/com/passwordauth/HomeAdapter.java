package zulfiqar.com.passwordauth;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sdsmdg.tastytoast.TastyToast;
import com.tomer.fadingtextview.FadingTextView;

import java.util.Calendar;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>  {


    private List<HomeInitialiser> listitem;
    private Context context;
    private View myview;
    private  boolean change = true;


    public HomeAdapter(Context context, List<HomeInitialiser> listitem) {
        this.listitem = listitem;
        this.context = context;
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final HomeInitialiser homeInitialiser = listitem.get(position);



        String headerGet = homeInitialiser.getHeading();

        holder.headings.setText(""+headerGet);



        if(homeInitialiser.getLink().equals(""))
        {
            holder.links.setVisibility(View.GONE);
        }
        else {
            holder.links.setVisibility(View.VISIBLE);
        }
        holder.links.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMedia();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(homeInitialiser.getLink()));
                context.startActivity(i);
            }
        });

//        TastyToast.makeText(context,""+homeInitialiser.getLink(),Toast.LENGTH_LONG,TastyToast.INFO);

        holder.descriptions.setText(""+homeInitialiser.getDescription());

        if(homeInitialiser.getDescription().length()>100)
        {
            holder.more.setVisibility(View.VISIBLE);
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    getMedia();
                    getTextChange(holder);
                }
            });

        }
        else {

            holder.more.setVisibility(View.GONE);
        }
        holder.cimages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMedia();
                Intent intt = new Intent(context,Fundamentals.class);
                intt.putExtra("link",""+homeInitialiser.getUri());
                context.startActivity(intt);
            }
        });
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMedia();
                setURl(homeInitialiser.getUri(),homeInitialiser);
            }
        });
        boolean isphoto = homeInitialiser.getUri() != null;

        if(isphoto)
        {
            Glide.with(context).load(homeInitialiser.getUri()).into(holder.cimages);
        }
        else {
            holder.cimages.setVisibility(View.GONE);
        }
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getMedia();

                try {
                    Intent share =    shareImageData(context ,""+homeInitialiser.getHeading(), ""+homeInitialiser.getUri() ,""+homeInitialiser.getDescription(),homeInitialiser.getLink());
                    context.startActivity(Intent.createChooser(share, "choose one"));
                }
                catch (Exception e){e.printStackTrace();}

            }
        });


        holder.dates.setText(""+homeInitialiser.getDate());
        holder.dates.setTextColor(Color.GRAY);

    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView headings  ,more;
        public TextView dates;
        public TextView  descriptions;
        public ImageView cimages,download,links;
        public RelativeLayout relay;
        public ImageView share;
        public WebView webView;
        public  int event;


        public ViewHolder(View itemView) {
            super(itemView);

            myview = itemView;
            download = (ImageView) itemView.findViewById(R.id.downloadbut);
            dates = (TextView) itemView.findViewById(R.id.date);
            more = (TextView) itemView.findViewById(R.id.more);
            headings = (TextView) itemView.findViewById(R.id.main_head);
            descriptions = (TextView) itemView.findViewById(R.id.description);
            cimages = (ImageView) itemView.findViewById(R.id.CircularImageOntop);
            relay = (RelativeLayout) itemView.findViewById(R.id.Layout_inCard);
            share = (ImageView) itemView.findViewById(R.id.share);
            webView = (WebView) itemView.findViewById(R.id.watsapp);
            links = (ImageView) itemView.findViewById(R.id.linkImage);

        }
    }



    private void setURl(String downloadUrl, HomeInitialiser homeInitialiser) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));

        request.allowScanningByMediaScanner();

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+homeInitialiser.getHeading().trim()+".png");

        DownloadManager dm = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);

        dm.enqueue(request);

        TastyToast.makeText(context,"downloading file",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS).show();
    }
    public static Intent shareImageData(Context context, String header, String link, String description, String linkOpen) {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        if (Build.VERSION.SDK_INT  < Build.VERSION_CODES.LOLLIPOP) {

            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }
        else {
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        }


        String textins = "";


            textins = "Al Ansaar Home Recommendations "+"\n\nAl Ansaar has New Feed in Home";

        String applink = Constants.appLink ;
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, header);
        String sAux ="\n"+" بسم الله الرحمن الرحيم " + "\n\n" + textins+"\n\n" +header+"\n";
        sAux = sAux+"\n"+description+"\n";
        if(link.equals(""))
        {
        }
        else {
            sAux = sAux +"\n follow link for open image \n"+ link +"\n";
        }
        if(linkOpen.equals(""))
        {
        }
        else {
            sAux = sAux +"\n follow link for more information \n"+ linkOpen +"\n";
        }

        sAux = sAux +"\nFollow link to download Al Ansaar (Spreading peace in the world)\n"+applink+"\n\n";
        shareIntent.putExtra(Intent.EXTRA_TEXT, sAux);

        return shareIntent;
    }

    private void getTextChange(ViewHolder holder) {
        if (change) {

            holder.descriptions.setMaxLines(Integer.MAX_VALUE);
            holder.more.setText("show less");
            change = false;
        } else {

            holder.more.setText("show more");
            change = true;
            holder.descriptions.setMaxLines(3);

        }
    }
    public String getSystemDate() {

        Calendar calendar =Calendar.getInstance();

        return ""+calendar.getTime();
    }
    public void getMedia() {
        final MediaPlayer mp = MediaPlayer.create(context,R.raw.tweet);
        mp.start();
    }
}
