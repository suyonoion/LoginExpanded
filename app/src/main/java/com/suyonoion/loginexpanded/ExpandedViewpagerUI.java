package com.suyonoion.loginexpanded;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suyono on 5/26/2015.
 * Copyright (c) 2015 by Suyono (ion).
 * All rights reserved.
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */
public class ExpandedViewpagerUI extends ViewPager {
    // Identifier agar tidak mencocokkan id public = mengubahnya ke tipe string
    public int setResource(String name, String Type)
    {
        return getContext().getResources().getIdentifier(name, Type, getContext().getPackageName());
    }

    public ExpandedViewpagerUI (Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            // array List string , daftar list judul
            List<String> mengisi_Tempat_judul = new ArrayList<>();
            /* judul page1
            * listjudul.tambahkan(dapakandarifolderRes.dapatkanStringDari(name="str_notifikasi",bertype="string")
            * */
            mengisi_Tempat_judul.add(getResources().getString(setResource("str_notifikasi","string")));
            /* judul page2
            * listjudul.tambahkan(dapakandarifolderRes.dapatkanStringDari(name="str_page2",bertype="string")
            * */
            mengisi_Tempat_judul.add(getResources().getString(setResource("str_page2","string")));
            /* judul page3
            * listjudul.tambahkan(dapakandarifolderRes.dapatkanStringDari(name="str_page3",bertype="string")
            * */
            mengisi_Tempat_judul.add(getResources().getString(setResource("str_page3","string")));

            //mengisi_Tempat_judul masukkan ke adapter
            AdapterExpandedViewpagerUI adapter = new AdapterExpandedViewpagerUI(mengisi_Tempat_judul);
            // Viewpager id = temukan target view menurut id-nya
            final ViewPager EasyExpandedViewPagerUI = (ViewPager) this.findViewById(setResource("id_viewpager", "id"));
            // membuat pageTransformer/efek transisi
            PageTransformer animasi = new PageTransformer() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB) //target api
                @Override
                public void transformPage(View view, float position) {
                    float ion = Float.valueOf("90"); //rotasi 90 derajat
                    final float rotation = (position < 0 ? ion : -ion) * Math.abs(position);
                    view.setAlpha(rotation > ion || rotation < -ion ? 0f : 1f);
                    view.setPivotX(position < 0f ? view.getWidth() : 0f);
                    view.setPivotY(view.getHeight() * 0.5f);
                    view.setRotationY(ion * position);
                }
            };
            //setPageTransformer Viewpagernya = animasi
            EasyExpandedViewPagerUI.setPageTransformer(true, animasi);
            EasyExpandedViewPagerUI.setAdapter(adapter);
            EasyExpandedViewPagerUI.setOffscreenPageLimit(3); //jumlah limit pages = 3
        }
    }

    public class AdapterExpandedViewpagerUI extends PagerAdapter {
        private List<String> adapterMengisi_Tempat_judul;
        public AdapterExpandedViewpagerUI(List<String> adapterMengisi_Tempat_judul) {
            this.adapterMengisi_Tempat_judul = adapterMengisi_Tempat_judul;
        }

        // jumlah pages = 3
        @Override
        public int getCount () {
            return 3;
        }

        @Override
        public Object instantiateItem (ViewGroup container,int position){
            int tampilkanMenurutId = 0;
            //definisikan page1, page2, page3
            switch (position) {
                case 0:
                    tampilkanMenurutId = setResource("id_halaman_1","id");

                    break;
                case 1:
                    tampilkanMenurutId = setResource("id_halaman_2","id");
                    break;
                case 2:
                    tampilkanMenurutId = setResource("id_halaman_3","id");
                    break;
            }
            // kemudian tampilkan
            return findViewById(tampilkanMenurutId);

        }

        //mendapatkan judul viewpager
        @Override
        public CharSequence getPageTitle ( int position){
            return adapterMengisi_Tempat_judul.get(position);
        }

        @Override
        public boolean isViewFromObject (View arg0, Object arg1){
            return arg0 ==  arg1;
        }

        @Override
        public Parcelable saveState () {
            return null;
        }
    }
}
