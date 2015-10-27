package com.suyonoion.loginexpanded;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.HashMap;

@SuppressWarnings("All")
public class LoginExpanded extends LinearLayout {
    // Deklarasi variable
    public Button button_buka,button_kunci,button_batal,button_set;
    public EditText editTxt_nama,editTxt_password;

    public LinearLayout konten;
    public LinearLayout form_login_expanded;

    // Sesi Manager Class
    SessionManager session;

    // Context
    private Context mContext;

    public LoginExpanded(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        new SettingsObserver(new Handler()).observe();
    }

    /* Mulai SettingsObserver class */
    class SettingsObserver extends ContentObserver{
        public SettingsObserver(Handler handler) {
            super(handler);
            // TODO Auto-generated constructor stub
        }
        void observe(){
            ContentResolver resolver = LoginExpanded.this.mContext.getContentResolver();
            // uri nama dan katasandi
            resolver.registerContentObserver(Settings.System.getUriFor("uri_nama"), false, this);
            resolver.registerContentObserver(Settings.System.getUriFor("uri_katasandi"), false, this);
            kodeUtama();
        }
        @Override
        public void onChange(boolean selfChange) {
            // TODO Auto-generated method stub
            //super.onChange(selfChange);
            kodeUtama(); //jalankan void kodeUtama()
        }
    }
    /* Selesai SettingsObserver class */


