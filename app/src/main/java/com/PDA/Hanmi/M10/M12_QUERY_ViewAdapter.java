package com.PDA.Hanmi.M10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.PDA.Hanmi.R;

import java.util.ArrayList;

public class M12_QUERY_ViewAdapter extends RecyclerView.Adapter<M12_QUERY_ViewAdapter.ViewHolder> {

    private ArrayList<M12_QUERY> arr_menu = null;

    /**
     * TODO : 어댑터의 동작원리 및 순서
     1.(getItemCount) 데이터 개수를 세어 어댑터가 만들어야 할 총 아이템 개수를 얻는다.
     2.(getItemViewType)[생략가능] 현재 itemview의 viewtype을 판단한다
     3.(onCreateViewHolder)viewtype에 맞는 뷰 홀더를 생성하여 onBindViewHolder에 전달한다.
     4.(onBindViewHolder)뷰홀더와 position을 받아 postion에 맞는 데이터를 뷰홀더의 뷰들에 바인딩한다.
     */

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    M12_QUERY_ViewAdapter(Context pContext, ArrayList<M12_QUERY> list) {
        arr_menu = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView INSPECT_REQ_NO;
        TextView MVMT_RCPT_DT;
        TextView INSPECT_GOOD_QTY;
        TextView INSPECT_BAD_QTY;
        TextView BP_NM;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            INSPECT_REQ_NO      = itemView.findViewById(R.id.INSPECT_REQ_NO);
            MVMT_RCPT_DT        = itemView.findViewById(R.id.MVMT_RCPT_DT);
            INSPECT_GOOD_QTY    = itemView.findViewById(R.id.INSPECT_GOOD_QTY);
            INSPECT_BAD_QTY     = itemView.findViewById(R.id.INSPECT_BAD_QTY);
            BP_NM               = itemView.findViewById(R.id.BP_NM);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            M12_QUERY menu = arr_menu.get(position);
                            mListener.onItemClick(v, position);
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

        View view = inflater.inflate(R.layout.list_view_m12_query, parent, false);
        M12_QUERY_ViewAdapter.ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder)holder;

        viewHolder.INSPECT_REQ_NO.setText(arr_menu.get(position).getINSPECT_REQ_NO());
        viewHolder.MVMT_RCPT_DT.setText(arr_menu.get(position).getMVMT_RCPT_DT());
        viewHolder.INSPECT_GOOD_QTY.setText(arr_menu.get(position).getINSPECT_GOOD_QTY());
        viewHolder.INSPECT_BAD_QTY.setText(arr_menu.get(position).getINSPECT_BAD_QTY());
        viewHolder.BP_NM.setText(arr_menu.get(position).getBP_NM());
        /*
        M12_QUERY menu = arr_menu.get(position);
        final String menu_nm = menu.getString("MENU_NM");
        final String menu_id = menu.getString("MENU_ID");
        holder.INSPECT_REQ_NO.setText(menu_nm);
        holder.MVMT_RCPT_DT.setText(menu_id);
        holder.INSPECT_GOOD_QTY.setText(menu_id);
        holder.INSPECT_BAD_QTY.setText(menu_id);
        holder.BP_NM.setText(menu_id);
        */
    }

    @Override
    public int getItemCount() {
        return arr_menu.size();
    }
}
