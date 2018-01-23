/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.basecode.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rengwuxian.materialedittext.validation.METValidator;

import ke.co.toshngure.basecode.annotations.GsonAvoid;


/**
 * Created by Anthony Ngure on 17/02/2017.
 * Email : anthonyngure25@gmail.com.
 */

public class BaseUtils {

    //stWyc&Y3bsb3M9

    public static float density = 1;
    public static Point displaySize = new Point();
    private static int screenHeight = 0;
    private static int screenWidth = 0;


    public static void tintProgressBar(@NonNull ProgressBar progressBar, @ColorInt int color) {
        progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    public static void tintImageView(ImageView imageView, @ColorInt int color) {
        imageView.setColorFilter(color);
    }


    @SuppressLint("MissingPermission")
    public static void makeCall(Context context, String phone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));
        if (callIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(callIntent, "Make call..."));
        } else {
            Toast.makeText(context, "Unable to find a calling application.", Toast.LENGTH_SHORT).show();
        }
    }


    public static void sendSms(Context context, String msg) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(intent, "Send sms..."));
        } else {
            Toast.makeText(context, "Unable to find a messaging application.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void shareText(Context context, String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        Intent intent = Intent.createChooser(sendIntent, "Share with...");
        context.startActivity(intent);
    }

    public static void sendEmail(Context context, String emailAddress, String subject, String body) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", emailAddress, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }


    public static void cacheInput(@NonNull EditText editText, @StringRes final int key, final PrefUtilsImpl prefUtils) {
        String currentInput = prefUtils.getString(key);
        editText.setText(currentInput);
        editText.setSelection(currentInput.length());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                prefUtils.writeString(key, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public static int getScreenHeight(Context context) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context context) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }


    public static boolean canConnect(@NonNull Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr != null ? connMgr.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }

    public static int generateColor(Object object) {
        return ColorGenerator.MATERIAL.getColor(object);
    }

    public static TextDrawable getTextDrawableAvatar(String text, @Nullable Object object) {
        if (object == null) {
            object = text;
        }
        return TextDrawable.builder().round().build(text, generateColor(object));
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static Gson getSafeGson() {
        return new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                return ((fieldAttributes.getAnnotation(GsonAvoid.class) != null));
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        }).create();
    }

    public static Gson getSafeGson(final String... avoidNames) {
        return new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                for (String avoidName : avoidNames) {
                    if (fieldAttributes.getName().equals(avoidName)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        }).create();
    }

    public static METValidator createEmailValidator(String errorMsg) {
        return new METValidator(errorMsg) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty && Patterns.EMAIL_ADDRESS.matcher(text).matches();
            }
        };
    }

    public static METValidator createPhoneValidator(String errorMsg) {
        return new METValidator(errorMsg) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty && Patterns.PHONE.matcher(text).matches();
            }
        };
    }

    public static METValidator createRequiredValidator(String errorMsg) {
        return new METValidator(errorMsg) {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        };
    }

    public static METValidator createLengthValidator(final int length) {
        return new METValidator("You must input " + length + " characters") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty && (text.length() == length);
            }
        };
    }

    /**
     * Get the color value for the given color attribute
     */
    @ColorInt
    public static int getColor(Context context, @AttrRes int colorAttrId) {
        int[] attrs = new int[]{colorAttrId /* index 0 */};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        int colorFromTheme = ta.getColor(0, 0);
        ta.recycle();
        return colorFromTheme;
    }

    public static void tintMenu(Activity activity, Menu menu, @ColorInt int color) {
        if (menu != null && menu.size() > 0) {
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                Drawable icon = item.getIcon();
                if (icon != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        icon.setTint(color);
                    } else {
                        icon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    }
                }
            }
        }
    }

}
