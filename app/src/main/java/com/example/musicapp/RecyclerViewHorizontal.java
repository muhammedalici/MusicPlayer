package com.example.musicapp;


import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


public class RecyclerViewHorizontal extends RecyclerView.Adapter<RecyclerViewHorizontal.MyViewHolder> {

    private int isDelete=0;
    private Context mContext ;
    private List<Model_musicNamePic> mData ;
    ArrayList<String> muzikler;



    public RecyclerViewHorizontal(Context mContext, List<Model_musicNamePic> mData)  {
        this.mContext = mContext;
        Collections.reverse(mData);
        List<Model_musicNamePic> unicData ;
        unicData = new ArrayList<Model_musicNamePic>();
        for (int i=0; i<mData.size();i++){
            isDelete=0;
            if(i==0){
                unicData.add(mData.get(i));
            }
            else {
                for(int j=0;j<unicData.size();j++){
                    if(mData.get(i).getIsim().equals(unicData.get(j).getIsim())){
                        isDelete=1;
                    }
                }
                if(isDelete==0){
                    unicData.add(mData.get(i));
                }
            }
        }


        this.mData = unicData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_sarki_list_horizontal,parent,false);
        return new MyViewHolder(view);

    }




    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {

        holder.title.setText(mData.get(position).getIsim());
        holder.image.setImageResource(mData.get(position).getResim());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent intent = new Intent(mContext,siparisDetailActivity.class);

                intent.putExtra("Urun_Ad",mData.get(position).getUrun_Ad());
                intent.putExtra("Urun_Miktar",mData.get(position).getUrun_Miktar());

                mContext.startActivity(intent);*/
                Intent intnt = new Intent(mContext,PlayScreenActivity.class);
                muzikler=new ArrayList<String>();
                for (int s =0; s<mData.size();s++){
                    muzikler.add(mData.get(s).getIsim());
                }
                intnt.putStringArrayListExtra("sarkilar",muzikler);
                int index=0;
                index=position;
                intnt.putExtra("index",String.valueOf(index));
                mContext.startActivity(intnt);


            }
        });


    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView image;
        CardView cardView ;




        public MyViewHolder(View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.card_hori_img);
            title=itemView.findViewById(R.id.card_hori_txt);
            cardView = (CardView) itemView.findViewById(R.id.card_hori_card);

        }
    }


}
