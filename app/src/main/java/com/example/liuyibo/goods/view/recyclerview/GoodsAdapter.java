package com.example.liuyibo.goods.view.recyclerview;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liuyibo.goods.Config;
import com.example.liuyibo.goods.MyApplication;
import com.example.liuyibo.goods.R;
import com.example.liuyibo.goods.entity.Goods;
import com.example.liuyibo.goods.utils.network.ConConfig;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/10/26.
 */

public class GoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private final String TAG = "GoodsAdapter";
    private List<Goods> goodsList;
    private static int viewHolderCount;
    private int skip=1;
    private Goods current;
    private PopupMenuItemClickListener popupMenuItemClickListener;
    private OnScrollEndListener scrollEndListener;

    public void setScrollEndListener(OnScrollEndListener scrollEndListener) {
        this.scrollEndListener = scrollEndListener;
    }

    public interface OnScrollEndListener{
        void onscrollend(List<Goods> goodsList,int skip);
    }
    public interface PopupMenuItemClickListener{
        public void click(int itemId,Goods current);
    }
    public void setOnPopupmenuItemClickListener(PopupMenuItemClickListener listener){
            this.popupMenuItemClickListener=listener;
    }
    public GoodsAdapter(List<Goods> goodsList) {
        this.goodsList = goodsList;
        this.skip=1;
        viewHolderCount = 0;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_item, parent, false);
        viewHolderCount++;
        Log.d(TAG, "onCreateViewHolder: new myviewholder");
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //TODO 网址转换成图片
        MyViewHolder holder1 = ((MyViewHolder) holder);
        Goods goods = goodsList.get(position);
        Log.d(TAG, "onBindViewHolder: "+goods.getName()+"   "+position);

        holder1.tvName.setText("名称："+(goods.getName()!=null?goods.getName():""));
        holder1.tvPrice.setText("单价："+(goods.getPrice()!=null?goods.getPrice():""));
        holder1.tvDw.setText("单位："+(goods.getDw()!=null?goods.getDw():""));
        holder1.tvDw.setText("单位："+(goods.getDw()!=null?goods.getDw():""));
        holder1.tvCategory.setText("分类："+(goods.getCategory()!=null?goods.getCategory():""));
        holder1.tvBz.setText("备注："+(goods.getBz()!=null?goods.getBz():""));
        holder1.tvID.setText("编号："+(goods.getIdnumber()!=null?goods.getIdnumber():""));
        holder1.itemView.setTag(position);
        if(position==getItemCount()-1){
            scrollEndListener.onscrollend(goodsList,skip);
            skip++;
        }
    }

    @Override
    public int getItemCount() {
        if(goodsList==null){
            return 0;
        }
        return goodsList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.text_id)
        TextView tvID;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_dw)
        TextView tvDw;
        @BindView(R.id.tv_category)
        TextView tvCategory;
        @BindView(R.id.tv_bz)
        TextView tvBz;
        @BindView(R.id.list_item)
        ConstraintLayout listItem;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            Log.d("MyViewHodler", "MyViewHolder: "+"before setonClickListener");
            view.setOnLongClickListener(this);
            Log.d("MyViewHodler", "MyViewHolder: "+"after setonClickListener");

        }

        @Override
        public boolean onLongClick(View view) {
            Log.d("MyViewHodler", "MyViewHolder: "+"onclick");
            if(Config.getAdminFlag()==Config.isAdmin){
                PopupMenu popupmenu = new PopupMenu(MyApplication.getContext(),view);
                int position = (int) view.getTag();
                final Goods current = goodsList.get(position);
                Menu menu = popupmenu.getMenu();
                menu.add(Menu.NONE, Menu.FIRST + 0, 0, "更改");
                menu.add(Menu.NONE, Menu.FIRST + 1, 1, "删除");
                popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(popupMenuItemClickListener!=null){
                            popupMenuItemClickListener.click(menuItem.getItemId(),current);
                        }
                        return false;
                    }
                });
                popupmenu.show();
            }
            return false;
        }
    }
}
