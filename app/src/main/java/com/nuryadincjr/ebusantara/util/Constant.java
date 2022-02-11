package com.nuryadincjr.ebusantara.util;

import static com.nuryadincjr.ebusantara.util.LocalPreference.getInstance;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.text.NumberFormat.getCurrencyInstance;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.nuryadincjr.ebusantara.pojo.Buses;
import com.nuryadincjr.ebusantara.pojo.ScheduleReference;
import com.nuryadincjr.ebusantara.pojo.Users;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Constant {
    public static final String COLLECTION_TRANSACTIONS = "transactions";
    public static final String COLLECTION_SCHEDULE = "schedule";
    public static final String COLLECTION_REVIEWS = "reviews";
    public static final String COLLECTION_BUSES = "buses";
    public static final String COLLECTION_CITIES = "cities";
    public static final String TITLE_VIW_ONLY = "View only";
    public static final String COLLECTION_USER = "users";
    public static final int RC_SIGN_IN = 0;

    public static Bitmap getQrCode(String code){
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = null;
        try {
            bitmap = barcodeEncoder.encodeBitmap(code, BarcodeFormat.QR_CODE, 700, 700);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @SuppressLint("SimpleDateFormat")
    public static Map<String, String>  getTime(ScheduleReference schedule) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatDate = new SimpleDateFormat("d MMM yyyy");

        Date departureDate = new Date();
        Date arrivalDate = new Date();
        try {
            departureDate = format.parse(schedule.getDepartureTime());
            arrivalDate = format.parse(schedule.getArrivalTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Map<String, String> map = new HashMap<>();
        map.put("departureDate", formatDate.format(departureDate));
        map.put("departureTime", formatTime.format(departureDate));

        map.put("arrivalDate", formatDate.format(arrivalDate));
        map.put("arrivalTime", formatTime.format(arrivalDate));

        return map;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getEstimatedTimes(ScheduleReference schedule) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date departureDate = new Date();
        Date arrivalDate = new Date();
        try {
            departureDate = format.parse(schedule.getDepartureTime());
            arrivalDate = format.parse(schedule.getArrivalTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long millisTime = arrivalDate.getTime() - departureDate.getTime();
        if(departureDate.getTime() > arrivalDate.getTime()){
            millisTime = departureDate.getTime() - arrivalDate.getTime();
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(millisTime) % 60;
        long hours = TimeUnit.MILLISECONDS.toHours(millisTime);
        String estimatedTime;
        if (hours > 0) {
            if(minutes>0){
                estimatedTime = hours+"h"+minutes+"m";
            }else estimatedTime = hours+"h";
        } else estimatedTime = minutes+"m";

        return estimatedTime;
    }

    @SuppressLint("SimpleDateFormat, DefaultLocale")
    public static int getIntEstimatedTimes(ScheduleReference schedule) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date departureDate = new Date();
        Date arrivalDate = new Date();
        try {
            departureDate = format.parse(schedule.getDepartureTime());
            arrivalDate = format.parse(schedule.getArrivalTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long millisTime = arrivalDate.getTime() - departureDate.getTime();
        if(departureDate.getTime() > arrivalDate.getTime()){
            millisTime = departureDate.getTime() - arrivalDate.getTime();
        }

        long minutes = MILLISECONDS.toMinutes(millisTime) % 60;
        long hours = MILLISECONDS.toHours(millisTime);
        String estimatedTime = format("%s%d", hours, minutes);

        return parseInt(estimatedTime);
    }

    public static Map<String, String> getPieces(Buses buses, String passengers) {
        String piece = buses.getPrice();
        double subTotal = parseDouble(passengers) * parseDouble(piece);

        DecimalFormat format = (DecimalFormat) getCurrencyInstance();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setCurrencySymbol("Rp");
        symbols.setMonetaryDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        format.setDecimalFormatSymbols(symbols);

        String displaySubTotal = passengers+"x"+format.format(parseDouble(piece));

        Map<String, String> map = new HashMap<>();
        map.put("displaySubTotal", displaySubTotal);
        map.put("subTotal", format.format(subTotal));

        return map;
    }

    public static Users getUsers(Context context) {
        SharedPreferences preference = getInstance(context).getPreferences();
        String uid = preference.getString("uid", "");
        String name = preference.getString("name", "");
        String email = preference.getString("email", "");
        String phone = preference.getString("phone", "");
        String photo = preference.getString("photo", "");

        return new Users(uid, name, phone, email, photo);
    }

    public static String toUpperCase(String item){
       return item.substring(0, 1).toUpperCase() + item.substring(1);
    }
}
