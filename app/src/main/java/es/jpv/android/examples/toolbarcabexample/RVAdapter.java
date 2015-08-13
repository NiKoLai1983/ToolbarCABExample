package es.jpv.android.examples.toolbarcabexample;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ItemViewHolder> {

    Context mContext;
    Cursor mCursor;
    OnItemClickListener onItemClickListener;
    OnItemLongClickListener onItemLongClickListener;

    RVAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    public void setCursor(Cursor cursor) {
        if (null != mCursor) {
            mCursor.close();
        }
        this.mCursor = cursor;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.rv_item, viewGroup, false);
        ItemViewHolder ivh = new ItemViewHolder(v, onItemClickListener, onItemLongClickListener);
        return ivh;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
        mCursor.moveToPosition(i);
        itemViewHolder.itemName.setText(mCursor.getString(1));
        itemViewHolder.position = i;
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public static interface OnItemClickListener {
        public void onItemClick(View v, int position);
    }

    public static interface OnItemLongClickListener {
        public void onItemLongClick(View v, int position);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener {

        LinearLayout linearLayout;
        TextView itemName;
        int position;
        OnItemClickListener mClickListener;
        OnItemLongClickListener mLongClickListener;

        ItemViewHolder(
                View itemView,
                OnItemClickListener mClickListener,
                OnItemLongClickListener mLongClickListener) {
            super(itemView);
            this.mClickListener = mClickListener;
            this.mLongClickListener = mLongClickListener;
            itemName = (TextView) itemView.findViewById(R.id.textView);
            itemName.setOnClickListener(this);
            itemName.setOnLongClickListener(this);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            linearLayout.setOnClickListener(this);
            linearLayout.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClick(v, position);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClick(v, position);
                return true;
            }
            return false;
        }
    }
}
