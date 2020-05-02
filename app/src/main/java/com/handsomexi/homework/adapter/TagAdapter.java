package com.handsomexi.homework.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handsomexi.homework.R;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    private List<String> tagList1=new ArrayList<>();
    private List<String> tagList;
    private SparseBooleanArray stateCheckedMap = new SparseBooleanArray();
    private Context context;

    public TagAdapter(Context context, List<String> tagList) {

        tagList1.clear();
        tagList1.addAll(tagList);
        this.tagList = tagList1;
        this.context=context;
        stateCheckedMap.clear();
        for(int i=0;i<tagList1.size();i++)
            stateCheckedMap.put(i,false);

    }

    public interface Textviewclick{
        void textviewclick(int i);
    }
    private Textviewclick mTextviewclick;
    public void setTextviewclick(Textviewclick mTextviewclick){
        this.mTextviewclick=mTextviewclick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tag, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTextView.setText(tagList.get(position));
        holder.itemView.setTag(tagList.get(position));
        holder.mTextView.setOnClickListener(v -> {
            for(int i = 0; i <tagList.size();i++){
                stateCheckedMap.put(i,false);
            }
            stateCheckedMap.put(position,true);
            TagAdapter.this.notifyDataSetChanged();
            mTextviewclick.textviewclick(position);

        });
        if(stateCheckedMap.valueAt(position)){
            holder.mTextView.setBackgroundResource(R.drawable.tag_checked_bg);
        }else{
            holder.mTextView.setBackgroundResource(R.drawable.tag_normal_bg);
        }

    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mTextView = view.findViewById(R.id.tag_tv);
        }
    }
}