    /*
     ------------------------------
     ----------KODE UTAMA----------
     ------------------------------
    */
    private void kodeUtama() {
        // Sesi Manager
        session = new SessionManager(getContext());

        // temukan target view menurut id
        editTxt_nama = (EditText)findViewById(setResource("editTxt_nama","id"));
        editTxt_password = (EditText)findViewById(setResource("editTxt_password","id"));
        session.checkLogin();

        // Tombol Buka jika di klik mencoba mencocokkan username dan katasandi
        button_buka = (Button) findViewById(setResource("tombol_buka","id"));
        button_buka.setOnClickListener(new OnClickListener() {
            // deklarasi uri
            String string_txt_nama = Settings.System.getString(mContext.getContentResolver(), "uri_nama");
            String string_txt_katasandi = Settings.System.getString(mContext.getContentResolver(), "uri_katasandi");
            @Override
            public void onClick(View v) {
                // default username dan password
                String default_txt_nama = "suyonoion";
                String default_txt_password = "maaadgroup.com";
                //jika uri pref masih belum diset ganti nama/sandi baru (uri default/kosong)
                if (TextUtils.isEmpty(string_txt_nama)) {

                    // jika default nama dan default katasandi COCOK maka terbuka kuncinya kemudian tampilkan konten
                    if (editTxt_nama.getText().toString().equals(default_txt_nama) && editTxt_password.getText().toString().equals(default_txt_password)) {
                        tampilSukses();
                    } else { //lainnya tampilkan dialog
                        pesanPemberitahuan("Login Gagal, Coba Lagi ?");
                    }

                } else {
                // lainnya lakukan jika default nama dan password diganti diset
                    if (editTxt_nama.getText().toString().trim().length() > 0 && editTxt_password.getText().toString().trim().length() > 0) {
                        //jika cocok
                        if (editTxt_nama.getText().toString().equals(string_txt_nama) && editTxt_password.getText().toString().equals(string_txt_katasandi)) {
                            session.createLoginSession(string_txt_nama, string_txt_katasandi);
                            tampilSukses();
                        } else {
                            // lainnya username/password tidak cocok
                            pesanPemberitahuan("Login gagal..!! nama dan kata sandi tidak cocok... tolong ketik secara hati-hati gan.");
                        }
                    } else {
                        // pengguna tidak memasukkan username atau password
                        // tampilkan peringatan
                        pesanPemberitahuan("Tolong masukkan username dan kata sandinya..!");
                    }
                }
            }
        });

        // Tombol kunci (logout) jika diklik
        button_kunci = (Button) findViewById(setResource("tombol_logout","id"));
        button_kunci.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
            }
        });

        // Tombol batal jika di klik statusbar expanded menutup dan abaikan semuanya
        button_batal = (Button) findViewById(setResource("tombol_batal","id"));
        button_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Object service = getContext().getSystemService("statusbar");
                    Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                    if (currentapiVersion <= 16) {
                        Method collapse = statusbarManager.getMethod("collapse");
                        collapse.invoke(service);
                    } else {
                        Method collapse2 = statusbarManager.getMethod("collapsePanels");
                        collapse2.invoke(service);
                    }

                } catch (Exception ex) {
                }
            }
        });

        // Tombol set jika di klik menjalankan activity SetNamaKataSandi.java
        // lalu statusbar expanded menutup
        button_set = (Button) findViewById(setResource("tombol_set","id"));
        button_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getContext(), SetNamaKataSandi.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                v.getContext().startActivity(intent);
                try {
                    Object service = getContext().getSystemService("statusbar");
                    Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                    if (currentapiVersion <= 16) {
                        Method collapse = statusbarManager.getMethod("collapse");
                        collapse.invoke(service);
                    } else {
                        Method collapse2 = statusbarManager.getMethod("collapsePanels");
                        collapse2.invoke(service);
                    }
                } catch (Exception ex) {
                }
            }
        });

    }

        /*
         ------------------------------
         --------- SESI MANAJER--------
         ------------------------------
        */
    public class SessionManager {
        // Shared Preferences
        SharedPreferences pref;
        // Editor untuk Shared preferences
        SharedPreferences.Editor editor;
        // Context
        Context _context;
        // Mode Shared pref
        int PRIVATE_MODE = 0;
        // nama Sharedpref
        private static final String PREF_NAME = "LoginExpandedPref";

        // ------------------
        // Defenisi semua Key
        private static final String IS_LOGIN = "IsLoggedIn";
        // User name / nama / nick
        public static final String KEY_NAME = "name";
        // Katasandi / password / katakunci
        public static final String KEY_PASSWORD = "password";
        // ------------------

        // Constructor
        public SessionManager(Context context){
            this._context = context;
            pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            editor = pref.edit();
        }

        /**
         * Membuat sesi login buat masuk
         * */
        public void createLoginSession(String name, String password){
            // nyimpan nilai login sebagai TRUE
            editor.putBoolean(IS_LOGIN, true);
            // nyimpan nama username di pref
            editor.putString(KEY_NAME, name);
            // nyimpan katasandi password di pref
            editor.putString(KEY_PASSWORD, password);
            // commit editor
            editor.commit();
        }

        /**
         * Check status login
         * jika tidak dalam posisi login, sembunyikan konten dan munculkan form login
         * lainnya (dalam posisi login) tampilkan konten (konten muncul ketika login berhasil)
         * */

        public void checkLogin(){

            if(!this.isLoggedIn()){ // jika tidak
                konten = (LinearLayout) findViewById(setResource("konten_sukses","id"));
                form_login_expanded = (LinearLayout) findViewById(setResource("form_login_expanded","id"));
                // jika user tidak dalam posisi logged
                konten.setVisibility(View.GONE); // sembunyikan konten
                form_login_expanded.setVisibility(View.VISIBLE); // munculkan form login
                clearForm(form_login_expanded); // kosongkan form
            } else { //selain itu (dalam posisi login) tampilkan konten sukses
                tampilSukses();
            }
        }


        /**
         * Mendapatkan data sesi
         * */
        public HashMap<String, String> getUserDetails(){
            HashMap<String, String> user = new HashMap<String, String>();
            // username / nama
            user.put(KEY_NAME, pref.getString(KEY_NAME, null));
            user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
            // tampilkan user
            return user;
        }

        /**
         * Clear hapus sesi lengkap
         * logout (kunci expanded)
         * */
        public void logoutUser(){
            form_login_expanded = (LinearLayout) findViewById(setResource("form_login_expanded","id"));
            // Clear / hapus sesi Shared Preferences
            editor.clear();
            editor.commit();
            kodeUtama(); //jalankan kodeUtama
            clearForm(form_login_expanded); //kosongkan form login
        }

        /**
         * Cek cepat untuk login / masuk
         * **/
        // apakah dalam kondisi login?
        public boolean isLoggedIn(){
            return pref.getBoolean(IS_LOGIN, false); //default false (tidak)
        }
    }

    // Void menampilkan konten
    private void tampilSukses() {
        konten  = (LinearLayout) findViewById(setResource("konten_sukses","id"));
        form_login_expanded = (LinearLayout) findViewById(setResource("form_login_expanded","id"));
        konten.setVisibility(View.VISIBLE);
        form_login_expanded.setVisibility(View.GONE);
    }


    // Void kosongkan form
    private void clearForm(ViewGroup group)
    {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }
    }

    // Identifier agar tidak mencocokkan id public = mengubahnya ke tipe string
    public int setResource(String name, String Type)
    {
        return getContext().getResources().getIdentifier(name, Type, getContext().getPackageName());
    }

    // alert dialog
    // dialog pesan peringatan
    public void pesanPemberitahuan(String pesan){
        // custom dialog
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(setResource("lay_error_loginexpanded","layout"));
        //judul dialog
        dialog.setTitle("Oooppsss...!");
        // set pesan dan tombol OK
        TextView text = (TextView) dialog.findViewById(setResource("pesan_alert_dialog","id"));
        text.setText(pesan);
        Button dialogButton = (Button) dialog.findViewById(setResource("tombol_ok_alertdialog","id"));
        // jika tombol diklik, tutup dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show(); // dialog ditampilkan
    }
}
