package lzl.com.baseadapterdemo.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by admin on 2016/4/12.
 * 这个是RecyclerView的通用基类，
 * 主要包括设置HeaderView，FooterView。
 * 向下滑动时候，不进行加载数据。
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder>{
    private static final String TAG = "BaseRecyclerAdapter--->";
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOT = 2;
    private boolean isScrolling = false;  //false表示滑动，true表示不滑动
    private View headerView,footerView;  //头和尾的view
    private List<T> lists;   //传入的集合。
    private int totalList;
    private int itemLayoutId; //设置Layout的id。
    private Context cxt;
    private OnItemViewClickListener onItemViewClickListener;
    public interface OnItemViewClickListener{
        //设置item中某个view的点击事件
        void onItemViewClick(RecyclerViewHolder view,int position);
    }
    public void setonItemViewClickListener(OnItemViewClickListener listener){
        this.onItemViewClickListener = listener;
    }
    public BaseRecyclerAdapter(RecyclerView view,List<T> list,int itemLayoutId){
        this.lists = list;
        this.itemLayoutId = itemLayoutId;
        this.cxt = view.getContext();

        view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //设置如果Recycler停止滑动，去绑定更新数据。
               /* if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    //表示停止滑动
                    isScrolling = true;
                }else{
                    //正在滑动
                    isScrolling = false;
                }*/
                //将上面代码改写成这样。
                isScrolling = (newState == RecyclerView.SCROLL_STATE_IDLE);
                if(isScrolling){ //为true时候表示没有滑动操作，去更新数据
                    notifyDataSetChanged();
                }
                Log.i(TAG,"滑动了吗？"+isScrolling);
                super.onScrollStateChanged(recyclerView,newState);
            }
        });
    }
    public void setFooterView(View view){
        this.footerView = view;
    }
    public void setHeaderView(View view){
        this.headerView = view;
    }

    /**
     * 创建RecyclerView里面的itemView。
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEAD && headerView !=null){
            return new RecyclerViewHolder(headerView);
        }
        if(viewType == TYPE_FOOT && footerView!=null){
            return new RecyclerViewHolder(footerView);
        }
        View view = LayoutInflater.from(cxt).inflate(itemLayoutId,parent,false);
        return new RecyclerViewHolder(view);
    }

    /**
     *  绑定每一个itemView的具体数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        //抽象方法，让子类去具体的实现
        bindConvert(holder,position,lists.get(position),isScrolling);
        if(onItemViewClickListener!=null){
           onItemViewClickListener.onItemViewClick(holder,position);
        }
    }

    public abstract void bindConvert(RecyclerViewHolder holder, int position, T item, boolean isScrolling);

    /**
     * 判断有多少个list.size()
     * @return
     */
    @Override
    public int getItemCount() {
        if(lists == null){
            return 0;
        }
        if(headerView!=null && footerView!=null){
            //头尾都不为空
            totalList =  lists.size()+2;
        }else if(headerView ==null && footerView ==null){
            //头尾都为空
            totalList = lists.size();
        }else{
            //头尾有一个不为空
            totalList = lists.size()+1;
        }
        return totalList;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0 && headerView !=null){
            //表示第一个item，并且headerView不为空
            return TYPE_HEAD;
        }
        if(position+1 == getItemCount() && footerView !=null){
            //表示最好一个item，并且footerView不为空
            return TYPE_FOOT;
        }
        return TYPE_ITEM;
    }
}
