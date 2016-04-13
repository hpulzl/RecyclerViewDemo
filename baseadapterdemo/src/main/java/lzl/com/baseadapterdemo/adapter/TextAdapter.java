package lzl.com.baseadapterdemo.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import lzl.com.baseadapterdemo.R;
import lzl.com.baseadapterdemo.adapter.base.BaseRecyclerAdapter;
import lzl.com.baseadapterdemo.adapter.base.RecyclerViewHolder;

/**
 * Created by admin on 2016/4/12.
 * 对具体的RecyclerView进行数据绑定
 */
public class TextAdapter extends BaseRecyclerAdapter<String> {

    public TextAdapter(RecyclerView view, List<String> list) {
        super(view, list, R.layout.recyclerview_item);
    }

    @Override
    public void bindConvert(RecyclerViewHolder holder, int position, String item, boolean isScrolling) {
        if(isScrolling) {
            //正在滑动
            return;
        }
        //不滑动的时候去加载数据
        holder.setText(R.id.info_tv, "第" + position + "条数据");
    }
}
