package com.tmm.android.rssreader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.step.launcher.R;

public  class RssListAdapter extends BaseAdapter {

 private LayoutInflater inflater;
private int resource;
private ArrayList<JSONObject> data;

/* public RssListAdapter(Context newspaperFragment, List<JSONObject> imageAndTexts) {
    super(newspaperFragment, 0, imageAndTexts);
  }*/

	public RssListAdapter(Context context, int resource, ArrayList<JSONObject> data) {
		
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.resource = resource;
		this.data = data;
		
	}
	

//@Override
  public View getView(int position, View convertView, ViewGroup parent) {
	  
	  View view;
	  
	  if(convertView == null) {
		  view = this.inflater.inflate(resource, parent, false);
	  } else {
		  view = convertView;
	  }
	  
	  try {
		return this.bindData(view, position);
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return view;

    //Activity activity = (Activity) getContext();
    //LayoutInflater inflater = activity.getLayoutInflater();

	  
    // Inflate the views from XML
	  
//	  View rowView = inflater.inflate(R.layout.newspaper_fragment, null);
//	  if(convertView ==null)
//	  {
//    
//    JSONObject jsonImageText = (JSONObject) getItem(position);

    // ////////////////////////////////////////////////////////////////////////////////////////////////////
    // The next section we update at runtime the text - as provided by the JSON
    // from our REST call
    // //////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    

    //TextView textView = (TextView) rowView.findViewById(R.id.job_text);
    //textView.setMovementMethod(LinkMovementMethod.getInstance());
//    Drawable d;
//    String url = "";
//    //ImageView imageView = (ImageView) rowView.findViewById(R.id.feed_image);
//    try {
//
//      if (jsonImageText.get("imageLink") != null) {
//        textView.setMovementMethod(LinkMovementMethod.getInstance());
//        
//        System.out.println("XXXX Link found!");
//        url = (String) jsonImageText.get("imageLink");
//        System.out.println("the link is " + url);
//        String delimiter = ".jpg";
//        String k;
//        k = url.split(delimiter)[0];
//        url = k + ".jpg";
//        URL feedImage= new URL(url);
//        
//        HttpURLConnection conn= (HttpURLConnection)feedImage.openConnection();
//        InputStream is = conn.getInputStream();
//        Bitmap img = BitmapFactory.decodeStream(is);
// 
//         
//        
//      //  imageView.setImageBitmap(img);
//        
//        //CharSequence img =
//        //    Html.fromHtml("<p> <img src="+url+"\"board\"/></p>");
//          //textView.append(img);
//        //textView.setMovementMethod(LinkMovementMethod.getInstance());
//        //SpannableString ss = new SpannableString("abc");
//
//        /*
//         * URL feedImage= new URL(url); Drawable img =
//         * Drawable.createFromPath(imagePath); img.setBounds(0,0, 50,50);
//         * HttpURLConnection conn=
//         * (HttpURLConnection)feedImage.openConnection(); InputStream is =
//         * conn.getInputStream(); Bitmap img = BitmapFactory.decodeStream(is);
//         */
//
//      }
//
//      //d = drawableFromUrl(url);
//      
//      Spanned text = (Spanned) jsonImageText.get("text");
//      //Log.e("RSSListAdapter", "the text is" + text);
//      textView.setText(text);
//      //textView.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
//      
//    }
//    
//      catch (MalformedURLException e) { //handle exception here - in case of
//     // invalid URL being parsed //from the RSS feed item } catch (IOException e)
//     //* { //handle exception here - maybe no access to web }
//      }
//    catch (JSONException e) {
//      textView.setText("JSON Exception");
//    } catch (IOException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
//
//    
//	  }
//	  else
//	  {
//		  
//		  rowView = convertView;
//		  
//	  }
//	  Log.v("the rowVIew says","the rowView says" + rowView.toString());
//	  
//	  return this.bindData(rowView, position);
//
//  }
//  public static Drawable drawableFromUrl(String url) throws IOException {
//    Bitmap x;
//
//    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
//    connection.connect();
//    InputStream input = connection.getInputStream();
//
//    x = BitmapFactory.decodeStream(input);
//    return new BitmapDrawable(x);
}

public int getCount() {
	// TODO Auto-generated method stub
	
	return this.data.size();
}

public Object getItem(int position) {
	return this.data.get(position);
	// TODO Auto-generated method stub
}

public long getItemId(int position) {
	// TODO Auto-generated method stub
	return position;
}




/**
 * Bind the provided data to the view
 * This is the only method not required by base adapter.		
 * @throws JSONException 
 */
public View bindData(View view, int position) throws JSONException{
	// make sure it's worth drawing the view
	if (this.data.get(position) == null){
		return view;
	}
	
	// pull out the object
	JSONObject item = this.data.get(position);
	Spanned text = (Spanned) item.get("header");
//  Spanned text = (Spanned) jsonImageText.get("text");
//  //Log.e("RSSListAdapter", "the text is" + text);
//  textView.setText(text);
	View viewElement = view.findViewById(R.id.newsList_text);
	TextView tv = (TextView) viewElement;
	
	tv.setText(text);
	
	// Extract the view object
	//View viewElement = view.findViewById(R.id.job_text);
	// cast to the correct type
	//TextView tv = (TextView) viewElement;
	// set the value
	//tv.setText(item.toString());
	
	/*viewElement = view.findViewById(R.id.emailList_from);
	tv = (TextView) viewElement;
	tv.setText(item.from);
	
	viewElement = view.findViewById(R.id.emailList_date);
	tv = (TextView) viewElement;
	tv.setText(item.date);
	*/
	
	// return final view object
	return view;
}




 
}