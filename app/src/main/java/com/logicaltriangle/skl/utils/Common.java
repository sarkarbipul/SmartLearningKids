package com.logicaltriangle.skl.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Common {
    private Context context;
    public Common(Context context) {
        this.context = context;
    }

    public boolean isNetWorkOk() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return ( networkInfo != null && networkInfo.isConnected() );
    }

    //http://mstpharmabd.com/apps/smart_kids_learning/Upload_images/item_img_1583129713.png
    //Must be called if link is valid
    public String getFileNameFromUrl(String fileUrl) {
        if (fileUrl != null)
            if (!fileUrl.isEmpty()) {
                if (fileUrl.contains("/")) {
                    String fileName = fileUrl.substring( fileUrl.lastIndexOf("/") + 1 );
                    if (!fileName.isEmpty()){
                        if (fileName.contains("/"))
                            fileName.replace("/", "");
                        if (fileName.contains("."))
                            return fileName;
                    }
                }
            }
        return "";
    }

    //Checking validity of file link
    private boolean isValidFileLink(String fileUrl) {
        if (fileUrl != null)
            if (!fileUrl.isEmpty()) {
                if (fileUrl.contains("/")) {
                    String fileName = getFileNameFromUrl(fileUrl);
                    if (!fileName.isEmpty())
                        if (fileName.contains("."))
                            return true;
                }
            }

        return false;
    }
}
