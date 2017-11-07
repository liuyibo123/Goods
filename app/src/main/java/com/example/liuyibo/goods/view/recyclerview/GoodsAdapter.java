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

import com.example.liuyibo.goods.MyApplication;
import com.example.liuyibo.goods.R;
import com.example.liuyibo.goods.entity.Goods;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/10/26.
 */

public class GoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private List<Goods> goodsList;
    private static int viewHolderCount;
    private Goods current;
    private PopupMenuItemClickListener popupMenuItemClickListener;
    public interface PopupMenuItemClickListener{
        public void click(int itemId,Goods current);
    }
    public void setOnPopupmenuItemClickListener(PopupMenuItemClickListener listener){
            this.popupMenuItemClickListener=listener;
    }
    public GoodsAdapter(List<Goods> goodsList) {
        this.goodsList = goodsList;
        viewHolderCount = 0;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_item, parent, false);
        viewHolderCount++;
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //TODO 网址转换成图片
        MyViewHolder holder1 = ((MyViewHolder) holder);
        Goods goods = goodsList.get(position);
        current = goods;
        holder1.tvName.append(goods.getName());
        holder1.tvPrice.append(goods.getPrice());
        holder1.tvDw.append(goods.getDw());
        holder1.tvCategory.append(goods.getCategory());
        holder1.tvBz.append(goods.getBz());
        holder1.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.tv_name)
        TextView tvName;
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
            view.setOnClickListener(this);
            Log.d("MyViewHodler", "MyViewHolder: "+"after setonClickListener");

        }
        @Override
        public void onClick(View view) {
            Log.d("MyViewHodler", "MyViewHolder: "+"onclick");
            PopupMenu popupmenu = new PopupMenu(MyApplication.getContext(),view);
            Menu menu = popupmenu.getMenu();
            menu.add(Menu.NONE, Menu.FIRST + 0, 0, "更改");
            menu.add(Menu.NONE, Menu.FIRST + 1, 1, "删除");
            popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    popupMenuItemClickListener.click(menuItem.getItemId(),current);
                    return false;
                }
            });
            popupmenu.show();
        }
    }
}
