package com.PDA.gmax;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.PDA.gmax.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private ArrayList<JSONObject> arr_menu = null;

    /**
     * TODO : 어댑터의 동작원리 및 순서
     1.(getItemCount) 데이터 개수를 세어 어댑터가 만들어야 할 총 아이템 개수를 얻는다.
     2.(getItemViewType)[생략가능] 현재 itemview의 viewtype을 판단한다
     3.(onCreateViewHolder)viewtype에 맞는 뷰 홀더를 생성하여 onBindViewHolder에 전달한다.
     4.(onBindViewHolder)뷰홀더와 position을 받아 postion에 맞는 데이터를 뷰홀더의 뷰들에 바인딩한다.
     */

    public interface OnItemClickListener {
        void onItemClick(View v, int position, JSONObject menuName);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    MenuAdapter(ArrayList<JSONObject> list) {
        arr_menu = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_view1, tv_view2;

        ViewHolder(final View itemView) {
            super(itemView);
            tv_view1 = (TextView) itemView.findViewById(R.id.tv_item);
            tv_view2 = (TextView) itemView.findViewById(R.id.tv_item2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            JSONObject menu = arr_menu.get(position);
                            mListener.onItemClick(v, position, menu);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.menu_recycler_item, parent, false);
        MenuAdapter.ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject menu = arr_menu.get(position);
            final String menu_nm = menu.getString("MENU_NM");
            final String menu_id = menu.getString("MENU_ID");
            holder.tv_view1.setText(menu_nm);
            holder.tv_view2.setText(menu_id);
        } catch (JSONException exJson) {
            TGSClass.AlertMessage(holder.itemView.getContext(), "JSON ERROR");
        }
    }

    @Override
    public int getItemCount() {
        return arr_menu.size();
    }
}
