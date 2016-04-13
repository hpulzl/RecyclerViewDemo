package lzl.com.recyclerviewdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import lzl.com.recyclerviewdemo.R;

/**
 * Created by admin on 2016/4/11.
 */
public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> list;
    private Context cxt;
    private final int TYPE_item = 1;
    private final int TYPE_FOOT = 2;
    private View footView;
    private int realListSize;

    public MainAdapter(Context context,List<String> list){
        this.cxt = context;
        this.list = list;
    }
    public void setFootView(View foot){
        this.footView = foot;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_FOOT && footView !=null){
            //当viewType是TYPE_FOOT并且footView不为空的时候，就可以显示FootView了。
            return new FootViewHolder(footView);
        }
        View view = LayoutInflater.from(cxt).inflate(R.layout.recyclerview_item,parent,false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof DataViewHolder){
            //设置动态信息
            ((DataViewHolder) holder).infoTv.setText(list.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        //如果是最后一条数据，就返回FootView类型
        if(position+1 == getItemCount()){
            return TYPE_FOOT;
        }
        return TYPE_item;
    }

    @Override
    public int getItemCount() {
        if(list==null){
            return 0;
        }
        //如果footView为空的话，就是获取list.size()个数据。
        realListSize = (footView == null ? list.size() : list.size()+1);
        return realListSize;
    }
    class DataViewHolder extends RecyclerView.ViewHolder {
        TextView infoTv;
        public DataViewHolder(View itemView) {
            super(itemView);
            infoTv = (TextView) itemView.findViewById(R.id.info_tv);
        }
    }
    class FootViewHolder extends RecyclerView.ViewHolder {
        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }
}
